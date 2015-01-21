
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
    
    public Figura[][] rozmisteni = new Figura[8][8];
    private boolean hra = false;
    private boolean jeNaTahu = true;
    
    public static Sachovnice getSachovnice(){
        if(jedinacek == null){
            jedinacek = new Sachovnice();
        }
        return jedinacek;
    }
    
    private Sachovnice(){}
    
    public void testMethod(){
        rozmisteni[5][5] = new Kral(new Point(6,6),false);
        rozmisteni[4][3] = new Kun(new Point(5,4), true);
        //rozmisteni[1][1] = new Pesec(new Point(2,2), false);
        //rozmisteni[2][2] = new Strelec(new Point(3,3), true);
        rozmisteni[0][2] = new Vez(new Point(1,3), true);
        //rozmisteni[0][1] = new Pesec(new Point(1,2), false);
    }
    
    public Figura vyberFiguru(String souradnice){
        Point pSourad = Sachovnice.souradniceNaPoint(souradnice);
        return rozmisteni[pSourad.x-1][pSourad.y-1];
    }
    
    public Figura vyberFiguru(Point souradnice){
        return rozmisteni[souradnice.x-1][souradnice.y-1];
    }
    
    public boolean tahni(Figura figura, String souradnice){
        Point kam = souradniceNaPoint(souradnice);
        for(Point policko : figura.getMozneTahy()){                             //Overi, zdali je tah mozny
            if(policko.equals(kam)){                                            //Tak je mozny
                if(getFiguraNaPozici(kam) != null){                             //Pokud je na policku, kde se táhne, nepřátelská figura
                    vyhodFiguru(getFiguraNaPozici(kam));
                }
                rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;  
                rozmisteni[kam.x-1][kam.y-1] = figura;
                figura.setPozice(kam);
                jeNaTahu = !jeNaTahu;                                           //Přepne tah na druhého hráče
                return true;
            }
        }
        return false;
    }
    
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
    
    public static String pointNaSouradnice(Point souradnice){
        if(!existujeSouradnice(souradnice)){
            throw new IllegalArgumentException("Neexistující souřadnice");
        }
        char posX = (char) (souradnice.x + 96);
        String posY = Integer.toString(souradnice.y);
        
        return String.join("", String.valueOf(posX).toUpperCase(), posY);
    }
    
    public static boolean existujeSouradnice(Point souradnice){
        return !((souradnice.x >= 9 || souradnice.x <= 0) || (souradnice.y >= 9 || souradnice.y <= 0));
    }
    public static boolean existujeSouradnice(int x, int y){
        return !((x >= 9 || x <= 0) || (y >= 9 || y <= 0));
    }
    
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
    
}
