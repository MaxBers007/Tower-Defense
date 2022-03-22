package warcraftTD;

import java.util.List;
import java.util.LinkedList;

import java.util.ArrayList;
import java.util.Iterator;

public class World {
	// l'ensemble des monstres, pour gerer (notamment) l'affichage
	World w; //le monde lui m�me

	List<Monster> monsters = new ArrayList<Monster>();
	List<Projectile> projectile = new ArrayList<Projectile>();
	List<Tower>   towers   = new ArrayList<Tower>();
	Background route;
	private List<Position> routePOS;

	// Position par laquelle les monstres vont venir
	private Position spawn;
	private Position chateau;

	// Information sur la taille du plateau de jeu
	int width;
	int height;
	int nbSquareX;
	int nbSquareY;
	double squareWidth;
	double squareHeight;
	boolean endpartie=false;

	long timebeg;

	// Nombre de points de vie du joueur
	int life = 20;
	// Nombre d'argent
	int money= 80;

	// Commande sur laquelle le joueur appuie (sur le clavier)
	char key;

	// Condition pour terminer la partie
	boolean end = false;
	// Condition pour commencer la partie
	boolean start = false;
	// Condition pour mettre en pause la partie
	boolean pause = false;

	/**
	 * Initialisation du monde en fonction de la largeur, la hauteur et le nombre de cases données
	 * @param width
	 * @param height
	 * @param nbSquareX
	 * @param nbSquareY
	 * @param startSquareX
	 * @param startSquareY
	 */
	public World(int width, int height, int nbSquareX, int nbSquareY) {
		this.width = width;
		this.height = height;
		this.nbSquareX = nbSquareX;
		this.nbSquareY = nbSquareY;
		this.route = new Background(nbSquareX,nbSquareY);
		this.routePOS = route.getRoutePOS();

		squareWidth = (double) 1 / nbSquareX;
		squareHeight = (double) 1 / nbSquareY;
		spawn = new Position(routePOS.get(0).x * squareWidth + squareWidth / 2, routePOS.get(0).y * squareHeight + squareHeight / 2);
		chateau = new Position(routePOS.get(routePOS.size()-1).x * squareWidth + squareWidth / 2, routePOS.get(routePOS.size()-1).y * squareHeight + squareHeight / 2);
		StdDraw.setCanvasSize(width, height);
		StdDraw.enableDoubleBuffering();
	}

	public List<Position> getRoutePOS() {
		return routePOS;
	}

	public Position getSpawn() {
		return spawn;
	}

	public Position getChateau() {
		return chateau;
	}

	/**
	 * Définit le décors du plateau de jeu.
	 */
	public void drawBackground() {	
		StdDraw.setPenColor(StdDraw.LIGHT_GREEN);
		for (int i = 0; i < nbSquareX; i++)
			for (int j = 0; j < nbSquareY; j++)
				//StdDraw.filledRectangle(i * squareWidth + squareWidth / 2, j * squareHeight + squareHeight / 2, squareWidth , squareHeight);
				StdDraw.picture(i * squareWidth + squareWidth / 2, j * squareHeight + squareHeight / 2, "images/herbe.png", squareWidth, squareHeight);
	}

	/**
	 * Initialise le chemin sur la position du point de d�part des monstres. Cette fonction permet d'afficher une route qui sera différente du décors.
	 */
	public void drawPath() {
		StdDraw.setPenColor(StdDraw.YELLOW);
		for (Position p:routePOS) {
			StdDraw.filledRectangle(p.x* squareWidth + squareWidth / 2, p.y * squareHeight + squareHeight / 2, squareWidth / 2, squareHeight / 2);
		}
		StdDraw.setPenColor(StdDraw.RED); //Spawn
		StdDraw.filledRectangle(spawn.x, spawn.y, squareWidth / 2, squareHeight / 2); // Le Spawn � d�finir
		StdDraw.setPenColor(StdDraw.BLUE); // La base � d�finir 
		StdDraw.picture(chateau.x, chateau.y, "images/chateau.png", squareWidth, squareHeight);
	}

	public Background getRoute() {
		return route;
	}

	/**
	 * Affiche certaines informations sur l'écran telles que les points de vie du joueur ou son or
	 */
	public void drawInfos() {
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	/**
	 * Fonction qui récupère le positionnement de la souris et permet d'afficher une image de tour en temps réél
	 *	lorsque le joueur appuie sur une des touches permettant la construction d'une tour.
	 */
	public void drawMouse() {
		double normalizedX = (int)(StdDraw.mouseX() / squareWidth) * squareWidth + squareWidth / 2;
		double normalizedY = (int)(StdDraw.mouseY() / squareHeight) * squareHeight + squareHeight / 2;
		String image = null;
		switch (key) {
		case 'a' : 
			// image pour représenter une tour d'archers
			image ="images/arche1.png";
			break;
		case 'b' :
			// TODO Ajouter une image pour représenter une tour à canon
			image="images/bombeur.png";
			break;
		}
		if (image != null)
			StdDraw.picture(normalizedX, normalizedY, image, squareWidth, squareHeight);
	}

	/**
	 * Pour chaque monstre de la liste de monstres de la vague, utilise la fonction update() qui appelle les fonctions run() et draw() de Monster.
	 * Modifie la position du monstre au cours du temps à l'aide du paramètre nextP.
	 */
	public void updateMonsters() {

		Iterator<Monster> i = monsters.iterator();
		Monster m;
		List<Monster> killMonsters = new ArrayList<Monster>();

		while (i.hasNext()) {
			m = i.next();
			if(System.currentTimeMillis()- timebeg >m.timeinter){
				if (m.update()) killMonsters.add(m);
			}
		}
		if(!killMonsters.isEmpty()) {
			Iterator<Monster> v = killMonsters.iterator();
			Monster mm;
			while (v.hasNext()) {
				mm = v.next();
				money += mm.money;
			}
			monsters.removeAll(killMonsters);
		}
	}
	public void updateTower() {

		Iterator<Tower> k = towers.iterator();
		Tower t;
		Tower at;
		while (k.hasNext()) {
			t = k.next();
			t.update();
		}
	}
	public void updateProjectile() {

		Iterator<Projectile> k = projectile.iterator();
		Projectile t;
		List<Projectile> useProjectile = new ArrayList<Projectile>();
		while (k.hasNext()) {
			t = k.next();
			if (t.update()) useProjectile.add(t);
		}
		if (!useProjectile.isEmpty()) projectile.removeAll(useProjectile);
	}

	/**
	 * Met à jour toutes les informations du plateau de jeu ainsi que les déplacements des monstres et les attaques des tours.
	 * @return les points de vie restants du joueur
	 */
	public int update() {
		drawBackground();
		drawPath();
		drawInfos();
		updateTower();
		if(start==true) {
			updateProjectile();
			updateMonsters();
		}
		drawMouse();
		return life;
	}

	/**
	 * Récupère la touche appuyée par l'utilisateur et affiche les informations pour la touche séléctionnée
	 * @param key la touche utilisée par le joueur
	 */
	public void keyPress(char key) {
		key = Character.toLowerCase(key);
		this.key = key;
		switch (key) {
		case 'a':
			System.out.println("Arrow Tower selected (50g).");
			break;
		case 'b':
			System.out.println("Bomb Tower selected (60g).");
			break;
		case 'e':
			System.out.println("Evolution selected (40g).");
			break;
		case 's':
			printCommands();
			start=true;
			pause=false;
			System.out.println("Starting game!");
			break;
		case 'q':
			end=true;
			System.out.println("Exiting.");
		case 'p':
			pause=true;
			System.out.println("Game Paused.");
			printCommands();
		}
	}

	/**
	 * Vérifie lorsque l'utilisateur clique sur sa souris qu'il peut: 
	 * 		- Ajouter une tour à la position indiquée par la souris.
	 * 		- Améliorer une tour existante.
	 * Puis l'ajouter à la liste des tours
	 * @param x
	 * @param y
	 */
	public void mouseClick(double x, double y) {
		double normalizedX = (int)(x / squareWidth) * squareWidth + squareWidth / 2;
		double normalizedY = (int)(y / squareHeight) * squareHeight + squareHeight / 2;
		Position p = new Position(normalizedX, normalizedY);
		switch (key) {
		case 'a':
			if(money>=50) {
				System.out.println("Tour d'archers install�");
				towers.add(new BaseTower(p,0.04,100,0.3,2,w,true,true,"images/arche1.png","fleche"));
				money -=50;
				break;
			}
			else System.out.println("Pas assez de g "+"vous avez: "+money+"g");
		case 'b':
			if(money>=60) {
				System.out.println("Tour de bombes install�");
				towers.add(new BaseTower(p,0.02,200,0.2,8,w,false,true,"images/bombeur.png","bombe"));
				money -=60;
				break;
			}
			else  System.out.println("Pas assez de g "+"vous avez: "+money+"g");
			}
	}

	/**
	 * Comme son nom l'indique, cette fonction permet d'afficher dans le terminal les différentes possibilités 
	 * offertes au joueur pour intéragir avec le clavier
	 */
	public void printCommands() {
		System.out.println("Press A to select Arrow Tower (cost 50g).");
		System.out.println("Press B to select Cannon Tower (cost 60g).");
		System.out.println("Press E to update a tower (cost 40g).");
		System.out.println("Click on the grass to build it.");
		if(start!=false | pause==true) System.out.println("Press S to start.");
	}

	/**
	 * Récupère la touche entrée au clavier ainsi que la position de la souris et met à jour le plateau en fonction de ces interractions
	 */
	public boolean run() {
		printCommands();
		timebeg = System.currentTimeMillis();
		while(!end & !endpartie) {

			StdDraw.clear();
			if (StdDraw.hasNextKeyTyped()) {
				keyPress(StdDraw.nextKeyTyped());
			}

			if (StdDraw.isMousePressed()) {
				mouseClick(StdDraw.mouseX(), StdDraw.mouseY());
				StdDraw.pause(50);
			}

			update();
			StdDraw.show();
			StdDraw.pause(20);
			if (pause) while(pause) {
				if (StdDraw.hasNextKeyTyped()) {
					keyPress(StdDraw.nextKeyTyped());
				}
			}
			if(monsters.isEmpty()) endpartie=true;
			if(life<=0) end=true;
		}
		if (end) while(true) {
			StdDraw.clear();
			StdDraw.show();
		}
		return endpartie;
	}
}
