
package sachy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.misc.IOUtils;

/**
 *Uživatelské rozhraní hry šachy
 * @author Lukáš Gryga
 */
public class GraphicsInterface implements MouseListener {
    
    private final ChessKordinator CHK = ChessKordinator.getChessKordinator();
    private final int PX=1;
    private final int PY=1;
    
    private JFrame okno;
    private JPanel platno;
    private Sachovnice sachovnice;
    private int VELIKOSTPOLE = 50;
    private Point vybranePolicko = new Point(9,9);
    
    public static void main(String[] args){
        Sachovnice s = new Sachovnice();
        s.novaHra();
        GraphicsInterface GUI = new GraphicsInterface(s);
        
    }
    
    public GraphicsInterface(Sachovnice s){
        sachovnice = s;
        initFrame();
    };
    
    /**
     * Nastaví velikost šachového pole
     * @param velikostPole velikost jednoho šachového políčka v pixelech
     */
    public void setVelikostPole(int velikostPole){
        VELIKOSTPOLE = velikostPole;
    }
    
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
            }
        };
        platno.setPreferredSize(new Dimension(200,200));
        okno = new JFrame();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setVisible(true);
        okno.setPreferredSize(new Dimension(500, 500));
        okno.setContentPane(platno);
        okno.pack();
        platno.addMouseListener(this);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Point souradnice = e.getPoint();
        int x = souradnice.x/VELIKOSTPOLE + PX;
        int y = souradnice.y/VELIKOSTPOLE + PY;
        try{
            try {
                System.in.read(Sachovnice.pointNaSouradnice(new Point(x,8-y)).getBytes());
            } catch (IOException ex) {
                Logger.getLogger(GraphicsInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(IllegalArgumentException ex){}
        
    }

    @Override
    public void mousePressed(MouseEvent e) {    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    
}
