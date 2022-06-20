package com.dgkey.gui;

import com.dgkey.Principal;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame {
    private static Loading loading;
    private final JButton cancel;
    private final JLabel load;

    public static boolean start = false;

    public Loading(){
        setAlwaysOnTop(true);

        setTitle("DGKeyJava by Rillis");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setUndecorated(false);
        setSize(300, 140);

        JPanel contentPane = new JPanel();
        contentPane.setBounds(0,0, getWidth(), getHeight());
        contentPane.setLayout(null);
        setContentPane(contentPane);

        load = new JLabel("Clique em iniciar para começar");
        load.setBounds(0, 30, 300, 20);
        load.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(load);

        cancel = new JButton("Iniciar");
        cancel.setBounds(100, 60, 100, 20);
        cancel.addActionListener(e -> {
            if(cancel.getText().equals("Iniciar")) start=true;
            else if(cancel.getText().equals("Abortar")) System.exit(0);
            else{
                new Thread(Principal::tryAgain).start();
            }
        });
        contentPane.add(cancel);


        setVisible(true);

    }
    public static void setMessage(String msg){
        loading.load.setText(msg);
        loading.repaint();
    }

    public static void setCancelMessage(String msg){
        loading.cancel.setText(msg);
        loading.repaint();
    }

    public static void setColor(Color color){
        loading.load.setForeground(color);
        loading.repaint();
    }

    public static void setAlwaysTop(boolean value){
        loading.setAlwaysOnTop(value);
    }

    public static void init(){
        new Thread(() -> loading = new Loading()).start();
    }

    public static void close(){
        loading.setVisible(false);
    }
}
