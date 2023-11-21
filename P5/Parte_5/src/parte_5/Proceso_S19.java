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

    public static String ck(String cc, String addr, String data) {
        String suma = "00";
        String bitLessImpor = " ";
        String AuxBin = "", C2 = "";
        int entero = 0;
        String[] dta = data.split("\\s+");
        String[] addrSeparado = addr.split("\\s+");

        for (int i = 0; i < dta.length; i++) {
            suma = Conloc.sumarHexadecimal(dta[0], entero);
            entero = Integer.parseInt(suma, 16);
        }
        for (int i = 0; i < addrSeparado.length; i++) {
            suma = Conloc.sumarHexadecimal(addrSeparado[i], entero);
            entero = Integer.parseInt(suma, 16);
        }

        suma = Conloc.sumarHexadecimal(cc, entero);

        bitLessImpor = suma.substring(suma.length() - 2);

        entero = Integer.parseInt(bitLessImpor, 16);             //Convierto a decimal
        AuxBin = Integer.toBinaryString(entero);              //Convierto a binario
        C2 = Salvacion.calcularComplementoDos(AuxBin);           //Saco complemeto 2
        entero = Integer.parseInt(C2, 2);                        //Covierto el binario a decimal
        bitLessImpor = Integer.toHexString(entero);              //Convierto el decimal a hexadecimal

        if (bitLessImpor.length() < 2) {
            bitLessImpor = "0".concat(bitLessImpor);
        }

        return bitLessImpor;
    }

    public static void S0(String nombreArch, String ccS0, String dtaS0, String ckS0) {
        Datos.add(new S19(nombreArch, "S0", ccS0, "00 00", dtaS0, ckS0));
        System.out.println("S0 agregado con exito");
    }
}
