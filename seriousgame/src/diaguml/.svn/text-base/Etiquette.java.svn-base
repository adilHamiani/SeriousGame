package diaguml;

import java.util.ArrayList;


public class Etiquette {

	private String id;
	private int rang;
	private String nom;
	private ArrayList<String> attributs;

	public Etiquette(String id, int rang, String nom,ArrayList<String> attributs){
		this.id = id;
		this.rang = rang;
		this.nom = nom;
		this.attributs = attributs;
	}

	public Etiquette(String nom){
		this.nom = nom;
	}

	public Etiquette(String id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public String getId(){
		return this.id;
	}

	public int getRang(){
		return this.rang;
	}

	public String getNom(){
		return this.nom;
	}

	public ArrayList<String> getAttributs(){
		return this.attributs;
	}

	public void setId(String num){
		this.id = num;
	}

	public void setRang(int rg){
		this.rang = rg;
	}

	public void setNom(String nom){
		this.nom = nom;
	}

	public void setAttributs(ArrayList<String> attr){
		this.attributs = attr;
	}



	@Override
	public boolean equals(Object e) {
		if (e instanceof Etiquette) {
			return this.nom.equals(((Etiquette) e).getNom());
		}
		else return false;
	}

}
