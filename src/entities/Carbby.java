package entities;

import static utilz.constants.EnemyConstant.*;
import static utilz.constants.Directions.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;

import main.Game;

public class Carbby extends Enemy {

    //AttackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxSetX;

    public Carbby(float x, float y) {
        super(x, y, CARABBY_WIDTH, CARABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
        initAttackBox();
    }

     private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxSetX = (int)(30 * Game.SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }
    
    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxSetX;
        attackBox.y = hitbox.y;
    }

    public void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - lvlOffset),(int)(attackBox.y),(int)(attackBox.width),(int)(attackBox.height));
    }

    private void updateBehavior(int[][] lvlData, Player player) {
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
                case ATTACK:
                    if(aniIndex == 0) 
                        attackChecked = false;
                        
                    if(aniIndex == 3 && !attackChecked) 
                        checkPlayerHit(attackBox,player);
                        //System.out.println("N");
                    
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX() {
        if(walkDir == RIGHT) 
            return width;
        else 
            return 0;
    }

    public int flipWidth() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

}
