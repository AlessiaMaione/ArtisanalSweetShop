package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.DettagliOrdineEntity;

public class DettaglioOrdineDAO {

    public static void aggiungiDettaglio(DettagliOrdineEntity dettaglio)
            throws DAOException, DBConnectionException {
        String query = "INSERT INTO DettagliOrdine (IDOrdine, CodiceProdotto, Quantita) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, dettaglio.getIdOrdine());
            stmt.setString(2, dettaglio.getCodiceProdotto());
            stmt.setInt(3, dettaglio.getQuantita());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nell'inserimento del dettaglio ordine");
        }
    }

    public static List<DettagliOrdineEntity> leggiDettagliOrdine(int idOrdine)
            throws DAOException, DBConnectionException {
        String query = "SELECT * FROM DettagliOrdine WHERE IDOrdine = ?";
        List<DettagliOrdineEntity> dettagli = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrdine);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dettagli.add(new DettagliOrdineEntity(
                            rs.getInt("IDOrdine"),
                            rs.getString("CodiceProdotto"),
                            rs.getInt("Quantita")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura dei dettagli ordine");
        }

        return dettagli;
    }

    public static void eliminaDettagliPerOrdine(int idOrdine)
            throws DAOException, DBConnectionException {
        String query = "DELETE FROM DettagliOrdine WHERE IDOrdine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrdine);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nell'eliminazione dei dettagli ordine");
        }
    }

    public static void aggiungiOIncrementaProdotto(int idOrdine, String codiceProdotto, int quantitaDaAggiungere)
            throws DAOException, DBConnectionException {

        String checkQuery = "SELECT Quantita FROM DettagliOrdine WHERE IDOrdine = ? AND CodiceProdotto = ?";
        String updateQuery = "UPDATE DettagliOrdine SET Quantita = Quantita + ? WHERE IDOrdine = ? AND CodiceProdotto = ?";
        String insertQuery = "INSERT INTO DettagliOrdine (IDOrdine, CodiceProdotto, Quantita) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, idOrdine);
                checkStmt.setString(2, codiceProdotto);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, quantitaDaAggiungere);
                            updateStmt.setInt(2, idOrdine);
                            updateStmt.setString(3, codiceProdotto);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, idOrdine);
                            insertStmt.setString(2, codiceProdotto);
                            insertStmt.setInt(3, quantitaDaAggiungere);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nell'aggiunta o aggiornamento del dettaglio ordine");
        }
    }
}

