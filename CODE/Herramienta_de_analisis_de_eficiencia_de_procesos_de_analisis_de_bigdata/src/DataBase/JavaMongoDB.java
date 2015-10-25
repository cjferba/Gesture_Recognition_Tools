/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import com.mongodb.MongoClient;
import com.mongodb.gridfs.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * @author Carlos JesÃºs FernÃ¡ndez Basso
 */
public class JavaMongoDB {

    /**
     * @param args the command line arguments
     */
    public MongoClient mongoClient;
    public DB db;
    public DBCollection col;
    private double time;
    public DBCursor cur;

    public JavaMongoDB() {
        try {
            this.mongoClient = new MongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JavaMongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JavaMongoDB(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public JavaMongoDB(String Server) {
        try {
            this.mongoClient = new MongoClient(Server);
        } catch (UnknownHostException ex) {
            Logger.getLogger(JavaMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error");
        }
    }

    public JavaMongoDB(String Server, int port) {
        try {
            this.mongoClient = new MongoClient(Server, port);
        } catch (UnknownHostException ex) {
            Logger.getLogger(JavaMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error");
        }
    }

    public JavaMongoDB(String Server, int port, String BD, String Coleccion) {
        try {
            this.mongoClient = new MongoClient(Server, port);
            AbrirBD(BD);
            AbrirColeccion(Coleccion);
        } catch (UnknownHostException ex) {
            mongoClient = null;
            Logger.getLogger(JavaMongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        mongoClient.close();
        db = null;
        col = null;
        cur = null;
    }

    /**
     * @return tiempo de la ultima operacion realizada
     */
    public double GetTime() {
        return time / 1000;
    }

    public void borrarColeccion(String nombre) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col = db.getCollection(nombre);
        col.drop();
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);

    }

    public void borrarelemento(DBObject elemento) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col.remove(elemento);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
    }

    /**
     *
     * @param BD
     */
    public void AbrirBD(String BD) {
        db = mongoClient.getDB(BD);
    }

    /**
     *
     * @return lista de bases de datos existentes
     */
    public List<String> GetBD() {
        return mongoClient.getDatabaseNames();
    }

    /**
     *
     * @param colecion
     */
    public void AbrirColeccion(String colecion) {
        col = db.getCollection(colecion);

    }

    /**
      * @return Coleciones dentro de la base de datos
     */
    public Set<String> GetColecciones() {
        return db.getCollectionNames();
    }

    /**
     *
     * @param Documento
     */
    public void InsertarDocumento(BasicDBObject Documento) {//cambiar a documento

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col.insert(Documento);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
    }

    public void InsertarDocumento(ArrayList<DBObject> Documento) {//cambiar a documento
        List<DBObject> doc = Documento;
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col.insert(doc);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
    }

    /**
     *
     * @param JsonEntrada String con el documento en el formato JSON
     */
    public void InsertarDocumentoJSON(String JsonEntrada) {
        DBObject Documento;
        Documento = (BasicDBObject) JSON.parse(JsonEntrada);
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col.insert(Documento);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);

    }

    public void Update(DBObject searchQuery, DBObject updateQuery) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        col.update(searchQuery, updateQuery);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);

    }

    public long Count() {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        long aux = col.count();
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
        return aux;

    }

    public double TamañoColecccion() {
        CommandResult aux = col.getStats();  
        double a = aux.getInt("size") / 1024.0 / 1024.0;
        return a;
    }

    /**
     *
     * @param db
     * @param coleccion
     * @param Consulta
     * @return
     */
    public DBObject Consultar(DB db, DBCollection coleccion, String Consulta) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBObject a = coleccion.findOne(Consulta);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
        return a;
    }

    public ArrayList<DBObject> Consultar(BasicDBObject Consulta, boolean lista) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor cur = col.find(Consulta);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);

        if (lista) {
            ArrayList<DBObject> Lista = new ArrayList<>();
            // DBObject a;
            for (int i = 0; i < cur.count(); i++) {
                System.out.println("Mongo conulat" + time);
                DBObject a = cur.next();
                Lista.add(a);
            }
            cur.close();
            return Lista;
        } else {
            return null;
        }

    }

    public DBCursor Consultar(BasicDBObject Consulta, DBObject campos) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        cur = col.find(Consulta, campos);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
        return cur;
    }

    /**
     * consulta en la base de datos de Mongo Mediante un onjeto BasicDBObject
     *
     * @param Consulta consulta mongoDB
     * @return cursor con los elementos de la consulta
     */
    public DBCursor Consultar(BasicDBObject Consulta) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        cur = col.find(Consulta);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);
        return cur;
    }

    /**
     * Consultar mediante una referencia Id
     *
     * @param id referencia a consultar
     * @return documento al que estamos referenciando mediante el ID
     */
    public DBObject ConsultarID(String id) {
        BasicDBObject filtro = new BasicDBObject();
        ObjectId a = new ObjectId(id);
        filtro.put("_id", a);
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        DBCursor cur = col.find(filtro);
        time_end = System.currentTimeMillis();
        time = (time_end - time_start);

        return cur.next();
    }

    public void InsertarInmagen() {
    }

    /**
     *
     * @param path
     * @param Basededatos
     * @param cubo
     * @param nombre
     */
    public void InsertarAudio(String path, DB Basededatos, String cubo, String nombre) {
        GridFS gfsAudio;
        GridFSInputFile gfsFile;
        File imageFile;
        imageFile = new File(path);
        gfsAudio = new GridFS(Basededatos, cubo);
        try {
            gfsFile = gfsAudio.createFile(imageFile);
            String newFileName = nombre;
            gfsFile.setFilename(newFileName);
            gfsFile.save();

        } catch (IOException ex) {
            Logger.getLogger(JavaMongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        //DBCollection collection = db.getCollection("Audio_meta");
        // collection.insert(info, WriteConcern.SAFE);
        //GridFS gfsAudioConsulta = new GridFS(db, "audio");
        //GridFSDBFile imageForOutput = gfsAudioConsulta.findOne("1073");
        //System.out.println(imageForOutput);
    }

    public static void LeeFichero(String Path) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(Path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            System.out.println("Nombre de las tablas. ");
            ArrayList<String> NombresTablas = new ArrayList<>();
            ArrayList<ArrayList<String>> AtributosTabla = new ArrayList<>();
            int contador = 0;
            boolean tabla = false;
            while ((linea = br.readLine()) != null) {
                //buscar los creates tables
                Pattern pat = Pattern.compile("[CREATE][create]");//CREATE TABLE 
                Matcher mat = pat.matcher(linea);
                if (mat.find()) {
                    int i = linea.indexOf("`");
                    int j = linea.lastIndexOf("`");

                    System.out.println("tabla: " + linea.substring(i + 1, j));
                    NombresTablas.add(linea.substring(i + 1, j));
                    contador++;
                    tabla = true;

                } else if (tabla == true) {
                    pat = Pattern.compile("`");
                    mat = pat.matcher(linea);
                    if (mat.find()) {
                        System.out.println();
                        int i = linea.indexOf("`");
                        int j = linea.lastIndexOf("`");
                        System.out.println(linea.substring(i + 1, j));
                    }
                }

                //cambiar las tablar por documentos...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /*public static void main(String[] args) throws UnknownHostException, IOException {

     JavaMongoDB conect = new JavaMongoDB("localhost", 27017);
     conect.AbrirBD("DemoJava");
     conect.Coleccion("Almacen");
     ArrayList<String> a = new ArrayList<>();
     a.add("carlos");
     a.add("nombre");
     conect.InsertarDocumento(a);

     //Recuperamos los valores de la colecciÃ³n, previamente hemos introducido 
     //unos valores desde la consola de mongo con db.things.save({name:"mongoDB"})
     /* DBCollection coleccion = db.getCollection("things");
     coleccion.drop();
     //coleccion.insert(doc);
     //Recuperamos el elemento
     //DBObject documento = coleccion.findOne();

     // returns default GridFS bucket (i.e. "fs" collection)
     db.dropDatabase();
     db = mongoClient.getDB("test");
     // saves the file to "fs" GridFS bucket        

     GridFS gfsAudio;
     GridFSInputFile gfsFile;
     File imageFile;
     //1074
     for (int i = 800; i < 1074; i++) {
     if (i < 1000) {
     imageFile = new File("C:\\DatosExtraidos\\Sample00" + i + "_audio.wav");
     } else {
     imageFile = new File("C:\\DatosExtraidos\\Sample0" + i + "_audio.wav");
     }
     gfsAudio = new GridFS(db, "audio");
     gfsFile = gfsAudio.createFile(imageFile);
     String newFileName = "" + i + "";
     gfsFile.setFilename(newFileName);
     gfsFile.save();
     if (i % 10 == 0) {
     System.out.println(i);
     }
     }
     //DBCollection collection = db.getCollection("Audio_meta");
     // collection.insert(info, WriteConcern.SAFE);

     GridFS gfsAudioConsulta = new GridFS(db, "audio");
     GridFSDBFile imageForOutput = gfsAudioConsulta.findOne("1073");
     System.out.println(imageForOutput);  */
    //}
}
