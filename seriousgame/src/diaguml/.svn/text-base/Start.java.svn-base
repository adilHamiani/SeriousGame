package diaguml;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/* 
  Cette Classe joue le role du menu
  c'est la premiere classe lancee qui presentra a l'utilisateur
  plusieurs choix pour les niveaux de difficultes 
  Derriere chaque bouton on lance la classe correspondant au
  niveau de difficulté.
  La classe Start reste toujours ouverte en arriere plan
 */

public class Start {

	public static JFrame jframe;
	public static JButton niveau1;
	public static JButton niveau2;
	public static JButton niveau3;
	public static JButton niveau4;
	public static int choixNiveau;

	public Start(){

		jframe = new JFrame();
		jframe.setTitle("Bienvenue dans l'application DiagUML ");
		jframe.setSize(400, 150);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		jframe.setVisible(true);

		niveau1 = new JButton("Débutant");
		niveau1.setAlignmentX(Component.CENTER_ALIGNMENT);
		niveau2 = new JButton("Intermédiaire");
		niveau2.setAlignmentX(Component.CENTER_ALIGNMENT);
		niveau3 = new JButton("Intermédiaire supérieur");
		niveau3.setAlignmentX(Component.CENTER_ALIGNMENT);
		niveau4 = new JButton("Confirmé");
		niveau4.setAlignmentX(Component.CENTER_ALIGNMENT);
		Container content = jframe.getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		content.add(niveau1);
		content.add(niveau2);
		content.add(niveau3);
		content.add(niveau4);

		//jframe.pack();
		jframe.setVisible(true);


		niveau1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					Construction c = new Construction();
					c.jframe.setSize(1000, 650);
					c.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					Start.choixNiveau = 1;
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		niveau2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					ConstAvancee c = new ConstAvancee();
					c.jframe.setSize(1000, 650);
					c.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					Start.choixNiveau = 2;
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		niveau3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					ConstAvancee2 c = new ConstAvancee2();
					c.jframe.setSize(1000, 650);
					c.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					Start.choixNiveau = 3;
				} catch (ParserConfigurationException  e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		niveau4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					ConstDifficile c = new ConstDifficile();
					c.jframe.setSize(1000, 700);
					c.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					Start.choixNiveau = 4;
				} catch (ParserConfigurationException  e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});	


	}

	public static void main(String[] args) {

		Start s = new Start();

	}

}
