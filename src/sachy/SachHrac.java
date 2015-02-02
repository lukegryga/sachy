
package sachy;

/**
 *Třída reprezentující šachového hráče
 * @author Lukáš
 */
public class SachHrac {
    
    private final String jmeno;
    private final boolean barva;
    
    /**
     *Vytvoří instanic šachového hráče
     * @param jmeno Jméno hráče
     * @param barva Barva za kterou hráč hraje
     */
    public SachHrac(String jmeno, boolean barva) {
        this.barva = barva;
        this.jmeno = jmeno;
    }

    /**
     * @return the jmeno
     */
    public String getJmeno() {
        return jmeno;
    }

    /**
     * @return the barva
     */
    public boolean isBarva() {
        return barva;
    }
    
    /**
     * př.Petr(bílá)
     * @return Jméno hráče + barva
     */
    @Override
    public String toString(){
        if(barva)
            return jmeno + "(bílá)";
        return jmeno + "(černá)";
    }
    
    
}
