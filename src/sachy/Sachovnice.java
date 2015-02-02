
package sachy;

import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;

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
    
    private final Deque<Figura> vyhozeneFigury = new LinkedList<>();
    private final ChessKordinator ChK = ChessKordinator.getChessKordinator();
    
    private final Figura[][] rozmisteni = new Figura[8][8];
    private final Kral[] kral = new Kral[2];                                            //Králové
    private final Deque<SachTah> tahy = new LinkedList<>();
    
    /**
     *Vytvorí instanci šachovnice. Šachovnic může být i více a na každé může probíhat jiná hra
     */
    public Sachovnice(){
    }
    
    /**
     *Připraví figury na šachovnici pro novou hru
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
     *Vrátí figuru na zadaných souřadnicích. POkud jsou souřadnice špatně zadny vrací null
     * @param souradnice Šachové souřadnice, ze kterých získáváme figuru
     * @return figuru ze souřadnic, v případě nenalezeí figury vrací null
     */
    public Figura vyberFiguru(String souradnice){
        Point pSourad = Sachovnice.souradniceNaPoint(souradnice);
        return vyberFiguru(pSourad);
    }
    
    /**
     *Vrátí figuru na zadaných souřadnicích. POkud jsou souřadnice špatně zadny vrací null
     * @param souradnice souřadnice, ze kterých získáváme figuru
     * @return figuru ze souřadnic, v případě nenalezeí figury vrací null
     */
    public Figura vyberFiguru(Point souradnice){
        
        if(!existujeSouradnice(souradnice))
            return null;
        return rozmisteni[souradnice.x-1][souradnice.y-1];
    }
    
    /**
     *Provede Šachový tah. Přesune figuru na nové souřadnice a ze starých souřadnic figuru
     * smaže. Pokud je nepřátelská figura vyhozena tak jí uloží do vyhozených figur pro
     * případné vrácení tahu
     * @param figura figura, kterou se má táhnout
     * @param souradnice kam se má figurou táhnout
     * @throws IllegalArgumentException pokud jsou nesmyslně zadány souřadnice
     * @return true pokud tah proběhl úspěšne, false v opačném případě
     */
    public boolean tahni(Figura figura, String souradnice) throws IllegalArgumentException{
        boolean vyhozeni = false;
        Point kam = souradniceNaPoint(souradnice);
        return tahni(figura,kam);
    }
    
    /**
     *Provede Šachový tah. Přesune figuru na nové souřadnice a ze starých souřadnic figuru
     * smaže. Pokud je nepřátelská figura vyhozena tak jí uloží do vyhozených figur pro
     * případné vrácení tahu
     * @param figura figura, kterou se má táhnout
     * @param kam kam se má figurou táhnout
     * @return true pokud tah proběhl úspěšne, false v opačném případě
     */
    public boolean tahni(Figura figura, Point kam){
        boolean vyhozeni = false;
        for(Point policko : figura.getMozneTahy()){                             //Overi, zdali je tah mozny
            if(policko.equals(kam)){                                            //Tak je mozny
                if(vyberFiguru(kam) != null){                                   //Pokud je na policku, kde se táhne, nepřátelská figura
                    vyhodFiguru(vyberFiguru(kam));
                    vyhozeni = true;
                }
                SachTah tah = new SachTah(figura, figura.getPozice(), kam, 0, vyhozeni);
                tahy.add(tah);
                rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;  //Nastaví původní políčko figury na null
                rozmisteni[kam.x-1][kam.y-1] = figura;                              //Umístí Figuru na táhnuté políčko
                figura.setPozice(kam);                                              //Nastaví nové souřadnice Figury
                return true;
            }
        }
        return false;
    }
    
    
    
    /**
     *Vrátí šachovnici do stavu před posledním táhnutím
     * @return true-pokud proces proběhl úspěšně false pokud již není co vracet
     */
    public boolean vratTah(){
        SachTah t = tahy.poll();
        if(t==null)
            return false;
        Figura f = t.getFigura();
        rozmisteni[t.getOdKud().x-1][t.getOdKud().y-1] = f;
        f.setPozice(t.getOdKud());
        if(t.isVyhozena()){
            Figura fVyhoz = vratVyhozFiguru();                                  //Získá poslední vyhozenou figuru a smaze ji ze zásobníku
            rozmisteni[fVyhoz.getPozice().x -1][fVyhoz.getPozice().y -1] = fVyhoz;
        }
        t.tahZpet();                                                            //Informuje SachTahy o tahu z5
    return false;
    }
    
    /**
     * Provede jakýkoliv tah a ověří zda král barvy figury není ohrožen.
     * @param figura figura, kterou se táhne
     * @param kam kam má figura táhnout
     * @return false, když král není ohrožen, true v opačném případě.
     */
    public boolean simTahOverKrale(Figura figura, Point kam) throws IllegalArgumentException{
        boolean kralOhrozen;
        Figura vyhozena = null;
        if(!existujeSouradnice(kam))
            throw new IllegalArgumentException("Pole na ze kterých, nebo na které se má táhnout neexistují");
        if(vyberFiguru(kam) != null){
            vyhozena = vyberFiguru(kam);
        }
        rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;      //Nastaví původní políčko figury na null
        rozmisteni[kam.x-1][kam.y-1] = figura;                                  //Umístí Figuru na táhnuté políčko
        kralOhrozen = getKral(figura.barva).jeKralOhrozen();
        rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = figura;    //Vrati Figuru z5
        rozmisteni[kam.x-1][kam.y-1] = vyhozena;                                //políčko kam vrátí do původního stavu
        return kralOhrozen;
    }

    /**
     *Zjistí, jaká figura na zadaných souřadnicích stojí, nebo jestli je souřadnice prázdná.
     * @param pozice pozice zjišťovaného pole
     * @return 1-bílá figura, 0-čarná figura, -1-na políčku je volno
     * @throws IllegalArgumentException Vyhodí, pokud na šachovnici neexistuje zadaná souřadnice
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
     *Zjistí, jaká figura na zadaných souřadnicích stojí, nebo jestli je souřadnice prázdná.
     * @param x x-ová souřadnice
     * @param y y-ová souřadnice
     * @return 1-bílá figura, 0-čarná figura, -1-na políčku je volno
     * @throws IllegalArgumentException Vyhodí, pokud na šachovnici neexistuje zadaná souřadnice
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
     *Umístí figuru na šachovnici na souřadnice zadané figury.
     * Metoda také může vymazat původní figuru na souřadnicích zadané figury.
     * @param f Figura, která má být umístěna na šachovnici
     * @return true pokud byla figura úspěšně přidána, false v opačném případě
     */
    public boolean pridejFiguru(Figura f){
        if(!Sachovnice.existujeSouradnice(f.getPozice())){
            System.err.println("Figura nelze umístit na šahocnici kvůli jejím souřadnicím!");
            return false;
        }
        if(f instanceof Kral){
            if((f.barva && getKral(true) != null) || (!f.barva && getKral(false) != null)){
                System.err.println("Nemůžete přidat na jednu šachovnici 2 krále stejné barvy!");
                return false;
            }
            kral[barvaNaInt(f.barva)] = (Kral) f;
        }
        rozmisteni[f.getPozice().x -1][f.getPozice().y -1 ] = f;
        return true;
    }
    
    /**
     *Získá krále dané barvy.
     * @param barva
     * @return bílý nebo černý král
     */
    public Kral getKral(boolean barva){
        return kral[barvaNaInt(barva)];
    }
    
    /**
     * Vykreslí šachovnici do konzole
     */
    public void vykresliAsciiSachovnici(){
        for(int i = 7;i >= 0; i--){
            for(int j = 0;j < 8; j++){
                if(rozmisteni[j][i] == null)
                    System.out.printf(" OO ");
                else
                    System.out.printf(" " + rozmisteni[j][i].vratVykresli() + " ");
            }
            System.out.printf("\n");
        }
    }

    /**
     *Převede šachové vyjádření souřadnic do číselného.
     * př. "A2" => p.x=1 p.x=2
     * @param souradnice souřadnice, které ze ktarých se určí hodnota.
     * @return Číselně vyjádřené souřadnice
     * @throws IllegalArgumentException Vyhodí, pokud na šachovnici neexistuje zadaná souřadnice
     */
    public static Point souradniceNaPoint(String souradnice){
        souradnice = souradnice.toLowerCase();
        char[] pozice = souradnice.toCharArray();
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
     *Převede zadané číselné souřadnice do šachových
     * př. p.x=1 p.x=2 => "A2"
     * @param souradnice souřadnice, které ze ktarých se určí hodnota.
     * @return šachově vyjdřené souřadnice
     * @throws IllegalArgumentException Vyhodí, pokud na šachovnici neexistuje zadaná souřadnice
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
     *Zjístí, zdali na šachovnice 8x8 existuje zadaná souřadnice
     * @param souradnice
     * @return true pokud se souřadnice na šachovnici standartní šachovnici nachází,false v opačném případě
     */
    public static boolean existujeSouradnice(Point souradnice){
        return !((souradnice.x >= 9 || souradnice.x <= 0) || (souradnice.y >= 9 || souradnice.y <= 0));
    }

    /**
     *Zjístí, zdali na šachovnice 8x8 existuje zadaná souřadnice
     * @param x x-ová souřadnice
     * @param y y-ová souřadnice
     * @return true pokud se souřadnice na šachovnici standartní šachovnici nachází,false v opačném případě
     */
    public static boolean existujeSouradnice(int x, int y){
        return !((x >= 9 || x <= 0) || (y >= 9 || y <= 0));
    }
    
    /**
     *Vrátí číselné zastoupení byrvy figury
     * @param barva barva figury
     * @return 1-bílá, 0-čarná
     */
    public static int barvaNaInt(boolean barva){
        if(barva)
            return 1;
        return 0;
    }
    
    /*
     * Vyhodí figuru z šachovnice tzn. zařadí jí mezi vyhozené a nastaví jí vyhozena true
     */
    private void vyhodFiguru(Figura figura){
        figura.setVyhozena(true);
        vyhozeneFigury.add(figura);
    }
    /*
     * Vrátí poslední vyhozenou figuru a nastaví jí vyhozena na false
     */
    private Figura vratVyhozFiguru(){
        Figura f = vyhozeneFigury.poll();
        f.setVyhozena(false);
        return f;
    }
    
}
