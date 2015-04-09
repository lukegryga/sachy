
package sachy;

import java.awt.Point;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final Deque<SachTah> tahy = new LinkedList<>();
    private final ChessKordinator ChK = ChessKordinator.getChessKordinator();
    private final Database database = new Database();
    
    private final Figura[][] rozmisteni = new Figura[8][8];
    private final Kral[] kral = new Kral[2];                                            //Králové
    private final SachHrac[] hraci = new SachHrac[2];
    
    
    /**
     *Vytvorí instanci šachovnice. Šachovnic může být i více a na každé může probíhat jiná hra
     */
    public Sachovnice(){
        this(null, null);
    }
    /**
     *Vytvorí instanci šachovnice. Šachovnic může být i více a na každé může probíhat jiná hra
     * @param hrac1 - Bílý Hráč
     * @param hrac0 - Černý Hráč
     */
    public Sachovnice(SachHrac hrac1, SachHrac hrac0){
            hraci[1] = hrac1;
            hraci[0] = hrac0;
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
        if(hraci[1].isBarva())
            hraci[1].setNaTahu(true);
        else
            hraci[0].setNaTahu(true);
    }
    
    /**
     * @return Hráč, který je aktuálně na tahu.
     */
    public SachHrac getHracNaTahu(){
        if(hraci[1].isNaTahu())
            return hraci[1];
        if(hraci[0].isNaTahu())
            return hraci[0];
        return null;
    }
    
    /**
     * @return vrací hráče na šachovnici
     */
    public SachHrac[] getHraci(){
        return hraci;
    }
    
    /**
     * Vrátí hráče na základě barvy
     * @param barva barva vráceného hráče
     * @return Šachového hráče na zadané šachovnici
     */
    public SachHrac getHrac(boolean barva){
        if(hraci[1].isBarva() == barva)
            return hraci[1];
        return hraci[0];
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
     * případné vrácení tahu. Po úspěšném provedení tahu přepne hráče na tahu
     * @param figura figura, kterou se má táhnout
     * @param kam kam se má figurou táhnout
     * @return true pokud tah proběhl úspěšne, false v opačném případě
     */
    public boolean tahni(Figura figura, Point kam){
        boolean vyhozeni = false;
        for(Point policko : figura.getMozneTahyOpt()){                             //Overi, zdali je tah mozny
            if(policko.equals(kam)){                                            //Tak je mozny
                if(vyberFiguru(kam) != null){                                   //Pokud je na policku, kde se táhne, nepřátelská figura
                    vyhodFiguru(vyberFiguru(kam));
                    vyhozeni = true;
                }
                SachTah tah = new SachTah(figura, figura.getPozice(), kam, 0, vyhozeni);
                tahy.add(tah);
                rozmisteni[figura.getPozice().x-1][figura.getPozice().y-1] = null;  //Nastaví původní políčko figury na null
                rozmisteni[kam.x-1][kam.y-1] = figura;                              //Umístí Figuru na táhnuté políčko
                figura.setPozice(kam);                                             //Nastaví nové souřadnice Figury
                prepniHraceNaTahu();
                oznamFiguramTah();
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
        }else
            rozmisteni[t.getKam().x-1][t.getKam().y-1] = null;
        t.tahZpet();                                                             //Informuje SachTahy o tahu z5
        prepniHraceNaTahu();
        oznamFiguramTah();
        return true;
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
     * Pokud je Figura Král, tak je uloží do proměnných Král. Pokud se uživatel pokusí
     * zadat 2 krále, metoda to nedovolí.
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
     * Vrátí rozmístění figur na dané šachovnici.
     * Nedoporučuje se s figurami během hry manipulovat
     * @return rozmístění figur na dané šachovnici
     */
    public Figura[][] getRozmisteniFigur(){
        return rozmisteni;
    }
    /**
     * Uloží hru do souborů ve složce GameData, v případě že složka a soubory neexistují,
     * pak je vytvoří. Při uložení hry se přepíše ta stará.
     * @throws IOException 
     */
    public void ulozHru() throws IOException{
        int i;
        String path = "GameData";
        String file = "rozmisteni.txt";
        database.uloz("Čistím Soubor", path, file, false);
        database.vymazRadek(path + "/" + file, 0);
        for(Figura ff[] : rozmisteni){
            for(Figura f : ff){
                if(f!=null)
                    database.uloz(f.textReprezentaceFigury(), path, file, true);
            }
        }
        file = "vyhozeneFigury.txt";
        database.uloz("Čistím Soubor", path, file, false);
        database.vymazRadek(path + "/" + file, 0);
        for(Figura f : vyhozeneFigury){
            database.uloz(f.textReprezentaceFigury(), path, file, true);
        }
        file = "hraci.txt";
        database.uloz(hraci[0].textReprezentaceHrace(), path, file, false);
        database.uloz(hraci[1].textReprezentaceHrace(), path, file, true);
    }
    /**
     * Uloží do souboru výpis tahů naposledy hrané hry, při další hře se soubor přepíše
     */
    public void ulozVypisTahu(){
        try {
            String path = "GameData";
            String file = "vypis.txt";
            database.uloz("Čistím Soubor", path, file, false);
            database.vymazRadek(path + "/" + file, 0);
            database.uloz(ChessKordinator.getChessKordinator().GUI.titulek,path, file, true);
            database.uloz("----------------------------------------------",path, file, true);
            for(SachTah t : tahy){
                database.uloz(t.toString(),path, file, true);
            }
        } catch (IOException ex) {
            Logger.getLogger(Sachovnice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Uloží záznam o hře do složky Záznamy. Ve složce SystemData/system.txt si ukládá,
     * kolik her již bylo odehráno (Kvůli číslování)
     */
    public void ulozZaznamHry(){
        String path = "SystemData";
        String file = "system.txt";
        String[] soubor;
        int cisloSouboru = 1;
        try {
            soubor = database.nacti(path + "/" + file);
            cisloSouboru = Integer.parseInt(soubor[0]);
        } catch (IOException ex) {
            try {
                database.uloz("1", path, file, false);
            } catch (IOException ex1) {
                return;
            }
        }
        try {
            database.uloz(String.valueOf(cisloSouboru+1), path, file, false);
        } catch (IOException ex) {
            Logger.getLogger(Sachovnice.class.getName()).log(Level.SEVERE, null, ex);
        }
        path = "Zaznamy";
        file = "hra" + cisloSouboru + ".txt";
        try {
                database.uloz(ChessKordinator.getChessKordinator().GUI.titulek,path, file, true);
                database.uloz("----------------------------------------------",path, file, true);
                for(SachTah t : tahy){
                    database.uloz(t.toString(),path, file, true);
                }
        } catch (IOException ex) {
            Logger.getLogger(Sachovnice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Načte Hru ze souborů ve složce GameData. Pokud není žádná hra uložená, nefunguje to. :D
     * @throws IOException 
     */
    public void nactiHru() throws IOException{
        String[] nacHraci = database.nacti("GameData/hraci.txt");
        String[] nacVyhozeneFigury = database.nacti("GameData/vyhozeneFigury.txt");
        String[] nacRozmisteni = database.nacti("GameData/rozmisteni.txt");
        String[] hrac0 = nacHraci[0].split(",");
        String[] hrac1 = nacHraci[1].split(",");
        hraci[0] = new SachHrac(hrac0[0], Sachovnice.intNaBool(Integer.parseInt(hrac0[1])), Sachovnice.intNaBool(Integer.parseInt(hrac0[2])));
        hraci[1] = new SachHrac(hrac1[0], Sachovnice.intNaBool(Integer.parseInt(hrac1[1])), Sachovnice.intNaBool(Integer.parseInt(hrac1[2])));
        for(String vyhozFigura : nacVyhozeneFigury){
            vyhozeneFigury.add(Figura.vytvorKonkretniFigufu(vyhozFigura.split(","), this));
        }
        for(String nacRozmisteni1 : nacRozmisteni){
            pridejFiguru(Figura.vytvorKonkretniFigufu(nacRozmisteni1.split(","), this));
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
        char posX = (char) (souradnice.x + 64);
        String posY = Integer.toString(souradnice.y);
        
        return posX + posY;
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
    
    /**
     * Vrací boolean na základě intu
     * @param barva - číselné zasoupení barvy
     * @return false pokud je číslo 0 a menší, true v opačném případě
     */
    public static boolean intNaBool(int barva){
        return barva > 0;
    }
    
    private void prepniHraceNaTahu(){
        if(hraci[1].isNaTahu()){
            hraci[1].setNaTahu(false);
            hraci[0].setNaTahu(true);
        }else if(hraci[0].isNaTahu()){
            hraci[1].setNaTahu(true);
            hraci[0].setNaTahu(false);
        }
    }
    
    private void oznamFiguramTah(){
        for(Figura[] ff : rozmisteni){
            for(Figura f: ff){
                if(f != null)
                    f.bylProvedenTah();
            }
        }
    }
    
    /**
     * Vyhodí figuru z šachovnice tzn. zařadí jí mezi vyhozené a nastaví jí vyhozena true
     */
    private void vyhodFiguru(Figura figura){
        figura.setVyhozena(true);
        vyhozeneFigury.add(figura);
    }
    /**
     * Vrátí poslední vyhozenou figuru a nastaví jí vyhozena na false
     */
    private Figura vratVyhozFiguru(){
        Figura f = vyhozeneFigury.poll();
        f.setVyhozena(false);
        return f;
    }
    
}
