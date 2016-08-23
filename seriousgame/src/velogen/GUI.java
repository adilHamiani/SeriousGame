package velogen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * 
 * classe contenant l'interface de l'application Instanciation et Modification d'attributs
 *
 */
public class GUI {

	/**
	 * fenetre qui apparaitra lorsque l'application sera executee
	 */
	public static JFrame menu = new JFrame();
	
	/**
	 * classe principale qui sera d'abord instanciee avant de creer tous ses attributs
	 * ayant une valeur null
	 */
	public static Class racine = null;
	
	/**
	 * les variables creees dans l'environnement seront stockees dans l'attribut instances
	 */
	public static ArrayList<Variable> instances = new ArrayList<Variable>();
	
	/**
	 * lorsque la classe principale racine ou l'attribut d'une variable ayant une valeur null
	 * a ete selectionne dans la JComboBox chooseCreate, cette variable ou attribut 
	 * sera cree en appuyant sur le JButton create
	 */
	public static JButton create;
	
	/**
	 * lorsqu'une variable a ete selectionnee dans la JComboBox chooseModify, les attributs de
	 * type Enum seront modifies en appuyant sur le JButton modify
	 */
	public static JButton modify;
	
	/**
	 * nombre maximal d'instances de type racine affichees possible. Ce nombre sera egal a nbColonnes*nbLignes 
	 */
	public static int nbInstancesMax;
	
	/** 
	 * nombre d'instances qui seront affichees sur une meme ligne 
	 */
	public static int nbColonnes;
	
	/** 
	 * nombre d'instances qui seront affichees sur une meme colonne 
	 */
	public static int nbLignes;
	
	/* A tout moment, chooseCreate, chooseField et chooseVar sont de meme taille. */
	/**
	 * Lorsque nbInstancesMax > 0 , chooseCreate contiendra le nom simple de la classe racine.
	 * De plus, chooseCreate contiendra les noms des attributs des variables de l'environnement ayant
	 * la valeur null
	 */
	public static JComboBox<String> chooseCreate = new JComboBox<String>();
	
	/**
	 * Soit i l'indice de l'item selectionnee dans la JComboBox chooseCreate.
	 * lorsque l'attribut d'une variable ayant une valeur null est selectionne dans chooseCreate,
	 * la variable ayant cet attribut sera chooseField.get(i)
	 */
	public static ArrayList<Field> chooseField = new ArrayList<Field>();
	
	/**
	 * Soit i l'indice de l'item selectionnee dans la JComboBox chooseCreate.
	 * lorsque l'attribut d'une variable ayant une valeur null est selectionne dans chooseCreate,
	 * l'instance de la classe Field correspondant a cet attribut sera chooseVar.get(i)
	 */
	public static ArrayList<Variable> chooseVar = new ArrayList<Variable>();
	
	/**
	 * les noms des instances creees possedant des attributs de type enum seront stockees
	 * dans la JComboBox chooseModify
	 */
	public static JComboBox<String> chooseModify = new JComboBox<String>();
	
	/**
	 * lorsque l'utilisateur cliquera sur le JButton modify, une instance de Modification sera creee
	 * et stockee dans l'attribut modifyWindow afin que la fenetre permettant a l'utilisateur
	 * de selectionner la nouvelle valeur des attributs de type enum de la variable selectionnee
	 * dans chooseModify apparaisse
	 */
	public static Modification modifyWindow;
	
	/**
	 * les dessins representant chaque instance de type racine seront stockes dans l'attribut dessin,
	 * chacun etant une instance de DrawInstance
	 */
	public static ArrayList<DrawInstance> dessin;
	
	/**
	 * l'ensemble des dessins stockes dans l'attribut dessin seront affiches sur le JPanel ensembleDessins
	 * afin que l'utilisateur puisse les voir simultanement. Ils seront affiches sur le cote gauche
	 * de la JFrame stockee dans l'attribut menu
	 */
	public static JPanel ensembleDessins;
	
	/**
	 * chaque instance creee sera representee dans un diagramme avec les valeurs de ses attributs.
	 * Ce diagramme sera ensuite affiche sur le cote droit de la JFrame stockee dans l'attribut menu.
	 */
	public static DrawDiagram diagramme;
	
	public GUI(Class classeInitiale){
		menu.setTitle("Instanciation et Modification d'attributs");
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setSize(1000,1000);
		Container content = menu.getContentPane();
	    content.setBackground(Color.WHITE);
	    content.setLayout(new BorderLayout());
	    racine = classeInitiale;
		create = new JButton("Creer");
		modify = new JButton("Modifier");
		modify.setEnabled(false);
		nbColonnes = 2;
		nbLignes = 2;
		nbInstancesMax = nbColonnes * nbLignes;
		chooseCreate.addItem(classeInitiale.getSimpleName());
		chooseField.add(null);
		chooseVar.add(null);
		dessin = new ArrayList<DrawInstance>();
		diagramme = new DrawDiagram();
		ensembleDessins = new JPanel();
		
		ensembleDessins.setLayout(new GridLayout(nbLignes, nbColonnes));
		ensembleDessins.setBackground(Color.WHITE);
		create.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if((chooseCreate.getItemCount()!=0) && (chooseCreate.getSelectedIndex()!=(-1))){
					String choix = (String) chooseCreate.getSelectedItem();
					try{
						Class classeChoisie;
						int indiceChoisi = GUI.chooseCreate.getSelectedIndex();
						Variable varMere = chooseVar.get(indiceChoisi);
						Field attributPere = chooseField.get(indiceChoisi);
						if(attributPere!=null){
							classeChoisie = attributPere.getType();
						}else{
							/* il s'agit d'une instance de la classe racine qui n'a donc
							 * ni variableMere ni attributPere */
							classeChoisie = racine;
						}
						boolean choseRootClass = classeChoisie.getName().equals(racine.getName());
						boolean canCreateRoot = choseRootClass && (GUI.nbInstancesMax>0);
						/* recherche du premier constructeur sans argument afin de creer
						 * une instance de la classe choisie */
						Constructor[] constructeurs = classeChoisie.getDeclaredConstructors();
						Constructor constrSansArg = null;
						for(Constructor constr : constructeurs){
							if(constr.getParameterTypes().length==0){
								constrSansArg = constr;
							}
						}
						if(constrSansArg!=null && (canCreateRoot || !choseRootClass)){
							/* creation d'une instance de la classe choisie en utilisant le constructeur
							 * sans arguments */
							Object[] rien = new Object[0];
							Object instanceClasseChoisie = constrSansArg.newInstance(rien);
							String nameNewVar;
							if(varMere!=null && attributPere!=null){
								nameNewVar = varMere.nom+"."+attributPere.getName();
							}else{
								/* il s'agit d'une instance de la classe racine */
								nameNewVar = classeChoisie.getSimpleName()+GUI.nbSameClass(classeChoisie);
								GUI.nbInstancesMax--;
							}
							// System.out.println("Nom de la nouvelle variable : "+nameNewVar);
							Variable newVar = new Variable(instanceClasseChoisie, nameNewVar);
							GUI.instances.add(newVar);
							GUI.addFieldsToInstances(newVar);
							if(choseRootClass){
								DrawInstance nouveauDessin = new DrawInstance(newVar);
								dessin.add(nouveauDessin);
								ensembleDessins.add(nouveauDessin);
							}
							Field attributVideAInstancier = GUI.chooseField.get(indiceChoisi);
							Variable varAyantAttribut = GUI.chooseVar.get(indiceChoisi);
							if(attributVideAInstancier!=null && varAyantAttribut!=null){
								attributVideAInstancier.set(varAyantAttribut.valeur, newVar.valeur);
							}
							if((choseRootClass&&(GUI.nbInstancesMax<=0)) || !choseRootClass){
								chooseField.remove(indiceChoisi);
								chooseVar.remove(indiceChoisi);
								chooseCreate.removeItemAt(indiceChoisi);
							}
						}
					}catch(Exception e){
						System.out.println("Impossible de creer la classe "+choix);
					}
				}
				if(chooseCreate.getItemCount()==0){
					create.setEnabled(false);
				}
				if(chooseModify.getItemCount()!=0){
					modify.setEnabled(true);
				}
				for(int i=0; i<dessin.size(); i++){
					GUI.dessin.get(i).repaint();
				}
				diagramme.repaint();
			}
		});
		
		modify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if((chooseModify.getItemCount()!=0)&&(chooseModify.getSelectedIndex()!=(-1))){
					String nomVarChoisi = (String)chooseModify.getSelectedItem();
					Variable varChoisie = GUI.getVariableByName(nomVarChoisi);
					create.setEnabled(false);
					modify.setEnabled(false);
					modifyWindow = new Modification(varChoisie);
				}
			}
		});
		
		JPanel choisirAction = new JPanel();
		choisirAction.setLayout(new GridLayout(1,4));
		choisirAction.add(chooseCreate);
		choisirAction.add(create);
		choisirAction.add(chooseModify);
		choisirAction.add(modify);
		content.add(choisirAction, BorderLayout.SOUTH);
		content.add(diagramme, BorderLayout.EAST);
		content.add(ensembleDessins, BorderLayout.WEST);
		menu.pack();
		menu.setVisible(true);
		
	}
	
	/**
	 * ajoute les attributs de valeur non null et de type non enum d'une instance a l'environnement.
	 * La fonction est ensuite appelee recursivement a chaque attribut qui a ete ajoute a l'environnement.
	 * @param newvar
	 */
	public static void addFieldsToInstances(Variable newvar){
		Class classeChoisie = newvar.valeur.getClass();
		Object instanceClasseChoisie = newvar.valeur;
		String nameNewVar = newvar.nom;
		Field[] attributs = GUI.getAllFields(classeChoisie);
		boolean hasEnum = false;
		for(Field f : attributs){
			if(f.getType().isEnum()){
				hasEnum = true;
			}else{
				try{
					/* si la classe a des attributs de type enum, il est possible pour l'utilisateur
					 * de modifier la classe.
					 * Pour chaque attribut de type non enum :
					 * - s'il est null, l'utilisateur devra l'instancier avec le bouton create 
					 * - s'il est non null, l'utilisateur pourra modifier ses attributs de type enum
					 *   avec le bouton modify */
					Object valeurAttribut = f.get(instanceClasseChoisie);
					String nomAttribut = nameNewVar+"."+f.getName();
					if(valeurAttribut == null){
						/* comme l'attribut est null, l'utilisateur devra l'instancier */
						chooseCreate.addItem(f.getType().getSimpleName()+" "+nomAttribut);
						chooseField.add(f);
						chooseVar.add(newvar);
					}else{
						Variable varAttribut = new Variable(valeurAttribut, nomAttribut);
						GUI.instances.add(varAttribut);
						
						/* si on appelle chooseModify ici, comme on l'appelle egalement en fin de methode,
						 * une meme variable risque d'etre ajoutee deux fois dans chooseModify, ce qui est incorrect */
						
						/* appel recursif sur les attributs de la nouvelle variable 
						 * pour les ajouter aussi aux instances de GUI. 
						 * Le cas terminal sera donc les variables ne possedant que des attributs enum */
						
						GUI.addFieldsToInstances(varAttribut);
					}
				}catch(Exception e){
					System.out.println("Impossible d'acceder a l'attribut "+nameNewVar+"."+f.getName());
				}
			}
		}
		if(hasEnum){
			chooseModify.addItem(nameNewVar);
		}
	}
	
	/**
	 * renvoie le nombre d'instances dans l'environnement ayant pour type la classe fournie en argument
	 * @param classe
	 * @return
	 */
	public static int nbSameClass(Class classe){
		int res = 0;
		for(int i=0; i<GUI.instances.size(); i++){
			Variable var_i = GUI.instances.get(i);
			Class class_i = var_i.valeur.getClass();
			if(class_i.getName().equals(classe.getName())){
				res++;
			}
		}
		return res;
	}
	
	/**
	 * renvoie la variable de l'environnement nommee nomVariable
	 * @param nomVariable
	 * @return
	 */
	public static Variable getVariableByName(String nomVariable){
		Variable res = null;
		for(Variable v : GUI.instances){
			if(v.nom.equals(nomVariable)){
				res = v;
			}
		}
		return res;
	}
	
	/**
	 * renvoie tous les attributs de la classe fournie en argument ainsi que tous les attributs
	 * herites des supertypes de cette classe
	 * @param type
	 * @return
	 */
	public static Field[] getAllFields(Class type){
		/* renvoie toutes les variables declarees dans la classe et toutes ses superclasses */
		Field[] fields = type.getDeclaredFields();
        if(type.getSuperclass() != null) {
        	Field[] superfields = getAllFields(type.getSuperclass());
        	Field[] res = new Field[fields.length+superfields.length];
        	for(int i=0; i<fields.length; i++){
        		res[i] = fields[i];
        	}
        	for(int i=0; i<superfields.length; i++){
        		res[fields.length+i] = superfields[i];
        	}
        	return res;
        }else{
        	return fields;
        }
	}
	
	public static void main(String[] args){
		Test t = new Test();
		Velo v = new Velo();
		GUI gui = new GUI(v.getClass());
		//try{
			//Field.isEnumConstant()
			//Class tailleClass = Taille.MIDDLE.getClass();
			//System.out.println(tailleClass.getName());
			//if(tailleClass.isEnum()){
				//Object[] tailleVals = tailleClass.getEnumConstants();
			/*
			Class couleur = Color.BLACK.getClass();
			Field[] valsCol = tailleClass.getDeclaredFields();
				for(Field c : valsCol){
					System.out.println(Modifier.toString(c.getModifiers())+" "+c.getName());
				}*/
			//}
		/*}catch(Exception e){
			System.out.println("Ca n'a pas marche");
		}*/
		
	}
	
}
