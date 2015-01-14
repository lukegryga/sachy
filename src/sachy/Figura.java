
package sachy;

import java.awt.Point;

public abstract class Figura 
{
    protected final Sachovnice sachovnice = Sachovnice.getSachovnice();
    protected final boolean barva;
    protected Point pozice;
    
    public Figura(Point pozice, boolean barva){
        this.pozice = pozice;
        this.barva = barva;
        
    }
    
    public Point getPozice(){
        return pozice;
    }
    
}
