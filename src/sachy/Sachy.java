/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachy;

import java.awt.Point;

/**
 *
 * @author Lukáš
 */
public class Sachy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Sachovnice sachovnice = Sachovnice.getSachovnice();
        sachovnice.testMethod();
        Figura vez1 = sachovnice.rozmisteni[0][0];
        for(Point p : vez1.getMozneTahy()){
            System.out.println(vez1 + " -> " + Sachovnice.pointNaSouradnice(p));
        }
    }
    
}
