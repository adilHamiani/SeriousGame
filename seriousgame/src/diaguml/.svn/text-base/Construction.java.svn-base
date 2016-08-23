package diaguml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
le premier niveau de difficulte
 */

public class Construction {

	private static final long serialVersionUID = -8928982366041695471L;
	public static JFrame jframe;
	public static ArrayList<Etiquette> res;
	public static ArrayList<Liaison> res2;
	public static mxGraph graph;
	public static String texte;


	public Construction() throws ParserConfigurationException, SAXException, IOException{

		super();
		this.jframe = new JFrame("Débutant ");
		Start.choixNiveau = 1;
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

		ParseurXML parseur = new ParseurXML(fChooser.getSelectedFile().getPath());

		//Liste contenant toutes les etiquettes (ou cellules)
		res = (ArrayList<Etiquette>) parseur.getEtiquettes();

		//Liste contenant toute les liaisons bonnes entre les etiquettes
		res2 = (ArrayList<Liaison>) parseur.getLiaisons();

		texte = parseur.getText();
		System.out.println(texte);


		JTextArea textArea = new JTextArea(5,60);
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.append(texte);
		textArea.setEditable(false);
		content.add(scrollPane,BorderLayout.NORTH);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);

		graph = new mxGraph() {
			// Surcharge de methode pour rendre le nom des labels inchangeables (vertex et edges).

			public boolean isCellEditable(Object cell)
			{
				return false;
				//!getModel().isVertex(cell);
			}

		};


		final Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();


		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		jframe.getContentPane().add(graphComponent);
		graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt) {   

				Object cell = evt.getProperty("cell");
				System.out.println("edge="+graph.getLabel(cell));
				System.out.println("edge="+evt.getProperty("cell"));  }
		});

		//Repérer une connection et donner sa nature, ses roles et multiplicites
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{   
				mxCell edge = (mxCell)evt.getProperty("cell");

				mxICell source=edge.getSource();

				System.out.println("cell source="+graph.getLabel(source));
				mxICell target=edge.getTarget();
				System.out.println("cell target="+graph.getLabel(target));

				Etiquette et_dep = new Etiquette(graph.getLabel(source));
				Etiquette et_arr = new Etiquette(graph.getLabel(target));
				String nature_liaison = new String(" ");
				String mult1 = "";
				String mult2 = "";
				String role1 = "";
				String role2 = "";

				Liaison liaison = new Liaison(et_dep,et_arr);
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
				// on ajoute les roles et les multiplicites
				Object e = graph.insertEdge(parent, null, nature_liaison,source, target);
				graph.insertVertex(e, null, role1+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
				graph.insertVertex(e, null, role2+"\n"+mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);

				//Object[] edges = graph.getAllEdges(graph);
				// On recupere tous les cellules du graph (edges et vertex)
				Object[] cells = mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, true);
				System.out.println("Il ya tant de cellules"+cells.length);
				if( (cells.length - res.size())==res2.size() ){
					JOptionPane.showMessageDialog(null, "Bravo vous avez réussi à construire le bon diagramme."+
							"\n"+" Vous pouvez passer au niveau supérieur"); 
				}
				/*	try {graph.getModel().remove( edge);
				Object e =graph.insertEdge(parent, null, "herite",source, target);
				 graph.insertVertex(e, null, "1..n", -1, 0, 0, 0, "align=left;verticalAlign=top", true);
				 graph.insertVertex(e, null, "1", 1, 0, 0, 0, "align=right;verticalAlign=bottom", true);}
				finally{ graph.getModel().endUpdate();}*/

				//changer la couleur une fois la connection établie: unitile pour l'instant!!!
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

		// On positionne les cellules dans le graph en fonction de leur rang
		try
		{
			//int s =0;
			int x=0,y=0,z=0,t=0,w=0;
			for (int i=0; i<res.size(); i++){
				//s = i%2;
				if (res.get(i).getRang()==1){
					graph.insertVertex(parent, null, res.get(i).getNom(), 400, 30,
							160, 30,"ROUNDED");
				}
				else if (res.get(i).getRang()==2) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+z, 150 ,
							160, 30,"ROUNDED");
					z = z+300;
				}

				else if (res.get(i).getRang()==3) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+y, 300 ,
							160, 30,"ROUNDED");
					y=y+300;
				}

				else if (res.get(i).getRang()==4) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+x, 450 ,
							160, 30,"ROUNDED");
					x=x+300;
				}

				else if (res.get(i).getRang()==5) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+t, 600 ,
							160, 30,"ROUNDED");
					t=t+300;
				}

				else if (res.get(i).getRang()==6) {
					graph.insertVertex(parent, null, res.get(i).getNom(), 100+w, 750 ,
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
		new CustomKeyBoardHandler(graphComponent);

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

		jframe.getContentPane().add(graphComponent);		

	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		Construction figure = new Construction();
		figure.jframe.setSize(1000, 700);
	}
}
