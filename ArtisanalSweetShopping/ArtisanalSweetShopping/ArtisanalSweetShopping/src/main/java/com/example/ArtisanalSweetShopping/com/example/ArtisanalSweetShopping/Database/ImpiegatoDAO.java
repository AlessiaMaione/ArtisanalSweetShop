package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ImpiegatoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import java.sql.*;

public class ImpiegatoDAO {

    public static void creaImpiegato(ImpiegatoEntity imp) throws DAOException, DBConnectionException {
        String query = "INSERT INTO Impiegati (IDImpiegato, Nome, Cognome, Password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, imp.getIDImpiegato());
            stmt.setString(2, imp.getNome());
            stmt.setString(3, imp.getCognome());
            stmt.setString(4, imp.getPassword());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella creazione dell'impiegato");
        }
    }

    public static ImpiegatoEntity leggiImpiegato(int id) throws DAOException, DBConnectionException {
        String query = "SELECT * FROM Impiegati WHERE IDImpiegato = ?";
        ImpiegatoEntity imp = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imp = new ImpiegatoEntity(
                            rs.getInt("IDImpiegato"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("Password")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura dell'impiegato");
        }

        return imp;
    }

    public static boolean verificaCredenziali(int id, String password) throws DAOException, DBConnectionException {
        String query = "SELECT COUNT(*) FROM Impiegati WHERE IDImpiegato = ? AND Password = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella verifica delle credenziali");
        }
    }

    public static void aggiornaImpiegato(ImpiegatoEntity imp) throws DAOException, DBConnectionException {
        String query = "UPDATE Impiegati SET Nome = ?, Cognome = ?, Password = ? WHERE IDImpiegato = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, imp.getNome());
            stmt.setString(2, imp.getCognome());
            stmt.setString(3, imp.getPassword());
            stmt.setInt(4, imp.getIDImpiegato());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore durante l'aggiornamento dell'impiegato");
        }
    }

    public static void eliminaImpiegato(int id) throws DAOException, DBConnectionException {
        String query = "DELETE FROM Impiegati WHERE IDImpiegato = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella rimozione dell'impiegato");
        }
    }
}
