/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carniceriamr;

/**
 *
 * @author santi
 */
public class Productos {
    
public int id;
public String nombre;
public double precio;
public double stock;
public int idCategoria; 


      public Productos() {}
    
     public Productos(String nombre, double precio, double stock, int idCategoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }
    
    
}
