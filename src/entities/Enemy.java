package entities;

import static utilz.constants.EnemyConstant.*;
import static utilz.constants.Directions.*;
import static utilz.HelpMethods.*;

import main.Game;

public abstract class Enemy extends Entity {
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 1.0f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int enemyY;
    protected int attackDistance = Game.TILES_SIZE;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }
    
    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType,enemyState)) {
                aniIndex = 0;
                if(enemyState == ATTACK)
                    enemyState = IDLE;
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
}
