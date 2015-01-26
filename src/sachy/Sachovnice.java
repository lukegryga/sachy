
package sachy;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *Třidá reprezenující stantartní šachovnici o velikosti 8x8 polí a adresací (A-H)X(1-8).
 * Šachovnice obsahuje metody pro vytvoření nové hry(Rozmístí figury v základním postavení),
 * pohyb jednotlivách figur a práci s nimi
 * Třída má také staticé metody, které slouží hlavě pro převody mezi šachovou adresací
 * a adresací typu Point.
 * 
 * @author Lukáš
 */
public class Sachovnice {
    
    private final ArrayList<Figura> vyhozeneFigury = new ArrayList<>();
    private final ChessKordinator ChK = ChessKordinator.getChessKordinator();
    
    private final Figura[][] rozmisteni = new Figura[8][8];
    private Kral[] kral = new Kral[2];                                            //Králové
    
    public Sachovnice(){
    }
    
    public void testMethod(){
        pridejFiguru(new Kral(new Point(2,2), true, this));
        pridejFiguru(new Kun(new Point(1,5), false, this));
    }
    
    /**
     *
     */
    public void novaHra(){
        boolean pp = false;
        for(int i=1; i<=8; i *= 8){                                             //Cyklus projede dvakrát
            pp = !pp;                                                           //Pri prvním projetí je pp true, pri druhem false
            pridejFiguru(new Vez(new Point(1,i*1), pp, this));
            pridejFiguru(new Kun(new Point(2,i*1), pp, this));
            pridejFiguru(new Strelec(new Point(3,i*1), pp, this));
            pridejFiguru(new Dama(new Point(4,i*1), pp, this));
            pridejFiguru(new Kral(new Point(5,i*1), pp, this));
            pridejFiguru(new Strelec(new Point(6,i*1), pp, this));
            pridejFiguru(new Kun(new Point(7,i*1), pp, this));
            pridejFiguru(new Vez(new Point(8,i*1), pp, this));    
        }
        for(int i=0; i<8; i++){
            pridejFiguru(new Pesec(new Point(i+1, 2), true, this));              //Rozestavení bílých pěšců
            pridejFiguru(new Pesec(new Point(i+1, 7), false, this));             //Rozestavení černých pěšců
        }
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public Figura vyberFiguru(String souradnice){
        Point pSourad = Sachovnice.souradniceNaPoint(souradnice);
        return vyberFiguru(pSourad);
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public Figura vyberFiguru(Point souradnice){
        
        if(!existujeSouradnice(souradnice))
            return null;
        return rozmisteni[souradnice.x-1][souradnice.y-1];
    }
    
    /**
     *
     * @param figura
     * @param souradnice
     * @return
     */
    public boolean tahni(Figura figura, String souradnice){
        boolean vyhozeni = false;
        Point kam = souradniceNaPoint(souradnice);
        for(Point policko : figura.getMozneTahy()){                             //Overi, zdali je tah mozny
            if(policko.equals(kam)){                                            //Tak je mozny
                if(vyberFiguru(kam) != null){                             //Pokud je na policku, kde se táhne, nepřátelská figura
                    vyhodFiguru(vyberFiguru(kam));
                    vyhozeni = true;
                }
                Point zalSouradnic = figura.getPozice();
                rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;
                rozmisteni[kam.x-1][kam.y-1] = figura;
                figura.setPozice(kam);
                if(getKral(figura.barva).jeKralOhrozen()){
                    rozmisteni[zalSouradnic.x-1][zalSouradnic.y-1] = figura;
                    if(vyhozeni)
                        vratPosledniVyhozFiguru();
                    else
                        rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;
                    figura.setPozice(zalSouradnic);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param pozice
     * @return
     */
    public int jeVolno(Point pozice){
        if(!existujeSouradnice(pozice)){
            throw new IllegalArgumentException("Neexistující souřadnice");
        }
        if(rozmisteni[pozice.x-1][pozice.y-1] == null){
            return -1;
        }else if(rozmisteni[pozice.x-1][pozice.y-1].barva){
            return 1;
        }
        return 0;
    }
    
    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int jeVolno(int x, int y){
        if(!existujeSouradnice(x, y)){
            throw new IllegalArgumentException("Neexistující souřadnice");
        }
        if(rozmisteni[x-1][y-1] == null){
            return -1;
        }else if(rozmisteni[x-1][y-1].barva){
            return 1;
        }
        return 0;
    }
    
    /**
     *
     * @param f
     * @return
     */
    public boolean pridejFiguru(Figura f){
        if(!Sachovnice.existujeSouradnice(f.getPozice()))
            return false;
        if(f instanceof Kral){
            if((f.barva && getKral(true) != null) || (!f.barva && getKral(false) != null))
                return false;
            kral[barvaNaInt(f.barva)] = (Kral) f;
        }
        rozmisteni[f.getPozice().x -1][f.getPozice().y -1 ] = f;
        return true;
    }
    
    /**
     *
     * @param barva
     * @return
     */
    public Kral getKral(boolean barva){
        return kral[barvaNaInt(barva)];
    }

    /**
     *
     * @param tah
     * @return
     */
    public static Point souradniceNaPoint(String tah){
        tah = tah.toLowerCase();
        char[] pozice = tah.toCharArray();
        if(pozice.length != 2){
            throw new IllegalArgumentException("Neplanty řetězec");
        }
        int x = pozice[0] - 96;
        int y = pozice[1] - '0';
        if(!existujeSouradnice(x, y)){
            throw new IllegalArgumentException("Neexistující souřadnice");
        }
        return new Point(x, y);
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public static String pointNaSouradnice(Point souradnice){
        if(!existujeSouradnice(souradnice)){
            throw new IllegalArgumentException("Neexistující souřadnice");
        }
        char posX = (char) (souradnice.x + 96);
        String posY = Integer.toString(souradnice.y);
        
        return String.join("", String.valueOf(posX).toUpperCase(), posY);
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public static boolean existujeSouradnice(Point souradnice){
        return !((souradnice.x >= 9 || souradnice.x <= 0) || (souradnice.y >= 9 || souradnice.y <= 0));
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean existujeSouradnice(int x, int y){
        return !((x >= 9 || x <= 0) || (y >= 9 || y <= 0));
    }
    
    /**
     *
     * @param barva
     * @return
     */
    public static int barvaNaInt(boolean barva){
        if(barva)
            return 1;
        return 0;
    }
    
    private void vyhodFiguru(Figura figura){
        figura.setVyhozena(true);
        vyhozeneFigury.add(figura);
    }
    
    private void vratPosledniVyhozFiguru(){
        pridejFiguru(vyhozeneFigury.get(vyhozeneFigury.size()));
        vyhozeneFigury.remove(vyhozeneFigury.size());
    }
    
}
