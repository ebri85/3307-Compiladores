/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Esau Brizuela Ruiz
 */
public class Generador {

    protected CargaInformacion cargaInformacion;
    protected ArchivoInfo infoArchivo;

    private String errores;
    private String pas;

    public Generador() {

    }

    public Generador(CargaInformacion cI, ArchivoInfo iA) {
        System.out.println("Generando Archivos...");
        this.cargaInformacion = cI;
        this.infoArchivo = iA;

        GeneraArchivos();
        

    }

    protected void GeneraArchivos() {
        try {
            errores = this.infoArchivo.strDirectorioEjecucionPazcal + "\\" + this.infoArchivo.nombArchivo + "-errores.txt";
            pas = this.infoArchivo.strDirectorioEjecucionPazcal + "\\" + this.infoArchivo.nombArchivo + ".pas";
            Files.write(Paths.get(errores), this.cargaInformacion.lineasParaArchivoErrores);
            Files.write(Paths.get(pas), this.cargaInformacion.codigoArchivo);

        } catch (Exception e) {
            System.out.println("Clase Generador-> GeneraArchivoErrores()=> " + e.getMessage());
            e.printStackTrace();

        }
    }

    protected void Compila() {
        try {

            String archivoPas = this.infoArchivo.nombArchivo + ".pas";
            String archivoExe = this.infoArchivo.nombArchivo + ".exe";

            Process process = Runtime.getRuntime().exec(new String[]{
                "FPC",
                archivoPas
            });

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        process.waitFor();

                        Runtime.getRuntime().exec(new String[]{
                            "cmd",
                            "/c",
                            "start",
                            "cmd",
                            "/k",
                            archivoExe
                        });

                    } catch (Exception e) {
                        System.out.println("Clase Generador-> Compila()-> Runtime => " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        } catch (Exception e) {
            System.out.println("Clase Generador-> Compila()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
