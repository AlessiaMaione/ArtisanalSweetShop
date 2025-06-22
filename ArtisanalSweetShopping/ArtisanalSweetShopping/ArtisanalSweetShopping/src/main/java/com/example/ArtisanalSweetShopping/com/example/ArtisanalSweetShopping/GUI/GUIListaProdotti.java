package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ProdottoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ProdottoEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIListaProdotti {

    private final List<GUIArticolo> carrello = new ArrayList<>();

    public GUIListaProdotti(String nomeUtente) {
        JFrame frame = new JFrame("Benvenuta " + nomeUtente + " - Prodotti disponibili");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        String[] colonne = {"Codice", "Nome", "Prezzo"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        JTable tabella = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabella);

        // Carica prodotti da DB
        try {
            List<ProdottoEntity> prodotti = ProdottoDAO.leggiTuttiProdotti();
            for (ProdottoEntity p : prodotti) {
                if (p.getQuantitaDisponibile() > 0) {
                    model.addRow(new Object[]{
                        p.getCodiceProdotto(),
                        p.getNome(),
                        p.getPrezzo()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dei prodotti: " + e.getMessage());
        }

        JPanel top = new JPanel();
        JLabel quantitàLabel = new JLabel("1");
        int[] quantità = {1};

        JButton meno = new JButton("−");
        JButton più = new JButton("+");

        meno.addActionListener(e -> {
            if (quantità[0] > 1) {
                quantità[0]--;
                quantitàLabel.setText(String.valueOf(quantità[0]));
            }
        });

        più.addActionListener(e -> {
            quantità[0]++;
            quantitàLabel.setText(String.valueOf(quantità[0]));
        });

        JButton aggiungi = new JButton("Aggiungi al carrello");
        JButton carrelloBtn = new JButton("🛒 Carrello");

        top.add(new JLabel("Quantità:"));
        top.add(meno);
        top.add(quantitàLabel);
        top.add(più);
        top.add(aggiungi);
        top.add(carrelloBtn);

        aggiungi.addActionListener(e -> {
            int riga = tabella.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona un prodotto.");
                return;
            }

            try {
                String codice = model.getValueAt(riga, 0).toString();
                String nome = model.getValueAt(riga, 1).toString();
                double prezzo = Double.parseDouble(model.getValueAt(riga, 2).toString());

                carrello.add(new GUIArticolo(codice, nome, prezzo, quantità[0]));
                JOptionPane.showMessageDialog(frame, "Prodotto aggiunto al carrello!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore durante l'aggiunta: " + ex.getMessage());
            }
        });

        carrelloBtn.addActionListener(e -> new GUICarrello(frame, carrello, nomeUtente));

        frame.add(top, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static class GUIArticolo {
        String codice, nome;
        double prezzo;
        int quantita;

        public GUIArticolo(String codice, String nome, double prezzo, int quantita) {
            this.codice = codice;
            this.nome = nome;
            this.prezzo = prezzo;
            this.quantita = quantita;
        }

        public double totale() {
            return prezzo * quantita;
        }

        @Override
        public String toString() {
            return nome + " x" + quantita + " (€" + String.format("%.2f", totale()) + ")";
        }
    }

    public static class GUIArticoloAccessor {
        public static String getCodice(GUIArticolo a) { return a.codice; }
        public static String getNome(GUIArticolo a) { return a.nome; }
        public static double getPrezzo(GUIArticolo a) { return a.prezzo; }
        public static int getQuantita(GUIArticolo a) { return a.quantita; }
        public static void setQuantita(GUIArticolo a, int q) { a.quantita = q; }
        public static double getTotale(GUIArticolo a) { return a.totale(); }
        public static String toString(GUIArticolo a) { return a.toString(); }
    }
}

