package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import javax.swing.*;
import java.awt.*;

public class GUIImpiegatoMenu {

    private JFrame frame;

    public GUIImpiegatoMenu() {
        frame = new JFrame("Pannello Impiegato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titolo = new JLabel("Seleziona un'area da gestire", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(titolo, BorderLayout.NORTH);

        JButton btnMagazzino = new JButton("Gestione Magazzino");
        JButton btnSconti = new JButton("Gestione Sconti");
        JButton btnReport = new JButton("Report");
        JButton btnOrdini = new JButton("Gestione Ordini");
        JButton btnEsci = new JButton("Logout");

        JPanel centro = new JPanel(new GridLayout(5, 1, 15, 15));
        centro.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        centro.add(btnMagazzino);
        centro.add(btnSconti);
        centro.add(btnReport);
        centro.add(btnOrdini);
        centro.add(btnEsci);

        frame.add(centro, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnMagazzino.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Gestione magazzino non ancora disponibile.");
        });

        btnSconti.addActionListener(e -> {
            frame.dispose();
            new GUIGestioneSconti();
        });

        btnReport.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Sezione report in sviluppo.");
        });

        btnOrdini.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Modulo ordini disponibile a breve.");
        });

        btnEsci.addActionListener(e -> {
            frame.dispose();
            new GUIImpiegatoLogin();
        });
    }

    public static void main(String[] args) {
        new GUIImpiegatoMenu();
    }
}
