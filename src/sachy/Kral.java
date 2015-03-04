

package sachy;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;



/**
 *Třída reprezenující šachového krále, klíčovou figuru celé šachové hry. Na šachovnice může
 * být vždy jen jeden král. Král nemá téměř žádný strategický význam. Může se
 * pohybovat o 1 políčko kterýmkoliv směrem, ale nesmí to pole být ohrožené.
 */
public class Kral extends Figura{
    
    private static BufferedImage whiteKing = null;
    private static BufferedImage blackKing = null;

    /**
     *Vytvoří šachového krále dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param sachovnice Šachovnice, na kterou se má figura přidat
     */
    public Kral(Point pozice, boolean barva,Sachovnice sachovnice) {
        super(pozice, barva, 0, sachovnice);
        if(whiteKing == null || blackKing == null){
            try {
                whiteKing = ImageIO.read(this.getClass().getResource("/chessFigures/whiteKing.png"));
                blackKing = ImageIO.read(this.getClass().getResource("/chessFigures/blackKing.png"));
            } catch (IOException ex) {
                System.err.println("Nenalezen obrázek krále, nemůže se vykreslit");
                System.err.println(ex.getMessage());
            }
        }
    }
    
    /**
     *Zjístí jestli je políčko na kterém král stojí ohroženo nepřátelskými figurami na šachovnici.
     * @return true - pokud je král ohrožen v opačném případě false
     */
    public boolean jeKralOhrozen(){
        return jeKralOhrozen(pozice);
    }
    
    /**
     *Zjístí jestli je políčko ohroženo nepřátelskými figurami figurami na šachovnici
     * @param souradnice souřadnice políčka
     * @return true - pokud je král ohrožen v opačném případě false
     */
    public boolean jeKralOhrozen(Point souradnice){
        int iBarva = Sachovnice.barvaNaInt(barva);
        int negIBarva = Sachovnice.barvaNaInt(!barva);
        Point zalSourad = (Point) souradnice.clone();                            //Zaloha pozice (kvůli manipulaci s Pointem)
        //Ověření, jestli krále neohrožuje Střelec, Věž nebo Dáma    
        for(SachoveSmery s : SachoveSmery.values()){
            zalSourad.setLocation(souradnice);
            int vzdalenost = 0;
            while(Sachovnice.existujeSouradnice(s.premistiPointVeSmeru(zalSourad,1))){
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
                        if(f instanceof Pesec && (((s == SachoveSmery.vlevoDolu || s == SachoveSmery.vlevoDolu) && !barva) || ((s == SachoveSmery.vlevoNahoru || s == SachoveSmery.vpravoNahoru)&& barva)))
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
        for(Point p : SachoveSmery.getPolePohybuKone(souradnice))
            if(Sachovnice.existujeSouradnice(p)){
                if(sachovnice.jeVolno(p) == negIBarva){                         //Pokud na místě stojí nepřítel
                    if(sachovnice.vyberFiguru(p) instanceof Kun)
                        return true;
                }
            }
        return false;
    }

    @Override
    public ArrayList<Point> getMozneTahy() {
        int iBarva = Sachovnice.barvaNaInt(barva);
        mozneTahy.clear();
        for(SachoveSmery s : SachoveSmery.values()){
            Point kPole = s.getPoleVeSmeru(pozice,1);
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
     *Zístí, jestli je král ohrožen a nemá kam jít
     * @return true - pokud je král umatován, false v opačném případě
     */
    public boolean jeMat(){
        getMozneTahy();
        if(mozneTahy.isEmpty()){
            if(jeKralOhrozen()){
                for(Figura ff[] : sachovnice.getRozmisteniFigur()){
                    for(Figura f: ff){
                        if(f != null){
                            if(f.barva == this.barva){
                                if(!f.getMozneTahyOpt().isEmpty())
                                    return false;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "Král:" + Sachovnice.pointNaSouradnice(pozice);
    }
    
    @Override
    public Image getImage() {
        if(barva)
            return whiteKing;
        return blackKing;
    }
    
}
