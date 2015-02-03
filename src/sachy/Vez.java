
package sachy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *Třída reprezentující šachovou věž. Vež se pohybuje vodorovně něbo svisle,
 * nemůže přeskakovat jiní figury s výjímkou rošády, kdy se zamění s králem.
 * @author Lukáš Grgyga
 */
public class Vez extends Figura {

    /**
     *Vytvoří šachovou věž dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Vez(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 5, sachovnice);
    }

    
    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(SachoveSmery s : SachoveSmery.values()){
            int vzdalenost = 1;
            if(s.primySmer){
                while(Sachovnice.existujeSouradnice(s.getPoleVeSmeru(pozice, vzdalenost))){                 //Pokud souradnice o jedna do pravaNahoru existuje
                    Point pPoint = s.getPoleVeSmeru(pozice, vzdalenost);
                    if(sachovnice.jeVolno(pPoint) != iBarva){                     //Pokud na místě nestojí figura stejné barvy                                           
                        if(!sachovnice.simTahOverKrale(this, pPoint)){                        
                            mozneTahy.add(new Point(pPoint));
                        }
                        if(sachovnice.jeVolno(pPoint) != -1){                     //Pokud na míste stojí figura cizí barvy (Není tam przdno)
                            break;
                        }
                    }else
                        break;
                    vzdalenost ++;
                }
            }
        }
        
        return mozneTahy;
    }

    
    @Override
    public String toString(){
        return "Věž:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
    @Override
    public Image getImage() {
        try {
            if(barva)
                return ImageIO.read(new File("src\\chessFigures\\whiteRook.png"));
            else
                return ImageIO.read(new File("src\\chessFigures\\blackRook.png"));
        } catch (IOException ex) {
            System.err.println("Nenalezen obrázek věže, nemůže se vykreslit");
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
