package velogen;

import java.awt.Color;
import java.awt.Graphics2D;

public class Velo {

	public Couleur couleurVelo;
	public Roue roueArriere;
	public Roue roueAvant;
	
	public Velo(){
		this.couleurVelo = Couleur.ROUGE;
		this.roueArriere = null;
		this.roueAvant = null;
	}
	
	public void drawVelo(Graphics2D g){
		if(this.couleurVelo == Couleur.NOIR){
			g.setColor(Color.BLACK);
		}else if(this.couleurVelo == Couleur.BLEU){
			g.setColor(Color.BLUE);
		}else if(this.couleurVelo == Couleur.ROUGE){
			g.setColor(Color.RED);
		}
		g.drawLine(30,70,50,30);
		g.drawLine(50, 30, 130, 30);
		g.drawLine(130, 30, 150, 70);
		g.drawLine(130, 30, 130, 15);
	}
	
	public void drawroueArriere(Graphics2D g){
		Couleur c = this.roueArriere.couleurRoue;
		g.setColor(Velo.couleurToColor(c));
		Taille t = this.roueArriere.tailleRoue;
		if(t == Taille.MOYEN){
			g.fillOval(20, 60, 20, 20);
		}else if(t == Taille.GRAND){
			g.fillOval(15, 55, 30, 30);
		}else if(t == Taille.PETIT){
			g.fillOval(25, 65, 10, 10);
		}
	}
	
	public void drawroueAvant(Graphics2D g){
		Couleur c = this.roueAvant.couleurRoue;
		g.setColor(Velo.couleurToColor(c));
		Taille t = this.roueAvant.tailleRoue;
		if(t == Taille.MOYEN){
			g.fillOval(140, 60, 20, 20);
		}else if(t == Taille.GRAND){
			g.fillOval(135, 55, 30, 30);
		}else if(t == Taille.PETIT){
			g.fillOval(145, 65, 10, 10);
		}
	}
	
	public static Color couleurToColor(Couleur c){
		Color res;
		switch(c){
			case BLEU : res = Color.BLUE; break;
			case NOIR : res = Color.BLACK; break;
			case ROUGE : res = Color.RED; break;
			default : res = Color.BLACK; break;
		}
		return res;
	}
	
}
