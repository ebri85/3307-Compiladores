/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author ebri_85
 */
public class ArchivoInfo {

    protected String archivo;
    protected Path directorio = Paths.get("");
    protected String nombArchivo;

    public ArchivoInfo() {

    }

    public ArchivoInfo(String arg) {
        this.archivo = arg;
    }

}
