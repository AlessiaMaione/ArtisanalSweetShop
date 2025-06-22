package com.example.ArtisanalSweetShopping.GUI;

import javax.swing.*;
import java.awt.*;

public class GUIImpiegatoMenu {

    private JFrame frame;

    public GUIImpiegatoMenu() {
        frame = new JFrame("Pannello Impiegato - Seleziona Operazione");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JLabel titolo = new JLabel("Seleziona l'area da gestire", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(titolo, BorderLayout.NORTH);

        JButton btnMagazzino = new JButton("Gestione Magazzino");
        JButton btnSconti = new JButton("Gestione Sconti");
        JButton btnReport = new JButton("Report");
        JButton btnOrdini = new JButton("Gestione Ordini");
        JButton btnEsci = new JButton("Esci");

        JPanel bottoniPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        bottoniPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        bottoniPanel.add(btnMagazzino);
        bottoniPanel.add(btnSconti);
        bottoniPanel.add(btnReport);
        bottoniPanel.add(btnOrdini);
        bottoniPanel.add(btnEsci);

        frame.add(bottoniPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnMagazzino.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Modulo magazzino in costruzione!");
            // new GUIGestioneMagazzino(); // quando disponibile
        });

        btnSconti.addActionListener(e -> {
            frame.dispose();
            new GUIGestioneSconti();
        });

        btnReport.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Sezione report ancora da glassare...");
        });

        btnOrdini.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Gestione ordini in arrivo!");
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
