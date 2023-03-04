package entities;

import gamestates.Playing;
import level.Level;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.constants.EnemyConstant.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] carbbyArray;
    private List<Carbby> carbbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        carbbies = level.getCrabs();
    }

    public void update(int[][] lvlData, Player player) {
        for(Carbby c : carbbies)
            if(c.isActive()) 
                c.update(lvlData, player);
    }

    public void draw(Graphics g, int xlvlOffset) {
        drawCrabs(g , xlvlOffset);
    }

    private void drawCrabs(Graphics g, int xlvlOffset) {
        boolean checkEnemy = false;
        for(Carbby c : carbbies) {
            if(c.isActive()) {
                g.drawImage(carbbyArray[c.getEnemyState()][c.getAniIndex()], (int)(c.getHitbox().x) - xlvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(), (int)(c.getHitbox().y) - CRABBY_DRAWOFFSET_Y,
                CARABBY_WIDTH*c.flipWidth(), CARABBY_HEIGHT, null);
                //c.drawAttackBox(g,xlvlOffset);
                checkEnemy = true;
            }
        }

        if(!checkEnemy) {
            playing.setGameComplete(true);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for(Carbby c : carbbies) {
            if(c.isActive()) {
                if(attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }
            }
        }
    }

    private void loadEnemyImgs() {
        carbbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtLast(LoadSave.CARBBY_SPRITES);

        for(int i=0; i<carbbyArray.length; i++)
            for(int j=0; j<carbbyArray[0].length; j++) 
                carbbyArray[i][j] = temp.getSubimage(j * CARABBY_WIDTH_DEFAULT, i * CARABBY_HEIGHT_DEFAULT, CARABBY_WIDTH_DEFAULT, CARABBY_HEIGHT_DEFAULT);
    }

    public void resetAll() {
        for(Carbby c : carbbies)
            c.resetEnemy();
    }
}
