/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTW;

import java.util.LinkedList;
import org.bson.types.ObjectId;

/**
 *
 * @author manupc
 */
public class Gesto {

    // Un gesto es una secuencia de posturas representadas como ángulos
    public String nombre;
    public Double idMysql;
    public ObjectId idMongo ;
    public LinkedList<PosturaAsAngleList> secPosturas;
    public int idGesto; // Clase que identifica al gesto (-1= desconocida)

    public Gesto() {

        secPosturas = new LinkedList<>();
        idGesto = -1;
    }

    public void addPostura(PosturaAsAngleList pose) {

        secPosturas.add(pose);
    }

    public void setClase(int clase) { // Clase a la que pertenece el gesto
        idGesto = clase;
    }
}
