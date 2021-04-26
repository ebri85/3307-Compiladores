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
           

            Valida valida = new Valida(cargaInformacion);
             Generador generador = new Generador(cargaInformacion, infoArchivoPazcal);
             int esProgramaValido =0; //esta variable va a definir si compila o no la aplicacion, de momento igual se va a enviar a PASCAL
             for(int i=0;i<valida.esProgramaValido.length;i++){
                  if(valida.esProgramaValido[i]==false){
                      esProgramaValido++;
                  }
             }
             //En este if se define si compila o no, si alguna de las validaciones arrojaron false, suma al esProgramaValido 
             if(esProgramaValido>0){
                 System.out.println("\n\n\t\t****DE MOMENTO VOY A PASAR EL ARCHIVO AUNQUE TIENE ERRORES PERO NO DEBERIA****\nCOMPILANDO....");
                 System.out.println("\nQUE PASCAL DECIDA SI LA APLIACION FUNCIONA");
                // generador.Compila();  
             }else{
                 generador.Compila();  
             }
            //generador.Compila();           

        } catch (Exception e) {
            System.out.println("Clase Main -> Ejecuta()=> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
