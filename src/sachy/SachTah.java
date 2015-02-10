
package sachy;

import java.awt.Point;

/**
 *Reprezentuje šachové tahy. Každý tah má mimo jiné i číslo tahu v šachové hře.
 * @author Lukáš Gryga
 */
public class SachTah {
    private static int tah = 1;
    
    private final int cisloTahu;
    private final int cas;
    private final Figura figura;
    private final Point odKud;
    private final Point kam;
    private final boolean vyhozena;
    
    /**
     *Vytvoří instanci reprezentující šachový tah. Spíše jen takovou <code>Přepravku</code>
     * @param figura - figrura, kterou se táhlo
     * @param odKud - původní pozice figury
     * @param kam - nová pozice figury
     * @param cas - čas provedení tahu
     * @param vyhozenaFigura - true, pokud byla při tahu vyhozena nepřátelská figura
     */
    public SachTah(Figura figura, Point odKud, Point kam, int cas, boolean vyhozenaFigura){
        this.cas = cas;
        this.odKud = odKud;
        this.kam = kam;
        this.figura = figura;
        vyhozena = vyhozenaFigura;
        cisloTahu = tah++;
    }
    
    /**
     * Nastaví statickou proměnou tah, ta slouží k nastavování <code>císloTahu</code>
     * u jednotlivých instancí. Tzn. každá vytvořená instance inkrementuje <code>tah</code> o 1.
     * !!!Pozor, pokud se zmeni tah, nemusí hra správně fungovat!!!
     * @param cisloTahu číslo, které se má nastavit
     */
    public static void setTah(int cisloTahu){
        cisloTahu = tah;
    }
    
    /**
     * @return Aktuální číslo Tahu
     */
    public static int getTah(){
        return tah;
    }
    
    /**
     * @return true-pokud při tomto tahu byla vyhozena Figura
     */
    public boolean isVyhozena(){
        return vyhozena;
    }

    /**
     * @return cisloTahu
     */
    public int getCisloTahu() {
        return cisloTahu;
    }

    /**
     * @return cas kdy tah proběhl vzhledem k dané šahové hře
     */
    public int getCas() {
        return cas;
    }

    /**
     * @return Figura, kterou bylo taženo
     */
    public Figura getFigura() {
        return figura;
    }

    /**
     * @return odKud bylo taženo
     */
    public Point getOdKud() {
        return odKud;
    }

    /**
     * @return kam se táhlo
     */
    public Point getKam() {
        return kam;
    }
    
    /**
     * Třídá zaznamená tahZpět, takže počet tahu nastaví o 1 menší
     */
    public void tahZpet(){
        tah --;
    }
    

}
