
package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *Abstaktní třída zastupující figuru obecně. Nese všechny společné informace o figuře,
 * včetně polí, kde se může na šachovnici pohnout.
 * Z figury jsou děděny všechny konkrétnídruhy figur.
 * @author Lukáš Gryga
 */
public abstract class Figura 
{

    /**
     *Sachovnice na které se daná figura nachází.
     */
    protected final Sachovnice sachovnice;
    
    /**
     *Možné pole šachovnice na které se figura může pohnout, včetně polí, kde vyhodí soupeřovu figuru.
     * Pole se aktualizuje zavoláním metody <code>getMozneTahy()</code>
     */
    protected final ArrayList<Point> mozneTahy = new ArrayList<>();
    
    /**
     *Číselné vyjádření hodnoty figury při hře
     */
    public final int hodnotaFigury;
    
    /**
     *Pozice na které se figura nachízí na šachovnici. Může nabývat hotnod x: 1-8, y: 1-8;
     */
    protected Point pozice;

    /**
     *Barva dané figury. bílá = true, černá = false
     */
    protected final boolean barva;

    /**
     *false = figura je na šachovnici ve hře, true = figura byla vahozena
     */
    protected boolean vyhozena = false;
    
    
    /**
     *Vytvoří šachovou figuru dané barvy na zadaných souřadnicích. Pozor, figura se nepřidá
     * na šachovnici (<code>Sachovnice</code>). Přidat na šachovnici lze metodou šachovnice: <code>pridejFiguru(Figura)</code>
     * @param pozice Počáteční souřadnice figury
     * @param barva Barva figury (true = bílá, false = černá)
     * @param hodnotaFiugry číselné ohodnocení důležitosti figury pro hru
     * @param sachovnice šachovnice, na které je přidáma figura
     */
    public Figura(Point pozice, boolean barva, int hodnotaFiugry, Sachovnice sachovnice){
        this.pozice = pozice;
        this.barva = barva;
        this.hodnotaFigury = hodnotaFiugry;
        this.sachovnice = sachovnice;
    }
    
    /**
     *Vráti aktuální pozici figury. Pozor, nemusí odpovídat existujícím souřadnicím na šachovnici.
     * @return Aktuální pozice figury
     */
    public Point getPozice(){
        return pozice;
    }

    /**
     *Nastaví souřadnice figury na nově zadané.
     * @param souradnice Místo, kde se má figura přesunout.
     */
    public void setPozice(Point souradnice){
        pozice = souradnice;
    }
    
    /**
     *Nastaví figuře, zda-li je vyhozená či ve hře.
     * @param bol True, má-li být figura nastavená jako vyhozená. False, má-li být figura nastavena opačně.
     */
    public void setVyhozena(boolean bol){
        vyhozena = bol;
    }
    
   /**
    * Vrací true, pokud je figura vyhozena.
    * @return True, je-li figura vyhozena. V opačném případě false
    */
    public boolean isVyhozena(){
        return vyhozena;
    }
    
    /**
     *Ověří všechny možnosti tahu figury na standartní šachovnici 8X8. Dále bere v potaz
     * ostatní figury na šachovnici. Mezi možnými tahy jsou také možné tahy na pole, kde stojí
     * nepřátelská figura(Vyhození figury). Dále bere v potaz možné ohrožení svého krále nepřítelem
     * při svém tahu, odsimuluje každý tah, aby se přesvědčila, že král nebude po jejím přesunu ohrožen.
     * @return Pole možných tahů figury.
     */
    public abstract ArrayList<Point> getMozneTahy();
    
}
