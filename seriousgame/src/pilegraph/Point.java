package pilegraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;

public class Point {

	public int x;
	public int y;
	public String nom;
	
	public Point(){
		this.x = new Integer(0);
		this.y = new Integer(0);
		this.nom = new String("");
	}
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
		this.nom = new String("");
	}
	
	public Point(int x, int y, String nom){
		this.x = x;
		this.y = y;
		this.nom = nom;
	}
	
	public Point(Point pnt){
		this.x = pnt.x;
		this.y = pnt.y;
		this.nom = new String("");
	}
	
	public void translate(){
		this.x++;
		this.y++;
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setName(String nom){
		this.nom = nom;
	}
	
	public void permute(){
		int temp = this.x;
		this.x = this.y;
		this.y = temp;
	}
	
	public void giveName(){
		String[] names = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Kappa", "Khi", "Phi", "Psi"};
		Random rand = new Random();
		this.nom = names[rand.nextInt(names.length)]+rand.nextInt(100);
	}
	
	
	/*
	public void mettreNull(){
		this = null;
	}
	*/
	public static void main(String[] args){
		
		/*
		Integer p = new Integer(1);
		Integer q = new Integer(2);
		Point pnt1 = new Point(p,q);
		Point pnt2 = new Point(pnt1);
		pnt1.giveName();
		int x = Integer.parseInt("256");
		Integer y = Integer.parseInt("512");
		System.out.println(x+" "+y);
		
		Class point = pnt1.getClass();
		Constructor[] constr = point.getDeclaredConstructors();
		for(int i=0; i<constr.length; i++){
			System.out.println("Constructeur "+i+" "+constr[i].toString());
		}
		
		Object[] arg = new Object[constr[2].getParameterTypes().length];
		String px = "12";
		String py = "13";
		arg[0] = Integer.parseInt(px);
		arg[1] = Integer.parseInt(py);
		try{
		Object newPoint = constr[2].newInstance(arg);
		System.out.println("newPoint.x = "+point.getDeclaredFields()[0].get(newPoint));
		System.out.println("newPoint.y = "+point.getDeclaredFields()[1].get(newPoint));
		}catch(Exception e){
			System.out.println("Ca n'a pas fonctionne");
		}
		*/
		/*(
		pnt1.x = 6;
		System.out.println(pnt2.x);
		pnt2.x = pnt1.x;
		pnt1.x = 9;
		pnt1.nom = "premier";
		System.out.println("pnt1.x="+pnt1.x+" pnt1.y="+pnt1.y);
		System.out.println("pnt2.x="+pnt2.x+" pnt2.y="+pnt2.y);
		pnt2.nom = pnt1.nom;
		pnt1.nom +="@@@@";
		System.out.println("pnt1.z="+pnt1.nom+" pnt2.z="+pnt2.nom);
		pnt2.translate();
		System.out.println("pnt2.x="+pnt2.x+" pnt2.y="+pnt2.y);
		pnt1.x = pnt1.y;
		pnt1.y = 200;
		System.out.println("pnt1.x="+pnt1.x+" pnt1.y="+pnt1.y);
		*/
	}
}