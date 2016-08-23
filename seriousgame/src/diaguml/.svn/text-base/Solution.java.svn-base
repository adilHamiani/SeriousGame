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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;


/*
Cette classe contient le scenario implémentant la solution du diagramme
 */

public class Solution {

	private static final long serialVersionUID = -8928982366041695471L;
	public static JFrame jframe;
	public static ArrayList<Etiquette> res;
	public static ArrayList<Liaison> res2;
	public static mxGraph graph;


	public Solution(String chemin) throws ParserConfigurationException, SAXException, IOException{

		super();
		this.jframe = new JFrame("Solution ");

		jframe.setSize(1200, 700);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container content = jframe.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new BorderLayout()); 
		content.setSize(800, 800);

		ParseurXML parseur = new ParseurXML(chemin);

		res = (ArrayList<Etiquette>) parseur.getEtiquettes();

		res2 = (ArrayList<Liaison>) parseur.getLiaisons();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);

		graph = new mxGraph() {
			// Surcharge de methode pour rendre le nom des labels inchangeables.

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

		//Repérer une connection et donner sa nature, role et multiplicite
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

				Liaison liaison = new Liaison(et_dep,et_arr);
				String mult1 = "";
				String mult2 = "";


				for (int i=0; i<res2.size();i++){
					if (liaison.equals(res2.get(i))){
						nature_liaison = res2.get(i).getNature()+"\n"
								+res2.get(i).getRole1()+"\n"+res2.get(i).getRole2()+"\n";
						mult1 = res2.get(i).getMult1();
						mult2 = res2.get(i).getMult2();
					}
				}


				graph.getModel().remove(edge);
				Object e = graph.insertEdge(parent, null, nature_liaison,source, target);
				graph.insertVertex(e, null, "role1"+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
				graph.insertVertex(e, null, mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);

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
					graph.insertVertex(parent, null, res.get(i).getNom(), 400, 20,
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

			String nature_liaison = new String(" ");
			Object[] tab =  graph.getChildVertices(graph.getDefaultParent());

			//mxCell celule = (mxCell) tab[3];

			String mult1 = "";
			String mult2 = "";
			String role1 = "";
			String role2 = "";

			for (int i=0; i<res2.size();i++){

				/*nature_liaison = res2.get(i).getNature()+"\n"
						+res2.get(i).getRole()+"\n"+res2.get(i).getMult1()+res2.get(i).getMult2();*/
				nature_liaison = res2.get(i).getNature();
				//	+res2.get(i).getRole()+"\n";
				mult1 = res2.get(i).getMult1();
				mult2 = res2.get(i).getMult2();
				role1 = res2.get(i).getRole1();
				role2 = res2.get(i).getRole2();

				//	System.out.println(graph.getLabel(graph.getModel().getRoot())+"YOP SALUT3");
				String et_dep = res2.get(i).getEtDep().getNom();
				String et_arr = res2.get(i).getEtArr().getNom();
				// sortir la bonne cellule de dep et d'arrive de tab
				int j=0;
				while( !graph.getLabel(((mxCell) tab[j])).equals(et_dep) ){

					j=j+1;
				}

				mxCell cel_dep = (mxCell) tab[j];

				j=0;
				while( !graph.getLabel(((mxCell) tab[j])).equals(et_arr) ){

					j=j+1;
				}

				mxCell cel_arr = (mxCell) tab[j];

				Object e = graph.insertEdge(parent, null, nature_liaison, cel_dep, cel_arr);
				graph.insertVertex(e, null, role1+"\n"+mult1, -0.5, 0, 0, 0, "align=left;verticalAlign=top", true);
				graph.insertVertex(e, null, role2+"\n"+mult2, 0.5, 0, 0, 0, "align=right;verticalAlign=bottom", true);


			}

		}
		finally
		{
			graph.getModel().endUpdate();
		}

		int taille = res.size();

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

		Object[] tab2 = graph.getChildEdges(graph.getDefaultParent());
		System.out.println(tab2.length+"taille du tab2");

		Object[] list1 = graph.getAllEdges(multiplicities);

		System.out.println("Vous avez construit une solution convenable du diagramme"+list1.length);

		// Ces actions permettent de rendre les edges indissociables de leur vertex
		// de meme un lien entre deux cellule ne s'etablit que s'il par d'une cellule
		// vers une autre sinon le lien ne s'effectue pas

		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
		graphComponent.setConnectable(true);
		graphComponent.setToolTips(true);

		// Enables rubberband selection
		new mxRubberband(graphComponent);
		// desactive la suppression et la modification depuis le clavier
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

	/*public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		Solution figure = new Solution();
		figure.jframe.setSize(800, 800);
	}*/
}
