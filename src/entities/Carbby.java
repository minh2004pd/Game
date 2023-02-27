package entities;

import static utilz.constants.EnemyConstant.*;
import static utilz.constants.Directions.*;
import static utilz.HelpMethods.*;

import main.Game;

public class Carbby extends Enemy {

    public Carbby(float x, float y) {
        super(x, y, CARABBY_WIDTH, CARABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }

     public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }
    
    private void updateMove(int[][] lvlData, Player player) {
        if(firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        
        if(inAir) {
            inAirCheck(lvlData);
        }
        else {
            switch(enemyState) {
                case IDLE:
                    changeState(RUNNING);
                    break;
                case RUNNING:
                    if(abilitySeePlayer(lvlData, player)) {
                       TowardsPlayer(player);
                       if(IsPlayerCloseForAttack(player))
                           changeState(ATTACK);
                    }
                    Run(lvlData);
                    break;
            }
        }
    }

}
