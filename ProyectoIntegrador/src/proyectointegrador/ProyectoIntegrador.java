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
    
    static Linea LinCod=null; //Va aguardar las variables
    static int aux;
    static boolean bandera;
    
    //METODO PARA EVALUAR ETIQUETA
    static boolean validarEtiq(String x){
        
        if(x == null){
            return true;
        }else{
            for(int i = 0; i <= x.length() - 1; i++){
                char c = x.charAt(i);
                if(Character.isLetter(c) && i == 0){
                    return true;
                }else if(Character.isLetterOrDigit(c) || c == '_'){
                    return true;
                }else{
                    return false;
                }
            }
        }
    //METODO PARA EVALUAR CODOP
    
    //METODO PARA LEER EL ASM
    static void Leer(){
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.asm","r");//r es para solo leer el archivo
            long cursorActual;//Para saber donde estamos en el asm
            cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
            FileReader leerArchivo = new FileReader("P1ASM.asm");//leo el archivo
            String lecturaLinea; //Guarda cada linea
            
            //Aqui es donde empieza a leer por linea
            while(cursorActual!=auxArchivo.length()){//mientras el lector no llegue al final del archivo
                lecturaLinea = auxArchivo.readLine();//leo la linea
                cursorActual = auxArchivo.getFilePointer();
                String[] campos = lecturaLinea.split("\\s+");//Separa por bloques segun cada espacios o tabulación
                LinCod = new Linea(null,null,null); //Inicializo valores
                //For para guardar la primera cada bloque en el arreglo campos
                
                aux=campos.length;
                bandera=true;
                switch (aux){
                    //CASO: SOLO CODOP
                    case 1: 
                        LinCod.setCodop(campos[0]);
                        if(LinCod.getCodop().equals("END")){
                            cursorActual=auxArchivo.length();
                                
                        }
                    break;
                    
                    //CASO: ETIQUETA, CODOP || CODOP, OPERANDO || CODOP
                    case 2:
                        //CASO: CODOP
                        if(campos[0].equals("")){//Cuando empieza con tabulador o un espacio, no hay etiqueta
                            LinCod.setCodop(campos[1]);//Falta caso end
                            if(LinCod.getCodop().equals("END")){
                            cursorActual=auxArchivo.length();
                                
                        }
                        }
                        else{
                            //CASO ETIQUETA, CODOP
                            LinCod.setEtiqueta(campos[0]);
                            LinCod.setCodop(campos[1]);
                            //CASO CODOP, OPERANDO
                        }
                        
                    break;
                    
                    case 3:
                        //CASO: CODOP, OPERANDO
                        if(campos[0].equals("")){//Cuando empieza con tabulador o un espacio, no hay etiqueta
                            LinCod.setCodop(campos[1]);
                            LinCod.setOperando(campos[2]);
                        }
                        else{
                            LinCod.setEtiqueta(campos[0]);
                            LinCod.setCodop(campos[1]);
                            LinCod.setOperando(campos[2]);
                        }
                    break;
                    
                    default: 
                        //IDENTIFICACION CASO: COMENTARIO
                        if(campos[0].equals(";")){//Si la linea empieza con ; lo identifica como comentario
                            System.out.println("COMENTARIO");
                            bandera=false;
                        }
                        else{
                            System.out.println("ERROR");
                        }
                }//Fin casos posibles
                
                if(bandera){
                    if(validarEtiq(LinCod.getEtiqueta()) == true){
                        System.out.println("ETIQUETA = "+LinCod.getEtiqueta());
                    }else{
                        System.out.println("Error");
                    }
                    System.out.println("CODOP = "+LinCod.getCodop());
                    System.out.println("OPERANDO = "+LinCod.getOperando());
                    LinCod.setEtiqueta("null");
                    LinCod.setCodop("null");
                    LinCod.setOperando("null");
                }//Fin no es un comentario
                
                System.out.println("");
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
