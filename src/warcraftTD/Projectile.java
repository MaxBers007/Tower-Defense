package warcraftTD;

public class Projectile {
	private Monster m;
	private double damage; //dommage engendrÈ
	
	private Position pOrigine; //Position inital du projectile
	private Position mOrigine; //Position intial du monstre
	
	public Position p; //Position du projectile
	public Position nextP; //Position ‡ atteindre
	private String type; //Soit type fleche soit boulet
	private double area; //distance max de tir (hit box)
	private double speed;

	World w;
	// Projectile(monstre,w,,5,p,'fleche',0.01,0.1)

	public Projectile(Monster m,World w, double damage, Position p, String type, double area,double speed) {
		this.m = m;
		this.damage = damage;
		this.p = p;
		this.pOrigine =p;
		this.mOrigine =m.p.clone();
		this.type = type;
		this.area = area;
		this.speed=speed;
		this.w = w;
		if (nextP==null) this.nextP=CalculFutur();

	}
	public Projectile(World w,Position p, double speed) {
		this.w =w;
		this.p=p;
		this.speed=speed;
	}

	private Position CalculFutur() {
		Monster mc = m.clone();
		
		Projectile pc = new Projectile (w,new Position(0,0),speed);
		pc.nextP = new Position(0,1);

		while(mc.p.dist(pOrigine) - pc.p.dist(new Position(0,0)) > 0.0001 ) {
			mc.move();
			pc.move();
		}
		return mc.p.clone();


	}

	public void move() {
		// Mesure sur quel axe le projectile se dirige.
		double dx = nextP.x - p.x;
		double dy = nextP.y - p.y;
		if (dy + dx != 0){
			// Mesure la distance √† laquelle le projectile √† pu se d√©placer.
			double ratioX = dx/(Math.abs(dx) + Math.abs(dy));
			double ratioY = dy/(Math.abs(dx) + Math.abs(dy));
			p.x += ratioX * speed*((double)w.height/w.width);
			p.y += ratioY * speed;
		}
	}
	public boolean toucher() {
		if (m.life<=0 | area <= m.p.dist(pOrigine) | p.dist(nextP) < 0.005 ) return true;
		if (m.p.dist(p) < 0.01){
			m.p.x += 0.01/2;
			m.p.y += 0.01/2;
			m.life -=damage;
			return true;
		}
		else return false;
	}

	public void draw() {
		switch(type) {
		case("fleche"):{
			StdDraw.setPenColor(StdDraw.BLACK);
			double dx = nextP.x - p.x;
			double dy = nextP.y - p.y;
			if (dy + dx != 0){
				double ratioX = dx/(Math.abs(dx) + Math.abs(dy));
				double ratioY = dy/(Math.abs(dx) + Math.abs(dy));
				Position f = new Position(p.x + ratioX * 0.005* ((double)w.height/w.width), p.y + ratioY * 0.005);
				StdDraw.line(p.x, p.y, f.x,f.y); //define le dessin graphique de la fleche
			}
			break;
		}
		case("bombe"):{
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledCircle(p.x, p.y, 0.005); //define le dessin graphique de la fleche
		}
		}
	}

	public boolean update() {
		move();
		draw();
		return toucher();

	}

}
