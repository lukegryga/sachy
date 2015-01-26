
package sachy;

import java.awt.Point;
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
    
    public void test(){
        Sachovnice s1 = new Sachovnice();
        s1.testMethod();
        Figura kral = s1.getKral(true);
        for(Point p : kral.getMozneTahy()){
            System.out.println(kral.toString() + "->" + Sachovnice.pointNaSouradnice(p));
        }
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
