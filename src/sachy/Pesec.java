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
public class Pesec extends Figura{

    public Pesec(Point pozice, boolean barva) {
        super(pozice, barva);
    }
    
    boolean jeTahPovolen(Point kam) throws Exception{
        int psX = kam.x - pozice.x;
        int psY = kam.y - pozice.y;
        if(barva == true){
            
        }return false;
    }
    
}
