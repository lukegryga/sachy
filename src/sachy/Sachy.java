/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachy;

import java.util.Scanner;

/**
 *
 * @author Lukáš
 */
public class Sachy {
    
    private static final Scanner sc = new Scanner(System.in, "windows-1250");
    private static final ChessKordinator ChM = ChessKordinator.getChessKordinator();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChM.zacniHru();
    }        
    
}
