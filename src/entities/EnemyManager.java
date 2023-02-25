package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

import static utilz.constants.EnemyConstant.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] carbbyArray;
    private List<Carbby> carbbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        carbbies = LoadSave.GetCrabs();
    }

    public void update(int[][] lvlData) {
        for(Carbby c : carbbies)
            c.update(lvlData);
    }

    public void draw(Graphics g, int xlvlOffset) {
        drawCrabs(g , xlvlOffset);
    }

    private void drawCrabs(Graphics g, int xlvlOffset) {
        for(Carbby c : carbbies) 
            g.drawImage(carbbyArray[c.getEnemyState()][c.getAniIndex()], (int)(c.getHitbox().x) - xlvlOffset - CRABBY_DRAWOFFSET_X, (int)(c.getHitbox().y) - CRABBY_DRAWOFFSET_Y, CARABBY_WIDTH, CARABBY_HEIGHT, null);
    }

    private void loadEnemyImgs() {
        carbbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtLast(LoadSave.CARBBY_SPRITES);

        for(int i=0; i<carbbyArray.length; i++)
            for(int j=0; j<carbbyArray[0].length; j++) 
                carbbyArray[i][j] = temp.getSubimage(j * CARABBY_WIDTH_DEFAULT, i * CARABBY_HEIGHT_DEFAULT, CARABBY_WIDTH_DEFAULT, CARABBY_HEIGHT_DEFAULT);
    }
}
