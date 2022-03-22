package warcraftTD;

import java.util.ArrayList;
import java.util.List;

public class CreatorPartie {
	List<Monster> monsters = new ArrayList<Monster>();
	int level;
	World w;
	
	public CreatorPartie(World w,int level) {
		this.level=level;
		this.w=w;
		for(int k=0;k<(5+level);k++) {
			if(level%2==0) {
				Monster monster = new BaseMonster(new Position(w.getSpawn().x, w.getSpawn().y),w);
				monster.index = 1;
				monster.nextP = new Position(w.getRoutePOS().get(1).x * w.squareWidth + w.squareWidth / 2, w.getRoutePOS().get(1).y* w.squareHeight + w.squareHeight / 2);
				monster.speed = 0.008 + Math.exp((level-1)/10)/10000;
				monster.life =5 + Math.exp((level-1)/100);
				monster.terre =true;
				monster.money=5;
				monster.timeinter =5000*k;
				monsters.add(monster);
			}
			else {
				Monster monster = new BaseMonster(new Position(w.getSpawn().x, w.getSpawn().y),w);
				monster.index = 1;
				monster.nextP = new Position(w.getRoutePOS().get(1).x * w.squareWidth + w.squareWidth / 2, w.getRoutePOS().get(1).y* w.squareHeight + w.squareHeight / 2);
				monster.speed = 0.02 + Math.exp((level-1)/10)/10000;
				monster.life =3+ Math.exp((level-1)/100);
				monster.money=(int) (8 + Math.round(Math.exp((level-1)/100)));
				monster.terre =false;
				monster.air=true;
				monster.timeinter =5000*k;
				monsters.add(monster);
			}
		}
		w.monsters.clear();
		w.monsters.addAll(monsters);
	}
}
