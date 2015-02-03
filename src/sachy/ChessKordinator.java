
package sachy;

import java.util.Scanner;

/**
 *Třída umožňující jednoduchou práci a ovládání šachovnice
 * @author Lukáš Gryga
 */
public class ChessKordinator {
    
    private static ChessKordinator jedinacek;
    
    private final Scanner sc = new Scanner(System.in, "utf-8");
    
    private Sachovnice sachovnice;
    private SachHrac hrac0;
    private SachHrac hrac1;
    private SachHrac hracNaTahu;                                                    //Buď hráč 1 nebo hráč 2
    private int kolo = 0;
    private boolean hra = false;
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
    public void hrej(){
        hra = true;
        sachovnice = new Sachovnice();
        sachovnice.novaHra();
        hrac1 = initHrac("Zadejte jmeno černého hráče:", true);
        hrac0 = initHrac("Zadejte jmeno bílého hráče:", false);
        hracNaTahu = hrac1;
        System.out.println("----Začíná nová hra----");
        System.out.println(hrac1 + " vs. " + hrac0);
        sachovnice.vykresliAsciiSachovnici();
        do{
            if(dalsiKolo())
                break;
            prepniHraceNaTahu();
            if(dalsiKolo())
                break;
            prepniHraceNaTahu();
        }while(true);
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("Mat");
        System.out.println("Vyhral: " + hracNaTahu);
    }
    
    /*Hraje hrac na tahu (nepřepne se hráč)
    Vraci true když je mat hráče který není na tahu*/
    private boolean dalsiKolo(){
        Figura f;
        kolo ++;
        System.out.printf("----------%d. kolo----------", kolo);
        f = vyberFiguru(hracNaTahu.isBarva());
        tahni(f);
        sachovnice.vykresliAsciiSachovnici();
        return sachovnice.getKral(!hracNaTahu.isBarva()).jeMat();
    }
    
    private Figura vyberFiguru(boolean barva){
        Figura f = null;
        boolean neuspeh = true;
        do{
            System.out.printf("\n" + hracNaTahu + ": Vyberte figuru kterou budete táhnout:");
            try{
                f = sachovnice.vyberFiguru(sc.next());
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
                if(f.getMozneTahy().isEmpty()){
                    System.out.printf("Figura nemůže nikam táhnout, vyberte jinou");
                    neuspeh = true;
                }
            }else{
                System.out.printf("Na políčku nestojí žádná figura");
            }
        }while(neuspeh);
        return f;
    }
    
    private void tahni(Figura f){
        boolean neuspeh = true;
        do{
            System.out.printf("\n" + f + " -> Kam chcete táhnout:");
            try{
                if(sachovnice.tahni(f, sc.next())){
                    neuspeh = false;
                }else{
                    System.out.println("Na políčko nelze táhnout!!!");
                }
            }catch(IllegalArgumentException e){
                System.err.println(e.getMessage());
                System.err.println("Zkus to znova!!!");
            }
        }while(neuspeh);
    }

    private void prepniHraceNaTahu(){
        if(hracNaTahu.equals(hrac0))
            hracNaTahu = hrac1;
        else
            hracNaTahu = hrac0;
    }
    
    private SachHrac initHrac(String zprava, boolean barva){
        String jmeno;
        System.out.println("Vytváří se hráč: " + zprava);
        jmeno = sc.next();
        return new SachHrac(jmeno, barva);
    }
}
