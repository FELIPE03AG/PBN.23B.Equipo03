
package parte_4;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Salvacion {
    static boolean encontrado;//Varible que utilizaremos para saber si el codop existe
    static int coincidencias=0;
    static NodoSalvacion auxSalvacion=null;
    
    static String Separarform(String forma) {
        String campo3 = forma;
        String resultado;
        StringBuilder campo3Separado = new StringBuilder();
        for (int i = 0; i < campo3.length(); i += 2) {
            campo3Separado.append(campo3.substring(i, Math.min(i + 2, campo3.length())));
            if (i + 2 < campo3.length()) {
                campo3Separado.append(",");
            }
        }
        resultado = campo3Separado.toString();
        return resultado;
    }
    
    static void BuscarCodop(ArrayList <Linea> AuxLineasCodigo){//Buscar es el codigo de operancion que se buscara en la salvacion
        for(Linea auxiliar : AuxLineasCodigo){
            String Buscar=auxiliar.getCodop();
            try{
                RandomAccessFile auxArchivo = new RandomAccessFile("Salvation_Tabop.txt","r");//r es para solo leer el archivo
                long cursorActual;//Para saber donde estamos
                cursorActual = auxArchivo.getFilePointer();//Puntero en el archivo
                FileReader leerArchivo = new FileReader("Salvation_Tabop.txt");//leeo el archivo del exel en txt
                String lecturaLinea;
                encontrado=false;
                coincidencias=0;
                IdentificacionDirectivas(auxiliar);
                //Aqui es donde empieza a leer por linea
                while(cursorActual!=auxArchivo.length() && encontrado==false){//mientras el lector no llegue al final del archivo
                    lecturaLinea = auxArchivo.readLine();//leeo la linea
                    cursorActual = auxArchivo.getFilePointer();
                    String[] campos = lecturaLinea.split("\\s+");//Separamos el txt por tabuladores
                    if(campos[0].equals(Buscar)){//En la primera palabra del txt estan los codop asi que si esa palabra es igual al CODOP buscado, lo encotnramos
                        auxSalvacion = new NodoSalvacion(campos[0],campos[1],campos[2],Separarform(campos[3]),campos[4],campos[5]);
                        coincidencias++;
                        IdentificarADDR(auxiliar,auxSalvacion);
                    }//Fin comparacion con salvacion
                }//Fin del while
                if(!encontrado){
                    if(coincidencias==0){
                        auxiliar.setADDR("ERROR");
                        System.out.print("ADDR no aceptado");
                        System.out.println(" en codop: "+auxiliar.getEtiqueta()+" "+ auxiliar.getCodop()+" porque NO EXISTE");
                    }
                    else if(coincidencias==1 && (auxSalvacion.AddrMode.equals("REL") || auxSalvacion.AddrMode.equals("REL(9-bit)"))) {
                            IdentificacionREL(auxiliar,auxSalvacion);
                            if(!encontrado){
                                auxiliar.setADDR("ERROR");
                                System.out.println("OPR fuera de rango en linea: "+auxiliar.getEtiqueta()+" "+ auxiliar.getCodop() + " " + auxiliar.getOperando());
                            }
                    }           
                    else if(coincidencias>1){
                        auxiliar.setADDR("ERROR");
                        System.out.println("OPR fuera de rango en linea: "+auxiliar.getEtiqueta()+" "+ auxiliar.getCodop() + " " + auxiliar.getOperando());
                    }
                    else{
                        auxiliar.setADDR("ERROR");
                        System.out.print("ADDR no aceptado");
                        System.out.println(" en codop: "+auxiliar.getEtiqueta()+" "+ auxiliar.getCodop()+" porque no cuenta para el proyecto");
                    }
                }
                leerArchivo.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }//Fin for
    }//Fin leer y buscar en salvacion
    
    static String FormIMM(String opr, String sourceform,int size, int calcular){
        String aux=" ";
        String postbyte=" ";
        String frmbase [] = sourceform.split(",");
        Integer opraux = Parte_4.ConvertirADecimal(opr);
        if(opraux<256 && size==2 && calcular==1 && frmbase[1].equals("ii")){
            postbyte=frmbase[0];
            frmbase[1]=Integer.toHexString(opraux).toUpperCase();
            if(opraux<16){
                frmbase[1]="0".concat(frmbase[1]);
            }
            postbyte=postbyte.concat(" ").concat(frmbase[1]);
        }
        else if(size==3 && calcular==2 && frmbase[1].equals("jj")&& frmbase[2].equals("kk")){
            aux=Parte_4.validarDireccion(opr);
            postbyte=frmbase[0].concat(" ").concat(aux.substring(0, 2)).concat(" ").concat(aux.substring(2));
        }
        return postbyte;
    }
    //Metodo para calcular bostbyte de Directo y extendido...
    static String FormDirExt(String opr, String sourceform,int size, int calcular, String tipo){
        String aux = " ";//variables de apoyo
        String postbyte=" ";
        String frmbase [] = sourceform.split(",");//separador del String por estacios entre comas.
        Integer opraux = Parte_4.ConvertirADecimal(opr); //convertimos a decimal
        if(opraux<256 && size==2 && calcular==1 && frmbase[1].equals("dd")&&tipo.equals("D")){
            //valida que sea de 8 bits, tenga 2 bits, 1 por calcular y que la forma sea 'dd' y el tipo 'D'.
            postbyte=frmbase[0];//igualamos la variable postbyte a la pos 0 del array
            frmbase[1]=Integer.toHexString(opraux).toUpperCase();//pasamos a hexadecimal la segunda posicion del array
            if(opraux<16){//valida que el auxiliar sea menor que 16
                frmbase[1]="0".concat(frmbase[1]);//concatena un 0 a la izquierda del String
            }//fin segundo if
            postbyte=postbyte.concat(" ").concat(frmbase[1]);
            //concatena la posicion 0, o el bit calculado, con el bit recien calculado, el que faltaba.
            //quedan 2 bits que son los totales en el directo
        } //Caso Extendido...
        else if(opraux>255 && size==3 && calcular==2 && frmbase[1].equals("hh") && frmbase[2].equals("ll")&&tipo.equals("E")){
            //valida que sea de 16 bits, tenga 3 bits totales y 2 por calcular. 
            //valida que tenga la forma base 'hh' 'll' y sea de tipo 'E'.
            aux=Parte_4.validarDireccion(opr);//valida la direccion del opr y lo guarda en la variable aux
            postbyte=frmbase[0].concat(" ").concat(aux.substring(0, 2)).concat(" ").concat(aux.substring(2));
            //concatena las 3 partes del string, la 0 que estaba calculada y la 1 y 2 recien calculadas.
        }//fin else if    
        
        return postbyte;
    }//fin de metodo DirExt
    
    static String calculoRR(String registro){
         switch(registro){
            case "X":
                if(registro.equals("X")){
                registro = "00";
             }
            break;
            case "Y":
                if(registro.equals("Y")){
                registro = "01";
                }
            break;
            case "SP":
                if(registro.equals("SP")){
                registro = "10";
                }
            break;
            case "PC":
                if(registro.equals("PC")){
                registro = "11";
                }

            break;    
        }// fin switch rr
        return registro;
    }
    
    public static String decimalABinario(int numeroDecimal) {
        if (numeroDecimal < 0 || numeroDecimal > 15) {
            return "Error"; // El número debe estar en el rango de 0 a 15 para representarse en 4 bits
        }
        StringBuilder resultado = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int bit = (numeroDecimal >> i) & 1; // Obtén el i-ésimo bit del número
            resultado.append(bit);
        }
        return resultado.toString();
    }
    
    public static String calcularComplementoDos(String numeroBinario) {
        int longitud = numeroBinario.length();
        StringBuilder complemento = new StringBuilder();

        // Invertir los bits del número
        for (int i = 0; i < longitud; i++) {
            char bit = numeroBinario.charAt(i);
            complemento.append((bit == '0') ? '1' : '0');
        }

        // Sumar 1 al número invertido
        int carry = 1;
        for (int i = longitud - 1; i >= 0; i--) {
            char bit = complemento.charAt(i);
            if (bit == '1' && carry == 1) {
                complemento.setCharAt(i, '0');
            } else if (bit == '0' && carry == 1) {
                complemento.setCharAt(i, '1');
                carry = 0;
            }
        }

        return complemento.toString();
    }
    
    static String idxIncrement(String opr, String sourceform){
        String xb=" ", postbyte=" ";
        char signo=' ';
        int valor =0;
        String frmbase [] = sourceform.split(",");
        
        if(frmbase[1].equals("xb")){
            postbyte=frmbase[0].concat(" ");
        } 
        else if(frmbase[2].equals("xb")){
            postbyte=frmbase[0].concat(" ").concat(frmbase[1].concat(" "));
        }
        
        String operando[] = opr.split(",");
        if (operando[1].startsWith("+") || operando[1].startsWith("-")) {
            xb = "10";
            signo = operando[1].charAt(0);
            operando[1] = operando[1].substring(1);
        } else if (operando[1].endsWith("+") || operando[1].endsWith("-")) {
            xb = "11";
            signo = operando[1].charAt(operando[1].length() - 1);
            operando[1] = operando[1].substring(0, operando[1].length() - 1);
        }
        xb = calculoRR(operando[1]).concat(xb);
        valor = Integer.parseInt(operando[0]);
        if (signo == '+') {
            valor = valor - 1;
            xb = xb.concat(decimalABinario(valor));
        } else if (signo == '-') {
            xb = xb.concat(calcularComplementoDos(decimalABinario(valor)));
        }
        postbyte = postbyte.concat(Integer.toHexString(Parte_4.binarioADecimal(xb)));
        return postbyte.toUpperCase();
    }
    
    static String idxAcc(String opr, String sourceform){
        String postbyte=" ", xb="111";
        String frmbase [] = sourceform.split(",");
        if(frmbase[1].equals("xb")){
            postbyte=frmbase[0].concat(" ");
        } 
        else if(frmbase[2].equals("xb")){
            postbyte=frmbase[0].concat(" ").concat(frmbase[1].concat(" "));
        }
        
        String operando[] = opr.split(",");
        xb=xb.concat(calculoRR(operando[1]).concat("1"));
        if(operando[0].equals("A")){
            xb=xb.concat("00");
        }
        else if(operando[0].equals("B")){
            xb=xb.concat("01");
        }
        else if (operando[0].equals("D")){
            xb=xb.concat("10");
        }
        postbyte = postbyte.concat(Integer.toHexString(Parte_4.binarioADecimal(xb)));
        return postbyte.toUpperCase();
    }
    
    static String idx5b (String opr, String sourceform){
        String postbyte=" ", xb=" ";
        int valor=0;
        String frmbase [] = sourceform.split(",");
        if(frmbase[1].equals("xb")){
            postbyte=frmbase[0].concat(" ");
        } 
        else if(frmbase[2].equals("xb")){
            postbyte=frmbase[0].concat(" ").concat(frmbase[1].concat(" "));
        }
        if(opr.startsWith(",")){
            opr="0".concat(opr);
        }
        String operando[] = opr.split(",");
        xb=calculoRR(operando[1]).concat("0");
        valor=Integer.parseInt(operando[0]);
        if(valor==16){
            xb=xb.concat("10000");
        }
        else if(valor<0){
            valor=valor*-1;
            xb=xb.concat("1").concat(calcularComplementoDos(decimalABinario(valor)));
        }
        else{
            xb=xb.concat("0").concat(decimalABinario(valor));
        }
        xb=Integer.toHexString(Parte_4.binarioADecimal(xb));
        if(xb.length()==1){
            xb="0".concat(xb);
        }
        postbyte = postbyte.concat(xb);
        return postbyte.toUpperCase();
    }
    
    static String idx1(String opr, String sourceform){
        String postbyte=" ", xb=" ", ff=" ";
        String frmbase [] = sourceform.split(",");
        if(frmbase[1].equals("xb") && frmbase[2].equals("ff")){
            postbyte=frmbase[0].concat(" ");
            String operando[] = opr.split(",");
            xb="111".concat(calculoRR(operando[1])).concat("00");
            if(operando[0].startsWith("-")){
                xb=xb.concat("1");
                if(operando[0].equals("-256")){
                    ff="00";
                }
                else{
                    ff=Integer.toBinaryString(Integer.parseInt(operando[0].substring(1)));
                    if(ff.length()>4){
                        switch (ff.length()) {
                            case 5:
                                ff="000".concat(ff);
                                break;
                            case 6:
                                ff="00".concat(ff);
                                break;
                            case 7:
                                ff="0".concat(ff);
                                break;
                            default:
                                break;
                        }
                    }
                    ff=calcularComplementoDos(ff);
                    ff=Integer.toHexString(Parte_4.binarioADecimal(ff));
                }
            }
            else{
                xb=xb.concat("0");
                ff=Integer.toHexString(Integer.parseInt(operando[0]));
            }
        }
        postbyte = postbyte.concat(Integer.toHexString(Parte_4.binarioADecimal(xb))).concat(" "+ff);
        return postbyte.toUpperCase();
    }
    
static String idx2(String opr, String sourceform) {
    String postbyte = " ", xb = " ", ee = " ", ff = " ";
    String frmbase[] = sourceform.split(",");
    
    if (frmbase[1].equals("xb") && frmbase[2].equals("ee") && frmbase[3].equals("ff")) {
        postbyte = frmbase[0].concat(" ");
        String operando[] = opr.split(",");
        xb = "111".concat(calculoRR(operando[1])).concat("010");
        ee=Parte_4.validarDireccion(operando[0]);
        System.out.println(ee);
        ff=ee.substring(2, 4);
        ee=ee.substring(0, 2);
    }

    postbyte = postbyte + Integer.toHexString(Parte_4.binarioADecimal(xb)) + " " + ee + " " + ff;
    return postbyte.toUpperCase();
}

    static void IdentificarADDR(Linea LinCod,NodoSalvacion AUX){
        if(LinCod.getOperando().equals(" ")){//Primer caso no hay operando
            if(AUX.Operando.equals("-")){//La estructura de operando que coincide
                LinCod.setADDR("INH");//ADDR correspondiente
                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                LinCod.setForm(AUX.SourceForm);
                LinCod.setCop(AUX.SourceForm);
                LinCod.setSize(AUX.byteTotal+" bytes");
                encontrado=true;//Ya lo encontro, puede terminar la busquerda
            }//Fin es inh
        }//Fin no hay operando
        else if(LinCod.getOperando().startsWith("#")){//Si el operando parece de tipo IMM
            if(AUX.Operando.equals("#opr8i")||AUX.Operando.equals("#opr16i")){//Puede ser de cualquiera de estas dos estructuras para coincidir
                if(Parte_4.IMM(LinCod.getOperando(),AUX.Operando)){//Evaluo si si es IMM
                    
                    LinCod.setADDR("IMM"); 
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    
                    LinCod.setForm(AUX.SourceForm);
                    LinCod.setCop(FormIMM(LinCod.getOperando().substring(1),AUX.SourceForm,Integer.parseInt(AUX.byteTotal),Integer.parseInt(AUX.byteCalcular)));
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si es IMM
            }
        }
        else if(LinCod.getOperando().startsWith("[D")){//Si comienza de esta forma el operando
            if(AUX.Operando.equals("[D,xysp]")){//Si estructura en la salvacion debe coincidir con esta
                if(Parte_4.IdxD(LinCod.getOperando())){//Valido que este bien el operando
                    LinCod.setADDR("[D,IDX]");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin si es [D,IDX]
        }   
        else if(LinCod.getOperando().startsWith("[")){//Si comienza de esta forma el operando
            if(AUX.Operando.equals("[oprx16,xysp]")){//Si estructura en la salvacion debe coincidir con esta
                if(Parte_4.Idx2C(LinCod.getOperando())){//Evalua que el operando
                    LinCod.setADDR("[IDX2]");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin si es [IDX2]
        }    
        else if(LinCod.getOperando().contains(",")){//Si tiene una coma pero no los elementos anteriores
            if(AUX.Operando.equals("oprx0_xysp")){//Debe coincidir con esta estructura
                if(!(Parte_4.IDX(LinCod.getOperando()).equals("0"))){//Valido que el operando completo tenga la forma completa
                    LinCod.setADDR(Parte_4.IDX(LinCod.getOperando()));
                    LinCod.setForm(AUX.SourceForm);
                    if(LinCod.getADDR().equals("IDX(pre-inc)")){
                        LinCod.setCop(idxIncrement(LinCod.getOperando(), LinCod.getForm()));
                    }
                    else if(LinCod.getADDR().equals("IDX(acc)")){
                        LinCod.setCop(idxAcc(LinCod.getOperando(), LinCod.getForm()));
                    }
                    else{
                        LinCod.setCop(idx5b(LinCod.getOperando(), LinCod.getForm()));
                    }
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }
            }//Fin estructura correspondiente
            else if(AUX.Operando.equals("oprx9,xysp")){//Si llego hasta esta estructura
                if(Parte_4.IDX1(LinCod.getOperando())){//Debe ser IDX1
                    LinCod.setADDR("IDX1");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    LinCod.setForm(AUX.SourceForm);
                    LinCod.setCop(idx1(LinCod.getOperando(), LinCod.getForm()));
                    encontrado=true;
                }//Validar operando con de IDX
            }
            else if(AUX.Operando.equals("oprx16,xysp")){//Si llego hasta esta estructura
                if(Parte_4.IDX2(LinCod.getOperando())){//Evaluar si es IDX2
                    LinCod.setADDR("IDX2");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                    LinCod.setForm(AUX.SourceForm);
                    LinCod.setCop(idx2(LinCod.getOperando(), LinCod.getForm()));
                    encontrado=true;
                }//Fin si es IDX2
            }
        }//Fin tiene comas   
        else if(Parte_4.ConvertirADecimal(LinCod.getOperando())!=-1){//Si es directamente un valor en cualquier base
            if(AUX.Operando.equals("opr8a")&&Parte_4.ConvertirADecimal(LinCod.getOperando())<256 
                && Parte_4.ConvertirADecimal(LinCod.getOperando())>=0){//Forma de DIR
                    LinCod.setADDR("DIR");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    LinCod.setForm(AUX.SourceForm);
                    LinCod.setCop(FormDirExt(LinCod.getOperando(),AUX.SourceForm,Integer.parseInt(AUX.byteTotal),Integer.parseInt(AUX.byteCalcular), "D"));
                    encontrado=true;
            }//Fin es DIR
            else if(AUX.Operando.equals("opr16a")){//Estructura de EXT
               if(Parte_4.EXT(LinCod.getOperando())){
                    LinCod.setADDR("EXT");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    LinCod.setForm(AUX.SourceForm);
                    LinCod.setCop(FormDirExt(LinCod.getOperando(),AUX.SourceForm,Integer.parseInt(AUX.byteTotal),Integer.parseInt(AUX.byteCalcular), "E"));
                    encontrado=true;
               }
            }//Fin es EXT
        }//Fin es DIR o EXT
        else if(Parte_4.validarEtiq(LinCod.getOperando())){//EXT con etiqueta
            if(AUX.Operando.equals("opr16a")){//Estructura correspondiente
                LinCod.setADDR("EXT");
                LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                LinCod.setSize(AUX.byteTotal+" bytes");
                encontrado=true;
            }//Fin es EXT
        }
    }
    
    static void IdentificacionREL(Linea LinCod,NodoSalvacion AUX){
        if(AUX.AddrMode.equals("REL")){//Si el operando es REL, hay dos posibilidades
            if(AUX.Operando.equals("rel8")){//Si es de este tipo
                if(Parte_4.validarEtiq(LinCod.getOperando())){//Cuando tiene una palabra
                    LinCod.setADDR("REL (8b)");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si coincide con una etiqueta
            }//Fin es rel8
            if(AUX.Operando.equals("rel16")){//Si es de este tipo
                if(Parte_4.validarEtiq(LinCod.getOperando())){//Cuando el operando es una etiqueta
                    LinCod.setADDR("REL (16b)");
                    LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                    LinCod.setSize(AUX.byteTotal+" bytes");
                    encontrado=true;
                }//Fin si existe la etiqueta
            }//Fin es rel16
        }//Fin es relativo
        else if(AUX.AddrMode.equals("REL(9-bit)")){//Si es de este tipo
            if(LinCod.getOperando().contains(",")){//Debe tener una copa
                String auxiliar[]=LinCod.getOperando().split(",");//Separar por comas
                if(auxiliar.length==2){//Debe tener solo dos bloques
                    if(auxiliar[0].equals("A") || auxiliar[0].equals("B") || auxiliar[0].equals("D") ||
                        auxiliar[0].equals("X") || auxiliar[0].equals("Y") || auxiliar[0].equals("SP")){//Estructuras validas para el primer bloque
                        if(Parte_4.validarEtiq(auxiliar[1])){//Si la parte dos es una etiqueta
                            LinCod.setADDR("REL (9b)");
                            LinCod.setPorCalcular(AUX.byteCalcular+ " bytes");
                            LinCod.setSize(AUX.byteTotal+" bytes");
                            encontrado=true;
                        }//Fin parte dos con etiqueta valida
                    }//Fin estructura del primer bloque
                }//Fin si son dos bloques
                        }//Fin si contiene una coma
        }//Fin rel(9-bit)
        else{
            System.out.println("CODOP O ADDR NO RECONOCIDO");
            System.out.println("CODOP= "+AUX.CODOP);
            System.out.println("OPERANDO= "+LinCod.getOperando());
            System.out.println("Forma del Operando= "+AUX.Operando);
            LinCod.setADDR( "OPR fuera de rango");
        }//Fin codop que no vamos a evaluar
    }
    
    static void IdentificacionDirectivas(Linea LinCod){ // Método para identificar las directivas
        String auxiliar = LinCod.getCodop();
        String tamPala = " ";
        if(auxiliar.contains(".")){  // Verificamos si el CODOP contiene un punto (.)
            String[] campos = auxiliar.split("\\.");
            auxiliar=campos[0];
            tamPala = campos[1];// Si contiene un punto, dividimos el CODOP y el tamaño de la palabra
        } //fin de if
        encontrado=true;
        switch(auxiliar){
            case "ORG": // Directiva ORG: Establece la dirección de inicio del programa
                if(Parte_4.ConvertirADecimal(LinCod.getOperando())==-1){
                    LinCod.setADDR("ERROR");
                    System.out.println("opr fuera de rango en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando());
                } 
                else{
                    LinCod.setADDR("DIRECT");
                } //Fin de la directiva ORG
            break;
            case "END":  // Directiva END: Marca el final del programa
                LinCod.setADDR("DIRECT");
            break; //Fin de la directiva END
            case "EQU":  // Directiva EQU: Asigna un valor constante a una etiqueta
                if(LinCod.getEtiqueta().equals(" ") || Parte_4.ConvertirADecimal(LinCod.getOperando())==-1){  
                    LinCod.setADDR("ERROR");
                    System.out.print("ERROR en: "+LinCod.getEtiqueta()+" EQU "+LinCod.getOperando()+" porque");
                    if(LinCod.getEtiqueta().equals(" ")){
                        System.out.println(" no tiene etiqueta");
                    }
                    else{
                        System.out.println(" el OPR esta fuera de rango");
                    }
                }
                else{
                    LinCod.setADDR("DIRECT");
                }
            break; //fin de la directiva EQU
            case "DC": // Manejo de la directiva "DC" que define constantes o datos
                int tam=0,auxtam=0;
                boolean oprBien=true,mayor255=false;
                if(!(LinCod.getOperando().equals(" "))){ 
                    if(LinCod.getOperando().contains(",")){ // Si el operando contiene comas, hay múltiples valores
                        String [] partOpr = LinCod.getOperando().split(",");
                        auxtam=partOpr.length;
                        for(int i=0; i<auxtam; i++){
                            if(Parte_4.ConvertirADecimal(partOpr[i])!=-1){
                               tam++;
                               if(Parte_4.ConvertirADecimal(partOpr[i])>255){
                                    mayor255=true;
                                }
                            }
                            else if(partOpr[i].startsWith("\"")&&LinCod.getOperando().endsWith("\"")){
                                tam=tam+partOpr[i].substring(1, partOpr[i].length()-1).length();
                            }
                            else{
                                oprBien=false;
                            } 
                        }
                    }
                    else if(LinCod.getOperando().startsWith("\"")&&LinCod.getOperando().endsWith("\"")){  // Si el operando está entre comillas, es una cadena
                        tam=LinCod.getOperando().substring(1, LinCod.getOperando().length()-1).length();
                    }
                    
                    if(tamPala.equals("B")){
                        if(tam!=0){ // Si el tamaño de la palabra es en bytes
                            if(oprBien && !mayor255){
                                LinCod.setSize(String.valueOf(tam)+" bytes");
                                LinCod.setADDR("DIRECT");
                            }
                            else{
                                LinCod.setADDR("ERROR");
                                System.out.println("OPR fuera de rango en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando());
                            }
                        }
                        else{
                            if(Parte_4.ConvertirADecimal(LinCod.getOperando())!=-1 && Parte_4.ConvertirADecimal(LinCod.getOperando())<255){
                                LinCod.setSize("1 bytes");
                                LinCod.setADDR("DIRECT");
                            }
                            else{
                                LinCod.setADDR("ERROR");
                                System.out.println("OPR fuera de rango en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando());
                            }
                        }
                    }
                    else if(tamPala.equals("W")){
                        if(tam!=0){ // Si el tamaño de la palabra es en palabras (2 bytes por palabra)
                            LinCod.setSize(String.valueOf(tam*2)+" bytes");
                            LinCod.setADDR("DIRECT");
                        }
                        else{ // Manejo de otros casos de tamaño de palabra no definidos
                            if(Parte_4.ConvertirADecimal(LinCod.getOperando())!=-1){
                                LinCod.setSize("2 bytes");
                                LinCod.setADDR("DIRECT");
                            }
                            else{
                                LinCod.setADDR("ERROR");
                                System.out.println("OPR fuera de rango en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando());
                            }
                        }
                    }
                    else{
                        LinCod.setADDR("ERROR");
                        System.out.println("ADDR no aceptado en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando()+ " porque el tamano de la palabra es incorrecto");
                    }
                }
                else{
                    LinCod.setADDR("ERROR");
                    System.out.println("OPR fuera de rango en codop: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" con opr: "+LinCod.getOperando());
                }
            break; //Fin de DC
            case "DS":  // Directiva DS: Reserva espacio de memoria en bytes
                String opr = LinCod.getOperando().toString();
                if(opr.matches("\\d+")){
                    if(tamPala.equals("B")){
                        LinCod.setADDR("DIRECT");
                        LinCod.setSize(LinCod.getOperando()+" bytes");
                    }
                    else if(tamPala.equals("W")){ // Reserva espacio de memoria en palabras (2 bytes por palabra)
                        LinCod.setADDR("DIRECT");
                        LinCod.setSize(String.valueOf(Integer.parseInt(LinCod.getOperando())*2)+" bytes");
                    }
                    else{
                        LinCod.setADDR("ERROR");
                        System.out.println("ADDR no aceptado en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando()+ " porque el tamano de la palabra es incorrecto");
                    }
                }
                else{
                    LinCod.setADDR("ERROR");
                    System.out.println("OPR fuera de rango en: "+LinCod.getEtiqueta()+" "+LinCod.getCodop()+" "+LinCod.getOperando()); 
                }
            break;
            default:
                encontrado=false;
            break;
        }
        if(encontrado==true){
            if(!((LinCod.getCodop().contains("DS")) || (LinCod.getCodop().contains("DC"))) && !(LinCod.getADDR().equals("ERROR"))){
                LinCod.setSize("0 bytes");
            }
            if(!(LinCod.getADDR().equals("ERROR"))){
                LinCod.setPorCalcular("0 bytes");
            }
        } //Fin del switch
    } // Fin IdentificacionDirectivas
}//Fin clase
