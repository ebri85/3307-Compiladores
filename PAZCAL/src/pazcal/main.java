/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

import javax.swing.JOptionPane;

/**
 *
 * @author Esau Brizuela Ruiz
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String arg = null;
        arg = args[0];
        Ejecuta(arg);
    }

    public static void Ejecuta(String archivoPazcal) {
        try {
            ArchivoInfo infoArchivoPazcal = new ArchivoInfo(archivoPazcal);
            CargaInformacion cargaInformacion = new CargaInformacion(infoArchivoPazcal);
            
            System.out.println(infoArchivoPazcal.toString());
        } catch (Exception e) {
            System.out.println("Clase Main -> Ejecuta()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
