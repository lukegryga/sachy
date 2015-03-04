
package sachy;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *Třída reprezentující šachovou dámu (Nejhodnotnější figuru). Dáma se může pohybovat
 * všemi osmi směry na jakoukoliv vzdálenost, ale nepřeskakovat přes jiné figury.
 * @author Lukáš Grgyga
 */
public class Dama extends Figura{
    
    private static BufferedImage whiteQueen = null;
    private static BufferedImage blackQueen = null;
    
    /**
     *Vytvoří šachovou dámu dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Dama(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 9, sachovnice);
        if(whiteQueen == null || blackQueen == null){
            try {
                whiteQueen = ImageIO.read(this.getClass().getResource("/chessFigures/whiteQueen.png"));
                blackQueen = ImageIO.read(this.getClass().getResource("/chessFigures/blackQueen.png"));
            } catch (IOException ex) {
                System.err.println("Nenalezen obrázek dámy, nemůže se vykreslit");
                System.err.println(ex.getMessage());
            }
        }
    }
    
    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(SachoveSmery s : SachoveSmery.values()){
            int vzdalenost = 1;                                                                 //Vdzálenost, která se bude postpně zvyšovat (Postup ve směru) 
            while(Sachovnice.existujeSouradnice(s.getPoleVeSmeru(pozice, vzdalenost))){         //Pokud souradnice o jedna do prava existuje
                Point pPoint = s.getPoleVeSmeru(pozice, vzdalenost);
                if(sachovnice.jeVolno(pPoint) != iBarva){                                       //Pokud na místě nestojí figura stejné barvy                                           
                    if(vzdalenost == 1 || sachovnice.getKral(barva).jeKralOhrozen()){           //Ověruje vazbu na krále pouze při táhnutí na vzálenost 1, nebo když je ohrožen vlastní král
                        if(!sachovnice.simTahOverKrale(this, pPoint)){                          
                            mozneTahy.add(new Point(pPoint));
                        }else{                                                                  //Když se figura pohne z místa a je ohrožen král, nemá již cenu dále ověřovat
                            break;
                        }
                    }else{
                        mozneTahy.add(new Point(pPoint));
                    }
                    if(sachovnice.jeVolno(pPoint) != -1){                                       //Pokud na míste stojí figura cizí barvy (Není tam przdno)
                        break;
                    }
                }else
                    break;
                vzdalenost++;
            }
        }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Dáma:" + Sachovnice.pointNaSouradnice(pozice);
    }

    @Override
    public Image getImage() {
        if(barva)
            return whiteQueen;
        return blackQueen;
    }
    
}
