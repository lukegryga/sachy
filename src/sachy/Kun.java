

package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Lukáš
 */
public class Kun extends Figura{
    
    /**
     *
     */
    public static final int hodnotaFigury = 3;

    /**
     *
     * @param pozice
     * @param barva
     */
    public Kun(Point pozice, boolean barva) {
        super(pozice, barva);
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        if(Sachovnice.existujeSouradnice(pozice.x + 1 , pozice.y + 2 )){
            if(sachovnice.jeVolno(pozice.x + 1, pozice.y + 2) != iBarva){       //Pokud na místě nestojí figura stejné barvy
                mozneTahy.add(new Point(pozice.x + 1, pozice.y + 2));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x + 2 , pozice.y + 1 )){
            if(sachovnice.jeVolno(pozice.x + 2, pozice.y + 1) != iBarva){
                mozneTahy.add(new Point(pozice.x + 2, pozice.y + 1));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x + 2 , pozice.y - 1 )){
            if(sachovnice.jeVolno(pozice.x + 2, pozice.y - 1) != iBarva){
                mozneTahy.add(new Point(pozice.x + 2, pozice.y - 1));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x + 1  , pozice.y - 2 )){
            if(sachovnice.jeVolno(pozice.x + 1, pozice.y - 2) != iBarva){
                mozneTahy.add(new Point(pozice.x + 1, pozice.y - 2));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x - 1 , pozice.y - 2 )){
            if(sachovnice.jeVolno(pozice.x - 1, pozice.y - 2) != iBarva){
                mozneTahy.add(new Point(pozice.x - 1, pozice.y - 2));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x - 2 , pozice.y - 1 )){
            if(sachovnice.jeVolno(pozice.x - 2, pozice.y - 1) != iBarva){
                mozneTahy.add(new Point(pozice.x - 2, pozice.y - 1));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x - 2 , pozice.y + 1 )){
            if(sachovnice.jeVolno(pozice.x - 2, pozice.y + 1) != iBarva){
                mozneTahy.add(new Point(pozice.x - 2, pozice.y + 1));
            }
        }
        if(Sachovnice.existujeSouradnice(pozice.x - 1 , pozice.y + 2 )){
            if(sachovnice.jeVolno(pozice.x - 1, pozice.y + 2) != iBarva){
                mozneTahy.add(new Point(pozice.x - 1, pozice.y + 2));
            }
        }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Kůň:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
}
