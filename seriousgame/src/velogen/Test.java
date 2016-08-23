package velogen;

import java.awt.Graphics2D;

public class Test{

	public TypeA a;
	public TypeB b;
	public TypeC c;
	public TestBis sub1;
	public TestBis sub2;
		
	public Test(){
		a = TypeA.AVAL3;
		b = TypeB.BVAL2;
		c = TypeC.CVAL3;
		sub1 = null;
		sub2 = new TestBis();
	}
	
	public void drawTest(Graphics2D g){
		/* comme il s'agit de la classe racine a l'origine de tous les autres attributs,
		 * il faudra dessiner l'instance elle-meme avant de dessiner ses attributs */
		g.fillOval(50, 50, 20, 20);
		g.drawRect(50, 50, 20, 20);
	}
	
	public void drawsub1(Graphics2D g){
		if(this.sub1!=null){
			g.drawLine(5, 30, 20, 5);
		}
	}
	
	public void drawsub2(Graphics2D g){
		if(this.sub2!=null){
			g.drawLine(5, 5, 20, 30);
		}
	}
	
	
	
}

