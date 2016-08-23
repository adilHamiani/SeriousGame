package pilegraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import com.mxgraph.swing.mxGraphComponent;

public class GUI {

	public static final long serialVersionUID = 1L;
	/**
	 * l'environnement affiche sera contenu dans l'attribut envCourant. Lorsque l'utilisateur
	 * retournera en arriere dans le temps, l'environnement courant sera stocke dans l'attribut
	 * quarantine et l'attribut envCourant sera affecte a un environnement precedent
	 */
	public static Environnement envCourant;
	/**
	 * paintbis est une instance de DrawEnvironnementBis qui affichera 
	 * - les poignees et variables contenues dans l'environnement 
	 * - les liens entre chaque poignee et la valeur vers laquelle elle pointe
	 * - les liens entre chaque variable et la valeur de chacun de ses attributs 
	 *   qui pourront etre d'autres variables
	 */
	public static DrawEnvironnementBis paintbis;
	/**
	 * JFrame qui sera affichee lorsque l'application sera executee
	 */
	public static JFrame homescreen = new JFrame();
	/**
	 * JComboBox contenant le nom simple de tous les types dont l'utilisateur
	 * pourra creer des instances
	 */
	public static JComboBox<String> type = new JComboBox<String>();
	/**
	 * JComboBox dans laquelle tous les noms simples des constructeurs
	 * du type selectionne dans la JComboBox type seront affiches
	 */
	public static JComboBox<String> constructeurs = new JComboBox<String>();
	/**
	 * JComboBox dans laquelle les noms simples des methodes de la classe selectionnee
	 * dans la JComboBox type seront affichees
	 */
	public static JComboBox<String> methodes = new JComboBox<String>();
	/**
	 * JComboBox stockant les noms de toutes les variables de type compatible
	 * avec celui selectionne dans la JComboBox type
	 */
	public static JComboBox<String> devantmethode = new JComboBox<String>();
	/**
	 * attribut qui contiendra la fenetre qui apparaitra lorsque l'utilisateur
	 * devra affecter un retour non void d'une methode
	 */
	public static RetourMethode recupererRetourMeth = null;
	/**
	 * JComboBox qui contiendra les noms des variables et poignees de l'environnement courant
	 */
	public static JComboBox<String> variables = new JComboBox<String>();
	/**
	 * Lorsqu'une variable est selectionnee dans la JComboBox variables,
	 * les noms de tous ses attributs seront affiches dans la JComboBox attributs.
	 * Lorsqu'une poignee est selectionnee, cette JComboBox sera videe de son contenu.  
	 */
	public static JComboBox<String> attributs = new JComboBox<String>();
	/**
	 * Lorsqu'une variable est selectionnee dans la JComboBox variables et 
	 * qu'un attribut est selectionne dans la JComboBox attributs,
	 * les noms des variables de type compatible avec l'attribut selectionne dans la JComboBox attributs
	 * seront affichees dans la JComboBox nouveauxattributsnom.
	 * Lorsqu'une poignee est selectionnee dans la JComboBox variables,
	 * la JComboBox attributs sera videe de son contenu et les noms des variables et poignees
	 * de type compatible avec la poignee selectionnee dans la JComboBox variables
	 * seront affiches dans la JComboBox nouveauxattributsnom
	 */
	public static JComboBox<String> nouveauxattributsnom = new JComboBox<String>();
	/**
	 * l'attribut instanceRecordingTape contiendra les copies de l'ensemble des variables de l'environnement
	 * faites suite a toute operation significative de l'utilisateur telle que l'appel d'un constructeur,
	 * d'une methode ou la modification d'un attribut
	 */
	public static ArrayList<ArrayList<Variable>> instanceRecordingTape = new ArrayList<ArrayList<Variable>>();
	/**
	 * l'attribut pileRecordingTape contiendra les copies de l'ensemble des poignees de l'environnement
	 * faites suite a toute operation significative de l'utilisateur telle que l'appel d'un constructeur,
	 * d'une methode ou la modification d'un attribut 
	 */
	public static ArrayList<ArrayList<Poignee>> pileRecordingTape = new ArrayList<ArrayList<Poignee>>();
	/**
	 * l'attribut time est une variable temps qui s'incrementera suite a toute operation significative 
	 * de l'utilisateur telle que l'appel d'un constructeur, d'une methode ou la modification d'un attribut 
	 */
	public static int time = 0;
	/**
	 * le JButton rewind permettra a l'utilisateur de retourner a l'instant precedant et de visualiser
	 * l'etat precedent de l'environnement, qu'il s'agisse des variables ou des poignees
	 */
	public static JButton rewind;
	/**
	 * Lorsque le bouton rewind a ete appuye au moins une fois, le JButton fastforward s'activera.
	 * En cliquant sur le JButton fastforward, l'utilisateur pourra avancer d'un cran dans le temps
	 * visualiser l'etat suivant de l'environnement, qu'il s'agisse des variables ou des poignees.
	 * Tant que l'environnement ne sera pas dans son etat courant, l'utilisateur ne pourra
	 * effectuer aucune operation sur les variables de l'environnement. 
	 */
	public static JButton fastforward;
	/** 
	 * l'environnement courant sera stocke dans l'attribut quarantine lors d'un retour en arriere 
	 * afin de pouvoir le recuperer lorsque l'utilisateur retourne a l'etat courant et qu'il
	 * puisse poursuivre ses operations sur les variables de l'environnement courant 
	 */
	public static Environnement quarantine;
	/**
	 * etatCourant est a true si et seulement si l'environnement affiche par l'attribut paintbis est
	 * l'environnement courant. Des que l'utilisateur retourne en arriere dans le temps,
	 * etatCourant passe a false
	 */
	public static boolean etatCourant;
	/**
	 * JList qui affichera toutes les commandes Java generees par les operations de l'utilisateur
	 */
	public static JList codeGenere;
	/**
	 * commandesDeclenchees contiendra les chaines de caractere correspondant
	 * aux commandes Java que les operations de l'utilisateur engendrent  
	 */
	public static DefaultListModel commandesDeclenchees;
	/**
	 * l'attribut choose contiendra la fenetre qui s'affichera lorsque l'utilisateur devra choisir
	 * les valeurs des arguments d'un constructeur ou d'une methode. Il s'agira d'une instance
	 * de la classe ChoixArguments 
	 */
	public static ChoixArguments choose = null;
	/**
	 * lorsque le constructeur a ete selectionne dans la JComboBox constructeurs, le constructeur
	 * selectionne sera appele en cliquant sur le JButton create
	 */
	public static JButton create;
	/**
	 * Lorsqu'une variable a ete selectionnee dans la JComboBox variables, que l'un de ses attributs
	 * a ete selectionne dans la JComboBox attributs et qu'une variable de type compatible avec l'attribut
	 * choisi a ete selectionnee dans nouveauxattributsnom, l'attribut selectionne de la variable choisie
	 * est affecte a sa nouvelle valeur choisie dans la JComboBox nouveauxattributsnom en cliquant sur
	 * le JButton modifyattribute.
	 * 
	 * Lorsqu'une poignee a ete selectionnee dans la JComboBox variables et qu'une poignee ou variable
	 * de type compatible a ete selectionnee dans la JComboBox nouveauxattributsnom,
	 * la poignee choisie est affectee a sa nouvelle valeur choisie dans nouveauxattributsnom
	 * en cliquant sur le JButton modifyattribute.
	 */
	public static JButton modifyattribute;
	/**
	 * le ramasse-miettes sera appele en cliquant sur le JButton ramassemiette qui lance la methode
	 * ramasseMiettes sur l'environnement courant stocke dans envCourant
	 * 
	 */
	public static JButton ramassemiette;
	/**
	 * Lorsqu'une methode a ete selectionnee dans la JComboBox methodes et que la variable sur laquelle
	 * la methode sera appelee a ete selectionnee dans la JComboBox devantmethode,
	 * la methode choisie sera appelee sur la variable choisie en cliquant sur le JButton callmethod.
	 * Si la methode requiert des arguments, une instance de ChoixArguments sera creee et stockee
	 * dans l'attribut choose de GUI afin que la fenetre permettant a l'utilisateur de choisir la valeur
	 * de chaque argument apparaisse.
	 */
	public static JButton callmethod;

	public GUI() throws HeadlessException {
		homescreen.setTitle("Gestion dynamique d'une pile d'execution");
		//homescreen.setSize(1000,1000);
		Container content = homescreen.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new BorderLayout()); 
		envCourant = new Environnement();
		paintbis = new DrawEnvironnementBis();		
		content.add(paintbis,BorderLayout.WEST);
		homescreen.pack();
		homescreen.setVisible(true);
	}

	public GUI(Environnement env) throws HeadlessException {
		envCourant = env;
		homescreen.setTitle("Gestion dynamique d'une pile d'exécution");
		homescreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//homescreen.setSize(1000,1000);
		Container content = homescreen.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new BorderLayout()); 
		GUI.homescreen.add(new JPanel(), BorderLayout.CENTER);
		GUI.paintbis = new DrawEnvironnementBis();
		content.add(paintbis, BorderLayout.WEST);
		GUI.create = new JButton("Créer");
		GUI.callmethod = new JButton("Appeler");
		GUI.ramassemiette = new JButton("Ramasse-Miettes");
		GUI.modifyattribute = new JButton("Modifier");
		GUI.modifyattribute.setEnabled(false);
		GUI.rewind = new JButton("Rewind");
		GUI.rewind.setEnabled(false);
		GUI.fastforward = new JButton("Fast-Forward");
		GUI.fastforward.setEnabled(false);
		GUI.etatCourant = true;

		GUI.initialiseTypeEtTypemethodes();

		GUI.type.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUI.refreshConstructeurs();
				GUI.refreshMethodes();
				GUI.refreshDevantmethode();

			}
		});

		create.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Class newclass = envCourant.types.get(type.getSelectedIndex());
				Constructor newconstr = newclass.getConstructors()[constructeurs.getSelectedIndex()];
				if(newconstr.getParameterTypes().length != 0){
					/* le constructeur appele requiert au moins un argument. Il faudra donc
					 * que l'utilisateur choisisse chaque argument parmi les variables de type compatible
					 * avec le type declare de chaque argument */
					ArrayList<ArrayList<Variable>> argsconstr = envCourant.getMethodOrConstructorArguments(newconstr.getParameterTypes());
					boolean lackArguments = false;
					ArrayList<String> noargsnames = new ArrayList<String>();
					for(int i=0; i<argsconstr.size(); i++){
						if(argsconstr.get(i).size()==0 && !Environnement.isTypable(newconstr.getParameterTypes()[i])){
							lackArguments = true;
							noargsnames.add(newconstr.getParameterTypes()[i].getSimpleName());
						}
					}
					if(!lackArguments){
						/* chaque argument de la methode appelee possede au moins une variable de type compatible
						 * dans l'environnement courant. La methode pourra donc etre appelee. L'utilisateur choisira
						 * chacun des arguments parmi tous les candidats */
						choose = new ChoixArguments(newconstr, envCourant.getMethodOrConstructorArguments(newconstr.getParameterTypes()));
						choose.setVisible(true);
					}else{
						/* certains arguments ne pourront pas etre pourvus par absence de variables de type compatible
						 * dans l'environnement. Il est donc impossible d'appeler le constructeur et un message d'erreur
						 * est affiche */
						String lackargsmessage = noargsnames.get(0);
						for(int i=1; i<noargsnames.size(); i++){
							lackargsmessage += ", "+noargsnames.get(i);
						}
						JOptionPane.showMessageDialog(GUI.homescreen, "Aucune variable de type "+lackargsmessage,
								"Manque d'arguments pour constructeur", JOptionPane.ERROR_MESSAGE);

					}
				}else{
					/* le constructeur appele ne requiert aucun argument. L'utilisateur ne devra donc choisir aucun
					 * argument et le constructeur est appele directement sans passer par la fenetre de choix d'arguments */
					Object[] args = new Object[0];
					envCourant.createVariable(newconstr, args);
					GUI.refreshComboBoxes();
					String nomclasse = newconstr.getDeclaringClass().getSimpleName();
					String newCodeLine = nomclasse+" p"+envCourant.cptPoignees+" = new "+nomclasse+"();";
					paintbis = new DrawEnvironnementBis();
					GUI.cloneMemoryState(newCodeLine);

				}
			}
		});

		GUI.methodes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {				
			}
		});

		callmethod.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Class newclass = envCourant.types.get(GUI.type.getSelectedIndex());
				Method chosenmethod = newclass.getMethods()[GUI.methodes.getSelectedIndex()];
				if(chosenmethod.getParameterTypes().length != 0){
					/* la methode appelee requiert au moins un argument. Il faudra donc
					 * que l'utilisateur choisisse chaque argument parmi les variables de type compatible
					 * avec le type declare de chaque argument */
					Class[] typesparams = chosenmethod.getParameterTypes();
					ArrayList<ArrayList<Variable>> argsconstr = envCourant.getMethodOrConstructorArguments(typesparams);
					boolean lackArguments = false;
					ArrayList<String> noargsnames = new ArrayList<String>();
					for(int i=0; i<argsconstr.size(); i++){
						if(argsconstr.get(i).size()==0 && !Environnement.isTypable(typesparams[i])){
							lackArguments = true;
							noargsnames.add(chosenmethod.getParameterTypes()[i].getSimpleName());
						}
					}
					if(!lackArguments){
						/* chaque argument de la methode appelee possede au moins une variable de type compatible
						 * dans l'environnement courant. La methode pourra donc etre appelee. L'utilisateur choisira
						 * chacun des arguments parmi tous les candidats */
						Variable frontofmeth = envCourant.getVariableByName((String)devantmethode.getSelectedItem());
						choose = new ChoixArguments(chosenmethod, frontofmeth, envCourant.getMethodOrConstructorArguments(typesparams));
						choose.setVisible(true);
					}else{
						/* certains arguments ne pourront pas etre pourvus par absence de variables de type compatible
						 * dans l'environnement. Il est donc impossible d'appeler la methode et un message d'erreur
						 * est affiche */
						String lackargsmessage = noargsnames.get(0);
						for(int i=1; i<noargsnames.size(); i++){
							lackargsmessage += ", "+noargsnames.get(i);
						}
						JOptionPane.showMessageDialog(GUI.homescreen, "Aucune variable de type "+lackargsmessage,
								"Manque d'arguments pour methode", JOptionPane.ERROR_MESSAGE);
					}
				}else{
					/* la methode appelee ne requiert aucun argument. L'utilisateur ne devra donc choisir aucun
					 * argument et la methode est appelee directement sans passer par la fenetre de choix d'arguments */
					Object[] args = new Object[0];
					Variable frontofmeth = envCourant.getVariableByName((String)devantmethode.getSelectedItem());
					Object returnmethode = envCourant.callMethodUpon(chosenmethod, frontofmeth, args);
					paintbis = new DrawEnvironnementBis();
					GUI.refreshVariables();
					if(returnmethode!=null){
						/* la methode retourne un objet. L'utilisateur choisira ce qu'il souhaite faire
						 * avec ce retour de methode. */
						ChoixArguments.meth = chosenmethod;
						ChoixArguments.devantmeth = frontofmeth;
						GUI.recupererRetourMeth = new RetourMethode(returnmethode, frontofmeth.name, 
								chosenmethod.getName(), "");
					}else{
						/* la methode retourne void et a uniquement des effets de bord */
						String newCodeLine = Environnement.trueVariableName(frontofmeth)+"."+chosenmethod.getName()+"();";
						GUI.cloneMemoryState(newCodeLine);
						GUI.refreshComboBoxes();
					}
				}
			}
		});

		ramassemiette.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				envCourant.ramasseMiettes();
				paintbis = new DrawEnvironnementBis();
				GUI.cloneMemoryState(" /* Appel du ramasse-miettes */ ");
				GUI.refreshComboBoxes();
			}
		});

		GUI.variables.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(GUI.variables.getItemCount()!=0){
					if(GUI.variables.getSelectedIndex()==(-1)){
						GUI.variables.setSelectedIndex(0);
					}
				}
				String nomChoisi = (String) GUI.variables.getSelectedItem();
				Poignee poigneeChoisie = envCourant.getPoigneeByName(nomChoisi);
				Variable variableChoisie = envCourant.getVariableByName(nomChoisi);
				if(variableChoisie!=null){
					GUI.refreshAttributs();
				}else if(poigneeChoisie!=null){
					GUI.attributs.removeAllItems();
					GUI.refreshPoigneesEtVariables();
				}else{
					GUI.attributs.removeAllItems();
					GUI.nouveauxattributsnom.removeAllItems();
					GUI.modifyattribute.setEnabled(false);
				}
			}
		});

		GUI.attributs.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(GUI.attributs.getItemCount()!=0){
					GUI.refreshNouveauxattributsnom();
				}
			}
		});

		modifyattribute.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if((GUI.variables.getItemCount()!=0) && (GUI.nouveauxattributsnom.getItemCount()!=0)){
					String nomVarAModifier = (String) GUI.variables.getSelectedItem();
					Variable varAModifier = envCourant.getVariableByName(nomVarAModifier);
					Poignee poigneeAModifier = envCourant.getPoigneeByName((String)GUI.variables.getSelectedItem());
					if(varAModifier!=null && GUI.attributs.getItemCount()!=0){
						String nomAttributChoisi = (String)GUI.attributs.getSelectedItem();
						Variable nouvelAttribut = envCourant.getVariableByName((String)GUI.nouveauxattributsnom.getSelectedItem());
						if(nouvelAttribut==null){
							/* mise a null d'un attribut d'une variable de l'environnement */
							envCourant.setAttributeNull(nomVarAModifier, nomAttributChoisi);
						}else if(GUI.nouveauxattributsnom.getItemCount()>1){
							/* modification de la valeur d'un attribut d'une variable de l'environnement, 
							 * la nouvelle valeur etant differente de null */
							envCourant.changeAttribute(nomAttributChoisi, varAModifier, nouvelAttribut);
						}
						String newCodeLine = Environnement.trueVariableName(varAModifier)+"."+nomAttributChoisi
						+" = "+Environnement.trueVariableName(nouvelAttribut)+";";
						paintbis = new DrawEnvironnementBis();
						GUI.cloneMemoryState(newCodeLine);
						GUI.refreshComboBoxes();
					}else if(poigneeAModifier!=null ){
						/* Modification de la valeur d'une poignee. Une poignee pourra :
						 * - soit etre mise a null
						 * - soit etre affectee a la valeur d'une autre poignee de type compatible
						 * - soit etre affectee a une nouvelle variable de type compatible */
						String newCodeLine;
						if(GUI.nouveauxattributsnom.getSelectedItem().equals("null")){
							/* il faut affecter la poignee selectionnee dans GUI.poignees a null */
							envCourant.mettrePoigneeNull(poigneeAModifier.nom);
							newCodeLine = poigneeAModifier.nom+" = null;";
						}else if(envCourant.getPoigneeByName((String)GUI.nouveauxattributsnom.getSelectedItem())!=null){
							/* La valeur selectionnee de GUI.poigneesEtVariables designe une poignee.
							 * Il faut donc affecter la poignee selectionnee dans GUI.poignee a la meme valeur que
							 * celle selectionnee dans GUI.poigneesEtVariables */
							String poigneeGauche = (String)GUI.variables.getSelectedItem();
							String poigneeDroite = (String)GUI.nouveauxattributsnom.getSelectedItem();
							envCourant.equalsPoigneePoignee(poigneeGauche, poigneeDroite);
							newCodeLine = poigneeGauche+" = "+poigneeDroite+";";
						}else{
							/* La valeur selectionnee de GUI.poigneesEtVariables designe une variable.
							 * La poignee selectionnee dans GUI.poignees doit donc desormais pointer
							 * vers la variable selectionnee dans GUI.poigneesEtVariables */
							String poigneeChoisie = (String)GUI.variables.getSelectedItem();
							String nomVarChoisie = (String)GUI.nouveauxattributsnom.getSelectedItem();
							Variable varChoisie = envCourant.getVariableByName(nomVarChoisie);
							String vraiNomVarChoisie = Environnement.trueVariableName(varChoisie);
							envCourant.equalsPoigneeVariable(poigneeChoisie, nomVarChoisie);
							newCodeLine = poigneeChoisie+" = "+vraiNomVarChoisie;
						}
						paintbis = new DrawEnvironnementBis();
						GUI.cloneMemoryState(newCodeLine);
						GUI.refreshComboBoxes();
					}
				}
			}
		});

		GUI.instanceRecordingTape.add(new ArrayList<Variable>());
		GUI.pileRecordingTape.add(new ArrayList<Poignee>());

		rewind.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(GUI.time>0){
					/* l'environnement n'est pas a l'etat initial et il est donc possible de retourner en arriere */
					if(GUI.time == (GUI.instanceRecordingTape.size()-1)){
						/* l'environnement se trouve dans son etat courant, ie l'etat le plus recent.
						 * Il s'agit donc du premier retour en arriere. 
						 * L'etat courant est stocke afin de pouvoir le retrouver par la suite et poursuivre
						 * les operations dessus */
						GUI.create.setEnabled(false);
						GUI.modifyattribute.setEnabled(false);
						GUI.ramassemiette.setEnabled(false);
						GUI.callmethod.setEnabled(false);
						GUI.fastforward.setEnabled(true);
						GUI.etatCourant = false;
						GUI.quarantine = GUI.envCourant;
						GUI.envCourant = new Environnement(GUI.pileRecordingTape.get(GUI.time-1), envCourant.types,
								envCourant.typesprimitifs, GUI.instanceRecordingTape.get(GUI.time-1), 
								envCourant.cptPoignees);
						GUI.paintbis = new DrawEnvironnementBis();
						GUI.time--;
					}else{
						envCourant.instances = GUI.instanceRecordingTape.get(GUI.time - 1);
						envCourant.pile = GUI.pileRecordingTape.get(GUI.time -1);
						GUI.paintbis = new DrawEnvironnementBis();
						GUI.time--;
						GUI.fastforward.setEnabled(true);
					}
					GUI.codeGenere.setSelectedIndex(GUI.time);
				}
				if(GUI.time==0){
					GUI.rewind.setEnabled(false);
				}
			}
		});

		fastforward.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(GUI.time<(GUI.instanceRecordingTape.size()-1)){
					/* l'environnement affiche n'est pas l'environnement courant, l'on peut donc avancer
					 * au prochain etat d'environnement */
					if((GUI.time+1) == (GUI.instanceRecordingTape.size()-1)){
						/* Retour a l'environnement courant ie le plus recent, l'utilisateur pourra poursuivre
						 *  les operations sur les instances de classes */
						// reactivation des boutons
						GUI.create.setEnabled(true);
						GUI.modifyattribute.setEnabled(true);
						GUI.ramassemiette.setEnabled(true);
						GUI.callmethod.setEnabled(true);
						GUI.rewind.setEnabled(true);
						GUI.fastforward.setEnabled(false);
						GUI.etatCourant = true;
						GUI.envCourant = GUI.quarantine;
						GUI.time++;
						GUI.paintbis = new DrawEnvironnementBis();
					}else{
						/* avancer a l'etat prochain de l'environnement ne ramene pas l'environnement a son
						 * etat courant, ie le plus recent */
						GUI.envCourant.instances = GUI.instanceRecordingTape.get(GUI.time + 1);
						GUI.envCourant.pile = GUI.pileRecordingTape.get(GUI.time + 1);
						GUI.time++;
						GUI.paintbis = new DrawEnvironnementBis();
						GUI.rewind.setEnabled(true);
					}
					GUI.codeGenere.setSelectedIndex(GUI.time);
				}
			}
		});

		commandesDeclenchees = new DefaultListModel();
		commandesDeclenchees.addElement("    ");
		codeGenere = new JList(commandesDeclenchees);
		codeGenere.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		codeGenere.setLayoutOrientation(JList.VERTICAL);
		codeGenere.setVisibleRowCount(10);
		codeGenere.setSelectedIndex(0);
		GUI.codeGenere.setSelectedIndex(0);
		JScrollPane listScrollPane = new JScrollPane(codeGenere);
		listScrollPane.setPreferredSize(new Dimension(250, 80));

		JPanel panmenu = new JPanel();
		panmenu.setLayout(new GridLayout(2,1));
		JPanel pancreate = new JPanel();
		pancreate.setLayout(new GridLayout(1,6));
		pancreate.add(type);
		pancreate.add(constructeurs);
		pancreate.add(create);
		pancreate.add(devantmethode);
		pancreate.add(methodes);
		pancreate.add(callmethod);
		panmenu.add(pancreate);
		JPanel panchangeatt = new JPanel();
		panchangeatt.setLayout(new GridLayout(1,4));
		panchangeatt.add(variables);
		panchangeatt.add(attributs);
		panchangeatt.add(nouveauxattributsnom);
		panchangeatt.add(GUI.modifyattribute);
		panchangeatt.add(ramassemiette);
		JPanel timecontrol = new JPanel();
		timecontrol.setLayout(new GridLayout(1,2));
		timecontrol.add(rewind);
		timecontrol.add(fastforward);
		panmenu.add(panchangeatt);
		JPanel codeWithTime = new JPanel();
		codeWithTime.setLayout(new BorderLayout());
		codeWithTime.add(listScrollPane, BorderLayout.CENTER);
		codeWithTime.add(timecontrol, BorderLayout.SOUTH);

		content.add(panmenu, BorderLayout.SOUTH);
		content.add(codeWithTime, BorderLayout.EAST);
		homescreen.pack();
		homescreen.setVisible(true);
	}

	/**
	 * Lorsqu'une poignee a ete selectionnee dans la JComboBox variables,
	 * les variables et poignees de type compatible sont rajoutees dans la
	 * JComboBox nouveauxattributsnom
	 */
	public static void refreshPoigneesEtVariables(){
		if(GUI.variables.getItemCount()!=0){
			if(GUI.variables.getSelectedIndex()==(-1)){
				GUI.variables.setSelectedIndex(0);
			}
			Poignee poigneeChoisie = envCourant.getPoigneeByName((String)GUI.variables.getSelectedItem()); 
			if(poigneeChoisie!=null){
				Class typePoignee = poigneeChoisie.typepgn;
				GUI.nouveauxattributsnom.removeAllItems();
				for(int i=0; i<envCourant.pile.size(); i++){
					if(typePoignee.isAssignableFrom(envCourant.pile.get(i).typepgn)){
						GUI.nouveauxattributsnom.addItem(envCourant.pile.get(i).nom);
					}
				}
				for(int i=0; i<envCourant.instances.size(); i++){
					Variable var_i = envCourant.instances.get(i);
					boolean accessible = Environnement.estAccessible(var_i);
					if(typePoignee.isAssignableFrom(var_i.type) && accessible){
						GUI.nouveauxattributsnom.addItem(var_i.name);
					}
				}
				GUI.nouveauxattributsnom.addItem("null");
				/* poigneesEtVariables contient toujours au moins un element qui est "null" 
				 * donc le bouton egalite est toujours active */
				boolean fenetrePrincipale = GUI.recupererRetourMeth == null 
				|| (!GUI.recupererRetourMeth.retourMeth.isVisible());
				boolean fenetrePrincipale2 = GUI.choose == null || (!GUI.choose.isVisible());
				if(GUI.etatCourant&&fenetrePrincipale&&fenetrePrincipale2){
					GUI.modifyattribute.setEnabled(true);
				}
			}
		}
	}

	/**
	 * vide la JComboBox variables de son contenu et y rajoute les noms
	 * de toutes les variables et poignees de l'environnement courant
	 */
	public static void refreshVariables(){
		GUI.variables.removeAllItems();
		for(int i=0; i<envCourant.instances.size(); i++){
			boolean accessible = Environnement.estAccessible(envCourant.instances.get(i));
			boolean primitif = envCourant.isPrimitif(envCourant.instances.get(i).type.getName());
			if(accessible && !primitif){
				GUI.variables.addItem(envCourant.instances.get(i).name);
			}
		}
		for(int i=0; i<envCourant.pile.size(); i++){
			GUI.variables.addItem(envCourant.pile.get(i).nom);
		}
		if(GUI.variables.getItemCount()!=0){
			GUI.variables.setSelectedIndex(0);
			Poignee p = envCourant.getPoigneeByName((String)GUI.variables.getSelectedItem());
			Variable v = envCourant.getVariableByName((String) GUI.variables.getSelectedItem());
			if(p!=null){
				/* une poignee a ete selectionnee */
				GUI.attributs.removeAllItems();
				GUI.refreshPoigneesEtVariables();
			}else if(v!=null){
				GUI.refreshAttributs();
			}
		}else{
			GUI.modifyattribute.setEnabled(false);
		}
	}

	/**
	 * vide la JComboBox attributs de son contenu et rajoute les noms
	 * des attributs de la variable selectionnee dans la JComboBox variables.
	 * Initialise ensuite la JComboBox nouveauxattributsnom avec les
	 * variables de type compatible avec le premier attribut rajoute dans la JComboBox attributs
	 */
	public static void refreshAttributs(){
		if(GUI.variables.getItemCount() != 0){
			if(GUI.variables.getSelectedIndex() == (-1)){
				GUI.variables.setSelectedIndex(0);
			}
			Variable varChoisie = envCourant.getVariableByName((String)GUI.variables.getSelectedItem());
			if(varChoisie!=null){
				Field[] allAttributs = Environnement.getAllFields(varChoisie.type);
				GUI.attributs.removeAllItems();
				for(int i=0; i<allAttributs.length; i++){
					GUI.attributs.addItem(allAttributs[i].getName());
				}
				if(GUI.attributs.getItemCount()!=0){
					GUI.attributs.setSelectedIndex(0);
					GUI.refreshNouveauxattributsnom();
				}else{
					GUI.modifyattribute.setEnabled(false);
				}
			}
		}
	}

	/**
	 * rajoute dans la JComboBox nouveauxattributsnom les variables de type compatible avec
	 * l'attribut selectionne dans la JComboBox attributs de la variable selectionnee dans
	 * la JComboBox variables. Si aucune variable compatible n'existe, 
	 * le JButton modifyattribute sera desactive. Sinon, il sera active.
	 * 
	 */
	public static void refreshNouveauxattributsnom(){
		if((GUI.variables.getItemCount()!=0) && (GUI.attributs.getItemCount()!=0)){
			GUI.nouveauxattributsnom.removeAllItems();
			Variable varChoisie = envCourant.getVariableByName((String)GUI.variables.getSelectedItem());
			if(varChoisie!=null){
				Field attributchoisi = Environnement.getAllFields(varChoisie.type)[GUI.attributs.getSelectedIndex()];
				Class typeattribut = attributchoisi.getType();
				for(int i=0; i<envCourant.instances.size(); i++){
					Variable var_i = envCourant.instances.get(i);
					boolean accessible = Environnement.estAccessible(var_i);
					if(typeattribut.isAssignableFrom(var_i.type) && accessible){
						GUI.nouveauxattributsnom.addItem(var_i.name);
					}
				}
				GUI.nouveauxattributsnom.addItem("null");
				boolean fenetrePrincipale = GUI.recupererRetourMeth == null 
				|| (!GUI.recupererRetourMeth.retourMeth.isVisible());
				boolean fenetrePrincipale2 = GUI.choose == null || (!GUI.choose.isVisible());
				if(GUI.etatCourant&&fenetrePrincipale&&fenetrePrincipale2){
					GUI.modifyattribute.setEnabled(true);
				}
			}
		}
	}

	/**
	 * rajoute dans la JComboBox types tous les types que l'utilisateur pourra creer
	 * et initialise les JComboBox constructeurs et methodes avec les constructeurs
	 * et methodes du premier type en appelant les methodes 
	 * refreshConstructeurs et refreshMethodes
	 */
	public static void initialiseTypeEtTypemethodes(){
		for(int i=0; i<envCourant.types.size(); i++){
			String nomSimpleType = envCourant.types.get(i).getSimpleName();
			GUI.type.addItem(nomSimpleType);
		}
		GUI.type.setSelectedIndex(0);
		GUI.refreshConstructeurs();
		GUI.refreshMethodes();
	}

	/**
	 * Lorsqu'une classe a ete selectionnee dans la JComboBox type, les noms simples des constructeurs
	 * declares dans cette classe seront stockes dans l'attribut constructeurs
	 */
	public static void refreshConstructeurs(){
		int choix = type.getSelectedIndex();
		constructeurs.removeAllItems();
		Constructor[] cstr = envCourant.types.get(choix).getConstructors();
		for(int i=0; i < cstr.length ; i++){
			constructeurs.addItem(Environnement.simpleConstructorName(cstr[i]));
		}
		if(GUI.constructeurs.getItemCount()!=0){
			GUI.constructeurs.setSelectedIndex(0);
			boolean fenetrePrincipale = GUI.recupererRetourMeth == null 
			|| (!GUI.recupererRetourMeth.retourMeth.isVisible());
			boolean fenetrePrincipale2 = GUI.choose == null || (!GUI.choose.isVisible());
			if(GUI.etatCourant&&fenetrePrincipale&&fenetrePrincipale2){
				GUI.create.setEnabled(true);
			}
		}else{
			GUI.create.setEnabled(false);
		}
	}

	/**
	 * Lorsqu'une classe a ete selectionnee dans la JComboBox type, les noms simples des methodes
	 * declarees dans cette classe seront stockees dans la JComboBox methodes
	 */
	public static void refreshMethodes(){
		int choix = type.getSelectedIndex();
		methodes.removeAllItems();
		Method[] mthds = envCourant.types.get(choix).getDeclaredMethods();
		for(int i=0; i < mthds.length ; i++){
			methodes.addItem(Environnement.simpleMethodName(mthds[i]));
		}
		if(GUI.methodes.getItemCount()!=0){
			GUI.methodes.setSelectedIndex(0);
		}
	}

	/**
	 * Lorsqu'une classe a ete selectionne dans la JComboBox type, les noms des variables de l'environnement
	 * sur lesquelles les methodes declarees dans la classe choisie peuvent etre appelees
	 * seront stockees dans la JComboBox devantmethode
	 */
	public static void refreshDevantmethode(){
		if((GUI.methodes.getItemCount()!=0)){
			GUI.devantmethode.removeAllItems();
			Class typechoisi = envCourant.types.get(GUI.type.getSelectedIndex());
			for(int i=0; i<envCourant.instances.size(); i++){
				Variable var_i = envCourant.instances.get(i);
				boolean accessible = Environnement.estAccessible(var_i);
				if(typechoisi.isAssignableFrom(var_i.type) && accessible){
					GUI.devantmethode.addItem(envCourant.instances.get(i).name);
				}
			}
			boolean fenetrePrincipale = GUI.recupererRetourMeth == null 
			||(!GUI.recupererRetourMeth.retourMeth.isVisible());
			boolean fenetrePrincipale2 = GUI.choose == null || (!GUI.choose.isVisible());
			if((GUI.devantmethode.getItemCount()!=0)&&(GUI.etatCourant)&&fenetrePrincipale&&fenetrePrincipale2){
				GUI.callmethod.setEnabled(true);
			}else{
				GUI.callmethod.setEnabled(false);
			}
		}
	}

	/**
	 * remet a jour les JComboBoxes variables, devantmethode et variables 
	 */
	public static void refreshComboBoxes(){
		GUI.refreshVariables();
		GUI.refreshDevantmethode();
		GUI.refreshPoigneesEtVariables();
		boolean fenetrePrincipale = GUI.recupererRetourMeth == null 
		|| (!GUI.recupererRetourMeth.retourMeth.isVisible());
		boolean fenetrePrincipale2 = GUI.choose == null || (!GUI.choose.isVisible());
		if(GUI.etatCourant && fenetrePrincipale && fenetrePrincipale2){
			if(GUI.constructeurs.getItemCount()!=0){
				GUI.create.setEnabled(true);
			}
			GUI.ramassemiette.setEnabled(true);
			GUI.rewind.setEnabled(true);
		}
	}

	/**
	 * copie les variables et les poignees de l'environnement courant.
	 * Ces copies sont respectivement ajoutees aux attributs
	 * instanceRecordingTape et pileRecordingTape de GUI.
	 * La chaine de caractere passee en argument est ajoutee a
	 * l'attribut commandesDeclenchees et est ensuite selectionnee
	 */
	public static void cloneMemoryState(String nouvelleLigneCode){
		GUI.instanceRecordingTape.add(envCourant.cloneInstances());
		GUI.pileRecordingTape.add(envCourant.clonePile());
		GUI.time++;
		GUI.commandesDeclenchees.addElement(nouvelleLigneCode);
		GUI.codeGenere.setSelectedIndex(GUI.time);
	}

	/**
	 * desactive tous les boutons de l'interface
	 */
	public static void deactivateButtons(){
		GUI.rewind.setEnabled(false);
		GUI.fastforward.setEnabled(false);
		GUI.create.setEnabled(false);
		GUI.modifyattribute.setEnabled(false);
		GUI.ramassemiette.setEnabled(false);
		GUI.callmethod.setEnabled(false);
	}

	/**
	 * active tous les boutons de l'interface 
	 */
	public static void activateButtons(){
		GUI.rewind.setEnabled(true);
		GUI.fastforward.setEnabled(true);
		GUI.create.setEnabled(true);
		GUI.modifyattribute.setEnabled(true);
		GUI.ramassemiette.setEnabled(true);
		GUI.callmethod.setEnabled(true);
	}

	public static void main(String[] args) throws InterruptedException {
		GUI gui = new GUI();

	}

}
