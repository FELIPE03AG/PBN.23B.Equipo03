/*
    UNIVERCIDAD DE GUADALAJARA
    CENTRO UNIVERSITARIO DE LOS ALTOS
    INGENIERÍA EN COMPUTACIÓN
    PROGRAMACIÓN DE BAJO NIVEL
    PROYECTO INTEGRADOR
            EQUIPO 3
*/

package parte_2;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Parte_2 {

        
    static Linea PrimerLinCod=null,NewLinCod=null,FinLinCod=null; //Va aguardar las variables
    static int aux;
    static boolean bandera = false;//Variable que indica si la linea es un comentario
    
    //METODO PARA EVALUAR ETIQUETA
    static boolean validarEtiq(String etiqueta){
        boolean banEtiq=false;
        if(etiqueta.length()<=8){
            for(int i = 0; i < etiqueta.length(); i++){
                char c = etiqueta.charAt(i);//Para revisar caracter por caracter
                if(Character.isLetter(c) && i == 0){ 
                    banEtiq =true;
                }//Fin if primer caracter debe ser una letra
                else if((Character.isLetterOrDigit(c) || c == '_' || (c==':' && i==etiqueta.length()-1)) && i!=0){
                    banEtiq =true;
                }//Fin if validacion letras numeros, guion bajo o dos puntos al final
                else{
                    banEtiq=false;
                    i=etiqueta.length();
                }//fin no es una etiqueta
            }//Fin for
        }//Fin largo menor a 8   
        return banEtiq;
    }//Fin validar etiqueta
    
    //METODO PARA EVALUAR CODOP
    static boolean validarCodop(String codop){
        boolean banCodop=false;
        int punto=0;//Auxiliar para validar que solo ponga un punto
        if(codop.length()<=5){
            for(int i = 0; i < codop.length(); i++){
                char c = codop.charAt(i);
                if(Character.isLetter(c)){
                    banCodop =true;
                }//Fin if solo letras
                else if(c=='.'&&punto==0&&i!=0){
                    banCodop =true;
                    punto++;
                }//Fin si no: puede ser un solo punto
                else{
                    banCodop=false;
                    i=codop.length();
                }//Fin else
                
            }//Fin for para recorrer el CODOP
        }//Fin no mas de 5 caracteres 
        return banCodop;
    }//FINAL DE METODO PARA EVALUAR CODOP
    
    //METODO IDENTIFICAR Y EVALUAR COMENTARIO
    static boolean Comentario(String x){
        boolean Coment=false;
        char c = x.charAt(0);//Para evaluar caracter por caracter
        if(c==';'){
            bandera=true;
        }//Fin debe empezar con punto y coma
        if (x.length() > 81){
            Coment = true;
        } 
        return Coment;
    }//Fin validacion comentario
    
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
                bandera=false;
                if(!(lecturaLinea.equals(""))){
                    Comentario(lecturaLinea);
                }
                if(!(bandera)){
                    String[] campos = lecturaLinea.split("\\s+");//Separa por bloques segun cada espacios o tabulación
                    NewLinCod = new Linea("null","null","null",null,"null"); //Inicializo valores

                    aux=0;
                    if(campos[0].equals("")){
                        aux++;
                    }//Fin si empieza con espacio
                    if(campos.length<=aux+4){//If para validacion de solo 3 bloques no mas
                        if(aux<campos.length){
                            if(validarEtiq(campos[aux])&&aux<campos.length-1 && validarCodop(campos[aux+1]) && !(campos[0].equals(""))){
                                NewLinCod.setEtiqueta(campos[aux]);
                                aux++;//Para movernos al siguiente bloque
                            }//Busco una etiqueta
                            if(validarCodop(campos[aux])){
                                NewLinCod.setCodop(campos[aux]);
                                aux++;//Para movernos al siguiente bloque
                                if(aux==campos.length-1){//Si hay siguiente bloque debe ser el operando
                                   NewLinCod.setOperando(campos[aux]); 
                                }
                                else if(aux==campos.length-2){
                                    NewLinCod.setOperando(campos[aux].concat(campos[aux+1]));
                                }
                                else if(NewLinCod.getCodop().equals("END")&&aux==campos.length){
                                    cursorActual=auxArchivo.length();
                                }
                                if(PrimerLinCod==null){
                                    PrimerLinCod=NewLinCod;
                                }
                                else{
                                    FinLinCod.setSiguiente(NewLinCod);
                                }
                                FinLinCod=NewLinCod;
                            }//Fin CODOP correcto
                            else{
                                System.out.println("ERROR");
                            }//Fin error con codigo de operacion
                        }//Fin if para recorrer bloques del codigo
                    }//Fin no mas de 3 codigos
                    else{
                        System.out.println("ERROR");
                    }
                }//Fin if: no es un comentario
                else{
                    if (Comentario (lecturaLinea)){
                        System.out.println("COMENTARIO CON EXCESO DE CARACTERES");
                        System.out.println("");
                    }//Fin comentario incorrecto
                    else {
                        System.out.println("COMENTARIO");
                        System.out.println("");
                    }//Fin comentario correcto
                }//Fin else no es linea de codigo, es un comentario 
            }//Fin del while
            leerArchivo.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    //METODO PARA IDENTIFICAR ADDR
    static void ADDR (Linea Evaluar){
        boolean fin =false;
        do{
            if(Evaluar.getOperando().equals("null")){
                switch (Evaluar.getCodop()){
                    case "END":
                        Evaluar.setADDR("DIRECT");
                    break;
                    
                    case "ABX":
                    case "ABY":
                        Evaluar.setADDR("IDX");
                    break;
                    default:
                        Evaluar.setADDR("INH");
                    break;
                }
            }
            else if(Evaluar.getCodop().equals("ORG") && Evaluar.getOperando().startsWith("$")){
                Evaluar.setADDR("DIRECT");
            }
            switch(Evaluar.getOperando().charAt(0)){
                case '#':
                    Evaluar.setADDR("IMM");
                break;
                case '[':
                break;
                
            }
            ImprimirLinCod(Evaluar);
            if(Evaluar==FinLinCod){
                fin=true;
            }
            else{
                Evaluar=Evaluar.getSiguiente();
            }
        }while(!fin);
    }
    
    static void ImprimirLinCod (Linea LinCod){
        System.out.println("ETIQUETA = "+LinCod.getEtiqueta());
        System.out.println("CODOP = "+LinCod.getCodop());
        System.out.println("OPERANDO = "+LinCod.getOperando());
        System.out.println("ADDR = "+LinCod.getADDR());
        System.out.println("");
    }
    public static void main(String[] args) {
        Leer();//Llamo el metodo
        ADDR(PrimerLinCod);
    }
    
}
