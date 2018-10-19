package de.imolli.mywarp.utils;

import de.imolli.mywarp.MyWarp;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;

public class SQLHandle {

    private static File databaseFile = new File("plugins/MyWarp/data.db");
    private static Connection con;

    //TODO: Maybe transfer complete system into a new thread to improve performance!

    public static void init() {

        if (!databaseFile.exists()) {
            createNewDatabase("plugins/MyWarp/data.db");
        }

        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connect();
        checkTables();
    }

    private static void checkTables() {

        //Creating Holograms Table
        update("CREATE TABLE IF NOT EXISTS `holograms` ("
                + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "`warp` varchar(100) NOT NULL DEFAULT '-',"
                + "`location` varchar(1000) NOT NULL DEFAULT '-',"
                + "`active` tinyint NOT NULL DEFAULT '0'"
                + ");");


        //Creating Warp Table
        update("CREATE TABLE IF NOT EXISTS `warps` ("
                + "`name` varchar(100) NOT NULL PRIMARY KEY,"
                + "`displayname` varchar(100) NOT NULL DEFAULT '-',"
                + "`location` varchar(500) NOT NULL DEFAULT 'world:0:0:0',"
                + "`creator` varchar(50) NOT NULL DEFAULT '-',"
                + "`flags` varchar(500) NOT NULL DEFAULT '-',"
                + "`item` varchar(500) NOT NULL DEFAULT 'PAPER',"
                + "`category` varchar(500) NOT NULL DEFAULT 'DEFAULT'"
                + ");");

    }

    private static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                //DatabaseMetaData meta = conn.getMetaData();
                MyWarp.getPlugin().getLogger().log(Level.INFO, "Database file has been created.");
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:plugins/MyWarp/data.db");
            MyWarp.getPlugin().getLogger().log(Level.INFO, "Successfully connected to database!");

        } catch (SQLException e) {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Error > " + e.getMessage());
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Connection to database failed!");
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (con != null) {
                con.close();
                con = null;
                MyWarp.getPlugin().getLogger().log(Level.INFO, "Successfully disconnected from database!");
            }
        } catch (SQLException e) {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Error > " + e.getMessage());
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while disconnection from database...");
            e.printStackTrace();
        }
    }

    private static void reconnect() {
        MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Trying to reconnect to database...");
        disconnect();
        connect();
    }

    public static void update(String query) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Error > " + e.getMessage());
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while trying to send an update to database!");
            reconnect();
        }
    }

    public static ResultSet query(String query) {
        ResultSet rs = null;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Error > " + e.getMessage());
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while trying to send a query to database!");
            reconnect();
        }
        return rs;
    }

    public static Boolean existColumn(String table, String name) {
        try {
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getColumns(null, null, table, name);

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while trying to check if a column exist.");
            reconnect();
            return false;
        }
    }

    public static void set(String table, String key, Object value, String where, String is) {
        String query = "UPDATE `" + table + "` SET `" + key + "` = '" + value + "' WHERE `" + where + "` = '" + is + "';";
        update(query);
    }

    public static String getString(String table, String key, String where, String is) {
        String value = null;
        ResultSet rs = query("SELECT `" + key + "` FROM `" + table + "` WHERE `" + where + "` = '" + is + "';");

        try {
            if (rs.next()) {
                value = rs.getString(key);
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        return value;
    }

    public static boolean contains(String table, String key, String value) {
        ResultSet rs = query("SELECT * FROM `" + table + "` WHERE `" + key + "` = '" + value + "';");

        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isConnected() {
        return con != null;
    }

}
