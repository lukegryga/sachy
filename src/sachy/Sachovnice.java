
package sachy;

import java.awt.Point;

/**
 *
 * @author Lukáš
 */
public class Sachovnice {
    private static Sachovnice jedinacek = null;
    
    private Figura[][] rozmisteni;
    
    public static Sachovnice getSachovnice(){
        if(jedinacek == null){
            jedinacek = new Sachovnice();
        }
        return jedinacek;
    }
    
    private Sachovnice(){}
    
    int jeVolno(Point pozice){
        if(rozmisteni[pozice.x-1][pozice.y-1] == null){
            return -1;
        }else if(rozmisteni[pozice.x-1][pozice.y-1].barva){
            
            return 1;
        }
        return 0;
    }
    
}
