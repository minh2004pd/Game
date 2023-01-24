package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private level levelOne;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtLast(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int i = 0; i<4; i++)
           for(int j=0; j<12; j++) {
            int index = i*12 +j;
            levelSprite[index] = img.getSubimage(j*32, i*32, 32, 32);
           }
    }

    public void draw(Graphics g, int lvlOffset) {
        for(int i = 0; i<Game.TILES_INT_HEIGHT; i++)
            for(int j = 0; j < levelOne.getLevelData()[0].length; j++) {
                int index = levelOne.getSpriteIndex(j, i);
                g.drawImage(levelSprite[index], Game.TILES_SIZE*j - lvlOffset, Game.TILES_SIZE*i,Game.TILES_SIZE,Game.TILES_SIZE, null);
            }

    }
    public void update() {

    }
    public level getCurrentLevel() {
        return levelOne;
    }
}