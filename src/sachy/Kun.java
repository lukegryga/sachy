

package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *Třída reprezentující šachového koně. Kůň se může pohybovat ve směru tvaru L a
 * přeskakovat jiné figury
 * @author Lukáš Grgyga
 */
public class Kun extends Figura{


    /**
     *Vytvoří šashového koně dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Kun(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 3, sachovnice);
    }

    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(Point p : SachoveSmery.getPolePohybuKone(pozice))
            if(Sachovnice.existujeSouradnice(p)){
                if(sachovnice.jeVolno(p) != iBarva){                            //Pokud na místě nestojí figura stejné barvy
                    mozneTahy.add(new Point(p));
                }
            }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Kůň:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
}
