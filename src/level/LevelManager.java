package level;

import main.Game;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gamestates.Gamestate;
import gamestates.Menu;

import java.awt.Graphics;

public class LevelManager {
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex;

    private Game game;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        BuildAllLevels();
    }

    private void BuildAllLevels() {
        BufferedImage[] allLevel = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevel) {
            levels.add(new Level(img));
        }
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
            for(int j = 0; j < levels.get(lvlIndex).getLevelData()[0].length; j++) {
                int index = levels.get(lvlIndex).getSpriteIndex(j, i);
                g.drawImage(levelSprite[index], Game.TILES_SIZE*j - lvlOffset, Game.TILES_SIZE*i,Game.TILES_SIZE,Game.TILES_SIZE, null);
            }

    }
    public void update() {

    }

    public void loadNextLevel() {
        lvlIndex++;
        if(lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more level");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLvlOffset());
    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
