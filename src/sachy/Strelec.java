

package sachy;

import java.awt.Point;
import java.util.ArrayList;


public class Strelec extends Figura{
    
    public static final int hodnotaFigury = 3;

    public Strelec(Point pozice, boolean barva) {
        super(pozice, barva);
    }

    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        int pXPos = pozice.x;
        int pYPos = pozice.y;
        //Ověřování směru vpravoNahoru
        while(Sachovnice.existujeSouradnice(++ pXPos,++ pYPos )){                 //Pokud souradnice o jedna do pravaNahoru existuje
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){                     //Pokud na místě nestojí figura stejné barvy
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){                     //Pokud na míste stojí figura cizí barvy (Není tam przdno)
                    break;
                }
            }else
                break;
        }
        pXPos = pozice.x;
        pYPos = pozice.y;
        //Ověřování směru vpravoDolu
        while(Sachovnice.existujeSouradnice(++ pXPos,-- pYPos )){
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){
                    break;
                }
            }else
                break;
        }
        pXPos = pozice.x;
        pYPos = pozice.y;
        //Ověřování směru vlevoDolu
        while(Sachovnice.existujeSouradnice(-- pXPos, --pYPos )){
            if(sachovnice.jeVolno(pXPos, pYPos) != iBarva){
                mozneTahy.add(new Point(pXPos, pYPos));
                if(sachovnice.jeVolno(pXPos, pYPos) != -1){
                    break;
                }
            }else
                break;
        }
        pXPos = pozice.x;
        pYPos = pozice.y;
        //Ověřování směru vlevoNahoru
        while(Sachovnice.existujeSouradnice(-- pXPos,++ pYPos )){
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
        return "Střelec:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
}
