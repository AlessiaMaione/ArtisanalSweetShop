package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.ScontoDAO;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ScontoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.OperationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneScontoControl {

    private static final Map<String, ScontoEntity> scontiTemporanei = new HashMap<>();

    public static class InputSconto {
        public String codiceSconto;
        public float percentuale;
        public int idImpiegato;
        public boolean utilizzato;
    }

    public static class OutputSconto {
        public ScontoEntity sconto;
        public String messaggio;

        public OutputSconto(ScontoEntity sconto, String messaggio) {
            this.sconto = sconto;
            this.messaggio = messaggio;
        }
    }

    public OutputSconto creaSconto(InputSconto input) throws OperationException {
        try {
            ScontoEntity sconto = new ScontoEntity(
                    input.codiceSconto,
                    input.percentuale,
                    input.idImpiegato,
                    input.utilizzato
            );
            ScontoDAO.creaSconto(sconto);
            scontiTemporanei.put(sconto.getCodiceSconto(), sconto);
            return new OutputSconto(sconto, "Sconto inserito correttamente.");
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante l'inserimento dello sconto.");
        }
    }

    public OutputSconto aggiornaSconto(InputSconto input) throws OperationException {
        try {
            ScontoEntity sconto = new ScontoEntity(
                    input.codiceSconto,
                    input.percentuale,
                    input.idImpiegato,
                    input.utilizzato
            );
            ScontoDAO.aggiornaSconto(sconto);
            scontiTemporanei.put(sconto.getCodiceSconto(), sconto);
            return new OutputSconto(sconto, "Sconto aggiornato correttamente.");
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante l’aggiornamento dello sconto.");
        }
    }

    public String eliminaSconto(String codiceSconto) throws OperationException {
        try {
            ScontoDAO.eliminaSconto(codiceSconto);
            scontiTemporanei.remove(codiceSconto);
            return "Sconto eliminato correttamente.";
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante l’eliminazione dello sconto.");
        }
    }

    public List<ScontoEntity> leggiTuttiSconti() throws OperationException {
        try {
            return ScontoDAO.leggiTuttiSconti();
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore durante il recupero degli sconti.");
        }
    }

    public ScontoEntity leggiTemporaneo(String codiceSconto) {
        return scontiTemporanei.get(codiceSconto);
    }
}
