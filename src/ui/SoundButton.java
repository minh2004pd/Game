package ui;

import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import static utilz.constants.UI.PauseButton.*;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundImgs; 
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtLast(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];

        for(int i=0; i < soundImgs.length; i++) 
            for(int j=0; j<soundImgs[0].length; j++) 
                soundImgs[i][j] = temp.getSubimage(j * SOUND_SIZE_DEFAULT, i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    public void update() {
        if(muted)
           rowIndex = 1;
        else  
           rowIndex = 0;
        
        colIndex = 0;
        if(mouseOver)
           colIndex = 1;
        if(mouseOver)
           colIndex = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean getMouseOver() {
        return mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean getMousePressed() {
        return mousePressed;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean getMuted() {
        return muted;
    }

    
}
