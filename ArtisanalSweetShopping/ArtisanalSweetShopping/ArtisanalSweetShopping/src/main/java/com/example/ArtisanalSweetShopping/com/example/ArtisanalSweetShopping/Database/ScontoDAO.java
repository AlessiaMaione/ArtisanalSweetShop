package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ScontoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ScontoDAO{

    public static void creaSconto(ScontoEntity sconto) throws DAOException, DBConnectionException {
        String query = "INSERT INTO Sconti (CodiceSconto, Percentuale, IDImpiegato, Utilizzato) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sconto.getCodiceSconto());
            stmt.setFloat(2, sconto.getPercentuale());
            stmt.setInt(3, sconto.getIdImpiegato());
            stmt.setBoolean(4, sconto.isUtilizzato());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella creazione dello sconto");
        }
    }

    public static ScontoEntity leggiScontoValido(String codiceSconto) throws DAOException, DBConnectionException {
        String query = "SELECT * FROM Sconti WHERE CodiceSconto = ? AND Utilizzato = FALSE";
        ScontoEntity sconto = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, codiceSconto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sconto = new ScontoEntity(
                        rs.getString("CodiceSconto"),
                        rs.getFloat("Percentuale"),
                        rs.getInt("IDImpiegato"),
                        rs.getBoolean("Utilizzato")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 👈 stampa l'errore preciso
            throw new DAOException("Errore nella lettura dello sconto");
        }


        return sconto;
    }

    public static void segnaScontoComeUtilizzato(String codiceSconto) throws DAOException, DBConnectionException {
        String query = "UPDATE Sconti SET Utilizzato = TRUE WHERE CodiceSconto = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, codiceSconto);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nell'aggiornamento dello stato dello sconto");
        }
    }

    public static void eliminaSconto(String codiceSconto) throws DAOException, DBConnectionException {
        String query = "DELETE FROM Sconti WHERE CodiceSconto = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, codiceSconto);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella rimozione dello sconto");
        }
    }
    public static void aggiornaSconto(ScontoEntity sconto) throws DAOException, DBConnectionException {
    String query = "UPDATE Sconto SET percentuale = ?, idImpiegato = ?, utilizzato = ? WHERE codiceSconto = ?";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setFloat(1, sconto.getPercentuale());
        stmt.setInt(2, sconto.getIdImpiegato());
        stmt.setBoolean(3, sconto.isUtilizzato());
        stmt.setString(4, sconto.getCodiceSconto());

        int righeModificate = stmt.executeUpdate();
        if (righeModificate == 0) {
            throw new DAOException("Nessuno sconto aggiornato: codice non trovato.");
        }

    } catch (SQLException e) {
        throw new DAOException("Errore durante l’aggiornamento dello sconto.");
    }
}
}
