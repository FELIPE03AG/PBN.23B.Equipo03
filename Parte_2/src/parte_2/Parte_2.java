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
    
    //METODO DE APOYO PARA PASAR DE OCTAL A DECIMAL...(OPCIONAL)
    public static int octalADecimal(int octal) {
    int decimal = 0;
    int potencia = 0;
    // Ciclo infinito que se rompe cuando octal es 0
    while (true) {
        if (octal == 0) {//HASTA QUE OCTAL = 0 SE PARA EL BUCLE
            break;
        } else {
            int temp = octal % 10;
            decimal += temp * Math.pow(8, potencia);
            octal = octal / 10;
            potencia++;
            //PROCESO DE CONVERSION...
        }
    }
    return decimal; //regresa el valor para trabajarlo
}//FIN METODO PARA PASAR OCTAL A DECIMAL...
   
    //funcion para validar tipo hexadecimal
    static boolean ValidarHexadecimal(String Hexa){
        boolean Valido=false;  //variable para aceptar o denegar
        if(Hexa.matches("[0-9A-F]+")){ // funcion que valida que tenga caracteres permitidos
            if(Hexa.length()<=4){ //si tiene el num mayor permitido (FFFF), o menos. Entonces acepta...
                Valido=true;
            }
        }
        return Valido;
    }
    
    //Metodo para evaluar Octal
    static boolean validarOctal(String Oct){
      boolean banOctal = false;
        int Octal;
        if(Oct.matches("[1-7]+")){ // valida que tenga numeros solo del 0 al 7
            Octal = Integer.parseInt(Oct); //convierte de String s entero
            if(octalADecimal(Octal)<=65535){ //revisa que sea menor o igual a 16 bits que es lo permitido
                banOctal=true;
            }
        }
      //proceso...
      return banOctal;
    }//Fin evaluar octal
    
    //Metodo para evaluar Binario
    static boolean ValidarBinario(String nBin){
        boolean Valido=false;  // variable que sirve para validar o denegar 
        if(nBin.matches("[0-1]+")){ //revisa que solo tenga valores permitidos (0 y 1).
            if(nBin.length()<=16){ //revisa que cumpla con la cantidad de bits permitidos.
                Valido=true; //valida verdadero
            }
        }
        return Valido;//regresa el valor
        
    }//Fin metodo para evaluar binario...
    
    
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
                cursorActual = auxArchivo.getFilePointer();
                bandera=false;
                if(!(lecturaLinea.equals(""))){
                    Comentario(lecturaLinea);
                }
                if(!(bandera)){
                    String[] campos = lecturaLinea.split("\\s+");//Separa por bloques segun cada espacios o tabulación
                    NewLinCod = new Linea(" "," "," ",null," "); //Inicializo valores

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
    
    //METODO PARA IDENTIFICAR ADDR SEGUN SU OPERANDO
    static void ADDR (Linea Evaluar){
        //HACE FALTA BUSCAR QUE EL CODOP EXISTA
        boolean fin =false;
        do{
            if(Evaluar.getOperando().equals(" ")){
                if (Evaluar.getCodop().equals("END")){
                    Evaluar.setADDR("DIRECT");
                }
                else{
                    Evaluar.setADDR("INH");
                }
            }
            else if(Evaluar.getCodop().equals("ORG") && Evaluar.getOperando().startsWith("$")){
                Evaluar.setADDR("DIRECT");
            }
            else if(Evaluar.getCodop().equals("SEX")){
                Evaluar.setADDR("INH");
            }
            else if(Evaluar.getOperando().contains(",") && !(Evaluar.getOperando().contains("[") || Evaluar.getOperando().contains("]"))){
                String partes[]=Evaluar.getOperando().split(",");
                if(partes[0].matches(".*\\d.*")){
                    int valor=Integer.parseInt(partes[0]);
                    if(partes[1].equals("X") || partes[1].equals("Y") ||
                                partes[1].equals("SP") || partes[1].equals("PC")){
                        if(valor>-17 && valor<15){
                            Evaluar.setADDR("IDX(5b)");
                        }
                        else if((valor>-257 && valor<-16)||(valor>15 && valor<256)){
                            Evaluar.setADDR("IDX1");
                        }
                        else if(valor>255 && valor<65536){
                            Evaluar.setADDR("IDX2");
                        }
                    }
                    if((partes[1].startsWith("+") || partes[1].startsWith("-") || partes[1].endsWith("+") ||
                            partes[1].endsWith("-"))&& (partes[1].contains("X") ||
                            partes[1].contains("Y") || partes[1].contains("SP") || partes[1].contains("PC")) ){
                        if(valor>0 && valor<9){
                            Evaluar.setADDR("IDX(pre-inc)");
                        }
                    }
                }
                else if(partes[0].equals("A")||partes[0].equals("B")||partes[0].equals("D")){
                    if(partes[1].equals("X") || partes[1].equals("Y") ||
                                partes[1].equals("SP") || partes[1].equals("PC")){
                        Evaluar.setADDR("IDX(acc)");
                    }
                }
            }
            else{
                switch(Evaluar.getOperando().charAt(0)){
                    case '#':
                        Evaluar.setADDR("IMM");
                    break;
                    case '$':
                        
                    break;
                    case '@':
                   
                    break;
                    case '%':
                    break;
                    case '[':
                        String partes[]=Evaluar.getOperando().split(",");
                        if(partes[0].equals("[D")){
                            
                            if(partes[1].equals("X]") || partes[1].equals("Y]") ||
                                partes[1].equals("SP]") || partes[1].equals("PC]")){
                                   Evaluar.setADDR("[D,IDX]");
                            }
                        }
                        else{
                            String valor = partes[0].substring(1);
                            if(valor.matches(".*\\d.*")){
                                int numero = Integer.parseInt(valor);
                                
                                if(numero<=65535 && (partes[1].equals("X]") || partes[1].equals("Y]") ||
                                                     partes[1].equals("SP]") || partes[1].equals("PC]"))){
                                    Evaluar.setADDR("[IDX2]");
                                }
                            }
                        }
                    break;
                
                }
            }
            if(Evaluar==FinLinCod){
                fin=true;
            }
            else{
                Evaluar=Evaluar.getSiguiente();
            }
        }while(!fin);
    }
    

    public static void main(String[] args) {
        Leer();//Llamo el metodo
        ADDR(PrimerLinCod);
        new Tabla().setVisible(true);
    }
    
}
