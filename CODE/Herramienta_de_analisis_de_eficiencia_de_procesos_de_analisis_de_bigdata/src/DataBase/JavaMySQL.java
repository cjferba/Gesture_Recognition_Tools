/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos Jesús Fernández Basso
 */
public class JavaMySQL {

    public Connection conexion = null;
    public Statement st = null;
    public double time;
    String server;
    String user;
    String password;

    public JavaMySQL(String server1, String user1, String password1) {

        server = server1;
        user = user1;
        password = password1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(server, user, password);
            st = conexion.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            conexion = null;
            st = null;
            System.out.println("Imposible realizar conexion con la BD");

        }
    }

    @Override
    public JavaMySQL clone() {

        JavaMySQL x = new JavaMySQL(server, user, password);
        return x;
    }

    public double GetTime() {
        return time / 1000;
    }

    public void insertar(String insercion) {
        long time_start, time_end;

        try {
            time_start = System.currentTimeMillis();
            st.executeUpdate(insercion);
            time_end = System.currentTimeMillis();
            time = (time_end - time_start);
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CrearTabla(String SQL) {
        try {
            st.executeUpdate(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet Consultar(String consulta) {
        ResultSet rs = null;
        long time_start, time_end;
        try {
            time_start = System.currentTimeMillis();
            rs = st.executeQuery(consulta);
            time_end = System.currentTimeMillis();
            time = (time_end - time_start);
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public int Update(String actualizacion) {
        long time_start, time_end;
        int salida = 0;
        try {
            time_start = System.currentTimeMillis();
            salida = st.executeUpdate(actualizacion);
            time_end = System.currentTimeMillis();
            time = (time_end - time_start);
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salida;

    }

    public void cerrar() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void EjecutarSQL(String sql) {
        try {
            long time_start = System.currentTimeMillis();
            st.execute(sql);
            long time_end = System.currentTimeMillis();
            time = (time_end - time_start);
        } catch (SQLException ex) {
            Logger.getLogger(JavaMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
