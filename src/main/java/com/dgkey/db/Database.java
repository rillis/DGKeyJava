package com.dgkey.db;

import java.sql.*;

public class Database {
    public Connection connection;

    public Database(String ip, String port, String db) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/"+db, "root", Credential.pass);
    }

    private void run(String stat) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(stat);
        stmt.close();
    }

    public void insert(String table, String[] fields, Object[] values) throws SQLException {
        StringBuilder val = new StringBuilder();
        for(Object value : values){
            if(value instanceof String) value = "'"+value+"'";
            val.append(value).append(",");
        }
        val.deleteCharAt(val.length()-1);
        run("INSERT INTO "+table+"("+String.join(", ", fields)+") VALUES ("+ val +")");
    }

    public void truncate(String table) throws SQLException {
        run("TRUNCATE TABLE "+table);
    }

    public void close() throws SQLException {
        connection.close();
    }
}
