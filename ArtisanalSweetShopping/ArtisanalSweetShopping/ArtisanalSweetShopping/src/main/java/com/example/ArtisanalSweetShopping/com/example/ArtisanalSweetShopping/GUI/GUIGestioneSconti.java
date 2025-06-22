package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ScontoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ScontoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUIGestioneSconti {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField codiceField, percentualeField;
    private JCheckBox utilizzatoBox;

    public GUIGestioneSconti() {
        frame = new JFrame("Gestione Sconti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{"Codice Sconto", "Percentuale", "Utilizzato"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        codiceField = new JTextField();
        percentualeField = new JTextField();
        utilizzatoBox = new JCheckBox("Utilizzato");

        form.add(new JLabel("Codice Sconto:"));
        form.add(codiceField);
        form.add(new JLabel("Percentuale:"));
        form.add(percentualeField);
        form.add(new JLabel(""));
        form.add(utilizzatoBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAggiungi = new JButton("Crea");
        JButton btnModifica = new JButton("Modifica");
        JButton btnElimina = new JButton("Elimina");
        JButton btnIndietro = new JButton("Indietro");

        buttons.add(btnAggiungi);
        buttons.add(btnModifica);
        buttons.add(btnElimina);
        buttons.add(btnIndietro);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(form, BorderLayout.NORTH);
        rightPanel.add(buttons, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.EAST);

        btnAggiungi.addActionListener(e -> {
            try {
                ScontoEntity sconto = raccogliDatiSconto();
                ScontoDAO.creaSconto(sconto);
                caricaSconti();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore creazione: " + ex.getMessage());
            }
        });

        btnModifica.addActionListener(e -> {
            int riga = table.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona uno sconto da modificare.");
                return;
            }
            try {
                ScontoEntity sconto = raccogliDatiSconto();
                ScontoDAO.aggiornaSconto(sconto);
                caricaSconti();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore modifica: " + ex.getMessage());
            }
        });

        btnElimina.addActionListener(e -> {
            int riga = table.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona uno sconto da eliminare.");
                return;
            }
            String codice = model.getValueAt(riga, 0).toString();
            int ok = JOptionPane.showConfirmDialog(frame, "Eliminare lo sconto \"" + codice + "\"?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    ScontoDAO.eliminaSconto(codice);
                    caricaSconti();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Errore eliminazione: " + ex.getMessage());
                }
            }
        });

        btnIndietro.addActionListener(e -> {
            frame.dispose();
            new GUIImpiegatoMenu();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                codiceField.setText(model.getValueAt(i, 0).toString());
                percentualeField.setText(model.getValueAt(i, 1).toString());
                utilizzatoBox.setSelected(Boolean.parseBoolean(model.getValueAt(i, 2).toString()));
            }
        });

        caricaSconti();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private ScontoEntity raccogliDatiSconto() {
        String codice = codiceField.getText().trim();
        float perc = Float.parseFloat(percentualeField.getText().trim());
        boolean usato = utilizzatoBox.isSelected();
        int idImpiegato = GUIImpiegatoLogin.getIdLoggato();
        return new ScontoEntity(codice, perc, idImpiegato, usato);
    }

    private void caricaSconti() {
        model.setRowCount(0);
        try {
            List<ScontoEntity> lista = ScontoDAO.leggiTuttiSconti();
            for (ScontoEntity s : lista) {
                model.addRow(new Object[] {
                        s.getCodiceSconto(),
                        s.getPercentuale(),
                        s.isUtilizzato()
                });
            }
        } catch (DAOException | DBConnectionException e) {
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento sconti: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new GUIGestioneSconti();
    }
}
