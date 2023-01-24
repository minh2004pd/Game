package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverLay;
import utilz.LoadSave;

public class Playing extends State implements Statemethods{
    private Player player;
    private PauseOverLay pauseOverLay;
    private LevelManager levelManager;
    private boolean paussed = false;

    private int xlvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTIlesOffset = lvlTilesWide - Game.TILES_INT_WIDTH;
    private int maxLvlOffsetX = maxTIlesOffset * Game.TILES_SIZE;

    private BufferedImage playingBG;
    
    public Playing(Game game) {
        super(game);
        initClasses();

        playingBG = LoadSave.GetSpriteAtLast(LoadSave.PLAYING_BG);
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(100,200,(int)(64 * Game.SCALE),(int)(40 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverLay = new PauseOverLay(this);
    }

    @Override
    public void update() {
        if(!paussed){
            levelManager.update();;
            player.update();
            checkCloseToBorder();
        }else {
            pauseOverLay.update();
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
        
        levelManager.draw(g,xlvlOffset);
        player.render(g,xlvlOffset);
        if(paussed) {
           g.setColor(new Color(0,0,0,155));
           g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
           pauseOverLay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
           player.setAttacking(true);
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(paussed)
           pauseOverLay.mouseReleased(e);
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(paussed)
           pauseOverLay.mousePressed(e);
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(paussed)
           pauseOverLay.mouseMoved(e);
        
    }

    public void unpauseGame() {
        paussed = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
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

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
    
}