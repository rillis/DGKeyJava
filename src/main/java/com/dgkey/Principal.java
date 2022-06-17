package com.dgkey;

import com.dgkey.gui.Loading;
import com.dgkey.gui.MainWindow;
import com.dgkey.gui.Screen;
import com.dgkey.logic.Key;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.net.MalformedURLException;
import java.util.Arrays;

public class Principal {
    public static int[] position;
    public static MainWindow mainWindow = null;

    public static void main(String[] args) throws Exception {
        debugLaF();

        Loading.init();
        while(!Loading.start){
            Thread.sleep(1);
        }
        Loading.setCancelMessage("Abortar");
        Loading.setMessage("Procurando mapa...");

        Screen.init();
        Key.init();

        tryAgain();
    }

    public static void tryAgain(){
        Loading.setMessage("Procurando mapa...");
        Loading.setColor(Color.BLACK);
        Loading.setCancelMessage("Abortar");
        BufferedImage mapImage = Screen.getMapImage();


        if(mapImage != null){
            Loading.close();
            mainWindow = new MainWindow(mapImage);
        }else{
            Loading.setMessage("Mapa não encontrado");
            Loading.setColor(Color.red);
            Loading.setCancelMessage("Retry");
        }
    }

    public static void debugLaF(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }
    }
}
