
package sachy;

import java.awt.Image;
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
    
    private boolean nedavnyTah = true;
    
    
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
     * @return 2 mala(pro černou figuru)/ velka(pro bílou figuru) pocatecni pismena nazvu figury
     */
    public String vratVykresli(){
        String s = toString().substring(0, 2);
        if(barva)
            return s.toUpperCase();
        return s.toLowerCase();
    }
    /**
     * Třeba zavolat na každé figuře po provedení tahu, či změně figur na šachovnici. Slouží k optimalizaci výkonu.
     */
    public void bylProvedenTah(){
        nedavnyTah = true;
    }
    
    /**
     * Tato metoda správně funguje pouze při oznamování všem figurám na šachovnici, že byl proveden tah. 
     * Oproti metodě getMozneTahy je výkonově optimalizována. 
     * Vrací voláním metody getMozneTahy() pouze v případě, že byl po posledním volání této metody proveden tah, v opačném
     * případě pouze vrací již uložné pole možných tahů.
     * @return Pole možných tahů figury.
     */
    public ArrayList<Point> getMozneTahyOpt(){
        if(nedavnyTah){
            nedavnyTah = false;
            return getMozneTahy();
        }
        return mozneTahy;
    }
    
    /**
     * Textová reprezentace figury.
     * Vrací ve formátu: NázevFigury,BarvaNaInt,PoziceX,PoziceY
     * @return např:kůň,1,5,5
     */
    public String textReprezentaceFigury(){
        String[] s = this.toString().split(":");
        return s[0] + "," + Sachovnice.barvaNaInt(barva) + "," + pozice.x + "," + pozice.y;
    }
    
    /**
     *Ověří všechny možnosti tahu figury na standartní šachovnici 8X8. Dále bere v potaz
     * ostatní figury na šachovnici. Mezi možnými tahy jsou také možné tahy na pole, kde stojí
     * nepřátelská figura(Vyhození figury). Dále bere v potaz možné ohrožení svého krále nepřítelem
     * při svém tahu, odsimuluje každý tah, aby se přesvědčila, že král nebude po jejím přesunu ohrožen.
     * @return Pole možných tahů figury.
     */
    public abstract ArrayList<Point> getMozneTahy();
    
    /**
     * Vrátí obrázek své figury
     * @return obrázek své figury
     */
    public abstract Image getImage();
    
    /**
     * Vytvoří konkrétní figuru na základě textové rezprezentacr Figury
     * @param s - Textová reprezentace figury. Je možno získat metodou textReprezentaceFigury()
     * @param sachovnice - šachovnice, na kterou se má figura umístit
     * @return konkrétní vytvořená figura
     */
    public static Figura vytvorKonkretniFigufu(String[] s, Sachovnice sachovnice){
        boolean barva;
        barva = 0 != Integer.parseInt(s[1]);
        switch(s[0]){
            case "Kůň" : 
                return new Kun(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);
            case "Střelec" :
                return new Strelec(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);
            case "Věž" :
                return new Vez(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);
            case "Pěšec" :
                return new Pesec(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);
            case "Dáma" :
                return new Dama(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);
            case "Král" :
                return new Kral(new Point(Integer.parseInt(s[2]),Integer.parseInt(s[3])),barva,sachovnice);   
        }
        return null;
    }
}
