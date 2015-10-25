/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTW;


public class PosturaAsAngleList {

    // Padre de cada articulación en el cuerpo humano
    final int HipCenterParent = PosturaAsQuaternionList.HipCenter;
    final int SpineParent = PosturaAsQuaternionList.HipCenter;
    final int ShoulderCenterParent = PosturaAsQuaternionList.Spine;
    final int HeadParent = PosturaAsQuaternionList.ShoulderCenter;

    final int ShoulderLeftParent = PosturaAsQuaternionList.ShoulderCenter;
    final int ElbowLeftParent = PosturaAsQuaternionList.ShoulderLeft;
    final int WristLeftParent = PosturaAsQuaternionList.ElbowLeft;
    final int HandLeftParent = PosturaAsQuaternionList.WristLeft;

    final int ShoulderRightParent = PosturaAsQuaternionList.ShoulderCenter;
    final int ElbowRightParent = PosturaAsQuaternionList.ShoulderRight;
    final int WristRightParent = PosturaAsQuaternionList.ElbowRight;
    final int HandRightParent = PosturaAsQuaternionList.WristRight;

    final int HipLeftParent = PosturaAsQuaternionList.HipCenter;
    final int KneeLeftParent = PosturaAsQuaternionList.HipLeft;
    final int AnkleLeftParent = PosturaAsQuaternionList.KneeLeft;
    final int FootLeftParent = PosturaAsQuaternionList.AnkleLeft;

    final int HipRightParent = PosturaAsQuaternionList.HipCenter;
    final int KneeRightParent = PosturaAsQuaternionList.HipRight;
    final int AnkleRightParent = PosturaAsQuaternionList.KneeRight;
    final int FootRightParent = PosturaAsQuaternionList.AnkleRight;

    final int NumberOfAngles = 20;

    public double[] angleBetweenBones;
    private int[] lista;

    // Inicializa una postura representada como ángulos entre huesos
    // partiendo de una postura representada como quaternions
    public PosturaAsAngleList(PosturaAsQuaternionList pose) {

        angleBetweenBones = new double[NumberOfAngles];

        angleBetweenBones[PosturaAsQuaternionList.HipCenter] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.HipCenter],
                pose.listaJoints[HipCenterParent]);

        angleBetweenBones[PosturaAsQuaternionList.Spine] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.Spine],
                pose.listaJoints[SpineParent]);

        angleBetweenBones[PosturaAsQuaternionList.ShoulderCenter] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.ShoulderCenter],
                pose.listaJoints[ShoulderCenterParent]);

        angleBetweenBones[PosturaAsQuaternionList.Head] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.Head],
                pose.listaJoints[HeadParent]);

        angleBetweenBones[PosturaAsQuaternionList.ShoulderLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.ShoulderLeft],
                pose.listaJoints[ShoulderLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.ElbowLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.ElbowLeft],
                pose.listaJoints[ElbowLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.WristLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.WristLeft],
                pose.listaJoints[WristLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.HandLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.HandLeft],
                pose.listaJoints[HandLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.ShoulderRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.ShoulderRight],
                pose.listaJoints[ShoulderRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.ElbowRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.ElbowRight],
                pose.listaJoints[ElbowRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.WristRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.WristRight],
                pose.listaJoints[WristRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.HandRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.HandRight],
                pose.listaJoints[HandRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.HipLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.HipLeft],
                pose.listaJoints[HipLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.KneeLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.KneeLeft],
                pose.listaJoints[KneeLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.AnkleLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.AnkleLeft],
                pose.listaJoints[AnkleLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.FootLeft] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.FootLeft],
                pose.listaJoints[FootLeftParent]);

        angleBetweenBones[PosturaAsQuaternionList.HipRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.HipRight],
                pose.listaJoints[HipRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.KneeRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.KneeRight],
                pose.listaJoints[KneeRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.AnkleRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.AnkleRight],
                pose.listaJoints[AnkleRightParent]);

        angleBetweenBones[PosturaAsQuaternionList.FootRight] = AngleBetweenJoints(pose.listaJoints[PosturaAsQuaternionList.FootRight],
                pose.listaJoints[FootRightParent]);

    }

    
    private double AngleBetweenJoints(MyQuaternion joint, MyQuaternion parent) {

        // Pasamos los quaternions a vectores 3D
        double Xj, Yj, Zj, // Vector 3D del joint
                Xp, Yp, Zp; // Vector 3D del joint padre
        double angle = 0; // Angulo entre el joint y el joint parent

        Xj = 1 - (2 * joint.y * joint.y) - (2 * joint.z * joint.z);
        Yj = (2 * joint.x * joint.y) + (2 * joint.z * joint.w);
        Zj = 2 * joint.x * joint.z - 2 * joint.y * joint.w;

        Xp = 1 - 2 * parent.y * parent.y - 2 * parent.z * parent.z;
        Yp = 2 * parent.x * parent.y + 2 * parent.z * parent.w;
        Zp = 2 * parent.x * parent.z - 2 * parent.y * parent.w;

        // Calculamos producto vectorial entre padre y joint
        double XcrossP, YcrossP, ZcrossP;

        XcrossP = Yp * Zj - Zp * Yj;
        YcrossP = Zp * Xj - Xp * Zj;
        ZcrossP = Xp * Yj - Yp * Xj;

        // Ahora calculamos la norma del producto vectorial
        double normCrossP = Math.sqrt((XcrossP * XcrossP) + (YcrossP * YcrossP) + (ZcrossP * ZcrossP));

        // Calculamos producto escalar entre padre y joint
        double dotP;
        dotP = Xp * Xj + Yp * Yj + Zp * Zj;

        // El angulo entre el hijo y el padre se calcula como el atan entre los dos resultados anteriores
        angle = Math.atan2(normCrossP, dotP);

        return angle;
    }

    

    // Devuelve la distancia de Chebisher de la postura actual a la pasada por argumento
    public double distanciaChe(PosturaAsAngleList otraPose, int[] lista) {

        double dist = 0;

        for (int i = 0; i < lista.length; i++) {
            double aux = Math.abs(angleBetweenBones[lista[i]] - otraPose.angleBetweenBones[lista[i]]);
            if (aux > dist) {
                dist = aux;
            }
        }

        return dist;
    }

    public double distanciaEU(PosturaAsAngleList otraPose, int[] lista) {
       
        double dist = 0;

        for (int i = 0; i < lista.length; i++) {
            double aux = Math.pow(Math.abs(angleBetweenBones[lista[i]] - otraPose.angleBetweenBones[lista[i]]), 2);
            dist = dist + aux;
            // if (aux > dist) {
            //   dist = aux;
            // }
        }

        return Math.sqrt(dist);
    }

}
