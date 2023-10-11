
package parte_3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MetodosP3 {
    static File archivo=null;
    static void CrearArchivoList(){ 
        try{
            archivo = new File ("P1ASM.lst"); //Nombro el archivo
            archivo.createNewFile();//Creo el archivo
            System.out.println("El archivo se creo correctamente: "+archivo.getName());
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst","rw"); //Encuentro el archivo y accedo para leer y escribir
            auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
            //escribe en el documento
            auxArchivo.writeBytes("TIPO,        "); 
            auxArchivo.writeBytes("VALOR,       ");
            auxArchivo.writeBytes("ETQ,         ");
            auxArchivo.writeBytes("CODOP,       ");
            auxArchivo.writeBytes("OPER \n");
        }//fin try
        catch(IOException ex){
            ex.printStackTrace();//Por si hay algun error al crear
            System.out.println("No se ha creado el archivo");
        }//fin catch
    }//fin crear
    
    static void LlenarList(ArrayList <Linea> AuxLineasCodigo) {
        int memoria=0, bytes=0, valorEqu=0;
        String valor=" ";
        CrearArchivoList();
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){
                if(!(auxiliar.getSize().equals(" "))){
                    auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                    if(auxiliar.getCodop().equals("ORG")){
                        System.out.println("Identifico ORG");
                        auxArchivo.writeBytes("DIR_INIC,        ");
                        memoria=Integer.parseInt(Parte_3.validarDireccion(auxiliar.getOperando()));
                    }
                    else if(auxiliar.getCodop().equals("EQU")){
                        auxArchivo.writeBytes("VALOR,       ");
                        valorEqu=Integer.parseInt(Parte_3.validarDireccion(auxiliar.getOperando()));
                    }
                    else{
                        if(memoria!=0){
                            auxArchivo.writeBytes("CONTLOC,       ");
                        }
                    }
                    if(valorEqu==0){
                        memoria=memoria+bytes;
                        valor= Integer.toString(memoria);
                    }
                    else{
                        valor=Integer.toString(valorEqu);
                        valorEqu=0;
                    }
                    //valor=valor.substring(0,2).concat(" ").concat(valor.substring(2, 4));
                    auxArchivo.writeBytes(valor+",       ");
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
                }
            }
        }catch(IOException ex){
            System.out.println("ERROR");
            ex.printStackTrace();
        }//fin catch
    }
}
