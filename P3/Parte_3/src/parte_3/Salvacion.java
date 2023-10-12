
package parte_3;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Salvacion {
    static boolean encontrado;//Varible que utilizaremos para saber si el codop existe
    static int coincidencias=0;
    static NodoSalvacion auxSalvacion=null;
    
    static void BuscarCodop(ArrayList <Linea> AuxLineasCodigo){//Buscar es el codigo de operancion que se buscara en la salvacion
        for(Linea auxiliar : AuxLineasCodigo){
            String Buscar=auxiliar.getCodop();
            try{
                RandomAccessFile auxArchivo = new RandomAccessFile("Salvation_Tabop.txt","r");//r es para solo leer el archivo
                long cursorActual;//Para saber donde estamos
                cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
                FileReader leerArchivo = new FileReader("Salvation_Tabop.txt");//leeo el archivo del exel en txt
                String lecturaLinea;
                encontrado=false;
                coincidencias=0;
                IdentificacionDirectivas(auxiliar);
                //Aqui es donde empieza a leer por linea
                while(cursorActual!=auxArchivo.length() && encontrado==false){//mientras el lector no llegue al final del archivo
                    lecturaLinea = auxArchivo.readLine();//leeo la linea
                    cursorActual = auxArchivo.getFilePointer();
                    String[] campos = lecturaLinea.split("\\s+");//Separamos el txt por tabuladores
                    if(campos[0].equals(Buscar)){//En la primera palabra del txt estan los codop asi que si esa palabra es igual al CODOP buscado, lo encotnramos
                        auxSalvacion = new NodoSalvacion(campos[0],campos[1],campos[2],campos[3],campos[4], campos[5]); 
                        coincidencias++;
                        IdentificarADDR(auxiliar,auxSalvacion);
                    }//Fin comparacion con salvacion
                    else if((!campos[0].equals(Buscar))){
                        
                    }
                }//Fin del while
                if(!encontrado){
                    if(coincidencias==0){
                        auxiliar.setADDR("ADDR no aceptado");
                        System.out.println("CODOP "+ auxiliar.getCodop()+" NO EXISTE");
                    }
                    else if(coincidencias==1) {
                            IdentificacionREL(auxiliar,auxSalvacion);
                    }           
                    else if(coincidencias>1){
                        auxiliar.setADDR("OPR fuera de rango");
                    }
                }
                leerArchivo.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }//Fin for
    }//Fin leer y buscar en salvacion
    
    static void IdentificarADDR(Linea LinCod,NodoSalvacion AUX){
        if(LinCod.getOperando().equals(" ")){//Primer caso no hay operando
            if(AUX.Operando.equals("-")){//La estructura de operando que coincide
                LinCod.setADDR("INH");//ADDR correspondiente
                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                LinCod.setSize(AUX.byteTotal+" bytes");
                encontrado=true;//Ya lo encontro, puede terminar la busquerda
            }//Fin es inh
        }//Fin no hay operando
        else if(LinCod.getOperando().startsWith("#")){//Si el operando parece de tipo IMM
            if(AUX.Operando.equals("#opr8i")||AUX.Operando.equals("#opr16i")){//Puede ser de cualquiera de estas dos estructuras para coincidir
                if(Parte_3.IMM(LinCod.getOperando(),AUX.Operando)){//Evaluo si si es IMM
                    LinCod.setADDR("IMM"); 
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si es IMM
            }
        }
        else if(LinCod.getOperando().startsWith("[D")){//Si comienza de esta forma el operando
            if(AUX.Operando.equals("[D,xysp]")){//Si estructura en la salvacion debe coincidir con esta
                if(Parte_3.IdxD(LinCod.getOperando())){//Valido que este bien el operando
                    LinCod.setADDR("[D,IDX]");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin si es [D,IDX]
        }   
        else if(LinCod.getOperando().startsWith("[")){//Si comienza de esta forma el operando
            if(AUX.Operando.equals("[oprx16,xysp]")){//Si estructura en la salvacion debe coincidir con esta
                if(Parte_3.Idx2C(LinCod.getOperando())){//Evalua que el operando
                    LinCod.setADDR("[IDX2]");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin si es [IDX2]
        }    
        else if(LinCod.getOperando().contains(",")){//Si tiene una coma pero no los elementos anteriores
            if(AUX.Operando.equals("oprx0_xysp")){//Debe coincidir con esta estructura
                if(!(Parte_3.IDX(LinCod.getOperando()).equals("0"))){//Valido que el operando completo tenga la forma completa
                    LinCod.setADDR(Parte_3.IDX(LinCod.getOperando()));
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin estructura correspondiente
            else if(AUX.Operando.equals("oprx9,xysp")){//Si llego hasta esta estructura
                if(Parte_3.IDX1(LinCod.getOperando())){//Debe ser IDX1
                    LinCod.setADDR("IDX1");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Validar operando con de IDX
            }
            else if(AUX.Operando.equals("oprx16,xysp")){//Si llego hasta esta estructura
                if(Parte_3.IDX2(LinCod.getOperando())){//Evaluar si es IDX2
                    LinCod.setADDR("IDX2");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si es IDX2
            }
        }//Fin tiene comas   
        else if(Parte_3.ConvertirADecimal(LinCod.getOperando())!=-1){//Si es directamente un valor en cualquier base
            if(AUX.Operando.equals("opr8a")&&Parte_3.ConvertirADecimal(LinCod.getOperando())<256 
                && Parte_3.ConvertirADecimal(LinCod.getOperando())>=0){//Forma de DIR
                    LinCod.setADDR("DIR");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
            }//Fin es DIR
            else if(AUX.Operando.equals("opr16a")){//Estructura de EXT
               if(Parte_3.EXT(LinCod.getOperando())){
                    LinCod.setADDR("EXT");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
               }
            }//Fin es EXT
        }//Fin es DIR o EXT
        else if(Parte_3.validarEtiq(LinCod.getOperando())){//EXT con etiqueta
            if(AUX.Operando.equals("opr16a")){//Estructura correspondiente
                LinCod.setADDR("EXT");
                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                LinCod.setSize(AUX.byteTotal+" bytes");
                encontrado=true;
            }//Fin es EXT
        }
    }
    
    static void IdentificacionREL(Linea LinCod,NodoSalvacion AUX){
        if(AUX.AddrMode.equals("REL")){//Si el operando es REL, hay dos posibilidades
            if(AUX.Operando.equals("rel8")){//Si es de este tipo
                if(LinCod.getOperando().startsWith("$")){//Si es una direccion debe ser en hexadecimal
                    if(Parte_3.ValidarHexadecimal(LinCod.getOperando().substring(1))){//Evaluo hexa
                        LinCod.setADDR("REL (8b)");
                        LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                        LinCod.setSize(AUX.byteTotal+" bytes");
                        encontrado=true;
                    }//Fin si tiene una direcccion valida
                }//Si indica hexadecimal
                else if(Parte_3.validarEtiq(LinCod.getOperando())){//Cuando tiene una palabra
                    LinCod.setADDR("REL (8b)");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si coincide con una etiqueta
            }//Fin es rel8
            if(AUX.Operando.equals("rel16")){//Si es de este tipo
                if(LinCod.getOperando().startsWith("$")){//Para evaluar direccion
                    if(Parte_3.ValidarHexadecimal(LinCod.getOperando().substring(1))){//Validar dir en hex
                        LinCod.setADDR("REL (16b)");
                        LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                        LinCod.setSize(AUX.byteTotal+" bytes");
                        encontrado=true;
                    }//Fin es correca la direccion del operando
                }//Fin es un valor hexa
                else if(Parte_3.validarEtiq(LinCod.getOperando())){//Cuando el operando es una etiqueta
                    LinCod.setADDR("REL (16b)");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si existe la etiqueta
            }//Fin es rel16
        }//Fin es relativo
        else if(AUX.AddrMode.equals("REL(9-bit)")){//Si es de este tipo
            if(LinCod.getOperando().contains(",")){//Debe tener una copa
                String auxiliar[]=LinCod.getOperando().split(",");//Separar por comas
                if(auxiliar.length==2){//Debe tener solo dos bloques
                    if(auxiliar[0].equals("A") || auxiliar[0].equals("B") || auxiliar[0].equals("D") ||
                        auxiliar[0].equals("X") || auxiliar[0].equals("Y") || auxiliar[0].equals("SP")){//Estructuras validas para el primer bloque
                        if(auxiliar[1].startsWith("$")){//Si es una direccion
                            if(Parte_3.ValidarHexadecimal(auxiliar[1].substring(1))){//Validar hexa
                                LinCod.setADDR("REL (9b)");
                                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                                LinCod.setSize(AUX.byteTotal+" bytes");
                                encontrado=true;
                            }//Fin direccion correcta
                        }//Fin base hexa
                        else if(Parte_3.validarEtiq(auxiliar[1])){//Si la parte dos es una etiqueta
                            LinCod.setADDR("REL (9b)");
                            LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                            LinCod.setSize(AUX.byteTotal+" bytes");
                            encontrado=true;
                        }//Fin parte dos con etiqueta valida
                    }//Fin estructura del primer bloque
                }//Fin si son dos bloques
                        }//Fin si contiene una coma
        }//Fin rel(9-bit)
        else{
            System.out.println("CODOP O ADDR NO RECONOCIDO");
            System.out.println("CODOP= "+AUX.CODOP);
            System.out.println("OPERANDO= "+LinCod.getOperando());
            System.out.println("Forma del Operando= "+AUX.Operando);
            LinCod.setADDR( "OPR fuera de rango");
        }//Fin codop que no vamos a evaluar
    }
    
    static void IdentificacionDirectivas(Linea LinCod){
        String auxiliar = LinCod.getCodop();
        String tamPala = " ";
        if(auxiliar.contains(".")){
            String[] campos = auxiliar.split("\\.");
            auxiliar=campos[0];
            tamPala = campos[1];
        }
        encontrado=true;
        switch(auxiliar){
            case "ORG":
                LinCod.setADDR("DIRECT");
                LinCod.setSize("0 bytes");
                LinCod.setPorCalcular("0 bytes");
            break;
            case "END":
                LinCod.setADDR("DIRECT");
                LinCod.setSize("0 bytes");
                LinCod.setPorCalcular("0 bytes");
            break;
            case "EQU":
                if(!(LinCod.getEtiqueta().equals(" ")) && Parte_3.ConvertirADecimal(LinCod.getOperando())!=-1){
                    LinCod.setADDR("DIRECT");
                    LinCod.setSize("0 bytes");
                    LinCod.setPorCalcular("0 bytes");
                }
                else{
                    LinCod.setADDR("OPR fuera de rango");
                    System.out.println("ERROR EQU NO tiene etiqueta o el operando es incorrecto");
                    encontrado=false;
                }
            break;
            case "DC":
                int tam=0;
                if(!(LinCod.getOperando().equals(" "))){
                    if(LinCod.getOperando().contains(",")){
                        String [] partOpr = LinCod.getOperando().split(",");
                        tam=partOpr.length;
                    }
                    if(tamPala.equals("B")){
                        if(tam!=0){
                            LinCod.setSize(String.valueOf(tam)+" bytes");
                            LinCod.setPorCalcular("0 bytes");
                        }
                        else{
                            LinCod.setSize("1 bytes");
                            LinCod.setPorCalcular("0 bytes");
                        }
                            LinCod.setADDR("DIRECT");
                    }else if(tamPala.equals("W")){
                        if(tam!=0){
                            LinCod.setSize(String.valueOf(tam*2)+" bytes");
                            LinCod.setPorCalcular("0 bytes");
                        }
                        else{
                        
                        }
                        LinCod.setADDR("DIRECT");
                    }else{
                        System.out.println("ERROR DC. el tamano de la palabra es incorrecto");
                        encontrado = false;
                    }
                }
            break;
            case "DS":
                String opr = LinCod.getOperando().toString();
                if(opr.matches("\\d+")){
                    if(tamPala.equals("B")){
                        LinCod.setADDR("DIRECT");
                        LinCod.setSize(LinCod.getOperando()+" bytes");
                        LinCod.setPorCalcular("0 bytes");
                    }
                    else if(tamPala.equals("W")){
                        LinCod.setADDR("DIRECT");
                        LinCod.setSize(String.valueOf(Integer.parseInt(LinCod.getOperando())*2)+" bytes");
                        LinCod.setPorCalcular("0 bytes");
                    }
                    else{
                        System.out.println("ERROR DS. el tamano de la palabra es incorrecto");
                        encontrado = false;
                    }
                }
                else{
                    LinCod.setADDR("OPR fuera de rango");
                    encontrado = false;
                }
            break;
            default:
                encontrado=false;
            break;
        }
    }
}//Fin clase
