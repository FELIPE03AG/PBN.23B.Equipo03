package parte_4;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Fase2 {
    static boolean absoluta = false;
    
    //Busqueda de direccion de la etiquta
    static String conlocEtq(String etiqueta){
        String direccion = " ";//variable que guarda la direccion
        for(Tabsim etq:Conloc.etiquetas){//busca en el arreglo con los datos del tabsim
            if(etq.getSi().equals(etiqueta)){//compara con las etiquetas
                direccion=etq.getTi();//Guarda la direccion que corresponde a esa etiqueta
                if(etq.tipo.equals("ABSOLUTA")){//Si es una etiqueta que pertenece a equ
                    absoluta=true;//activa la bandera
                }//Es una etiqueta absoluta
            }//Se encontro la etiqueta
        }//For para recorrer todas las etiquetas
        return direccion;
    }//Fin del metodo para encontrar el conloc de una etiqueta
    
    //metodo resta hexadecimal
    static String restaHex(String conloc, String conloc2){
       int valor1 = Integer.parseInt(conloc, 16);//Pasa a entero el valor hexadecimal
       int valor2 = Integer.parseInt(conloc2, 16);//Valor dos guarda el sustraendo
       int res = valor1 - valor2;//Se hace la resta y se guarda en res
       String resta = Integer.toHexString(res).toUpperCase();//Se pasa el resultado a hexadecimal
       return resta;
    }//fin metodo resta hexadecimal
    
    // Función para completar un número binario a 8 bits (un byte)
    public static String completarBinarioAByte(String binario,int cantidadbits) {
        int longitud = binario.length(); // Obtiene la longitud del número binario
        if (longitud < 8) {
            int cantidadCeros = cantidadbits - longitud; // Calcula cuántos ceros se deben agregar al principio
            StringBuilder ceros = new StringBuilder();
            for (int i = 0; i < cantidadCeros; i++) {
                ceros.append("0"); // Agrega ceros al principio del número binario
            }
            return ceros.toString() + binario; // Retorna el número binario completo a 8 bits
        } else if (longitud == 8) {
            return binario; // Si ya tiene 8 bits, retorna el mismo número binario
        } else {
            return binario.substring(longitud - 8); // Toma los últimos 8 bits del número binario si tiene más de 8 bits
        }
    }//Fin metodo completar a binario en byte
    
    public static String decimalABinario(int numeroDecimal) {
        if (numeroDecimal == 0) {
            return "0"; // Si el número es 0, su representación binaria también es 0
        }

        StringBuilder binario = new StringBuilder();
        while (numeroDecimal > 0) {
            int residuo = numeroDecimal % 2; // Obtiene el residuo de dividir por 2
            binario.insert(0, residuo); // Inserta el residuo al inicio del StringBuilder
            numeroDecimal = numeroDecimal / 2; // Divide el número por 2 para la próxima iteración
        }

        return binario.toString();
    }//fin decimal a binario
    
    public static void postREL8(Linea relativo, String origen){
        String postbyte = " ";//Variable que guarda el postbyte de la instruccion
        String rr = " ";//Variable que guarda la parte que calculamos
        String destino=" ";//Direccion a la que va el brinco
        int ori, dest,resta;//Variables auxiliares para la resta de direcciones
        boolean destinoExist=false; //Variable que verifica que el destino existe
       
        destino=conlocEtq(relativo.getOperando());//Se guarda la direccion de destino
        if (absoluta){//Si es absoluta revisa que si exista una instruccion en esa posicion
            for(Linea existe:Parte_4.LineasASM){
                if(existe.getConloc().equals(destino)){
                    destinoExist=true;//enciende la bandera
                }//fin si existe esa direccion con una instruccion
            }//fin for, buscar la direccion
            if(!destinoExist){
                destino=" ";//limpia el destino
            }//Fin no existe
            absoluta=false;//reinicia la bandera
        }//fin es absoluta
        
        String frmbase [] = relativo.getForm().split(",");//separa el frmbase en calculado y por calcular
        if (!(destino.equals(" "))) {//si existe una direccion de memoria
            postbyte = frmbase[0];//guarda la parte que ya esta calculada
            ori = Parte_4.ConvertirADecimal("$".concat(origen));//guarda el valor decimal del origen
            dest = Parte_4.ConvertirADecimal("$".concat(destino));//guarda el valor decimal del destino
            if (ori > dest) {//Si origen es mayor que el destino
                resta = ori - dest;//restar el destino al origen
                if (resta < 129) {//validacion del rango del salto negativo
                    rr = decimalABinario(resta);//Paso de decimal a binario el resultado de la resta
                    rr = completarBinarioAByte(rr,8);//lo completa a byte
                    rr = Salvacion.calcularComplementoDos(rr);//calcula el complemento a 2
                    rr = Integer.toHexString(Parte_4.ConvertirADecimal("%".concat(rr))).toUpperCase();//guarda el resultado en hexadecimal
                }//fin rango valido
                else {
                    rr = null;
                }
            } 
            else if (dest > ori) {//o si el destino es mayor al origen
                resta = dest - ori;//Hace la resta correspondiente 
                if (resta < 128) {//valida el rango del salto positivo
                    rr = Integer.toHexString(resta).toUpperCase();//guarda el resultado en hexadecimal
                }//fin rango correcto
                else {
                    rr = null;
                }
            }//fin destino mayor al origen
            else if(dest == ori){
                rr="00";
            }
            
            if (rr != null) {
                if (rr.length() == 1) {//si no es menos de un byte
                    rr = "0".concat(rr);//completa a expresion en byte
                }//fin no es un byte
                postbyte = postbyte.concat(" ").concat(rr);//Concatena lo que calculamos con lo que se tenia
                relativo.setCop(postbyte);//guardo el postbyte completo en los datos de la instruccion
            }
        }//fin direccion valida
        if(destino.equals(" ")||rr==null){
            relativo.setCop("ERROR");
            Parte_4.Errores.add("ERROR rango del salto de rel "+relativo.getCodop()+" "+relativo.getOperando());
        }
    }//Fin postbyte de lo rel 8
    
    public static void postREL16(Linea relativo, String origen){
        String postbyte = " ";//Variable que guarda el postbyte de la instruccion
        String rr = " ";//Variable que guarda la parte que calculamos
        String destino=" ";//Direccion a la que va el brinco
        int ori, dest,resta;//Variables auxiliares para la resta de direcciones
        boolean destinoExist=false; //Variable que verifica que el destino existe
       
        destino=conlocEtq(relativo.getOperando());//Se guarda la direccion de destino
        if (absoluta){//Si es absoluta revisa que si exista una instruccion en esa posicion
            for(Linea existe:Parte_4.LineasASM){
                if(existe.getConloc().equals(destino)){
                    destinoExist=true;//enciende la bandera
                }//fin si existe esa direccion con una instruccion
            }//fin for, buscar la direccion
            if(!destinoExist){
                destino=" ";//limpia el destino
            }//Fin no existe
            absoluta=false;//reinicia la bandera
        }//fin es absoluta
        
        
        String frmbase [] = relativo.getForm().split(",");//separa el frmbase en calculado y por calcular
        if (!(destino.equals(" "))) {//si existe una direccion de memoria
            postbyte = frmbase[0].concat(" ").concat(frmbase[1]);//guarda la parte que ya esta calculada
            ori = Parte_4.ConvertirADecimal("$".concat(origen));//guarda el valor decimal del origen
            dest = Parte_4.ConvertirADecimal("$".concat(destino));//guarda el valor decimal del destino
            if (ori > dest) {//Si origen es mayor que el destino
                resta = ori - dest;//restar el destino al origen
                if (resta < 32769) {//validacion del rango del salto negativo
                    rr = decimalABinario(resta);//Paso de decimal a binario el resultado de la resta
                    rr = completarBinarioAByte(rr,16);//lo completa a byte
                    rr = Salvacion.calcularComplementoDos(rr);//calcula el complemento a 2
                    rr = Integer.toHexString(Parte_4.ConvertirADecimal("%".concat(rr))).toUpperCase();//guarda el resultado en hexadecimal
                }//fin rango valido
                else {
                    rr = null;
                }
            } 
            else if (dest > ori) {//o si el destino es mayor al origen
                resta = dest - ori;//Hace la resta correspondiente 
                if (resta < 65535) {//valida el rango del salto positivo
                    rr = Integer.toHexString(resta).toUpperCase();//guarda el resultado en hexadecimal
                }//fin rango correcto
                else {
                    rr = null;
                }
            }//fin destino mayor al origen
            else if (dest == ori) {
                rr = "0000";
            }
            
            if (rr != null) {
                switch (rr.length()) {
                    //fin no es un byte
                    case 1:
                        //si no es menos de un byte
                        rr = "000".concat(rr);//completa a expresion en byte
                        break;
                    case 2:
                        rr = "00".concat(rr);//completa a expresion en byte
                        break;
                    case 3:
                        rr = "0".concat(rr);//completa a expresion en byte
                        break;
                    default:
                        break;
                }//fin switch
                rr = rr.substring(0, 2).concat(" ").concat(rr.substring(2, 4));//divide por byte
                postbyte = postbyte.concat(" ").concat(rr);//concatena lo calculado con lo que se tenia
                relativo.setCop(postbyte);//guardo el postbyte completo en los datos de la instruccion
            }
        }//fin direccion valida
        if (destino.equals(" ") || rr == null) {
            relativo.setCop("ERROR");
            Parte_4.Errores.add("ERROR rango del salto de rel" + relativo.getCodop() + " " + relativo.getOperando());
        }
    }//Fin postbyte de lo rel 16
    
    static String calcularlb(String registro, String codop, String salto) {
        String lb = " ";
        try {
            RandomAccessFile auxArchivo = new RandomAccessFile("TablaA6.txt","r");//r es para solo leer el archivo
            long cursorActual;//Para saber donde estamos en el asm
            cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
            FileReader tablaFile = new FileReader("TablaA6.txt");//leo el archivo
            String lecturaLinea; //Guarda cada linea

            while (cursorActual!=auxArchivo.length()) {
                lecturaLinea = auxArchivo.readLine();//leo la linea
                cursorActual = auxArchivo.getFilePointer(); //Se posiciona el puntero para la lectura
                String[] campos = lecturaLinea.split("\\s+"); //Se separa la linia por bloques
                if (campos.length == 4) {//Debo tener 4 campos o bloques
                    String instruccion = campos[0]; //Guarda la intruccion de la linea
                    String tipo = campos[1];//Guarda el registro que corresponde
                    String relativo = campos[2];//Guarda si es positivo o negativo
                    //Busca una coincidencia exacta entre la linea de la tabla y la intruccion
                    if (instruccion.equals(codop)) {
                        if (tipo.equals(registro)) {
                            if (relativo.equals(salto)) {
                                lb = campos[3].trim();//Si todo concuerda, guarda el lb
                                cursorActual=auxArchivo.length();//Para salir del while una vez que se encuentre
                            }
                        }
                    }//fin inicio de coincidencia
                }//Fin si son 4 bloques
            }//fin while
            tablaFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lb;
    }//fin calcular lb
    
    static public void postbRel9(Linea relativo, String origen){
        String postbyte = " ";//Variable que guarda el postbyte de la instruccion
        String rr = " ", lb=" ", salto=" ";//Variable que guarda la parte que calculamos
        String destino=" ";//Direccion a la que va el brinco
        int ori, dest,resta;//Variables auxiliares para la resta de direcciones
        boolean destinoExist=false; //Variable que verifica que el destino existe
       
        String operandos[]=relativo.getOperando().split(",");
        destino=conlocEtq(operandos[1]);//Se guarda la direccion de destino
        if (absoluta){//Si es absoluta revisa que si exista una instruccion en esa posicion
            for(Linea existe:Parte_4.LineasASM){
                if(existe.getConloc().equals(destino)){
                    destinoExist=true;//enciende la bandera
                }//fin si existe esa direccion con una instruccion
            }//fin for, buscar la direccion
            if(!destinoExist){
                destino=" ";//limpia el destino
            }//Fin no existe
            absoluta=false;//reinicia la bandera
        }//fin es absoluta
        
        String frmbase [] = relativo.getForm().split(",");//separa el frmbase en calculado y por calcular
        if (!(destino.equals(" "))) {//si existe una direccion de memoria
            postbyte = frmbase[0].concat(" ");
            ori = Parte_4.ConvertirADecimal("$".concat(origen));//guarda el valor decimal del origen
            dest = Parte_4.ConvertirADecimal("$".concat(destino));//guarda el valor decimal del destino
            if (ori > dest) {//Si origen es mayor que el destino
                salto="Negativo";
                resta = ori - dest;//restar el destino al origen
                if (resta < 257) {//validacion del rango del salto negativo
                    rr = decimalABinario(resta);//Paso de decimal a binario el resultado de la resta
                    rr = completarBinarioAByte(rr,8);//lo completa a byte
                    rr = Salvacion.calcularComplementoDos(rr);//calcula el complemento a 2
                    rr = Integer.toHexString(Parte_4.ConvertirADecimal("%".concat(rr))).toUpperCase();//guarda el resultado en hexadecimal
                }
                else {
                    rr = null;
                }
            } 
            else if (dest > ori) {//o si el destino es mayor al origen
                salto = "Positivo";
                resta = dest - ori;//Hace la resta correspondiente 
                if (resta < 256) {//valida el rango del salto positivo
                    rr = Integer.toHexString(resta).toUpperCase();//guarda el resultado en hexadecimal
                }//fin rango correcto
                else {
                    rr = null;
                }
            }//fin destino mayor al origen
            else if (dest == ori) {
                salto = "Positivo";
                rr="00";
            }
            else {
                rr = null;
            }

            if (rr != null) {
                lb = calcularlb(operandos[0], relativo.getCodop(), salto);
                if (rr.length() == 1) {//si no es menos de un byte
                    rr = "0".concat(rr);//completa a expresion en byte
                }//fin no es un byte
                rr = " ".concat(rr);
                relativo.setCop(postbyte.concat(lb).concat(rr));
            }
        }//fin direccion valida
        if(destino.equals(" ")||rr==null){
            relativo.setCop("ERROR");
            Parte_4.Errores.add("ERROR rango del salto de rel "+relativo.getCodop()+" "+relativo.getOperando());
        }
    }// fin calculo del postbyte del los relativos de 9bits
    
    static void buscarRels(){//Metodo para identificar rels y calcular su postbyte
        for(Linea asm:Parte_4.LineasASM){//for para recorrer todas las intrucciones
            if(asm.getADDR().equals("REL (8b)")){//si es relativa de 8b
                postREL8(asm, Conloc.sumarHexadecimal(asm.getConloc(), 2));//calcula su postbyte
            }//fin relativo de 8b
            else if(asm.getADDR().equals("REL (16b)")){//Si es relativo de 16b
                postREL16(asm, Conloc.sumarHexadecimal(asm.getConloc(), 4));//calcula su postbyte
            }//fin rel16b
            else if(asm.getADDR().equals("REL (9b)")){
                postbRel9(asm, Conloc.sumarHexadecimal(asm.getConloc(), 3));
            }//fin rel9
        }//fin for asm
    }//Fin buscar rels
}//Fin de la clase