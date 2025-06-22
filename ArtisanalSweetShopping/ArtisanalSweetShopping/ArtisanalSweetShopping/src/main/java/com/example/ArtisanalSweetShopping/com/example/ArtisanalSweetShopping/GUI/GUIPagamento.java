package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoControl;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.GUI.GUIListaProdotti.GUIArticolo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIPagamento {

    public GUIPagamento(double totale, List<GUIArticolo> carrello, String nomeUtente) {
        JFrame frame = new JFrame("Pagamento");
        frame.setSize(420, 300);
        frame.setLayout(new BorderLayout());

        JLabel titolo = new JLabel("Totale da pagare: €" + String.format("%.2f", totale), SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        frame.add(titolo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField indirizzo = new JTextField();
        JTextField carta = new JTextField();
        JTextField scontoField = new JTextField();  // Campo opzionale

        JButton conferma = new JButton("Conferma Ordine");

        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Indirizzo di consegna:")); form.add(indirizzo);
        form.add(new JLabel("Numero carta:")); form.add(carta);
        form.add(new JLabel("Codice sconto (opzionale):")); form.add(scontoField);
        form.add(new JLabel()); form.add(conferma);

        frame.add(form, BorderLayout.CENTER);

        conferma.addActionListener(e -> {
            if (indirizzo.getText().trim().isEmpty() || carta.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Compila tutti i campi obbligatori.");
                return;
            }

            try {
                // Preparo l'input
                AcquistoControl.InputOrdine input = new AcquistoControl.InputOrdine();
                input.nomeUtente = nomeUtente;
                input.indirizzo = indirizzo.getText().trim();
                input.cartaCredito = carta.getText().trim();
                input.codiceSconto = scontoField.getText().trim().isEmpty() ? null : scontoField.getText().trim();
                input.prodotti = new ArrayList<>();

                for (GUIArticolo a : carrello) {
                    AcquistoControl.ProdottoQuantita pq = new AcquistoControl.ProdottoQuantita();
                    pq.codiceProdotto = a.codice;
                    pq.quantita = a.quantita;
                    input.prodotti.add(pq);
                }

                Object avvio = new AcquistoControl().avviaOrdine(input);
                int idCarrello = -1;

                if (avvio instanceof AcquistoControl.OutputOrdine ord) {
                    idCarrello = ord.idCarrello;
                } else if (avvio instanceof AcquistoControl.OutputPagamentoFallito err) {
                    JOptionPane.showMessageDialog(frame, "Errore: " + err.messaggio + "\n" + err.suggerimento);
                    return;
                }

                Object confermaOrdine = new AcquistoControl().confermaOrdine(idCarrello);
                if (confermaOrdine instanceof String confermaMsg) {
                    JOptionPane.showMessageDialog(frame, confermaMsg + "\nGrazie per l'acquisto!");
                    frame.dispose();
                    new Main();
                } else if (confermaOrdine instanceof AcquistoControl.OutputPagamentoFallito err2) {
                    JOptionPane.showMessageDialog(frame, "Pagamento rifiutato: " + err2.messaggio + "\n" + err2.suggerimento);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Errore durante la conferma dell’ordine: " + ex.getMessage());
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

            JOptionPane.showMessageDialog(frame, "Ordine confermato! Grazie per l'acquisto.");
            frame.dispose();
            new Main(); // Torna all'inizio
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
