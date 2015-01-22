
package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Lukáš
 */
public class Sachovnice {
    private static Sachovnice jedinacek = null;
    
    private final ArrayList<Figura> vyhozeneFigury = new ArrayList<>();
    private final ChessKordinator ChK = ChessKordinator.getChessKordinator();
    
    private final Figura[][] rozmisteni = new Figura[8][8];
    private Kral k1;                                                            //Bily Kral
    private Kral k0;                                                            //Cerny Kral
    
    /**
     *
     * @return
     */
    public static Sachovnice getSachovnice(){
        if(jedinacek == null){
            jedinacek = new Sachovnice();
        }
        return jedinacek;
    }
    
    private Sachovnice(){}
    
    /**
     *
     */
    public void novaHra(){
        boolean pp = false;
        for(int i=1; i<=8; i *= 8){                                             //Cyklus projede dvakrát
            pp = !pp;                                                               //Pri prvním projetí je pp true, pri druhem false
            pridejFiguru(new Vez(new Point(1,i*1), pp));
            pridejFiguru(new Kun(new Point(2,i*1), pp));
            pridejFiguru(new Strelec(new Point(3,i*1), pp));
            pridejFiguru(new Dama(new Point(4,i*1), pp));
            pridejFiguru(new Kral(new Point(5,i*1), pp));
            pridejFiguru(new Strelec(new Point(6,i*1), pp));
            pridejFiguru(new Kun(new Point(7,i*1), pp));
            pridejFiguru(new Vez(new Point(8,i*1), pp));    
        }
        for(int i=0; i<8; i++){
            pridejFiguru(new Pesec(new Point(i+1, 2), true));              //Rozestavení bílých pěšců
            pridejFiguru(new Pesec(new Point(i+1, 7), false));             //Rozestavení černých pěšců
        }
        k1 = (Kral) rozmisteni[4][0];
        k0 = (Kral) rozmisteni[4][7];
    }
    
    /**
     *
     * @param souradnice
     * @return
     */
    public Figura vyberFiguru(String souradnice){
        Point pSourad = Sachovnice.souradniceNaPoint(souradnice);
        return rozmisteni[pSourad.x-1][pSourad.y-1];
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
                if(getFiguraNaPozici(kam) != null){                             //Pokud je na policku, kde se táhne, nepřátelská figura
                    vyhodFiguru(getFiguraNaPozici(kam));
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
        rozmisteni[f.getPozice().x -1][f.getPozice().y -1 ] = f;
        return true;
    }
    
    /**
     *
     * @param barva
     * @return
     */
    public Kral getKral(boolean barva){
        if(barva)
            return k1;
        return k0;
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
    
    private Figura getFiguraNaPozici(Point pozice){
        return rozmisteni[pozice.x-1][pozice.y-1];
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
