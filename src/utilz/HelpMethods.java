package utilz;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.constants.EnemyConstant.*;
import java.awt.Point;

import entities.Carbby;

import java.awt.Color;

import main.Game;

public class HelpMethods {
    
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

        if(!IsSolid(x, y, lvlData))
           if(!IsSolid(x+width,y+height,lvlData))
              if(!IsSolid(x+width, y, lvlData))
                 if(!IsSolid(x,y+height,lvlData))
                    return true;
        return false;

    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
      int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if(x < 0 || x >= maxWidth)
           return true;
        if(y < 0 || y >= Game.GAME_HEIGHT)
           return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        
        return IsTileSolid((int)xIndex, (int)yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {

      int value = lvlData[(int)yTile][(int)xTile];

      if(value >= 48 || value < 0 || value != 11) 
         return true;
      return false;

    }
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {

      int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
      if(xSpeed > 0) {
         // Right
         int tileXPos = currentTile * Game.TILES_SIZE;
         int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
         return tileXPos + xOffset -1;
      }else {
         // Left
         return currentTile * Game.TILES_SIZE;
      }
    }
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed) {
      int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
      if(airSpeed > 0) {
         // Falling - touching floor
         int tileYPos = currentTile * Game.TILES_SIZE;
         int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
         return tileYPos + yOffset - 1;
      }else {
         // Jumping
         return currentTile * Game.TILES_SIZE;
      }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox,int[][] lvlData) {
      //check the pixel below bottomLeft and bottomRight
      if(!IsSolid(hitbox.x,hitbox.y + hitbox.height + 1,lvlData)) 
          if(!IsSolid(hitbox.x + hitbox.width,hitbox.y + hitbox.height + 1, lvlData))
              return false;
      return true;
    }

    public static boolean InFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
         if(xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
         return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

   public static boolean IsAllTileWalkable(int xStart, int xEnd, int[][] lvlData, int yTIle) {
      for(int i=xStart; i<xEnd; i++) {
         if(IsTileSolid(i, yTIle, lvlData))
            return false;

         if(!IsTileSolid(i, yTIle + 1, lvlData))
            return false;
      }

      return true;
   }

   public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, int yTile) {
      int enemyXTile = (int)(enemyHitbox.x / Game.TILES_SIZE);
      int playerXTile = (int)(playerHitbox.x / Game.TILES_SIZE);

      if(enemyXTile < playerXTile) 
        return IsAllTileWalkable(enemyXTile, playerXTile, lvlData, yTile); 
      else 
        return IsAllTileWalkable(playerXTile, enemyXTile, lvlData, yTile);
        
   }

   public static int[][] GetLevelData(BufferedImage img){
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

  public static ArrayList<Carbby> GetCrabs(BufferedImage img) {
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
   public static Point GetPlayerSpawn(BufferedImage img) {
      for(int i=0; i < img.getHeight(); i++)
      for(int j=0; j < img.getWidth(); j++) {
          Color color = new Color(img.getRGB(j, i));
          int value = color.getGreen();
          if (value == 100)
              return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
      }
      return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
   }
}
