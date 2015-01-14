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
    
    boolean pohniSe(Point kam) throws Exception{
        int psX = kam.x - pozice.x;
        int psY = kam.y - pozice.y;
        if(barva == true){
            if(pozice.y = 2){
                if(((psY == 1 || psY == 2 ) && psY == 0 ) ||
                        (psX == 1 || psX == -1) && psY == 1 && sachovnice.jeVolno(pozice) == 0);
            }
        }
    }
    
}
