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
public class PosturaAsQuaternionList {

    // Articulaciones del cuerpo humano
    // Ver: http://msdn.microsoft.com/en-us/library/microsoft.kinect.jointtype.aspx
    public static final int HipCenter = 0;
    public static final int Spine = 1;
    public static final int ShoulderCenter = 2;
    public static final int Head = 3;

    public static final int ShoulderLeft = 4;
    public static final int ElbowLeft = 5;
    public static final int WristLeft = 6;
    public static final int HandLeft = 7;

    public static final int ShoulderRight = 8;
    public static final int ElbowRight = 9;
    public static final int WristRight = 10;
    public static final int HandRight = 11;

    public static final int HipLeft = 12;
    public static final int KneeLeft = 13;
    public static final int AnkleLeft = 14;
    public static final int FootLeft = 15;

    public static final int HipRight = 16;
    public static final int KneeRight = 17;
    public static final int AnkleRight = 18;
    public static final int FootRight = 19;

    final int NumJoints = 20;

    /* Lista de joints (quaternions) que representan una postura
     IMPORTANTE: El orden de almacenamiento de los joints importa
     Pongamos como referencia el siguiente orden de almacenamiento:
    
     */
    public MyQuaternion[] listaJoints;

    public PosturaAsQuaternionList() {

        listaJoints = new MyQuaternion[NumJoints];

        for (int i = 0; i < NumJoints; i++) {
            listaJoints[i] = new MyQuaternion();

        }

    }

    // Pone el quaternion de una articulación dada en la lista
    public void addQuaternion(int joint, MyQuaternion q) {

        listaJoints[joint] = q;
    }

}
