package ui;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.constants.UI.BUTTONS.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class MenuButton {
    private int xPos, yPos,rowIndex,index;
    private int xOffsetCenter = B_WIDTH / 2;
    private boolean mouseOver,mousePressed;
    private Gamestate state;
    private BufferedImage[] imgs;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter,yPos,B_WIDTH,B_HEIGHT);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtLast(LoadSave.MENU_BUTTONS);
        for(int i=0; i<imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex  * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);

    }

    public void update() {
        index = 0;
        if(mouseOver) {
            index = 1;
        }
        if(mousePressed) {
            index = 2;
        }
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

    public void applyGamestate() {
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

}
