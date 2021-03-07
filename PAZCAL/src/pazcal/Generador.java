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

    public Generador() {

    }

    public Generador(CargaInformacion cI, ArchivoInfo iA) {
        this.cargaInformacion = cI;
        this.infoArchivo = iA;
        GeneraArchivos();        

    }

    protected void GeneraArchivos() {
        try {
            String errores = this.infoArchivo.strDirectorioEjecucionPazcal + "\\" + this.infoArchivo.nombArchivo + "-errores.txt";
            String pas = this.infoArchivo.strDirectorioEjecucionPazcal + "\\" + this.infoArchivo.nombArchivo + ".pas";
            Files.write(Paths.get(errores), this.cargaInformacion.lineasParaArchivoErrores);
            Files.write(Paths.get(pas), this.cargaInformacion.codigoArchivo);
            
        } catch (Exception e) {
            System.out.println("Clase Generador-> GeneraArchivoErrores()=> " + e.getMessage());
            e.printStackTrace();
            
        }
    }

}
