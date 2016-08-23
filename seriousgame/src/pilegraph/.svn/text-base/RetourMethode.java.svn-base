package pilegraph;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * fenetre permettant a l'utilisateur d'affecter le retour d'une methode :
 * - soit a une nouvelle poignee qui sera creee dans l'environnement
 * - soit a une poignee de type compatible qui existe deja dans l'environnement
 * - soit a un attribut de type compatible d'une variable existant deja dans l'environnement 
 *
 */
public class RetourMethode {

	/**
	 * JFrame qui s'affichera lorsque l'utilisateur devra choisir comment affecter
	 * un retour non void d'une methode
	 */
	public static JFrame retourMeth = null;
	
	/**
	 * valeurRetour contient le retour non void de la methode
	 */
	public static Object valeurRetour;
	
	/**
	 * Si le retour de la methode est deja present dans l'environnement, variableRetour contiendra
	 * la variable de l'environnement dans laquelle ce retour est stocke.
	 * Si le retour de la methode n'existe pas dans l'environnement, une nouvelle variable dans laquelle
	 * sera stocke ce retour sera creee et ajoutee a l'environnement. Dans ce cas, variableRetour
	 * contiendra cette variable nouvellement creee.
	 */
	public static Variable variableRetour;
	
	/**
	 * le JButton newpoignee affecte le retour de la methode a une nouvelle poignee qui sera
	 * creee et ajoutee a l'environnement
	 */
	public static JButton newpoignee;
	
	/**
	 * nomsarguments sera une chaine de caractere specifiant les noms de chaque argument
	 * qui fut passe a la methode qui a ete appelee
	 */
	public static String nomsarguments;
	
	// attributs servant a affecter le retour de methode a un attribut d'une variable
	
	/**
	 * les variables possedant au moins un attribut de type compatible avec le retour de la
	 * methode seront stockes dans la JComboBox varscompatibles
	 */
	public static JComboBox<String> varscompatibles = new JComboBox<String>();
	
	/**
	 * les noms des attributs de la variable selectionnee dans la JComboBox varscompatibles
	 * ayant un type compatible avec le retour non void de la methode
	 * seront stockes dans la JComboBox attributscomaptibles
	 */
	public static JComboBox<String> attributscompatibles = new JComboBox<String>();
	
	/**
	 * les attributs de la variable selectionnee dans la JComboBox varscompatibles
	 * ayant un type compatible avec le retour non void de la methode
	 * seront stockes dans la JComboBox attributscomaptibles
	 */
	public static ArrayList<Field> attributscompatiblesvals = new ArrayList<Field>();
	
	/**
	 * lorsqu'une variable et son attribut de type compatible avec le retour non void de la methode
	 * ont ete selectionnes respectivement dans les JComboBox varscompatibles et attributscompatibles,
	 * le retour sera affecte a l'attribut selectionne de la variable selectionnee en cliquant
	 * sur le JButton affecteval
	 */
	public static JButton affecteval;
	
	// attributs servant a affecter le retour de methode a une poignee de l'environnement
	
	/**
	 * les noms des poignees de type compatible avec le retour non void de la methode seront
	 * stockes dans la JComboBox poigneesCompatibles 
	 */
	public static JComboBox<String> poigneesCompatibles = new JComboBox<String>();
	
	/**
	 * lorsqu'une poignee de type compatible avec le retour non void de la methode a ete selectionnee
	 * dans la JComboBox poigneesCompatibles, le retour de la methode sera affecte a la poignee
	 * en cliquant sur le JButton affectePoignee
	 */
	public static JButton affectePoignee;
	
	public RetourMethode(Object valeurRetour, String devantMethode, String nomMethode, String nomsarguments){
		GUI.deactivateButtons();
		RetourMethode.valeurRetour = valeurRetour;
		Class typeretour = valeurRetour.getClass();
		RetourMethode.nomsarguments = nomsarguments;
		RetourMethode.varscompatibles.removeAllItems();
		RetourMethode.attributscompatibles.removeAllItems();
		RetourMethode.attributscompatiblesvals.clear();
		affecteval = new JButton("Modifier");
		boolean isalreadythere = false;
		for(int i=0; i<GUI.envCourant.instances.size(); i++){
			/* il faut determiner si la valeur retournee par la fonction existe deja.
			 * Il faut egalement determiner les variables possedant des attributs auxquels
			 * l'on pourra affecter le retour de la fonction */
			Variable var_i = GUI.envCourant.instances.get(i);
			if(var_i.valeur == RetourMethode.valeurRetour){
				isalreadythere = true;
				RetourMethode.variableRetour = var_i;
			}
			Field[] allFields_i = Environnement.getAllFields(var_i.type);
			boolean iscompatible = false;
			for(int j=0; j<allFields_i.length; j++){
				/* Une variable est consideree comme compatible avec le retour de methode 
				 * si au moins l'un de ses attributs est de type compatible 
				 * avec le type de retour de methode */
				if(allFields_i[j].getType().isAssignableFrom(typeretour)){
					iscompatible = true;
				}
			}
			if(iscompatible){
				varscompatibles.addItem(var_i.name);
			}
		}
		if(varscompatibles.getItemCount()!=0){
			RetourMethode.refreshAttributsCompatibles();
			RetourMethode.affecteval.setEnabled(true);
		}else{
			RetourMethode.affecteval.setEnabled(false);
		}
		if(!isalreadythere){
			/* Le retour de la methode n'existe pas dans l'environnement.
			 * Il faut donc creer une nouvelle Variable dans l'Environnement
			 * qui contiendra ce retour dans son attribut valeur */
			String nomvar = devantMethode+"."+nomMethode;
			int suffix = 0;
			for(int i=0; i<GUI.envCourant.instances.size(); i++){
				/* Une meme methode peut etre appelee sur une meme variable a plusieurs reprises
				 * avec des retours potentiellement differents lorsque la valeur de la variable change.
				 * Afin de distinguer ces retours, la variable aura pour nom : 
				 * nom de la variable . nom de la methode suivi du nombre de fois 
				 * ou la methode a ete appelee sur la meme variable*/
				Variable instance_i = GUI.envCourant.instances.get(i);
				String typeRetour = RetourMethode.valeurRetour.getClass().getName();
				if(instance_i.name.startsWith(nomvar) && instance_i.type.getName().equals(typeRetour)){
					suffix++;
				}
			}
			nomvar = nomvar+suffix;
			RetourMethode.variableRetour = new Variable(nomvar,RetourMethode.valeurRetour);
			GUI.envCourant.instances.add(variableRetour);
			GUI.envCourant.addAttributesToInstances(variableRetour);
		}
		RetourMethode.varscompatibles.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RetourMethode.refreshAttributsCompatibles();
			}
		});
		RetourMethode.affecteval.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int nbvarscomp = RetourMethode.varscompatibles.getItemCount();
				int nbattscomp = RetourMethode.attributscompatibles.getItemCount();
				int nbattscompvals = RetourMethode.attributscompatiblesvals.size();
				if((nbvarscomp!=0)&&(nbattscomp!=0)&&(nbattscomp==nbattscompvals)){
					/* Appel de la methode changeAttribute pour changer l'attribut selectionne  
					 * de la variable choisie */
					String nomattribut = (String)attributscompatibles.getSelectedItem();
					String nomvar = (String)varscompatibles.getSelectedItem();
					Variable varChoisie = GUI.envCourant.getVariableByName(nomvar);
					GUI.envCourant.changeAttribute(nomattribut,varChoisie,variableRetour);
					GUI.paintbis = new DrawEnvironnementBis();
					String vraiNomDevant = Environnement.trueVariableName(ChoixArguments.devantmeth);
					String vraiNomVar = Environnement.trueVariableName(varChoisie);
					String newCodeLine = vraiNomVar+"."+nomattribut+" = "+vraiNomDevant+"."+ChoixArguments.meth.getName()
						+"("+RetourMethode.nomsarguments+");";
					GUI.cloneMemoryState(newCodeLine);
					RetourMethode.retourMeth.setVisible(false);
					GUI.refreshComboBoxes();
				}
			}
		});
		newpoignee = new JButton("Creer Poignee");
		newpoignee.setEnabled(true);
		RetourMethode.newpoignee.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				/* creation d'une nouvelle poignee a laquelle on affecte le retour de la methode */
				String nompgn = "p"+GUI.envCourant.cptPoignees;
				Poignee newpgn = new Poignee(variableRetour, nompgn, GUI.envCourant.cptPoignees);
				variableRetour.poignees.add(newpgn);
				GUI.envCourant.pile.add(newpgn);
				GUI.envCourant.cptPoignees++;
				RetourMethode.newpoignee.setEnabled(false);
				GUI.paintbis = new DrawEnvironnementBis();
				GUI.recupererRetourMeth.retourMeth.setVisible(false);
				String vraiNomDevant = Environnement.trueVariableName(ChoixArguments.devantmeth);
				String newCodeLine = ChoixArguments.meth.getReturnType().getSimpleName()+" "+newpgn.nom+" = "
					+vraiNomDevant+"."+ChoixArguments.meth.getName()+"("+RetourMethode.nomsarguments+");";
				GUI.cloneMemoryState(newCodeLine);
				GUI.refreshComboBoxes();
			}
		});
		
		affectePoignee = new JButton("Affecter Poignee");
		RetourMethode.refreshPoigneesCompatibles();
		RetourMethode.affectePoignee.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(poigneesCompatibles.getItemCount()!=0){
					/* le retour de methode est affecte a une poignee de type compatible */
					String poigneeChoisie = (String) poigneesCompatibles.getSelectedItem();
					GUI.envCourant.equalsPoigneeVariable(poigneeChoisie, variableRetour.name);
					String vraiNomDevant = Environnement.trueVariableName(ChoixArguments.devantmeth);
					String newCodeLine = poigneeChoisie+" = "+vraiNomDevant+"."+ChoixArguments.meth.getName()
						+"("+RetourMethode.nomsarguments+");";
					GUI.paintbis = new DrawEnvironnementBis();
					GUI.cloneMemoryState(newCodeLine);
					GUI.recupererRetourMeth.retourMeth.setVisible(false);
					GUI.refreshComboBoxes();
				}
			}
		});
		String nomFenetre = "Retour "+Environnement.trueVariableName(ChoixArguments.devantmeth)+"."
			+ChoixArguments.meth.getName()+"("+RetourMethode.nomsarguments+")";
		RetourMethode.retourMeth = new JFrame(nomFenetre);
		Container content = RetourMethode.retourMeth.getContentPane();
	    content.setBackground(Color.WHITE);
	    content.setLayout(new GridLayout(3,1));
	    JPanel panchangeatt = new JPanel();
	    panchangeatt.setLayout(new GridLayout(1,3));
	    panchangeatt.add(varscompatibles);
	    panchangeatt.add(attributscompatibles);
	    panchangeatt.add(affecteval);
	    JPanel panPoignee = new JPanel();
	    panPoignee.setLayout(new GridLayout(1,2));
	    panPoignee.add(poigneesCompatibles);
	    panPoignee.add(affectePoignee);
		content.add(newpoignee);
		content.add(panchangeatt);
		content.add(panPoignee);
		RetourMethode.retourMeth.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RetourMethode.retourMeth.pack();
		RetourMethode.retourMeth.setVisible(true);
	}
	
	/** 
	 * les noms des poignees de type compatible avec le retour non void de la methode
	 * sont ajoutees a la JComboBox poigneesCompatibles 
	 */
	public static void refreshPoigneesCompatibles(){
		poigneesCompatibles.removeAllItems();
		for(int i=0; i<GUI.envCourant.pile.size(); i++){
			Poignee pgni = GUI.envCourant.pile.get(i);
			if(pgni.typepgn.isAssignableFrom(valeurRetour.getClass())){
				poigneesCompatibles.addItem(pgni.nom);
			}
		}
		if(poigneesCompatibles.getItemCount()!=0){
			/* il existe au moins une poignee de type compatible, 
			 * le bouton d'affectation est donc active */
			poigneesCompatibles.setSelectedIndex(0);
			affectePoignee.setEnabled(true);
		}else{
			/* il n'existe aucune poignee de type compatible,
			 * le bouton d'affectation est donc desactive */
			affectePoignee.setEnabled(false);
		}
	}
	
	/**
	 * les noms des attributs de la variable selectionnee dans la JComboBox varscompatibles
	 * ayant un type compatible avec le retour de la methode sont stockes dans la
	 * JComboBox attributscompatibles et l'attribut lui-meme 
	 * sera stocke dans l'ArrayList attributscompatiblesvals
	 */
	public static void refreshAttributsCompatibles(){
		int nbitems = RetourMethode.varscompatibles.getItemCount();
		if(nbitems!=0){
			/* on recherche pour une variable donnee quels attributs sont compatibles avec le type
			 * de retour de la methode afin d'afficher les noms de ces attributs dans attributscompatibles */
			Variable var_choisie = GUI.envCourant.getVariableByName((String)RetourMethode.varscompatibles.getSelectedItem());
			Field[] attChoisis = Environnement.getAllFields(var_choisie.type);
			RetourMethode.attributscompatibles.removeAllItems();
			RetourMethode.attributscompatiblesvals.clear();
			for(int i=0; i<attChoisis.length; i++){
				if(attChoisis[i].getType().isAssignableFrom(RetourMethode.valeurRetour.getClass())){
					RetourMethode.attributscompatibles.addItem(attChoisis[i].getName());
					RetourMethode.attributscompatiblesvals.add(attChoisis[i]);
				}
			}
		}
	}
	
}
