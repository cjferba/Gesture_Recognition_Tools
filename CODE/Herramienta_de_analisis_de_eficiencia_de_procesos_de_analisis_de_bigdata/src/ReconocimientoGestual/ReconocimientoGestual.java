package ReconocimientoGestual;

import DTW.Gesto;
import DataBase.JavaMongoDB;
import DataBase.JavaMySQL;
import Utilidades.Utilidades;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Carlos Jesus Fernandez Basso
 */
public class ReconocimientoGestual {

    private ArrayList<Double> DisMedCentroide;
    private ArrayList<ArrayList<Gesto>> ListaGestos;
    private ArrayList<Gesto> ListaPorClases;
    int[] listaDimensiones = new int[20];
    int[] listaDimensionesReduc = new int[4];

    /**
     * Contructor por defecto de la clase con el numero de cenroides y la
     * semilla para el random
     *
     */
    public ReconocimientoGestual() {

        ListaGestos = new ArrayList<>();
        DisMedCentroide = new ArrayList<>();
        listaDimensiones = new int[20];
        for (int i = 0; i < 20; i++) {
            listaDimensiones[i] = i;
        }
        listaDimensionesReduc = new int[4];
        listaDimensionesReduc[0] = 5;
        listaDimensionesReduc[1] = 6;
        listaDimensionesReduc[2] = 9;
        listaDimensionesReduc[3] = 10;

    }

    /**
     *
     * @param G1 Gesto 1
     * @param G2 Gesto 2
     * @param tipo booleano para elegir distancia a utilizar
     * @param dimension
     * @return valor de la distancia mediante DTW
     */
    public double DistanciaGestos(Gesto G1, Gesto G2, boolean tipo, boolean dimension) {
        int[] lista;
        if (dimension) {
            lista = listaDimensiones;
        } else {
            lista = listaDimensionesReduc;
        }
        if (tipo) {
            return DTW.DTW.DTW(G1, G2, 0, lista) / (G1.secPosturas.size() + G2.secPosturas.size());
        } else {
            return DTW.DTW.DTW(G1, G2, 1, lista) / (G1.secPosturas.size() + G2.secPosturas.size());

        }

    }

    /**
     *
     * @param ListGestos
     * @param G
     * @param tipo
     * @param dimension
     * @return
     */
    public int DistanciaMinConjunto(ArrayList<Gesto> ListGestos, Gesto G, boolean tipo, boolean dimension) {

        double valor;
        int lugar = 0;
        double Min = DistanciaGestos(G, ListGestos.get(0), tipo, dimension);
        for (int j = 1; j < ListGestos.size(); j++) {
            valor = DistanciaGestos(G, ListGestos.get(j), tipo, dimension);
            if (valor < Min && G != ListGestos.get(j)) {
                Min = valor;
                lugar = j;
            }
        }
        return lugar;

    }

    public String buscar(Gesto ges, ArrayList<Gesto> x, boolean tipo, boolean dimension) {
        String resul;
        int mejor = 0;
        double Min = DistanciaGestos(ges, x.get(0), tipo, dimension);
        for (int i = 1; i < x.size(); i++) {
            double Distancia = DistanciaGestos(ges, x.get(i), tipo, dimension);
            if (Distancia < Min) {
                mejor = i;
                Min = Distancia;
            }
        }
        resul = x.get(mejor).nombre;
        return resul;
    }

    public double Test(ArrayList<Gesto> gestosPrueba, ArrayList<Gesto> x, boolean tipo, boolean dimension) {
        double porcentaje;
        int acierto = 0;
        int fallo = 0;
        for (int i = 0; i < gestosPrueba.size(); i++) {
            String clase = buscar(gestosPrueba.get(i), x, tipo, dimension);
            // System.out.println(clase + " - " + gestosPrueba.get(i).nombre);
            if (clase.equals(gestosPrueba.get(i).nombre)) {
                acierto++;
            } else {
                fallo++;
            }
        }
        System.out.println("Aciertos: " + acierto + " - Fallos: " + fallo);
        porcentaje = (double) acierto / (double) gestosPrueba.size();
        return porcentaje;
    }

    public ArrayList<ArrayList<Integer>> probarBusquedaVecino(JavaMongoDB BD, ArrayList<String> Busquedaclases, ArrayList<String> Clases, ArrayList<Gesto> BaseDeConocimiento, int local, boolean tipo, boolean dimension) {
        ArrayList<ArrayList<Integer>> salida = new ArrayList<>();
        for (int i = 0; i < Busquedaclases.size(); i++) {
            BasicDBObject query = new BasicDBObject("Nombre", Busquedaclases.get(i));
            ArrayList<Gesto> x = Utilidades.ImportarDocumentoMongoDB(BD, query);
            ArrayList<Integer> ListaSoluciones = new ArrayList<>(Collections.nCopies(Clases.size(), 0));
            int acierto = 0;
            for (int j = 0; j < x.size(); j++) {
                String clas;

                clas = buscar(x.get(j), BaseDeConocimiento, tipo, dimension);

                ListaSoluciones.set(Clases.indexOf(clas), ListaSoluciones.get(Clases.indexOf(clas)) + 1);
            }
            salida.add(ListaSoluciones);
        }
        return salida;

    }

    public ArrayList<Integer> BuscarClase(JavaMongoDB BD, String clase, ArrayList<String> Clases, ArrayList<Gesto> BaseDeConocimiento, boolean tipo, boolean dimension) {
        BasicDBObject query = new BasicDBObject("Nombre", clase);
        ArrayList<Gesto> x = Utilidades.ImportarDocumentoMongoDB(BD, query);
        ArrayList<Integer> salida = new ArrayList<>(Collections.nCopies(Clases.size(), 0));
        int acierto = 0;
        for (int j = 0; j < x.size(); j++) {
            String clas;

            clas = buscar(x.get(j), BaseDeConocimiento, tipo, dimension);

            salida.set(Clases.indexOf(clas), salida.get(Clases.indexOf(clas)) + 1);
        }

        return salida;

    }

    public double probar(JavaMySQL BD, ArrayList<String> clases, ArrayList<Gesto> BaseDeConocimiento, int local, boolean busqueda, boolean dimension) {
        double total = 0, aciertototal = 0;
        for (int i = 0; i < clases.size(); i++) {
            ArrayList<Gesto> x = Utilidades.ImportarMySql(BD, clases.get(i));
            //System.out.println("clase: " + clases.get(i));
            //System.out.println("" + x.get(0).secPosturas.get(0) + x.get(3).secPosturas.get(0));
            /* for (int z = 0; z < 10; i++) {
             System.out.println("Salida: " + DTW.DTW.DTW(x.get(z), x.get(z + 1), 0));
             System.out.println("salida 2 : " + DTW.DTW.DTW(x.get(z), x.get(z + 1), 1));

             }*/
            System.out.println("\nClase:  " + clases.get(i) + " de tamaño: " + x.size());
            int acierto = 0;
            //ArrayList<Double> salida = new ArrayList<>();

            for (int j = 0; j < x.size(); j++) {
                String clas;
                //System.err.println(j + " de " + x.size());

                clas = buscar(x.get(j), BaseDeConocimiento, busqueda, dimension);

                if (clas.equals(clases.get(i))) {
                    acierto++;
                } else {
                    System.out.println("nos dice: " + clas + " , es: " + x.get(j).nombre + " clase: " + clases.get(i));
                }
            }
            aciertototal = acierto + aciertototal;
            total = total + x.size();

            System.out.print("   Acierto: " + acierto + " de " + x.size());

        }
        System.out.println("porcentaje: " + aciertototal / total);

        return 0.0;

    }

}
