
package sachy;

/**
 *Třída reprezentující šachového hráče
 * @author Lukáš
 */
public class SachHrac {
    
    private final String jmeno;
    private final boolean barva;
    private boolean naTahu;
    
    /**
     *Vytvoří instanic šachového hráče
     * @param jmeno Jméno hráče
     * @param barva Barva za kterou hráč hraje
     */
    public SachHrac(String jmeno, boolean barva) {
        this(jmeno, barva, false);
    }
    /**
     *Vytvoří instanic šachového hráče
     * @param jmeno Jméno hráče
     * @param barva Barva za kterou hráč hraje
     * @param naTahu Je-li hráč aktuálně na tahu
     */
    public SachHrac(String jmeno ,boolean barva, boolean naTahu){
        this.barva = barva;
        this.jmeno = jmeno;
        this.naTahu = naTahu;
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
            return getJmeno() + "(bílá)";
        return getJmeno() + "(černá)";
    }

    /**
     * @return True-pokud hráč táhne, false v opačném případě
     */
    public boolean isNaTahu() {
        return naTahu;
    }

    /**
     * @param naTahu nastaví, zda hráč táhne (true) nebo hráč netáhne (false)
     */
    public void setNaTahu(boolean naTahu) {
        this.naTahu = naTahu;
    }
    
    /**
     * Vrací textovou reprezentaci hráče (Pro uložení do souboru)
     * @return jmeno,barva(int),naTahu(int)
     */
    public String textReprezentaceHrace(){
        return getJmeno() + "," + Sachovnice.barvaNaInt(barva) + "," + Sachovnice.barvaNaInt(naTahu);
    }

    /**
     * @return the jmeno
     */
    public String getJmeno() {
        return jmeno;
    }
    
    
}
