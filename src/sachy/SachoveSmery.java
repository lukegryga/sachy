/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachy;

/**
 *
 * @author Lukáš
 */
public enum SachoveSmery {
    nahoru(0,1),
    vpravoNahoru(1,1),
    vpravo(1,0),
    vpravoDolu(1,-1),
    dolu(0,-1),
    vlevoDolu(-1,-1),
    vlevo(-1,0),
    vlevoNahoru(-1,1);
    
    public final int posX;
    public final int posY;
    
    SachoveSmery(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    
}
