
package parte_3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MetodosP3 {
    static File archivo = new File("P1ASM.lst");
    static File archivo2 = new File("TABSIM.txt");
    
    static void CrearArchivoList(){ 
        try {
            // Eliminar el archivo si ya existe
            if (archivo.exists()) {
                archivo.delete();
            }

            // Crear el archivo nuevamente
            archivo.createNewFile();

            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst", "rw");
            auxArchivo.seek(auxArchivo.length());

            // Escribir en el documento
            auxArchivo.writeBytes("TIPO,        ");
            auxArchivo.writeBytes("VALOR,       ");
            auxArchivo.writeBytes("ETQ,         ");
            auxArchivo.writeBytes("CODOP,       ");
            auxArchivo.writeBytes("OPER \n");

            // Cerrar el archivo después de escribir
            auxArchivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("No se ha creado el archivo");
        }
    }//fin crear
    
    public static String sumarHexadecimal(String hexadecimal, int entero) {
        // Convertir el valor hexadecimal a entero
        int valorHexadecimal = Integer.parseInt(hexadecimal, 16);

        // Sumar el entero al valor hexadecimal
        int suma = valorHexadecimal + entero;

        // Obtener la representación hexadecimal del resultado
        String resultadoHexadecimal = Integer.toHexString(suma).toUpperCase();

        return resultadoHexadecimal;
    }
    
    //funcion para llenar el txt list
    static void LlenarList(ArrayList <Linea> AuxLineasCodigo) {
        int bytes=0;//variable int de apoyo
        String valor="0";
        boolean equ=false;//variable booleana de apoyo
        CrearArchivoList(); //mandamos llamar el metodo crear list
        Creartabsim();//mandamos llamar el metodo crear tabsim
        try{
            RandomAccessFile auxArchivo = new RandomAccessFile("P1ASM.lst","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){ //ciclo for para excribir en linea
                if(!(auxiliar.getSize().equals(" "))){//analiza el valor de size
                    auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                    if(auxiliar.getCodop().equals("ORG")){//analiza si el CODOP es igual a ORG para seguir validando
                        if(!(auxiliar.getADDR().equals("ERROR"))){//analiza si el ADDR es erroneo "ERROR".
                            auxArchivo.writeBytes("DIR_INIC,        "); // escribe esto en el txt list
                            valor=Parte_3.validarDireccion(auxiliar.getOperando());
                            //la variable valor toma el valor de operando
                            auxArchivo.writeBytes("$".concat(valor)+",       ");
                            //deja un espacio en el txt para organizarce
                        }//fin de segundo if
                        else{
                            System.out.println("NO SE PUEDE CALCULAR EL CONLOC PORQUE OPR DE ORG ES INCORRECTO"); //manda error por fallas en operando 
                        }//fin del else
                    }//fin del primer if
                    else if(auxiliar.getCodop().equals("EQU")){ //analiza que el OPR sea = EQU
                        auxArchivo.writeBytes("VALOR,          ");// escribe valor y deja un espacio para organizarse
                        auxArchivo.writeBytes(Parte_3.validarDireccion(auxiliar.getOperando())+",       ");//escribe esto en el txt
                        auxiliar.setConloc(Parte_3.validarDireccion(auxiliar.getOperando()));//valida la direccion y la establece en conloc
                        equ=true;
                    }//fin de else if
                    else{
                        if(!(valor.equals("0"))){//valida que no sea 0 la varaible valor
                            auxArchivo.writeBytes("CONTLOC,       ");// escribe esto en el txt list
                            valor= sumarHexadecimal(valor,bytes);//suma los bytes al valor
                            auxArchivo.writeBytes(Parte_3.validarDireccion(valor)+",       ");//escribe en el txt list el nuevo valor
                            auxiliar.setConloc(Parte_3.validarDireccion("$".concat(valor))); //toma conloc el nuevo valor
                            String[] campos= auxiliar.getSize().split("\\s+");//agarra el valor de la posicion
                            bytes=Integer.parseInt(campos[0]);//lo converte de String a entero
                        }//fin del if
                    }//fin de else
                    if(!(valor.equals("0")) || equ){//revisa que valor sea diferente a 0 o a la variable equ
                        if(auxiliar.getEtiqueta().equals(" ")){// revisa si el valor.etiqueta es igual a nada
                            auxArchivo.writeBytes("NULL,       "); //escribe null
                        }//fin del if
                        else{//inicio de else
                            auxArchivo.writeBytes(auxiliar.getEtiqueta()+",       "); //escribe esto en el txt
                        }//fin del else
                        auxArchivo.writeBytes(auxiliar.getCodop()+",       ");//escribe esto en el txt
                        if(auxiliar.getOperando().equals(" ")){//valida que sea igual a nada, pues a " ".
                            auxArchivo.writeBytes("NULL,       ");//escribe esta parte en el txt list ...
                        }
                        else{
                            auxArchivo.writeBytes(auxiliar.getOperando()+",       ");
                        }
                        auxArchivo.writeBytes("\n");
                    }
                    equ=false;
                }
            }
        }catch(IOException ex){
            System.out.println("ERROR");
            ex.printStackTrace();
        }//fin catch
    }    
    
    //metodo para crear la tabsim
    static void Creartabsim(){
        try {
            // Eliminar el archivo si ya existe
            if (archivo2.exists()) {//si ya existe uno
                archivo2.delete();//borra el anterior para que no haga muchos
            }

            // Crear el archivo nuevamente
            archivo2.createNewFile();

            RandomAccessFile auxArchivo = new RandomAccessFile("TABSIM.txt", "rw");
            auxArchivo.seek(auxArchivo.length());

            // Escribir en el documento
            auxArchivo.writeBytes("TIPO            ");//escribe en txt
            auxArchivo.writeBytes("Si       ");//escribe en txt
            auxArchivo.writeBytes("Ti            ");//escribe en txt
            auxArchivo.writeBytes("\n");//escribe en txt

            // Cerrar el archivo después de escribir
            auxArchivo.close();//cerrar archivo y guardar
        } catch (IOException ex) {
            ex.printStackTrace(); //escribe en consola
            System.out.println("No se ha creado el archivo");//en caso de error no crea nada y manda aviso
        }
    }// fin metodo crear tabsim
    
    //funcion para llenar el tabsim
    //ya esta creado, solo falta llenarlo con la siguiente funcion...
    static void LlenarTabsim(ArrayList <Linea> AuxLineasCodigo) {
        Creartabsim(); //mandamos a llamar primero la funcion de crear
        try{ // try para el proceso
            RandomAccessFile auxArchivo = new RandomAccessFile("TABSIM.txt","rw"); //Encuentro el archivo y accedo para leer y escribir
            for(Linea auxiliar : AuxLineasCodigo){ //bucle for each para escribir el txt
                auxArchivo.seek(auxArchivo.length()); //Seek posiciona el puntero donde escribir, length es para decirle donde esta el final
                if(!(auxiliar.getEtiqueta().equals(" "))&& !(auxiliar.getADDR().equals("ERROR"))){
                 //en la anterior linea  comprueba si los valores de etiqueta y ADDR
                 //del objeto Linea son diferentes de " " y "ERROR", para seguir con la validacion
                    if(auxiliar.getCodop().equals("EQU")){//analiza si el CODOP es igual a EQU
                        auxArchivo.writeBytes("ABSOLUTA       ");// aqui valida que sea Absoluta si y solo si es de forma EQU
                    }//fin del segundo if
                    else{//si no es EQU(absoluta),entonces...
                        auxArchivo.writeBytes("RELATIVA       "); //es relativa, por que es de forma ORG
                    }//fin del else
                    auxArchivo.writeBytes(auxiliar.getEtiqueta()+"       "); // escribe la etiqueta en txt
                    auxArchivo.writeBytes(auxiliar.getConloc()+"       "); // escribe el CONTLOC en el txt
                    auxArchivo.writeBytes("\n"); //hace el salto de linea para la siguiente instruccion...
                }//fin del primer if 
            } //fin del bucle for
                
        }catch(IOException ex){ //catch por si no es valido el proceso
            System.out.println("ERROR");//imprimer error
            ex.printStackTrace();//imprime en la consola
        }//fin catch
    } //fin del metodo llenar tabsim
    
}
 