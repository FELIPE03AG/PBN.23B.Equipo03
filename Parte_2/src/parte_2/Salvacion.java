
package parte_2;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Salvacion {
    static boolean encontrado =false;
    static NodoSalvacion primer=null,Ultimo=null,Nuevo=null;
    
    static void BuscarCodop(String Buscar){
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
                    if(primer==null){
                        primer=Nuevo;
                    }
                    else{
                        Ultimo.siguiente=Nuevo;
                    }
                    Ultimo=Nuevo;
                }
                else if(primer!=null && (!campos[0].equals(Buscar))){
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
            BuscarCodop(LinCod.getCodop());
            if(!encontrado){
                if(LinCod.getCodop().equals("ORG")){
                    if(Parte_2.ValidarHexadecimal(LinCod.getOperando().substring(1)) 
                            && LinCod.getOperando().substring(1).length()==4){
                        LinCod.setADDR("DIRECT");
                    }
                }
                else if(LinCod.getCodop().equals("END") || LinCod.getCodop().equals("EQU")){
                    LinCod.setADDR("DIRECT");
                }
            }
            else{
                if(primer==Ultimo){
                    if(primer.Operando.equals("-")){
                        if(LinCod.getOperando().equals(" ")){
                            LinCod.setADDR(primer.AddrMode);
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("IDX")){
                        if(!(Parte_2.IDX(LinCod.getOperando(),primer.Operando).equals("-1"))){
                            LinCod.setADDR(Parte_2.IDX(LinCod.getOperando(),primer.Operando));
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("IMM")){
                        if(Parte_2.IMM(LinCod.getOperando(), primer.Operando)){
                            LinCod.setADDR("IMM");
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("REL")){
                        //FALTA VALIDAR REL
                        LinCod.setADDR("REL");
                    }
                    else if(primer.AddrMode.equals("REL(9-bit)")){
                        LinCod.setADDR("REL(9b)");
                    }
                    else{
                        System.out.println("ADDR NO RECONOCIDO");
                        System.out.println("CODOP= "+primer.CODOP);
                        System.out.println("OPERANDO= "+LinCod.getOperando());
                        System.out.println("Forma del Operando= "+primer.Operando);
                        LinCod.setADDR( "OPR fuera de rango");
                    }
                }
                encontrado=false;
                primer=null;
                Ultimo=null;
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
