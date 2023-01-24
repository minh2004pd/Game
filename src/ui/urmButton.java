package ui;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import static utilz.constants.UI.URM.*;

import utilz.LoadSave;

public class urmButton extends PauseButton{

    private BufferedImage[] img;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public urmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    public void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtLast(LoadSave.URM_BUTTONS);

        img = new BufferedImage[3];
        for(int i = 0; i <  img.length; i++) {
            img[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img[index], x, y, URM_SIZE, URM_SIZE, null);

    }

    public void update() {
        index = 0;
        if(mouseOver) 
           index = 1;
        if(mousePressed)
           index = 2;
    }

    void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    
}
