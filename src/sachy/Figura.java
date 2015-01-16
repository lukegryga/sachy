
package sachy;

import java.awt.Point;
import java.util.ArrayList;

public abstract class Figura 
{
    protected final Sachovnice sachovnice = Sachovnice.getSachovnice();
    protected final boolean barva;
    protected Point pozice;
    protected boolean vyhozena = false;
    protected ArrayList<Point> mnozinaTahu = new ArrayList<>();
    
    public Figura(Point pozice, boolean barva){
        this.pozice = pozice;
        this.barva = barva;
        //Toto je komentář
    }
    
    public Point getPozice(){
        return pozice;
    }
    
}
