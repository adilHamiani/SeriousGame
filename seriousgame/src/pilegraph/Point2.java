package pilegraph;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Point2 extends Point{
	
	public int z;

	public Point2(){
		super();
		z = 0;
	}
	
	public void translate3d(){
		this.x++;
		this.y++;
		this.z++;
	}
	
	public static void main(String[] args){
		
		/*
		Integer var1 = null;
		Integer var2 = 2;
		var2 = var1;
		System.out.println(var1 == null);
		var1 = 1;
		System.out.println(var2);
		*/
		/*
		Point pnt = new Point();
		Point2 pnt2 = new Point2();
		Point3 pnt3 = new Point3();
		Class point1 = pnt.getClass();
		Class point2 = pnt2.getClass();
		Class point3 = pnt3.getClass();
		
		pnt2 = pnt3;
		System.out.println(point1.isAssignableFrom(point3));
		Field[] att3 = Environnement.getAllFields(point3);
		for(Field field : att3){
			System.out.println(Modifier.toString(field.getModifiers())+" "+field.getType()+" "+field.getName());
		}
		
		pnt3.translate();
		
		System.out.println(point1.isAssignableFrom(point2));
		System.out.println(point2.isAssignableFrom(point1));
		System.out.println(point2.isAssignableFrom(point3));
		*/
		
	}
	
}
