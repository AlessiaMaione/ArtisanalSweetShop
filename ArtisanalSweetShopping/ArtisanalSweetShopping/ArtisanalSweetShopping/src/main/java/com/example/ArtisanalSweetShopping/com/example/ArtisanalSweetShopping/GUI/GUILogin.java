package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ClienteRegistratoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ClienteRegistratoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import java.awt.*;

public class GUILogin {

    public GUILogin() {
        JFrame frame = new JFrame("Login - Artisanal Sweet Shopping");
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Accedi");
        JButton registratiBtn = new JButton("Registrati");

        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Nome utente:")); form.add(usernameField);
        form.add(new JLabel("Password:")); form.add(passwordField);
        form.add(loginBtn); form.add(registratiBtn);

        frame.add(new JLabel("Inserisci le tue credenziali:", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(form, BorderLayout.CENTER);

        // Azione bottone login
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Inserisci username e password");
                return;
            }

            try {
                ClienteRegistratoEntity utente = ClienteRegistratoDAO.leggiCliente(username);
                if (utente == null) {
                    JOptionPane.showMessageDialog(frame, "Utente non registrato.");
                    return;
                }

                if (!utente.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Password errata.");
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Benvenuta " + username + "!");
                frame.dispose();
                new GUIListaProdotti(username);

            } catch (DAOException | DBConnectionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Errore durante il login:\n" + ex.getMessage());
            }
        });

        registratiBtn.addActionListener(e -> {
            frame.dispose();
            new GUIRegistrazione(); // Deve esistere una schermata di registrazione
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUILogin::new);
    }
}
