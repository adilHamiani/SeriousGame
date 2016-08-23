package diaguml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParseurXML {

	String chemin;
	Document doc;

	public ParseurXML(String chemin_) throws ParserConfigurationException, SAXException, IOException{
		chemin = chemin_;

		File fXmlFile = new File(chemin);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

	}

	public List<Etiquette> getEtiquettes(){

		List<Etiquette> res = new ArrayList<Etiquette>(); 
		//NodeList a = (NodeList) doc.getElementsByTagName("etiquette");

		/*	for (int i=0; i<a.getLength(); i++){
			Element e = (Element) a.item(i);

			String nom = e.getAttribute("nom");
			int id = Integer.parseInt(e.getAttribute("id"));
			int rg = Integer.parseInt(e.getAttribute("rang"));
			/*NodeList b = (NodeList) e.getElementsByTagName("attribut");
			ArrayList<String> list_attributs = new ArrayList<String>();
			System.out.println("DIVING INTO LIST OF LENGTH "+b.getLength());
			for(int j=0; j<b.getLength(); j++){
				Element elt = (Element) b.item(j);
				System.out.println("ABOUT TO ADD A NEW THING "+elt.getAttribute("nomatr"));
				list_attributs.add(elt.getAttribute("nomatr"));
			}*/

		NodeList a = (NodeList) doc.getElementsByTagName("etiquette");
		for(int j=0; j<a.getLength(); j++){
			Element root = (Element) a.item(j);
			NodeList list = root.getChildNodes();
			ArrayList<String> list_attributs = new ArrayList<String>();
			String nom = root.getAttribute("nom");
			String id = (root.getAttribute("id"));
			int rg = Integer.parseInt(root.getAttribute("rang"));

			for (int i=1; i<list.getLength(); i=i+2){
				Element e = (Element) list.item(i);

				list_attributs.add(e.getAttribute("nom"));
			}

			Etiquette etiquette = new Etiquette(id,rg,nom,list_attributs);
			res.add(etiquette);

		}


		return res;
	}


	public List<Liaison> getLiaisons(){

		List<Liaison> res = new ArrayList<Liaison>(); 
		NodeList a = (NodeList) doc.getElementsByTagName("relation");

		for (int i=0; i<a.getLength(); i++){
			Element e = (Element) a.item(i);

			String et_dep = e.getAttribute("de");
			String et_ar =  e.getAttribute("a");
			String nat = e.getAttribute("nature");
			String role1 = e.getAttribute("role1");
			String role2 = e.getAttribute("role2");
			String multiplicite1 = e.getAttribute("multiplicite1");
			String multiplicite2 = e.getAttribute("multiplicite2");
			//int id = Integer.parseInt(e.getAttribute("id"));
			Liaison liaison = new Liaison(new Etiquette(et_dep), new Etiquette(et_ar),nat,
					role1,role2,multiplicite1,multiplicite2);
			res.add(liaison);

		}

		return res;
	}

	public String getText(){

		String res_int = new String(""); 
		NodeList a = (NodeList) doc.getElementsByTagName("texte");
		Element e = (Element) a.item(0);
		res_int = e.getAttribute("enonce");
		String res = "";
		int k=0;

		String[] texteParse = res_int.split("#");
		/*
    List<String> parts = new ArrayList<String>();
    int len = res_int.length();
    for (int i=0; i<len; i+=130)
    {
        parts.add(res_int.substring(i, Math.min(len, i + 130)));
    }
		 */
		System.out.println("Nb Chaines = "+texteParse.length);
		for(int i=0; i<texteParse.length;i++) {
			System.out.println("Chaine "+i+" = "+texteParse[i]);
			res += "\n"+texteParse[i];
		}
		//res = res_int;
		return res;
	}





}
