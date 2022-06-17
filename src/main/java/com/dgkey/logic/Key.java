package com.dgkey.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key {
    public static BufferedImage[][] keys = new BufferedImage[9][8];
    public static int[] selectedButton = null;


    public static void init(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    System.out.println(""+(i*8+j+1));
                    keys[i][j] = ImageIO.read(Key.class.getClassLoader().getResource("key ("+(i*8+j+1)+").png"));
                    System.out.println(""+(i*8+j+1)+ " OK");
                } catch (Exception e) {
                    keys[i][j] = null;
                }
            }
        }
    }
}
