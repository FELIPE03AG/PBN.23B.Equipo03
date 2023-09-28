
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
                else if(LinCod.getCodop().equals("END") || LinCod.getCodop().equals("EQU") || 
                        LinCod.getCodop().equals("DS.B")){
                    LinCod.setADDR("DIRECT");
                }
            }
            else{
                if(primer==Ultimo){
                    if(primer.Operando.equals("-")){
                        if(LinCod.getOperando().equals(" ")){
                            LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                            LinCod.setSize(primer.byteTotal+" bytes");
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("IDX")){
                        if(!(Parte_2.IDX(LinCod.getOperando()).equals("-1"))){
                            LinCod.setADDR(Parte_2.IDX(LinCod.getOperando()));
                            LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                            LinCod.setSize(primer.byteTotal+" bytes");
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("IMM")){
                        if(Parte_2.IMM(LinCod.getOperando(), primer.Operando)){
                            LinCod.setADDR("IMM");
                            LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                            LinCod.setSize(primer.byteTotal+" bytes");
                        }
                        else{
                            LinCod.setADDR( "OPR fuera de rango");
                        }
                    }
                    else if(primer.AddrMode.equals("REL")){
                        if(primer.Operando.equals("rel8")){
                            if(LinCod.getOperando().startsWith("$")){
                                if(Parte_2.ValidarHexadecimal(LinCod.getOperando().substring(1))){
                                    LinCod.setADDR("REL (8b)");
                                    LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                    LinCod.setSize(primer.byteTotal+" bytes");
                                }
                            }
                            else if(Parte_2.BuscarEtiqueta(LinCod.getOperando())){
                                LinCod.setADDR("REL (8b)");
                                LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                LinCod.setSize(primer.byteTotal+" bytes");
                            }
                        }
                        
                        if(primer.Operando.equals("rel16")){
                            if(LinCod.getOperando().startsWith("$")){
                                if(Parte_2.ValidarHexadecimal(LinCod.getOperando().substring(1))){
                                    LinCod.setADDR("REL (16b)");
                                    LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                    LinCod.setSize(primer.byteTotal+" bytes");
                                }
                            }
                            else if(Parte_2.BuscarEtiqueta(LinCod.getOperando())){
                                LinCod.setADDR("REL (16b)");
                                LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                LinCod.setSize(primer.byteTotal+" bytes");
                            }
                        }
                    }
                    else if(primer.AddrMode.equals("REL(9-bit)")){
                        if(LinCod.getOperando().contains(",")){
                            String auxiliar[]=LinCod.getOperando().split(",");
                            if(auxiliar.length==2){
                            auxiliar[0]=auxiliar[0].toUpperCase();
                            if(auxiliar[0].equals("A") || auxiliar[0].equals("B") || auxiliar[0].equals("D") ||
                                auxiliar[0].equals("X") || auxiliar[0].equals("Y") || auxiliar[0].equals("SP")){
                                    if(auxiliar[1].startsWith("$")){
                                        if(Parte_2.ValidarHexadecimal(auxiliar[1].substring(1))){
                                            LinCod.setADDR("REL (9b)");
                                            LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                            LinCod.setSize(primer.byteTotal+" bytes");
                                        }
                                    }
                                    else if(Parte_2.BuscarEtiqueta(auxiliar[1])){
                                        LinCod.setADDR("REL (9b)");
                                        LinCod.setPorCalcular(primer.byteCalcular+ " bytes");
                                        LinCod.setSize(primer.byteTotal+" bytes");
                                    }
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("CODOP O ADDR NO RECONOCIDO");
                        System.out.println("CODOP= "+primer.CODOP);
                        System.out.println("OPERANDO= "+LinCod.getOperando());
                        System.out.println("Forma del Operando= "+primer.Operando);
                        LinCod.setADDR( "OPR fuera de rango");
                    }
                }
                else{
                    NodoSalvacion AUX = primer;
                    boolean salir=false,siguiente;
                    while(salir==false){
                        siguiente=false;
                        if(LinCod.getOperando().equals(" ")){
                            if(AUX.Operando.equals("-")){
                                LinCod.setADDR("INH");
                                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                LinCod.setSize(AUX.byteTotal+" bytes");
                                salir=true;
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(LinCod.getOperando().startsWith("#")){
                            if(AUX.Operando.equals("#opr8i")||AUX.Operando.equals("#opr16i")){
                                if(Parte_2.IMM(LinCod.getOperando(),AUX.Operando)){
                                    LinCod.setADDR("IMM"); 
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(LinCod.getOperando().startsWith("[D")){
                            if(AUX.Operando.equals("[D,xysp]")){
                                if(Parte_2.IdxD(LinCod.getOperando())){
                                    LinCod.setADDR("[D,IDX]");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(LinCod.getOperando().startsWith("[")){
                            if(AUX.Operando.equals("[oprx16,xysp]")){
                                if(Parte_2.Idx2C(LinCod.getOperando())){
                                    LinCod.setADDR("[IDX2]");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(LinCod.getOperando().contains(",")){
                            if(AUX.Operando.equals("oprx0_xysp")){
                                if(!(Parte_2.IDX(LinCod.getOperando()).equals("0"))){
                                    LinCod.setADDR(Parte_2.IDX(LinCod.getOperando()));
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    siguiente=true;
                                }
                            }
                            else if(AUX.Operando.equals("oprx9,xysp")){
                                if(Parte_2.IDX1(LinCod.getOperando())){
                                    LinCod.setADDR("IDX1");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    siguiente=true;
                                }
                            }
                            else if(AUX.Operando.equals("oprx16,xysp")){
                                if(Parte_2.IDX2(LinCod.getOperando())){
                                    LinCod.setADDR("IDX2");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(Parte_2.ConvertirADecimal(LinCod.getOperando())!=-1){
                            if(AUX.Operando.equals("opr8a")&&Parte_2.ConvertirADecimal(LinCod.getOperando())<256){
                                if(Parte_2.DIR(LinCod.getOperando())){
                                    LinCod.setADDR("DIR");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else if(AUX.Operando.equals("opr16a")){
                                if(Parte_2.EXT(LinCod.getOperando())){
                                    LinCod.setADDR("EXT");
                                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                    LinCod.setSize(AUX.byteTotal+" bytes");
                                    salir=true;
                                }
                                else{
                                    AUX=Ultimo;
                                }
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else if(Parte_2.BuscarEtiqueta(LinCod.getOperando())){
                            if(AUX.Operando.equals("opr16a")){
                                LinCod.setADDR("EXT");
                                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                LinCod.setSize(AUX.byteTotal+" bytes");
                                salir=true;
                            }
                            else{
                                siguiente=true;
                            }
                        }
                        else{
                            AUX=Ultimo;
                        }
                    
                        if(AUX==Ultimo && salir==false){
                            LinCod.setADDR( "OPR fuera de rango");
                            salir=true;
                        }
                        else if(siguiente){
                            AUX=AUX.siguiente;
                        }
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