package pilegraph;

import java.util.ArrayList;

/**
 * 
 * variable de l'environnement contenant une instance d'une classe Java
 *
 */
public class Variable {
	
	/** 
	 * type declare de la variable lors de sa creation 
	 */
	public Class type;
	
	/** 
	 * nom de la variable dans l'environnement 
	 */
	public String name;
	
	/** 
	 * valeur de l'instance Java cree. Comme l'application est completement generique,
	 * toutes les valeurs seront de type Object 
	 */
	public Object valeur;
	
	/** 
	 * liste des poignees pointant vers la variable 
	 */
	public ArrayList<Poignee> poignees;
	
	/**
	 * Une variable mere peut avoir plusieurs attributs de meme type et de meme valeur, 
	 * ie pointant vers la meme variable fille. Pour les differencier il faut donc stocker
	 * dans la variable fille chaque variable mere mais aussi le nom de l'attribut de la variable mere.
	 * A tout moment, variablesMeres et attributsPeres seront de meme taille 
	 */
	public ArrayList<Variable> variablesMeres;
	
	/** 
	 * nom de l'attribut pere ayant pour valeur la variable 
	 */
	public ArrayList<String> attributsPeres;
	
	/** 
	 * identifiants des noeuds representant les attributs peres dans le graphe affiche par DrawEnvironnementBis 
	 */
	public ArrayList<String> identifiantsPeres;
	
	/** 
	 * identifiant du noeud representant la variable dans le graphe affiche par DrawEnvironnementBis 
	 */
	public String identifiant;
	
	public Variable(Class type, String name, Object valeur){
		this.type = type;
		this.name = name;
		this.valeur = valeur;
		this.poignees = new ArrayList<Poignee>();
		this.variablesMeres = new ArrayList<Variable>();
		this.attributsPeres = new ArrayList<String>();
	}
	
	public Variable(String name, Object val){
		this.type = val.getClass();
		this.name = name;
		this.valeur = val;
		this.poignees = new ArrayList<Poignee>();
		this.variablesMeres = new ArrayList<Variable>();
		this.attributsPeres = new ArrayList<String>();
	}
	
	public Variable(Class type, String name){
		this.type = type;
		this.name = name;
		this.valeur = null;
		this.poignees = new ArrayList<Poignee>();
		this.variablesMeres = new ArrayList<Variable>();
		this.attributsPeres = new ArrayList<String>();
	}
	
	public Variable(Class type, String name, Object valeur,
			ArrayList<Poignee> poignees, ArrayList<Variable> variablesMeres,
			ArrayList<String> attributsPeres) {
		super();
		this.type = type;
		this.name = name;
		this.valeur = valeur;
		this.poignees = poignees;
		this.variablesMeres = variablesMeres;
		this.attributsPeres = attributsPeres;
	}

	
	
	public Variable(Class type, String name, Object valeur,
			ArrayList<Poignee> poignees, ArrayList<Variable> variablesMeres,
			ArrayList<String> attributsPeres,
			ArrayList<String> identifiantsPeres, String identifiant) {
		super();
		this.type = type;
		this.name = name;
		this.valeur = valeur;
		this.poignees = poignees;
		this.variablesMeres = variablesMeres;
		this.attributsPeres = attributsPeres;
		this.identifiantsPeres = identifiantsPeres;
		this.identifiant = identifiant;
	}

	/** 
	 * retire la poignee ayant pour nom namepgn 
	 * de la liste de poignee pointant vers la variable 
	 */
	public void retirePoignee(String namepgn){
		int k = 0;
		while((k<poignees.size())){
			if(poignees.get(k).nom.equals(namepgn)){
				poignees.remove(k);
			}else{
				k++;
			}
		}
	}
	
	/** 
	 * Copie une variable ainsi que sa valeur. La copie d'une variable
	 * ne sera donc pas affectee par les methodes appelees sur la variable originelle
	 * ni par les modifications d'attributs de la variable originelle  
	 */
	public Variable cloneVariable(){
		if(this.type.getName().equals("int")||this.type.getName().equals("java.lang.Integer")){
			Integer oldVal = new Integer((Integer)this.valeur);
			/* System.out.println("Valeur clonee de "+this.name+" est "+oldVal); */
			return new Variable(this.type, this.name, oldVal, (ArrayList<Poignee>) this.poignees.clone(),
					(ArrayList<Variable>) this.variablesMeres.clone(), (ArrayList<String>) this.attributsPeres.clone(),
					(ArrayList<String>) this.identifiantsPeres.clone(), this.identifiant);
		}else{
			return new Variable(this.type, this.name, this.valeur, (ArrayList<Poignee>) this.poignees.clone(),
				(ArrayList<Variable>) this.variablesMeres.clone(), (ArrayList<String>) this.attributsPeres.clone(),
				(ArrayList<String>) this.identifiantsPeres.clone(), this.identifiant);
		}
	}
	
	/** 
	 * renvoie true si et seulement si au moins l'un des attributs
	 * de la variable a pour valeur varAtt 
	 */
	public boolean possedeAttributDans(Variable varAtt){
		boolean hasField = false;
		for(int i=0; i<varAtt.variablesMeres.size(); i++){
			if(this.name.equals(varAtt.variablesMeres.get(i).name)){
				hasField = true;
			}
		}
		return hasField;
	}
	
}