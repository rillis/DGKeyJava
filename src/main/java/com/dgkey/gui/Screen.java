package com.dgkey.gui;

import com.dgkey.Principal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class Screen {

    private static int[] findSubimage(BufferedImage im1, BufferedImage im2){
        int w1 = im1.getWidth(); int h1 = im1.getHeight();
        int w2 = im2.getWidth(); int h2 = im2.getHeight();
        assert(w2 <= w1 && h2 <= h1);
        // will keep track of best position found
        int bestX = 0; int bestY = 0; double lowestDiff = Double.POSITIVE_INFINITY;
        // brute-force search through whole image (slow...)
        for(int x = 0;x < w1-w2;x++){
            for(int y = 0;y < h1-h2;y++){
                double comp = compareImages(im1.getSubimage(x,y,w2,h2),im2);
                if(comp < lowestDiff){
                    bestX = x; bestY = y; lowestDiff = comp;
                }

                if((int) Math.round(lowestDiff*100) <= 5){
                    return new int[]{bestX,bestY, (int) Math.round(lowestDiff*100)};
                }
            }
        }
        // return best location
        return new int[]{bestX,bestY, (int) Math.round(lowestDiff*100)};
    }
    private static double compareImages(BufferedImage im1, BufferedImage im2){
        assert(im1.getHeight() == im2.getHeight() && im1.getWidth() == im2.getWidth());
        double variation = 0.0;
        for(int x = 0;x < im1.getWidth();x++){
            for(int y = 0;y < im1.getHeight();y++){
                variation += compareARGB(im1.getRGB(x,y),im2.getRGB(x,y))/Math.sqrt(3);
            }
        }
        return variation/(im1.getWidth()*im1.getHeight());
    }
    private static double compareARGB(int rgb1, int rgb2){
        double r1 = ((rgb1 >> 16) & 0xFF)/255.0; double r2 = ((rgb2 >> 16) & 0xFF)/255.0;
        double g1 = ((rgb1 >> 8) & 0xFF)/255.0;  double g2 = ((rgb2 >> 8) & 0xFF)/255.0;
        double b1 = (rgb1 & 0xFF)/255.0;         double b2 = (rgb2 & 0xFF)/255.0;
        double a1 = ((rgb1 >> 24) & 0xFF)/255.0; double a2 = ((rgb2 >> 24) & 0xFF)/255.0;
        return a1*a2*Math.sqrt((r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2));
    }
    public static Robot robot = null;

    public static void init(){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    public static int[] getMapOnScreen(){
        try {
            URL url = Screen.class.getClassLoader().getResource("x.png");
            Dimension size = Toolkit. getDefaultToolkit(). getScreenSize();
            BufferedImage screenImage = robot.createScreenCapture(new Rectangle(0,0, size.width, size.height));
            BufferedImage xImage = ImageIO.read(url);
            int[] response = findSubimage(screenImage, xImage);
            if(response[2]<=5){
                Principal.position = new int[]{response[0], response[1]};
                return new int[]{response[0], response[1]};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{-1,-1};
    }

    public static BufferedImage getMapImage(){
        return getMapImage(getMapOnScreen());
    }
    public static BufferedImage getMapImage(int[] i){
        if(i[0]>0){
            return Screen.robot.createScreenCapture(new Rectangle(i[0]-268, i[1]-8, 280, 280));
        }
        return null;
    }


}
