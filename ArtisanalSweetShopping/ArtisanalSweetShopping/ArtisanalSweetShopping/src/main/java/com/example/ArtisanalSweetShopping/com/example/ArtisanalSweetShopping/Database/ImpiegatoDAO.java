package com.example.ArtisanalSweetShopping.Database;

import com.example.ArtisanalSweetShopping.Entity.ImpiegatoEntity;
import com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import java.sql.*;

public class ImpiegatoDAO {

    public static void creaImpiegato(ImpiegatoEntity imp) throws DAOException, DBConnectionException {
        String query = "INSERT INTO Impiegato (id, nome, cognome, email, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, imp.getId());
            stmt.setString(2, imp.getNome());
            stmt.setString(3, imp.getCognome());
            stmt.setString(4, imp.getEmail());
            stmt.setString(5, imp.getPassword());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella creazione dell'impiegato");
        }
    }

    public static ImpiegatoEntity leggiImpiegato(int id) throws DAOException, DBConnectionException {
        String query = "SELECT * FROM Impiegato WHERE id = ?";
        ImpiegatoEntity imp = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                imp = new ImpiegatoEntity(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura dell'impiegato");
        }

        return imp;
    }

    public static boolean verificaCredenziali(int id, String password) throws DAOException, DBConnectionException {
        String query = "SELECT COUNT(*) FROM Impiegato WHERE id = ? AND password = ?";

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
        String query = "UPDATE Impiegato SET nome = ?, cognome = ?, email = ?, password = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, imp.getNome());
            stmt.setString(2, imp.getCognome());
            stmt.setString(3, imp.getEmail());
            stmt.setString(4, imp.getPassword());
            stmt.setInt(5, imp.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore durante l'aggiornamento dell'impiegato");
        }
    }

    public static void eliminaImpiegato(int id) throws DAOException, DBConnectionException {
        String query = "DELETE FROM Impiegato WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella rimozione dell'impiegato");
        }
    }
}
