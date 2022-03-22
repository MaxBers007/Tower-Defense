package warcraftTD;

public class Main {

	public static void main(String[] args) {
		int width = 800;
		int height = 600;
		int nbSquareX = 16;
		int nbSquareY = 12;
		
		World w;
		
		w = new World(width, height, nbSquareX, nbSquareY);
		w.w=w;
		
		int level=1;
		CreatorPartie Partie = new CreatorPartie(w, level);
		while(true) {
			if (w.run()) level++;
			w.endpartie=false;
			Partie = new CreatorPartie(w, level);
		}
		// Lancement de la boucle principale du jeu
	}
}

