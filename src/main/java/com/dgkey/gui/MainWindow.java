package com.dgkey.gui;

import com.dgkey.Principal;

import com.dgkey.logic.Key;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
            map.setIcon(new ImageIcon(Screen.getMapImage(Principal.position)));
            clearLabels();
            repaint();
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

        new Thread(() -> {
            while(true){
                map.setIcon(new ImageIcon(Screen.getMapImage(Principal.position)));
                repaint();
                try {
                    Thread.sleep(500);
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
                            labelsMap[finalI][finalJ].setIcon(null);
                            labelsMap[finalI][finalJ].setHorizontalAlignment(JLabel.CENTER);
                        }else{
                            labelsMap[finalI][finalJ].setIcon(new ImageIcon(Key.keys[Key.selectedButton[0]][Key.selectedButton[1]]));
                            labelsMap[finalI][finalJ].setHorizontalAlignment(JLabel.LEADING);
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
            }
        }
        repaint();
    }
}
