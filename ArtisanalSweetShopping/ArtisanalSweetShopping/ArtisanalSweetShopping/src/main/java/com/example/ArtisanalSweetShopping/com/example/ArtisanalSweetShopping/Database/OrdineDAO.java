package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.OrdineEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import java.sql.*;

public class OrdineDAO {

    public static OrdineEntity creaOrdine(OrdineEntity ordine) throws DAOException, DBConnectionException {
        String query = "INSERT INTO Ordini (Data, CostoTotale, Stato, CodiceSconto, NomeUtente, Indirizzo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, new java.sql.Date(ordine.getData().getTime()));
            stmt.setFloat(2, ordine.getCostoTotale());
            stmt.setString(3, ordine.getStato());
            stmt.setString(4, ordine.getCodiceSconto());
            stmt.setString(5, ordine.getNomeUtente());
            stmt.setString(6, ordine.getIndirizzo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ordine.setIdOrdine(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore durante la creazione dell'ordine");
        }

        return ordine;
    }

    public static OrdineEntity leggiOrdine(int idOrdine) throws DAOException, DBConnectionException {
        String query = "SELECT * FROM Ordini WHERE IDOrdine = ?";
        OrdineEntity ordine = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrdine);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ordine = new OrdineEntity(
                            rs.getInt("IDOrdine"),
                            rs.getDate("Data"),
                            rs.getFloat("CostoTotale"),
                            rs.getString("Stato"),
                            rs.getString("NomeUtente"),
                            rs.getString("CodiceSconto"),
                            rs.getString("Indirizzo")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura dell'ordine");
        }

        return ordine;
    }

    public static void aggiornaStatoOrdine(int idOrdine, String nuovoStato) throws DAOException, DBConnectionException {
        String query = "UPDATE Ordini SET Stato = ? WHERE IDOrdine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuovoStato);
            stmt.setInt(2, idOrdine);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nell'aggiornamento dello stato dell'ordine");
        }
    }

    public static void eliminaOrdine(int idOrdine) throws DAOException, DBConnectionException {
        String query = "DELETE FROM Ordini WHERE IDOrdine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrdine);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella cancellazione dell'ordine");
        }
    }
}
