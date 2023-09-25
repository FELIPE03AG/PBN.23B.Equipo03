
package parte_2;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Salvacion {
    static boolean encontrado =false;
    static NodoSalvacion primer=null,Ultimo=null;
    
    static void BuscarCodop(String Buscar){
        NodoSalvacion AuxPrimer=null, AuxUltimo=null,Nuevo=null;
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("Salvation_Tabop.txt","r");//r es para solo leer el archivo
            long cursorActual;//Para saber donde estamos
            cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
            FileReader leerArchivo = new FileReader("Salvation_Tabop.txt");//leeo el archivo
            String lecturaLinea;
            
            //Aqui es donde empieza a leer por linea
            while(cursorActual!=auxArchivo.length() && encontrado==false){//mientras el lector no llegue al final del archivo
                lecturaLinea = auxArchivo.readLine();//leeo la linea
                cursorActual = auxArchivo.getFilePointer();
                String[] campos = lecturaLinea.split("\\s+");
                if(campos[0].equals(Buscar)){
                    Nuevo = new NodoSalvacion(campos[0],campos[1],campos[2],campos[3],campos[4], campos[5],null); 
                    if(AuxPrimer==null){
                        AuxPrimer=Nuevo;
                    }
                    else{
                        AuxUltimo.siguiente=Nuevo;
                    }
                    AuxUltimo=Nuevo;
                }
                else if(AuxPrimer!=null && (!campos[0].equals(Buscar))){
                    encontrado=true;
                }
            }//Fin del while
            leerArchivo.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }//Fin leer y buscar en salvacion
    
    //METODO PARA IDENTIFICAR EL ADDR DESDE EL EXCEL
    static void GuardarADDR(Linea LinCod){
        boolean fin=false;
        while(!fin){
            LinCod.setCodop(LinCod.getCodop().toUpperCase());
            BuscarCodop(LinCod.getCodop());
            if(!encontrado){
                if(LinCod.getCodop().equals("ORG")){
                    LinCod.setADDR("DIRECT");
                }
                else if(LinCod.getCodop().equals("END")){
                    LinCod.setADDR("DIRECT");
                }
            }
            else{
                encontrado=false;
            }
            if(LinCod==Parte_2.FinLinCod){
                fin=true;
            }
            else{
                LinCod=LinCod.getSiguiente();
            }
        }

    }
}
