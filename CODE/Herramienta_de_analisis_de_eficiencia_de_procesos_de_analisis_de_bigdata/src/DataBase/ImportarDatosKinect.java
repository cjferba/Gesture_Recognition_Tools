package DataBase;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class ImportarDatosKinect {

    private JavaMongoDB BDMongoSubdocumentos;
    private double timeMongoDBSubdocumentos;
    private JavaMongoDB BDMongoReferencias;
    private double timeMongoDBReferencias;

    private JavaMySQL BDMySQL;
    private double timeMySql;

    private String TablaGestosSql = "CREATE TABLE IF NOT EXISTS `gestos` (\n"
            + "  `idGestos` bigint(20) unsigned NOT NULL AUTO_INCREMENT,\n"
            + "  `Nombre` varchar(60) NOT NULL,\n"
            + "  `NumFrame` int(11) NOT NULL,\n"
            + "  PRIMARY KEY (`idGestos`)\n"
            + ") ENGINE=InnoDB AUTO_INCREMENT=24913 DEFAULT CHARSET=utf8;";

    private String TablaWRSql = "CREATE TABLE IF NOT EXISTS `wr` (\n"
            + "  `idGesto` bigint(20) unsigned NOT NULL,\n"
            + "  `Frame` bigint(20) unsigned NOT NULL,\n"
            + "  `1` varchar(100) NOT NULL,\n"
            + "  `2` varchar(100) NOT NULL,\n"
            + "  `3` varchar(100) NOT NULL,\n"
            + "  `4` varchar(100) NOT NULL,\n"
            + "  `5` varchar(100) NOT NULL,\n"
            + "  `6` varchar(100) NOT NULL,\n"
            + "  `7` varchar(100) NOT NULL,\n"
            + "  `8` varchar(100) NOT NULL,\n"
            + "  `9` varchar(100) NOT NULL,\n"
            + "  `10` varchar(100) NOT NULL,\n"
            + "  `11` varchar(100) NOT NULL,\n"
            + "  `12` varchar(100) NOT NULL,\n"
            + "  `13` varchar(100) NOT NULL,\n"
            + "  `14` varchar(100) NOT NULL,\n"
            + "  `15` varchar(100) NOT NULL,\n"
            + "  `16` varchar(100) NOT NULL,\n"
            + "  `17` varchar(100) NOT NULL,\n"
            + "  `18` varchar(100) NOT NULL,\n"
            + "  `19` varchar(100) NOT NULL,\n"
            + "  `20` varchar(100) NOT NULL,\n"
            + "  PRIMARY KEY (`Frame`,`idGesto`),\n"
            + "  KEY `FK_WR_idx` (`idGesto`),\n"
            + "  CONSTRAINT `FK_WR` FOREIGN KEY (`idGesto`) REFERENCES `gestos` (`idGestos`) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    private String TablaWPSql = "CREATE TABLE IF NOT EXISTS `wp` (\n"
            + "  `idGesto` bigint(20) unsigned NOT NULL,\n"
            + "  `Frame` bigint(20) unsigned NOT NULL,\n"
            + "  `1` varchar(100) NOT NULL,\n"
            + "  `2` varchar(100) NOT NULL,\n"
            + "  `3` varchar(100) NOT NULL,\n"
            + "  `4` varchar(100) NOT NULL,\n"
            + "  `5` varchar(100) NOT NULL,\n"
            + "  `6` varchar(100) NOT NULL,\n"
            + "  `7` varchar(100) NOT NULL,\n"
            + "  `8` varchar(100) NOT NULL,\n"
            + "  `9` varchar(100) NOT NULL,\n"
            + "  `10` varchar(100) NOT NULL,\n"
            + "  `11` varchar(100) NOT NULL,\n"
            + "  `12` varchar(100) NOT NULL,\n"
            + "  `13` varchar(100) NOT NULL,\n"
            + "  `14` varchar(100) NOT NULL,\n"
            + "  `15` varchar(100) NOT NULL,\n"
            + "  `16` varchar(100) NOT NULL,\n"
            + "  `17` varchar(100) NOT NULL,\n"
            + "  `18` varchar(100) NOT NULL,\n"
            + "  `19` varchar(100) NOT NULL,\n"
            + "  `20` varchar(100) NOT NULL,\n"
            + "  PRIMARY KEY (`Frame`,`idGesto`),\n"
            + "  KEY `FK_WP_idx` (`idGesto`),\n"
            + "  CONSTRAINT `FK_WP` FOREIGN KEY (`idGesto`) REFERENCES `gestos` (`idGestos`) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    private String TablaPPSql = "CREATE TABLE IF NOT EXISTS `pp` (\n"
            + "  `idGesto` bigint(20) unsigned NOT NULL,\n"
            + "  `Frame` bigint(20) unsigned NOT NULL,\n"
            + "  `1` varchar(100) NOT NULL,\n"
            + "  `2` varchar(100) NOT NULL,\n"
            + "  `3` varchar(100) NOT NULL,\n"
            + "  `4` varchar(100) NOT NULL,\n"
            + "  `5` varchar(100) NOT NULL,\n"
            + "  `6` varchar(100) NOT NULL,\n"
            + "  `7` varchar(100) NOT NULL,\n"
            + "  `8` varchar(100) NOT NULL,\n"
            + "  `9` varchar(100) NOT NULL,\n"
            + "  `10` varchar(100) NOT NULL,\n"
            + "  `11` varchar(100) NOT NULL,\n"
            + "  `12` varchar(100) NOT NULL,\n"
            + "  `13` varchar(100) NOT NULL,\n"
            + "  `14` varchar(100) NOT NULL,\n"
            + "  `15` varchar(100) NOT NULL,\n"
            + "  `16` varchar(100) NOT NULL,\n"
            + "  `17` varchar(100) NOT NULL,\n"
            + "  `18` varchar(100) NOT NULL,\n"
            + "  `19` varchar(100) NOT NULL,\n"
            + "  `20` varchar(100) NOT NULL,\n"
            + "  PRIMARY KEY (`Frame`,`idGesto`),\n"
            + "  KEY `FK_PP_idx` (`idGesto`),\n"
            + "  CONSTRAINT `FK_PP` FOREIGN KEY (`idGesto`) REFERENCES `gestos` (`idGestos`) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    /**
     * Creacion de un objeto con las bases de datos null
     */
    public ImportarDatosKinect() {
        BDMongoSubdocumentos = null;
        BDMongoReferencias = null;
        BDMySQL = null;
    }

    /**
     * Funcion para borrar todos los datos de las bases de datos
     *
     * @param MySQL conexion a la base de datos MySQL
     * @param MongoDBSubdocumentos conexion a la base de datos MongoDB
     * subdocumentos
     * @param MongoDBReferencias conexion a la base de datos MongoDB referencias
     */
    public void Reset(boolean MySQL, boolean MongoDBSubdocumentos, boolean MongoDBReferencias) {
        timeMongoDBSubdocumentos = 0;
        timeMongoDBReferencias = 0;
        timeMySql = 0;
        if (MongoDBSubdocumentos && BDMongoSubdocumentos.db.collectionExists("Gestos")) {
            BDMongoSubdocumentos.borrarColeccion(BDMongoSubdocumentos.col.getName());
            BDMongoSubdocumentos.db.createCollection("Gestos", null);
        }
        if (MongoDBReferencias && BDMongoReferencias.db.collectionExists("Gestos2") && BDMongoReferencias.db.collectionExists("GestosListas")) {
            BDMongoReferencias.borrarColeccion("Gestos2");
            BDMongoReferencias.borrarColeccion("GestosListas");
            BDMongoReferencias.db.createCollection("Gestos2", null);
            BDMongoReferencias.db.createCollection("GestosListas", null);
        }
        if (MySQL) {
            try {
                BDMySQL.st.execute("DROP TABLE IF EXISTS pp , wp , wr;");
                BDMySQL.st.execute("DROP TABLE IF EXISTS gestos; ");
                BDMySQL.CrearTabla(TablaGestosSql);
                BDMySQL.CrearTabla(TablaWRSql);
                BDMySQL.CrearTabla(TablaWPSql);
                BDMySQL.CrearTabla(TablaPPSql);
            } catch (SQLException ex) {
                Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void Delete() {
        timeMongoDBSubdocumentos = 0;
        timeMySql = 0;
        BDMongoSubdocumentos.col.remove(null);
        BDMongoReferencias.col.remove(null);

        try {
            BDMySQL.st.execute("DROP TABLE IF EXISTS `proyectogestos`.`gestos`, `proyectogestos`.`pp`, `proyectogestos`.`wp`, `proyectogestos`.`wr`;");
            BDMySQL.CrearTabla(TablaGestosSql);
            BDMySQL.CrearTabla(TablaWRSql);
            BDMySQL.CrearTabla(TablaWPSql);
            BDMySQL.CrearTabla(TablaPPSql);
        } catch (SQLException ex) {
            Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Constructor con parametros
     *
     * @param BD MongoDB Subdocumentos
     * @param BD2 MongoDB Referencias
     * @param BD3 MySQL
     */
    public ImportarDatosKinect(JavaMongoDB BD, JavaMongoDB BD2, JavaMySQL BD3) {
        BDMongoSubdocumentos = BD;
        BDMongoReferencias = BD2;
        BDMySQL = BD3;
    }

    /**
     * Leemos un archivo y procesamos los datos para insertarlos en la base de
     * datos
     *
     * @param MySQL booleano para indicar , el insertar los datos en MySQL
     * @param MongoDB_Subdocumentos booleano para indicar , el insertar los
     * datos en MongoDB con el formato de subdocumentos
     * @param MongoDB_Referencias booleano para indicar , el insertar los datos
     * en MongoDB con el formato de referencias
     */
    private void leerArchivosDatos(boolean MySQL, boolean MongoDB_Subdocumentos, boolean MongoDB_Referencias, String path) {
        String linea;

        timeMongoDBSubdocumentos = 0;
        timeMongoDBReferencias = 0;
        timeMySql = 0;

        //variables utilizadas
        String SqlFrameWP = "";
        String SqlFramePP = "";
        String SqlFrameWR = "";
        BasicDBObject DocumentTipo1 = new BasicDBObject();
        BasicDBObject DocumentTipo2 = new BasicDBObject();
        BasicDBList ListaAux = new BasicDBList();
        BasicDBList ListaIdsMongo = new BasicDBList();
        //variables dentro del for
        BasicDBObject FrameMG = null;
        BasicDBObject FrameMGTipo2 = null;
        BasicDBList ListaWR = new BasicDBList();
        BasicDBList ListaWP = new BasicDBList();
        BasicDBList ListaPP = new BasicDBList();

        // String Sql = "";
        try {
            File archivo = new File(path);
            FileReader lectura = new FileReader(archivo);
            BufferedReader BuffLectu = new BufferedReader(lectura);

            while ((linea = BuffLectu.readLine()) != null) {

                if (MongoDB_Subdocumentos) {
                    DocumentTipo1 = new BasicDBObject();
                    ListaAux = new BasicDBList();
                }
                if (MongoDB_Referencias) {
                    DocumentTipo2 = new BasicDBObject();
                    ListaIdsMongo = new BasicDBList();
                }

                if (MySQL) {
                    SqlFrameWP = "INSERT INTO `wp`"
                            + " (`idGesto`, `Frame`,`1`, `2`, `3`, `4`, `5`, `6`, `7`,"
                            + " `8`, `9`, `10`, `11`, `12`, `13`, "
                            + "`14`, `15`, `16`, `17`, `18`, `19`, `20`) VALUES ";
                    SqlFramePP = "INSERT INTO `pp`"
                            + " (`idGesto`, `Frame`,`1`, `2`, `3`, `4`, `5`, `6`, `7`,"
                            + " `8`, `9`, `10`, `11`, `12`, `13`, "
                            + "`14`, `15`, `16`, `17`, `18`, `19`, `20`) VALUES ";
                    SqlFrameWR = "INSERT INTO `wr`"
                            + " (`idGesto`, `Frame`,`1`, `2`, `3`, `4`, `5`, `6`, `7`,"
                            + " `8`, `9`, `10`, `11`, `12`, `13`, "
                            + "`14`, `15`, `16`, `17`, `18`, `19`, `20`) VALUES ";
                }
                String[] aux1 = linea.split("-");

                int numero = Integer.parseInt(aux1[1].replace(" ", "").replace(":", ""));
                if (MongoDB_Subdocumentos) {
                    DocumentTipo1.append("Nombre", aux1[0]);
                }
                if (MongoDB_Referencias) {
                    DocumentTipo2.append("Nombre", aux1[0]);
                }

                int IdGesto = -1;
                if (MySQL) {
                    String SqlNombre = "INSERT INTO gestos (Nombre, NumFrame) VALUES ('" + aux1[0] + "', " + numero + ");";
                    BDMySQL.insertar(SqlNombre);
                    timeMySql = timeMySql + BDMySQL.GetTime();
                    ResultSet consultaAux = BDMySQL.Consultar("SELECT max(idGestos) FROM gestos;");
                    consultaAux.next();
                    IdGesto = consultaAux.getInt(1);
                }
                /**
                 * for para recorrer el numero de frames del gesto
                 */
                for (int i = 0; i <= numero; i++) {

                    //tipo de estructura 1 mongoDB
                    if (MongoDB_Subdocumentos || MongoDB_Referencias) {
                        FrameMG = new BasicDBObject();
                        ListaWR = new BasicDBList();
                        ListaWP = new BasicDBList();
                        ListaPP = new BasicDBList();
                        FrameMGTipo2 = new BasicDBObject();
                    }

                    //tipo de estructura 2 mongoDB
                    ArrayList<BasicDBObject> ListaFrameIds = new ArrayList<>();

                    //Variables para extraer datos
                    String[] ListaMyWR = new String[20];
                    String[] ListaMyWP = new String[20];
                    String[] ListaMyPP = new String[20];

                    aux1[1] = aux1[1].replace(":", "");
                    aux1[1] = aux1[1].replace(" ", "");

                    String lineaFarme = BuffLectu.readLine();
                    lineaFarme = lineaFarme.replace("[", "");
                    lineaFarme = lineaFarme.replace(";", "");
                    int NumeroFrame = Integer.parseInt(lineaFarme);
                    if (MongoDB_Subdocumentos) {
                        FrameMG.append("Numero", NumeroFrame);
                    }
                    if (MongoDB_Referencias) {
                        FrameMGTipo2.append("Numero", NumeroFrame);
                    }
                    //posiciones en el espacio del frame 
                    String lineaFarmeWR = BuffLectu.readLine();
                    lineaFarmeWR = lineaFarmeWR.replace("WR{", "");
                    lineaFarmeWR = lineaFarmeWR.replace("){", "");
                    String WR[] = lineaFarmeWR.split("\\);\\(");

                    //posiciones en el espacio del frame 
                    String lineaFarmeWP = BuffLectu.readLine();
                    lineaFarmeWP = lineaFarmeWP.replace("WP{", "");
                    lineaFarmeWP = lineaFarmeWP.replace("){", "");
                    String WP[] = lineaFarmeWP.split("\\);\\(");

                    //posiciones en el espacio del frame 
                    String lineaFarmePP = BuffLectu.readLine();
                    lineaFarmePP = lineaFarmePP.replace("PP{", "");
                    lineaFarmePP = lineaFarmePP.replace("){", "");
                    String PP[] = lineaFarmePP.split("\\);\\(");

                    for (int j = 0; j < 20; j++) {

                        BasicDBList ListaWRIds = new BasicDBList();
                        BasicDBList ListaWPIds = new BasicDBList();
                        BasicDBList ListaPPIds = new BasicDBList();
                        BasicDBObject FrameId = new BasicDBObject();

                        //WR
                        BasicDBList ListaAux2 = new BasicDBList();
                        WR[j] = WR[j].replace(")}", "");
                        String[] PointWR = WR[j].split(",");
                        PointWR[0] = PointWR[0].replace("(", "");
                        if (MongoDB_Subdocumentos || MongoDB_Referencias) {
                            ListaAux2.add(Double.parseDouble(PointWR[0]));
                            ListaAux2.add(Double.parseDouble(PointWR[1]));
                            ListaAux2.add(Double.parseDouble(PointWR[2]));
                            ListaAux2.add(Double.parseDouble(PointWR[3]));
                            ListaWR.add(j, ListaAux2);
                        }
                        String StrWR = "" + PointWR[0] + " " + PointWR[1] + " " + PointWR[2] + " " + PointWR[3];

                        ListaMyWR[j] = StrWR;

                        //WP
                        ListaAux2 = new BasicDBList();
                        WP[j] = WP[j].replace(")}", "");
                        String[] PointWP = WP[j].split(",");
                        if (MongoDB_Subdocumentos || MongoDB_Referencias) {
                            ListaAux2.add(Double.parseDouble(PointWP[0].replace("(", "")));
                            ListaAux2.add(Double.parseDouble(PointWP[1]));
                            ListaAux2.add(Double.parseDouble(PointWP[2]));
                            ListaWP.add(j, ListaAux2);
                        }
                        String StrWP = "" + PointWR[0] + " " + PointWR[1] + " " + PointWR[2];

                        ListaMyWP[j] = StrWP;
                        //ObjetoInsercionTipo2.append("WP", ListaAux2);
                        // ListaWPIds.add(ObjetoInsercionTipo2.getObjectId("_id"));

                        //PP
                        ListaAux2 = new BasicDBList();
                        PP[j] = PP[j].replace(")}", "");
                        String[] PointPP = PP[j].split(",");
                        if (MongoDB_Subdocumentos || MongoDB_Referencias) {
                            ListaAux2.add(Double.parseDouble(PointPP[0].replace("(", "")));
                            ListaAux2.add(Double.parseDouble(PointPP[1]));
                            ListaPP.add(j, ListaAux2);
                        }
                        String StrPP = "" + PointWR[0] + " " + PointWR[1];

                        ListaMyPP[j] = StrPP;

                        //ObjetoInsercionTipo2.append("PP", ListaAux2);
                        //ListaPPIds.add(ObjetoInsercionTipo2.getObjectId("_id"));
                    }
                    if (MongoDB_Referencias) {
                        FrameMGTipo2.append("WR", ListaWR);
                        FrameMGTipo2.append("WP", ListaWP);
                        FrameMGTipo2.append("PP", ListaPP);
                        BDMongoReferencias.AbrirColeccion("Gestos2");
                        //System.out.println("mongo: " + BDMongoTipo2.col + " elemento" + FrameMGTipo2);
                        BDMongoReferencias.InsertarDocumento(FrameMGTipo2);
                        ListaIdsMongo.add(FrameMGTipo2.getObjectId("_id"));
                        timeMongoDBReferencias = timeMongoDBReferencias + BDMongoReferencias.GetTime();
                    }
                    if (MongoDB_Subdocumentos) {
                        FrameMG.append("WR", ListaWR);
                        FrameMG.append("WP", ListaWP);
                        FrameMG.append("PP", ListaPP);
                        ListaAux.add(FrameMG);
                    }
                    String lineaFinal = BuffLectu.readLine();

                    if (MySQL) {
                        SqlFrameWR = SqlFrameWR + "\n(" + IdGesto + "," + NumeroFrame + ",'" + ListaMyWR[0] + "','" + ListaMyWR[1]
                                + "','" + ListaMyWR[2] + "','" + ListaMyWR[3] + "','" + ListaMyWR[4] + "','" + ListaMyWR[5]
                                + "','" + ListaMyWR[6] + "','" + ListaMyWR[7] + "','" + ListaMyWR[8] + "','" + ListaMyWR[9]
                                + "','" + ListaMyWR[10] + "','" + ListaMyWR[11] + "','" + ListaMyWR[12] + "','" + ListaMyWR[13]
                                + "','" + ListaMyWR[14] + "','" + ListaMyWR[15] + "','" + ListaMyWR[16] + "','" + ListaMyWR[17]
                                + "','" + ListaMyWR[18] + "','" + ListaMyWR[19] + "'),";
                        SqlFrameWP = SqlFrameWP + "\n(" + IdGesto + "," + NumeroFrame + ",'" + ListaMyWP[0] + "','" + ListaMyWP[1]
                                + "','" + ListaMyWP[2] + "','" + ListaMyWP[3] + "','" + ListaMyWP[4] + "','" + ListaMyWP[5]
                                + "','" + ListaMyWP[6] + "','" + ListaMyWP[7] + "','" + ListaMyWP[8] + "','" + ListaMyWP[9]
                                + "','" + ListaMyWP[10] + "','" + ListaMyWP[11] + "','" + ListaMyWP[12] + "','" + ListaMyWP[13]
                                + "','" + ListaMyWP[14] + "','" + ListaMyWP[15] + "','" + ListaMyWP[16] + "','" + ListaMyWP[17]
                                + "','" + ListaMyWP[18] + "','" + ListaMyWP[19] + "'),";
                        SqlFramePP = SqlFramePP + "\n(" + IdGesto + "," + i + ",'" + ListaMyPP[0] + "','" + ListaMyPP[1]
                                + "','" + ListaMyPP[2] + "','" + ListaMyPP[3] + "','" + ListaMyPP[4] + "','" + ListaMyPP[5]
                                + "','" + ListaMyPP[6] + "','" + ListaMyPP[7] + "','" + ListaMyPP[8] + "','" + ListaMyPP[9]
                                + "','" + ListaMyPP[10] + "','" + ListaMyPP[11] + "','" + ListaMyPP[12] + "','" + ListaMyPP[13]
                                + "','" + ListaMyPP[14] + "','" + ListaMyPP[15] + "','" + ListaMyPP[16] + "','" + ListaMyPP[17]
                                + "','" + ListaMyPP[18] + "','" + ListaMyPP[19] + "'),";

                    }
                }

                //Mongo Tipo1 Insert
                if (MongoDB_Subdocumentos) {
                    DocumentTipo1.append("Frames", ListaAux);
                    BDMongoSubdocumentos.InsertarDocumento(DocumentTipo1);
                    timeMongoDBSubdocumentos = timeMongoDBSubdocumentos + BDMongoSubdocumentos.GetTime();
                }
                //Mongo Tipo2 Insert
                if (MongoDB_Referencias) {
                    DocumentTipo2.append("Frames", ListaIdsMongo);
                    BDMongoReferencias.AbrirColeccion("GestosListas");
                    //  System.out.println("mongo: " + BDMongoTipo2.col + " elemento: " + DocumentTipo2);
                    BDMongoReferencias.InsertarDocumento(DocumentTipo2);
                    timeMongoDBReferencias = timeMongoDBReferencias + BDMongoReferencias.GetTime();
                }
                if (MySQL) {
                    //MySql Insert             
                    BDMySQL.insertar(SqlFrameWR.substring(0, SqlFrameWR.length() - 1));
                    timeMySql = timeMySql + BDMySQL.GetTime();
                    BDMySQL.insertar(SqlFrameWP.substring(0, SqlFrameWP.length() - 1));
                    timeMySql = timeMySql + BDMySQL.GetTime();
                    BDMySQL.insertar(SqlFramePP.substring(0, SqlFramePP.length() - 1));
                    timeMySql = timeMySql + BDMySQL.GetTime();
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*if (MongoDBtipo1) {
         BDMongoSubdocumentos.close();
         }
         if (MongoDBtipo2) {
         BDMongoReferencias.close();
         }
         if (MySQL) {
         BDMySQL.cerrar();
         }*/

    }

    /**
     * Realiza la insercion de los datos de los archivos .txt, en las bases de
     * datos indicadas, mongo estruturada por referencias, mongo estruturada por
     * subdocumentos y MySQL
     *
     * @param ListaRutas lista de las rutas a los archivos a importar
     * @param MySQL booleano que nos indica la insercion en la base de datos
     * MySQL
     * @param MongoDB_Subdocumentos booleano que nos indica la insercion en la
     * base de datos Mongo tipo subdocumentos
     * @param MongoDB_Referencias booleano que nos indica la insercion en la
     * base de datos Mongo tipo Referencias
     * @return devuelve el numero de gestos insertados
     */
    public int ImportarDatos(ArrayList<String> ListaRutas, boolean MySQL, boolean MongoDB_Subdocumentos, boolean MongoDB_Referencias) {
        double TimeMG = 0, TimeMG2 = 0, TimeMY = 0;
        for (int i = 0; i < ListaRutas.size(); i++) {
            this.leerArchivosDatos(MySQL, MongoDB_Subdocumentos, MongoDB_Referencias, ListaRutas.get(i));
            TimeMG = TimeMG + timeMongoDBSubdocumentos;
            TimeMG2 = TimeMG2 + timeMongoDBReferencias;
            TimeMY = TimeMY + timeMySql;
        }
        timeMongoDBSubdocumentos = TimeMG;
        timeMongoDBReferencias = TimeMG2;
        timeMySql = TimeMY;
        long registros = 0;
        if (MongoDB_Referencias) {
            registros = BDMongoReferencias.Count();
        } else if (MongoDB_Subdocumentos) {
            registros = BDMongoSubdocumentos.Count();
        } else if (MySQL) {
            try {
                ResultSet numero = BDMySQL.Consultar("Select count(*) as COUNT from gestos");
                numero.next();
                registros = numero.getInt("COUNT");
            } catch (SQLException ex) {
                Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (int) registros;

    }

    public int ImportarDatos(String ruta, boolean MySQL, boolean MongoDB_Subdocumentos, boolean MongoDB_Referencias, boolean reg) {

        this.leerArchivosDatos(MySQL, MongoDB_Subdocumentos, MongoDB_Referencias, ruta);
        long registros = 0;
        if (reg) {
            if (MongoDB_Referencias) {
                registros = BDMongoReferencias.Count();
            } else if (MongoDB_Subdocumentos) {
                registros = BDMongoSubdocumentos.Count();
            } else if (MySQL) {
                try {
                    ResultSet numero = BDMySQL.Consultar("Select count(*) as COUNT from gestos");
                    numero.next();
                    registros = numero.getInt("COUNT");
                } catch (SQLException ex) {
                    Logger.getLogger(ImportarDatosKinect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return (int) registros;
    }

    /**
     * funcion para consultar tiempos
     *
     * @param BD entero: 1- tiempo MongoDB Subdocumentos 2- tiempo MongoDB
     * referencias 3- tiempo MySQL
     * @return tiempo en realizar la importacion de datos
     */
    public double getTime(int BD) {
        if (BD == 1) {
            return timeMongoDBSubdocumentos;
        } else if (BD == 2) {
            return timeMongoDBReferencias;
        } else if (BD == 3) {
            return timeMySql;
        } else {
            return 0;
        }
    }
    /*  public static void main(String[] args) {
     ImportarDatosKinect a;
     double TimeMG = 0, TimeMY = 0, TimeMG2 = 0;
     JavaMongoDB AuxBDMongo = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo.AbrirBD("ProyectoGestosTrainning");
     AuxBDMongo.AbrirColeccion("Gestos");
     JavaMongoDB AuxBDMongo2 = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo2.AbrirBD("ProyectoGestosTrainning");
     AuxBDMongo2.AbrirColeccion("Gestos2");

     JavaMySQL AuxBDMySQL = new JavaMySQL("jdbc:mysql://localhost/proyectogestos", "root", "alfaomega");
     a = new ImportarDatosKinect(AuxBDMongo, AuxBDMongo2, AuxBDMySQL, null);
     a.Reset(true, true, true);
     for (int i = 1; i < 403; i++) {
     if (i != 2 && i != 331 && i != 349 && i != 350 && i != 382 && i != 387 && i != 388 && i != 398 && i != 399 && i != 400) {
     String p = "F:\\Datos Proyecto\\trainning\\Sample" + i + "_data.txt";
     AuxBDMongo = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo.AbrirBD("ProyectoGestosTrainning");
     AuxBDMongo.AbrirColeccion("Gestos");
     AuxBDMySQL = new JavaMySQL("jdbc:mysql://localhost/proyectogestos", "root", "alfaomega");
     AuxBDMongo2 = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo2.AbrirBD("ProyectoGestosTrainning");
     AuxBDMongo2.AbrirColeccion("Gestos2");
     a = new ImportarDatosKinect(AuxBDMongo, AuxBDMongo2, AuxBDMySQL, p);
     a.leerArchivosDatos(true, true, true);
     TimeMG = TimeMG + a.timeMongoDBSubdocumentos;
     TimeMG2 = TimeMG2 + a.timeMongoDBReferencias;
     TimeMY = TimeMY + a.timeMySql;
     System.out.println(i);
     }
     }
     System.out.println("Tiempo MG: " + TimeMG);
     System.out.println("Tiempo MY: " + TimeMY);
     System.out.println("Tiempo MG2: " + TimeMG2);
     /*
     //Validacion
     AuxBDMongo = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo.AbrirBD("ProyectoGestosValidacion");
     AuxBDMongo.AbrirColeccion("Gestos");
     AuxBDMongo2 = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo2.AbrirBD("ProyectoGestosValidacion");
     AuxBDMongo2.AbrirColeccion("Gestos2");
     JavaMySQL AuxBDMySQL = new JavaMySQL("jdbc:mysql://localhost/test", "root", "alfaomega");
     a = new ImportarDatosKinect(AuxBDMongo, AuxBDMongo2, AuxBDMySQL, null);
     a.Reset(true, true, true);
     for (int i = 410; i <= 710; i++) {
     if (i != 419 && i != 511 && i != 512 && i != 513 && i != 514 && i != 515 && i != 540
     && i != 551 && ((556 > i) || (i > 621)) && i != 701 && i != 690 && i != 691 && i != 650 && i != 649 && i != 652) {

     String p = "F:\\Datos Proyecto\\validation\\Sample" + i + "_data.txt";
     AuxBDMongo = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo.AbrirBD("ProyectoGestosValidacion");
     AuxBDMongo.AbrirColeccion("Gestos");
     AuxBDMongo2 = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo2.AbrirBD("ProyectoGestosValidacion");
     AuxBDMongo2.AbrirColeccion("Gestos2");
     AuxBDMySQL = new JavaMySQL("jdbc:mysql://localhost/test", "root", "alfaomega");
     a = new ImportarDatosKinect(AuxBDMongo, AuxBDMongo2, AuxBDMySQL, p);
     a.leerArchivosDatos(true, true, true);
     System.out.println(i);
     }
     }

     /*
     ImportarDatosKinect a;

     JavaMongoDB AuxBDMongo = new DataBase.JavaMongoDB("localhost", 27017);
     AuxBDMongo.AbrirBD("test");
     AuxBDMongo.AbrirColeccion("nombreColeccion");
     DBObject DocumentTipo1 = new BasicDBObject();
     BasicDBList xx = new BasicDBList();
     for (int i = 0; i < 20; i++) {
     xx.add(i);
     }
     DocumentTipo1.put("list", xx);

     WriteResult x = AuxBDMongo.col.insert(DocumentTipo1);
     System.out.println("elemento1:" + DocumentTipo1.get("_id"));
     DBCursor s = AuxBDMongo.col.find(DocumentTipo1);
     System.out.println("elemento2:" + DocumentTipo1.get("_id"));
     */
        //System.out.println("elemento:" + s.next().get("_id"));
    //System.out.println("Lista:" + s.next().get("list"));
    //System.out.println("elemento:" + s.next().get("_id"));
    // System.out.println("id: " + x.getField("_id") + "  ");///no sirve
    //}
}
