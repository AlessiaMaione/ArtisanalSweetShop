package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl.InputOrdine;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl.OutputOrdine;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl.OutputPagamentoFallito;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl.ProdottoQuantita;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ProdottoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ProdottoEntity;

public class GUICliente{

    private static class CarrelloItem {
        String codiceProdotto;
        String nome;
        double prezzo;
        int quantita;

        CarrelloItem(String codice, String nome, double prezzo, int quantita) {
            this.codiceProdotto = codice;
            this.nome = nome;
            this.prezzo = prezzo;
            this.quantita = quantita;
        }

        double getTotale() {
            return prezzo * quantita;
        }
    }

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField userField, indirizzoField, scontoField, cartaField, quantitaField;
    private JTextArea carrelloArea;
    private List<CarrelloItem> carrello = new ArrayList<>();
    private int carrelloId = -1;

    public GUICliente() {
        frame = new JFrame("Artisanal Sweet Shopping - Acquisto Online");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setLayout(new BorderLayout());

        String[] colonne = {"Codice", "Nome", "Prezzo", "Disponibile"};
        tableModel = new DefaultTableModel(colonne, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        quantitaField = new JTextField("1", 5);
        JButton btnAggiungi = new JButton("Aggiungi al carrello");
        JPanel pannelloSuperiore = new JPanel(new FlowLayout());
        pannelloSuperiore.add(new JLabel("Quantità:"));
        pannelloSuperiore.add(quantitaField);
        pannelloSuperiore.add(btnAggiungi);

        userField = new JTextField(15);
        indirizzoField = new JTextField(15);
        scontoField = new JTextField(15);
        cartaField = new JTextField(15);

        JPanel pannelloDati = new JPanel(new GridLayout(5, 2));
        pannelloDati.add(new JLabel("Nome utente:")); pannelloDati.add(userField);
        pannelloDati.add(new JLabel("Indirizzo:")); pannelloDati.add(indirizzoField);
        pannelloDati.add(new JLabel("Codice sconto:")); pannelloDati.add(scontoField);
        pannelloDati.add(new JLabel("Carta di credito:")); pannelloDati.add(cartaField);

        carrelloArea = new JTextArea(10, 30);
        carrelloArea.setEditable(false);
        JScrollPane carrelloScroll = new JScrollPane(carrelloArea);

        JButton btnCheckout = new JButton("Checkout");
        JButton btnConferma = new JButton("Conferma ordine");
        JButton btnAnnulla = new JButton("Annulla carrello");

        JPanel pannelloBottoni = new JPanel(new FlowLayout());
        pannelloBottoni.add(btnCheckout);
        pannelloBottoni.add(btnConferma);
        pannelloBottoni.add(btnAnnulla);

        JPanel pannelloEst = new JPanel(new BorderLayout());
        pannelloEst.add(pannelloDati, BorderLayout.NORTH);
        pannelloEst.add(carrelloScroll, BorderLayout.CENTER);
        pannelloEst.add(pannelloBottoni, BorderLayout.SOUTH);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(pannelloSuperiore, BorderLayout.NORTH);
        frame.add(pannelloEst, BorderLayout.EAST);

        caricaProdotti();
        frame.setVisible(true);

        btnAggiungi.addActionListener(e -> {
            int riga = table.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona un prodotto.");
                return;
            }

            try {
                int quantita = Integer.parseInt(quantitaField.getText());
                if (quantita <= 0) throw new NumberFormatException();
                String codice = (String) tableModel.getValueAt(riga, 0);
                String nome = (String) tableModel.getValueAt(riga, 1);
                double prezzo = Double.parseDouble(tableModel.getValueAt(riga, 2).toString());

                carrello.add(new CarrelloItem(codice, nome, prezzo, quantita));
                carrelloArea.append(nome + " x" + quantita + " (€" + (prezzo * quantita) + ")\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Quantità non valida.");
            }
        });

        btnCheckout.addActionListener(e -> {
            InputOrdine input = new InputOrdine();
            input.nomeUtente = userField.getText().trim();
            input.indirizzo = indirizzoField.getText().trim();
            input.codiceSconto = scontoField.getText().trim();
            input.cartaCredito = cartaField.getText().trim();

            List<ProdottoQuantita> prodotti = new ArrayList<>();
            for (CarrelloItem item : carrello) {
                ProdottoQuantita pq = new ProdottoQuantita();
                pq.codiceProdotto = item.codiceProdotto;
                pq.quantita = item.quantita;
                prodotti.add(pq);
            }
            input.prodotti = prodotti;

            try {
                Object res = new AcquistoControl().avviaOrdine(input);
                if (res instanceof OutputOrdine ord) {
                    carrelloId = ord.idCarrello;
                    carrelloArea.append("Totale: €" + ord.totale + "\n");
                } else if (res instanceof OutputPagamentoFallito fail) {
                    carrelloArea.append( fail.messaggio + "\n " + fail.suggerimento + "\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore nel checkout: " + ex.getMessage());
            }
        });

        btnConferma.addActionListener(e -> {
            if (carrelloId == -1) {
                JOptionPane.showMessageDialog(frame, "Effettua prima il checkout.");
                return;
            }

            try {
                Object res = new AcquistoControl().confermaOrdine(carrelloId);
                if (res instanceof String msg) {
                    JOptionPane.showMessageDialog(frame, msg);
                    carrello.clear();
                    carrelloId = -1;
                    carrelloArea.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore nella conferma: " + ex.getMessage());
            }
        });

        btnAnnulla.addActionListener(e -> {
            if (carrelloId == -1) {
                JOptionPane.showMessageDialog(frame, "Nessun carrello da annullare.");
                return;
            }

            try {
                String msg = new AcquistoControl().annullaOrdine(carrelloId);
                JOptionPane.showMessageDialog(frame, msg);
                carrello.clear();
                carrelloId = -1;
                carrelloArea.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore nell'annullamento: " + ex.getMessage());
            }
        });
    }

    private void caricaProdotti() {
        try {
            List<ProdottoEntity> prodotti = ProdottoDAO.leggiTuttiProdotti();
            for (ProdottoEntity p : prodotti) {
                tableModel.addRow(new Object[] {
                        p.getCodiceProdotto(), p.getNome(), p.getPrezzo(), p.getQuantitaDisponibile()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dei prodotti.");
        }
    }

    public static void main(String[] args) {
        new GUICliente();
    }
}

