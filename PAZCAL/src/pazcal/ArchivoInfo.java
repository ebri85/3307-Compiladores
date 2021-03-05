/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ebri_85
 */
public class ArchivoInfo {

    protected String archivo;
    protected String nombArchivo;
    protected Path pathEjecucionPazcal = Paths.get("");
    protected Path pathCompletaPazcal = pathEjecucionPazcal.toAbsolutePath();
    protected String strDirectorioEjecucionPazcal = pathCompletaPazcal.toString();

    protected boolean extensionPazcal;

    public ArchivoInfo() {

    }

    public ArchivoInfo(String arg) {
        this.archivo = arg;
        GeneraDatos();
    }

    private void GeneraDatos() {
        try {
            this.nombArchivo = NombreArchivo();
            this.extensionPazcal = EsArchivoPazcal();

        } catch (Exception e) {
            System.out.println("Clase ArchivoInfo -> GeneraDatos()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String NombreArchivo() {
        try {
            int indicePunto = 0;
            String resultado = null;
            String str = this.pathEjecucionPazcal.resolve(this.archivo).getFileName().toString();
            indicePunto = str.indexOf(".");
            //System.out.println(str.indexOf("."));

            // System.out.println(resultado);
            boolean esTrue = true;
            //boolean mtch =false;
            while (esTrue) {

                //  Pattern ptr = Pattern.compile("^a-z0-9&&[^\\w]*", Pattern.CASE_INSENSITIVE);
                Pattern ptr = Pattern.compile("[^.]*", Pattern.CASE_INSENSITIVE);
                Matcher match = ptr.matcher(str.substring(0, indicePunto));
                //mtch = match.matches();

                //System.out.print(mtch);
                if (match.matches()) {
                    resultado = str.substring(0, indicePunto);
                    esTrue = false;
                } else {

                    throw new Exception("El nombre del archivo no cumple", null);
                }

            }
            return resultado;

        } catch (Exception e) {
            System.out.println("Clase ArchivoInfo -> NombreArchivo()=> " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean EsArchivoPazcal() {
        try {
            boolean error = true;
            boolean resultado = false;

            while (error) {

                Pattern ptr = Pattern.compile(".PAZCAL", Pattern.CASE_INSENSITIVE);
                Matcher match = ptr.matcher(this.archivo);
                resultado = match.find()  ;
                
                //System.out.println("es Pazcal =>"+ resultado);
                error = false;
            }
            return resultado;

        } catch (Exception e) {
            System.out.println("Clase ArchivoInfo -> EsArchivoPazcal()=> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public String toString() {
        return "ArchivoInfo{" + "\narchivo = " + archivo
                + "\nnombArchivo= " + nombArchivo
                + "\npathEjecucionPazcal= " + pathEjecucionPazcal
                + "\npathCompletaPazcal= " + pathCompletaPazcal
                + "\nstrDirectorioEjecucionPazcal= " + strDirectorioEjecucionPazcal
                + "\nextensionPazcal= " + extensionPazcal + '}';
    }

}
