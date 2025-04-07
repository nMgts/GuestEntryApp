package com.example.guestentryapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    private void createTable() {
        getConnection();
        String query = "create table if not exists guest (id integer not null primary key autoincrement, " +
                "date text not null, entryTime text not null, exitTime text not null, medicalExams bool not null, " +
                "instructionStatement bool not null, signature blob not null)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void insertGuest() {

    }
}
