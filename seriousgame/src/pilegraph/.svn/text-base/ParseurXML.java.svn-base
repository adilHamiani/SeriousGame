package pilegraph;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseurXML {

	String path;
	public Document dom;
	
	public ParseurXML(String path){
		this.path = path;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			File xmlFile = new File(path);
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(xmlFile);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public ArrayList<Class> extraitClassesDessinables(){
		ArrayList<Class> res = new ArrayList<Class>();
		Element docEle = dom.getDocumentElement();
		NodeList nodeListDraw = docEle.getElementsByTagName("Dessinable");
		if(nodeListDraw != null && nodeListDraw.getLength() > 0) {
			for(int i = 0 ; i < nodeListDraw.getLength();i++) {
				Element el = (Element)nodeListDraw.item(i);
				String className = el.getTextContent();
				try{
					Class classeDessinable = Class.forName(className);
					res.add(classeDessinable);
				}catch(Exception e){
					
				}
			}
		}
		return res;
	}
	
	public ArrayList<String> extraitClassesPrimitives(){
		ArrayList<String> res = new ArrayList<String>();
		Element docEle = dom.getDocumentElement();
		NodeList nodeListDraw = docEle.getElementsByTagName("Primitive");
		if(nodeListDraw != null && nodeListDraw.getLength() > 0) {
			for(int i = 0 ; i < nodeListDraw.getLength();i++) {
				Element el = (Element)nodeListDraw.item(i);
				String className = el.getTextContent();
				res.add(className);
			}
		}
		return res;
	}
	
	public static void main(String[] args){
		
		JFileChooser fChooser = new JFileChooser("./xml");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
		fChooser.setFileFilter(filter);
		fChooser.showOpenDialog(null);
		System.out.println(fChooser.getSelectedFile().getPath());	
		
		ParseurXML parseur = new ParseurXML(fChooser.getSelectedFile().getPath());
		
		ArrayList<Class> drawable = parseur.extraitClassesDessinables();
		ArrayList<String> primitifs = parseur.extraitClassesPrimitives();
		
		for(Class c : drawable){
			System.out.println("Classe dessinable "+c.getName());
			for(Field f : Environnement.getAllFields(c)){
				System.out.println(Modifier.toString(f.getModifiers())+" "+f.getType().getName()+" "+f.getName());
			}
		}
		for(String p : primitifs){
			System.out.println("Classe primitive "+p);
		}
	}
	
}
