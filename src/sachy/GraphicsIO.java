

package sachy;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *Třída sloužící ke grafické komunikaci s uživatelem, co se týče zadávání jmen, IP atd.
 * @author Lukáš Gryga
 */
public class GraphicsIO {
    
    private static JFrame hlaskoveOkno = null;
    private static JLabel hlaska = new JLabel();
    
    /**
     * Zobrazí okno s hláškou. Okno lze zavřít metedou <code>skryjHlasku</code>
     * @param message zpráva zobrazící se v okně
     */
    public static void zobrazHlasku(String message){
        if(hlaskoveOkno == null){
            hlaskoveOkno = new JFrame();
            hlaskoveOkno.setLayout(new FlowLayout());
            hlaskoveOkno.setPreferredSize(new Dimension(400,100));
            hlaskoveOkno.setLocationRelativeTo(null);
            hlaskoveOkno.add(hlaska);
        }
        hlaska.setText(message);
        hlaskoveOkno.setVisible(true);
        hlaskoveOkno.pack();
    }
    
    /**
     * Pokud je otevřeno okno s hláškou, zavře ho
     */
    public static void skryjHlasku(){
        if(hlaskoveOkno.isVisible()){
            hlaskoveOkno.setVisible(false);
        }
    }
    
    /**
     * Vrátí šachovnici na základě jmen zadaných do grafického okna.
     * Uspí vlákno ve kterém je volána metoda a počká na zadání jmen do políček
     * @return 
     */
    public static Sachovnice initHraci(){
        setNimbus();
        Thread hlavniVlakno = Thread.currentThread();
        JFrame okno = new JFrame();
        okno.setLayout(new FlowLayout());
        okno.setSize(250,220);
        okno.setLocationRelativeTo(null);
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel napis1 = new JLabel("Zadejte jméno bílého hráče.");
        JTextField hrac1 = new JTextField("",15);
        JLabel napis0 = new JLabel("Zadejte jméno černého hráče.");
        JTextField hrac0 = new JTextField("",15);
        JButton hotovo = new JButton("Hotovo");
        JLabel chybHlaska = new JLabel("!!!!Pole nejsou správně vyplněná!!!!");
        okno.add(napis1);
        okno.add(hrac1);
        okno.add(napis0);
        okno.add(hrac0);
        okno.add(hotovo);
        okno.add(chybHlaska);
        chybHlaska.setVisible(false);
        hotovo.addActionListener((ActionEvent e) -> {
            if(!"".equals(hrac1.getText()) && !"".equals(hrac0.getText())){
                hlavniVlakno.resume();
                okno.dispose();
            }else{
                chybHlaska.setVisible(true);
            }
        });
        okno.setVisible(true);
        
        hlavniVlakno.suspend();
        System.out.println("Bílý hráč:" + hrac1.getText());
        System.out.println("Černý hráč:" + hrac0.getText());
        return new Sachovnice(new SachHrac(hrac1.getText(), true), new SachHrac(hrac0.getText(), false));
    }
    
    /**
     * Uspí vlákno na kterém se volá metoda do té doby, dokud uživatel nezadá své jméno
     * do grafického okna.
     * @return Zadané jméno hráče jiné jež prázdný řetězec
     */
    public static String initHrac(){
        setNimbus();
        Thread hlavniVlakno = Thread.currentThread();
        JFrame okno = new JFrame();
        okno.setLayout(new FlowLayout());
        okno.setSize(200,150);
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setLocationRelativeTo(null);
        okno.add(new JLabel("Zadejte své jméno:"));
        JTextField jmenoHrace = new JTextField("", 15);
        okno.add(jmenoHrace);
        JButton hotovo = new JButton("Hotovo");
        okno.add(hotovo);
        JLabel chybHlaska = new JLabel("!!!!Vyplňte prosím pole!!!!");
        okno.add(chybHlaska);
        chybHlaska.setVisible(false);
        okno.setVisible(true);
        hotovo.addActionListener((ActionEvent event) -> {
            if(!"".equals(jmenoHrace.getText())){
                hlavniVlakno.resume();
                okno.dispose();
            }else{
                chybHlaska.setVisible(true);
            }
        });
        hlavniVlakno.suspend();
        return jmenoHrace.getText();
    }
    
    /**
     * Uspí vlákno na kterém se volá metoda do té doby, dokud uživatel nezadá IP
     * adresu ke které se chce připojit
     * @return text zadaný uživatelem (IPAdresa)
     */
    public static String IPAdresa(){
        Thread hlavniVlakno = Thread.currentThread();
        setNimbus();
        JFrame okno = new JFrame();
        okno.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        okno.setLocationRelativeTo(null);
        okno.setSize(200,150);
        okno.setLayout(new FlowLayout());
        okno.add(new JLabel("IP ke které se chcete připojit:"));
        JTextField ipAdress = new JTextField("", 15);
        okno.add(ipAdress);
        JButton hotovo = new JButton("Hotovo");
        okno.add(hotovo);
        JLabel chybovaHlaska = new JLabel("!!!!Musíte vyplnit políčko!!!!");
        okno.add(chybovaHlaska);
        okno.setVisible(true);
        chybovaHlaska.setVisible(false);
        hotovo.addActionListener((ActionEvent event) ->{
            if(!ipAdress.equals("")){
                hlavniVlakno.resume();
                okno.dispose();
            }else{
                chybovaHlaska.setVisible(true);
            }
        });
        hlavniVlakno.suspend();
        return ipAdress.getText();
    }
    
    
    private static void setNimbus(){
        for(UIManager.LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()){
            if(lafi.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(lafi.getClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GraphicsIO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(GraphicsIO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(GraphicsIO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(GraphicsIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
