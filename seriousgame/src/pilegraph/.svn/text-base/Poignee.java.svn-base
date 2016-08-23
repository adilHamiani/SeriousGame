package pilegraph;

/**
 * 
 * Poignee de l'environnement pointant vers une variable de l'environnement.
 * Lors de sa creation, la poignee aura pour type declare le type de la premiere variable
 * a laquelle elle sera affectee. Cependant, la poignee pourra ulterieurement etre affectee
 * a une variable n'ayant pas le meme type que le type declare de la poignee mais un type compatible,
 * ie un type heritant du type declare initialement.
 *
 */
public class Poignee {

	/**
	 * variable vers laquelle la poignee pointe 
	 */
	public Variable pointeVers;
	/** 
	 * nom de la poignee dans l'environnement 
	 */
	public String nom;
	/**
	 * type declare de la poignee lors de sa creation 
	 */
	public Class typepgn;
	/** 
	 * entier indiquant le numero de la case memoire de la poignee 
	 */
	public int caseMemoire;
	/** 
	 * identifiant du Vertex JGraphX representant la poignee dans le graphe affiche par DrawEnvironnementBis 
	 */
	public String idPoignee;
	
	public Poignee(String nom, int caseMemoire){
		this.pointeVers = null;
		this.nom = nom;
		this.caseMemoire = caseMemoire;
		
	}
	
	public Poignee(Variable pointevers, String nom, int caseMemoire){
		this.pointeVers = pointevers;
		this.nom = nom;
		this.typepgn = pointevers.type;
		this.caseMemoire = caseMemoire;
	}
	
	/** 
	 * renvoie une copie d'une poignee de l'environnement. La copie de sera
	 * pas affectee par les modifications faites sur la poignee originelle 
	 */
	public Poignee clonePoignee(){
		Poignee res;
		if(this.pointeVers !=null){
			res = new Poignee(this.pointeVers.cloneVariable(), this.nom, this.caseMemoire);
		}else{
			res = new Poignee(this.nom, this.caseMemoire); 
		}
		return res;
	}
	
}
