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
    
    enum PARENTESIS {
    LLAVE_IZQ {
        @Override
        public String toString() {
            return "{";
        }
    ;
    },
     LLAVE_LLAVE_DER {
        @Override
        public String toString() {
            return "}";
        }
    ;
    },
      PARENTESIS_IZQ {
        @Override
        public String toString() {
            return "(*";
        }
    ;
    },
      PARENTESIS_DER {
        @Override
        public String toString() {
            return "*)";
        }
    ;
};

}

    protected CargaInformacion cargaInformacion;
    protected boolean[] esProgramaValido = new boolean[5]; //cuando se genera un error que es de PAZCAL el valor debe de ser false para no compilar 
    //y solo generar archivo errores y no llamar pascal.

    public Valida() {

    }

    public Valida(Object o) {
        System.out.println("Validando...");
        this.cargaInformacion = (CargaInformacion) o;
        RealizaValidaciones();
    }

    protected void RealizaValidaciones() {
        try {
            int posArrL = 0;
            int comentarios = 0;
            String hilera = null;

            boolean encuentraReservada = false;
            boolean validaProgram = false;
            for (String ln : this.cargaInformacion.codigoArchivo) {
                System.out.println("Validando linea...");
                this.esProgramaValido[0] = tamanolineasValidas(ln, posArrL);
                this.esProgramaValido[1] = lineasPuntoComa(ln, posArrL);
                comentarios = EncuentraComentarios(ln, posArrL);
                posArrL++;

            }

            for (int i = 0; i < this.esProgramaValido.length; i++) {
                switch (i) {
                    case 0: //Case para tamano de lineas
                        if (this.esProgramaValido[i]) {
                            System.out.println("CANTIDAD CARACTERES........ [OK]");
                        }
                        break;
                    case 1:
                        if (this.esProgramaValido[i]) {
                            System.out.println("PUNTO Y COMA........ [OK]");
                        }
                        break;
                    case 2:
                        if (this.esProgramaValido[i]) {
                            System.out.println("COMENTARIOS........ [OK]");
                        }
                        break;

                }

            }

            System.out.println("Inicio de Comentarios encontrados = > " + comentarios);
        } catch (Exception e) {
            System.out.println("Clase Valida-> RealizaValidaciones()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    //#region MetodosValidacionLineas
    private boolean tamanolineasValidas(String ln, int nLn) {

        int cantidadErrores = 0;
        boolean resultado = false;
        cantidadErrores = ValidaTamanoLinea(ln, nLn);

        return !(cantidadErrores > 0);
    }

    private int ValidaTamanoLinea(String ln, int nLn) {
        try {
            int contErrores = 0;

            boolean resultado;
            String msgE = null;
            resultado = (ln.length() > 150);
            if (resultado) {
                contErrores++;
                System.out.println("Linea Numero -> " + nLn + " CANTIDAD CARACTERES........ [Error]");
                msgE = "\t\tERROR 0001: Linea con mas de los caracteres soportados ->" + ln.length();
                MensajeError(msgE, nLn);
            }
            //System.out.println("Cantidad de Caracteres = " + ln.length());
            return contErrores;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaTamanoLinea()=> " + e.getMessage());
            e.printStackTrace();
            return 1;

        }
    }

    //#endregion MetodosValidacionLineas
    //#region MetodosValidacionesPuntoComa
    private boolean lineasPuntoComa(String ln, int nLn) {

        int cantidadErrores = 0;
        boolean resultado = false;
        cantidadErrores = ValidaPuntoyComa(ln, nLn);

        return !(cantidadErrores > 0);
    }

    private int ValidaPuntoyComa(String ln, int nLn) {
        try {
            int resultado;
            int contErrores = 0;

            String str = "\\;$";

            Pattern ptr = Pattern.compile(str);
            Matcher mtch = ptr.matcher(ln);

            if (!mtch.find()) {
                contErrores++;
                System.out.println("Linea Numero -> " + nLn + " -> " + ln + "\nPunto y Coma.......[ERROR]");
                String msgE = "\t\tERROR 0002: Linea " + (nLn) + " no termina con =>;";
                MensajeError(msgE, nLn);
            }
            return contErrores;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaPuntoyComa()=> " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    //#region MetodosValidacionesPuntoComa
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

    private int EncuentraComentarios(String ln, int nLn) {
        try {
            int resultado = 0;
            int sumaParentesis = 0;
            String[] arr = ln.split("\\s");
            //String patron = "(\\{)|(\\})|(\\(\\*)|(\\*\\))";
            String[] patron = {"{", "}", "(*", "*)"};
            

            for (int j = 0; j < patron.length; j++) {
                //Pattern ptr = Pattern.compile(patron[j]);

                for (int i = 0; i < arr.length; i++) {
                    // Matcher mtch = ptr.matcher(arr[i]);

                    if (arr[i].equalsIgnoreCase(patron[j])) {
                        sumaParentesis++;
                        System.out.println("Se encontro parentesis-> " + patron[j]);

                    }

                }
            }
            
            
            System.out.println("Cantidad de parentesis encontrados-> " + sumaParentesis);

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaProgram()=> " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private int ValidaComentarios() {
        try {
            int resultado;
            int encontro = 0;
            int contErrores = 0;
            //String patron = "\\(\\*\\s*(\\.?)\\s*\\*\\)\\;|\\{\\s*[\\.]?\\s*\\}\\;";
            String patron = "(\\{+)|(\\(\\*+)|(\\}+)|(\\*\\)+)";

            Pattern ptr = Pattern.compile(patron);

            for (String ln : this.cargaInformacion.codigoArchivo) {
                Matcher mtch = ptr.matcher(ln);
                System.out.println("Linea -> " + ln);
                if (mtch.find() == true) {
                    System.out.println("Encontro -> " + mtch.toMatchResult());

                    encontro++;
                }

            }

            return encontro;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaComentarios()=> " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private void MensajeError(String msgE, int nLn) {
        try {
            System.out.println("Generando ERROR...");
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
