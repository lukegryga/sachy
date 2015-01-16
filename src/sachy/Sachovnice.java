
package sachy;

import java.awt.Point;

/**
 *
 * @author Lukáš
 */
public class Sachovnice {
    private static Sachovnice jedinacek = null;
    
    private Figura[][] rozmisteni;
    private boolean hra = false;
    private boolean jeNaTahu = true;
    
    public static Sachovnice getSachovnice(){
        if(jedinacek == null){
            jedinacek = new Sachovnice();
        }
        return jedinacek;
    }
    
    private Sachovnice(){}
    
    public boolean tahni(){
        return false;
        
    }
    
    int jeVolno(Point pozice){
        if(rozmisteni[pozice.x-1][pozice.y-1] == null){
            return -1;
        }else if(rozmisteni[pozice.x-1][pozice.y-1].barva){
            return 1;
        }
        return 0;
    }
    
    static Point prevedNaSouradnice(String tah){
        tah = tah.toLowerCase();
        char[] pozice = tah.toCharArray();
        if(pozice.length != 2){
            throw new IllegalArgumentException("Neplanty řetězec");
        }
        int x = pozice[0] - 96;
        int y = pozice[1] - '0';
        if((x >= 9 || x <= 0) || (y >= 9 || y <= 0)){
            throw new IllegalArgumentException("Neplatne souřadnice");
        }
        return new Point(x, y);
    }
    
}
