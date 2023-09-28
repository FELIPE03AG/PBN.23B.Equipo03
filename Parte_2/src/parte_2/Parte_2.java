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
    static Linea PrimerLinCod=null,NewLinCod=null,FinLinCod=null; //Para guardar en una lista las lineas del codigo
    static boolean Comentario = false;//Variable que indica si la linea es un comentario
    
//**************************************************************** PARTE 1 *******************************************************
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
            Comentario=true;
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
                Comentario=false;
                if(!(lecturaLinea.equals(""))){//El if hace que se ignore cuando hay un linea de codigo vacia
                    Comentario(lecturaLinea);
                }
                if(!(Comentario)){ //Si no es un comentario, debe ser una linea de codigo
                    String[] campos = lecturaLinea.split("\\s+");//Separa por bloques segun cada espacios o tabulación
                    NewLinCod = new Linea(" "," "," ",null," "); //Inicializo valores
                    if(campos.length<=3 && campos.length>1){//If para validacion de solo 4 bloques no mas
                        if(!(campos[0].equals(""))){
                            if(validarEtiq(campos[0])){
                                if(campos[0].endsWith(":")){
                                    NewLinCod.setEtiqueta(campos[0].substring(0, campos[0].length()-1));
                                }else{
                                    NewLinCod.setEtiqueta(campos[0]);
                                }
                            }
                            else{
                                NewLinCod.setEtiqueta("Formato Etiqueta");
                            }
                        }//Busco una etiqueta
                        if(validarCodop(campos[1])){
                            NewLinCod.setCodop(campos[1]);
                            if(campos.length==3){//Si hay siguiente bloque debe ser el operando
                                NewLinCod.setOperando(campos[2]); 
                            }
                            else if(NewLinCod.getCodop().equals("END")){
                                if(NewLinCod.getOperando().equals(" ")){
                                    cursorActual=auxArchivo.length();
                                }
                                else{
                                    System.out.println("EL END NO DEBE DE LLEVAR OPERANDO");
                                    NewLinCod.setOperando(" ");
                                }
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
                            System.out.println("ERROR "+campos[1]+"No es un CODOP");
                        }//Fin error con codigo de operacion
                    }//Fin no mas de 4 bloques
                    else{
                        if(campos.length>4){
                            System.out.println("ERROR mas de 3 bloques en la linea");
                        }
                    }
                }//Fin if: no es un comentario, es una linea de codigo
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
            
            if(cursorActual==auxArchivo.length() && (!NewLinCod.getCodop().equals("END"))){
                System.out.println("END NO LOCALIZADO");
                PrimerLinCod=null;
                FinLinCod=null;
                            }
            leerArchivo.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
//******************************************************************** PARTE 2 *************************************************
    
    //METODO DE APOYO PARA PASAR DE OCTAL A DECIMAL...
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
    
    public static int binarioADecimal(String binario) {
        int decimal = 0;
        int longitud = binario.length();

        for (int i = 0; i < longitud; i++) {
            char digito = binario.charAt(i);
            int valorDigito = Character.getNumericValue(digito);
            
            // Multiplicar el valor actual por 2 elevado a la posición
            int exponente = longitud - 1 - i;
            decimal += valorDigito * Math.pow(2, exponente);
        }

        return decimal;
    }
    
     //METODO PARA VALIDAR UN HEXADECIMAL
    static boolean ValidarHexadecimal(String Hexa){
        Hexa=Hexa.toUpperCase();
        boolean Valido=false;  //variable para aceptar o denegar
        if(Hexa.matches("[0-9A-F]+")){ // funcion que valida que tenga caracteres permitidos
            if(Hexa.length()<=4){ //si tiene el num mayor permitido (FFFF), o menos. Entonces acepta...
                Valido=true;
            }
        }
        return Valido;
    }//Fin evaluar hexadecimal
    
    //METODO PARA EVALUAR UN OCTAL
    static boolean ValidarOctal(String Oct){
      boolean banOctal = false;
        int Octal;
        if(Oct.matches("[0-7]+")){ // valida que tenga numeros solo del 0 al 7
            Octal = Integer.parseInt(Oct); //convierte de String s entero
            if(octalADecimal(Octal)<=65535){ //revisa que sea menor o igual a 16 bits que es lo permitido
                banOctal=true;
            }
        }
      //proceso...
      return banOctal;
    }//Fin evaluar octal
    
    //METODO PARA EVALUAR UN BINARIO
    static boolean ValidarBinario(String nBin){
        boolean Valido=false;  // variable que sirve para validar o denegar 
        if(nBin.matches("[0-1]+")){ //revisa que solo tenga valores permitidos (0 y 1).
            if(nBin.length()<=16){ //revisa que cumpla con la cantidad de bits permitidos.
                Valido=true; //valida verdadero
            }
        }
        return Valido;//regresa el valor
        
    }//Fin metodo para evaluar binario..
    
    //METODO PARA PASAR DE CUALQUIER BASE A DECIMAL
    static int ConvertirADecimal(String Operando){
        int Decimal=0;
        switch(Operando.charAt(0)){
            case '$':
                if(ValidarHexadecimal(Operando.substring(1))){
                    Decimal = Integer.parseInt(Operando.substring(1), 16);
                }
                else{
                    Decimal = -1;
                }
            break;
            case '@':
                if(ValidarOctal(Operando.substring(1))){
                    Decimal = octalADecimal(Integer.parseInt(Operando.substring(1)));
                }
                else{
                    Decimal = -1;
                }
            break;
            case '%':
                if(ValidarBinario(Operando.substring(1))){
                    Decimal = binarioADecimal(Operando.substring(1));
                }
                else{
                    Decimal=-1;
                }
            break;
            default:
                if(Operando.matches("[0-9]+")&&Integer.parseInt(Operando)<65535){
                    Decimal = Integer.parseInt(Operando);
                }
                else{
                    Decimal=-1;
                }
            break;
        }
        return Decimal;
    }//Fin metodo convertir a decimal
    
    //METODO PARA EVALUAR UN ADDR IMM
    static boolean IMM(String operando, String FormaOpr){
        boolean imm=false;
        int AUX=0;
        if(operando.charAt(0)=='#'){
            AUX=ConvertirADecimal(operando.substring(1));
            if(AUX!=-1){
                if(FormaOpr.equals("#opr8i") && AUX<256 && AUX>=0){
                    imm=true;
                }
                else if(FormaOpr.equals("#opr16i")){
                    if(operando.startsWith("$") && ValidarHexadecimal(operando) && operando.length()==5){
                        imm=true;
                    }
                    else if(AUX<=65535 && AUX>255){
                        imm=true;
                    }
                } 
            }
        }//Fin es imm
        return imm;
    }
    
    //METODO PARA EVALUAR UN ADDR DIR
    static boolean DIR(String operando){
        boolean dir=false;
            if(ConvertirADecimal(operando)<=255 && ConvertirADecimal(operando)!=-1){
                dir=true;
            }
        return dir;
    }//Fin metodo para dir
    
    //METODO PARA EVALUAR UN ADDR EXT
    static boolean EXT(String operando){
        boolean ext=false;
            if(ConvertirADecimal(operando)>255){
                ext=true;
            }
        return ext;
    }//Fin metodo para dir
    
    //METODO PARA EVALUAR UN ADDR IDX  
    static String IDX(String Operando){
        String idx="0";
        if (Operando.matches("^[^,]*,[^,]*$")){
                String partes[]=Operando.split(",");
                if(partes[0].matches(".*\\d.*")){
                    int valor=Integer.parseInt(partes[0]);
                    if(partes[1].equals("X") || partes[1].equals("Y") ||
                       partes[1].equals("SP") || partes[1].equals("PC")){
                            if(valor>-17 && valor<=15){
                                idx="IDX(5b)";
                            }
                            else{
                                idx="0";
                            }
                    }
                    else if((partes[1].startsWith("+") || partes[1].startsWith("-") || partes[1].endsWith("+") ||
                             partes[1].endsWith("-"))&& (partes[1].contains("X") ||
                             partes[1].contains("Y") || partes[1].contains("SP")) ){
                                if(valor>0 && valor<9){
                                    idx="IDX(pre-inc)";
                                }
                                else{
                                    idx="0";
                                }
                    }
                    else{
                        idx="0";
                    }
                }
                else if(partes[0].equals("A")||partes[0].equals("B")||partes[0].equals("D")){
                    if(partes[1].equals("X") || partes[1].equals("Y") ||
                        partes[1].equals("SP") || partes[1].equals("PC")){
                            idx="IDX(acc)";
                    }
                }
                else{
                    idx="0";
                }
        }
            return idx;
    }//Fin metodo para IDX
    
    //METODO PARA ADDR IDX1
    static boolean IDX1(String Operando){
        boolean idx1=false;
        String partes[]=Operando.split(",");
        if(partes[0].matches(".*\\d.*")){
            int valor=Integer.parseInt(partes[0]);
            if(partes[1].equals("X") || partes[1].equals("Y") ||
               partes[1].equals("SP") || partes[1].equals("PC")){
                    if((valor>-257 && valor<-16)||(valor>15 && valor<256)){
                        idx1=true;
                    }
            }
        }
        return idx1;
    }//Fin modo dir IDX1
            
    //METODO PARA ADDR IDX2
    static boolean IDX2(String Operando){
        boolean idx2=false;
        String partes[]=Operando.split(",");
        if(partes[0].matches(".*\\d.*")){
            int valor=Integer.parseInt(partes[0]);
            if(partes[1].equals("X") || partes[1].equals("Y") ||
               partes[1].equals("SP") || partes[1].equals("PC")){
                    if(valor>255 && valor<65536){
                        idx2=true;
                    }
            }
        }
        return idx2;
    }
    
    //METODO PARA ADDR [D,IDX]
    static boolean IdxD(String Operando){
        boolean idx=false;
        String partes[]=Operando.split(",");
            if(partes[0].equals("[D")){               
                if(partes[1].equals("X]") || partes[1].equals("Y]") ||
                   partes[1].equals("SP]") || partes[1].equals("PC]")){
                        idx=true;
                }
                else{
                    idx=false;
                }
            }
            return idx;
    }

    //METODO PARA [IDX2]
    static boolean Idx2C(String Operando){
        boolean idx=false;
        String partes[]=Operando.split(",");
        String valor = partes[0].substring(1);
        if(valor.matches(".*\\d.*")){
            int numero = Integer.parseInt(valor);
            if(numero<=65535 && (partes[1].equals("X]") || partes[1].equals("Y]") ||
                                 partes[1].equals("SP]") || partes[1].equals("PC]"))){
                idx=true;
            }
        }
        return idx;        
    }
    
    //METODO PARA BUSCAR ETIQUETA
    static boolean BuscarEtiqueta(String Etiqueta){
        boolean existe=false, fin=false;
        Linea Auxiliar = PrimerLinCod;
        while(existe==false && fin ==false){
            if(Auxiliar.getEtiqueta().equals(Etiqueta)){
                    existe=true;
            }
            else{
                if(Auxiliar!=FinLinCod){
                    Auxiliar=Auxiliar.getSiguiente();
                }
                else{
                    fin=true;
                }
            }
        }
        return existe;
    }//Fin encontrar etiqueta
    
    public static void main(String[] args) {
        Leer();//Llamo el metodo
        if(PrimerLinCod!=null){
            Salvacion.GuardarADDR(PrimerLinCod);
            new Tabla().setVisible(true);
        }
        
    }   
}