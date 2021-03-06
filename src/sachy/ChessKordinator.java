
package sachy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Třída umožňující jednoduchou práci a ovládání šachovnice
 * @author Lukáš Gryga
 */
public class ChessKordinator {
    
    private static final int PORT = 10097;
    
    private static ChessKordinator jedinacek;
    
    /**
     * Taktovací frekvence
     */
    public final int TAKTOVANI = 50;
    
    private Socket sProtihrac;
    private PrintStream out;
    private BufferedReader in;
    private boolean barvaHrace;
    
    private final Scanner sc = new Scanner(System.in, "UTF-8");
    
    GraphicsInterface GUI;
    private Sachovnice sachovnice;
    
    private final Thread hlavniVlakno = Thread.currentThread();
    
    
    /**
     * Slouží k alternativnímu toku dat na vsput a další zpracování dat
     */
    String vyrovnavaci = "";
    /**
     * Získá instanci šachového kordinátora, jimž lze jednoduše ovládat šachovou hru
     * @return ChessKordinator
     */
    public static ChessKordinator getChessKordinator(){
        if(jedinacek == null){
            jedinacek = new ChessKordinator();
        }
        return jedinacek;
    }
    private ChessKordinator(){}
    /**
     * Začne novou šachovou hru se standartním rozestavením šachových figur
     */
    public void novaHra(){
        //sachovnice = new Sachovnice(initHrac("Zadejte jmeno bílého hráče:", true),initHrac("Zadejte jmeno černého hráče:", false));
        sachovnice = GraphicsIO.initHraci();
        GUI = new GraphicsInterface(sachovnice, false, sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[0]);
        sachovnice.sachHodiny.setCas(GraphicsIO.ziskejCislo("Nastave šachové hodiny [s]"));
        sachovnice.novaHra();
        System.out.println("----Začíná nová hra----");
        System.out.println(sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[0]);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        do{
            if(dalsiKolo())
                break;
        }while(true);
        GUI.setMat();
        sachovnice.ulozZaznamHry();
        GUI.prekresli();
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
        GraphicsIO.zobrazHlasku("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
    }
    
    /**
     * Načte uloženou hru a pokračuje v ní.
     */
    public void hrejUlozenouHru(){
        sachovnice = new Sachovnice();
        try {
            sachovnice.nactiHru();
        } catch (IOException ex) {
            System.err.println("Hra se nepodařila načíst, nebo není žádná uložená");
            return;
        }
        GUI = new GraphicsInterface(sachovnice, false, sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[0]);
        System.out.println(sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[0]);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        if(!sachovnice.getKral(true).jeMat() && !sachovnice.getKral(false).jeMat()){
            do{
                if(dalsiKolo())
                    break;
            }while(true);
        }
        GUI.setMat();
        sachovnice.ulozZaznamHry();
        GUI.prekresli();
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
        GraphicsIO.zobrazHlasku("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
    }
    /**
     * Vytvoří novou servrovou hru, nebo se k ní připojí.Když je vytvořená nová hra (vytvor
     * hru je true), pak hráč bude mít bílé, v opačném případě černé figury
     * @param vytvorSHru true - založí novou servrovou hru, false - připojí se k jiné hře
     */
     public void serverHra(boolean vytvorSHru){
        if(vytvorSHru)
            najdiHrace();
        else
            for(int i=0;i<2;i++){
            if(pripojKServru())
                break;
            if(i==1)
                return;
            }
        barvaHrace = vytvorSHru;
        sachovnice = initSHraci(vytvorSHru);
        sachovnice.novaHra();
        GUI = new GraphicsInterface(sachovnice, true, sachovnice.getHrac(vytvorSHru).toString());
        sachovnice.vykresliAsciiSachovnici();
        do{
            if(dalsiSKolo())
                break;
        }while(true);
        GUI.setMat();
        sachovnice.ulozZaznamHry();
        GUI.prekresli();
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
        GraphicsIO.zobrazHlasku("Vyhral: " + sachovnice.getHrac(!sachovnice.getHracNaTahu().isBarva()));
    }
     
     private boolean dalsiSKolo(){
        Figura f;
        System.out.printf("----------%d. kolo----------", SachTah.getTah());
        if(sachovnice.getHracNaTahu().isBarva() == barvaHrace){
            f = vyberFiguru(sachovnice.getHracNaTahu().isBarva(), false);
            tahni(f, false);
        }else{
            System.out.println("\nČekání na protihráčův tah");
            f = vyberFiguru(sachovnice.getHracNaTahu().isBarva(), true);
            tahni(f, true);
        }
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        return sachovnice.getKral(sachovnice.getHracNaTahu().isBarva()).jeMat();
     }
    
    /*Hraje hrac na tahu (nepřepne se hráč)
    Vraci true když je mat hráče který není na tahu*/
    private boolean dalsiKolo(){
        Figura f;
        System.out.printf("----------%d. kolo----------", SachTah.getTah());
        f = vyberFiguru(sachovnice.getHracNaTahu().isBarva(), false);
        tahni(f, false);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        return sachovnice.getKral(sachovnice.getHracNaTahu().isBarva()).jeMat();
    }
    /**
     * 
     * @param barva barva hráče který je na tahu
     * @param serverProtihrac aby program rozeznal, jestli má příjmout data přes síť nebo z počítače.
     * @return Vybraná figura
     */
    private Figura vyberFiguru(boolean barva, boolean serverProtihrac){
        Figura f = null;
        boolean neuspeh = true;
        do{
            System.out.printf("\n" + sachovnice.getHracNaTahu()+ ": Vyberte figuru kterou budete táhnout:");
            try{
                if(!serverProtihrac)
                    f = sachovnice.vyberFiguru(input());
                else
                    f = sachovnice.vyberFiguru(prijmi());
            }catch(IllegalArgumentException e){
                System.err.println(e.getMessage());
                System.err.println("Zkus to znova!!!");
            }
            if(f != null){
                neuspeh = false;
                if(f.barva != barva){
                    System.out.printf("Nemůžete vybrat figuru jiné barvy");
                    neuspeh = true;
                }
            }else{
                System.out.printf("Na políčku nestojí žádná figura");
            }
        }while(neuspeh);
        GUI.setVybranaFigura(f);
        return f;
    }
    /**Táhne, nebo změni vybranou figuru
     * 
     * @param f - figura, kterou se má táhnout
     * @param serverProtihrac - aby program rozeznal, jestli má příjmout data přes síť nebo z počítače.
     */
    private void tahni(Figura f, boolean serverProtihrac){
        boolean neuspeh = true; //Pro smyšku neúspěšnýc tahů
        boolean opetVybrana; // Pro smyčku vybírání jiné figury
        String kam = null;
        do{
            try{
                do{
                    opetVybrana = false;
                    System.out.printf("\n" + f + " -> Kam chcete táhnout:");
                    if(!serverProtihrac)
                        kam = input();
                    else{
                        kam = prijmi();
                    }
                    if(sachovnice.jeVolno(Sachovnice.souradniceNaPoint(kam)) == Sachovnice.barvaNaInt(f.barva)){
                        f = sachovnice.vyberFiguru(kam);
                        System.out.println("Vybral jste jinou figuru!");
                        System.out.println("Správný šachista když už se figury dotkne, tak hraje");
                        System.out.println("Vybraná figura je nyní:" + f);
                        opetVybrana = true;
                        GUI.setVybranaFigura(f);
                    }
                }while(opetVybrana);
                if(sachovnice.tahni(f, kam)){
                    neuspeh = false;
                }else{
                    System.out.printf("\nNa políčko nelze táhnout!!!");
                }
            }catch(IllegalArgumentException e){
                System.err.println(e.getMessage());
                System.err.println("Zkus to znova!!!");
            }
        }while(neuspeh);
        GUI.setVybranaFigura(null);
    }
    
    private Sachovnice initSHraci(boolean server){
        System.out.println("Zadejte jméno vašeho hráče:");
        //String jmeno = sc.nextLine();
        String jmeno = GraphicsIO.initHrac();
        posli(jmeno);
        SachHrac hrac1 = new SachHrac(jmeno, server);
        System.out.println("Čekání na protihráče....");
        GraphicsIO.zobrazHlasku("Čekání na protihráče....");
        SachHrac hrac2 = new SachHrac(prijmi(), !server);
        GraphicsIO.skryjHlasku();
        System.out.println(hrac1 + "(Vy) vs. " + hrac2 + "(Soupeř)");
        return new Sachovnice(hrac1, hrac2);
    }
    
    private SachHrac initHrac(String zprava, boolean barva){
        String jmeno;
        System.out.println("Vytváří se hráč: " + zprava);
        jmeno = sc.nextLine();
        return new SachHrac(jmeno, barva);
    }
    
    private String input(){
        byte[] b = new byte[400];
        
        try {
            int pp = System.in.available();
            boolean ppp = vyrovnavaci.isEmpty();
            while(pp<= 0 && ppp){
                try {
                    Thread.sleep(TAKTOVANI);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChessKordinator.class.getName()).log(Level.SEVERE, null, ex);
                }
                pp = System.in.available();
                ppp = vyrovnavaci.isEmpty();
            }
            if(!vyrovnavaci.isEmpty()){
                String s = vyrovnavaci;
                vyrovnavaci = "";
                System.out.printf(s);
                return s;
            }
            return sc.next();
        } catch (IOException ex) {
            Logger.getLogger(ChessKordinator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private boolean pripojKServru(){
        System.out.println("IP Adresa:");
        try {
            String myIp = InetAddress.getLocalHost().getHostAddress();
            String zacatekMyIp = myIp.substring(0, myIp.lastIndexOf(".")+1);
            GraphicsIO.zobrazHlasku("Hledám hráče na lan...");
            for(int i=0;i<256;i++){
                try{
                    System.out.println(zacatekMyIp + Integer.toString(i));
                    sProtihrac = new Socket();
                    sProtihrac.connect(new InetSocketAddress(zacatekMyIp + Integer.toString(i), PORT), 200);
                    if(sProtihrac.isConnected()){
                        out = new PrintStream(sProtihrac.getOutputStream());
                        in = new BufferedReader(new InputStreamReader(sProtihrac.getInputStream()));
                    }
                    break;
                }catch(IOException e){
                    if(i==255){
                        GraphicsIO.zobrazHlasku("Žádná hra nenalezena. Zkuste to později");
                        return false;
                    }
                }
            }
            GraphicsIO.skryjHlasku();
            //sProtihrac = new Socket(sc.nextLine(), PORT);
            //sProtihrac = new Socket(GraphicsIO.IPAdresa(), PORT);
            //out = new PrintStream(sProtihrac.getOutputStream());
            //in = new BufferedReader(new InputStreamReader(sProtihrac.getInputStream()));
        } catch (IOException ex) {
           System.err.println("Špatá IP Adesa, zkuste to znovu.");
           return false;
        }
        System.out.println("Připojeno.");
        return true;
    }

    private void najdiHrace() {
        ServerSocket server = null;
        try{
           server = new ServerSocket(PORT);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            System.out.println("Čekám na připojení hráče....");
            GraphicsIO.zobrazHlasku("Čekám na připojení hráče....");
            sProtihrac = server.accept();
            out = new PrintStream(sProtihrac.getOutputStream());
            in = new BufferedReader(new InputStreamReader(sProtihrac.getInputStream()));
            System.out.println("Protihráč připojen z IP:" + sProtihrac.getInetAddress());
       }catch(Exception e){
           System.out.println(e.getMessage());
       }
        GraphicsIO.skryjHlasku();
    }
    /**
     * Pošle data do počítače protihráče
     * @param message -Posílaná zpráva
     */
    public void posli(String message) {
        out.flush();
        out.println(message);
    }
    //**
    
    public void doselCas(boolean hrac){
        hlavniVlakno.interrupt();
        sachovnice.ulozZaznamHry();
        GUI.prekresli();
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Došel Čas");
        System.out.println("Vyhral: " + sachovnice.getHrac(hrac));
        GraphicsIO.zobrazHlasku("Vyhral: " + sachovnice.getHrac(hrac));
    }
    
    private String prijmi(){
        try {
            return in.readLine();
        } catch (IOException ex) {
            Logger.getLogger("Nepodařilo se příjmout soupeřova data");
        }
        return null;
    }
}
