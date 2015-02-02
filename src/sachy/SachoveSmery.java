
package sachy;

import java.awt.Point;

/**
 *Třída zastupující 8 směrů, kterými se můžou pohybovat šachové figury. Dále je možno z
 * jakékoliv instance získat pole tahů pro koně.
 * @author Lukáš Gryga
 */
public enum SachoveSmery {

    /**
     *
     */
    nahoru(0,1, true),

    /**
     *
     */
    vpravoNahoru(1,1, false),

    /**
     *
     */
    vpravo(1,0, true),

    /**
     *
     */
    vpravoDolu(1,-1, false),

    /**
     *
     */
    dolu(0,-1, true),

    /**
     *
     */
    vlevoDolu(-1,-1, false),

    /**
     *
     */
    vlevo(-1,0, true),

    /**
     *
     */
    vlevoNahoru(-1,1, false);
    
    /**
     *posun X-ové souřadnice o 1 v daném směru
     */
    public final int posX;

    /**
     *posun X-ové souřadnice o 1 v daném směru
     */
    public final int posY;
    
    private static final Point[] kun = {new Point(1,2), new Point(2,1), new Point(2,-1), new Point(1,-2),
        new Point(-1,-2), new Point(-2,-1), new Point(-2,1), new Point(-1,2)}; 
    
    /**
     *Přímý směr je vodorovný nebo svislý směr, nikoliv směr po diagonále
     * False - když je směr po diagonále, true v opačném případě
     */
    public final boolean primySmer;
    
    SachoveSmery(int posX, int posY, boolean primySmer){
        this.posX = posX;
        this.posY = posY;
        this.primySmer = primySmer;
    }
    
    /**
     *Získá Point ve směru ze vzdálenosti: <code>vzdalenost</code>
     * @param odKud pole, od kterého se bude počítat získaný Point
     * @param vzdalenost kolikáté poolíčko v daném směru vrátit
     * @return nový Point s posunutýma souřadnicema
     */
    public Point getPoleVeSmeru(Point odKud, int vzdalenost){
        Point poleVeSmeru = (Point) odKud.clone();
        poleVeSmeru.translate(vzdalenost*posX, vzdalenost*posY);
        return poleVeSmeru;
    }
    
    /**
     *Změní souřadnice Pointu: <code>presouvanaSouradnice</code> na souřadnice v
     * určitém směru z určité vzdálenosti.
     * @param presouvanaSouradnice Point, jehož souřadnice se mají změnit
     * @param vzdalenost kolikáte políčko v daném směru vrátit
     * @return <code>presouvanaSouradnice</code> s pozměněnýma souřadnica
     */
    public Point premistiPointVeSmeru(Point presouvanaSouradnice, int vzdalenost){
        presouvanaSouradnice.translate(vzdalenost*posX, vzdalenost*posY);
        return presouvanaSouradnice;
    }
    
    /**
     *Vrací pole možných tahů koně.
     * @param poziceKone Souřadnice od kterých se budou počítat možnosti tahu koně.
     * @return 8 možných polí, na které může kůň (Směr ve tvaru L)
     */
    public static Point[] getPolePohybuKone(Point poziceKone){
        Point[] tahyKone = new Point[8];
        for(int i = 0; i<8;i++){
            tahyKone[i] = new Point(poziceKone.x + kun[i].x, poziceKone.y + kun[i].y);
        }
        return tahyKone;
    }
    
}
