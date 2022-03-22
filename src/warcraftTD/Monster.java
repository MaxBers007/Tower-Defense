package warcraftTD;

public abstract class Monster {
	// Position du monstre à l'instant t
	Position p;
	// Vitesse du monstre
	double speed;
	// Position du monstre à l'instant t+1
	Position nextP;
	// Boolean pour savoir si le monstre à atteint le chateau du joueur
	boolean reached;
	int money;
	// Compteur de déplacement pour savoir si le monstre à atteint le chateau du joueur
	World w;
	int index ; // index dans le parcours du chemin de World
	double life;
	boolean air =false; // type air si true
	boolean terre =false;
	
	long timeinter;


	public Monster(Position p,World w) {
		this.p = p;
		this.nextP = new Position(p);
		this.w =w;
	}
	public Monster(Position p) {
		this.p = p;
		this.nextP = new Position(p);
	}
	public Monster(Position p,World w,Position nextP) {
		this.p = p;
		this.nextP = nextP;
		this.w=w;
	}

	/**
	 * Déplace le monstre en fonction de sa vitesse sur l'axe des x et des y et de sa prochaine position.
	 */
	public void move() {
		// Mesure sur quel axe le monstre se dirige.
		double dx = nextP.x - p.x;
		double dy = nextP.y - p.y;
		if (dy + dx != 0){
			// Mesure la distance à laquelle le monstre à pu se déplacer.
			double ratioX = dx/(Math.abs(dx) + Math.abs(dy));
			double ratioY = dy/(Math.abs(dx) + Math.abs(dy));
			p.x += ratioX * speed*((double)w.height/w.width);
			p.y += ratioY * speed;
		}
		if (Math.pow(p.x-nextP.x,2) + Math.pow(p.y- nextP.y,2) <=0.0001) {
			if (index< w.getRoutePOS().size()-1) {
				index++;
				nextP= new Position(w.getRoutePOS().get(index).x * w.squareWidth + w.squareWidth / 2, w.getRoutePOS().get(index).y* w.squareHeight + w.squareHeight / 2);
			}
			else {
				w.life -=1;
				life =0;
			}

		}
	}

	public boolean update() {
		move();
		draw();
		if (life<=0) return true; //regarde si le monstre est mort ou mort au ch�teau....
		else return false;
	}
	
	public Monster clone() {
		Monster mc = new BaseMonster(p.clone(),w,nextP);
		mc.index=index;
		mc.air=air;
		mc.reached=reached;
		mc.speed=speed;
		mc.life=speed;
		mc.terre=terre;
		
		return  mc;
	}

	/**
	 * Fonction abstraite qui sera instanciée dans les classes filles pour afficher le monstre sur le plateau de jeu.
	 */
	public abstract void draw();
}
