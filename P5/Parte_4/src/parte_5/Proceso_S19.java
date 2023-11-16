package parte_5;

import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author anaya
 */
public class Proceso_S19 {
    static ArrayList <S19> Datos = new ArrayList<>();
    static S19 AuxS19;
  
        public static void NombreASM(String nombreArchivo) {
        System.out.print("data: ");
        for (int i = 0; i < nombreArchivo.length(); i++) {
            char caracter = nombreArchivo.charAt(i);
            int codigoASCII = (int) caracter;
            String hexa = Integer.toHexString(codigoASCII); //Perdon, hice esto en una copia y se me olvido copiarla a la original
            System.out.print(hexa + " ");
        }
    }
}
