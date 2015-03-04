

package sachy;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *Třída reprezentující šachového střelce. Střelec se pohybuje po diagonálách a 
 * nemůže přeskakovat jiné figury
 * @author Lukáš Grgyga
 */
public class Strelec extends Figura{
    
    private static BufferedImage whiteBishop = null;
    private static BufferedImage blackBishop = null;
    /**
     *Vytvoří šachov´ho střelce dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Strelec(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 3, sachovnice);
        if(whiteBishop == null || blackBishop == null){
            try {
                whiteBishop = ImageIO.read(this.getClass().getResource("/chessFigures/whiteBishop.png"));
                blackBishop = ImageIO.read(this.getClass().getResource("/chessFigures/blackBishop.png"));
            } catch (IOException ex) {
                System.err.println("Nenalezen obrázek střelce, nemůže se vykreslit");
                System.err.println(ex.getMessage());
            }
        }
    }


    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(SachoveSmery s : SachoveSmery.values()){
            int vzdalenost = 1;
            if(!s.primySmer){
                while(Sachovnice.existujeSouradnice(s.getPoleVeSmeru(pozice, vzdalenost))){                 //Pokud souradnice o jedna do pravaNahoru existuje
                    Point pPoint = s.getPoleVeSmeru(pozice, vzdalenost);
                    if(sachovnice.jeVolno(pPoint) != iBarva){                     //Pokud na místě nestojí figura stejné barvy                                         
                        if(vzdalenost == 1 || sachovnice.getKral(barva).jeKralOhrozen()){  //Ověruje vazbu na krále pouze při táhnutí na vzálenost 1, nebo když je ohrožen vlastní král  
                            if(!sachovnice.simTahOverKrale(this, pPoint)){                        
                                mozneTahy.add(new Point(pPoint));
                            }else{                                              //Když se figura pohne z místa a je ohrožen král, nemá již cenu dále ověřovat
                                break;
                            }
                        }else{
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
        return "Střelec:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
    @Override
    public Image getImage() {
        if(barva)
            return whiteBishop;
        return blackBishop;
    }
    
}
