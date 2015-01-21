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
public enum SachoveSmery {
    nahoru(0,1, true),
    vpravoNahoru(1,1, false),
    vpravo(1,0, true),
    vpravoDolu(1,-1, false),
    dolu(0,-1, true),
    vlevoDolu(-1,-1, false),
    vlevo(-1,0, true),
    vlevoNahoru(-1,1, false);
    
    public final int posX;
    public final int posY;
    
    public final boolean primySmer;
    
    SachoveSmery(int posX, int posY, boolean primySmer){
        this.posX = posX;
        this.posY = posY;
        this.primySmer = primySmer;
    }
    
    public Point getPoleVeSmeru(Point odKud){
        Point poleVeSmeru = (Point) odKud.clone();
        poleVeSmeru.translate(posX, posY);
        return poleVeSmeru;
    }
    
    public Point premistiPointVeSmeru(Point presouvanaSouradnice){
        presouvanaSouradnice.translate(posX, posY);
        return presouvanaSouradnice;
    }
    
}
