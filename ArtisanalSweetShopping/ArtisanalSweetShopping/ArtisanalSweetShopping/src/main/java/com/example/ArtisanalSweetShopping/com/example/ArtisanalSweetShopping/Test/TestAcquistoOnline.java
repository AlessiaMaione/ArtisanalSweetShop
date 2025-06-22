package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Test;


import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.AcquistoOnlineControl;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.OperationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAcquistoOnline {

    public static void main(String[] args) {

        AcquistoOnlineControl control = new AcquistoOnlineControl();

        String utente = "Alessia";                         // già registrato nel DB
        List<String> codici = Arrays.asList("P013", "P017");    // prodotti esistenti
        List<Integer> quantita = Arrays.asList(2, 1);
        String indirizzo = "Via Mille 10, Ancona";
        String cartaInput = "1234098765434567";                 // serve solo se l'utente non ha carta memorizzata

        ArrayList<Integer> idTemporanei = new ArrayList<>();

        try {
            System.out.println("Avvio ordine online...");

            ArrayList<String> output = control.acquistoOnline(utente, codici, quantita, indirizzo, cartaInput, idTemporanei);

            System.out.println("Totale: €" + output.get(0));
            System.out.println("Carta: " + output.get(1));
            System.out.println("Carrello temporaneo salvato, ID: " + idTemporanei);

            System.out.println("\n Conferma ordine...");
            control.confermaOrdineOnline(idTemporanei);

        } catch (OperationException e) {
            System.out.println("Errore durante il test:");
            e.printStackTrace();
        }
    }
}
