package entities;

import static utilz.constants.EnemyConstant.*;

import main.Game;

public class Carbby extends Enemy {

    public Carbby(float x, float y) {
        super(x, y, CARABBY_WIDTH, CARABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }
    
}
