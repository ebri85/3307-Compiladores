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
public class Variable {
    
    protected String nombre;
    protected String tipoDato;

    public Variable() {
    }

    public Variable(String nombre, String tipoDato) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
    }

    @Override
    public String toString() {
        return "Variable{" + "nombre=" + nombre + ", tipoDato=" + tipoDato + '}';
    }
    
    
    
}
