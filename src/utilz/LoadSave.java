package utilz;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static utilz.constants.EnemyConstant.*;

import javax.imageio.ImageIO;

import entities.Carbby;
import main.Game;


public class LoadSave {

    public static final String PLAYER_ATLAS = "res/player_sprites.png";
    public static final String LEVEL_ATLAS = "res/outside_sprites.png";
    //public static final String LEVEL_ONE_DATA = "res/level_one_data (1).png";
    public static final String LEVEL_ONE_DATA = "res/level_one_data_long.png";
    public static final String MENU_BUTTONS = "res/button_atlas.png";
    public static final String MENU_BACKGROUND = "res/menu_background.png";
    //public static final String BACKGROUND_MENU = "res/background_menu.png";
    public static final String BACKGROUND_MENU = "res/97978b7d3d8dd42ae7b9fe723f816f80.jpg";
    public static final String PAUSE_BACKGROUND = "res/pause_menu.png";
    public static final String SOUND_BUTTONS = "res/sound_button.png";
    public static final String URM_BUTTONS = "res/urm_buttons.png";
    public static final String PLAYING_BG = "res/playing_bg_img.png";
    public static final String SMALL_CLOUDS = "res/small_clouds.png";
    public static final String BIG_CLOUDS = "res/big_clouds.png";
    public static final String CARBBY_SPRITES = "res/crabby_sprite.png";
    public static final String STATUS_BAR = "res/health_power_bar.png";
    
    public static BufferedImage GetSpriteAtLast(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    public static ArrayList<Carbby> GetCrabs() {
        BufferedImage img = GetSpriteAtLast(LEVEL_ONE_DATA);
        ArrayList<Carbby> list = new ArrayList<>();
        for(int i=0; i < img.getHeight(); i++)
           for(int j=0; j < img.getWidth(); j++) {
               Color color = new Color(img.getRGB(j, i));
               int value = color.getGreen();
               if (value == CRABBY)
                   list.add(new Carbby(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
           }
        return list;
    }

    public static int[][] GetLevelData(){
        BufferedImage img = GetSpriteAtLast(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int i=0; i < img.getHeight(); i++)
           for(int j=0; j < img.getWidth(); j++) {
               Color color = new Color(img.getRGB(j, i));
               int value = color.getRed();
               if (value >= 48)
                   value = 0;
               lvlData[i][j] = value;
           }
        return lvlData;

    }

}
