
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
    }
    
    public Point getPozice(){
        return pozice;
    }
    public void setPozice(Point souradnice){
        pozice = souradnice;
    }
    public ArrayList<Point> getMozneTahy(){
        return mnozinaTahu;
    }
    
    public void setVyhozena(boolean bol){
        vyhozena = bol;
    }
    
    abstract void ziskejMozneTahy();
    
}
