/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parte_4;

public class CalculoREL {
    static boolean absoluta = false;
    
    //Busqueda de direccion de la etiquta
    static String conlocEtq(String etiqueta){
        String direccion = " ";
        for(Tabsim etq:Conloc.etiquetas){
            if(etq.getSi().equals(etiqueta)){
                direccion=etq.getTi();
                if(etq.tipo.equals("ABSOLUTA")){
                    absoluta=true;
                }
            }
        }
        return direccion;
    }
    //metodo resta hexadecimal
    static String restaHex(String conloc, String conloc2){
       int valor1 = Integer.parseInt(conloc, 16);
       int valor2 = Integer.parseInt(conloc2, 16);
       int res = valor1 - valor2;
       String resta = Integer.toHexString(res).toUpperCase();
       return resta;
        
    }//fin metodo resta hexadecimal
    
    // Función para completar un número binario a 8 bits (un byte)
    public static String completarBinarioAByte(String binario) {
        int longitud = binario.length(); // Obtiene la longitud del número binario
        if (longitud < 8) {
            int cantidadCeros = 8 - longitud; // Calcula cuántos ceros se deben agregar al principio
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
    }
    
    public static void postREL(Linea relativo, String origen){
        String postbyte = " ";
        String rr = " ";
        String destino=" ";
        int ori, dest,resta;
        boolean destinoExist=false;
        
        destino=conlocEtq(relativo.getOperando());
        if (absoluta){
            for(Linea existe:Parte_4.LineasASM){
                if(existe.getConloc().equals(destino)){
                    destinoExist=true;
                }
            }
            if(!destinoExist){
                destino=" ";
            }
            absoluta=false;
        }
        
        String frmbase [] = relativo.getForm().split(",");
        if (!(destino.equals(" "))) {
            postbyte = frmbase[0];
            ori = Parte_4.ConvertirADecimal("$".concat(origen));
            dest = Parte_4.ConvertirADecimal("$".concat(destino));
            if (ori > dest) {
                System.out.println("SI ENTRO");
                resta = ori - dest;
                System.out.println("resta en decimal = "+resta);
                if (resta < 129) {
                    rr = Salvacion.decimalABinario(resta);
                    System.out.println("en binario = "+rr);
                    rr = completarBinarioAByte(rr);
                    System.out.println("en byte = "+rr);
                    rr = Salvacion.calcularComplementoDos(rr);
                    System.out.println("Complemento = "+rr);
                    rr = Integer.toHexString(Parte_4.ConvertirADecimal(rr)).toUpperCase();
                    postbyte = postbyte.concat(" ").concat(rr);
                }
            } else if (dest > ori) {
                resta = dest - ori;
                if (resta < 128) {
                    rr = Salvacion.decimalABinario(resta);
                    rr = Integer.toHexString(Parte_4.ConvertirADecimal(rr)).toUpperCase();
                    if (rr.length() == 1) {
                        rr = "0".concat(rr);
                    }
                    postbyte = postbyte.concat(" ").concat(rr);
                }
            }

        }
        relativo.setCop(postbyte);
    }//Fin postbyte de lo rel 8 y 16
    
    static void buscarRels(){
        for(Linea asm:Parte_4.LineasASM){
            if(asm.getADDR().equals("REL (8b)")){
                postREL(asm, Conloc.sumarHexadecimal(asm.getConloc(), 2));
            }
        }
    }
}
