package diaguml;

import java.awt.BorderLayout;
import java.awt.Color;
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
le troisieme niveau de difficulte
 */
public class ConstAvancee2 {

	private static final long serialVersionUID = -8928982366041695471L;
	public static JFrame jframe;
	public static ArrayList<Etiquette> res;
	public static ArrayList<Liaison> res2;
	public static mxGraph graph;
	public static String texte;
	public static JButton valider;
	public static JButton solution;
	public static mxGraphComponent graphComponent;
	public static String chemin;
	public Object parent;
	public static ArrayList<Liaison> liaison_cree;
	public static JTextArea affichageErreur;




	public ConstAvancee2() throws ParserConfigurationException, SAXException, IOException{

		super();
		this.jframe = new JFrame("Intermédiaire superieur ");

		jframe.setSize(1000, 1000);
		jframe.setLocationRelativeTo(null);
		Start.choixNiveau=3;
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

		res = (ArrayList<Etiquette>) parseur.getEtiquettes();

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

				ArrayList<String> warning = new ArrayList<String>();
				ArrayList<String> sourceHeriteMult = new ArrayList<String>();
				for(int i=0;i<ConstAvancee2.liaison_cree.size();i++){
					Liaison liaisonI = ConstAvancee2.liaison_cree.get(i);
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

				Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cells.length);

				if( (cells.length - res.size())==res2.size() ){
					if (Liaison.egalite_liaison(res2,liaison_cree)){
						JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
								"\n"+" Vous pouvez passer au niveau supérieur"); 
					}
				}

				graph.refresh();

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
				return !getModel().isVertex(cell);
			}
		};


		parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();


		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt) {   

				Object cell = evt.getProperty("cell");
				System.out.println("edge="+graph.getLabel(cell));
				System.out.println("edge="+evt.getProperty("cell"));  }
		});

		//Reperer une connection et donner sa nature, roles et multiplicites
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{   
				mxCell edge = (mxCell)evt.getProperty("cell");
				mxICell source=edge.getSource();

				//graph.getLabel(source);
				System.out.println("cell source="+graph.getLabel(source));
				mxICell target=edge.getTarget();
				System.out.println("cell target="+graph.getLabel(target));
				//graph.removeSelectionCell(edge);

				Etiquette et_dep = new Etiquette(source.getId(),graph.getLabel(source));
				Etiquette et_arr = new Etiquette(target.getId(),graph.getLabel(target));
				String nature_liaison = new String(" ");
				String mult1 = "";
				String mult2 = "";
				String role1 = "";
				String role2 = "";

				Liaison liaison = new Liaison(et_dep,et_arr);
				liaison_cree.add(liaison);

				for (int i=0; i<res2.size() ;i++){
					if (liaison.equals(res2.get(i))){

						nature_liaison = res2.get(i).getNature();

						mult1 = res2.get(i).getMult1();
						mult2 = res2.get(i).getMult2();
						role1 = res2.get(i).getRole1();
						role2 = res2.get(i).getRole2();
						graph.getModel().remove(edge);
						Object e = graph.insertEdge(parent, null, nature_liaison,source, target);
						Object g1 = graph.insertVertex(e, null, role1+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
						Object g2 = graph.insertVertex(e, null, role2+"\n"+mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);

					}
				}
				// On recupere toutes les cellules du graph arcs et noeuds
				Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cells.length);

				//if( (cells.length - res.size())==res2.size() ){
				if (Liaison.egalite_liaison(res2,liaison_cree)){
					JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
							"\n"+" Vous pouvez passer au niveau supérieur"); 
				}
				//}


				//changer la couleur une fois la connection établie: unitile pour l'instant!!!
				//mxConstants.PREVIEW_BORDER();
				//graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,"red", new Object[]{target});
				//graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,"green", new Object[]{target});
			}

		});

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			// Au clique sur une cellule cette fonction est lancee
			public void mouseReleased(MouseEvent e) {
				Object cell = graphComponent.getCellAt(e.getX(), e.getY());

				if (cell != null)
				{

					System.out.println("cell="+graph.getLabel(cell));
				}
			}
		});

		// Placement des classes dans le diagramme selon leur rang
		try
		{
			//int s =0;
			int x=0,y=0,z=0,t=0,w=0;
			for (int i=0; i<res.size(); i++){
				//s = i%2;
				if (res.get(i).getRang()==1){
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600,
							160, 30,"ROUNDED");
				}
				else if (res.get(i).getRang()==2) {
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600,
							160, 30,"ROUNDED");
					z = z+300;
				}

				else if (res.get(i).getRang()==3) {
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600 ,
							160, 30,"ROUNDED");
					y=y+300;
				}

				else if (res.get(i).getRang()==4) {
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600 ,
							160, 30,"ROUNDED");
					x=x+300;
				}

				else if (res.get(i).getRang()==5) {
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600 ,
							160, 30,"ROUNDED");
					t=t+300;
				}

				else if (res.get(i).getRang()==6) {
					graph.insertVertex(parent, null, res.get(i).getNom(), Math.random()*600, Math.random()*600 ,
							160, 30,"ROUNDED");
					w=w+300;
				}


			}

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
		gauche.setLayout(new GridLayout(2,1));
		//gauche.setLayout(new FlowLayout());
		gauche.add(valider);
		gauche.add(solution);

		content.add(gauche, BorderLayout.EAST);


		content.add(graphComponent, BorderLayout.CENTER);		

	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ConstAvancee2 figure = new ConstAvancee2();
		figure.jframe.setSize(1000, 700);
	}
}

