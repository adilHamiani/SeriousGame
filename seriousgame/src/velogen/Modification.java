package velogen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * 
 * fenetre permettant a l'utilisateur de modifier les valeurs des attributs de type Enum
 * d'une variable de l'environnement
 *
 */

public class Modification {

	/**
	 * fenetre s'affichant lorsque l'utilisateur voudra changer la valeur des
	 * attributs enum d'une instance creee dans l'environnement
	 */
	public static JFrame window = null;
	
	/**
	 * la variable dont les attributs seront modifies
	 */
	public static Variable varModifiable;
	
	/**
	 * Ensemble de listes. Chaque liste contiendra toutes les valeurs possibles des attributs enum
	 * de la variable varModifiable
	 */
	public static ArrayList<DefaultListModel> listModel = new ArrayList<DefaultListModel>();
	
	/**
	 * Ensemble de listes. Chaque liste contiendra toutes les valeurs possibles des attributs enum
	 * de la variable varModifiable
	 */
	public static ArrayList<JList> jList = new ArrayList<JList>();
	
	/**
	 * Ensemble de listes. Chaque liste contiendra toutes les valeurs possibles des attributs enum
	 * de la variable varModifiable
	 */
	public static ArrayList<JScrollPane> scrollPane = new ArrayList<JScrollPane>();
	
	/**
	 * nouvelles valeurs choisies par l'utilisateur des attributs de type Enum 
	 * de la variable varModifiable
	 */
	public static ArrayList<Object[]> valeursEnum = new ArrayList<Object[]>();
	
	/**
	 * attributs de type Enum de la variable varModifiable
	 */
	public static ArrayList<Field> attributsEnum = new ArrayList<Field>();
	
	/**
	 * Lorsque l'utilisateur aura selectionne les nouvelles valeurs de chaque attribut de type
	 * Enum de la variable varModifier, ces nouvelles valeurs seront affectees aux attributs
	 * en cliquant sur le JButton modifier
	 */
	public static JButton modifier;
	
	public Modification(Variable varModifiable){
		Modification.varModifiable = varModifiable;
		listModel.clear();
		jList.clear();
		scrollPane.clear();
		valeursEnum.clear();
		attributsEnum.clear();
		modifier = new JButton("Modifier");
		modifier.setEnabled(true);
		Field[] attributs = GUI.getAllFields(varModifiable.valeur.getClass());
		for(int k=0; k<attributs.length; k++){
			Field f = attributs[k];
			int modF = f.getModifiers();
			boolean estFinal = Modifier.isFinal(modF);
			if(f.getType().isEnum() && !estFinal){
				/*
				System.out.println("L'attribut "+f.getName()+" est du type Enum "
						+f.getClass().getName()+" et n'est pas final");
				*/
				try{
					Object[] valsEnum = f.getType().getEnumConstants();
					JComboBox<Object> comboBox = new JComboBox<Object>(); 
					DefaultListModel enumListModel = new DefaultListModel();
					int indexDejaChoisi = -1;
					Object valeurDejaChoisie = f.get(varModifiable.valeur);
					for(int i=0; i<valsEnum.length; i++){
						comboBox.addItem(valsEnum[i].toString());
						enumListModel.addElement(valsEnum[i].toString());
						if(valeurDejaChoisie == valsEnum[i]){
							indexDejaChoisi = i;
						}
					}
					
					JList enumSelec = new JList(enumListModel);
					enumSelec.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					enumSelec.setLayoutOrientation(JList.VERTICAL);
					enumSelec.setVisibleRowCount(10);
					
					if(indexDejaChoisi!=(-1)){
						comboBox.setSelectedIndex(indexDejaChoisi);
						enumSelec.setSelectedIndex(indexDejaChoisi);
					}else{
						comboBox.setSelectedIndex(0);
						enumSelec.setSelectedIndex(0);
					}
					
					JScrollPane listScrollPane = new JScrollPane(enumSelec);
					listScrollPane.setPreferredSize(new Dimension(120, 100));
					listModel.add(enumListModel);
					jList.add(enumSelec);
					scrollPane.add(listScrollPane);
					valeursEnum.add(valsEnum);
					attributsEnum.add(f);
					
				}catch(Exception e){
					System.out.println("Impossible d'acceder a "+varModifiable.nom+"."+f.getName());
				}
			}else{
				/*System.out.println("L'attribut "+f.getName()+" n'est pas de type Enum : "+f.isEnumConstant() 
						+" "+ f.getType().getName()+" "+f.getType().isEnum());*/
			}
		}
		modifier.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				for(int i=0; i<Modification.valeursEnum.size(); i++){
					Object[] allValsEnum = valeursEnum.get(i);
					int indexChosenVal = Modification.jList.get(i).getSelectedIndex();
					Object chosenValue = allValsEnum[indexChosenVal];
					Field attribut = attributsEnum.get(i);
					try{
						attribut.set(Modification.varModifiable.valeur, chosenValue);
					}catch(Exception e){
						System.out.println("Impossible de modifier la valeur de "
								+Modification.varModifiable.nom+"."+attribut.getName());
					}
				}
				for(int i=0; i<GUI.dessin.size(); i++){
					GUI.dessin.get(i).repaint();
				}
				GUI.diagramme.repaint();
				Modification.window.setVisible(false);
				GUI.modify.setEnabled(true);
				if(GUI.chooseCreate.getItemCount()!=0){
					GUI.create.setEnabled(true);
				}
			}
		});
		window = new JFrame();
		window.setBackground(Color.WHITE);
		Container content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel nomsAttributsEnums = new JPanel();
		nomsAttributsEnums.setLayout(new GridLayout(1, attributsEnum.size()));
		for(int i=0; i<attributsEnum.size(); i++){
			JLabel nomAttribut = new JLabel(attributsEnum.get(i).getName());
			nomsAttributsEnums.add(nomAttribut);
		}
		JPanel listeValsEnums = new JPanel();
		
		for(int i=0; i<scrollPane.size(); i++){
			listeValsEnums.add(scrollPane.get(i));
		}
		
		content.add(nomsAttributsEnums, BorderLayout.NORTH);
		content.add(listeValsEnums, BorderLayout.CENTER);
		content.add(modifier, BorderLayout.SOUTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		
	}
	
	public static void main(String[] args){
		Test t = new Test();
		String n = "t";
		Variable v = new Variable(t,n);
		Modification m = new Modification(v);
		
	}
	
}
