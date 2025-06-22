package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.*;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.*;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AcquistoOnlineControl {

    private static OrdineEntity ordineInAttesa;
    private static ArrayList<DettagliOrdineEntity> dettagliInAttesa = new ArrayList<>();

    public ArrayList<String> acquistoOnline(String nomeUtente, List<String> codiciProdotto, List<Integer> quantita, String indirizzo, String numeroCartaInput, ArrayList<Integer> idOrdini) throws OperationException {

        ClienteRegistratoEntity cliente = null;
        float prezzoSingolo;
        float prezzoTotale = 0;
        ArrayList<String> returnList = new ArrayList<>();
        returnList.add("0");     // prezzo totale
        returnList.add("null");  // numero carta

        try {
            cliente = ClienteRegistratoDAO.leggiCliente(nomeUtente);
            if (cliente == null) {
                throw new OperationException("Utente non registrato.");
            }

            String numeroCarta = (cliente.getNumeroCarta() != null && cliente.getNumeroCarta().length() >= 12)
                    ? cliente.getNumeroCarta()
                    : numeroCartaInput;

            if (numeroCarta == null || numeroCarta.length() < 12) {
                throw new OperationException("Carta di credito non valida.");
            }

            returnList.set(1, numeroCarta);
            boolean clienteAbituale = cliente.getNumeroOrdini() >= 3;

            dettagliInAttesa.clear();

            for (int i = 0; i < codiciProdotto.size(); i++) {
                String codice = codiciProdotto.get(i);
                int qta = quantita.get(i);

                ProdottoEntity prodotto = ProdottoDAO.leggiProdotto(codice);
                if (prodotto == null) {
                    throw new OperationException("Prodotto non trovato: " + codice);
                }

                if (prodotto.getQuantitaDisponibile() < qta) {
                    throw new OperationException("Quantità insufficiente per: " + codice);
                }

                prezzoSingolo = prodotto.getPrezzo();
                if (clienteAbituale) {
                    prezzoSingolo -= (prezzoSingolo * 10) / 100;
                }

                float subtot = prezzoSingolo * qta;
                prezzoTotale += subtot;

                dettagliInAttesa.add(new DettagliOrdineEntity(0, codice, qta));
            }

            ordineInAttesa = new OrdineEntity(0, new Date(),prezzoTotale, "ordinata", nomeUtente, null, indirizzo);

            returnList.set(0, String.valueOf(prezzoTotale));
            idOrdini.add(0); // per mantenere compatibilità con chiamante

        }catch (DBConnectionException dbEx) {
            throw new OperationException("Errore interno di connessione al database.");
        }catch (DAOException ex) {
            throw new OperationException("Errore durante l'elaborazione dell'acquisto.");
        }

        return returnList;
    }

    public void confermaOrdineOnline(ArrayList<Integer> idOrdini) throws OperationException {
        try {
            if (ordineInAttesa == null || dettagliInAttesa.isEmpty()) {
                throw new OperationException("Nessun ordine in attesa da confermare.");
            }

            OrdineEntity ordineCreato = OrdineDAO.creaOrdine(ordineInAttesa);
            int idOrdineCreato = ordineCreato.getIdOrdine();

            for (DettagliOrdineEntity d : dettagliInAttesa) {
                d.setIdOrdine(idOrdineCreato);
                DettaglioOrdineDAO.aggiungiDettaglio(d);

                ProdottoEntity p = ProdottoDAO.leggiProdotto(d.getCodiceProdotto());
                p.setQuantitaDisponibile(p.getQuantitaDisponibile() - d.getQuantita());
                ProdottoDAO.aggiornaProdotto(p);
            }

            ClienteRegistratoDAO.incrementaNumeroOrdini(ordineCreato.getNomeUtente());

            ordineInAttesa = null;
            dettagliInAttesa.clear();
            idOrdini.clear();

            System.out.println("Ordine confermato! ID: " + idOrdineCreato);

        } catch (DBConnectionException dbEx) {
            throw new OperationException("Errore interno durante la connessione al DB.");
        } catch (DAOException ex) {
            throw new OperationException("Errore durante la conferma dell’ordine.");
        }
    }
}

