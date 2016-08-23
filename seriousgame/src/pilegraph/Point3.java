package pilegraph;

public class Point3 extends Point2{
	public Point2 pbis;
	public int t;
	
	public Point3(){
		super();
		t = 0;
		pbis = null;
	}
	
	public void translate4d(){
		this.x++;
		this.y++;
		this.z++;
		this.t++;
	}
	
	public static void main(String[] args){
		/*
		Point pnt = new Point();
		Point2 pnt2 = new Point2();
		Point3 pnt3 = new Point3();
		pnt3.pbis = pnt3;
		Class point = pnt.getClass();
		Class point2 = pnt2.getClass();
		Class point3 = pnt3.getClass();
		System.out.println(point.isAssignableFrom(point2));
		System.out.println(point2.isAssignableFrom(point));
		System.out.println(point.isAssignableFrom(point3));
		System.out.println(point2.isAssignableFrom(point3));
		System.out.println(point3.isAssignableFrom(point));
		*/
	}
}
