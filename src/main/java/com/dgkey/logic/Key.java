package com.dgkey.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key {
    public static BufferedImage[][] keys = new BufferedImage[9][8];
    public static String[][] tooltips = new String[9][8];
    public static int[] selectedButton = null;

    private static String[] d1 = new String[]{"Blue", "Crimson", "Gold", "Green", "Orange", "Purple", "Silver", "Yellow"};
    private static String[] d2 = new String[]{"Corner", "Crescent", "Diamond", "Pentagon", "Rectangle", "Shield", "Triangle", "Wedge"};
    private static String[] d3 = new String[]{"Level", "Group Gatestone", "Gatestone", "Gatestone 2", "Boss/Danger"};

    public static void init(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                tooltips[i][j] = "";

                if((i*8+j+1)<=64){
                    tooltips[i][j] = d1[i] + " " + d2[j];
                }else if((i*8+j+1)<=64+d3.length){
                    tooltips[i][j] = d3[(i*8+j+1)-65];
                }


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
