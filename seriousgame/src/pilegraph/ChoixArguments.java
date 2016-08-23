package pilegraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.*;

/**
 * 
 * fenetre qui apparaitra lorsque l'utilisateur devra choisir la valeur de chaque argument
 * d'une methode ou constructeur appele parmi les variables de type compatible de l'environnement
 *
 */
public class ChoixArguments extends JFrame{

	private static final long serialVersionUID = 1L;
	/**
	 * argsProposes.size() = argsChoisis.length = nombre d'arguments du constructeur ou de la methode appelee
	 * pour tout i entre 0 et argsProposes.size()-1, argsProposes.get(i) contiendra toutes les
	 * variables de GUI.envCourant compatibles avec le i-eme argument du constructeur
	 * ou methode appelee
	 */
	public static ArrayList<ArrayList<Variable>> argsProposes;
	/** 
	 * Pour tout i entre 0 et argsChoisis.length - 1, argsChoisis[i] contiendra la valeur 
	 * de chaque variable que l'utilisateur aura choisie comme argument 
	 * a passer au constructeur ou a la methode appelee
	 */
	public static Object[] argsChoisis;
	/**
	 * Pour tout i entre 0 et argsProposes.size()-1, si le i-eme argument du constructeur ou methode appelee
	 * ne peut pas etre fourni directement par l'utilisateur en le tapant avec le clavier,
	 * les noms de chaque variable de argsProposes.get(i) seront stockees dans une JComboBox
	 * qui sera ensuite ajoutee a l'attribut choix
	 */
	public static ArrayList<JComboBox<String>> choix;
	/**
	 * Pour tout i entre 0 et argsProposes.size()-1, si le i-eme argument du constructeur ou methode appelee
	 * peut etre fourni directement par l'utilisateur en le tapant avec le clavier,
	 * une JTextField sera creee et ajoutee a l'attribut choix afin que l'utilisateur y ecrit la valeur
	 * qu'il souhaite fournir
	 */
	public static ArrayList<JTextField> choixPrimitifs;
	/** 
	 * si un constructeur a ete appele, constr contient le constructeur appele 
	 */
	public static Constructor constr;
	/** 
	 * si une methode a ete appelee, meth contient la methode appelee 
	 */
	public static Method meth;
	/** 
	 * si un contructeur a ete appele, devantmeth contient la variable sur laquelle
	 * la methode a ete appelee 
	 */
	public static Variable devantmeth;
	/**
	 * des que tous les arguments seront choisis, la methode ou le constructeur sera appelee
	 * en cliquant sur le JButton creer
	 */
	public static JButton creer;
	
	public ChoixArguments(Constructor constr, ArrayList<ArrayList<Variable>> argsProposes){
		/* choix des arguments d'un contructeur */
		this.constr = constr;
		this.meth = null;
		this.devantmeth = null;
		this.argsProposes = argsProposes;
		Container content = this.getContentPane();
	    content.setBackground(Color.WHITE);
	    content.setLayout(new FlowLayout());
		argsChoisis = new Object[argsProposes.size()];
		choix = new ArrayList<JComboBox<String>>();
		choixPrimitifs = new ArrayList<JTextField>();
		for(int i=0; i<argsProposes.size(); i++){
			Class typeArgI = constr.getParameterTypes()[i];
			content.add(new JLabel(typeArgI.getSimpleName()));
			if(Environnement.isTypable(typeArgI)){
				JTextField primitiveVal = new JTextField(10);
				choixPrimitifs.add(primitiveVal);
				content.add(primitiveVal);
			}else{
				JComboBox<String> combo_i = new JComboBox<String>();
				for(int j=0; j<argsProposes.get(i).size(); j++){
					combo_i.addItem(argsProposes.get(i).get(j).name);
				}
				choix.add(combo_i);
				content.add(combo_i);
			}
		}
		GUI.deactivateButtons();
		
		this.creer = new JButton("Créer");
		creer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* appel du constructeur avec les arguments choisis */
				String nomsarguments = "";
				int nbArgsTypables = 0;
				Class[] typeArgs = ChoixArguments.constr.getParameterTypes();
				int nbArgs = typeArgs.length;
				for(int i=0; i<nbArgs; i++){
					String prefix = ",";
					if(i==0){
						prefix = "";
					}
					Class typeArgI = typeArgs[i];
					String nomTypeArgI = typeArgI.getSimpleName();
					if(Environnement.isTypable(typeArgI)){
						String chaineEntree = ChoixArguments.choixPrimitifs.get(nbArgsTypables).getText();
						nbArgsTypables++;
						try{
							if(nomTypeArgI.equals("Integer") || nomTypeArgI.equals("int")){
								argsChoisis[i] = Integer.parseInt(chaineEntree);
								nomsarguments += prefix+chaineEntree;
							}else if(nomTypeArgI.equals("Double") || nomTypeArgI.equals("double")){
								argsChoisis[i] = Double.parseDouble(chaineEntree);
								nomsarguments += prefix+chaineEntree;
							}else if(nomTypeArgI.equals("String")){
								argsChoisis[i] = chaineEntree;
								nomsarguments += prefix+"\""+chaineEntree+"\"";
							}
						}catch(NumberFormatException e){
							String numero = "Le "+(i+1)+"eme ";
							if(i==0){
								numero = "Le premier ";
							}else if(i==1){
								numero = "Le second ";
							}
							JOptionPane.showMessageDialog(GUI.choose, numero+"argument fourni \""+chaineEntree 
									+"\" n'est pas de type "+nomTypeArgI, "Argument incorrect", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}else{
						Variable varChoisieI = ChoixArguments.argsProposes.get(i).get(choix.get(i).getSelectedIndex());
						argsChoisis[i] = varChoisieI.valeur;
						nomsarguments += prefix+Environnement.trueVariableName(varChoisieI);
					}
				}
				String classname = ChoixArguments.constr.getDeclaringClass().getSimpleName();
				GUI.envCourant.createVariable(ChoixArguments.constr, argsChoisis);
				GUI.paintbis = new DrawEnvironnementBis();
				String newCodeLine = classname+" p"+GUI.envCourant.cptPoignees+" = new "+ classname+"("+nomsarguments+");";
				GUI.cloneMemoryState(newCodeLine);
				GUI.choose.setVisible(false);
				GUI.refreshComboBoxes();
				
			}
			
		});
		this.setTitle("Choix arguments "+Environnement.simpleConstructorName(constr));
		//this.setSize(1000,1000);
	    content.add(creer);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.pack();
		this.setVisible(true);
	}
	
	public ChoixArguments(Method meth, Variable devantmeth, ArrayList<ArrayList<Variable>> argsProposes){
		this.constr = null;
		this.meth = meth;
		this.devantmeth = devantmeth;
		this.argsProposes = argsProposes;
		argsChoisis = new Object[argsProposes.size()];
		choix = new ArrayList<JComboBox<String>>();
		choixPrimitifs = new ArrayList<JTextField>();
		Container content = this.getContentPane();
	    content.setBackground(Color.WHITE);
	    content.setLayout(new FlowLayout());
	    for(int i=0; i<meth.getParameterTypes().length; i++){
			Class typeArgI = meth.getParameterTypes()[i];
			content.add(new JLabel(typeArgI.getSimpleName()));
			if(Environnement.isTypable(typeArgI)){
				/* l'utilisateur peut taper directement la valeur 
				 * du i-eme argument de la methode meth */
				JTextField primitiveVal = new JTextField(10);
				choixPrimitifs.add(primitiveVal);
				content.add(primitiveVal);
			}else{
				/* l'utilisateur ne peut pas taper directement la valeur 
				 * du i-eme argument de la methode meth. Il devra choisir
				 * une variable de l'environnement de type compatible */
				JComboBox<String> combo_i = new JComboBox<String>();
				for(int j=0; j<argsProposes.get(i).size(); j++){
					combo_i.addItem(argsProposes.get(i).get(j).name);
				}
				choix.add(combo_i);
				content.add(combo_i);
			}
		}
		GUI.deactivateButtons();
		this.creer = new JButton("Appeler");
		creer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nomsarguments = "";
				int nbArgsTypables = 0;
				Class[] typeArgsMeth = ChoixArguments.meth.getParameterTypes();
				for(int i=0; i<typeArgsMeth.length; i++){
					String prefix = ",";
					if(i==0){
						prefix = ""; 
					}
					Class typeArgI = typeArgsMeth[i];
					String nomTypeArgI = typeArgI.getSimpleName();
					if(Environnement.isTypable(typeArgI)){
						String chaineEntree = ChoixArguments.choixPrimitifs.get(nbArgsTypables).getText();
						nbArgsTypables++;
						try{
							if(nomTypeArgI.equals("Integer") || nomTypeArgI.equals("int")){
								argsChoisis[i] = Integer.parseInt(chaineEntree);
								nomsarguments += prefix+chaineEntree;
							}else if(nomTypeArgI.equals("Double") || nomTypeArgI.equals("double")){
								argsChoisis[i] = Double.parseDouble(chaineEntree);
								nomsarguments += prefix+chaineEntree;
							}else if(nomTypeArgI.equals("String")){
								argsChoisis[i] = chaineEntree;
								nomsarguments += prefix+"\""+chaineEntree+"\"";
							}
						}catch(NumberFormatException e){
							String numero = "Le "+(i+1)+"eme ";
							if(i==0){
								numero = "Le premier ";
							}else if(i==1){
								numero = "Le second ";
							}
							JOptionPane.showMessageDialog(GUI.choose, numero+"argument fourni \""+chaineEntree 
									+"\" n'est pas de type "+nomTypeArgI, "Argument incorrect", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}else{
						Variable varChoisieI = ChoixArguments.argsProposes.get(i).get(choix.get(i).getSelectedIndex());
						argsChoisis[i] = varChoisieI.valeur;
						nomsarguments += prefix + Environnement.trueVariableName(varChoisieI);
					}
				}
				Object returnmethod;
				returnmethod = GUI.envCourant.callMethodUpon(ChoixArguments.meth, ChoixArguments.devantmeth, argsChoisis);
				GUI.paintbis = new DrawEnvironnementBis();
				/* la fenetre de choix disparait */
				GUI.choose.setVisible(false);
				if(returnmethod!=null){
					/* le retour de la methode est non void, l'utilisateur sera donc renvoye vers 
					 * une fenetre RetourMethode afin de decider que faire avec ce retour */
					GUI.recupererRetourMeth = new RetourMethode(returnmethod, ChoixArguments.devantmeth.name, 
							ChoixArguments.meth.getName(), nomsarguments);
					GUI.recupererRetourMeth.retourMeth.setVisible(true);
				}else{
					/* la methode appelee renvoie void */
					String vraiNomDevant = Environnement.trueVariableName(ChoixArguments.devantmeth);
					String newCodeLine = vraiNomDevant+"."+ChoixArguments.meth.getName()+
						"("+nomsarguments+");";
					GUI.cloneMemoryState(newCodeLine);
					GUI.refreshComboBoxes();
				}
			}
			
		});
		this.setTitle("Choix arguments "+Environnement.simpleMethodName(meth));
		//this.setSize(1000,1000);
		content.add(creer);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.pack();
		this.setVisible(true);
	}
}
