package pilegraph;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Segment {

	public Point pntLeft;
	public Point pntRight;
	
	public Segment(Point pntLeft, Point pntRight) {
		super();
		this.pntLeft = pntLeft;
		this.pntRight = pntRight;
	}
	
	public Segment(Point pntLeft) {
		this.pntLeft = pntLeft;
		this.pntRight = new Point(0,0);
	}
	
	public Segment(int plx, int ply, int prx, int pry){
		this.pntLeft = new Point(plx,ply);
		this.pntRight = new Point(prx,pry);
	}
	
	public Segment(Point pntl, int prx, int pry){
		this.pntLeft = pntl;
		this.pntRight = new Point(prx, pry);
	}
	
	public Segment(Segment sgt){
		this.pntLeft = sgt.pntLeft;
		this.pntRight = sgt.pntRight;
	}
	
	public Segment(Segment sgtl, Segment sgtr){
		this.pntLeft = sgtl.pntRight;
		this.pntRight = sgtr.pntLeft;
	}
	
	/* methodes pour tester l'appel de methode dans l'application pile d'execution */
	public void emptyPoints(){
		this.pntLeft = null;
		this.pntRight = null;
	}
	
	public void fullPoints(){
		this.pntLeft = new Point(1,1);
		this.pntRight = new Point(5,5);
	}
	
	public void permute(){
		Point temp = this.pntLeft;
		this.pntLeft = this.pntRight;
		this.pntRight = temp;
	}
	
	public Point getLeftPoint(){
		return this.pntLeft;
	}
	
	public Point middlePoint(){
		int xmid = (this.pntLeft.x + this.pntRight.x)/2;
		int ymid = (this.pntLeft.y + this.pntRight.y)/2; 
		Point middle = new Point(xmid, ymid);
		return middle;
	}
	
	public void shuffle(){
		String temp = this.pntLeft.nom;
		this.pntLeft.nom = this.pntRight.nom;
		this.pntRight.nom = temp;
	}
	
	public void setPoints(Point pntLeft, Point pnrRight){
		this.pntLeft = pntLeft;
		this.pntRight = pntRight;
	}
	
	public void setCoordinates(int pntLx, int pntLy, int pntRx, int pntRy){
		this.pntLeft.x = pntLx;
		this.pntLeft.y = pntLy;
		this.pntRight.x = pntRx;
		this.pntRight.y = pntRy;
	}
	
	public void setCoordinates(Point pntL, int pntRx, int pntRy){
		this.pntLeft.x = pntL.x;
		this.pntLeft.y = pntL.y;
		this.pntRight.x = pntRx;
		this.pntRight.y = pntRy;
	}
	
	public static void main(String[] args){
		
		/*
		ArrayList<String> a = new ArrayList<String>();
		Class t = a.getClass();
		System.out.println(t.getActualTypeArguments()[0]);
		Type[] tt = t.getGenericInterfaces();
		for(int i=0; i<tt.length; i++){
			Type g = tt[i];
			if (g instanceof ParameterizedType) {
		        ParameterizedType pType = (ParameterizedType)g;
		        System.out.print("Raw type: " + pType.getRawType() + " - ");
		        System.out.println("Type args: " + pType.getActualTypeArguments()[0]);
		    } else {
		        System.out.println("Not ParametrizedType");
		    }
		}
		System.out.println(t.toString());
		*/
		//System.out.println(t instanceof ParameterizedType);
		/*
		Segment s1 = new Segment(new Point(1,2), new Point(3,4));
		Segment s2 = new Segment(new Point(5,6), new Point(7,8));
		System.out.println("s1.pntRight.x="+s1.pntRight.x+" s1.pntRight.y="+s1.pntRight.y);
		boolean b = Modifier.isStatic(s1.getClass().getDeclaredFields()[1].getModifiers());
		System.out.println(s1.getClass().getDeclaredFields()[1].getName()+" is static = "+b);
		boolean b1 = Modifier.isStatic(s1.getClass().getDeclaredFields()[0].getModifiers());
		System.out.println(s1.getClass().getDeclaredFields()[0].getName()+" is static = "+b1);
		
		Point pnt1 = new Point(1,2);
		Point pnt2 = new Point(7,8);
		Segment sgt1 = new Segment(pnt1, pnt2);
		Segment sgt2 = new Segment(sgt1);
		sgt1.pntLeft.translate();
		sgt1.pntLeft.translate();
		System.out.println(sgt1.pntLeft.x+" "+sgt1.pntLeft.y);
		System.out.println(sgt2.pntLeft.x+" "+sgt2.pntLeft.y);
		Class segment = sgt1.getClass();
		Class point = pnt1.getClass();
		Field[] sgtatt = segment.getFields();
		Field[] pntatt = point.getFields();
		Constructor[] sgtconst = segment.getConstructors();
		for(int k=0; k<point.getDeclaredMethods().length; k++){
			System.out.println("Methode point num"+k+" "+point.getDeclaredMethods()[k].toString());
		}
		System.out.println((point.getDeclaredMethods()[0].getReturnType()==Void.TYPE));
		System.out.println((point.getDeclaredMethods()[0].getReturnType().getName().equals("void")));
		for(int k=0; k<point.getDeclaredConstructors().length; k++){
			System.out.println("Constructeur point num"+k+" "+point.getDeclaredConstructors()[k].toString());
		}
		for(int k=0; k<point.getDeclaredMethods().length; k++){
			System.out.println("Methode point num"+k+" "+point.getDeclaredMethods()[k].toString());
		}
		for(int k=0; k<segment.getDeclaredConstructors().length; k++){
			System.out.println("Constructeur segment num"+k+" "+segment.getDeclaredConstructors()[k].toString());
		}
		try {
			int i=1; int j=3; int k = 7; int l = 9;
			int m=11; int n = 13; int o = 17; int p = 19;
			Object pntx = pnt1.getClass().getConstructors()[2].newInstance(i,j);
			Object pnty = pnt1.getClass().getConstructors()[2].newInstance(k,l);
			Object constargs[] = new Object[2];
			constargs[0] = pntx;
			constargs[1] = pnty;*/
			//Object sgta = sgtconst[0].newInstance(pntx, pnty);
			/*
			System.out.println("AVANT");
			Object sgta = sgtconst[0].newInstance(constargs);
			System.out.println("APRES");
			*/
			//Object sgtb = sgtconst[1].newInstance(sgta);
			/*
			Object pntq = pnt1.getClass().getConstructors()[2].newInstance(m,n);
			Object pntr = pnt1.getClass().getConstructors()[2].newInstance(o,p);
			Object sgtb = sgtconst[0].newInstance(sgtatt[0].get(sgta), pntr);
			
			Object sgtal = sgtatt[0].get(sgta);
			Object sgtar = sgtatt[1].get(sgta);
			Object sgtbl = sgtatt[0].get(sgtb);
			Object sgtbr = sgtatt[1].get(sgtb);
			System.out.println((sgtal == sgtbl) +" && "+ (sgtar == sgtbr));
	
			System.out.println(point.getMethods()[0].toString());
			point.getMethods()[0].invoke(sgtal);
			System.out.println(sgtatt[0].get(sgta) == sgtatt[0].get(sgtb));
			System.out.println(pntatt[0].get(sgtal));
			System.out.println(pntatt[1].get(sgtal));
			System.out.println(pntatt[0].get(sgtbl));
			System.out.println(pntatt[1].get(sgtbl));
			
			
			
		} catch (Exception e) {
		}*/
		
	}
}
