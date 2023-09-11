/*
    UNIVERCIDAD DE GUADALAJARA
    CENTRO UNIVERSITARIO DE LOS ALTOS
    INGENIERÍA EN COMPUTACIÓN
    PROGRAMACIÓN DE BAJO NIVEL
    PROYECTO INTEGRADOR
            EQUIPO 3
*/

package proyectointegrador;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ProyectoIntegrador {
    
    static Linea LinCod=null;
    
    static void Leer(){
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.asm","r");//r es para solo leer el archivo
            long cursorActual;//Para saber donde estamos
            cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
            FileReader leerArchivo = new FileReader("P1ASM.asm");//leeo el archivo
            String lecturaLinea;
            
            //Aqui es donde empieza a leer por linea
            while(cursorActual!=auxArchivo.length()){//mientras el lector no llegue al final del archivo
                lecturaLinea = auxArchivo.readLine();//leeo la linea
                cursorActual = auxArchivo.getFilePointer();
                String[] campos = lecturaLinea.split("\\s+");
                for(int i=0; i<campos.length;i++){
                    if(campos[0].equals(";")){//Si la linea empieza con ; lo identifica como comentario
                        System.out.println("COMENTARIO");
                        i=campos.length;
                    }
                }//fin for, reccorre lineas
                
                if(campos[0].equals("")){//Cuando empieza con tabulador o un espacio, no hay etiqueta
                    System.out.println("ETIQUETA = null");
                    if(campos.length==3){
                        System.out.println("CODOP = "+campos[1]);
                        System.out.println("OPERANDO = "+campos[2]);
                    }
                    else if(campos.length==2){
                        System.out.println("CODOP = "+campos[1]);
                        System.out.println("OPERANDO = null");
                    }
                }
                else{
                    if(campos.length==3){
                        System.out.println("ETIQUETA = "+ campos[0]);
                        System.out.println("CODOP = "+campos[1]);
                        System.out.println("OPERANDO = "+campos[2]);
                    }
                    else if(campos.length==2){
                        System.out.println("ETIQUETA = "+ campos[0]);
                        System.out.println("CODOP = "+campos[1]);
                        System.out.println("OPERANDO = null");
                    }
                }
                System.out.println("");
                //Validar el end
            }//Fin del while
            leerArchivo.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Leer();//Llamo el metodo
    }
    
}
