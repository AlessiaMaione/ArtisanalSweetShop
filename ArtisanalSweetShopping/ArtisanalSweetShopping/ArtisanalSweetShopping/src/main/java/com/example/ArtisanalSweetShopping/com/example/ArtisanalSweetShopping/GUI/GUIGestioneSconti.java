package com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.Database.ScontoDAO;
import com.example.ArtisanalSweetShopping.Entity.ScontoEntity;
import com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUIGestioneSconti {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField codiceField, percentualeField, idImpiegatoField;
    private JCheckBox utilizzatoBox;

    public GUIGestioneSconti() {
        frame = new JFrame("Gestione Sconti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout());

        String[] colonne = {"Codice", "Percentuale", "ID Impiegato", "Utilizzato"};
        model = new DefaultTableModel(colonne, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel pannelloCampi = new JPanel(new GridLayout(4, 2, 10, 10));
        codiceField = new JTextField();
        percentualeField = new JTextField();
        idImpiegatoField = new JTextField();
        utilizzatoBox = new JCheckBox("Utilizzato");

        pannelloCampi.add(new JLabel("Codice Sconto:")); pannelloCampi.add(codiceField);
        pannelloCampi.add(new JLabel("Percentuale:")); pannelloCampi.add(percentualeField);
        pannelloCampi.add(new JLabel("ID Impiegato:")); pannelloCampi.add(idImpiegatoField);
        pannelloCampi.add(new JLabel("")); pannelloCampi.add(utilizzatoBox);

        JButton btnAggiungi = new JButton("Crea Sconto");
        JButton btnModifica = new JButton("Modifica Sconto");
        JButton btnElimina = new JButton("Elimina Sconto");
        JButton btnIndietro = new JButton("Indietro");

        JPanel pannelloBottoni = new JPanel(new FlowLayout());
        pannelloBottoni.add(btnAggiungi);
        pannelloBottoni.add(btnModifica);
        pannelloBottoni.add(btnElimina);
        pannelloBottoni.add(btnIndietro);

        JPanel pannelloEst = new JPanel(new BorderLayout(10, 10));
        pannelloEst.add(pannelloCampi, BorderLayout.NORTH);
        pannelloEst.add(pannelloBottoni, BorderLayout.SOUTH);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(pannelloEst, BorderLayout.EAST);

        caricaSconti();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnAggiungi.addActionListener(e -> {
            try {
                ScontoEntity sconto = raccogliDatiSconto();
                ScontoDAO.inserisciSconto(sconto);
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

            String codice = (String) model.getValueAt(riga, 0);
            int conferma = JOptionPane.showConfirmDialog(frame, "Eliminare lo sconto \"" + codice + "\"?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (conferma == JOptionPane.YES_OPTION) {
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
                idImpiegatoField.setText(model.getValueAt(i, 2).toString());
                utilizzatoBox.setSelected(Boolean.parseBoolean(model.getValueAt(i, 3).toString()));
            }
        });
    }

    private ScontoEntity raccogliDatiSconto() {
        String codice = codiceField.getText().trim();
        float perc = Float.parseFloat(percentualeField.getText().trim());
        int id = Integer.parseInt(idImpiegatoField.getText().trim());
        boolean usato = utilizzatoBox.isSelected();

        return new ScontoEntity(codice, perc, id, usato);
    }

    private void caricaSconti() {
        model.setRowCount(0);
        try {
            List<ScontoEntity> lista = ScontoDAO.leggiTuttiSconti();
            for (ScontoEntity s : lista) {
                model.addRow(new Object[] {
                        s.getCodiceSconto(),
                        s.getPercentuale(),
                        s.getIdImpiegato(),
                        s.isUtilizzato()
                });
            }
        } catch (DAOException | DBConnectionException e) {
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento sconti: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new GUIGestioneSconti();
    
