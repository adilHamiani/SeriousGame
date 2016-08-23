package velogen;

import java.awt.*;
import java.lang.reflect.*;

/**
 * graphe sur lequel le dessin representant une instance de la classe racine de GUI sera representee.
 * La representation de chaque instance dependra des valeurs de ses attributs.
 */
public class DrawInstance extends Component{

	private static final long serialVersionUID = 1L;
	
	/**
	 * graphe sur lequel le dessin representant une instance sera representee
	 */
	public Graphics2D inst;
	
	/**
	 * variable qui sera representee sur un dessin
	 */
	public Variable varInitiale;
	
	public DrawInstance(){
		super();
	}
	
	public DrawInstance(Variable varInitiale){
		super();
		this.varInitiale = varInitiale;
	}
	
	@Override
	public void paint(Graphics g) {
		inst= (Graphics2D) g;
		inst.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		inst.drawString(varInitiale.nom, 5, 15);
		drawInst(this.varInitiale);
	}
	
	/**
	 * affiche un dessin representant la variable var sur l'attribut inst de DrawInstance
	 * @param var
	 */
	public void drawInst(Variable var){
		Variable var_i = var; 
		Method[] meths = var_i.valeur.getClass().getDeclaredMethods();
		Field[] fields = GUI.getAllFields(var_i.valeur.getClass());
		if(var_i.valeur.getClass().getName().equals(GUI.racine.getName())){
			/* il s'agit d'une variable du type initial. Une methode dessinera donc
			 * le dessin principal sur lequel se grefferont les dessins de chaque attribut */
			String simpleClassName = var_i.valeur.getClass().getSimpleName();
			String methodName = "draw"+simpleClassName;
			Method drawMain = DrawInstance.getMethodByName(methodName, var_i.valeur.getClass());
			if(drawMain!=null){
				try{
					drawMain.invoke(var_i.valeur, inst);
				}catch(Exception e){
					System.out.println("Echec appel methode "+drawMain.toString());
				}
			}
		}
		/* verifier si le type de chaque attribut possede au moins un attribut enum,
		 * si oui, verifier que la valeur de l'attribut est non null et l'afficher et
		 * faire un appel recursif. Sinon, ne pas afficher l'attribut. */
		for(int j=0; j<fields.length; j++){
			Class typeFieldJ = fields[j].getType();
			if(!typeFieldJ.isEnum()){
				if(DrawInstance.hasEnumFields(typeFieldJ) && !DrawInstance.estPrimitif(typeFieldJ)){
					/* comme l'attribut possede lui-meme des attributs de type enum,
					 * il est possible de l'afficher en appelant la methode ayant pour nom
					 * le String "draw"+ nom de l'attribut en question, il s'agit d'une convention */
					
					/*System.out.println("Il faut afficher l'attribut "+fields[j].getName()
							+" de la variable "+var_i.nom);*/
					
					Method meth = DrawInstance.getMethodByName("draw"+fields[j].getName(), var_i.valeur.getClass());
					if(meth!=null){
						try{
							meth.invoke(var_i.valeur, inst);
						}catch(Exception e){
							System.out.println("Impossible d'appeler la methode d'affichage");
						}
					}
					try{
						/* recherche de la variable qui a la meme valeur que l'attribut J de la variable initiale */
						Object valeurFieldJ = fields[j].get(var_i.valeur);
						Variable contientFieldJ = null;
						for(int k=0; k<GUI.instances.size(); k++){
							if(GUI.instances.get(k).valeur == valeurFieldJ){
								contientFieldJ = GUI.instances.get(k);
							}
						}
						if(contientFieldJ!=null){
							/* appel recursif pour afficher l'attribut */
							this.drawInst(contientFieldJ);
						}
					}catch(Exception e){
						System.out.println("Acces a "+var_i.nom+"."+fields[j].getName()+" impossible");
					}
				}else{
					/*System.out.println("Il ne faut pas afficher l'attribut "+fields[j].getName()
						+" de la variable "+var_i.nom);*/
				}
			}
		}
	}
	
	/**
	 * recupere une methode d'une classe a partir de son nom methodName
	 * @param methodName
	 * @param type
	 * @return
	 */
	public static Method getMethodByName(String methodName, Class type){
		Method[] meths = type.getDeclaredMethods();
		Method meth = null;
		for(int i=0; i<meths.length; i++){
			if(meths[i].getName().equals(methodName)){
				meth = meths[i];
			}
		}
		return meth;
	}
	
	/**
	 * renvoie true si et seulement si la classe possede au moins un attribut de type Enum
	 * @param type
	 * @return
	 */
	public static boolean hasEnumFields(Class type){
		boolean res = false;
		if(!type.isEnum()){
			Field[] attributs = GUI.getAllFields(type);
			for(int i=0; i<attributs.length; i++){
				if(attributs[i].getType().isEnum()){
					res = true;
				}
			}
		}
		return res;
	}
	
	/**
	 * renvoie true si et seulement si la classe est primitive ou est de type String
	 * @param type
	 * @return
	 */
	public static boolean estPrimitif(Class type){
		String nomType = type.getSimpleName();
		return (type.isPrimitive() || nomType.equals("String"));
	}
	
	@Override
	public Dimension getPreferredSize() {	
		return new Dimension(180,100);
	}
	
	public static void main(String[] args){
		/*
		TypeA var = TypeA.AVAL1;
		Class typea = var.getClass();
		System.out.println(typea.getName()+" "+typea.isEnum());
		Field[] fields = GUI.getAllFields(typea);
		for(int i=0; i<fields.length; i++){
			Class typeFieldI = fields[i].getType();
			System.out.println(typeFieldI.getName()+" "+fields[i].getName()
				+" "+DrawInstance.hasEnumFields(typeFieldI));
		}
		*/
	}

}
