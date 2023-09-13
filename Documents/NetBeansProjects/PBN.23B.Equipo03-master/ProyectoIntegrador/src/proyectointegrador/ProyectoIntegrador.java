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
    static boolean validarEtiq(String etiqueta){
        boolean banEtiq=false;
        if(etiqueta.length()<=8){
            for(int i = 0; i < etiqueta.length(); i++){
                char c = etiqueta.charAt(i);
                if(Character.isLetter(c) && i == 0){
                    banEtiq =true;
                }
                else if(Character.isLetterOrDigit(c) || c == '_' || (c==':' && i==etiqueta.length()-1)){
                    banEtiq =true;
                }
                else{
                    banEtiq=false;
                    i=etiqueta.length();
                }
            }
        }   
        return banEtiq;
    }//Fin validar etiqueta
    
    // Metodo para evaluar el comentario
    static boolean validarComen(String Comentario){
        boolean bancomentario = false;
        if (Comentario == null){
            return true;
        }
        
        if (Comentario.length()<=80){
            bancomentario = true;
        } else{
            bancomentario = false;
        }
        return bancomentario;
        
        
    }//Final del metodo para evaluar el comentario
    
    //METODO PARA EVALUAR CODOP
    static boolean validarCodop(String codop){
        boolean banCodop=false;
                if(codop.length()<=5){
            for(int i = 0; i < codop.length(); i++){
                char c = codop.charAt(i);
                if(Character.isLetter(c) && i == 0){
                    banCodop =true;
                }
                else if(Character.isLetter(c) ||  c=='.' ){
                    banCodop =true;
                }
                else{
                    banCodop=false;
                    i=codop.length();
                }
            }
        } 
        
       
        return banCodop;
    }//FINAL DE METODO PARA EVALUAR CODOP
    
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
                
                aux=campos.length;
                bandera=true;
                //VALIDADACIÓN DE CASOS
                switch (aux){
                    //CASO: ETIQUETA, CODOP || CODOP, OPERANDO || CODOP
                    case 2:
                        //CASO: CODOP
                        if(campos[0].equals("")){//Cuando empieza con tabulador o un espacio, no hay etiqueta
                            if(validarCodop(campos[1])){
                                LinCod.setCodop(campos[1]);
                                //CASO END
                                if(LinCod.getCodop().equals("END")){
                                    cursorActual=auxArchivo.length();
                                }
                            }
                            else{
                                System.out.println("ERROR");
                            }
                        }
                        else{
                            //CASO ETIQUETA, CODOP
                            if(validarEtiq(campos[0])&&validarCodop(campos[1])){
                                LinCod.setEtiqueta(campos[0]);
                                LinCod.setCodop(campos[1]);
                            }
                            else{
                                System.out.println("ERROR");
                            }
                        }
                        
                    break;
                    
                    case 3:
                        //CASO: CODOP, OPERANDO
                        if(campos[0].equals("")){//Cuando empieza con tabulador o un espacio, no hay etiqueta
                            if(validarCodop(campos[1])){
                                LinCod.setCodop(campos[1]);
                                LinCod.setOperando(campos[2]);
                            }
                            else{
                                System.out.println("ERROR");
                            }
                        }
                        else{
                            //CASO: ETIQUETA, CODOP, OPERANDO
                            if(validarEtiq(campos[0])&&validarCodop(campos[1])){
                                LinCod.setEtiqueta(campos[0]);
                                LinCod.setCodop(campos[1]);
                                LinCod.setOperando(campos[2]);
                            }
                            else{
                                System.out.println("ERROR");
                            }
                        }
                    break;
                    
                    default: 
                        //IDENTIFICACION CASO: COMENTARIO
                        if(campos[0].equals(";")){//Si la linea empieza con ; lo identifica como comentario
                            System.out.println("COMENTARIO");
                            bandera=false;
                            if(validarComen(LinCod.getEtiqueta()) == true){
                        System.out.println("ETIQUETA = "+LinCod.getEtiqueta());
                    }else{
                        System.out.println("Error");
                    }
                        }
                        else{
                            System.out.println("ERROR");
                        }
                }//Fin casos posibles
                
                if(bandera){
                    System.out.println("ETIQUETA = "+LinCod.getEtiqueta());
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
