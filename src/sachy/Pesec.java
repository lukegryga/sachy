
package sachy;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *Třída reprezentující šachového pěšce. Pěšec se může pohybovat přímo před sebe,
 * ale brát figury jen do šikma. Pohybovat se vždy o jedno políčko. Když dojede
 * na druhý konec šachovnice, může se změnit v kteroukoliv Figuru
 * @author Lukáš Grgyga
 */
public class Pesec extends Figura{
    
    private static BufferedImage whitePawn = null;
    private static BufferedImage blackPawn = null;

    /**
     *Vytvoří šachového pěšce dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Pesec(Point pozice, boolean barva, Sachovnice sachovnice) {
        super(pozice, barva, 1, sachovnice);
        if(whitePawn == null || blackPawn == null){
            try {
                whitePawn = ImageIO.read(this.getClass().getResource("/chessFigures/whitePawn.png"));
                blackPawn = ImageIO.read(this.getClass().getResource("/chessFigures/blackPawn.png"));
            } catch (IOException ex) {
                System.err.println("Nenalezen obrázek pěšce, nemůže se vykreslit");
                System.err.println(ex.getMessage());
            }
        }
    }
    
    @Override
    public ArrayList<Point> getMozneTahy(){
        mozneTahy.clear();
        if(barva == true){                                                      //Bílý pěšec
            if(Sachovnice.existujeSouradnice(pozice.x, pozice.y+1)){            //Pokud je na sachovnice souradnice o 1 nahoru
                if(sachovnice.jeVolno(pozice.x, pozice.y+1) == -1){             //Pokud na souradnici o 1 nahoru nic nestojí
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x, pozice.y+1)))
                        mozneTahy.add(new Point(pozice.x, pozice.y+1));
                    if(pozice.y == 2 && sachovnice.jeVolno(pozice.x, pozice.y+2) == -1){    //Pokud se pěšec ještě nepohnul a je o 2 dopředu volno
                        if(!sachovnice.simTahOverKrale(this, new Point(pozice.x, pozice.y+2)))
                            mozneTahy.add(new Point(pozice.x, pozice.y+2));
                    }
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x-1, pozice.y+1)){          //Pokud existuje pole vlevo nahoru
                if(sachovnice.jeVolno(pozice.x-1, pozice.y+1) == 0){            //Pokud vlevo nahoře je soupeřova figura
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x-1, pozice.y+1)))
                        mozneTahy.add(new Point(pozice.x-1, pozice.y+1));
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x+1, pozice.y+1)){          //Pokud existuje pole vpravo nahoru
                if(sachovnice.jeVolno(pozice.x+1, pozice.y+1) == 0){            //Pokud v pravo nahoře je soupeřova figura
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x+1, pozice.y+1)))
                        mozneTahy.add(new Point(pozice.x+1, pozice.y+1));
                }
            }
        }else{                                                                  //Černý pěšec
            if(Sachovnice.existujeSouradnice(pozice.x, pozice.y-1)){            //Pokud je na sachovnice souradnice o 1 dolu
                if(sachovnice.jeVolno(pozice.x, pozice.y-1) == -1){             //Pokud na souradnici o 1 nahoru nic nestojí
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x, pozice.y-1)))
                        mozneTahy.add(new Point(pozice.x, pozice.y-1));
                    if(pozice.y == 7 && sachovnice.jeVolno(pozice.x, pozice.y-2) == -1){    //Pokud se pěšec ještě nepohnul a je o 2 dolu volno
                        if(!sachovnice.simTahOverKrale(this, new Point(pozice.x, pozice.y-2)))
                            mozneTahy.add(new Point(pozice.x, pozice.y-2));
                    }
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x-1, pozice.y-1)){          //Pokud existuje pole vlevo dolu
                if(sachovnice.jeVolno(pozice.x-1, pozice.y-1) == 1){            //Pokud vlevo dole je soupeřova figura
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x-1, pozice.y-1)))
                        mozneTahy.add(new Point(pozice.x-1, pozice.y-1));
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x+1, pozice.y-1)){          //Pokud existuje pole vpravo nahoru
                if(sachovnice.jeVolno(pozice.x+1, pozice.y-1) == 1){            //Pokud v pravo dole je soupeřova figura
                    if(!sachovnice.simTahOverKrale(this, new Point(pozice.x+1, pozice.y-1)))
                        mozneTahy.add(new Point(pozice.x+1, pozice.y-1));
                }
            }
        }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Pěšec:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
    @Override
    public Image getImage() {
        if(barva)
            return whitePawn;
        return blackPawn;
    }
}
