package Utilidades;

import DTW.*;
import DataBase.JavaMongoDB;
import DataBase.JavaMySQL;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author Carlos
 */
public class Utilidades {

    public static double time;

    public static Double TamañoBD(JavaMongoDB Mongodb) {

        return Mongodb.TamañoColecccion();
    }

    public static Double TamañoBD(JavaMySQL MySql, boolean tipo) {

        Double Tiempo = 0.0;

        String QueryProyectoGestos = "SELECT\n"
                + "  table_schema \"proyectogestos\",\n"
                + "  sum( data_length + index_length ) / 1024 / 1024 \"Tamaño en MB\"\n"
                + "  FROM\n"
                + "  information_schema.TABLES where TABLE_SCHEMA='proyectogestos' group BY table_schema;";
        String QueryTest = "SELECT\n"
                + "  table_schema \"test\",\n"
                + "  sum( data_length + index_length ) / 1024 / 1024 \"Tamaño en MB\"\n"
                + "  FROM\n"
                + "  information_schema.TABLES where TABLE_SCHEMA='test' group BY table_schema;";
        String QueryMySql;
        if (tipo) {
            QueryMySql = QueryProyectoGestos;
        } else {
            QueryMySql = QueryTest;
        }
        try {
            ResultSet Resultado = MySql.Consultar(QueryMySql);
            Resultado.next();
            Tiempo = Double.parseDouble(Resultado.getString("Tamaño en MB"));

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Tiempo;
    }

    public final static ArrayList<Gesto> ImportarMongoDBSubdocumentos(JavaMongoDB BDMongo, double porcentaje) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor lista = BDMongo.Consultar(null);
        int extraciones = (int) ((porcentaje / 100) * lista.size());
        int Num = 0;

        while (Num < extraciones) {

            Num++;
            DBObject Gesto1 = lista.next();
            BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");
            for (int i = 0; i < FramesGesto1.size(); i++) {
                PQL = new PosturaAsQuaternionList();
                BasicDBObject wr = (BasicDBObject) FramesGesto1.get(i);
                BasicDBList wrget = (BasicDBList) wr.get("WR");

                for (int j = 0; j < wrget.size(); j++) {

                    BasicDBList aux = (BasicDBList) wrget.get("" + j);
                    MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                            (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                    PQL.addQuaternion(j, auxQua);
                }

                PAL = new PosturaAsAngleList(PQL);
                G.addPostura(PAL);
                G.nombre = (String) Gesto1.get("Nombre");
                G.idMongo = (ObjectId) Gesto1.get("_id");
            }
            Gestos.add(G);
            G = new Gesto();

        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarDocumentoMongoDB(JavaMongoDB BDMongo, BasicDBObject consulta) {
        time = 0;
        Gesto G;
        G = new Gesto();

        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;
        ArrayList<Gesto> Gestos = new ArrayList<>();

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor lista = BDMongo.Consultar(consulta);

        while (lista.hasNext()) {
            DBObject Gesto1 = lista.next();
            BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");
            G.nombre = (String) Gesto1.get("Nombre");

            for (int i = 0; i < FramesGesto1.size(); i++) {
                PQL = new PosturaAsQuaternionList();
                BasicDBObject wr = (BasicDBObject) FramesGesto1.get(i);
                BasicDBList wrget = (BasicDBList) wr.get("WR");
                for (int j = 0; j < wrget.size(); j++) {

                    BasicDBList aux = (BasicDBList) wrget.get("" + j);
                    MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                            (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                    PQL.addQuaternion(j, auxQua);

                }
                PAL = new PosturaAsAngleList(PQL);
                G.addPostura(PAL);
            }
            Gestos.add(G);
            G = new Gesto();
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;

        return Gestos;

    }

    public final static ArrayList<ArrayList<Gesto>> ImportarMongoDBSubdocumentosClases(JavaMongoDB BDMongo) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<ArrayList<Gesto>> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();

        List NombreClase = BDMongo.col.distinct("Nombre");

        for (int i = 0; i < NombreClase.size(); i++) {
            BasicDBObject query = new BasicDBObject("Nombre", NombreClase.get(i));
            DBCursor lista = BDMongo.Consultar(query);
            ArrayList<Gesto> ges = new ArrayList<Gesto>();
            while (lista.hasNext()) {
                DBObject Gesto1 = lista.next();
                BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");
                for (int s = 0; s < FramesGesto1.size(); s++) {

                    PQL = new PosturaAsQuaternionList();
                    BasicDBObject wr = (BasicDBObject) FramesGesto1.get(s);
                    BasicDBList wrget = (BasicDBList) wr.get("WR");
                    for (int j = 0; j < wrget.size(); j++) {

                        BasicDBList aux = (BasicDBList) wrget.get("" + j);
                        MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                                (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                        PQL.addQuaternion(j, auxQua);

                    }
                    PAL = new PosturaAsAngleList(PQL);
                    G.addPostura(PAL);
                    G.nombre = (String) Gesto1.get("Nombre");
                    G.idMongo = (ObjectId) Gesto1.get("_id");
                }
                ges.add(G);
                G = new Gesto();
            }
            Gestos.add(ges);
        }

        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarMongoDBReferences(JavaMongoDB BDMongo) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor lista = BDMongo.Consultar(null);

        while (lista.hasNext()) {
            DBObject Gesto1 = lista.next();
            BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");
            BDMongo.AbrirColeccion("Gestos2");
            ArrayList<DBObject> ListaFrameGesto = BuscarIds(FramesGesto1, BDMongo);
            for (int i = 0; i < ListaFrameGesto.size(); i++) {

                PQL = new PosturaAsQuaternionList();
                BasicDBObject wr = (BasicDBObject) ListaFrameGesto.get(i);
                BasicDBList wrget = (BasicDBList) wr.get("WR");
                for (int j = 0; j < wrget.size(); j++) {

                    BasicDBList aux = (BasicDBList) wrget.get("" + j);
                    MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                            (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                    PQL.addQuaternion(j, auxQua);

                }
                PAL = new PosturaAsAngleList(PQL);
                G.addPostura(PAL);
                G.nombre = (String) Gesto1.get("Nombre");
                G.idMongo = (ObjectId) Gesto1.get("_id");
            }
            Gestos.add(G);
            G = new Gesto();
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarMongoDBReferences(JavaMongoDB BDMongo, double porcentaje) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor lista = BDMongo.Consultar(null);

        int extraciones = (int) ((porcentaje / 100) * lista.size());
        int Num = 0;

        while (Num < extraciones) {

            Num++;
            DBObject Gesto1 = lista.next();
            BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");
            BDMongo.AbrirColeccion("Gestos2");
            ArrayList<DBObject> ListaFrameGesto = BuscarIds(FramesGesto1, BDMongo);
            for (int i = 0; i < ListaFrameGesto.size(); i++) {

                PQL = new PosturaAsQuaternionList();
                BasicDBObject wr = (BasicDBObject) ListaFrameGesto.get(i);
                BasicDBList wrget = (BasicDBList) wr.get("WR");
                for (int j = 0; j < wrget.size(); j++) {

                    BasicDBList aux = (BasicDBList) wrget.get("" + j);
                    MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                            (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                    PQL.addQuaternion(j, auxQua);

                }
                PAL = new PosturaAsAngleList(PQL);
                G.addPostura(PAL);
                G.nombre = (String) Gesto1.get("Nombre");
                G.idMongo = (ObjectId) Gesto1.get("_id");
            }
            Gestos.add(G);
            G = new Gesto();
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarMySql(JavaMySQL BDMySQL) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        ResultSet lista = BDMySQL.Consultar("SELECT * FROM proyectogestos.gestos;");

        try {
            while (lista.next()) {

                String GestoNombre = lista.getString("Nombre");
                double GestoID = lista.getDouble("IdGestos");
                int GestoFrame = lista.getInt("NumFrame");
                JavaMySQL BDMySQL2 = BDMySQL.clone();
                ResultSet FramesGesto1 = BDMySQL2.Consultar("SELECT * FROM proyectogestos.wr where IdGesto="
                        + GestoID + "order by Frame;");
                while (FramesGesto1.next()) {
                    PQL = new PosturaAsQuaternionList();

                    for (int j = 1; j <= 20; j++) {
                        String wr = FramesGesto1.getString("" + j);
                        String[] aux = wr.split("  ");
                        MyQuaternion auxQua = new MyQuaternion(Double.parseDouble(aux[0]),
                                Double.parseDouble(aux[1]), Double.parseDouble(aux[2]), Double.parseDouble(aux[3]));
                        PQL.addQuaternion(j - 1, auxQua);

                    }
                    PAL = new PosturaAsAngleList(PQL);
                    G.addPostura(PAL);
                    G.nombre = GestoNombre;
                    G.idMysql = GestoID;

                }
                BDMySQL2.cerrar();
                Gestos.add(G);
                G = new Gesto();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarMySql(JavaMySQL BDMySQL, double porcentaje) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        ResultSet lista = BDMySQL.Consultar("SELECT count(*) FROM proyectogestos.gestos;");

        try {
            lista.next();
            int numero = lista.getInt(1);
            lista = BDMySQL.Consultar("SELECT * FROM proyectogestos.gestos;");
            int extraciones = (int) ((porcentaje / 100) * numero);
            int Num = 0;

            while (Num < extraciones) {
                lista.next();
                Num++;

                String GestoNombre = lista.getString("Nombre");
                double GestoID = lista.getDouble("IdGestos");
                int GestoFrame = lista.getInt("NumFrame");
                JavaMySQL BDMySQL2 = BDMySQL.clone();
                ResultSet FramesGesto1 = BDMySQL2.Consultar("SELECT * FROM proyectogestos.wr where IdGesto="
                        + GestoID + "order by Frame;");
                while (FramesGesto1.next()) {
                    PQL = new PosturaAsQuaternionList();

                    for (int j = 1; j <= 20; j++) {
                        String wr = FramesGesto1.getString("" + j);
                        String[] aux = wr.split("  ");
                        MyQuaternion auxQua = new MyQuaternion(Double.parseDouble(aux[0]),
                                Double.parseDouble(aux[1]), Double.parseDouble(aux[2]), Double.parseDouble(aux[3]));
                        PQL.addQuaternion(j - 1, auxQua);

                    }
                    PAL = new PosturaAsAngleList(PQL);
                    G.addPostura(PAL);
                    G.nombre = GestoNombre;
                    G.idMysql = GestoID;

                }
                BDMySQL2.cerrar();
                Gestos.add(G);
                G = new Gesto();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public final static ArrayList<Gesto> ImportarMySql(JavaMySQL BDMySQL, String nombre) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        ResultSet lista = BDMySQL.Consultar("SELECT * FROM gestos where Nombre='" + nombre + "'");

        try {
            while (lista.next()) {

                String GestoNombre = lista.getString("Nombre");
                double GestoID = lista.getDouble("IdGestos");
                int GestoFrame = lista.getInt("NumFrame");
                JavaMySQL BDMySQL2 = BDMySQL.clone();
                ResultSet FramesGesto1 = BDMySQL2.Consultar("SELECT * FROM wr where IdGesto="
                        + GestoID + "order by Frame;");
                while (FramesGesto1.next()) {
                    PQL = new PosturaAsQuaternionList();

                    for (int j = 1; j <= 20; j++) {
                        String wr = FramesGesto1.getString("" + j);
                        String[] aux = wr.split("  ");
                        MyQuaternion auxQua = new MyQuaternion(Double.parseDouble(aux[0]),
                                Double.parseDouble(aux[1]), Double.parseDouble(aux[2]), Double.parseDouble(aux[3]));
                        PQL.addQuaternion(j - 1, auxQua);

                    }
                    PAL = new PosturaAsAngleList(PQL);
                    G.addPostura(PAL);
                    G.nombre = GestoNombre;
                    G.idMysql = GestoID;

                }
                BDMySQL2.cerrar();
                Gestos.add(G);
                G = new Gesto();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }

    public static ArrayList<DBObject> BuscarIds(BasicDBList ListaIds, JavaMongoDB DBMongo) {
        ArrayList<DBObject> salida = new ArrayList<>();
        for (int i = 0; i < ListaIds.size(); i++) {
            ObjectId auxID = (ObjectId) ListaIds.remove(i);
            DBObject Doc = DBMongo.ConsultarID(auxID.toString());
            salida.add(Doc);
        }

        return salida;
    }

    public static ArrayList<String> ExtraerClases(JavaMongoDB BDMongo) {
        ArrayList<String> NombresClases = new ArrayList(BDMongo.col.distinct("Nombre"));
        return NombresClases;
    }

    public static ArrayList<String> ExtraerClases(JavaMySQL BDMySQL) {
        ArrayList<String> NombresClases = new ArrayList();
        ResultSet aux = BDMySQL.Consultar("Select Distinct Nombre from gestos;");
        try {
            while (aux.next()) {
                NombresClases.add(aux.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return NombresClases;
    }

    public final static ArrayList<Gesto> ImportarMongoDBReferencias(JavaMongoDB BDMongo, String clase) {
        time = 0;
        Gesto G;
        G = new Gesto();
        ArrayList<Gesto> Gestos = new ArrayList<>();
        PosturaAsAngleList PAL;
        PosturaAsQuaternionList PQL;

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        BDMongo.AbrirColeccion("GestosListas");
        DBCursor lista = BDMongo.Consultar(null);

        while (lista.hasNext()) {
            DBObject Gesto1 = lista.next();
            BasicDBList FramesGesto1 = (BasicDBList) Gesto1.get("Frames");

            for (int i = 0; i < FramesGesto1.size(); i++) {
                BDMongo.AbrirColeccion("Gestos2");
                DBCursor SkeletonGesto = BDMongo.Consultar(new BasicDBObject("_id", FramesGesto1.get(i)));
                BasicDBObject wr = (BasicDBObject) SkeletonGesto.next();
                BasicDBList wrget = (BasicDBList) wr.get("WR");
                PQL = new PosturaAsQuaternionList();
                for (int j = 0; j < wrget.size(); j++) {

                    BasicDBList aux = (BasicDBList) wrget.get("" + j);
                    MyQuaternion auxQua = new MyQuaternion((Double) aux.get(0),
                            (Double) aux.get(1), (Double) aux.get(2), (Double) aux.get(3));
                    PQL.addQuaternion(j, auxQua);
                }

                PAL = new PosturaAsAngleList(PQL);
                G.addPostura(PAL);
                G.nombre = (String) Gesto1.get("Nombre");
                G.idMongo = (ObjectId) Gesto1.get("_id");
            }
            Gestos.add(G);
            G = new Gesto();
        }
        time_end = System.currentTimeMillis();
        time = (time_end - time_start) / 1000.0;
        return Gestos;

    }
}
