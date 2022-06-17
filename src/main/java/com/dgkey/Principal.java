package com.dgkey;

import com.dgkey.gui.MainWindow;
import com.dgkey.gui.Screen;
import com.dgkey.logic.Key;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.Arrays;

public class Principal {
    public static int[] position;
    public static void main(String[] args) throws Exception {
        Thread.sleep(2);

        Screen.init();
        Key.init();

        BufferedImage mapImage = Screen.getMapImage();

        if(mapImage != null){
            new MainWindow(mapImage);
        }else{
            JOptionPane.showMessageDialog(null, "Não achei o mapa.");
        }


    }
}
