package entities;

import static utilz.constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private int aniTick, aniIndex, anispeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right,down,jump;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y,width,height);
        loadAnimation();
        initHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));

    }

    public void update() {

        updatePos();
        updateAnimationTick();
        setAnimation();

    }

    public void render(Graphics g, int lvlOffset) {

        g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset, (int)(hitbox.y - yDrawOffset),width,height, null);
        //drawHitbox(g);
    }

    private void updateAnimationTick() {

        aniTick++;
        if(aniTick >= anispeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
                
        }

    }

    private void setAnimation() {
        int startAni = playerAction;

        if(moving)
          playerAction = RUNNING;
        else
          playerAction = IDLE;

        if(inAir) {
            if(airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

        if(attacking)
          playerAction = ATTACK_1;

        if(startAni != playerAction)
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

        if(left)
            xSpeed -= playerSpeed;
        if(right)
            xSpeed += playerSpeed;
        
        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData)) 
                inAir = true;
            
        if(inAir) {

        if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += gravity;
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
            animations = new BufferedImage[9][6];
        
        for(int i = 0; i < animations.length; i++)
           for(int j=0; j < animations[i].length; j++)
              animations[i][j] = img.getSubimage(j*64, i*40, 64, 40);

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
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

    public void setUp(boolean up){
        this.up = up;
    }

    public boolean isUp() {
        return up;
    }

    public void setDown(boolean down){
        this.down = down;
    }

    public boolean isDown() {
        return down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
    
}