

package sachy;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *Třída reprezentující šachového koně. Kůň se může pohybovat ve směru tvaru L a
 * přeskakovat jiné figury
 * @author Lukáš Grgyga
 */
public class Kun extends Figura{
    
    private static BufferedImage whiteHorse = null;
    private static BufferedImage blackHorse = null;


    /**
     *Vytvoří šashového koně dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Kun(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 3, sachovnice);
        if(whiteHorse == null || blackHorse == null){
            try {
                whiteHorse = ImageIO.read(this.getClass().getResource("/chessFigures/whiteHorse.png"));
                blackHorse = ImageIO.read(this.getClass().getResource("/chessFigures/blackHorse.png"));
            } catch (IOException ex) {
                System.err.println("Nenalezen obrázek koně, nemůže se vykreslit");
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(Point pPoint : SachoveSmery.getPolePohybuKone(pozice))
            if(Sachovnice.existujeSouradnice(pPoint)){
                if(sachovnice.jeVolno(pPoint) != iBarva){                            //Pokud na místě nestojí figura stejné barvy                                          
                    if(!sachovnice.simTahOverKrale(this, pPoint)){                        
                        mozneTahy.add(new Point(pPoint));
                    }
                }
            }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Kůň:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
    @Override
    public Image getImage() {
        if(barva)
            return whiteHorse;
        return blackHorse;

    }
    
}
