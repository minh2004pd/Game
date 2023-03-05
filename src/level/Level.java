package level;

import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;
import static utilz.HelpMethods.*;

import entities.Carbby;
import main.Game;

public class Level {
    private BufferedImage img;
    private ArrayList<Carbby> crabs;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTIlesOffset;
    private int maxLvlOffsetX ;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
        calcPlayerSpawn();
    }
    
    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTIlesOffset = lvlTilesWide - Game.TILES_INT_WIDTH;
        maxLvlOffsetX =Game.TILES_SIZE * maxTIlesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }
    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Carbby> getCrabs() {
        return crabs;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
    
}
