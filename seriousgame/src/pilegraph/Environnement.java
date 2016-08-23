package pilegraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Environnement {

	/**
	 *  ensemble des poignees de l'environnement 
	 */
	public ArrayList<Poignee> pile;
	/**
	 * classes dont l'utilisateur pourra creer des instances 
	 */
	public ArrayList<Class> types;
	/** 
	 * types consideres comme etant primitifs 
	 */
	public ArrayList<String> typesprimitifs;
	/** 
	 * ensemble des variables creees 
	 */
	public ArrayList<Variable> instances;
	/** 
	 * nombre de poignees dans l'environnement courant. Cet entier servira a generer
	 * les noms des variables creees. 
	 */
	public int cptPoignees;
	
	public Environnement(){
		this.pile = new ArrayList<Poignee>();
		this.types = new ArrayList<Class>();
		this.typesprimitifs = new ArrayList<String>();
		this.instances = new ArrayList<Variable>();
		this.cptPoignees = 0;
	}
	
	public Environnement(ArrayList<Poignee> pile, ArrayList<Class> types,
			ArrayList<String> typesprimitifs, ArrayList<Variable> instances,
			int cptPoignees) {
		super();
		this.pile = pile;
		this.types = types;
		this.typesprimitifs = typesprimitifs;
		this.instances = instances;
		this.cptPoignees = cptPoignees;
	}
	
	/** 
	 * creation d'une variable a partir d'un constructeur et des valeurs
	 * de ses arguments, puis stockage de la variable creee dans l'environnement 
	 */
	public void createVariable(Constructor constr, Object[] cstrargs){
		try{
			String nomvar = constr.getDeclaringClass().getSimpleName()+cptPoignees;
			Object valeur = constr.newInstance(cstrargs);
			Variable newvar = new Variable(nomvar, valeur);
			String nompgn = "p"+cptPoignees;
			Poignee newpgn = new Poignee(newvar, nompgn, cptPoignees);
			newvar.poignees.add(newpgn);
			this.pile.add(newpgn);
			this.instances.add(newvar);
			this.addAttributesToInstances(newvar);
			if(this.hasStaticField(newvar)){
				this.refreshRelatedVariables(newvar);
			}
			cptPoignees++;
		}catch(Exception e){
			System.out.println("L'appel du constructeur "+constr.toString()+" n'a pas fonctionne");
		}
	}
	
	/** 
	 * renvoie true si et seulement si classname figure parmi les classes
	 * contenues dans l'attribut types de l'environnement. La classe sera alors
	 * affichee dans le graphe de DrawEnvironnementBis 
	 */
	public boolean isDrawable(String classname){
		boolean res = false;
		for(int k=0; k<this.types.size(); k++){
			if(this.types.get(k).getName().equals(classname)){
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * renvoie true si et seulement si classname figure parmi les noms de classes
	 * contenus dans l'attribut typesprimitifs de l'environnement. La classe est alors
	 * consideree comme etant primitive.
	 */
	public boolean isPrimitif(String classname){
		boolean isprimitif = false;
		for(int k=0; k<this.typesprimitifs.size(); k++){
			if(this.typesprimitifs.get(k).equals(classname)){
				isprimitif = true;
			}
		}
		//boolean res = isprimitive || isprimitif;
		return isprimitif;
	}
	
	/**
	 * renvoie true si et seulement si l'utilisateur peut taper manuellement
	 * la valeur d'une variable du type fourni en argument. Les types pouvant etre fournis
	 * manuellement sont Integer, int, Double, double et String
	 */
	public static boolean isTypable(Class type){
		boolean res = false;
		String nom = type.getSimpleName();
		String[] typable = {"Integer", "int", "Double", "double", "String"};
		for(String s : typable){
			if(nom.equals(s)){
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * renvoie le nom simple de la methode constitue de :
	 * visibilite de la methode, nom simple du type de retour, nom de la methode,
	 * puis nom simple du type de chaque argument
	 */
	public static String simpleMethodName(Method meth){
		String methstring ;
		String methmod = Modifier.toString(meth.getModifiers());
		String methret = meth.getReturnType().getSimpleName();
		String methname = meth.getName();
		String methargs = "(";
		for(int i=0; i<meth.getParameterTypes().length; i++){
			if(i==0){
				methargs += meth.getParameterTypes()[i].getSimpleName();
			}else{
				methargs += ","+meth.getParameterTypes()[i].getSimpleName();
			}
		}
		methargs += ")";
		methstring = methmod+" "+methret+" "+methname+methargs;
		return methstring;
	}
	
	/**
	 * renvoie le nom simple du constructeur constitue de :
	 * visibilite du constructeur, nom du type cree,
	 * puis nom simple du type de chaque argument
	 */
	public static String simpleConstructorName(Constructor constr){
		String constrstring ;
		String constrmod = Modifier.toString(constr.getModifiers());
		String constrname = constr.getDeclaringClass().getSimpleName();
		String constrargs = "(";
		for(int i=0; i<constr.getParameterTypes().length; i++){
			if(i==0){
				constrargs += constr.getParameterTypes()[i].getSimpleName();
			}else{
				constrargs += ","+constr.getParameterTypes()[i].getSimpleName();
			}
		}
		constrargs += ")";
		constrstring = constrmod+" "+constrname+constrargs;
		return constrstring;
	}
	
	/**
	 * renvoie le nom d'une variable de l'environnement debutant par le nom
	 * d'une poignee a laquelle elle est liee soit directement, soit indirectement
	 * en passant par des attributs d'autres variables, comme par exemple poignee1.attributA.attributB
	 */
	public static String trueVariableName(Variable var){
		if(var==null){
			return "null";
		}else{
			if(var.poignees.size()!=0){
				Random rand = new Random();
				int index = rand.nextInt(var.poignees.size());
				return var.poignees.get(index).nom;
			}else{
				if(var.variablesMeres.size()!=0){
					int i=0;
					boolean found = false;
					while((i<var.variablesMeres.size()) && (!found)){
						if(estAccessible(var.variablesMeres.get(i))){
							found = true;
						}else{
							i++;
						}
					}
					if(found){
						String prefixe = trueVariableName(var.variablesMeres.get(i));
						String suffixe = "."+var.attributsPeres.get(i);
						return prefixe+suffixe;
					}else{
						return null;
					}
				}else{
					return null;
				}
			}
		}
	}
	
	/**
	 * ajoute dans l'environnement tous les attributs de la variable var qui sont non primitifs 
	 * et non nulls et qui ne sont pas deja presents dans l'environnement, 
	 * puis appel recursif de addAttributesToInstances sur tout attribut non primitif et non null,
	 * peu importe qu'il soit deja present ou non dans l'environnement 
	 */
	public void addAttributesToInstances(Variable var){
		if(!this.isPrimitif(var.type.getName())){
			Field[] allFields = Environnement.getAllFields(var.type);
			for(int k=0; k<allFields.length; k++){
				Field attribut_k = allFields[k];
				try{
					/* il faut verifier si l'attribut n'est pas partage avec une autre variable 
					 * dans ce cas il ne faudra pas creer de nouvelle variable */
					Object valeurAttribut_k = attribut_k.get(var.valeur);
					if(valeurAttribut_k!=null){
						boolean alreadythere = false;
						int alreadyIndex = 0;
						for(int p=0; p<this.instances.size(); p++){
							if(this.instances.get(p).valeur == valeurAttribut_k){
								alreadythere = true;
								alreadyIndex = p;
							}
						}
						if(!alreadythere){
							/* l'attribut est nouveau donc on l'ajoute a la liste des variables qui seront affichees */
							/* attributsPeres indique le nom de l'attribut afin de pouvoir ensuite 
							 * afficher correctement le lien */
							Variable newvar = new Variable(attribut_k.getType(), var.name+"."+attribut_k.getName(), 
									valeurAttribut_k);
							newvar.variablesMeres.add(var);
							newvar.attributsPeres.add(attribut_k.getName());
							this.instances.add(newvar);
							this.addAttributesToInstances(newvar);
							/*System.out.println("Attribut "+attribut_k.getName()+" de "+var.name
									+" formant nouvelle variable "+newvar.valeur);*/
						}else{
							/* L'attribut existe deja dans l'environnement, il obtient donc une variableMere 
							 * et un attributPere supplementaires */
							Variable contientAttribut = this.instances.get(alreadyIndex);
							contientAttribut.variablesMeres.add(var);
							contientAttribut.attributsPeres.add(attribut_k.getName());
							/*System.out.println("L'attribut "+attribut_k.getName()+" de "
									+var.name+" existe deja = "+contientAttribut.valeur);*/
							this.addAttributesToInstances(contientAttribut);
						}
					}
				}catch(Exception e){
					System.out.println("Impossible d'acceder a l'attribut "+attribut_k.getName()
							+" de la variable "+var.name);
				}
			}
		}else{
			/*System.out.println("La classe "+var.type.getName()+" est primitive");*/
		}
	}
	
	/**
	 * mise a null de l'attribut nomAtt de la variable nomVar
	 */
	public void setAttributeNull(String nomVar, String nomAtt){
		Variable var = this.getVariableByName(nomVar);
		if(var!=null){
			/* recherche du Field correspondant au String nomAtt designant d'attribut */
			Field[] attributsVar = Environnement.getAllFields(var.type);
			Field attributRecherche = null;
			for(int i=0; i<attributsVar.length; i++){
				if(attributsVar[i].getName().equals(nomAtt)){
					attributRecherche = attributsVar[i];
				}
			}
			if(attributRecherche!=null){
				/* il faut maintenant trouver dans l'environnement quelle variable contient la valeur
				 * de l'attribut nomAtt de la variable nomVar */
				try{
					Object valeurAtt = attributRecherche.get(var.valeur);
					attributRecherche.set(var.valeur, null);
					Variable contientAtt = null;
					for(int i=0; i<this.instances.size(); i++){
						if(this.instances.get(i).valeur == valeurAtt){
							contientAtt = this.instances.get(i);
						}
					}
					if(contientAtt!=null){
						int i=0;
						while(i<contientAtt.variablesMeres.size()){
							if(contientAtt.variablesMeres.get(i).name.equals(nomVar) && 
									contientAtt.attributsPeres.get(i).equals(nomAtt)){
								contientAtt.variablesMeres.remove(i);
								contientAtt.attributsPeres.remove(i);
							}else{
								i++;
							}
						}
					}
					if(Modifier.isStatic(attributRecherche.getModifiers())){
						this.refreshRelatedVariables(var);
					}
				}catch(Exception e){
					System.out.println("Impossible de mettre "+nomVar+"."+nomAtt+" a null");
				}
			}
		}
	}
	
	/**
	 * modifie la valeur de l'attribut attributeName de la variable hasAttribute en lui
	 * affectant pour valeur la variable newAttribute
	 */
	public void changeAttribute(String attributeName, Variable hasAttribute, Variable newAttribute){
		Field attribute = null;
		Field[] allFields = Environnement.getAllFields(hasAttribute.type);
		/* recherche du Field correspondant au String attributeName */
		for(int i=0; i<allFields.length; i++){
			if(allFields[i].getName().equals(attributeName)){
				attribute = allFields[i];
			}
		}
		if(attribute!=null && attribute.getType().isAssignableFrom(newAttribute.type)){
			/* il faut trouver la Variable correspondant a l'attribut attributeName de hasAttribute */
			try{
				/* il faut retirer hasAttribute de l'attribut classesMeres de la variable ayant pour valeur
				 * l'attribut attributeName de hasAttribute */
				Variable oldAttribute = null;
				for(int i=0; i<this.instances.size(); i++){
					Variable var_i = this.instances.get(i);
					for(int j=0; j<var_i.variablesMeres.size(); j++){
						if(var_i.variablesMeres.get(j).name.equals(hasAttribute.name) 
								&& var_i.attributsPeres.get(j).equals(attribute.getName())){
							oldAttribute = var_i;
						}
					}
				}
				if(oldAttribute!=null){
					/* la variable ayant pour valeur hasAttribute.valeur.attributeName existe bien */
					this.retireVariableMere(attributeName, hasAttribute, oldAttribute);
				}
				/* Il ne faut pas pouvoir ajouter deux fois le meme attributPere d'une meme variableMere.
				 * comme on applique retireVariableMere, ce cas n'arrivera jamais. */
				/* On peut ajouter plusieurs fois une meme variable mere lorsque 
				 * deux attributs d'une variable mere de meme type mais de noms differents
				 * pointent vers la meme valeur par exemple */
				newAttribute.variablesMeres.add(hasAttribute);
				newAttribute.attributsPeres.add(attributeName);
				attribute.set(hasAttribute.valeur, newAttribute.valeur);
				if(Modifier.isStatic(attribute.getModifiers())){
					this.refreshRelatedVariables(hasAttribute);
				}
			}catch(Exception e){
				System.out.println("L'attribut "+attributeName+" de la variable "
						+hasAttribute.name+" n'a pas pu etre modifie");
			}
		}
	}
	
	/**
	 * renvoie pour chaque argument d'un constructeur toutes les variables de type
	 * compatible dans l'environnement stockes dans une liste.
	 * Comme pour chaque argument une ArrayList<Variable> sera determinee,
	 * le retour sera une ArrayList<ArrayList<Variable>>
	 */
	public ArrayList<ArrayList<Variable>> getConstructorArguments(Constructor cstr){
		ArrayList<ArrayList<Variable>> res = new ArrayList<ArrayList<Variable>>();
		Class[] typeargs = cstr.getParameterTypes();
		if(typeargs.length>0){
			for(int i=0; i<typeargs.length; i++){
				ArrayList<Variable> vars_i = new ArrayList<Variable>();
				Class class_arg_i = typeargs[i];
				for(int j=0; j<this.instances.size(); j++){
					//if(this.instances.get(j).type.getName().equals(class_arg_i.getName())){
					if(this.instances.get(j).type.isAssignableFrom(class_arg_i)){
						vars_i.add(this.instances.get(j));
					}
				}
				res.add(vars_i);
			}
		}
		return res;
	}
	
	/**
	 * renvoie pour chaque argument d'un constructeur ou d'une methode toutes les variables de type
	 * compatible dans l'environnement stockes dans une liste.
	 * Comme pour chaque argument une ArrayList<Variable> sera determinee,
	 * le retour sera une ArrayList<ArrayList<Variable>>
	 */
	public ArrayList<ArrayList<Variable>> getMethodOrConstructorArguments(Class[] typesArguments){
		ArrayList<ArrayList<Variable>> res = new ArrayList<ArrayList<Variable>>();
		if(typesArguments.length>0){
			for(int i=0; i<typesArguments.length; i++){
				ArrayList<Variable> vars_i = new ArrayList<Variable>();
				Class typeArgument_i = typesArguments[i];
				for(int j=0; j<this.instances.size(); j++){
					if(typeArgument_i.isAssignableFrom(this.instances.get(j).type)){
						vars_i.add(this.instances.get(j));
					}
				}
				res.add(vars_i);
			}
		}
		return res;
	}
	
	/** 
	 * supprime les liens entre une variable et tous ses attributs et realise ensuite 
	 * la meme chose recursivement a chacun de ces attributs qui est de type non primitif.
	 * Renvoie la liste des attributs supprimes 
	 */
	public ArrayList<Variable> linkedAttributes(Variable culprit, boolean start){
		ArrayList<Variable> ret = new ArrayList<Variable>();
		for(int i=0; i<this.instances.size(); i++){
			Variable var_i = this.instances.get(i);
			if(start){
				int j=0;
				boolean islinked = false;
				while(j<var_i.variablesMeres.size()){
					if(var_i.variablesMeres.get(j).name.equals(culprit.name)){
						islinked = true;
						var_i.variablesMeres.remove(j);
						var_i.attributsPeres.remove(j);
					}else{
						j++;
					}
				}
				if(islinked){
					ret.add(var_i);
					ArrayList<Variable> ret_rec = this.linkedAttributes(var_i, true);
					for(int k=0; k<ret_rec.size(); k++){
						ret.add(ret_rec.get(k));
					}
				}
			}else{
				boolean islinked = false;
				for(int j=0; j<var_i.variablesMeres.size(); j++){
					if(var_i.variablesMeres.get(j).name.equals(culprit.name)){
						islinked = true;
					}
				}
				if(islinked){
					ret.add(var_i);
					ArrayList<Variable> ret_rec = this.linkedAttributes(var_i, false);
					for(int k=0; k<ret_rec.size(); k++){
						ret.add(ret_rec.get(k));
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * renvoie la liste des attributs d'une variable culprit.
	 * On ajoute ensuite recursivement a cette liste les attributs de chaque attribut. 
	 */
	public ArrayList<Variable> removeAttributesFromInstances(Variable culprit, boolean start){
		ArrayList<Variable> ret = new ArrayList<Variable>();
		if(!this.isPrimitif(culprit.type.getName())){
			boolean reset = false;
			int i = 0;
			while(i<this.instances.size()){
				reset = false;
				boolean islinked = false;
				int j=0;
				Variable var_i = this.instances.get(i);
				while(j<var_i.variablesMeres.size()){
					if(var_i.variablesMeres.get(j).name.equals(culprit.name)){
						islinked = true;
						var_i.variablesMeres.remove(j);
						var_i.attributsPeres.remove(j);
						if(j<=i){
							reset = true;
						}
					}else{
						j++;
					}
				}
				if(islinked){
					ret.add(var_i);
					ArrayList<Variable> ret_rec = this.removeAttributesFromInstances(var_i, false);
					for(int k=0; k<ret_rec.size(); k++){
						ret.add(ret_rec.get(k));
					}
				}
				if(reset){
					i=0;
				}else{
					i++;
				}
			}
		}
		return ret;
	}
	
	/**
	 * callMethodUpon appelle la methode meth avec les arguments methargs sur la variable upon.
	 * Comme la methode peut avoir des effets de bord sur la variable et ses attributs,
	 * on supprime les liens entre la variable et ses attributs en appelant la methode linkedAttributes .
	 * Les attributs de la variable seront ensuite rajoutes a l'environnement 
	 * apres l'appel de la methode meth sur upon en appelant la methode addAttributesToInstances.
	 * si la methode possede un attribut declare en static, afin de prendre en compte
	 * la modification potentielle de cet attribut qui se fera sur toutes les instances
	 * possedant ce meme attribut en static, il faudra supprimer puis rajouter
	 * les attributs de chaque variable possedant cet attribut static.
	 * La methode refreshRelatedVariables realise ceci.
	 */
	public Object callMethodUpon(Method meth, Variable upon, Object[] methargs){
		try{
			ArrayList<Variable> motherVariables = upon.variablesMeres;
			ArrayList<String> fatherAttributes = upon.attributsPeres;
			/* comme la methode peut avoir des effets de bord sur la variable et ses attributs,
			 * on supprime les liens entre la variable et ses attributs 
			 * en appelant la methode linkedAttributes .
			 * Les attributs de la variable seront ensuite rajoutes a l'environnement
			 * en appelant la methode addAttributesToInstances */
			ArrayList<Variable> linked_atts = this.linkedAttributes(upon, true);
			Object retourMethode = meth.invoke(upon.valeur, methargs);
			this.addAttributesToInstances(upon);
			if(this.hasStaticField(upon)){
				/* si la methode possede un attribut declare en static, afin de prendre en compte
				 * la modification potentielle de cet attribut qui se fera sur toutes les instances
				 * possedant ce meme attribut en static, il faudra supprimer puis rajouter
				 * les attributs de chaque variable possedant cet attribut static.
				 * La methode refreshRelatedVariables realise ceci */
				this.refreshRelatedVariables(upon);
			}
			return retourMethode;
		}catch(Exception e){
			System.out.println("Echec d'appel de la methode "+upon.toString()+" sur variable "+upon.name);
			Object ret = null;
			return ret;
		}
	}
	
	/**
	 * recupere de l'environnement la variable ayant pour nom namevar
	 */
	public Variable getVariableByName(String namevar){
		Variable res = null;
		for(int k = 0; k<this.instances.size(); k++){
			if(this.instances.get(k).name.equals(namevar)){
				res = this.instances.get(k);
			}
		}
		return res;
	}
	
	/**
	 * recupere de l'environnement la classe ayant pour nom classname
	 */
	public Class getClasseByName(String classname){
		Class res = null;
		int k = 0;
		boolean found = false;
		while((k<this.types.size()) && (!found)){
			if(this.types.get(k).getName().equals(classname)){
				res = this.types.get(k);
				found = true;
			}
			k++;
		}
		return res;
	}
	
	/**
	 * recupere de l'environnement la poignee ayant pour nom namepgn
	 */
	public Poignee getPoigneeByName(String namepgn){
		Poignee res = null;
		for(int k = 0; k<this.pile.size(); k++){
			if(this.pile.get(k).nom.equals(namepgn)){
				res = this.pile.get(k);
			}
		}
		return res;
	}
	
	/**
	 * affecte la poignee ayant pour nom namepgnleft la valeur pointee 
	 * par la poignee ayant pour nom namepgnright
	 */
	public void equalsPoigneePoignee(String namepgnleft, String namepgnright){
		Poignee pgnleft = this.getPoigneeByName(namepgnleft);
		Poignee pgnright = this.getPoigneeByName(namepgnright);
		if((pgnleft!=null) && (pgnright!=null) && (pgnleft.typepgn.isAssignableFrom(pgnright.typepgn))){
			if(pgnright.pointeVers!=null){
				if(pgnleft.pointeVers!=null){
					pgnleft.pointeVers.retirePoignee(pgnleft.nom);
				}
				pgnright.pointeVers.poignees.add(pgnleft);
				pgnleft.pointeVers = pgnright.pointeVers;
			}else{
				if(pgnleft.pointeVers!=null){
					pgnleft.pointeVers.retirePoignee(pgnleft.nom);
					pgnleft.pointeVers = null;
				}
			}
		}
	}
	
	/**
	 * retire des attributs variablesMeres et attributsPeres de la variable varFille
	 * respectivement la variable varMere et la chaine nomattribut
	 */
	public void retireVariableMere(String nomattribut, Variable varMere, Variable varFille){
		int i = 0;
		while(i<varFille.variablesMeres.size()){
			if(varFille.variablesMeres.get(i).name.equals(varMere.name) 
					&& varFille.attributsPeres.get(i).equals(nomattribut)){
				varFille.variablesMeres.remove(i);
				varFille.attributsPeres.remove(i);
			}else{
				i++;
			}
		}
	}
	
	/**
	 * affecte la poignee ayant pour nom namevar a la variable nommee namevar de l'environnement
	 */
	public void equalsPoigneeVariable(String namepgn, String namevar){
		Poignee pgn = this.getPoigneeByName(namepgn);
		if(pgn!=null){
			if(namevar.equals("null")){
				if(pgn.pointeVers!=null){
					pgn.pointeVers.retirePoignee(namepgn);
					pgn.pointeVers = null;
				}
			}else{
				Variable var = this.getVariableByName(namevar);
				if(var!=null){
					if(pgn.typepgn.isAssignableFrom(var.type)){
						if(pgn.pointeVers!=null){
							pgn.pointeVers.retirePoignee(namepgn);
						}
						var.poignees.add(pgn);
						pgn.pointeVers = var;
					}
				}
			}
		}
	}
	
	/**
	 * affecte la poignee nommee namepgn a la valeur null
	 */
	public void mettrePoigneeNull(String namepgn){
		Poignee pgn = this.getPoigneeByName(namepgn);
		if((pgn!=null)&&(pgn.pointeVers!=null)){
			pgn.pointeVers.retirePoignee(namepgn);
			pgn.pointeVers = null;
		}
	}
	
	/**
	 * le ramasse-miette supprime de l'environnement les variables qui sont completement inaccessibles,
	 * ie aucune poignee ne permet d'y acceder, que ce soit directement ou indirectement
	 * par le biais des attributs des variables vers lesquelles les poignees pointent.
	 */
	public void ramasseMiettes(){
		int k = 0;
		boolean modification = true;
		while(modification){
			/* il est necessaire de faire un balayage supplementaire tant qu'une suppression 
			 * de variable est effectuee lors d'un balayage */
			modification = false;
			while(k < this.instances.size()){
				Variable varMere_k = this.instances.get(k);
				if(varMere_k.poignees.isEmpty() && varMere_k.variablesMeres.isEmpty()){
					/* avant de supprimer l'element, il faut le retirer des listes variablesMeres
					 * de tous ses attributs */
					Field[] allFields_k = Environnement.getAllFields(varMere_k.type);
					for(int i=0; i<allFields_k.length; i++){
						String field_i_name = allFields_k[i].getName();
						for(int j=0; j<this.instances.size(); j++){
							Variable varFille_j = this.instances.get(j);
							this.retireVariableMere(field_i_name, varMere_k, varFille_j);
						}
					}
					modification = true;
					this.instances.remove(k);
				}else{
					k++;
				}
			}
		}
	}
	
	/**
	 * renvoie true si et seulement si une variable est accessible,
	 * que ce soit directement a travers une poignee quand cette poignee pointe vers la variable
	 * ou a travers les attributs des variables vers lesquelles les poignees pointent
	 */
	public static boolean estAccessible(Variable var){
		boolean accessible = false;
		if(var.poignees.size()>0){
			accessible = true;
		}
		int i=0;
		while((!accessible)&&(i<var.variablesMeres.size())){
			accessible = estAccessible(var.variablesMeres.get(i));
			i++;
		}
		return accessible;
	}
	
	/** 
	 * renvoie la variable de l'environnement contenant 
	 * l'attribut nomAttPere de la variable NomVarMere 
	 */
	public Variable variableContenantAttribut(String nomVarMere, String nomAttPere){
		Variable varFille = null;
		for(int i=0; i<this.instances.size(); i++){
			Variable var_i = this.instances.get(i);
			for(int j=0; j<var_i.variablesMeres.size(); j++){
				boolean estVarFille = var_i.variablesMeres.get(j).name.equals(nomVarMere);
				boolean hasAttPere = var_i.attributsPeres.get(j).equals(nomAttPere);
				if(estVarFille && hasAttPere){
					varFille = var_i;
				}
			}
		}
		return varFille;
	}
	
	/**
	 * renvoie toutes les variables ayant pour variable mere la variable nommee nomVarMere
	 * et pour attribut pere correspondant l'attribut nomAttPere. Fonction de test permettant 
	 * de verifier qu'a tout moment, tout attribut non null d'une
	 * variable quelconque pointe sur une unique variable.
	 */
	public ArrayList<Variable> varsContainField(String nomVarMere, String nomAttPere){
		ArrayList<Variable> res = new ArrayList<Variable>();
		for(int i=0; i<this.instances.size(); i++){
			Variable var_i = this.instances.get(i);
			for(int j=0; j<var_i.variablesMeres.size(); j++){
				boolean estVarFille = var_i.variablesMeres.get(j).name.equals(nomVarMere);
				boolean hasAttPere = var_i.attributsPeres.get(j).equals(nomAttPere);
				if(estVarFille && hasAttPere){
					res.add(var_i);
				}
			}
		}
		return res;
	}
	
	/**
	 * renvoie toutes les variables filles de la variable var,
	 * ie toutes les variables vers lesquelles les attributs non nulls de var pointent
	 */
	public ArrayList<Variable> variablesContenantAttributs(Variable var){
		ArrayList<Variable> res = new ArrayList<Variable>();
		for(int i=0; i<this.instances.size(); i++){
			Variable var_i = this.instances.get(i);
			if(var.possedeAttributDans(var_i)){
				res.add(var_i);
			}
		}
		return res;
	}
	
	/**
	 * verifie recursivement si une variable ainsi que toutes les variables ayant pour valeurs
	 * les attributs de cette variable possedent au moins un attribut declare en statique
	 */
	public boolean hasStaticField(Variable var){
		boolean has = false;
		if(!this.isPrimitif(var.type.getName())){
			Field[] allFields = Environnement.getAllFields(var.type);
			for(int i=0; i<allFields.length; i++){
				if(Modifier.isStatic(allFields[i].getModifiers())){
					has = true;
				}
			}
			if(!has){
				/* appel recursif sur chaque variable contenant la valeur d'un attribut de var */
				ArrayList<Variable> varsAttributs = this.variablesContenantAttributs(var);
				int i=0;
				while((!has)&&(i<varsAttributs.size())){
					has = this.hasStaticField(varsAttributs.get(i));
					i++;
				}
			}
		}
		return has;
	}
	
	/**
	 * soit TypeA le type de la variable var. refreshRelatedVariables supprime les liens
	 * entre les variables ayant pour type TypeA, tout supertype de TypeA ou sous-type de TypeA.
	 * Les attributs de toutes ces variables sont ensuite rajoutes a l'environnement
	 * en appelant la fonction addAttributesToInstances sur chacune de ces variables.
	 * 
	 */
	public void refreshRelatedVariables(Variable var){
		for(int i=0; i<this.instances.size(); i++){
			Variable var_i = this.instances.get(i);
			if((!var_i.name.equals(var.name))&&
					((var_i.type.isAssignableFrom(var.type)) || (var.type.isAssignableFrom(var.type)))){
				/* met a jour les liens des variables de type compatible avec celui de var */
				ArrayList<Variable> linked_atts = this.linkedAttributes(var_i, true);
				this.addAttributesToInstances(var_i);
			}
		}
		
	}
	
	/** 
	 * renvoie toutes les variables declarees dans la classe type et toutes ses superclasses 
	 */
	public static Field[] getAllFields(Class type){
		Field[] fields = type.getDeclaredFields();
        if(type.getSuperclass() != null) {
        	Field[] superfields = getAllFields(type.getSuperclass());
        	Field[] res = new Field[fields.length+superfields.length];
        	for(int i=0; i<fields.length; i++){
        		res[i] = fields[i];
        	}
        	for(int i=0; i<superfields.length; i++){
        		res[fields.length+i] = superfields[i];
        	}
        	return res;
        }else{
        	return fields;
        }
	}
	
	/**
	 * renvoie une copie de toutes les variables de l'environnement.
	 * Les copies des instances ne seront pas affectees par les modifications 
	 * effectuees sur les variables de l'environnement originel
	 */
	public ArrayList<Variable> cloneInstances(){
		ArrayList<Variable> snapshot = new ArrayList<Variable>();
		for(int i=0; i<this.instances.size(); i++){
			snapshot.add(this.instances.get(i).cloneVariable());
		}
		return snapshot;
	}
	
	/**
	 * renvoie une copie de toutes les poignees de l'environnement.
	 * Les copies des poignees ne seront pas affectees par les modifications 
	 * effectuees sur les poignees de l'environnement originel
	 */
	public ArrayList<Poignee> clonePile(){
		ArrayList<Poignee> stack = new ArrayList<Poignee>();
		for(int i=0; i<this.pile.size(); i++){
			stack.add(this.pile.get(i).clonePoignee());
		}
		return stack;
	}
	
	public static void main(String[] args) {
		
		Environnement env = new Environnement();
		
		JFileChooser fChooser = new JFileChooser("./xml");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
		fChooser.setFileFilter(filter);
		fChooser.showOpenDialog(null);
		System.out.println(fChooser.getSelectedFile().getPath());	
		
		ParseurXML parseur = new ParseurXML(fChooser.getSelectedFile().getPath());
		ArrayList<Class> drawable = parseur.extraitClassesDessinables();
		ArrayList<String> primitifs = parseur.extraitClassesPrimitives();
		
		env.types = drawable;
		env.typesprimitifs = primitifs;
		
		GUI gui = new GUI(env);
	}
}
