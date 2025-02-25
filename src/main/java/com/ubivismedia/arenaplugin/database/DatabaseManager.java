package com.ubivismedia.arenaplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:plugins/ArenaInstance/database.db";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Datenbankverbindung hergestellt.");
            createTablesIfNotExists();
        } catch (SQLException e) {
            System.err.println("Fehler beim Herstellen der Datenbankverbindung: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTablesIfNotExists() {
        String createPlayerStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "points INTEGER DEFAULT 0," +
                "waves_completed INTEGER DEFAULT 0," +
                "mobs_killed INTEGER DEFAULT 0," +
                "pvp_kills INTEGER DEFAULT 0" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(createPlayerStatsTable)) {
            stmt.execute();
            System.out.println("Tabellen wurden überprüft und ggf. erstellt.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabellen: " + e.getMessage());
        }
    }

    public void savePlayerStats(String uuid, String name, int points, int wavesCompleted, int mobsKilled, int pvpKills) {
        String upsertPlayerStats = "INSERT INTO player_stats (uuid, name, points, waves_completed, mobs_killed, pvp_kills) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET " +
                "name = excluded.name," +
                "points = excluded.points," +
                "waves_completed = excluded.waves_completed," +
                "mobs_killed = excluded.mobs_killed," +
                "pvp_kills = excluded.pvp_kills;";

        try (PreparedStatement stmt = connection.prepareStatement(upsertPlayerStats)) {
            stmt.setString(1, uuid);
            stmt.setString(2, name);
            stmt.setInt(3, points);
            stmt.setInt(4, wavesCompleted);
            stmt.setInt(5, mobsKilled);
            stmt.setInt(6, pvpKills);
            stmt.executeUpdate();
            System.out.println("Spielerstatistiken gespeichert.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern der Spielerstatistiken: " + e.getMessage());
        }
    }

    public void printTopPlayers() {
        String queryTopPlayers = "SELECT name, points FROM player_stats ORDER BY points DESC LIMIT 10;";

        try (PreparedStatement stmt = connection.prepareStatement(queryTopPlayers);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Top-Spieler:");
            while (rs.next()) {
                System.out.printf("%s - %d Punkte%n", rs.getString("name"), rs.getInt("points"));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Top-Spieler: " + e.getMessage());
        }
    }
}
