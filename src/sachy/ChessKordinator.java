
package sachy;

import java.util.Scanner;

public class ChessKordinator {
    
    private static ChessKordinator jedinacek;
    
    private final Scanner sc = new Scanner(System.in, "windows-1250");
    
    private Sachovnice sachovnice;
    private SachHrac hrac1;
    private SachHrac hrac2;
    private SachHrac hracNaTahu;                                                    //Buď hráč 1 nebo hráč 2
    private boolean hra = false;
    
    public static ChessKordinator getChessKordinator(){
        if(jedinacek == null){
            jedinacek = new ChessKordinator();
        }
        return jedinacek;
    }
    private ChessKordinator(){}
    
    public void zacniHru(){
        System.out.println("Právě byla zahájená nová šachová hra");
        hrac1 = initHrac("Zadejte jméno hráče1: ", true);
        hrac2 = initHrac("Zadejte jméno hráče2: ", false);
        hra = true;
        hracNaTahu = hrac1;
        sachovnice = Sachovnice.getSachovnice();
        sachovnice.novaHra();
        hrej();
    }
    
    private void hrej(){
        boolean opakovat = true;
        int kolo = 1;
        while(hra == true){
            if(hracNaTahu.equals(hrac1))
                kolo ++;
            System.out.println("Kolo " + kolo + ": Na tahu :" + hracNaTahu.getJmeno());
            Figura vybFigura = null;
            do{
                System.out.printf("Vyber figuru:");
                String vybSouradnice = sc.nextLine();
                vybFigura = overVyber(vybSouradnice);
            }while(vybFigura == null);
            System.out.println("Vybraná figura: " + vybFigura.toString());
            do{
                String kam;
                System.out.printf("Zadejte cílovou souřadnici:");
                kam = sc.nextLine();
                if(sachovnice.tahni(vybFigura, kam)){
                    opakovat = false;
                }
            }while(opakovat);
            prepniHraceNaTahu();
            if(sachovnice.getKral(hracNaTahu.isBarva()).jeMat()){
                System.err.println("Konec Hry");
                return;
            }
        }
    }
    
    private Figura overVyber(String souradnice){
        Figura vybFigura;
        try{
            vybFigura = sachovnice.vyberFiguru(souradnice);
        }catch(IllegalArgumentException e){
            System.err.println("Špatně zadaná nebo neexistující souřadnice");
            return null;
        }
        if(vybFigura == null){
            System.err.println("Na vybraném poli nic nestojí");
            return null;
        }else if(vybFigura.barva != hracNaTahu.isBarva()){
            System.err.println("Nemůžete vybrat soupeřovu figuru");
            return null;
        }
        return vybFigura;
    }
    
    private void prepniHraceNaTahu(){
        if(hracNaTahu.equals(hrac1))
            hracNaTahu = hrac2;
        else
            hracNaTahu = hrac1;
    }
    
    private SachHrac initHrac(String zprava, boolean barva){
        String jmeno;
        System.out.println("Vytváří se hráč: " + zprava);
        jmeno = sc.nextLine();
        return new SachHrac(jmeno, barva);
    }
}
