package diaguml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
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

import pilegen.GUI;

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
  le deuxieme niveau de difficulte
 */
public class ConstAvancee {

	private static final long serialVersionUID = -8928982366041695471L;
	public static JFrame jframe;
	public static ArrayList<Etiquette> res;
	public static ArrayList<Liaison> res2;
	public static mxGraph graph;
	public static String texte;
	public static JButton solution;
	public static JButton newcell;
	public static JButton checkcells;
	public Object parent;
	public static String chemin;
	public static ArrayList<Liaison> liaison_cree;


	public ConstAvancee() throws ParserConfigurationException, SAXException, IOException{

		super();
		this.jframe = new JFrame("Intermédiaire ");
		Start.choixNiveau = 2;
		jframe.setSize(1200, 1000);
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

		res = (ArrayList<Etiquette>) parseur.getEtiquettes();

		res2 = (ArrayList<Liaison>) parseur.getLiaisons();

		texte = parseur.getText();
		System.out.println(texte);

		solution = new JButton("solution");
		solution.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					Solution sol = new Solution(chemin);
					sol.jframe.setSize(1000, 700);
					sol.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

					/*
					 Si on veut faire apparaitre la solution pendant un certain temps 
					 puis la faire disparaitre 
					 try {
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


		JTextArea textArea = new JTextArea(5,60);
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.append(texte);
		textArea.setEditable(false);
		content.add(scrollPane,BorderLayout.NORTH);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);

		graph = new mxGraph();/* {
			// Surcharge de methode pour rendre le nom des labels inchangeables.

			public boolean isCellEditable(Object cell)
			{
				//return false; pour rendre les noms des vertex et edges inchangeables
				//!getModel().isVertex(cell); pour rendre uniquement les noms des vertex inchangeables 
			}

		};*/


		final Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();


		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			// A chaque clique sur une cellule (edge ou vertex) cette fonction est lancee
			public void invoke(Object sender, mxEventObject evt) {   

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
				//graph.removeSelectionCell(edge);

				Etiquette et_dep = new Etiquette(graph.getLabel(source));
				Etiquette et_arr = new Etiquette(graph.getLabel(target));
				String nature_liaison = new String(" ");
				String mult1 = "";
				String mult2 = "";
				String role1 = "";
				String role2 = "";

				Liaison liaison = new Liaison(et_dep,et_arr);
				liaison_cree.add(liaison);

				for (int i=0; i<res2.size();i++){
					if (liaison.equals(res2.get(i))){
						nature_liaison = res2.get(i).getNature();

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
				graph.insertVertex(e, null, role1+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
				graph.insertVertex(e, null, role2+"\n"+mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);

				// On recupere toutes les cellules du graph arcs et noeuds
				Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cells.length);
				//if( (cells.length - res.size())==res2.size() ){
				if (Liaison.egalite_liaison(res2,liaison_cree)){
					JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
							"\n"+" Vous pouvez passer au niveau supérieur"); 
				}
				//}

				/*	try {graph.getModel().remove( edge);
					Object e =graph.insertEdge(parent, null, "herite",source, target);
					 graph.insertVertex(e, null, "1..n", -1, 0, 0, 0, "align=left;verticalAlign=top", true);
					 graph.insertVertex(e, null, "1", 1, 0, 0, 0, "align=right;verticalAlign=bottom", true);}
					finally{ graph.getModel().endUpdate();}*/

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

		// Placement des classes dans le diagramme selon leur rang
		try
		{
			//int s =0;
			int x=0,y=0,z=0,t=0,w=0;
			for (int i=0; i<res.size(); i++){
				//s = i%2;
				if (res.get(i).getRang()==1){
					graph.insertVertex(parent, null, res.get(i).getNom(), 400, 40,
							160, 30,"ROUNDED");
				}
				else if (res.get(i).getRang()==2) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+z, 200 ,
							160, 30,"ROUNDED");
					z = z+300;
				}

				else if (res.get(i).getRang()==3) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+y, 400 ,
							160, 30,"ROUNDED");
					y=y+300;
				}

				else if (res.get(i).getRang()==4) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+x, 600 ,
							160, 30,"ROUNDED");
					x=x+300;
				}

				else if (res.get(i).getRang()==5) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+t, 800 ,
							160, 30,"ROUNDED");
					t=t+300;
				}

				else if (res.get(i).getRang()==6) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+w, 1000 ,
							160, 30,"ROUNDED");
					w=w+300;
				}


			}

		}
		finally
		{
			graph.getModel().endUpdate();
		}

		int taille = res.size();

		// On etablit les regles des relations dans les mxMultiplicities

		mxMultiplicity[] multiplicities = new mxMultiplicity[taille];

		for (int j=0; j<res2.size(); j++){


			multiplicities[j] = new mxMultiplicity(true, res2.get(j).getEtDep().getNom(), null, null, 0,
					"1", Arrays.asList(new String[] { res2.get(j).getEtArr().getNom() }) ,"L'héritage multiple est interdit en java", "Ce n'est pas son supertype", true);


		}
		int i = 0;

		while (res.get(i).getRang() != 1){
			i=i+1;
		}

		multiplicities[taille-1] = new mxMultiplicity(true, res.get(i).getNom(), null, null, 0,
				"0", null,res.get(i).getNom()+"n'a pas de superclasse",null, true);


		Object[] tab2 =  graph.getChildCells(graph.getDefaultParent(),true,true);
		System.out.println(tab2.length+"taille du tab2");

		for (Object vertex: tab2){

			graph.cellLabelChanged(vertex, "",false);

		}


		graph.setMultiplicities(multiplicities);

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

		Object[] list1 = graph.getAllEdges(multiplicities);
		if(list1.length == taille){
			System.out.println("Vous avez construit une solution convenable du diagramme");
		}

		JPanel gauche = new JPanel();
		gauche.setLayout(new GridLayout(3,1));
		//gauche.setLayout(new FlowLayout());

		gauche.add(solution);
		gauche.add(newcell);
		gauche.add(checkcells);
		content.add(gauche, BorderLayout.EAST);


		content.add(graphComponent, BorderLayout.CENTER);	


	}



	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ConstAvancee figure = new ConstAvancee();
		figure.jframe.setSize(1000, 700);
	}
}
