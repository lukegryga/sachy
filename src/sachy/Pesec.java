/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachy;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Lukáš
 */
public class Pesec extends Figura{
    
    /**
     *
     */
    public static final int hodnotaFigury = 1;

    /**
     *
     * @param pozice
     * @param barva
     */
    public Pesec(Point pozice, boolean barva) {
        super(pozice, barva);
    }
    
    /**
     *
     * @return
     */
    @Override
    public ArrayList<Point> getMozneTahy(){
        mozneTahy.clear();
        if(barva == true){                                                      //Bílý pěšec
            if(Sachovnice.existujeSouradnice(pozice.x, pozice.y+1)){            //Pokud je na sachovnice souradnice o 1 nahoru
                if(sachovnice.jeVolno(pozice.x, pozice.y+1) == -1){             //Pokud na souradnici o 1 nahoru nic nestojí
                    mozneTahy.add(new Point(pozice.x, pozice.y+1));
                    if(pozice.y == 2 && sachovnice.jeVolno(pozice.x, pozice.y+2) == -1){    //Pokud se pěšec ještě nepohnul a je o 2 dopředu volno
                        mozneTahy.add(new Point(pozice.x, pozice.y+2));
                    }
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x-1, pozice.y+1)){          //Pokud existuje pole vlevo nahoru
                if(sachovnice.jeVolno(pozice.x-1, pozice.y+1) == 0){            //Pokud vlevo nahoře je soupeřova figura
                    mozneTahy.add(new Point(pozice.x-1, pozice.y+1));
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x+1, pozice.y+1)){          //Pokud existuje pole vpravo nahoru
                if(sachovnice.jeVolno(pozice.x+1, pozice.y+1) == 0){            //Pokud v pravo nahoře je soupeřova figura
                    mozneTahy.add(new Point(pozice.x+1, pozice.y+1));
                }
            }
        }else{                                                                  //Černý pěšec
            if(Sachovnice.existujeSouradnice(pozice.x, pozice.y-1)){            //Pokud je na sachovnice souradnice o 1 dolu
                if(sachovnice.jeVolno(pozice.x, pozice.y-1) == -1){             //Pokud na souradnici o 1 nahoru nic nestojí
                    mozneTahy.add(new Point(pozice.x, pozice.y-1));
                    if(pozice.y == 7 && sachovnice.jeVolno(pozice.x, pozice.y-2) == -1){    //Pokud se pěšec ještě nepohnul a je o 2 dolu volno
                        mozneTahy.add(new Point(pozice.x, pozice.y-2));
                    }
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x-1, pozice.y-1)){          //Pokud existuje pole vlevo dolu
                if(sachovnice.jeVolno(pozice.x-1, pozice.y-1) == 1){            //Pokud vlevo dole je soupeřova figura
                    mozneTahy.add(new Point(pozice.x-1, pozice.y-1));
                }
            }
            if(Sachovnice.existujeSouradnice(pozice.x+1, pozice.y-1)){          //Pokud existuje pole vpravo nahoru
                if(sachovnice.jeVolno(pozice.x+1, pozice.y-1) == 1){            //Pokud v pravo dole je soupeřova figura
                    mozneTahy.add(new Point(pozice.x+1, pozice.y-1));
                }
            }
        }
        return mozneTahy;
    }
    
    @Override
    public String toString(){
        return "Pešec:" + Sachovnice.pointNaSouradnice(pozice);
    }
}
