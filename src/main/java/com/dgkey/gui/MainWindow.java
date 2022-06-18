package com.dgkey.gui;

import com.dgkey.*;
import com.dgkey.db.*;
import com.dgkey.logic.Key;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.sql.*;

public class MainWindow extends JFrame {
    JPanel contentPane;
    JLabel map;
    JLabel[][] labelsMap = new JLabel[8][8];

    public MainWindow(BufferedImage mapImage){
        setAlwaysOnTop(true);

        setTitle("DGKeyJava by Rillis");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        setSize(600, 360);

        contentPane = new JPanel();
        contentPane.setBounds(0,0,getWidth(), getHeight());
        contentPane.setLayout(null);
        setContentPane(contentPane);

        genLabels(10, 30);

        map = new JLabel("a");
        map.setIcon(new ImageIcon(mapImage));
        map.setBounds(10, 30, 280, 280);
        map.setOpaque(true);
        contentPane.add(map);

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try {
                    Principal.database.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        JButton reloadMap = new JButton("Reload");
        reloadMap.setBounds(10,10,100,20);
        reloadMap.addActionListener(e -> {
            map.setIcon(new ImageIcon(Screen.getMapImage(Principal.position)));
            repaint();
        });
        contentPane.add(reloadMap);

        JButton clearAll = new JButton("Clear");
        clearAll.setBounds(130,10,100,20);
        clearAll.addActionListener(e -> {
            try {
                Principal.database.truncate("oper");
                Principal.database.insert("oper", new String[]{"operacao", "posicao", "time"}, new Object[]{"clear", "0,0", System.currentTimeMillis()});
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(clearAll);

        JToggleButton topToggle = new JToggleButton("Always on Top: on");
        topToggle.setBounds(250,10,150,20);
        topToggle.setSelected(true);
        topToggle.addActionListener(e -> {
            if(topToggle.isSelected()) topToggle.setText("Always on Top: on");
            else topToggle.setText("Always on Top: off");
            setAlwaysOnTop(topToggle.isSelected());
        });
        contentPane.add(topToggle);

        JToggleButton numbers = new JToggleButton("Numbers: off");
        numbers.setBounds(430,10,100,20);
        numbers.setSelected(false);
        numbers.addActionListener(e -> {
            if(numbers.isSelected()) numbers.setText("Numbers: on");
            else numbers.setText("Numbers: off");
            for (JLabel[] labels : labelsMap) {
                for(JLabel label : labels){
                    int a = 255;
                    if (!numbers.isSelected()) a = 0;
                    label.setForeground(new Color(0,255,0,a));
                }
            }
        });
        contentPane.add(numbers);

        genButtons();

        setVisible(true);


        //------------- Primeira run
        PreparedStatement pr = null;
        ResultSet rs = null;
        try{
            pr = Principal.database.connection.prepareStatement("SELECT * FROM oper");
            rs = pr.executeQuery();
            while(rs.next()){
                Comando comando = new Comando(rs.getString("operacao"), new Mark(rs.getString("posicao"), rs.getString("tipo")));
                System.out.println(comando.toString());
                runCmd(comando);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(pr!=null){
                try{pr.close();}catch (Exception e){e.printStackTrace();}
            }
            if(rs!=null){
                try{rs.close();}catch (Exception e){e.printStackTrace();}
            }
        }
        /**
        final long[] timesRun = {0};
        final long[] time1 = {System.currentTimeMillis()};
        */

        final long[] lastRead = {System.currentTimeMillis()};
        new Thread(() -> {
            while(true){
                map.setIcon(new ImageIcon(Screen.getMapImage(Principal.position)));
                repaint();

                PreparedStatement prr = null;
                ResultSet rss = null;
                try{
                    prr = Principal.database.connection.prepareStatement("SELECT * FROM oper WHERE time >= "+ lastRead[0]);
                    rss = prr.executeQuery();
                    while(rss.next()){
                        Comando comando = new Comando(rss.getString("operacao"), new Mark(rss.getString("posicao"), rss.getString("tipo")));
                        runCmd(comando);
                        lastRead[0] = Math.max(rss.getLong("time"), lastRead[0]);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    if(prr!=null){
                        try{prr.close();}catch (Exception e){e.printStackTrace();}
                    }
                    if(rss!=null){
                        try{rss.close();}catch (Exception e){e.printStackTrace();}
                    }
                }

                /** ---
                    timesRun[0] +=1;
                    if(System.currentTimeMillis()- time1[0] >=1000){
                        System.out.println(timesRun[0]);
                        time1[0] = System.currentTimeMillis();
                        timesRun[0] = 0;
                    }
                */

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void genLabels(int xI, int yI){
        int xOff = 14;
        int yOff = 19;

        int xAdd = 32;
        int yAdd = 32;

        int w = 27;
        int h = 27;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                labelsMap[i][j] = new JLabel();
                //labelsMap[i][j].setBorder(BorderFactory.createLineBorder(Color.GREEN));
                labelsMap[i][j].setBounds(xI + xOff + (xAdd*i),yI + yOff + (yAdd*j), w, h);
                int finalJ = j;
                int finalI = i;
                labelsMap[i][j].setText(""+(j*8+i+1));
                labelsMap[i][j].setForeground(new Color(0,255,0,0));
                labelsMap[i][j].setHorizontalAlignment(JLabel.CENTER);
                labelsMap[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if(Key.selectedButton == null){
                            try {
                                Principal.database.insert("oper", new String[]{"operacao", "posicao", "time"}, new Object[]{"remove", finalI+","+finalJ, System.currentTimeMillis()});
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }else{
                            try {
                                Principal.database.insert("oper", new String[]{"operacao", "posicao", "tipo", "time"}, new Object[]{"create", finalI+","+finalJ, Key.selectedButton[0]+","+Key.selectedButton[1], System.currentTimeMillis()});
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        repaint();
                        clearButtonSelection();
                        Key.selectedButton = null;
                    }
                });
                contentPane.add(labelsMap[i][j]);
            }
        }
    }

    private JLabel[][] buttons = new JLabel[9][8];

    private void genButtons(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.println(i+"|"+j);
                if(Key.keys[i][j] != null){
                    System.out.println(i+"|"+j);
                    buttons[i][j] = new JLabel();
                    buttons[i][j].setBounds(310+(j*32),30+(i*32), 27, 27);
                    buttons[i][j].setIcon(new ImageIcon(Key.keys[i][j]));
                    buttons[i][j].setToolTipText(Key.tooltips[i][j]);
                    int finalI = i;
                    int finalJ = j;
                    buttons[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (buttons[finalI][finalJ].getBorder() != null) {
                                Key.selectedButton = null;
                                buttons[finalI][finalJ].setBorder(null);
                            } else {
                                clearButtonSelection();
                                Key.selectedButton = new int[]{finalI, finalJ};
                                buttons[finalI][finalJ].setBorder(BorderFactory.createLineBorder(Color.GREEN));
                            }
                            repaint();
                        }});
                    contentPane.add(buttons[i][j]);
                }
            }
        }
    }

    private void clearButtonSelection(){
        for (JLabel[] buttonArrays : buttons) {
            for(JLabel button : buttonArrays){
                if(button!=null) button.setBorder(null);
            }
        }
        repaint();
    }

    private void clearLabels(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                labelsMap[i][j].setIcon(null);
                labelsMap[i][j].setHorizontalAlignment(JLabel.CENTER);
                labelsMap[i][j].setToolTipText(null);
            }
        }
        repaint();
    }

    private void runCmd(Comando cmd){
        if(cmd.operacao.equals("create")){
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setIcon(new ImageIcon(Key.keys[cmd.mark.type_int[0]][cmd.mark.type_int[1]]));
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setHorizontalAlignment(JLabel.LEADING);
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setToolTipText(Key.tooltips[cmd.mark.type_int[0]][cmd.mark.type_int[1]]);
            repaint();
        }
        else if(cmd.operacao.equals("remove")){
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setIcon(null);
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setHorizontalAlignment(JLabel.CENTER);
            labelsMap[cmd.mark.position_int[0]][cmd.mark.position_int[1]].setToolTipText(null);
            repaint();
        }
        else if(cmd.operacao.equals("clear")){
            clearLabels();
        }
    }
}
