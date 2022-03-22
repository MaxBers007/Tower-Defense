package warcraftTD;

public class BaseMonster extends Monster {

	public BaseMonster(Position p,World w) {
		super(p, w);
	}
	public BaseMonster(Position p,World w,Position nextP) {
		super(p,w,nextP);
	}
	
	/**
	 * Affiche un monstre qui change de couleur au cours du temps
	 * Le monstre est représenté par un cercle de couleur bleue ou grise
	 */
	public void draw() {
		if(terre==true) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.filledCircle(p.x, p.y, 0.01);
		}
		else {
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledSquare(p.x, p.y, 0.01);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.square(p.x, p.y, 0.01);
		}
	}
}
