
package parte_3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MetodosP3 {
    static File archivo = new File("P1ASM.lst");
    static File archivo2 = new File("TABSIM.txt");
    
    static void CrearArchivoList(){ 
        try {
            // Eliminar el archivo si ya existe
            if (archivo.exists()) {
                archivo.delete();
            }

            // Crear el archivo nuevamente
            archivo.createNewFile();

            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst", "rw");
            auxArchivo.seek(auxArchivo.length());

            // Escribir en el documento
            auxArchivo.writeBytes("TIPO,        ");
            auxArchivo.writeBytes("VALOR,       ");
            auxArchivo.writeBytes("ETQ,         ");
            auxArchivo.writeBytes("CODOP,       ");
            auxArchivo.writeBytes("OPER \n");

            // Cerrar el archivo después de escribir
            auxArchivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("No se ha creado el archivo");
        }
    }//fin crear
    
    public static String sumarHexadecimal(String hexadecimal, int entero) {
        // Convertir el valor hexadecimal a entero
        int valorHexadecimal = Integer.parseInt(hexadecimal, 16);

        // Sumar el entero al valor hexadecimal
        int suma = valorHexadecimal + entero;

        // Obtener la representación hexadecimal del resultado
        String resultadoHexadecimal = Integer.toHexString(suma).toUpperCase();

        return resultadoHexadecimal;
    }
    
    static void LlenarList(ArrayList <Linea> AuxLineasCodigo) {
        int bytes=0;
        String valor="0";
        boolean equ=false;
        CrearArchivoList();
        Creartabsim();
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){
                if(!(auxiliar.getSize().equals(" "))){
                    auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                    if(auxiliar.getCodop().equals("ORG")){
                        if(!(auxiliar.getADDR().equals("ERROR"))){
                            auxArchivo.writeBytes("DIR_INIC,        ");
                            valor=Parte_3.validarDireccion(auxiliar.getOperando());
                            auxArchivo.writeBytes(sumarHexadecimal(valor,bytes)+",       ");
                        }
                        else{
                            System.out.println("NO SE PUEDE CALCULAR EL CONLOC PORQUE OPR DE ORG ES INCORRECTO");
                        }
                    }
                    else if(auxiliar.getCodop().equals("EQU")){
                        auxArchivo.writeBytes("VALOR,       ");
                        auxArchivo.writeBytes(Parte_3.validarDireccion(auxiliar.getOperando())+",       ");
                        auxiliar.setConloc(Parte_3.validarDireccion(auxiliar.getOperando()));
                        equ=true;
                    }
                    else{
                        if(!(valor.equals("0"))){
                            auxArchivo.writeBytes("CONTLOC,       ");
                            valor= sumarHexadecimal(valor,bytes);
                            auxArchivo.writeBytes(valor+",       ");
                            auxiliar.setConloc(valor);
                            String[] campos= auxiliar.getSize().split("\\s+");
                            bytes=Integer.parseInt(campos[0]);
                        }
                    }
                    if(!(valor.equals("0")) || equ){
                        if(auxiliar.getEtiqueta().equals(" ")){
                            auxArchivo.writeBytes("NULL,       ");
                        }
                        else{
                            auxArchivo.writeBytes(auxiliar.getEtiqueta()+",       ");
                        }
                        auxArchivo.writeBytes(auxiliar.getCodop()+",       ");
                        if(auxiliar.getOperando().equals(" ")){
                            auxArchivo.writeBytes("NULL,       ");
                        }
                        else{
                            auxArchivo.writeBytes(auxiliar.getOperando()+",       ");
                        }
                        auxArchivo.writeBytes("\n");
                    }
                    equ=false;
                }
            }
        }catch(IOException ex){
            System.out.println("ERROR");
            ex.printStackTrace();
        }//fin catch
    }    
    
    //metodo para crear la tabsim
    static void Creartabsim(){
        try {
            // Eliminar el archivo si ya existe
            if (archivo2.exists()) {
                archivo2.delete();
            }

            // Crear el archivo nuevamente
            archivo2.createNewFile();

            RandomAccessFile auxArchivo = new RandomAccessFile("TABSIM.txt", "rw");
            auxArchivo.seek(auxArchivo.length());

            // Escribir en el documento
            auxArchivo.writeBytes("TIPO            ");
            auxArchivo.writeBytes("Si       ");
            auxArchivo.writeBytes("Ti            ");
            auxArchivo.writeBytes("\n");

            // Cerrar el archivo después de escribir
            auxArchivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("No se ha creado el archivo");
        }
    }// fin metodo crear tabsim
    
    //funcion para llenar el tabsim
    static void LlenarTabsim(ArrayList <Linea> AuxLineasCodigo) {
        Creartabsim(); //mandamos a llamar primero la funcion de crear
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("TABSIM.txt","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){
                auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                if(!(auxiliar.getEtiqueta().equals(" "))&& !(auxiliar.getADDR().equals("ERROR"))){
                    if(auxiliar.getCodop().equals("EQU")){
                        auxArchivo.writeBytes("ABSOLUTA       ");
                    }
                    else{
                        auxArchivo.writeBytes("RELATIVA       ");
                    }
                    auxArchivo.writeBytes(auxiliar.getEtiqueta()+"       ");
                    auxArchivo.writeBytes(auxiliar.getConloc()+"       ");
                    auxArchivo.writeBytes("\n");
                } 
            }
                
        }catch(IOException ex){
            System.out.println("ERROR");
            ex.printStackTrace();
        }//fin catch
    } //fin del metodo llenar tabsim
    
}
