package com.unibra.game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Game extends JFrame {
    public static final boolean soundFX = false;
    public static void main(String[] args) { EventQueue.invokeLater(() -> new Game().setVisible(true)); }
    public Game() {
        super("Caça aos Cristais");
        Scenary panel = new Scenary();
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) { panel.pauseOnFocusLoss(); }
        });
    }
}
