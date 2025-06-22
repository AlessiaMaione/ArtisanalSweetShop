package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.ClienteRegistratoEntity;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;

import java.sql.*;

public class ClienteRegistratoDAO {

    public static void creaCliente(ClienteRegistratoEntity cliente) throws DAOException, DBConnectionException {
        String query = "INSERT INTO ClientiRegistrati (NomeUtente, Password, Email, NumeroTelefono, NumeroOrdini, NumeroCarta) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cliente.getNomeUtente());
            stmt.setString(2, cliente.getPassword());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getNumeroTelefono());
            stmt.setInt(5, cliente.getNumeroOrdini());
            stmt.setString(6, cliente.getNumeroCarta());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella creazione del cliente registrato");
        }
    }

    public static ClienteRegistratoEntity leggiCliente(String nomeUtente) throws DAOException, DBConnectionException {
        String query = "SELECT * FROM ClientiRegistrati WHERE NomeUtente = ?";
        ClienteRegistratoEntity cliente = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomeUtente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new ClienteRegistratoEntity(
                            rs.getString("NomeUtente"),
                            rs.getString("Password"),
                            rs.getString("Email"),
                            rs.getString("NumeroTelefono"),
                            rs.getInt("NumeroOrdini"),
                            rs.getString("NumeroCarta")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura del cliente");
        }

        return cliente;
    }

    public static void aggiornaCliente(ClienteRegistratoEntity cliente) throws DAOException, DBConnectionException {
        String query = "UPDATE ClientiRegistrati SET Password = ?, Email = ?, NumeroTelefono = ?, NumeroOrdini = ?, NumeroCarta = ? WHERE NomeUtente = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cliente.getPassword());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getNumeroTelefono());
            stmt.setInt(4, cliente.getNumeroOrdini());
            stmt.setString(5, cliente.getNumeroCarta());
            stmt.setString(6, cliente.getNomeUtente());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore durante l'aggiornamento del cliente");
        }
    }

    public static void eliminaCliente(String nomeUtente) throws DAOException, DBConnectionException {
        String query = "DELETE FROM ClientiRegistrati WHERE NomeUtente = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomeUtente);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nella rimozione del cliente");
        }
    }

    public static void incrementaNumeroOrdini(String nomeUtente) throws DAOException, DBConnectionException {
        String query = "UPDATE ClientiRegistrati SET NumeroOrdini = NumeroOrdini + 1 WHERE NomeUtente = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomeUtente);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore nell'incremento del numero ordini");
        }
    }
}

