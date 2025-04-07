package com.example.guestentryapp.db;

import com.example.guestentryapp.models.Guest;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Db {
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:guest.db");
                logger.info("Connected to database");
                createTable();
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void stopConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }

    private void createTable() {
        getConnection();
        String query = "create table if not exists guest (id integer not null primary key autoincrement, " +
                "date text not null, entryTime text not null, exitTime text not null, name text not null," +
                "purpose text not null, medicalExams bool not null, instructionStatement bool not null, " +
                "signature blob not null)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    public void insertGuest(LocalDate date, String entryTime, String exitTime,
                             String name, String purpose, boolean medicalExams,
                             boolean instructionStatement, byte[] signatureBytes) {
        getConnection();
        String insertQuery = "insert into guest (date, entryTime, exitTime, name, purpose, medicalExams, " +
                "instructionStatement, signature)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, date.toString());
            statement.setString(2, entryTime);
            statement.setString(3, exitTime);
            statement.setString(4, name);
            statement.setString(5, purpose);
            statement.setBoolean(6, medicalExams);
            statement.setBoolean(7, instructionStatement);
            statement.setBytes(8, signatureBytes);

            statement.executeUpdate();
            logger.info("Guest inserted successfully");
        } catch (SQLException e) {
            logger.warning("Error during guest insert");
            logger.info(e.toString());
        }
    }

    public List<Guest> showGuests() {
        getConnection();
        String query = "select * from guest";
        List<Guest> guests = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String entryTime = rs.getString("entryTime");
                String exitTime = rs.getString("exitTime");
                String name = rs.getString("name");
                String purpose = rs.getString("purpose");
                boolean medicalExams = rs.getBoolean("medicalExams");
                boolean instructionStatement = rs.getBoolean("instructionStatement");
                byte[] signature = rs.getBytes("signature");

                Guest guest = new Guest(id, date, entryTime, exitTime, name, purpose,
                        medicalExams, instructionStatement, signature);
                guests.add(guest);
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }

        return guests;
    }
}
