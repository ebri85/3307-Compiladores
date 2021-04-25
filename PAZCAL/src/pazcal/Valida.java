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
                return "};";
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
    private ArrayList<Integer> posicionProgram = new ArrayList<>();
    private ArrayList<Integer> posicionVar = new ArrayList<>();
    private ArrayList<Integer> posicionDefVar = new ArrayList<>();
    private ArrayList<Integer> posicionReservadas = new ArrayList<>();
    private HashMap<Integer, String> noReservadas = new HashMap<>();
    private ArrayList<Integer> posicionBeginEnd = new ArrayList<>();
    private ArrayList<Integer> posicionReadLn = new ArrayList<>();
    private ArrayList<Integer> posicionWriteLn = new ArrayList<>();

    protected CargaInformacion cargaInformacion;
    protected boolean[] esProgramaValido = new boolean[10]; //cuando se genera un error que es de PAZCAL el valor debe de ser false para no compilar 
    //y solo generar archivo errores y no llamar pascal.

    public Valida() {

    }

    public Valida(Object o) {
        System.out.println("Validando...");
        this.cargaInformacion = (CargaInformacion) o;
        RealizaValidaciones();
        //ImprimeArreglos();
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
                this.esProgramaValido[3] = ValidaProgram();
                this.esProgramaValido[4] = ValidaVar();
                this.esProgramaValido[5] = EncuentraReservadas();
                this.esProgramaValido[6] = ValidaBeginEnd();
                this.esProgramaValido[7] = ValidarReadLn();
                this.esProgramaValido[8] = ValidarWriteLn();
                this.esProgramaValido[9] = (EncuentraNoReservadas() != null || EncuentraNoReservadas() != "EXCEPTION");

                evalua = false;
            }

            for (int i = 0; i < this.esProgramaValido.length; i++) {
                switch (i) {
                    case 0: //Case para tamano de lineas
                        if (this.esProgramaValido[i]) {
                            System.out.println("CANTIDAD CARACTERES........ [OK]");
                        } else {
                            System.out.println("CANTIDAD CARACTERES........ [ERROR]");

                            this.posicionTamanoLinea.forEach((e) -> {

                                String msgE = "\t\tERROR 0001: Linea con mas de los caracteres soportados ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;
                    case 1:
                        if (this.esProgramaValido[i]) {
                            System.out.println("PUNTO Y COMA........ [OK]");
                        } else {
                            System.out.println("PUNTO Y COMA........ [ERROR]");
                            this.posicionPuntoComa.forEach((e) -> {

                                String msgE = "\t\tERROR 0002: No se encontro Punto y Coma (;) ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;
                    case 2:
                        if (this.esProgramaValido[i]) {
                            System.out.println("COMENTARIOS........ [OK]");
                        } else {
                            System.out.println("COMENTARIOS........ [ERROR]");

                            this.posicionComentarios.keySet().forEach((e) -> {
                                String msgE = "\t\tERROR 0004: Validar el formato del comentario  ";
                                MensajeError(msgE, (e - 1));
                            });

                        }
                        break;

                    case 3:
                        if (this.esProgramaValido[i]) {
                            System.out.println("FORMATO PROGRAM........ [OK]");
                        } else {
                            System.out.println("FORMATO PROGRAM........ [ERROR]");
                            this.posicionProgram.forEach((e) -> {

                                String msgE = "\t\tERROR 0005: Error en la estructura del la instruccion PROGRAM ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;
                    case 4:
                        if (this.esProgramaValido[i]) {
                            System.out.println("FORMATO VAR........ [OK]");
                        } else {
                            System.out.println("FORMATO VAR........ [ERROR]");
                            this.posicionDefVar.forEach((e) -> {

                                String msgE = "\t\tERROR 0006: Error en la estructura del la instruccion VAR ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;
                    case 5:
                        if (this.esProgramaValido[i]) {
                            System.out.println("ENCONTRO RESERVADAS........ [OK]");

                            this.posicionReservadas.forEach((e) -> {

                                String msgE = "\t\tMENSAJE se encontro Reservada que no pertenece a PAZCAL ";

                                MensajeError(msgE, (e - 1));
                            });
                        } else {
                            System.out.println("ENCONTRO RESERVADAS........ [NO]");
                        }
                        break;
                    case 6:
                        if (this.esProgramaValido[i]) {
                            System.out.println("BEGIN se encuentra antes de END........ [OK]");
                        } else {
                            System.out.println("BEGIN se encuentra antes de END........ [NO]");
                            this.posicionBeginEnd.forEach((e) -> {

                                String msgE = "\t\tERROR 0008: Error en la estructura del la instruccion BEGIN/END ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;

                    case 7:
                        if (this.esProgramaValido[i]) {
                            System.out.println("FORMATO READLN........ [OK]");
                        } else {
                            System.out.println("FORMATO READLN........ [ERROR]");
                            this.posicionReadLn.forEach((e) -> {

                                String msgE = "\t\tERROR 0009: Error en la estructura del la instruccion READLN ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                        break;
                    case 8:
                        if (this.esProgramaValido[i]) {
                            System.out.println("FORMATO WRITELN........ [OK]");
                        } else {
                            System.out.println("FORMATO WRITELN........ [ERROR]");
                            this.posicionWriteLn.forEach((e) -> {

                                String msgE = "\t\tERROR 0010: Error en la estructura del la instruccion WRITELN ";

                                MensajeError(msgE, (e - 1));
                            });
                        }
                    case 9:
                        if (this.esProgramaValido[i]) {
                            System.out.println("ENCUENTRA NO RESERVADAS........ [OK]");
                        } else {
                            System.out.println("ENCUENTRA NO RESERVADAS........ [ERROR]");
                            this.posicionComentarios.keySet().forEach((e) -> {

                                String msgE = "\t\tERROR 0011: " + this.noReservadas.get(e).toUpperCase() + " No es una palabra Reservada PASCAL o PAZCAL ";

                                MensajeError(msgE, (e - 1));
                            });
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
//msgE = "\t\tERROR 0001: Linea con mas de los caracteres soportados ->" + ln.length();
            this.cargaInformacion.codigoArchivo.forEach((e) -> {
                int nLn = 0;
                boolean resultado;
                nLn = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                resultado = (e.length() > 150);
                if (resultado) {

                    this.posicionTamanoLinea.add(nLn); //almacena el numero de linea donde se encuentro el error

                }
            });

            Collections.sort(this.posicionTamanoLinea);

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
            //String msgE = "\t\tERROR 0002: Linea " + (nLn) + " no termina con =>;";
            String str = ";";

            //Pattern ptr = Pattern.compile(str);
            int nLn = 0;

            boolean continua = true;
            for (String e : this.cargaInformacion.codigoArchivo) {
                boolean encontro = false;
                String[] divide = e.split("\\s+");

//                for (int i = 0; i < divide.length; i++) {
//                    //Matcher mtch = ptr.matcher(divide[i]);
//
//                    //encontro = (";" ==divide[i]);
//                    encontro = e.trim().endsWith(";");
//                    if (encontro == false) {
//                        nLn = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
//                    }
                encontro = e.trim().contains(";");
                if (encontro == false) {
                    nLn = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                    this.posicionPuntoComa.add(nLn);
                }
            }

            Collections.sort(this.posicionPuntoComa);
            return this.posicionPuntoComa.size();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaPuntoyComa()=> " + e.getMessage());
            e.getCause();
            return 1;
        }
    }
    //#region MetodosValidacionesPuntoComa

    //    private void ValidaBlancos(String ln, int nLn) {
    //        try {
    //
    //            String msgE = "ERROR 0003: Linea con mas espacios en blancos de lo permitido";
    //            String str = "[\\s]{150,}";
    //            Pattern ptr = Pattern.compile(str);
    //
    //            Matcher match = ptr.matcher(ln);
    //
    //            if (match.matches()) {
    //                System.out.println("Se encontraron 150 espacios en blanco");
    //            } else {
    //                System.out.println("Se encontraron mas de 150 espacios en blanco");
    //                MensajeError(msgE, nLn);
    //            }
    //
    //        } catch (Exception e) {
    //            System.out.println("Clase Valida-> ValidaBlancos()=> " + e.getMessage());
    //            e.printStackTrace();
    //
    //        }
    //    }
    //    private int ValidaSuperfluos(String ln, int nLn) {
    //        try {
    //            int resultado;
    //
    //            String strInicio = "(\\s)?"; //<- deberia de validar si existe 2 o mas espacios en blanco
    //            Pattern ptr = Pattern.compile(strInicio);
    //
    //            Matcher match = ptr.matcher(ln);
    //
    //            resultado = (match.matches()) ? 1 : 0;
    //
    //            return resultado;
    //
    //        } catch (Exception e) {
    //            System.out.println("Clase Valida-> ValidaSuperfluos()=> " + e.getMessage());
    //            e.printStackTrace();
    //            return -1;
    //        }
    //    }
    //Se estan encontrando las reservadas pero pendiente mas analisis para empezar a descartar.
    protected String EncuentraNoReservadas() {
        try {
            String resultado = null;
            int cont = 0;
            boolean continua = true;
            // boolean esReservadaPazcal = false;

            while (continua) {
                String val = null;
                for (String reservada : this.cargaInformacion.reservadasPascal) {
                    if (reservada != null) {
                        for (String e : this.cargaInformacion.codigoArchivo) {
                            boolean esReservada = false;
                            String[] divide = e.split("\\s");
                            for (int i = 0; i < divide.length; i++) {
                                Pattern ptr = Pattern.compile(reservada, Pattern.CASE_INSENSITIVE);
                                Matcher mt = ptr.matcher(divide[i]);

                                esReservada = mt.matches();

                                if (!esReservada) {
                                    resultado = divide[i];
                                    this.noReservadas.put(this.cargaInformacion.codigoArchivo.indexOf(e), resultado);
//                                    this.noReservadas.add(this.cargaInformacion.codigoArchivo.indexOf(e) + 1);
                                }
                            }
                        }
                    }

                }
                //   Collections.sort(noReservadas);

                continua = false;
            }
            //System.out.println(this.posicionReservadas);

            //System.out.println("Se encontraron " + cont + " reservadas");
            return resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> EncuentraNoReservadas()=> " + e.getMessage());
            e.printStackTrace();
            return "EXCEPTION";
        }
    }

    //Se estan encontrando las reservadas pero pendiente mas analisis para empezar a descartar.
    protected boolean EncuentraReservadas() {
        try {
            boolean resultado = false;
            int cont = 0;
            boolean continua = true;
            boolean esReservadaPazcal = false;

            while (continua) {
                String val = null;
                for (String reservada : this.cargaInformacion.reservadasPascal) {
                    if (reservada != null) {
                        for (String e : this.cargaInformacion.codigoArchivo) {
                            boolean esReservada = false;
                            String[] divide = e.split("\\s");
                            for (int i = 0; i < divide.length; i++) {
                                Pattern ptr = Pattern.compile(reservada, Pattern.CASE_INSENSITIVE);
                                Matcher mt = ptr.matcher(divide[i]);

                                esReservada = mt.matches();
                                esReservadaPazcal = reservada.toUpperCase().matches("(END|PROGRAM|VAR|BEGIN|READLN|WRITELN|REPEAT|INTEGER|CHAR|REAL)");
                                if (esReservadaPazcal == false) {
                                    if (esReservada) {
                                        val = reservada;
                                        this.posicionReservadas.add(this.cargaInformacion.codigoArchivo.indexOf(e) + 1);
                                    }
                                }
                            }
                        }
                    }

                }
                Collections.sort(posicionReservadas);

                continua = false;
            }
            //System.out.println(this.posicionReservadas);
            resultado = this.posicionReservadas.isEmpty();

            //System.out.println("Se encontraron " + cont + " reservadas");
            return !resultado;

        } catch (Exception e) {
            System.out.println("Clase Valida-> EncuentraReservadas()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean ValidarReadLn() {
        try {

            boolean continua = true;
            while (continua) {

                String patron = "((readln)\\s*\\(\\s*(input)\\s*,\\s*[a-z]{1,15}\\s*\\)\\s*\\;)|((readln)\\s*\\(\\s*[a-z]{1,15}\\s*\\)\\s*\\;)";
                Pattern ptr = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);

                this.cargaInformacion.codigoArchivo.forEach((String e) -> {
                    int posicion = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                    String[] strs = e.split("\\s");
                    for (int i = 0; i < strs.length; i++) {
                        if (strs[i].equalsIgnoreCase("READLN")) {
                            boolean resultado = true;
                            Matcher mtch = ptr.matcher(e);
                            resultado = mtch.find();
                            System.out.println("Evaluando READLN-> " + resultado);

                            if (!e.isEmpty()) {
                                if (resultado == false) {
                                    this.posicionReadLn.add(posicion);
                                }
                            }
                        }
                    }

                });
                Collections.sort(posicionReadLn);
                continua = false;
            }

            return this.posicionReadLn.isEmpty();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidarReadLn()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean ValidarWriteLn() {
        try {

            boolean continua = true;
            while (continua) {
                //String a=   "(writeln)\\*\\;|((writeln)\\s*\\(\\s*(output)\\s*\\)\\;)|((writeln)\\s*\\(\\s*(output)\\s*,\\'\\s*[a-z]{1,15}\\s*\\'\\)\\;)|((writeln)\\s*\\(\\s*([a-z]{1,15}),\\s*\\)\\;)";

                String patron = "(writeln)\\*\\;|((writeln)\\s*\\(\\s*(output)\\s*\\)\\;)|((writeln)\\s*\\(\\s*(output)\\s*,\\'\\s*[a-z]{1,15}\\s*\\'\\)\\;)|((writeln)\\s*\\(\\s*([a-z]{1,15}),\\s*\\)\\;)";
                Pattern ptr = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);

                this.cargaInformacion.codigoArchivo.forEach((String e) -> {
                    int posicion = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                    String[] strs = e.split("\\s");
                    for (int i = 0; i < strs.length; i++) {
                        if (strs[i].equalsIgnoreCase("WRITELINE")) {
                            boolean resultado = true;
                            Matcher mtch = ptr.matcher(e);
                            resultado = mtch.find();
                            System.out.println("Evaluando WRITELN-> " + resultado);

                            if (!e.isEmpty()) {
                                if (resultado == false) {
                                    this.posicionReadLn.add(posicion);
                                }
                            }
                        }
                    }

                });
                Collections.sort(posicionReadLn);
                continua = false;
            }

            return this.posicionReadLn.isEmpty();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidarWriteLn()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean ValidaProgram() {
        try {

            boolean continua = true;
            while (continua) {

                String patron = "(program)\\s+[a-z]{1,15}\\s*\\({1}\\s*(input)\\s*\\,{1}\\s*(output)\\s*\\){1}\\s*\\;{1}|(program)\\s+[a-z]{1,15}\\s*\\;{1}|(program)\\s+[a-z]{1,15}\\s*\\({1}\\s*(input)\\s*\\){1}\\s*\\;{1}|(program)\\s+[a-z]{1,15}\\s*\\({1}\\s*(output)\\s*\\){1}\\s*\\;{1}";
                Pattern ptr = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);

                this.cargaInformacion.codigoArchivo.forEach((String e) -> {
                    int posicion = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                    String[] strs = e.split("\\s");
                    for (int i = 0; i < strs.length; i++) {
                        if (strs[i].equalsIgnoreCase("PROGRAM")) {
                            boolean resultado = false;
                            Matcher mtch = ptr.matcher(e);
                            resultado = mtch.find();

                            if (!e.isEmpty()) {
                                if (resultado == false) {
                                    this.posicionProgram.add(posicion);
                                }
                            }
                        }
                    }

                });
                Collections.sort(posicionProgram);
                continua = false;
            }

            return this.posicionProgram.isEmpty();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaProgram()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean ValidaVar() {
        try {

            String patron = "(VAR)";
            String patron2 = "(BEGIN)";

            Pattern ptr1 = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);
            Pattern ptr2 = Pattern.compile(patron2, Pattern.CASE_INSENSITIVE);

            int posVar = 0;
            int posBegin = 0;
            ArrayList<String> temp = new ArrayList<>();
            boolean resultado1 = true;
            boolean resultado2 = true;
            for (String e : this.cargaInformacion.codigoArchivo) {

                int posicion = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;

                Matcher mtch1 = ptr1.matcher(e);
                Matcher mtch2 = ptr2.matcher(e);

                resultado1 = mtch1.matches();
                resultado2 = mtch2.matches();

                if (!e.isEmpty()) {
                    if (resultado1) {
                        posVar = this.cargaInformacion.codigoArchivo.indexOf(e);
                    }

                }
                if (!e.isEmpty()) {
                    if (resultado2) {
                        posBegin = this.cargaInformacion.codigoArchivo.indexOf(e);
                    }
                }
                mtch1.reset();
            }
            while (posVar < posBegin) {
                temp.add(this.cargaInformacion.codigoArchivo.get(posVar));
                posVar++;
            }

            for (String e : temp) {
                boolean res = true;
                String patronV = "(VAR)";
                String patronVariables = "(\\s+(\\w[^\\.\\%]){1,15}:\\s+(INTEGER|CHAR|REAL);)";
                Pattern ptrVariables = Pattern.compile(patronVariables, Pattern.CASE_INSENSITIVE);
                Pattern ptrV = Pattern.compile(patronV, Pattern.CASE_INSENSITIVE);

                boolean esVariable = false;

                Matcher mtchV = ptrV.matcher(e);
                Matcher mtchVariables = ptrVariables.matcher(e);

                res = mtchV.matches();
                esVariable = mtchVariables.find();

                if (!e.isEmpty()) {
                    if (!res) {
                        //System.out.println(res + "  " + e);
                        this.posicionVar.add(this.cargaInformacion.codigoArchivo.indexOf(e) + 1);
                    }
                }
                if (!e.isEmpty()) {
                    if (esVariable) {
                        // System.out.println(esVariable + "  " + e);
                        this.posicionVar.add(this.cargaInformacion.codigoArchivo.indexOf(e) + 1);
                    }
                }
            }

            Collections.sort(posicionVar);

            return this.posicionVar.isEmpty();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaVar()=> " + e.getMessage());
            e.getCause();
            return false;
        }
    }

    private boolean ValidaBeginEnd() {//Solo esta validando de momento si End esta antes ubicado que Begin
        try {

            String patron = "BEGIN";
            String patron2 = "(END\\s+\\.)";

            Pattern ptr1 = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);
            Pattern ptr2 = Pattern.compile(patron2, Pattern.CASE_INSENSITIVE);

            int posBegin = 0;
            int posEnd = 0;
            //ArrayList<String> temp = new ArrayList<>();
            boolean resultado1 = true;
            boolean resultado2 = true;
            for (String e : this.cargaInformacion.codigoArchivo) {

                int posicion = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;

                Matcher mtch1 = ptr1.matcher(e);
                Matcher mtch2 = ptr2.matcher(e);

                resultado1 = mtch1.matches();
                resultado2 = mtch2.matches();

                if (!e.isEmpty()) {
                    if (resultado1) {
                        posBegin = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                        // System.out.println(" Begin"+posBegin);
                    }

                }
                if (!e.isEmpty()) {
                    if (resultado2) {
                        posEnd = this.cargaInformacion.codigoArchivo.indexOf(e) + 1;
                        //System.out.println(" End"+posEnd);
                    }
                }

            }
//            while (posBegin < posEnd) {
//                temp.add(this.cargaInformacion.codigoArchivo.get(posEnd));
//                posEnd++;
//            }
            boolean resultado = false;

            resultado = posBegin > posEnd;

            if (posBegin != 0 || posEnd != 0) {

                if (resultado) {

                    this.posicionBeginEnd.add(posEnd);//solo esta guardando la posicion de END para generar el error luego
                }

            } else {
                this.posicionBeginEnd.add(posBegin);
                this.posicionBeginEnd.add(posEnd);
            }

            Collections.sort(posicionBeginEnd);

            return this.posicionBeginEnd.isEmpty();

        } catch (Exception e) {
            System.out.println("Clase Valida-> ValidaBeginEnd()=> " + e.getMessage());
            e.getCause();
            return false;
        }
    }

    private boolean EncuentraComentarios() {
        try {

            boolean resultado = false;
            boolean flagParzq =false;
            boolean flagParDer =false;
            boolean flagLlIz =false;
            boolean flagLlDer =false;
            
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

            this.posicionProgram.forEach((e) -> {
                System.out.println("Linea Formato Program Erroneo-> " + e);
            });
            this.posicionVar.forEach((e) -> {
                System.out.println("Linea Formato Var Erroneo-> " + e);
            });

            this.posicionDefVar.forEach((e) -> {
                System.out.println("Linea Definicion Variable Error-> " + e);
            });

            this.posicionReservadas.forEach((e) -> {
                System.out.println("Resevada Encontrada en Linea-> " + e + " palabra-> " + this.cargaInformacion.reservadasPascal.get(e));
            });

            this.posicionBeginEnd.forEach((e) -> {
                System.out.println("Begin - END -> " + e);
            });
            this.posicionReadLn.forEach((e) -> {
                System.out.println("Linea ReadLn Error -> " + e);
            });
            this.posicionWriteLn.forEach((e) -> {
                System.out.println("Linea WriteLn Error -> " + e);
            });

            imprimir = false;
        }

    }

}
