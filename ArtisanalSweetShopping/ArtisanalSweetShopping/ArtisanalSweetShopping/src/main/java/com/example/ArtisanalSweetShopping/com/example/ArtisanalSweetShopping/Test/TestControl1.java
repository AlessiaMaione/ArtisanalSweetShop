package com.example.ArtisanalSweetShopping.Test;

import com.example.ArtisanalSweetShopping.Database.ScontoDAO;
import com.example.ArtisanalSweetShopping.Entity.ScontoEntity;
import com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

public class TestControl1 {
    public static void main(String[] args) {
        String codiceTest = "SPRING25";

        // Step 1: Crea un nuovo sconto
        ScontoEntity sconto = new ScontoEntity(codiceTest, 25.0f, 101, false);

        try {
            ScontoDAO.inserisciSconto(sconto);
            System.out.println("✅ Sconto inserito correttamente: " + codiceTest);

            // Step 2: Simula modifica (es. viene utilizzato)
            sconto.setUtilizzato(true);
            sconto.setPercentuale(20.0f); // cambia la percentuale
            ScontoDAO.aggiornaSconto(sconto);
            System.out.println("✏️  Sconto aggiornato: utilizzato = true, percentuale = 20.0");

            // Step 3: Elimina alla fine per mantenere DB pulito
            ScontoDAO.eliminaSconto(codiceTest);
            System.out.println("🗑️  Sconto di test eliminato: " + codiceTest);

        } catch (DAOException | DBConnectionException e) {
            System.out.println("❌ Errore durante il test controllo sconti:");
            e.printStackTrace();
        }
    }
}
