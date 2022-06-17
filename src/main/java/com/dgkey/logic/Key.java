package com.dgkey.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key {
    public static BufferedImage[][] keys = new BufferedImage[8][8];
    public static int[] selectedButton = null;


    public static void init(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    keys[i][j] = ImageIO.read(Key.class.getClassLoader().getResource("key ("+(i*8+j+1)+").png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
