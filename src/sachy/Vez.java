
package sachy;

import java.awt.Point;
import java.util.ArrayList;


public class Vez extends Figura {
    
    public static final int hodnotaFigury = 5;

    public Vez(Point pozice, boolean barva) {
        super(pozice, barva);
    }
    
    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        int pXPos = pozice.x;
        int pYPos = pozice.y;
        //Ověřování směru vpravo
        while(Sachovnice.existujeSouradnice(++ pXPos, pYPos )){                 //Pokud souradnice o jedna do prava existuje
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){                     //Pokud na místě nestojí figura stejné barvy
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){                     //Pokud na míste stojí figura cizí barvy (Není tam przdno)
                    break;
                }
            }else
                break;
        }
        pXPos = pozice.x;
        //Ověřování směru vlevo
        while(Sachovnice.existujeSouradnice(-- pXPos, pYPos )){
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){
                    break;
                }
            }else
                break;
        }
        pXPos = pozice.x;
        //Ověřování směru nahoru
        while(Sachovnice.existujeSouradnice(pXPos, ++pYPos )){
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){
                    break;
                }
            }else
                break;
        }
        pYPos = pozice.y;
        //Ověřování směru dolu
        while(Sachovnice.existujeSouradnice(pXPos, --pYPos )){
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){
                    break;
                }
            }else
                break;
        }
        return mozneTahy;
    }

    
    @Override
    public String toString(){
        return "Věž:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
}
