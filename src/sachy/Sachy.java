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
        Kral kral = (Kral) sachovnice.rozmisteni[5][5];
        for(Point p : kral.getMozneTahy()){
            System.out.println(kral + " -> " + Sachovnice.pointNaSouradnice(p));
       }
        
        if(kral.jeKralOhrozen()){
            System.out.println("Ano");
        }else
            System.out.println("Ne");
    }
    
}
