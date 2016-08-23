package velogen;

public class Pneu {

	public Epaisseur thickness;
	public Couleur couleurPneu;
	
	public Pneu(){
		this.thickness = Epaisseur.MOYEN;
		this.couleurPneu = Couleur.BLEU;
	}
	
	public enum Epaisseur{
		FIN, MOYEN, EPAIS
	}
	
}
