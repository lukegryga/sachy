
package sachy;

import java.util.Timer;
import java.util.TimerTask;

/**
 *Třída reprezentující šachové hodiny. Třída má budík 1 a budík 2, které mezi sebou přepíná a postupně odečítává čas.
 * @author Lukáš Gryga
 */
public class SachHodiny {
    
    private Budik budik1;
    private Budik budik0;
    
    /**
     * Vytvoří instanci Šachových hodin. Obou budíkům nastaví stejný výchozí čas
     * @param StartCas výchozí čas budíků
     */
    public SachHodiny(int StartCas){
        this(StartCas, StartCas);
    }
    
    /**
     *Vytvoří instanci šachových hodin. Každému budíku nastaví výchozí čas
     * @param cas1 čas budíku 1
     * @param cas0 čas budíku 0
     */
    public SachHodiny(int cas1, int cas0){
        budik1 = new Budik(cas1);
        budik0 = new Budik(cas0);
        Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
            }
                
            }, 500, 500);
    }
    
    /**
     *Zapne odpočítávání konkrétního budíku na budíku.
     * @param budik true - zapne odpočítávání na budíku 1, v opačném případě začne odpočítávat budík 0
     */
    public void start(boolean budik){
        if(budik)
            budik1.setAktivni(true);
        else
            budik0.setAktivni(true);
    }
    
    /**
     *Zastaví odpočet
     */
    public void stop(){
        budik1.setAktivni(false);
        budik0.setAktivni(false);
    }
    
    /**
     *Zastaví odpočet na budíku kde odpočet probíhá a zapne odpočet na budíku, kde odpočet neprobíhá
     */
    public void prepni(){
        budik1.aktivni = !budik1.aktivni;
        budik0.aktivni = !budik0.aktivni;
    }
    
    
    /**
     *Kolik minut zbývá na konkrétním budíku
     * @param budik true - zbývající minuty budíku 1 false - zbývající minuty budíku 0
     * @return minuty zbývající do konce odpočtu budíku
     */
    public int getMinutes(boolean budik){
        if(budik)
            return budik1.cas/60;
        return budik0.cas/60;
    }
    
    /**
     *Kolik sekund zbývá na konkrétním budíku. Tato funkce vrací pouze zbytek po dělení 60 celkového času budíku. Tzn. maximální hodnota je 59
     * @param budik true - zbývající sekundy budíku 1 false - zbývající sekundy budíku 0
     * @return Tato funkce vrací pouze zbytek po dělení 60 celkového času budíku. Tzn. maximální hodnota je 59
     */
    public int getSec(boolean budik){
        if(budik)
            return budik1.cas%60;
        return budik0.cas%60;
    }
    /**
     * Tato funkce frací počet sekund do konce odpočtu
     * @param budik true - zbývající sekundy budíku 1 false - zbývající sekundy budíku 0
     * @return Počet sekund do konce odpočtu
     */
    public int getCas(boolean budik){
        if(budik)
            return budik1.cas;
        return budik0.cas;
    }
    /**
     * Nastaví čas budíků.
     * @param cas 
     */
    public void setCas(int cas){
        setCas(true, cas);
        setCas(false, cas);
    }
    /**
     * Nastaví čas konkrétního budíku
     * @param budik true - nastaví budík 1, false - nastaví budík0
     * @param cas čas který má být nastaven
     */
    public void setCas(boolean budik,int cas){
        if(budik)
            budik1.cas = cas;
        else
            budik0.cas = cas;
    }
    
    
    class Budik{
        int cas;
        boolean aktivni;
        
        private final Timer casovac = new Timer();
        
        Budik(int cas){
            this.cas = cas;
            casovac.scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run() {
                        if(aktivni)
                            odecti();
                    }
                }, 1000, 1000);
        }
        
        boolean isAktivni(){
            return aktivni;
        }
        
        void setAktivni(boolean zapni){
            aktivni = zapni;
        }
        
        public void odecti(){
            cas--;
        }
    }
    
}
