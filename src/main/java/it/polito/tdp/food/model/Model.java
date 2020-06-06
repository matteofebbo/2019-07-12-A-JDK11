package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi;
	private FoodDao dao;
	private Graph<Food,DefaultWeightedEdge> grafo;
	
	public Model() {
	}
	
	public List<Food> getFoods(int portions) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao=new FoodDao();
		this.cibi=dao.getFoodsByPortions(portions);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, cibi);
		
		//aggiungo archi
		for(Food f1:this.cibi) {
			for(Food f2:this.cibi) {
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
					Double peso= dao.calorieCongiunte(f1,f2);
					if(peso!=null) {
						Graphs.addEdge(this.grafo, f1, f2, peso);
					}
				}
			}
		}
		System.out.println(this.grafo);
		
		return this.cibi;
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		
		List<FoodCalories> result= new ArrayList<>();
		List<Food> vicini=Graphs.neighborListOf(this.grafo, f);
		for(Food v: vicini) {
			Double peso=this.grafo.getEdgeWeight(this.grafo.getEdge(f, v));
			result.add(new FoodCalories(v,peso));
		}
		Collections.sort(result);
		return result;
	}
}
