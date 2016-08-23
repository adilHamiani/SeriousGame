package velogen;

import java.awt.*;
import java.lang.reflect.*;

/**
 * 
 * diagramme dans lequel chaque noeud contiendra le nom des variables de l'environnement
 * ainsi que les valeurs de leurs attributs
 *
 */
public class DrawDiagram extends Component{

	/**
	 * graphe sur lequel les noeuds representant toutes les instances seront affichees
	 */
	public static Graphics2D graph2d;
	/**
	 * nombre de noeuds affiches par ligne
	 */
	public static int nbColumns = 2;
	/**
	 * longueur d'un noeud representant une variable de l'environnement
	 */
	public static int sizeRectX = 220;
	/**
	 * largeur d'un noeud representant 
	 */
	public static int sizeRectY = 100;
	
	public DrawDiagram(){
		super();
	}
	
	/**
	 * le diagramme est trace en appelant paint ou repaint sur une instance de DrawDiagram
	 */
	@Override
	public void paint(Graphics g) {
		graph2d = (Graphics2D) g;
		graph2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawDiag();
	}
	
	/**
	 * affiche un noeud pour chaque variable de l'environnement qui contiendra son nom et la valeur
	 * de tous ses attributs
	 */
	private void drawDiag(){
		int nbInstances = GUI.instances.size();
		for(int i=0; i<nbInstances; i++){
			Variable var_i = GUI.instances.get(i);
			int row = i/nbColumns;
			int column = i % nbColumns;
			graph2d.setColor(Color.BLACK);
			int xdep = column*sizeRectX+10;
			int ydep = row*sizeRectY+10;
			graph2d.drawRect(xdep, ydep, sizeRectX-10, sizeRectY-10);
			String nomSimpleClasse = var_i.valeur.getClass().getSimpleName();
			graph2d.drawString(nomSimpleClasse+" "+var_i.nom, xdep+5, ydep+13);
			Field[] attributs = GUI.getAllFields(var_i.valeur.getClass());
			for(int j=0; j<attributs.length; j++){
				Field attributJ = attributs[j];
				String nomTypeAtt = attributJ.getType().getSimpleName();
				String nomAtt = attributJ.getName();
				try{
					Object valeurAttribut = attributJ.get(var_i.valeur);
					if(attributJ.getType().isEnum()){
						if(valeurAttribut!=null){
							String valAtt = valeurAttribut.toString();
							graph2d.drawString(nomTypeAtt+" "+nomAtt+" = "+valAtt, xdep+5, ydep+(j+3)*13);
						}else{
							graph2d.drawString(nomTypeAtt+" "+nomAtt+" = null", xdep+5, ydep+(j+3)*13);
						}
					}else{
						/* recherche de la variable contenant la valeur d'un attribut non enum */
						Variable varContenantAttribut = null;
						for(int k=0; k<GUI.instances.size(); k++){
							if(GUI.instances.get(k).valeur == valeurAttribut){
								varContenantAttribut = GUI.instances.get(k);
							}
						}
						if(varContenantAttribut!=null){
							graph2d.drawString(nomTypeAtt+" "+nomAtt+" = "+varContenantAttribut.nom, xdep+5, ydep+(j+3)*13);
						}else{
							graph2d.drawString(nomTypeAtt+" "+nomAtt+" = null", xdep+5, ydep+(j+3)*13);
						}
					}
				}catch(Exception e){
					System.out.println("Acces attribut ENUM "+var_i.nom+"."+attributJ.getName()+" impossible");
				}
			}
		}
	}

	
	@Override
	public Dimension getPreferredSize() {	
		int nbLines = (GUI.instances.size()/nbColumns)+1;
		return new Dimension(nbColumns*sizeRectX+20, nbLines*sizeRectY+20);
	}
	
}
