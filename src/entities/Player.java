package entities;

import static utilz.constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.image.BufferedImage;

import gamestates.Playing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import static utilz.constants.*;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    //status bar UI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (152 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (44 * Game.SCALE);
    private int healthBarYStart = (int) (24 * Game.SCALE);

    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipWidth = 1;

    //AttackBox
    private boolean attackChecked;

    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing = playing;
        this.state = IDLE;
        maxHealth = 100;
        currentHealth = maxHealth;
        runSpeed = 1.0f * Game.SCALE;
        loadAnimation();
        initHitbox(20, 27);
        initAttackBox();
    }

    public void setSpawn(Point player) {
        this.x = player.x;
        this.y = player.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(20 * Game.SCALE),(int)(20 * Game.SCALE));
    }

    public void update() {
        updateHealthBar();

        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();
        if(attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();

    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
           return;
        
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        }else if(left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (int) (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) (((float)currentHealth / (float)maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {

        g.drawImage(animations[state][aniIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y - yDrawOffset),
        width*flipWidth,height, null);
        //drawHitbox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);

        drawStatusUI(g);
    }

    private void drawStatusUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY,statusBarWidth,statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart, healthBarYStart, healthWidth, healthBarHeight);
    }

    public void changeHealth(int value) {
        currentHealth += value;;

        if(currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        }else if(currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void updateAnimationTick() {

        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
                
        }

    }

    private void setAnimation() {
        int startAni = state;

        if(moving)
          state = RUNNING;
        else
          state = IDLE;

        if(inAir) {
            if(airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }

        if(attacking) {
          state = ATTACK;
          if(startAni != ATTACK) {
            aniIndex = 1;
            aniTick = 0;
          }
        }

        if(startAni != state)
           resetAniTick();
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {

        moving = false;

        if(jump)
            jump();
        // if(!left && !right && !inAir)
        //     return;
        if(!inAir) {
            if((!left && !right) || (left && right))
                 return;
        }

        float xSpeed = 0;

        if(left) {
            xSpeed -= runSpeed;
            flipX = width;
            flipWidth = -1;
        }

        if(right) {
            xSpeed += runSpeed;
            flipX = 0;
            flipWidth = 1;
        }

        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData)) 
                inAir = true;
            
        if(inAir) {

        if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            updateXPos(xSpeed);
        }else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
            if(airSpeed > 0) 
            //if touching floor
               resetInAir();
            else 
            //if touching roof
               airSpeed = fallSpeedAfterCollision;
            updateXPos(xSpeed);
        }

        }else 
            updateXPos(xSpeed);
        moving = true;
        
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {

        if(CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)) {
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }

    }

    private void loadAnimation() {

        BufferedImage img = LoadSave.GetSpriteAtLast(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        
        for(int i = 0; i < animations.length; i++)
           for(int j=0; j < animations[i].length; j++)
              animations[i][j] = img.getSubimage(j*64, i*40, 64, 40);

        statusBarImg = LoadSave.GetSpriteAtLast(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left){
        this.left = left;
    }

    public boolean isLeft() {
        return left;
    }

    public void setRight(boolean right){
        this.right = right;
    }

    public boolean isRight() {
        return right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, lvlData)) 
            inAir = true;
    }
    
}
