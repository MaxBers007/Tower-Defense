package warcraftTD;

public class BaseTower extends Tower {
	
	public BaseTower(Position p, double speedshoot, long timerefill, double areashoot, double damage,
			World w, boolean air, boolean terre,String image,String type) {
		super( p, speedshoot, timerefill,  areashoot,  damage,  w,  air, terre,image,type);
	}
	
	/**
	 * dessine la tour
	 */
	public void draw() {
		StdDraw.picture(p.x, p.y, image, w.squareWidth, w.squareHeight);
	}
}
