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
    
    
     
    public boolean reabastecerCarnita(int idProducto, double kilosNuevos) { //boolean porque regresa cierto o falso 
        String sql = "UPDATE productos SET stock = stock + ? WHERE id_producto = ?";
        
        
        Conexion objCon = new Conexion(); //obj para mnadar a llamarer la función de conectar a base de datos
try (java.sql.Connection con = objCon.conectar();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, kilosNuevos); // reemplazar los signos de pregunta por valores de los parametros
            ps.setInt(2, idProducto); 
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; //si devuelve 0 es falso (por los del boolean) porque no se modificó nada en la base de datos, hay un pedo ps 
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error al reabastecer stock: " + e.getMessage());
            return false;
        }
    }
    
    
    public int obtenerIdPorNombre(String nombreCarne) { // pa transformar en nombre en un ide segun la carne que se seleccione en reabastecimiento 
    String sql = "SELECT id_producto FROM productos WHERE nombre = ?";
    try {
        Conexion cn = new Conexion();
        java.sql.Connection con = cn.conectar();
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombreCarne);
        java.sql.ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            int id = rs.getInt("id_producto");
            con.close();
            return id;
        }
        con.close();
    } catch (Exception e) {
        System.out.println("Error al buscar ID: " + e.getMessage());
    }
    return -1;
}
    
    
   public void rellenarComboCarnes(javax.swing.JComboBox combo) { //para rellenar los combobox con todas las carnes
    String sql = "SELECT nombre FROM productos ORDER BY id_producto ASC";
    try {
        Conexion cn = new Conexion();
        java.sql.Connection con = cn.conectar();
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        java.sql.ResultSet rs = ps.executeQuery();
        
        combo.removeAllItems();
        while (rs.next()) {
            combo.addItem(rs.getString("nombre"));
        }
        con.close();
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
} 
    
    
    
}

