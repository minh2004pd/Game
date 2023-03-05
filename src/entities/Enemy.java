package entities;

import static utilz.constants.EnemyConstant.*;
import static utilz.constants.Directions.*;
import static utilz.HelpMethods.*;
import java.awt.geom.Rectangle2D;
import static utilz.constants.*;

import main.Game;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    
    protected int walkDir = LEFT;
    protected int enemyY;
    protected int attackDistance = Game.TILES_SIZE;
    protected boolean attackChecked;
    
    private boolean active = true;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        runSpeed = 0.35f * Game.SCALE;
    }
    
    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType,state)) {
                aniIndex = 0;

                switch(state) {
                    case ATTACK,HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if(!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
            
        firstUpdate = false;
    }

    protected void inAirCheck(int[][] lvlData) {
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            enemyY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void Run(int[][] lvlData) {
        float xSpeed = 0;

        if(walkDir == LEFT) xSpeed = - runSpeed;
        else xSpeed = runSpeed;

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if(InFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    public void hurt(int damage) {
        currentHealth -= damage;

        if(currentHealth <=0) {
            changeState(DEAD);
        }else {
            changeState(HIT);
        }
    }

    protected void changeState(int state) {
        this.state = state;
        aniIndex = 0;
        aniTick = 0;
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT) walkDir = RIGHT;
        else walkDir = LEFT; 
    }

    protected boolean abilitySeePlayer(int[][] lvlData, Player player) {
        int playerY = (int)(player.hitbox.y / Game.TILES_SIZE);
        if(playerY == enemyY) 
            if(isPlayerInRanger(player)) 
                if(isSightClear(lvlData,hitbox,player.hitbox,enemyY))
                    return true;
            
        
        return false;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        changeState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
           player.changeHealth(-GetEnemyDMG(enemyType));
        attackChecked = true;
    }

    protected void TowardsPlayer(Player player) {
        if(player.hitbox.x > hitbox.x) 
            walkDir = RIGHT;
        else 
            walkDir = LEFT;
    }

    protected boolean IsPlayerCloseForAttack(Player player) {
        int absDistance = (int)Math.abs(hitbox.x - player.hitbox.x);
        return absDistance <= attackDistance;
    }

    private boolean isPlayerInRanger(Player player) {
        int absDistance = (int)Math.abs(hitbox.x - player.hitbox.x);
        return absDistance <= attackDistance*5;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return state;
    }

    public boolean isActive() {
        return active;
    }
}
