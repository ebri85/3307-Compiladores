/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pazcal;

/**
 *
 * @author ebri_85
 */
public class Dato {
    
    protected int numeroLinea;
    protected String dato;
    
    public Dato(){}
    
    public Dato(int numeroLinea, String dato){
        this.numeroLinea = numeroLinea;
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "Dato{" + "numeroLinea=" + numeroLinea + ", dato=" + dato + '}';
    }
    
    
}
