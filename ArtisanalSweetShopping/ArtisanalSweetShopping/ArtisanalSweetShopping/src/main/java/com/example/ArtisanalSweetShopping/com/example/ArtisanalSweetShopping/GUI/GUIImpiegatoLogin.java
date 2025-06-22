package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ImpiegatoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import java.awt.*;

public class GUIImpiegatoLogin {

    private JFrame frame;
    private JTextField idField;
    private JPasswordField passwordField;
    public static int idLoggato = -1;

    public GUIImpiegatoLogin() {
        frame = new JFrame("Login Impiegato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 230);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titolo = new JLabel("Accesso Impiegato", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(titolo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(2, 2, 10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        idField = new JTextField();
        passwordField = new JPasswordField();

        centro.add(new JLabel("ID Impiegato:")); centro.add(idField);
        centro.add(new JLabel("Password:")); centro.add(passwordField);

        JButton accedi = new JButton("Accedi");
        JPanel bottom = new JPanel(); bottom.add(accedi);

        accedi.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String pw = new String(passwordField.getPassword()).trim();

                if (ImpiegatoDAO.verificaCredenziali(id, pw)) {
                    idLoggato = id;
                    frame.dispose();
                    new GUIImpiegatoMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Inserisci un ID numerico valido.");
            } catch (DAOException | DBConnectionException ex) {
                JOptionPane.showMessageDialog(frame, "Errore durante la connessione: " + ex.getMessage());
            }
        });

        frame.add(centro, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static int getIdLoggato() {
        return idLoggato;
    }

    public static void main(String[] args) {
        new GUIImpiegatoLogin();
    }
}
