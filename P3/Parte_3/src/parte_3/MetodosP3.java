
package parte_3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MetodosP3 {
    static File archivo = new File("P1ASM.lst");
    static void CrearArchivoList(){ 
        try {
            // Eliminar el archivo si ya existe
            if (archivo.exists()) {
                archivo.delete();
                System.out.println("El archivo se va a sobreescribir");
            }

            // Crear el archivo nuevamente
            archivo.createNewFile();
            System.out.println("El archivo se creo correctamente: " + archivo.getName());

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
        String valor="0",valorEqu="0";
        CrearArchivoList();
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){
                if(!(auxiliar.getSize().equals(" "))){
                    auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                    if(auxiliar.getCodop().equals("ORG")){
                        System.out.println("Identifico ORG");
                        auxArchivo.writeBytes("DIR_INIC,        ");
                        valor=Parte_3.validarDireccion(auxiliar.getOperando());
                        auxArchivo.writeBytes(sumarHexadecimal(valor,bytes)+",       ");
                    }
                    else if(auxiliar.getCodop().equals("EQU")){
                        auxArchivo.writeBytes("VALOR,       ");
                        auxArchivo.writeBytes(Parte_3.validarDireccion(auxiliar.getOperando())+",       ");
                    }
                    else{
                        if(!(valor.equals("0"))){
                            auxArchivo.writeBytes("CONTLOC,       ");
                            valor= sumarHexadecimal(valor,bytes);
                            auxArchivo.writeBytes(valor+",       ");
                        }
                    }
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
                    String[] campos= auxiliar.getSize().split("\\s+");
                    bytes=Integer.parseInt(campos[0]);
                    System.out.println(bytes);
                }
            }
        }catch(IOException ex){
            System.out.println("ERROR");
            ex.printStackTrace();
        }//fin catch
    }
}
