

package sachy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *Třída sloužící k jenoduché práci s textovými soubory
 * @author luke
 */
public class Database {
    
    /**
     *Uloží textový řetězec na první volný řádek v .txt souboru, pokud soubor neexistuje, vytvoří ho
     * @param zapsat Textový řetězec, který chcete zapsat do .txt souboru
     * @param cesta Cesta k složce, kde je uložený txt soubor, do kterého chcete zapisovat
     * @param soubor .txt soubor do kterého chcete zapisovat
     * @param pripsat true k souboru připisuje zato false soubor přepíše
     * @throws IOException
     */
    public void uloz(String zapsat,String cesta,String soubor, boolean pripsat)throws IOException{
        File f = new File(cesta);
        if(!f.exists())
            f.mkdirs();
        if(pripsat){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cesta + "/" + soubor,true))) {
                bw.write(zapsat);
                bw.newLine();
                bw.flush();
            }
        }else{
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cesta + "/" + soubor))) {
                bw.write(zapsat);
                bw.newLine();
                bw.flush();
            }
        }
    }
    
    /**
     *Načte všechny řádky v souboru
     * @param cestaSoubor celá cesta k souboru, včetně názvu souboru, do které chcete zapisovat
     * @return Pole, které tvoří text na jednotlivých řádcích
     * @throws IOException
     */
    public String[] nacti(String cestaSoubor)throws IOException{
        int i = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(cestaSoubor))) {
            while(br.readLine()!=null){
                i++;
            }
        br.close();
        }
        String[] nacteno = new String[i];
        try(BufferedReader br = new BufferedReader(new FileReader(cestaSoubor))) {
            for(int j=0;j<i;j++){
                nacteno[j] = br.readLine();
            }
            br.close();
        }
        return nacteno;
    }
    
    /**
     * Vymaže zadaný řádek v souboru
     * @param cestaSoubor celá cesta k souboru, včetně názvu souboru, do které chcete zapisovat
     * @param index řádek, který chcete vymazat, začínaje 0
     * @return vrací false, pokud takový řádek v souboru neexistuje, v opačném případě vrátí true
     * @throws IOException
     */
    public boolean vymazRadek(String cestaSoubor, int index)throws IOException{
        String[] nacteneRadky = nacti(cestaSoubor);
        if(index < nacteneRadky.length){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(cestaSoubor))){
                for(int i=0;i<nacteneRadky.length;i++){
                    if(i!=index){
                        bw.write(nacteneRadky[i]);
                        bw.newLine();
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }
    
}
