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
        showLabels();

        map = new JLabel("a");
        map.setIcon(new ImageIcon(mapImage));
        map.setBounds(10, 30, 280, 280);
        map.setOpaque(true);
        contentPane.add(map);

        JButton reloadMap = new JButton("Reload");
        reloadMap.setBounds(10,10,100,20);
        reloadMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map.setIcon(new ImageIcon(Screen.getMapImage(Principal.position)));
                repaint();
            }
        } );
        contentPane.add(reloadMap);

        genButtons();

        setVisible(true);
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
                labelsMap[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if(Key.selectedButton == null){
                            labelsMap[finalI][finalJ].setIcon(null);
                        }else{
                            labelsMap[finalI][finalJ].setIcon(new ImageIcon(Key.keys[Key.selectedButton[0]][Key.selectedButton[1]]));
                        }
                        repaint();
                        clearButtonSelection();
                        Key.selectedButton = null;
                    }
                });
            }
        }
    }

    private JButton[][] buttons = new JButton[8][8];

    private void genButtons(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(310+(i*32),30+(j*32), 27, 27);
                buttons[i][j].setIcon(new ImageIcon(Key.keys[i][j]));
                int finalI = i;
                int finalJ = j;
                buttons[i][j].addActionListener(e -> {
                    if(buttons[finalI][finalJ].isSelected()){
                        Key.selectedButton = null;
                        buttons[finalI][finalJ].setSelected(false);
                    }else{
                        clearButtonSelection();
                        Key.selectedButton = new int[]{finalI, finalJ};
                        buttons[finalI][finalJ].setSelected(true);
                    }
                    repaint();
                });
                contentPane.add(buttons[i][j]);
            }
        }
    }

    private void clearButtonSelection(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j].setSelected(false);
            }
        }
        repaint();
    }

    private void showLabels(){
        for (JLabel[] labels : labelsMap){
            for(JLabel label : labels){
                contentPane.add(label);
            }
        }
    }
}
