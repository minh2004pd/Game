package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import level.LevelManager;
import main.Game;
import ui.GameCompleted;
import ui.PauseOverLay;
import utilz.LoadSave;
import static utilz.constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private PauseOverLay pauseOverLay;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GameOver gameOver;
    private GameCompleted gameCompleted;
    private boolean paussed = false;

    private int xlvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX ;

    private BufferedImage playingBG, bigClouds, smallCLouds;
    private int[] smallCLoudsPos;
    private Random rnd = new Random();
    private boolean gameOv;
    private boolean gameCo = false;
    
    public Playing(Game game) {
        super(game);
        initClasses();

        playingBG = LoadSave.GetSpriteAtLast(LoadSave.PLAYING_BG);
        bigClouds = LoadSave.GetSpriteAtLast(LoadSave.BIG_CLOUDS);
        smallCLouds = LoadSave.GetSpriteAtLast(LoadSave.SMALL_CLOUDS);

        smallCLoudsPos = new int[8];
        for(int i=0; i<smallCLoudsPos.length; i++) {
            smallCLoudsPos[i] = (int)(90*Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
        }

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        enemyManager = new EnemyManager(this);
        levelManager = new LevelManager(game);

        player = new Player(100,200,(int)(64 * Game.SCALE),(int)(40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverLay = new PauseOverLay(this);
        gameOver = new GameOver(this);
        gameCompleted = new GameCompleted(this);
    }

    @Override
    public void update() {
        if(paussed) {
            pauseOverLay.update();
        }else if(gameCo) {
            gameCompleted.update();
        }else if(!gameOv) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
        
    }

    private void checkCloseToBorder() {
        int playerX = (int) (player.getHitbox().x);
        int diff = playerX - xlvlOffset;
        
        if(diff > rightBorder) {
            xlvlOffset += diff - rightBorder;
        }else if(diff < leftBorder) {
            xlvlOffset += diff - leftBorder;
        }

        if(xlvlOffset >  maxLvlOffsetX) 
           xlvlOffset = maxLvlOffsetX;
        else if(xlvlOffset < 0)
           xlvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(playingBG, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT,null);

        drawClouds(g);
        
        levelManager.draw(g,xlvlOffset);
        player.render(g,xlvlOffset);
        enemyManager.draw(g,xlvlOffset);
        if(paussed) {
           g.setColor(new Color(0,0,0,155));
           g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
           pauseOverLay.draw(g);
        }else if(gameOv) {
            gameOver.draw(g);
        }else if(gameCo) {
            gameCompleted.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        for(int i = 0; i < 3; i++) 
            g.drawImage(bigClouds, i * BIG_CLOUD_WIDTH - (int)(xlvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        
        for(int i=0; i<smallCLoudsPos.length; i++) {
            g.drawImage(smallCLouds, SMALL_CLOUD_WIDTH * 4 * i - (int)(xlvlOffset * 0.7), smallCLoudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT,  null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOv)
            if(e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOv) {
            if(paussed)
               pauseOverLay.mouseReleased(e);
            else if(gameCo) 
               gameCompleted.mouseReleased(e);
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOv) {
            if(paussed)
               pauseOverLay.mousePressed(e);
            else if(gameCo)
               gameCompleted.mousePressed(e);
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOv) {
            if(paussed)
                pauseOverLay.mouseMoved(e);
            else if(gameCo)
                gameCompleted.mouseMoved(e);
        }
        
    }

    public void unpauseGame() {
        paussed = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOv)
           gameOver.keyPressed(e);
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: 
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    paussed = !paussed;
                    break;
            }
    }

    @Override
    public void KeyReleased(KeyEvent e) {
        if(!gameOv)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: 
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
            }
    }

    public void setMaxLevelOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void resetAll() {
        gameOv = false;
        paussed = false;
        gameCo = false;
        player.resetAll();
        enemyManager.resetAll();
    }

    public void setGameOver(boolean gameOv) {
        this.gameOv = gameOv;
    }

    public void setGameComplete(boolean check) {
        this.gameCo = check;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    
}
