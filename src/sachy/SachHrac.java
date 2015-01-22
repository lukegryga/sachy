
package sachy;


public class SachHrac {
    
    private final String jmeno;
    private final boolean barva;
    
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
    
    
    
}
