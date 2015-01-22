

package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Lukáš
 */
public class Kral extends Figura{
    
    /**
     *
     */
    public static final int hodnotaFigury = 0;

    /**
     *
     * @param pozice
     * @param barva
     */
    public Kral(Point pozice, boolean barva) {
        super(pozice, barva);
    }
    
    /**
     *
     * @return
     */
    public boolean jeKralOhrozen(){
        return jeKralOhrozen(pozice);
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public boolean jeKralOhrozen(Point souradnice){
        int iBarva = Sachovnice.barvaNaInt(barva);
        int negIBarva = Sachovnice.barvaNaInt(!barva);
        Point zalSourad = (Point) souradnice.clone();                            //Zaloha pozice (kvůli manipulaci s Pointem
        //Ověření, jestli krále neohrožuje Střelec, Věž nebo Dáma    
        for(SachoveSmery s : SachoveSmery.values()){
            zalSourad.setLocation(souradnice);
            int vzdalenost = 0;
            while(Sachovnice.existujeSouradnice(s.premistiPointVeSmeru(zalSourad))){
                vzdalenost++;
                if(sachovnice.jeVolno(zalSourad) == iBarva){
                    //Aby král nezavazel sám sobě
                    if(!sachovnice.vyberFiguru(zalSourad).equals(this))
                        break;
                }else if(sachovnice.jeVolno(zalSourad) == -1){
                }else{
                    Figura f = sachovnice.vyberFiguru(zalSourad);
                    //Ověření nepřátelského pěšce a krále
                    if(vzdalenost == 1){
                        if(f instanceof Kral)
                            return true;
                        if(f instanceof Pesec && (s == SachoveSmery.vlevoDolu || s == SachoveSmery.vpravoDolu || s == SachoveSmery.vpravoDolu || s == SachoveSmery.vpravoNahoru))
                            return true;
                    }
                    if(f instanceof Dama)
                        return true;
                    if(s.primySmer && f instanceof Vez)
                        return true;
                    else if(!s.primySmer && f instanceof Strelec)
                        return true;
                    else break;
                }
            }
        }
        //Ověření jestli krále neohrožuje kůň
        zalSourad.setLocation(souradnice.x + 1, souradnice.y + 2);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x + 2, souradnice.y + 1);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x + 2, souradnice.y - 1);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x + 1 , souradnice.y - 2);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x - 1 , souradnice.y - 2);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x - 2 , souradnice.y - 1);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x - 2 , souradnice.y + 1);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        zalSourad.setLocation(souradnice.x - 1 , souradnice.y + 2);
        if(Sachovnice.existujeSouradnice(zalSourad)){
            if(sachovnice.jeVolno(zalSourad) == negIBarva){       //Pokud na místě stojí nepřítel
                if(sachovnice.vyberFiguru(zalSourad) instanceof Kun)
                    return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(SachoveSmery s : SachoveSmery.values()){
            Point kPole = s.getPoleVeSmeru(pozice);
            if(Sachovnice.existujeSouradnice(kPole)){
                if(!jeKralOhrozen(kPole)){
                    if(sachovnice.jeVolno(kPole) != iBarva)
                        mozneTahy.add(kPole);
                }
            }
        }
        return mozneTahy;
    }
    
    /**
     *
     * @return
     */
    public boolean jeMat(){
        getMozneTahy();
        if(mozneTahy.isEmpty()){
            if(jeKralOhrozen())
                return true;
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "Král:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
}
