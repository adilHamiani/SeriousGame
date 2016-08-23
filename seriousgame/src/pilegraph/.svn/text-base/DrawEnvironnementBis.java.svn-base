package pilegraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.font.TextAttribute;

import javax.swing.JPanel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

/**
 * 
 * graphe representant l'environnement constitue de poignees et de variables,
 * chaque poignee pouvant pointer vers une variable
 * et chaque attribut de variable pouvant pointer vers une autre variable qui est sa valeur
 *
 */
public class DrawEnvironnementBis extends Component {
	
	private static final long serialVersionUID = 1L;
	/**
	 * longueur des noeuds representant une poignee
	 */
	public static int sizePoigneeX = 80;
	/**
	 * largeur des noeuds representant une poignee
	 */
	public static int sizePoigneeY = 16;
	/**
	 * offset horizontal de tous les noeuds representant des variables
	 */
	public static int offsetX = 150;
	/**
	 * offset vertical de tous les noeuds representant des variables
	 */
	public static int offsetY = 20;
	/**
	 * longueur des noeuds representant une variable
	 */
    public static int sizeRectX = 200;
    /**
     * ecart horizontal entre deux noeuds representant des variables
     */
    public static int ecartX = 70;
    /**
     * largeur d'un noeud representant une variable
     */
    public static int sizeRectY = 90;
    /**
     * ecart vertical entre deux noeuds representant des variables
     */
    public static int ecartY = 20;
    /**
     * Epaisseur accordee a chaque ligne sur laquelle un attribut d'une variable sera ecrit.
     * La largeur d'un noeud representant une variable sera (nombre d'attributs + 2)*sizeLineY 
     */
    public static int sizeLineY = 12;
    /**
     * representation graphique de l'environnement, avec chaque poignee et variable representee
     * par un noeud, et les liens entre les noeuds indiquant les valeurs des poignees ainsi
     * que les valeurs des attributs de chaque variable. Le mxGraphComponent sera ajoute
     * dans la JFrame homescreen de GUI afin que l'utilisateur puisse voir graphiquement
     * l'etat courant de l'environnement
     */
    public mxGraphComponent graphe;
    /**
     * diametre du noeud representant l'attribut d'une variable. Chaque attribut sera un noeud ayant
     * pour noeud parent le noeud representant la variable possedant cet attribut.
     * Selon la terminologie de JGraphX, chaque attribut sera donc un port du noeud representant
     * la variable possedant cet attribut.
     */
	final int PORT_DIAMETER = 20;
	/**
     * rayon du noeud representant l'attribut d'une variable. Chaque attribut sera un noeud ayant
     * pour noeud parent le noeud representant la variable possedant cet attribut. 
     * Selon la terminologie de JGraphX, chaque attribut sera donc un port du noeud representant
     * la variable possedant cet attribut.
     */
	final int PORT_RADIUS = PORT_DIAMETER / 2;
	
	public DrawEnvironnementBis(mxGraphComponent graphe) {
		super();
		this.graphe = graphe;
		BorderLayout layout = (BorderLayout) GUI.homescreen.getContentPane().getLayout();
		GUI.homescreen.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		GUI.homescreen.getContentPane().add(graphe, BorderLayout.CENTER);
		graphe.setToolTips(true);
	}
	
	public DrawEnvironnementBis() {
		super();
		mxGraph graph = new mxGraph() {
			public boolean isPort(Object cell) {
				mxGeometry geo = getCellGeometry(cell);
				return (geo != null) ? geo.isRelative() : false;
			}
			public String getToolTipForCell(Object cell){
				if (model.isEdge(cell)){
					return convertValueToString(model.getTerminal(cell, true)) + " -> " +
						convertValueToString(model.getTerminal(cell, false));
				}
				return super.getToolTipForCell(cell);
			}
			public boolean isCellEditable(Object cell){
				return false;
			}
			public boolean isCellFoldable(Object cell, boolean collapse){
				return false;
			}
		};
		/* style du Edge, ie liaison entre deux Vertices */
		Map<String, Object> style = new HashMap<String, Object>();
		style.put(mxConstants.STYLE_ROUNDED, true);
		style.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		style.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
		style.put(mxConstants.STYLE_FONTCOLOR, "#446299");
		mxStylesheet foo = new mxStylesheet();
		foo.setDefaultEdgeStyle(style);
		graph.setStylesheet(foo);
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
		graphComponent.setConnectable(true);
		graphComponent.setToolTips(true);
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try{
			/* on affiche les poignees */
			for(int i = 0; i<GUI.envCourant.pile.size();i++){
				Poignee pgnI = GUI.envCourant.pile.get(i);
				mxCell vi = (mxCell) graph.insertVertex(parent, null, pgnI.typepgn.getSimpleName()+" @"+pgnI.nom,
						0, i*sizePoigneeY, sizePoigneeX, sizePoigneeY, "");
				pgnI.idPoignee = vi.getId();
				vi.setConnectable(false);
				mxGeometry geo = graph.getModel().getGeometry(vi);
			}
			/* initialisation de l'attribut identifiantsPeres de chaque variable */
			for(int i=0; i<GUI.envCourant.instances.size(); i++){
				Variable varI = GUI.envCourant.instances.get(i);
				varI.identifiantsPeres = new ArrayList<String>();
				for(int j=0; j<varI.attributsPeres.size(); j++){
					varI.identifiantsPeres.add("");
				}
			}
			
			
			/* on affiche les variables */
			ArrayList<Integer> cpt = new ArrayList<Integer>();
			for(int k=0; k<GUI.envCourant.types.size(); k++){
				cpt.add(0);
			}
			for(int i = 0; i<GUI.envCourant.instances.size();i++){
				Variable var_i = GUI.envCourant.instances.get(i);
				int row = 0;
				boolean classeDessinable = false;
				for(int j = 0; j<GUI.envCourant.types.size(); j++){
					if(GUI.envCourant.types.get(j).getName().equals(var_i.type.getName())){
						row = j;
						classeDessinable = true;
					}
				}
				/* une variable dont le type ne figure pas parmi les classes dans env.types ne sera pas affichee */
				if(classeDessinable){
					int xdep = (row)*(sizeRectX+ecartX) + offsetX;
					int ydep = cpt.get(row)*sizeRectY + offsetY;
					cpt.set(row, cpt.get(row)+1);
					Field[] allFields = Environnement.getAllFields(var_i.type);
					/* identifiant du noeud ou vertex */
					String nomVar_i = var_i.name;
					String nomNoeudVar_i = var_i.type.getSimpleName()+" "+var_i.name;
					mxCell vi = (mxCell) graph.insertVertex(parent, null, nomNoeudVar_i,
							xdep, ydep, sizeRectX, sizeLineY*(allFields.length+2), "");
					vi.setConnectable(false);
					var_i.identifiant = vi.getId();
					mxGeometry geo = graph.getModel().getGeometry(vi);
					for(int j=0; j < allFields.length; j++){
						/* affichage de chaque attribut */
						Field attributJ = allFields[j];
						String typeatt = attributJ.getType().getSimpleName();
						String nomatt = attributJ.getName();
						String typeAttComplet = attributJ.getType().getName();
						
						int modifier = attributJ.getModifiers();
						String prefix = "";
						if(Modifier.isPublic(modifier)){
							prefix = "+";
						}else if(Modifier.isPrivate(modifier)){
							prefix = "-";
						}else if(Modifier.isProtected(modifier)){
							prefix = "#";
						}
						String suffix = "";
						/* affichage des valeurs des types primitifs */
						if(typeAttComplet.equals("java.lang.Integer") || typeAttComplet.equals("int")){
							if(GUI.etatCourant){
								try{
									suffix = " = "+attributJ.get(var_i.valeur);
								}catch(Exception e){}
							}else{
								ArrayList<Variable> valsVar = GUI.envCourant.varsContainField(var_i.name, attributJ.getName());
								/*System.out.println(valsVar.size()+" contiennent "+attributJ.getName()+" de "+var_i.name);
								for(int q=0; q<valsVar.size(); q++){
									System.out.println(valsVar.get(q).name+" vaut "+valsVar.get(q).valeur);
								}*/
								Variable valVar = GUI.envCourant.variableContenantAttribut(var_i.name, attributJ.getName());
								if(valVar!=null && valVar.valeur!=null){
									suffix = " = "+valVar.valeur;
								}
							}
						}else if(typeAttComplet.equals("java.lang.String")){
							if(GUI.etatCourant){
								try{
									suffix = " = \""+attributJ.get(var_i.valeur)+"\"";
								}catch(Exception e){}
							}else{
								Variable valVar = GUI.envCourant.getVariableByName(nomVar_i+"."+attributJ.getName());
								if(valVar!=null && valVar.valeur!=null){
									suffix = " = \""+valVar.valeur+"\"";
								}
							}
						}
						
						mxGeometry geoj = new mxGeometry(0.9, (new Double(j+1))/(new Double(allFields.length+1)),
								PORT_DIAMETER, PORT_DIAMETER);
						geoj.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
						geoj.setRelative(true);
						mxCell portj;
						if(Modifier.isStatic(modifier)){
							portj = new mxCell(null, geoj, "shape=ellipse;perimter=ellipsePerimeter;fillColor=blue");
						}else{
							portj = new mxCell(null, geoj, "shape=ellipse;perimter=ellipsePerimeter");
						}
						portj.setVertex(true);
						portj.setConnectable(false);
						graph.cellLabelChanged(portj, prefix+" "+attributJ.getName()+suffix, false);
						graph.addCell(portj, vi);
						
						Variable contientAttributJ = null;
						if(!GUI.envCourant.isPrimitif(attributJ.getType().getName())){
							contientAttributJ = GUI.envCourant.variableContenantAttribut(var_i.name, attributJ.getName());
						}else{
							contientAttributJ = GUI.envCourant.getVariableByName(nomVar_i+"."+attributJ.getName());
						}
						if(contientAttributJ!=null){
							/* System.out.println(contientAttributJ.name+" contient l'attribut "
									+attributJ.getName()+" de "+var_i.name); */
							int index = -1;
							for(int k=0; k<contientAttributJ.variablesMeres.size(); k++){
								boolean isVarMere = contientAttributJ.variablesMeres.get(k).name.equals(var_i.name);
								boolean isAttPere = contientAttributJ.attributsPeres.get(k).equals(attributJ.getName());
								if(isVarMere && isAttPere){
									index = k;
								}
							}
							if(index!=(-1)){
								/* System.out.println("Identifiant Pere de "+var_i.name+" vaut "+portj.getId());*/ 
								contientAttributJ.identifiantsPeres.set(index, portj.getId());
							}
						}
						
					}
				}
			}
			
			/* liens entre poignees et variables */
			Object[] cells =  graph.getChildCells(graph.getDefaultParent(),true,true);
			for(int i=0; i<GUI.envCourant.pile.size(); i++){
				Poignee poigneeI = GUI.envCourant.pile.get(i);
				Variable varPointeeI = poigneeI.pointeVers;
				if(varPointeeI!=null){
					Object noeudPoignee = null;
					Object noeudPointe = null;
					String pointname = varPointeeI.name;
					for (Object vertex: cells){
						if(((mxCell) vertex).getId().equals(varPointeeI.identifiant)){
							noeudPointe = vertex;
						}
						if(((mxCell) vertex).getId().equals(GUI.envCourant.pile.get(i).idPoignee)){
							noeudPoignee = vertex;
						}
					}
					if(noeudPoignee!=null && noeudPointe!=null){
						graph.insertEdge(parent, null, "", noeudPoignee, noeudPointe);
					}
				}
			}
			
			/* liens entre attributs et variables */
			for(int i = 0; i<GUI.envCourant.instances.size(); i++){
				Variable var_i = GUI.envCourant.instances.get(i);
				Object noeudVar_i = null;
				for(Object vertex : cells ){
					if(((mxCell) vertex).getId().equals(var_i.identifiant)){
						noeudVar_i = vertex;
					}
				}
				if((noeudVar_i!=null) && (GUI.envCourant.isDrawable(var_i.type.getName()))){
					/* System.out.println("Lien entre "+graph.getLabel(noeudVar_i)+" et ses attributs"); */
					for(int j=0; j<var_i.variablesMeres.size(); j++){
						Variable varMere = GUI.envCourant.getVariableByName(var_i.variablesMeres.get(j).name);
						Object noeudVarMere = null;
						for(Object vertex : cells){
							if(((mxCell) vertex).getId().equals(varMere.identifiant)){
								noeudVarMere = vertex;
							}
						}
						if(noeudVarMere!=null){
							Object[] cellsFils =  graph.getChildCells(noeudVarMere,true,true);
							String nomNoeudPere = varMere.name+"."+var_i.attributsPeres.get(j);
							/* System.out.println("Recherche du noeud de "+graph.getLabel(noeudVarMere)
									+" correspondant a l'attribut "+nomNoeudPere+" ?"); */
							for(Object vertex : cellsFils){
								if(((mxCell) vertex).getId().equals(var_i.identifiantsPeres.get(j))){	
									/* vertex est le noeud recherche */
									graph.insertEdge(parent, null, "", vertex, noeudVar_i);
								}else{
									/* vertex n'est pas le noeud recherche */
								}
							}
						}
					}
				}
			}
		}finally{
			graph.getModel().endUpdate();
			
		}
		BorderLayout layout = (BorderLayout) GUI.homescreen.getContentPane().getLayout();
		GUI.homescreen.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		GUI.homescreen.getContentPane().add(graphComponent, BorderLayout.CENTER);
		graphComponent.setToolTips(true);
		this.graphe = graphComponent;
		GUI.homescreen.revalidate();
	}

}

