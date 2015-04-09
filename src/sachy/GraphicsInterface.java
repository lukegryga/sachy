
package sachy;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *Uživatelské rozhraní hry šachy
 * @author Lukáš Gryga
 */
public class GraphicsInterface{
    /**
     * Název hry ve tvaru: JménoHráče1(bílá) vs JménoHráče2(černá)
     */
    public final String titulek;
    
    private final ChessKordinator CHK = ChessKordinator.getChessKordinator();
    private final int PX=1;
    private final int PY=1;
    private final boolean serverHra;
    private final Sachovnice sachovnice;
    
    private JTextArea konzole;
    private JFrame okno;
    private JFrame oknoKonzole;
    private JPanel platno;
    private Button bVratTah;
    private int VELIKOSTPOLE = 50;
    private Figura vybranaFigura = null;
    private boolean jeMat = false;
    
    /**
     * Konsruktou vytvoří třídu reprezentující Grafické Rozhraní šachů
     * @param s - šachovnice, která se bude překreslovat
     * @param serverHra true, pokud se jedná o server hru, false v opačném případě
     * @param titulek text, který bude zobrazen v rámu okna
     */
    public GraphicsInterface(Sachovnice s, boolean serverHra, String titulek){
        sachovnice = s;
        this.serverHra = serverHra;
        this.titulek = titulek;
        initFrame();
    };
    
    /**
     * Nastaví velikost šachového pole
     * @param velikostPole velikost jednoho šachového políčka v pixelech
     */
    public void setVelikostPole(int velikostPole){
        VELIKOSTPOLE = velikostPole;
    }
    /**
     *Znázorní se políčko, na kterém stojí daná figura + znázorní se její možné tahy
     * @param f vybraná figura
     */
    public void setVybranaFigura(Figura f){
        vybranaFigura = f;
    }

    
    /**
     * Preskresli Sachovnici
     */
    public void prekresli(){
        platno.repaint();
    }
    
    private void initFrame(){
        platno = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                vykresliSachovnici(g);
                vykresliFigury(g);
                vykresliMoznostiVybFigury(g);
                if(jeMat){
                vykresliMat(g);
                }
            }
        };
        platno.setPreferredSize(new Dimension(200,200));
        okno = new JFrame();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setVisible(true);
        okno.setResizable(false);
        okno.setPreferredSize(new Dimension(407, 430));
        okno.setContentPane(platno);
        okno.getContentPane().setLayout(new FlowLayout());
        okno.pack();
        okno.setTitle(titulek);
        okno.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                oknoZavreno();
            }
        });
        platno.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mouseClicked1(e);
            }
        });
    }
    /**
     * Vytvoří okno, ve kterém se bude vypisovat obsah konzole
     */
    private void oknoKonzole(){
        oknoKonzole = new JFrame();
        //Nastaví umístění okna vedle hlavního okna (PH = pravý horní roh hlavního okna)
        Point PH =(Point) okno.getLocation().clone();
        PH.translate(okno.getWidth(), PX);
        oknoKonzole.setLocation(PH);
        //Nastaví výšku okna takovou jakou má hlavní okno
        oknoKonzole.setSize(300,okno.getHeight());
        oknoKonzole.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        oknoKonzole.setResizable(false);
        oknoKonzole.setTitle("Okno Konzole");
        oknoKonzole.setVisible(true);
        konzole = new JTextArea(20,20);
        JScrollPane scrollPane = new JScrollPane(konzole);
        scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scrollPane.createVerticalScrollBar();
        konzole.setLineWrap(true);
        konzole.setEditable(false);
        konzole.setFont(new Font("Conslas",Font.BOLD,14));
        oknoKonzole.add(konzole);
        
    }
    /**
     * Vypíše text do informační konzole
     * @param text text k zapsání
     */
    public void writeToKonzole(String text){
        konzole.append(text + "\n");
    }
    /**
     * Řekne GUI, že již má vykreslit MAT (Použít metodu: vykresliMat())
     */
    public void setMat(){
        jeMat = true;
    }
    
    private void vykresliSachovnici(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        boolean prepinatko = false;
        g2.setColor(Color.lightGray);
        for(int k=0;k<2;k++){
            for(int i=0; i<8;i++){
                for(int j=0;j<8;j++){
                    if(prepinatko)
                        g2.fillRect(j*VELIKOSTPOLE+PX, i*VELIKOSTPOLE+PY, VELIKOSTPOLE, VELIKOSTPOLE);
                    prepinatko = !prepinatko;
                }
                prepinatko = !prepinatko;
            }
            g.setColor(Color.darkGray);
        }
        g2.setColor(new Color(110, 80, 20));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(0, 0, VELIKOSTPOLE*8, VELIKOSTPOLE*8);
    }
    
    private void vykresliFigury(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Figura f;
        for(int i=7; i>=0;i--){
            for(int j=0;j<8;j++){
                f = sachovnice.vyberFiguru(new Point(j+1,i+1));
                if(f != null)
                    g2.drawImage(f.getImage(), j*VELIKOSTPOLE+PX, (7-i)*VELIKOSTPOLE+PY, VELIKOSTPOLE, VELIKOSTPOLE, null); // 7-i, aby se figury nevykreslovali obráceně, ale aby se černé figury vykreslili nahoře a bílé dole
            }
        }
    }
    
    private void vykresliMoznostiVybFigury(Graphics g){
        if(vybranaFigura != null){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.blue);
            Figura f = vybranaFigura;
            g2.drawRect((f.getPozice().x-1)*VELIKOSTPOLE+PX, (8-f.getPozice().y)*VELIKOSTPOLE+PY, VELIKOSTPOLE, VELIKOSTPOLE);
            g2.setColor(new Color(0,255,0,150));
            for(Point p : vybranaFigura.getMozneTahyOpt()){
                g2.fillOval((p.x-1)*VELIKOSTPOLE+PX + VELIKOSTPOLE/3, (8-p.y)*VELIKOSTPOLE+PY + VELIKOSTPOLE/3, VELIKOSTPOLE/3, VELIKOSTPOLE/3);
            }
        }
    }

    /**
     * Vykreslí přes šachovnici velkým červeně mat
     * @param g 
     */
    public void vykresliMat(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            Font font = new Font("Segoe UI", 1, 20);
            g2.setFont(font);
            g2.setColor(Color.RED);
            Point souradnice = sachovnice.getKral(!sachovnice.getHracNaTahu().isBarva()).getPozice();
            g2.drawRect((souradnice.x-1)*VELIKOSTPOLE+PX, (souradnice.y-1)*VELIKOSTPOLE+PY, VELIKOSTPOLE, VELIKOSTPOLE);
            g2.drawString("MAT", VELIKOSTPOLE*(souradnice.x-1)+PX+2, VELIKOSTPOLE*(souradnice.y)+PY-15);
    }
    /**
     * Překreslí plátno a pošle do proměnné vyrovnávací textovou reprezentaci kliknutého políčka
     * V případě servrové hry pošle reprezentaci i po síti protihráči
     * @param e 
     */
    public void mouseClicked1(MouseEvent e) {
        Point souradnice = e.getPoint();
        int x = souradnice.x/VELIKOSTPOLE + PX;
        int y = souradnice.y/VELIKOSTPOLE + PY;
        String gen=Sachovnice.pointNaSouradnice(new Point(x,9-y));
        CHK.vyrovnavaci = gen;
        try {
            Thread.sleep(CHK.TAKTOVANI + 30);
        } catch (InterruptedException ex) {
            Logger.getLogger(GraphicsInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(serverHra)
            CHK.posli(gen);
        platno.repaint();
    }

    /**
     * Zavře okno a uloží hru do souboru
     */
    public void oknoZavreno(){
        try {
            sachovnice.ulozHru();
            sachovnice.ulozVypisTahu();
        } catch (IOException ex) {
            Logger.getLogger(GraphicsInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
