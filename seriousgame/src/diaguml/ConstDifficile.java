package diaguml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pilegen.ChoixArguments;
import pilegen.GUI;
import pilegen.Variable;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;

/*
Cette classe contient le scenario implémentant 
le quatrieme niveau de difficulte
 */

public class ConstDifficile {

	private static final long serialVersionUID = -8928982366041695471L;
	public static JFrame jframe;
	public static ArrayList<Etiquette> res;
	public static ArrayList<Liaison> res2;
	public static mxGraph graph;
	public static String texte;
	public static JButton valider;
	public static JButton solution;
	public static JButton newcell;
	public static JButton checkcells;
	public static mxGraphComponent graphComponent;
	public static String chemin;
	public Object parent;
	public static ArrayList<Liaison> liaison_cree;
	public static JTextArea affichageErreur;




	public ConstDifficile() throws ParserConfigurationException, SAXException, IOException{

		super();
		this.jframe = new JFrame("Confirmé ");
		Start.choixNiveau = 4;
		jframe.setSize(1000, 1000);
		jframe.setLocationRelativeTo(null);
		Container content = jframe.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new BorderLayout()); 
		content.setSize(600, 600);
		JFileChooser fChooser = new JFileChooser("./xml");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
		fChooser.setFileFilter(filter);
		fChooser.showOpenDialog(null);
		System.out.println(fChooser.getSelectedFile().getPath());	
		chemin = fChooser.getSelectedFile().getPath();

		ParseurXML parseur = new ParseurXML(chemin);
		liaison_cree = new ArrayList<Liaison>();
		affichageErreur = new JTextArea(5,40);

		// Liste contenant toutes les etiquettes (classes)
		res = (ArrayList<Etiquette>) parseur.getEtiquettes();

		//Liste contenant toutes les liaison qui sont bonne
		res2 = (ArrayList<Liaison>) parseur.getLiaisons();

		texte = parseur.getText();
		System.out.println(texte);

		// le bouton valider permet de selectionner les liens incorrectes 
		// et les affiche dans une boite de dialogue ainsi que dans l'espace reserve en bas de l'ecran
		valider = new JButton("valider");
		valider.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {


				int taille = res.size();



				// On recupere toutes les cellules du graph arcs et noeuds
				// On les rend tous visibles au cas ou on cache les roles et multiplicite
				// lorsque les liaisons sont etblies

				/*	Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				for(int i=0;i<cells.length;i++){
					graph.getModel().setVisible(cells[i], true);

				}*/


				ArrayList<String> warning = new ArrayList<String>();
				ArrayList<String> sourceHeriteMult = new ArrayList<String>();
				for(int i=0;i<liaison_cree.size();i++){
					Liaison liaisonI = liaison_cree.get(i);
					boolean isCorrect = false;
					boolean heriteMult = false;
					for(int j=0; j<res2.size(); j++){
						Liaison liaisonCorrecteJ = res2.get(j);
						if(liaisonI.equals(liaisonCorrecteJ)){
							isCorrect = true;
						}
					}
					for(int j=0; j<liaison_cree.size(); j++){
						Liaison liaisonJ = liaison_cree.get(j);
						boolean sameSource = liaisonI.getEtDep().getId().equals(liaisonJ.getEtDep().getId());
						boolean sameTarget = liaisonI.getEtArr().getId().equals(liaisonJ.getEtArr().getId());
						if(sameSource && !sameTarget){
							heriteMult = true;
						}
					}
					if(!isCorrect){
						String nomSource = liaisonI.getEtDep().getNom();
						String nomArrivee = liaisonI.getEtArr().getNom();
						warning.add("La liaison entre "+nomSource+" et "+nomArrivee+" est incorrecte");
					}
					if(heriteMult){
						boolean existeDeja = false;
						for(int j=0; j<sourceHeriteMult.size(); j++){
							if(liaisonI.getEtDep().getId().equals(sourceHeriteMult.get(j))){
								existeDeja = true;
							}
						}
						if(!existeDeja){
							sourceHeriteMult.add(liaisonI.getEtDep().getId());
							String cellulesMeres = null;
							for(int j=0; j<liaison_cree.size(); j++){
								if(liaison_cree.get(j).getEtDep().getId().equals(liaisonI.getEtDep().getId())){
									if(cellulesMeres == null){
										cellulesMeres = liaison_cree.get(j).getEtArr().getNom();
									}else{
										cellulesMeres += ", "+liaison_cree.get(j).getEtArr().getNom();
									}
								}
							}
							warning.add("La cellule "+liaisonI.getEtDep().getNom()
									+" herite de plusieurs cellules : "+cellulesMeres);
						}
					}
				}
				String affiche ="";
				for (int k=0;k<warning.size();k++){
					affiche += warning.get(k)+"\n";

				}
				if(warning.size()>0){
					affichageErreur.setText(affiche);
					JOptionPane.showMessageDialog(jframe, "Veuillez corriger les erreurs suivantes :\n"+affiche,
							"Erreurs de construction", JOptionPane.ERROR_MESSAGE);
				}else{
					affichageErreur.setText("");
					JOptionPane.showMessageDialog(jframe, "Aucune erreur détectée\nVeuillez poursuivre la construction du diagramme");
				}
				graph.refresh();
				//((mxGraphComponent) graph.getModel()).refresh();

				Object[] cellules = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cellules.length);

				if( (cellules.length - res.size())==res2.size() ){
					if (Liaison.egalite_liaison(res2,liaison_cree)){
						JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
								"\n"+" Vous pouvez passer au niveau supérieur"); 
					}
				}

				graph.refresh();

				/*try{
					Thread.sleep(4000);//Ici une pause de 4 sec
				} catch(InterruptedException e) {
					graph.setMultiplicities(null);
				}*/

			}
		});

		/* Bouton pour mettre les noms des cellulles selon les conventions
		 de nommage des classes Java et determine les noms des classes qui n'ont
		 pas lieu d'etre dans le graph
		 */
		checkcells = new JButton("Verification noms cellule");
		checkcells.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				graph.getModel().beginUpdate();

				try
				{
					Object[] cells =  graph.getChildCells(graph.getDefaultParent(),true,true);
					System.out.println(cells.length+"taille du tab2");
					ArrayList<String> chaines = new ArrayList<String>();
					ArrayList<Etiquette> list_et = new ArrayList<Etiquette>();
					Boolean vide = false;

					for (Object vertex: cells){

						String s= graph.getLabel(vertex);
						if (s.length()==0 && ((mxCell) vertex).isVertex()) {
							vide = true;
						}
					}
					if (vide==true){

						JOptionPane.showMessageDialog(jframe, "Veuillez remplir toutes les cellules",
								"", JOptionPane.ERROR_MESSAGE);

					}

					if (vide == false){
						/* toutes les cellules ont un label non vide */
						for (Object vertex: cells){
							if ( ((mxCell) vertex).isVertex() ) {
								String s= graph.getLabel(vertex);
								//String s = graph.getLabel(vert.getSource());
								if(s.length()>0){
									s= s.replaceAll("\\s", "");
									s= s.replace("\r\n", "");
									if(s.length() == 1){
										s = s.substring(0,1).toUpperCase();
									}else if(s.length() > 1){
										s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
									}
								}
								Etiquette et = new Etiquette(s);
								list_et.add(et);
								graph.cellLabelChanged(vertex, s,false);
								if(liaison_cree!=null){
									for(int i=0; i<liaison_cree.size(); i++){
										Liaison liaisonI = liaison_cree.get(i);
										if(liaisonI.getEtDep().getId().equals(((mxCell) vertex).getId())){
											System.out.println("Ancienne valeur dep "+liaisonI.getEtDep().getNom());
											liaisonI.getEtDep().setNom(s);
											System.out.println("Nouvelle valeur dep "+liaisonI.getEtDep().getNom());
										}
										if(liaisonI.getEtArr().getId().equals(((mxCell) vertex).getId())){
											System.out.println("Ancienne valeur arr "+liaisonI.getEtArr().getNom());
											liaisonI.getEtArr().setNom(s);
											System.out.println("Nouvelle valeur arr "+liaisonI.getEtArr().getNom());
										}
									}
								}
							}
							//graph.cellLabelChanged(vertex, "",false);

						}
						for(int i=0;i<list_et.size();i++){
							Boolean trouve = false;
							for(Etiquette et : res){
								if (et.equals(list_et.get(i)) ) {
									trouve =true;
								}
							}
							if (trouve == false){
								chaines.add(list_et.get(i).getNom());
							}
						}
					}
					String resultat ="";

					for (String ch:chaines){
						resultat = resultat + ch+"\n";
					}



					if(resultat.length()!=0){

						JOptionPane.showMessageDialog(jframe, "Veuillez renommez les cellules suivantes"+"\n"+resultat,
								"Erreur nom des cellules", JOptionPane.ERROR_MESSAGE);
					}
				}



				finally
				{
					graph.getModel().endUpdate();
				}


			}
		});

		// Au clique sur ce bouton on affiche la solution
		solution = new JButton("solution");
		solution.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					Solution sol = new Solution(chemin);
					sol.jframe.setSize(1000, 700);
					sol.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					/*try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

					/*double timer = System.currentTimeMillis();
					while((System.currentTimeMillis()-timer)<5000){

					}*/
					//sol.jframe.dispose();

				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		//solution.setEnabled(false);

		// Bouton pour creer une nouvelle cellule
		newcell = new JButton("creer cellule");
		newcell.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				graph.getModel().beginUpdate();

				try
				{
					graph.insertVertex(parent, null,"Ecrire Nom", 10, 10, 160, 30,"ROUNDED");
				}
				finally
				{
					graph.getModel().endUpdate();
				}

			}
		});


		JTextArea textArea = new JTextArea(5,60);
		JScrollPane scrollPane = new JScrollPane(textArea);
		JScrollPane scrollPane2 = new JScrollPane(affichageErreur);
		textArea.setText(texte);
		textArea.setEditable(false);
		affichageErreur.setEditable(false);
		content.add(scrollPane, BorderLayout.NORTH);
		content.add(scrollPane2, BorderLayout.SOUTH);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);



		graph = new mxGraph() {
			// Surcharge de methode pour rendre le nom des labels inchangeables.
			public boolean isCellEditable(Object cell) {
				//return false;
				return getModel().isVertex(cell);
			}
		};


		parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();


		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			// A chaque clique sur une cellule (edge ou vertex) cette fonction est lancee
			public void invoke(Object sender, mxEventObject evt) {   
				//Object cell = graphComponent.getCellAt(evt.getX(), evt.getY());
				//evt.getProperties()
				Object cell = evt.getProperty("cell");
				System.out.println("edge="+graph.getLabel(cell));
				System.out.println("edge="+evt.getProperty("cell"));  }
		});

		//Repérer une connection et donner sa nature, roles et multiplicites
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{   
				mxCell edge = (mxCell)evt.getProperty("cell");

				mxICell source=edge.getSource();

				System.out.println("cell source="+graph.getLabel(source));
				mxICell target=edge.getTarget();
				System.out.println("cell target="+graph.getLabel(target));

				Etiquette et_dep = new Etiquette(source.getId(),graph.getLabel(source));
				Etiquette et_arr = new Etiquette(target.getId(),graph.getLabel(target));
				String nature_liaison = new String(" ");

				Liaison liaison = new Liaison(et_dep,et_arr);
				String mult1 = "";
				String mult2 = "";
				String role1 = "";
				String role2 = "";
				liaison_cree.add(liaison);

				for (int i=0; i<res2.size();i++){
					if (liaison.equals(res2.get(i))){
						nature_liaison = res2.get(i).getNature();
						//+res2.get(i).getRole()+"\n";

						mult1 = res2.get(i).getMult1();
						mult2 = res2.get(i).getMult2();
						role1 = res2.get(i).getRole1();
						role2 = res2.get(i).getRole2();
					}
				}
				// remove all cells
				// graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))

				graph.getModel().remove(edge);
				Object e = graph.insertEdge(parent, null, nature_liaison,source, target);
				Object g1 = graph.insertVertex(e, null, role1+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
				Object g2 = graph.insertVertex(e, null, role2+"\n"+mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);
				// Ici on rend les roles et multiplicites non visibles a mettre dans un try catch
				// avec un begin et end update du graph
				//((Component) g1).setVisible(false);
				//((Component) g2).setVisible(false);
				//graph.getModel().setVisible(g1, false);
				//graph.getModel().setVisible(g2, false);

				// On recupere toutes les cellules du graph arcs et noeuds
				Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cells.length);

				//if( (cells.length - res.size())==res2.size() ){
				if (Liaison.egalite_liaison(res2,liaison_cree)){
					JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
							"\n"+" Vous pouvez passer au niveau supérieur"); 
				}
				//}

				//changer la couleur une fois la connection établie: inutile pour l'instant!!!
				//mxConstants.PREVIEW_BORDER();
				//graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,"red", new Object[]{target});
				//graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,"green", new Object[]{target});
			}

		});

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				Object cell = graphComponent.getCellAt(e.getX(), e.getY());

				if (cell != null)
				{

					System.out.println("cell="+graph.getLabel(cell));
				}
			}
		});


		try
		{
			graph.insertVertex(parent, null, "Ecrire Nom", 20, 20,
					160, 30,"ROUNDED");

		}


		finally
		{
			graph.getModel().endUpdate();
		}

		// Ces actions permettent de rendre les edges indissociables de leur vertex
		// de meme un lien entre deux cellule ne s'etablit que s'il par d'une cellule
		// vers une autre sinon le lien ne s'effectue pas

		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
		graphComponent.setConnectable(true);
		graphComponent.setToolTips(true);

		//	// Enables rubberband selection
		new mxRubberband(graphComponent);
		//new CustomKeyBoardHandler(graphComponent);
		new mxKeyboardHandler(graphComponent);

		//	// Installs automatic validation (use editor.validation = true
		//	 //if you are using an mxEditor instance)
		graph.getModel().addListener(mxEvent.CHANGE, new mxIEventListener()
		{
			public void invoke(Object senr, mxEventObject evt)
			{
				graphComponent.validateGraph();
			}
		});

		// Initial validation
		graphComponent.validateGraph();

		//	Object[] list1 = graph.getAllEdges(multiplicities);
		Object[] list1 = graph.getChildCells(graph.getDefaultParent(),true,true);
		if(list1.length == res.size()){
			System.out.println("Vous avez construit une solution convenable du diagramme");
		}

		JPanel gauche = new JPanel();
		gauche.setLayout(new GridLayout(4,1));
		//gauche.setLayout(new FlowLayout());
		gauche.add(valider);
		gauche.add(solution);
		gauche.add(newcell);
		gauche.add(checkcells);
		content.add(gauche, BorderLayout.EAST);


		content.add(graphComponent, BorderLayout.CENTER);		

	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ConstDifficile figure = new ConstDifficile();
		figure.jframe.setSize(1000, 700);
	}
}


