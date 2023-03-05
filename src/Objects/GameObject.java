package Objects;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Graphics;

import main.Game;

public class GameObject {
    protected int x,y,objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean animationObject, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void initHitBox(int width, int height) {
        hitbox = new Rectangle2D.Float(x,y,(int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    protected void drawHitBox(Graphics g, int xlvloffset) {
        g.setColor(Color.red);
        g.drawRect((int)hitbox.x - xlvloffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    // protected void updateAnimationTick() {
    //     aniTick++;
    //     if(aniTick >= aniSpeed) {
    //         aniTick = 0;
    //         aniIndex++;
    //         if(aniIndex >= GetSpriteAmount(enemyType,enemyState)) {
    //             aniIndex = 0;

    //             switch(enemyState) {
    //                 case ATTACK,HIT -> enemyState = IDLE;
    //                 case DEAD -> active = false;
    //             }
    //         }
    //     }
    // }
}
