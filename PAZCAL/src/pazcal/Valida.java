/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        LLAVE_DER {
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


    private HashMap<Integer, String> posicionComentarios = new HashMap<>();
    private ArrayList<Integer> posicionTamanoLinea = new ArrayList<>();
    private ArrayList<Integer> posicionPuntoComa = new ArrayList<>();

    protected CargaInformacion cargaInformacion;
    protected boolean[] esProgramaValido = new boolean[5]; //cuando se genera un error que es de PAZCAL el valor debe de ser false para no compilar 
    //y solo generar archivo errores y no llamar pascal.

    public Valida() {

    }

    public Valida(Object o) {
        System.out.println("Validando...");
        this.cargaInformacion = (CargaInformacion) o;
        RealizaValidaciones();
        ImprimeArreglos();
    }

    protected void RealizaValidaciones() {
        try {

            int comentarios = 0;

            boolean evalua = true;

            //System.out.println("Validando linea...");
            while (evalua) {
                this.esProgramaValido[0] = tamanolineasValidas();
                this.esProgramaValido[1] = lineasPuntoComa();
                this.esProgramaValido[2] = EncuentraComentarios();
                evalua = false;
            }

            for (int i = 0; i < this.esProgramaValido.length; i++) {
                switch (i) {
                    case 0: //Case para tamano de lineas
                        if (this.esProgramaValido[i]) {
                            System.out.println("CANTIDAD CARACTERES........ [OK]");
                        } else {
                            System.out.println("CANTIDAD CARACTERES........ [ERROR]");
                        }
                        break;
                    case 1:
                        if (this.esProgramaValido[i]) {
                            System.out.println("PUNTO Y COMA........ [OK]");
                        } else {
                            System.out.println("PUNTO Y COMA........ [ERROR]");
                        }
                        break;
                    case 2:
                        if (this.esProgramaValido[i]) {
                            System.out.println("COMENTARIOS........ [OK]");
                        } else {
                            System.out.println("COMENTARIOS........ [ERROR]");
                        }
                        break;

                    default:
                        break;

                }

            }

        } catch (Exception e) {
            System.out.println("Clase Valida-> RealizaValidaciones()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    //#region MetodosValidacionLineas
    private boolean tamanolineasValidas() {

        int cantidadErrores = 0;
        boolean resultado = false;
        cantidadErrores = ValidaTamanoLinea();

        return !(cantidadErrores > 0);
    }

    private int ValidaTamanoLinea() {
        try {
      
            this.cargaInformacion.codigoArchivo.forEach((e) -> {
                int nLn = 0;
                boolean resultado;
                nLn = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                resultado = (e.length() > 150);
                if (resultado) {

                    this.posicionTamanoLinea.add(nLn); //almacena el numero de linea donde se encuentro el error
                    //System.out.println("Linea Numero -> " + nLn + " CANTIDAD CARACTERES........ [Error]");
                    //msgE = "\t\tERROR 0001: Linea con mas de los caracteres soportados ->" + ln.length();
                    //MensajeError(msgE, nLn);

                }
            });

            Collections.sort(this.posicionTamanoLinea);

            //System.out.println("Cantidad de Caracteres = " + ln.length());
            return this.posicionTamanoLinea.size();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaTamanoLinea()=> " + e.getMessage());
            e.printStackTrace();
            return 1;

        }
    }

    //#endregion MetodosValidacionLineas
    //#region MetodosValidacionesPuntoComa
    private boolean lineasPuntoComa() {

        int cantidadErrores = 0;
        boolean resultado = false;
        cantidadErrores = ValidaPuntoyComa();

        return !(cantidadErrores > 0);
    }

    private int ValidaPuntoyComa() {
        try {
            String str = "\\.*\\;";

            Pattern ptr = Pattern.compile(str);

            this.cargaInformacion.codigoArchivo.forEach((e) -> {
                int nLn = 0;
                nLn = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                boolean encontro = false;
                Matcher mtch = ptr.matcher(e);
                encontro = mtch.find();
                if (!encontro) {

                    this.posicionPuntoComa.add(nLn);
                    //System.out.println("Linea Numero -> " + nLn + " -> " + ln + "\nPunto y Coma.......[ERROR]");
                    //String msgE = "\t\tERROR 0002: Linea " + (nLn) + " no termina con =>;";
                    //MensajeError(msgE, nLn);
                }

            });
            Collections.sort(this.posicionPuntoComa);
            return this.posicionPuntoComa.size();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaPuntoyComa()=> " + e.getMessage());
            e.getCause();
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

    private boolean EncuentraComentarios() {
        try {

            boolean resultado = false;

            String llaveDer = null, llaveIzq = null, parIzq = null, parDer = null;
            int nLn = 0;
            int llaveD = 0;
            int llaveI = 0;
            int parI = 0;
            int parD = 0;
            int posicionL = 0;
            int posicionP = 0;
            String hileraL = "";
            String hileraP = "";
            String msgE = "\t\tERROR 0003: =>";

            PARENTESIS[] valores = PARENTESIS.values();
            for (String ln : this.cargaInformacion.codigoArchivo) {
                String[] arr = ln.split("\\s");
                nLn = this.cargaInformacion.codigoArchivo.indexOf(ln) + 1;

                for (PARENTESIS p : valores) {
                    for (int i = 0; i < arr.length; i++) {

                        if (arr[i].equalsIgnoreCase(p.toString())) {
                            //almacena donde se encuentran los parentesis o llaves 
                            this.posicionComentarios.put(nLn, p.name());

                            switch (PARENTESIS.valueOf(p.name())) {

                                case LLAVE_IZQ:
                                    llaveI++;
                                    llaveIzq = arr[i];
                                    posicionL = nLn;
                                    hileraL += "\t" + posicionL + " " + ln + "\n";

                                    break;
                                case LLAVE_DER:
                                    llaveD++;
                                    llaveDer = arr[i];
                                    posicionL = nLn;
                                    hileraL += "\t" + posicionL + " " + ln + "\n";

                                    break;
                                case PARENTESIS_IZQ:
                                    parI++;
                                    parIzq = arr[i];
                                    posicionP = nLn;
                                    hileraP += "\t" + posicionP + " " + ln + "\n";

                                    break;

                                case PARENTESIS_DER:
                                    parD++;
                                    parDer = arr[i];
                                    posicionP = nLn;
                                    hileraP += "\t" + posicionP + " " + ln + "\n";

                                    break;

                                default:
                                    break;
                            }
                        }

                    }

                }
            }

            int sumaLLaves = 0, sumaParentesis = 0;
            sumaLLaves = llaveI + llaveD;
            sumaParentesis = parD + parI;
            boolean esParLlave = (sumaLLaves % 2 == 0);
            boolean esParParen = (sumaParentesis % 2 == 0);

            if (!esParLlave || !esParParen) {

                // System.out.println(hileraL);
                //  MensajeError(hileraL, posicionL);
                resultado = false;
            }

            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaProgram()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void MensajeError(String msgE, int nLn) {
        try {
            System.out.println("Generando ERRORES...");
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

    private void ImprimeArreglos() {
        boolean imprimir = true;

        while (imprimir == true) {
            this.posicionPuntoComa.forEach((e) -> {
                System.out.println("Lineas que no tienen Punto y Coma-> " + e);
            });

            this.posicionTamanoLinea.forEach((e) -> {
                System.out.println("Linea que excede el tamano-> " + e);
            });
            int val = 0;

            this.posicionComentarios.keySet().forEach((e) -> {
                System.out.println("Posicion-> " + e + " Tipo LLave-> " + this.posicionComentarios.get(e));
            });
            imprimir = false;
        }

    }

}
