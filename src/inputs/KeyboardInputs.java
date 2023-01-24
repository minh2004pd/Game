package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().KeyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().KeyReleased(e);
                break;
            default:
                break;
        }
        
    }
    

}