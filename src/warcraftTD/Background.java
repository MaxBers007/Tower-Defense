package warcraftTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Genere la route et la stocke dans routePOS avec des objets de classe Position
// Ici 0 représente haut, 1 droite, 2 bas, 3 gauche DEF DIRECTION

// Tous les tests n ont pas etait fait car il a de l aleatoire (donc beaucoup de test) et rarement une erreur stackoverflow arrive...
// Mais le programme a juste besoin  d être relance et cela marchera pour le reste du jeu
// Une relance automatiqe est envigasable en cas d erreur si il me reste du temps

// postion[] ==> [x][y]

public class Background {
	private List<Position> routePOS;
	private ArrayList<int[]> route;
	private Stack<Integer> choix;
	private int nbSquareX;
	private int nbSquareY;
	private int longMaxi;

	public Background(int nbSquareX,int nbSquareY){
		longMaxi = ((nbSquareX*nbSquareY)/3) ;
		this.nbSquareX=nbSquareX;
		this.nbSquareY=nbSquareY;

		route = new ArrayList<int[]>();
		route.ensureCapacity(longMaxi+1);
		routePOS = new ArrayList<Position>();
		choix = new Stack<Integer>();

		int entrer = (int) Math.round(3*Math.random());
		switch(entrer) {
		case(0):{
			int[] position= {(int) Math.round((nbSquareX-1)*Math.random()),0};
			route =route(entrer,position);
		}
		case(1):{
			int[] position= {nbSquareX-1,(int) Math.round((nbSquareY-1)*Math.random())};
			route =route(entrer,position);
		}
		case(2):{
			int[] position= {(int) Math.round((nbSquareX-1)*Math.random()),nbSquareY-1};
			route =route(entrer,position);
		}
		case(3):{
			int[] position= {0,(int) Math.round((nbSquareY-1)*Math.random())};
			route =route(entrer,position);
		}
		}
		for(int i=0;i<route.size();i++) {
			routePOS.add(new Position(route.get(i)[0],route.get(i)[1]));
		}

	}

	// on a ici le génrateur de route aléatoire

	private ArrayList<int[]> route(int entrer,int[] position) {
		while(true) {
			if (position == null) break;
			if (route.size()>=longMaxi) {
				return route;
			}
			else {
				int[] nposition = position.clone();
				if (!route.contains(position.clone())) {
					route.add(nposition.clone());
				}
				position=choix(entrer,position);
				//routetrie.add(position);

			}

		}
		int[] tempo=route.get(0);
		route.clear();
		choix.clear();
		return route(entrer,tempo);
	}
	private int[] choix(int entrer,int[] position) { //choix du chemin avec des anti-boucle et anti-queu
		if (route.size()<2) {
			choix.add((entrer+2)%4);
			return PostionAv((entrer+2)%4,position);
		}
		else{
			ArrayList<Integer> possible = new ArrayList<Integer>(); //les possilibite d'avance de route
			for(int k=0;k<4;k++) {
				int[] tempo = PostionAv(k,position.clone());
				if (k!=((choix.peek()+2)%4)& verifeR(tempo)) {
					possible.add(k);
					if (!verifeR(PostionAv(k,tempo.clone())) | !verifeR(PostionAv((k-1+4)%4,tempo.clone())) | !verifeR(PostionAv((k+1+4)%4,tempo.clone())) ) {
						possible.remove(Integer.valueOf(k));
					}
				}
			}
			//if (AntiBoucle(choix.size()) != 9999) possible.remove(Integer.valueOf(AntiBoucle(choix.size()))); // à debugger si le temps permet
			if (AntiQueu(choix.size()) != 9999) possible.remove((Integer.valueOf(AntiQueu(choix.size()))));


			if (Contient(possible, choix.lastElement())) { //Pour augmenter les chances d'avoir un chemin droit si possible
				possible.add(choix.lastElement());
			}

			if (possible.size()>0) {
				int avanceR = randomTab(possible,true);
				choix.add(avanceR);
				return PostionAv(avanceR,position.clone());
			}
			else return null;


			//int[] hasTab= {-1,0,0,0,1}; // 3 chance sur 5 (zero) de rester dans la même direction ect...

		}
	}

	private int[] PostionAv(int Direc,int[] postion){      //retourne la postion d'avance
		switch(Direc) {
		case 0: {
			postion[1]--;
			return postion;
		}
		case 1:{
			postion[0]++;
			return postion;
		}
		case 2: {
			postion[1]++;
			return postion;
		}
		case 3: {
			postion[0]--;
			return postion;
		}
		}
		return postion;
	}

	public List<Position> getRoutePOS() {
		return routePOS;
	}

	private int AntiBoucle(int size) {
		int i = 1;
		if (size>=3) {
			switch(choix.get(size-i)) {
			case 0:{
				i=IteraS(i,0,size);
				switch(choix.get(size-i)) {
				case 1:{
					i=IteraS(i,1,size);
					if(choix.get(size-i)==2) return 3;
				}
				case 3:{
					i=IteraS(i,3,size);
					if(choix.get(size-i)==1) return 1;
				}
				}

			}
			case 1:{
				i=IteraS(i,1,size);
				switch(choix.get(size-i)) {
				case 2:{
					i=IteraS(i,2,size);
					if(choix.get(size-i)==3) return 0;
				}
				case 0:{
					i=IteraS(i,0,size);
					if(choix.get(size-i)==3) return 2;
				}
				}
			}
			case 2:{
				i=IteraS(i,2,size);
				switch(choix.get(size-i)) {
				case 3:{
					i=IteraS(i,3,size);
					if(choix.get(size-i)==0) return 1;
				}
				case 1:{
					i=IteraS(i,1,size);
					if(choix.get(size-i)==0) return 3;
				}
				}
			}
			case 3:{
				i=IteraS(i,3,size);
				switch(choix.get(size-i)) {
				case 0:{
					i=IteraS(i,0,size);
					if(choix.get(size-i)==1) return 2;
				}
				case 2:{
					i=IteraS(i,2,size);
					if(choix.get(size-i)==1) return 0;
				}
				}
			}
			}
		}
		return 9999;
	}
	private int AntiQueu(int size) {
		if(size>=2) {
			for(int k=0;k<4;k++) {
				if(k==choix.get(size-2)) {
					if (choix.get(size-1)==((k-1+4)%4)) return ((k-2+4)%4);
					if (choix.get(size-1)==((k+1+4)%4)) return ((k+2+4)%4);
				}
			}

		}
		return 9999;
	}


	private int IteraS(int position,int perm,int size) { //Parcours une liste jusqu a une occurence différente de perm
		int tempo=position;
		while(choix.get(position)==perm & position<size) {
			if (choix.get(position)!=perm) return position;
			position--;
		}
		return tempo;
	}

	public boolean verifeR(int[] pos) { //retourne vrai si la position sur la route est possible
		if (Contient(route,pos) |(pos[0]>=(nbSquareX-1) | pos[0]<0 | pos[1]>=(nbSquareY-1) | pos[1]<0)){
			return false;
		}
		return true;
	}

	public static int randomTab(ArrayList<Integer> tab,boolean type) { //choix au hasard d'un entier dans un tableau ou d'un entier
		int c =(int) Math.round((tab.size()-1)*Math.random());
		if (type) return tab.get(c);						  //si type = true alors retour d'une valeur d'un tableau aléatoirement
		else return c;
	}

	public String affiche() {
		String retour = "";
		for(int i=0; i<route.size();i++) {
			int[] line = route.get(i);
			retour += "["+line[0]+","+line[1]+"]"+" ";
		}
		return retour;
	}

	public static boolean Contient (ArrayList<int[]> tab,int[] x) { //vérifie si une arraylist continet la valeur voulue
		if(!tab.isEmpty()) {
			for(int i=0;i<tab.size()-1;i++) {
				if(tab.get(i)[0]==x[0] & tab.get(i)[1]==x[1]) return true;
			}
		}
		return false;
	}
	public static boolean Contient (ArrayList<Integer> tab,int x) { //vérifie si une arraylist continet la valeur voulue
		if(!tab.isEmpty()) {
			for(int i=0;i<tab.size()-1;i++) {
				if(tab.get(i)==x) return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {



		//Background back = new Background(11,11);
		//System.out.println(back.affiche());
		//System.out.println(back.getRoutePOS());
	}
}



















