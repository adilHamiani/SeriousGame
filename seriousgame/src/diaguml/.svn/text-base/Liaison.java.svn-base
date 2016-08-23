package diaguml;

import java.util.ArrayList;

public class Liaison {

	private Etiquette et_dep;
	private Etiquette et_arr;
	private String nature;
	private String role1;
	private String role2;
	private String multiplicite1;
	private String multiplicite2;

	public Liaison(Etiquette et1, Etiquette et2, String nat, String role1, String role2, String mult1, String mult2){

		this.et_dep = et1;
		this.et_arr = et2;
		this.nature = nat;
		this.role1 = role1;
		this.role2 = role2;
		this.multiplicite1 = mult1;
		this.multiplicite2 = mult2;

	}

	public Liaison(Etiquette et1, Etiquette et2){

		this.et_dep = et1;
		this.et_arr = et2;	

	}

	public Liaison(Etiquette et1, Etiquette et2, String nat){

		this.et_dep = et1;
		this.et_arr = et2;
		this.nature = nat;

	}

	public Etiquette getEtDep(){
		return this.et_dep;
	}

	public Etiquette getEtArr(){
		return this.et_arr;
	}

	public String getNature(){
		return this.nature;
	}

	public String getRole1(){
		return this.role1;
	}

	public String getRole2(){
		return this.role2;
	}

	public String getMult1(){
		return this.multiplicite1;
	}

	public String getMult2(){
		return this.multiplicite2;
	}

	public void setEtDep(Etiquette et_dep){
		this.et_dep = et_dep;
	}

	public void setEtArr(Etiquette et_arr){
		this.et_arr = et_arr;
	}

	public void setNature(String nat){
		this.nature = nat;
	}

	public void setRole1(String role1){
		this.role1 = role1;
	}

	public void setRole2(String role2){
		this.role2 = role2;
	}

	public void setMult1(String mult){
		this.multiplicite1 = mult;
	}

	public void setMult2(String mult){
		this.multiplicite2 = mult;
	}

	@Override
	public boolean equals(Object e) {
		if (e instanceof Liaison) {
			return( this.et_dep.equals(((Liaison) e).getEtDep()) 
					&& this.et_arr.equals(((Liaison) e).getEtArr()));
		}
		else return false;
	}

	public static Boolean egalite_liaison(ArrayList<Liaison> l1, ArrayList<Liaison> l2){
		Boolean incl1 = true;
		Boolean incl2 = true;
		for (int i = 0; i<l1.size();i++){
			if( !l2.contains(l1.get(i)) ){
				incl1 = false;
			}
		}

		for (int i = 0; i<l2.size();i++){
			if( !l1.contains(l2.get(i)) ){
				incl2 = false;
			}
		}

		return (incl1 && incl2);
	}


}
