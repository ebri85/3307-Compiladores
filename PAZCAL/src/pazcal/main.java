/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;



/**
 *
 * @author Esau Brizuela Ruiz
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            String arg = null;
            arg = args[0];
            Ejecuta(arg);

        } else {
            System.out.println("No se indico el nombre del archivo a procesar");

        }

    }

    public static void Ejecuta(String archivoPazcal) {
        try {
            System.out.println("Compilando...");
            ArchivoInfo infoArchivoPazcal = new ArchivoInfo(archivoPazcal);
            CargaInformacion cargaInformacion = new CargaInformacion(infoArchivoPazcal);
            Generador generador = new Generador(cargaInformacion, infoArchivoPazcal);

            Valida valida = new Valida(cargaInformacion);
            
            generador.Compila();           

        } catch (Exception e) {
            System.out.println("Clase Main -> Ejecuta()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
