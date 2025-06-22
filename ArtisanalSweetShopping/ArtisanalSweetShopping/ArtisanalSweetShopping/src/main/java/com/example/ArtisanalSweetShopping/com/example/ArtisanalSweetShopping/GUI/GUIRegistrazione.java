package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ClienteRegistratoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ClienteRegistratoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import java.awt.*;

public class GUIRegistrazione {

    private JFrame frame;
    private JTextField usernameField, emailField, telefonoField, cartaField;
    private JPasswordField passwordField;

    public GUIRegistrazione() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Registrazione Cliente");
        frame.setBounds(100, 100, 400, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 2, 5, 5));

        JLabel lblUsername = new JLabel("Nome utente:");
        JLabel lblPassword = new JLabel("Password:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblTelefono = new JLabel("Telefono:");
        JLabel lblCarta = new JLabel("Numero carta:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        telefonoField = new JTextField();
        cartaField = new JTextField();

        JButton registrati = new JButton("Registrati");
        JButton annulla = new JButton("Annulla");

        frame.add(lblUsername); frame.add(usernameField);
        frame.add(lblPassword); frame.add(passwordField);
        frame.add(lblEmail); frame.add(emailField);
        frame.add(lblTelefono); frame.add(telefonoField);
        frame.add(lblCarta); frame.add(cartaField);
        frame.add(registrati); frame.add(annulla);

        // Azione Registrazione
        registrati.addActionListener(e -> {
            try {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String email = emailField.getText().trim();
                String telefono = telefonoField.getText().trim();
                String numeroCarta = cartaField.getText().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || telefono.isEmpty() || numeroCarta.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Compila tutti i campi");
                    return;
                }

                if (ClienteRegistratoDAO.leggiCliente(username) != null) {
                    JOptionPane.showMessageDialog(frame, "Nome utente già registrato");
                    return;
                }

                ClienteRegistratoEntity nuovoCliente = new ClienteRegistratoEntity(
                        username,
                        password,
                        email,
                        telefono,
                        0,
                        numeroCarta
                );

                ClienteRegistratoDAO.creaCliente(nuovoCliente);
                JOptionPane.showMessageDialog(frame, "Registrazione completata con successo");
                frame.dispose();
                new GUILogin();

            } catch (DAOException | DBConnectionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage());
            }
        });

        annulla.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIRegistrazione::new);
    }
}
