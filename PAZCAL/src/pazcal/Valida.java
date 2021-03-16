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
            for (String ln : this.cargaInformacion.codigoArchivo) {
                System.out.println("Linea -> " + ln);

               i = ValidaTamanoLinea(ln);
               i += ValidaPuntoyComa(ln);

            }
        } catch (Exception e) {
            System.out.println("Clase Valida-> RealizaValidaciones()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int ValidaTamanoLinea(String ln) {
        try {
            
            int resultado;
            boolean imprime;
            resultado = (ln.length() < 150) ? 1 : 0;
            if (resultado > 0) {
                System.out.println("Linea -> "+ln+"\nTamano Correcto Linea");
            } else {
                System.out.println("Linea -> "+ln+"\nTamano Incorrecto Linea");
            }
            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaTamanoLinea()=> " + e.getMessage());
            e.printStackTrace();
            return -1;

        }
    }

    private int ValidaPuntoyComa(String ln) {
        try {
            int resultado;
            String str = "\\;$";

            Pattern ptr = Pattern.compile(str);
            Matcher mtch = ptr.matcher(ln);
            resultado = (mtch.find()) ? 1 : 0;

            if (resultado > 0) {
                System.out.println("Linea -> "+ln+"\nContiene punto y coma");
            } else {
                System.out.println("Linea -> "+ln+"\nNo contiene punto y coma");
            }

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaPuntoyComa()=> " + e.getMessage());
            e.printStackTrace();
            return -1;

        }
    }

    private int ValidaBlancos(String ln) {
        try {
            int resultado;
            String str = "[\\s]{150,}";
            Pattern ptr = Pattern.compile(str);

            Matcher match = ptr.matcher(ln);

            resultado = (match.matches()) ? 1 : 0;

            if (resultado > 0) {
                System.out.println("Se encontraron 150 espacios en blanco");
            } else {
                System.out.println("Se encontraron mas de 150 espacios en blanco");
            }

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaBlancos()=> " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private int ValidaSuperfluos(String ln) {
        try {
            int resultado;
            String strInicio = "(^\\s)*";
            Pattern ptr = Pattern.compile(strInicio);

            Matcher match = ptr.matcher(ln);

            resultado = (match.matches()) ? 1 : 0;

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaBlancos()=> " + e.getMessage());
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

    private int ValidaComentarios(String ln, int nLn) {
        try {
            String patron = "\\(\\*([\\.]+)\\*\\)\\;|\\{([\\.]+)\\)\\;";

            Pattern ptr = Pattern.compile(patron, Pattern.MULTILINE);
            Matcher mtch = ptr.matcher(ln);

            int resultado;

            resultado = (mtch.matches()) ? 1 : 0;

            return resultado;
        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaComentarios()=> " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

}
