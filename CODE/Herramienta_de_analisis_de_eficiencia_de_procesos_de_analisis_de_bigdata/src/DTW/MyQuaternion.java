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
public class MyQuaternion {
    
    
    public double x, y, z, w; // Componentes del Quaternion
        
        
    
    public MyQuaternion() {
        
        x= y= z= w= 0;
    }
    
    // Constructor del quaternion con sus 4 componentes
    public MyQuaternion(double _x, double _y, double _z, double _w) {
            
        x= _x;
        y= _y;
        z= _z;
        w= _w;
    }
    
}
