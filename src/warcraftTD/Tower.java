package warcraftTD;

import java.util.Iterator;

public abstract class Tower {
	// Position du monstre Ã  l'instant t
	public Position p;
	// Vitesse de tir
	private double speedshoot;
	// temps jusqu'au prochain tir
	private long timerefill; //en milliseconde
	// Porté de tir
	private double areashoot;
	// Degat
	private double damage;

	public World w;

	public String image; //emplcament de l'image
	private boolean air;
	private boolean terre; // possiblite d attque sol ou air
	private String type;

	private boolean charge = true; //tir chargé
	private long lastshoot; //temps du dernier tir effectué
	
	// Tower(p,0.1,100,2*squareWidth,5,w,true,true,"image")

	public Tower(Position p, double speedshoot, long timerefill, double areashoot, 
			double damage, World w, boolean air, boolean terre,String image,String type) {
		this.p = p;
		this.speedshoot = speedshoot;
		this.timerefill = timerefill * 1000000;
		this.areashoot = areashoot;
		this.type=type;
		this.damage = damage;
		this.w = w;
		this.air = air;
		this.terre = terre;
		this.image = image;
	}

	public boolean Charge() {
		if (charge==true) return true;
		else if (System.nanoTime() - lastshoot >= timerefill ) {
			charge=true;
			return charge;
		}
		else return false;

	}

	public boolean Porte(Monster m) { //regarde si le monstre en question est a porte de la tour
		if( (m.air == air | m.terre== terre) & p.dist(m.p) <= areashoot) return true;
		else return false;
	}
	public void shoot(Monster m) {
		if (Charge() & Porte(m) & System.currentTimeMillis()- w.timebeg >m.timeinter) {
			Projectile tir = new Projectile(m,w,damage,p.clone(),type,areashoot,speedshoot);
			w.projectile.add(tir);
			lastshoot = System.nanoTime();
			charge=false;
		}
	}

	public void update() {
		draw();
		if(Charge() & w.start==true & w.pause==false) {
			Iterator<Monster> i = w.monsters.iterator();
			Monster m;
			while (i.hasNext()) {
				m = i.next();
				if (Porte(m)) shoot(m);
			}
		}
	}





	public Position getP() {
		return p;
	}
	public double getSpeedshoot() {
		return speedshoot;
	}
	public double getTimerefill() {
		return timerefill;
	}
	public double getAreashoot() {
		return areashoot;
	}
	public double getDamage() {
		return damage;
	}
	public boolean isAir() {
		return air;
	}
	public boolean isTerre() {
		return terre;
	}
	
	public abstract void draw();

}
