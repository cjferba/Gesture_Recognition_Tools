/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTW;

/**
 *
 * @author manupc
 */
public class DTW {


    /**
     * Aplica Dynamic Time Warping sobre 2 gestos
     * @param g1
     * @param g2
     * @param tipo 0 la distancia a utilizar es cherviser sino es la distancia euclidea
     * @return Devuelve la distancia o "cercanía" en el matching entre los 2 gestos
     */
    public static double DTW(Gesto g1, Gesto g2, int tipo, int [] lista) {

        double dist = 0;

        // Comprobación de que los gestos tienen datos
        if (g1.secPosturas.size() == 0 || g2.secPosturas.size() == 0) {
            return 0;
        }

        // Reserva de memoria
        double[][] matrizAux = new double[2][g2.secPosturas.size()];
        for (int i = 0; i < matrizAux.length; i++) {
            for (int j = 0; j < matrizAux[0].length; j++) {
                matrizAux[i][j] = 0;
            }
        }

        int filaActual, filaAnterior; // Variables para iterar entre las filas de matrizAux
        filaActual = 0;
        filaAnterior = 1;

        // Iteramos DTW
        for (int i = 0; i < g1.secPosturas.size(); i++) {

            filaActual = (filaActual + 1) % 2;
            filaAnterior = (filaAnterior + 1) % 2;

            for (int j = 0; j < g2.secPosturas.size(); j++) {
                double curDist = 0;
                if (tipo == 0) {
                    curDist = g1.secPosturas.get(i).distanciaChe(g2.secPosturas.get(j),lista);
                }
                if (tipo == 1) {
                    curDist = g1.secPosturas.get(i).distanciaEU(g2.secPosturas.get(j),lista);
                }
                double costoAcumulado = -1; // -1 significa infinito

                // Calculo del mejor costo acumulado
                if (i > 0) {
                    costoAcumulado = matrizAux[filaAnterior][j];
                }

                if (j > 0 && (matrizAux[filaActual][j - 1] < costoAcumulado || costoAcumulado == -1)) {
                    costoAcumulado = matrizAux[filaActual][j - 1];
                }

                if (i > 0 && j > 0 && (matrizAux[filaAnterior][j - 1] < costoAcumulado || costoAcumulado == -1)) {
                    costoAcumulado = matrizAux[filaAnterior][j - 1];
                }

                if (i == 0 && j == 0) // caso base
                {
                    costoAcumulado = 0;
                }

                // Calculamos coste de la celda actual
                matrizAux[filaActual][j] = curDist + costoAcumulado;
            }
        }

        // Calculamos distancia definitiva
        dist = matrizAux[filaActual][matrizAux[0].length - 1];

        return dist;
    }

}
