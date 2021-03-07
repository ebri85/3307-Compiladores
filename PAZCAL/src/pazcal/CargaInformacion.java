/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Esau Brizuela Ruiz
 */
public class CargaInformacion {

    protected ArchivoInfo archivo;

    protected List<String> codigoArchivo = Collections.emptyList();
    protected ArrayList<String> reservadasPascal = new ArrayList();
    protected ArrayList<String> lineasParaArchivoErrores = new ArrayList();

    public CargaInformacion() {

    }

    public CargaInformacion(ArchivoInfo archivo) {

        this.archivo = archivo;
        GeneraDatos();
    }

    private void GeneraDatos() {
        try {
            boolean cargaCodigo = CargaCodigo();
            boolean cargaReservadas = CargaReservadas();
            boolean cargaErrores = CargaErrores();

        } catch (Exception e) {
            System.out.println("Clase CargaInformacion-> GeneraDatos()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean CargaCodigo() {
        try {

            File file = new File(archivo.archivo);

            if (file.exists()) {

                this.codigoArchivo = Files.readAllLines(file.toPath());
                //System.out.println(this.codigoArchivo);
                return true;

            } else {
                throw new Exception("Archivo No existe ->" + archivo.archivo);
            }

        } catch (Exception e) {
            System.out.println("Clase CargaInformacion-> CargaCodigo()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean CargaReservadas() {
        try {
            InputStream iSt = this.getClass().getResourceAsStream("/pazcal/UTILS/reservadas.txt");
            InputStreamReader iStR = new InputStreamReader(iSt);
            BufferedReader bR = new BufferedReader(iStR);
            String reservada = null;

            do {
                reservada = bR.readLine();
                this.reservadasPascal.add(reservada);
            } while (reservada != null);

            System.out.println(this.reservadasPascal);
            return true;
        } catch (Exception e) {
            System.out.println("Clase CargaInformacion-> CargaReservadas()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean CargaErrores() {
        try {
            int i = 0;
            int j = 0;
            String str1 = null;
            String str2 = null;

            if (this.codigoArchivo.isEmpty()) {
                throw new Exception("Lista Codigo se encuentra vacia");

            }

            for (String ln : this.codigoArchivo) {
                j = this.codigoArchivo.lastIndexOf(i);

                if (ln.isEmpty()) {
                    j = i;

                } else {
                    i++;
                    j = i;
                }
                str1 = String.format("%05d", j);
                str2 = str1 + " " + ln;
                this.lineasParaArchivoErrores.add(str2);
            }
            System.out.println(this.lineasParaArchivoErrores);

            return false;
        } catch (Exception e) {
            System.out.println("Clase CargaInformacion-> CargaErrores()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void AgregaError(int nl, String msg) {
        try {
            
            this.lineasParaArchivoErrores.add(nl, "\n" + "Error => " + msg);
            
        } catch (Exception e) {
            System.out.println("Clase CargaInformacion-> AgregaError()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CargaInformacion"
                + "{" + "archivo=" + archivo
                + "codigoArchivo=" + codigoArchivo
                + "reservadasPascal=" + reservadasPascal
                + ", lineasParaArchivoErrores=" + lineasParaArchivoErrores
                + '}';
    }

}
