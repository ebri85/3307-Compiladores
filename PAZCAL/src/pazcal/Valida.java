/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Esau Brizuela Ruiz
 */
public class Valida {

    protected CargaInformacion cargaInformacion;

    public Valida() {

    }

    public Valida(Object o) {
        System.out.println("Validando...");
        this.cargaInformacion = (CargaInformacion) o;
        RealizaValidaciones();
    }
//Pendiente enviar errores a archivo Errores

    protected void RealizaValidaciones() {
        try {
            int i = 0;

            boolean encuentraReservada = false;
            boolean validaProgram = false;
//            for (String ln : this.cargaInformacion.codigoArchivo) {
//                System.out.println("Linea -> " + ln);
//                
//            }
            ValidaComentarios();
        } catch (Exception e) {
            System.out.println("Clase Valida-> RealizaValidaciones()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ValidaTamanoLinea(String ln, int nLn) {
        try {

            boolean resultado;
            String msgE = "ERROR 0001: Linea con mas de los caracteres soportados";
            System.out.println("Cantidad de Caracteres = " + ln.length());
            resultado = (ln.length() <= 150);
            if (resultado) {
                System.out.println("Linea -> " + ln + "\nTamano Correcto Linea");
            } else {
                System.out.println("Linea -> " + ln + "\nTamano Incorrecto Linea");
                MensajeError(msgE, nLn);
            }

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaTamanoLinea()=> " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void ValidaPuntoyComa(String ln, int nLn) {
        try {
            int resultado;
            String msgE = "ERROR 0002: Instruccion no termina con =>;";
            String str = ".*\\;$";

            Pattern ptr = Pattern.compile(str);
            Matcher mtch = ptr.matcher(ln);

            if (mtch.matches()) {
                System.out.println("Linea -> " + ln + "\nContiene punto y coma");
            } else {
                System.out.println("Linea -> " + ln + "\nNo contiene punto y coma");
                MensajeError(msgE, nLn);
            }

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaPuntoyComa()=> " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void ValidaBlancos(String ln, int nLn) {
        try {

            String msgE = "ERROR 0003: Linea con mas espacios en blancos de lo permitido";
            String str = "[\\s]{150,}";
            Pattern ptr = Pattern.compile(str);

            Matcher match = ptr.matcher(ln);

            if (match.matches()) {
                System.out.println("Se encontraron 150 espacios en blanco");
            } else {
                System.out.println("Se encontraron mas de 150 espacios en blanco");
                MensajeError(msgE, nLn);
            }

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaBlancos()=> " + e.getMessage());
            e.printStackTrace();

        }
    }

    private int ValidaSuperfluos(String ln, int nLn) {
        try {
            int resultado;

            String strInicio = "(\\s)?"; //<- deberia de validar si existe 2 o mas espacios en blanco
            Pattern ptr = Pattern.compile(strInicio);

            Matcher match = ptr.matcher(ln);

            resultado = (match.matches()) ? 1 : 0;

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaSuperfluos()=> " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    protected boolean EncuentraReservadas(String ln) {
        try {
            boolean resultado = false;
            int cont = 0;
            boolean continua = true;

            while (continua) {
                String[] arr = ln.split("\\s");
                for (String reservada : this.cargaInformacion.reservadasPascal) {

                    for (int i = 0; i < arr.length; i++) {
                        Pattern ptr = Pattern.compile(reservada, Pattern.CASE_INSENSITIVE);
                        Matcher mt = ptr.matcher(arr[i]);
                        if (mt.find()) {
                            cont++;
                        }

                    }

                }
                continua = false;
            }

            resultado = (cont > 0);

            //System.out.println("Se encontraron " + cont + " reservadas");
            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> EncuentraReservadas()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean ValidaProgram(String ln) {
        try {
            boolean resultado = false;
            boolean continua = true;
            while (continua) {
                String patron = "[PROGRAM]\\s+[a-z]{15}\\s+\\(\\);|[PROGRAM]\\s+[a-z]{15}\\s+\\(\\s*[input\\s*,\\s*output]\\s*\\);|[PROGRAM]\\s+[a-z]{15}\\s+\\(\\s*[input]\\s*\\);|[PROGRAM]\\s+[a-z]{15}\\s+\\(\\s*[\\s*output]\\s*\\);"; //PROGRAM NombreDePrograma ( INPUT, OUTPUT );

                Pattern ptr = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);
                Matcher mtch = ptr.matcher(ln);

                resultado = mtch.matches();
                continua = false;
            }
            if (resultado) {
                System.out.println("Formato de Instruccion Program correcto");
            } else {
                System.out.println("Formato de Instruccion Program incorrecto");
            }

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaProgram()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void ValidaComentarios() {
        try {
            //String patron = "\\(\\*\\s*(\\.?)\\s*\\*\\)\\;|\\{\\s*[\\.]?\\s*\\}\\;";
            String patron  = "(\\{+)";
            Pattern ptr = Pattern.compile(patron);

            for (String ln : this.cargaInformacion.codigoArchivo) {
                Matcher mtch = ptr.matcher(ln);
                System.out.println("Linea -> " + ln);  
                System.out.println("Encontro -> " + mtch.find());

            }

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaComentarios()=> " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void MensajeError(String msgE, int nLn) {
        try {

            String strL = null;
            String str = null;
            strL = this.cargaInformacion.lineasParaArchivoErrores.get(nLn);
            this.cargaInformacion.lineasParaArchivoErrores.remove(nLn);
            str = strL + "\n"
                    + msgE;

            this.cargaInformacion.lineasParaArchivoErrores.add(nLn, str);

        } catch (Exception e) {
            System.out.println("Clase Valida-> MensajeError()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
