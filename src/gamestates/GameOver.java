package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import main.Game;

public class GameOver {
    private Playing playing;

    public GameOver(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press esc to enter Main Menu", Game.GAME_WIDTH / 2, 300);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            playing.resetAll();
            Gamestate.state =  Gamestate.MENU;
        }
    }
}
