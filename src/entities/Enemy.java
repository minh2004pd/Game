package entities;

import static utilz.constants.EnemyConstant.*;
import static utilz.constants.Directions.*;
import static utilz.HelpMethods.*;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Enemy extends Entity {
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.8f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int enemyY;
    protected int attackDistance = Game.TILES_SIZE;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean attackChecked;
    
    private boolean active = true;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }
    
    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType,enemyState)) {
                aniIndex = 0;

                switch(enemyState) {
                    case ATTACK,HIT -> enemyState = IDLE;
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
        if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            enemyY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void Run(int[][] lvlData) {
        float xSpeed = 0;

        if(walkDir == LEFT) xSpeed = -walkSpeed;
        else xSpeed = walkSpeed;

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
        this.enemyState = state;
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
        fallSpeed = 0;
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
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }
}
