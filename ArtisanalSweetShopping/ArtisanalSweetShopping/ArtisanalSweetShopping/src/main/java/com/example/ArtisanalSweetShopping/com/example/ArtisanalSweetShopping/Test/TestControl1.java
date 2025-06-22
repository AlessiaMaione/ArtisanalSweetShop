package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Test;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.GestioneScontoControl;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.GestioneScontoControl.InputSconto;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control.GestioneScontoControl.OutputSconto;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.OperationException;

public class TestControl1 {

    public static void main(String[] args) {

        GestioneScontoControl control = new GestioneScontoControl();
        String codiceTest = "TEST";

        try {
            //1: crea sconto temporaneo
            InputSconto nuovo = new InputSconto();
            nuovo.codiceSconto = codiceTest;
            nuovo.percentuale = 25.0f;
            nuovo.idImpiegato = 1; // Erika De Luca
            nuovo.utilizzato = false;

            OutputSconto creato = control.creaSconto(nuovo);
            System.out.println("Sconto creato: " + creato.sconto.getCodiceSconto() +
                    ", % = " + creato.sconto.getPercentuale() +
                    ", ID impiegato = " + creato.sconto.getIdImpiegato());

            // 2: aggiorna sconto (simula utilizzo e modifica %)
            InputSconto aggiornato = new InputSconto();
            aggiornato.codiceSconto = codiceTest;
            aggiornato.percentuale = 20.0f;
            aggiornato.idImpiegato = 1;
            aggiornato.utilizzato = true;

            OutputSconto mod = control.aggiornaSconto(aggiornato);
            System.out.println("Sconto aggiornato: " + mod.sconto.getCodiceSconto() +
                    ", usato = " + mod.sconto.isUtilizzato() +
                    ", nuova % = " + mod.sconto.getPercentuale());

            // 🔹 STEP 3: elimina sconto temporaneo
            String eliminato = control.eliminaSconto(codiceTest);
            System.out.println(eliminato);

        } catch (OperationException e) {
            System.out.println("Errore nel test completo:");
            e.printStackTrace();
        }
    }
}

