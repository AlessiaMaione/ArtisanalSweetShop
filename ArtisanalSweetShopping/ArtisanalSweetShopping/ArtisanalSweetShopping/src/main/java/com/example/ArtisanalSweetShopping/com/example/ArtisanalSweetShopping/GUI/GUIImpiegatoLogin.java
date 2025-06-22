package com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.Database.ImpiegatoDAO;
import com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import java.awt.*;

public class GUIImpiegatoLogin {

    private JFrame frame;
    private JTextField idField;
    private JPasswordField passwordField;

    public GUIImpiegatoLogin() {
        frame = new JFrame("Accesso Impiegato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titolo = new JLabel("Login Impiegato", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(titolo, BorderLayout.NORTH);

        JPanel campiPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        idField = new JTextField();
        passwordField = new JPasswordField();

        campiPanel.add(new JLabel("ID Impiegato:"));
        campiPanel.add(idField);
        campiPanel.add(new JLabel("Password:"));
        campiPanel.add(passwordField);

        JButton btnLogin = new JButton("Accedi");

        JPanel fondoPanel = new JPanel();
        fondoPanel.add(btnLogin);

        frame.add(campiPanel, BorderLayout.CENTER);
        frame.add(fondoPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnLogin.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String pw = new String(passwordField.getPassword()).trim();

                if (ImpiegatoDAO.verificaCredenziali(id, pw)) {
                    frame.dispose();
                    new GUIImpiegatoMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID non valido.");
            } catch (DAOException | DBConnectionException ex) {
                JOptionPane.showMessageDialog(frame, "Errore di connessione: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new GUIImpiegatoLogin();
    }
}
