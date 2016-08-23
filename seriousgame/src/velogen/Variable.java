package velogen;

/**
 * 
 * variable de l'environnement contenant une instance Java et un nom
 *
 */
public class Variable {

	/**
	 * instance Java stockee dans la variable
	 */
	public Object valeur;
	/**
	 * nom de la variable dans l'environnement
	 */
	public String nom;
	
	public Variable(Object valeur, String nom){
		this.valeur = valeur;
		this.nom = nom;
	}
	
}
