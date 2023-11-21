package parte_5;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Proceso_S19 {

    static List<S19> Datos = new ArrayList<S19>();
    static S19 AuxS19;
    static String archivoASM;

    public static String NombreASM() {
        String nombreArchivo = archivoASM;
        String data = "";
        if(archivoASM.equals("P5ASM.asm")) {

        } else {
            // Obtener un objeto Path
            Path path = Paths.get(archivoASM);
            // Obtener el nombre del archivo
            nombreArchivo = path.getFileName().toString();
        }
        System.out.println("Nombre del archivo = "+nombreArchivo);
        for (int i = 0; i < nombreArchivo.length(); i++) {
            char caracter = nombreArchivo.charAt(i);
            int codigoASCII = (int) caracter;
            String hexa = Integer.toHexString(codigoASCII).toUpperCase();  // Convertir a mayúsculas
            data = data + hexa.concat(" ");
            //System.out.print(hexa + " ");           
        }
        //System.out.println();
        return data;
    }

    public static String cc(String data) {
        String bytes[] = data.split(" ");
        int NumeroDeBytes = bytes.length;
        int resultado = NumeroDeBytes + 3;
        // Asegurarse de que el resultado tenga siempre dos dígitos
        String sumaDeBytes = String.format("%02X", resultado);
        return sumaDeBytes;
    }
    
    public static String sumarHexadecimal(String hex1) {
        // Separar los valores hexadecimales por espacios
        String[] hexArray = hex1.split("\\s+");
        int sumaDecimal = 0;//Variable que guarada la suma
 
        for(int i=0; i<hexArray.length;i++){
            sumaDecimal = sumaDecimal+Parte_5.ConvertirADecimal("$".concat(hexArray[i]));
        }
        // Convertir la suma a formato hexadecimal
        String sumaHexadecimal = Integer.toHexString(sumaDecimal).toUpperCase();
        // Devolver el resultado
        return sumaHexadecimal;
    }
    
    public static String ck(String cc, String addr, String data) {
        String suma = "00";
        String bitLessImpor = " ";
        int entero = 0;
        suma=sumarHexadecimal(cc.concat(" ").concat(data).concat(" ").concat(addr));//Suma de bytes

        if(suma.length() < 2){
            bitLessImpor=suma;
        }//Fin guardar valor en hexa de la suma
        else{
            bitLessImpor = suma.substring(suma.length()-2,suma.length());
        }//Fin si es un byte identificar el menos importante
        entero = Parte_5.ConvertirADecimal("$".concat(bitLessImpor)); //valor decimal de la suma
        suma=Fase2.decimalABinario(entero);//Calcular valor en binario
        //COMPLETAR A BYTE
        suma = Fase2.completarBinarioAByte(suma, 8);
        
        // CALCULO DEL COMPLEMENTO A1
        StringBuilder complementoUno = new StringBuilder();
        for (int i = 0; i < suma.length(); i++) {
            char bit = suma.charAt(i);
            complementoUno.append((bit == '0') ? '1' : '0'); // Invertir cada bit del número binario
        }
        
        //Calcular hexadecimal del complemento uno
        bitLessImpor = Integer.toHexString(Parte_5.ConvertirADecimal("%".concat(complementoUno.toString())));
        if (bitLessImpor.length() < 2) {
            bitLessImpor = "0".concat(bitLessImpor);
        }//Completar a byte
        return bitLessImpor.toUpperCase();
    }

    public static void S0() {
        AuxS19 = new S19 ("S0"," ","00 00"," "," ");
        AuxS19.setData(NombreASM());
        AuxS19.setCc(cc(AuxS19.getData()));
        AuxS19.setCk(ck(AuxS19.getCc(),AuxS19.getAddr(),AuxS19.getData()));
        System.out.println(AuxS19.getSn().concat(" ")+AuxS19.getCc().concat(" ")+
                AuxS19.getAddr().concat(" ")+AuxS19.getData().concat(" ")+AuxS19.getCk());
        Datos.add(AuxS19);
    }
}
