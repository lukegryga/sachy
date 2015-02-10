
package sachy;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Třída umožňující jednoduchou práci a ovládání šachovnice
 * @author Lukáš Gryga
 */
public class ChessKordinator {
    
    private static ChessKordinator jedinacek;
    
    private final Scanner sc = new Scanner(System.in, "utf-8");
    
    GraphicsInterface GUI;
    private Sachovnice sachovnice;
    
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
        sachovnice = new Sachovnice(initHrac("Zadejte jmeno bílého hráče:", true),initHrac("Zadejte jmeno černého hráče:", false));
        GUI = new GraphicsInterface(sachovnice);
        sachovnice.novaHra();
        System.out.println("----Začíná nová hra----");
        System.out.println(sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[1]);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        do{
            if(dalsiKolo())
                break;
        }while(true);
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + sachovnice.getHracNaTahu());
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
        GUI = new GraphicsInterface(sachovnice);
        System.out.println(sachovnice.getHraci()[1] + " vs. " + sachovnice.getHraci()[1]);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        do{
            if(dalsiKolo())
                break;
        }while(true);
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + sachovnice.getHracNaTahu());
    }
    
    /*Hraje hrac na tahu (nepřepne se hráč)
    Vraci true když je mat hráče který není na tahu*/
    private boolean dalsiKolo(){
        Figura f;
        System.out.printf("----------%d. kolo----------", SachTah.getTah());
        f = vyberFiguru(sachovnice.getHracNaTahu().isBarva());
        tahni(f);
        sachovnice.vykresliAsciiSachovnici();
        GUI.prekresli();
        return sachovnice.getKral(sachovnice.getHracNaTahu().isBarva()).jeMat();
    }
    
    private Figura vyberFiguru(boolean barva){
        Figura f = null;
        boolean neuspeh = true;
        do{
            System.out.printf("\n" + sachovnice.getHracNaTahu()+ ": Vyberte figuru kterou budete táhnout:");
            try{
                f = sachovnice.vyberFiguru(input());
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
    /*tahne, nebo změni vybranou figuru*/
    private void tahni(Figura f){
        boolean neuspeh = true; //Pro smyšku neúspěšnýc tahů
        boolean opetVybrana; // Pro smyčku vybírání jiné figury
        String kam = null;
        do{
            try{
                do{
                    opetVybrana = false;
                    System.out.printf("\n" + f + " -> Kam chcete táhnout:");
                    kam = input();
                    if(sachovnice.jeVolno(Sachovnice.souradniceNaPoint(kam)) == Sachovnice.barvaNaInt(f.barva)){
                        f = sachovnice.vyberFiguru(kam);
                        System.out.println("Vybral jste jinou figuru!");
                        System.out.println("Správná šachista když už se figury dotkne tak hraje");
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
    
    private SachHrac initHrac(String zprava, boolean barva){
        String jmeno;
        System.out.println("Vytváří se hráč: " + zprava);
        jmeno = sc.next();
        return new SachHrac(jmeno, barva);
    }
    
    private String input(){
        byte[] b = new byte[400];
        
        try {
            int pp = System.in.available();
            boolean ppp = vyrovnavaci.isEmpty();
            while(pp<= 0 && ppp){
                try {
                    Thread.sleep(50);
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
}
