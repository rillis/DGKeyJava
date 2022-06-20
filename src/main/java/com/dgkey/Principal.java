package com.dgkey;

import com.dgkey.db.*;
import com.dgkey.gui.*;
import com.dgkey.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Principal {
    public static int[] position;
    public static MainWindow mainWindow = null;
    public static Database database = null;

    public static void main(String[] args) throws Exception {
        debugLaF();

        Loading.init();
        while(!Loading.start){
            Thread.sleep(1);
        }
        Loading.setCancelMessage("Abortar");
        Loading.setMessage("Conectando...");
        Loading.setAlwaysTop(false);

        String ip = JOptionPane.showInputDialog("Digite o ip");

        try{
            database = new Database(ip, "3306", "runescape");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao conectar");
            e.printStackTrace();
            System.exit(0);
        }

        Loading.setAlwaysTop(true);
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
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }
}
