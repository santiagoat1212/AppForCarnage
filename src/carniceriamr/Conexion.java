/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carniceriamr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author santi
 */
public class Conexion {
    
    String url="jdbc:postgresql://localhost:5432/CarniceriaMR";
        String usuario="postgres";
        String password="gokuleganaatodos123";
        Connection objConnection = null;
        
         public Connection conectar(){
             try{
            objConnection=DriverManager.getConnection(url, usuario, password);
           if(objConnection!=null){
           System.out.println("Conexión exitosa a DB CarniceriaMR");}
        }catch(SQLException e){
         System.err.println("Conexión fallida a base de datos.");
          System.err.println(e.toString());
        }
    
       return objConnection;  }
         
        public boolean GuardarVenta(int idCliente, double totalVenta, java.util.List<Object[]> listaProductos) {
        String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total) VALUES (?, NOW(), ?) RETURNING id_venta";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        java.sql.Connection con = null;
        try {
            con = conectar(); // nos conectamos a la base de datos
            con.setAutoCommit(false); // como para hacer varios comandos y asi, inserts por ejemplo

            int idVentaGenerado = 0;
            
            
            try (java.sql.PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                psVenta.setInt(1, idCliente);
                psVenta.setDouble(2, totalVenta);
                
                java.sql.ResultSet rs = psVenta.executeQuery();
                if (rs.next()) {
                    idVentaGenerado = rs.getInt("id_venta"); // Obtenemos el id de esta venta
                }
            }

            
            try (java.sql.PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 java.sql.PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                
                for (Object[] item : listaProductos) {
                    int idProd = (int) item[0];
                    double cant = (double) item[1];
                    double precio = (double) item[2];
                    double subtotal = (double) item[3];

                    
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, idProd);
                    psDetalle.setDouble(3, cant);
                    psDetalle.setDouble(4, precio);
                    psDetalle.setDouble(5, subtotal);
                    psDetalle.executeUpdate();

                    // Descontar los kilos del inventario
                    psStock.setDouble(1, cant);
                    psStock.setInt(2, idProd);
                    psStock.executeUpdate();
                }
            }

            con.commit(); //este es por si todo jaló bien, que se guardee en la base de datos 
            return true;

        } catch (java.sql.SQLException e) {
            // Si algo falla, cancelar cancelar dio mio señor salvame 
            if (con != null) {
                try { con.rollback(); } catch (java.sql.SQLException ex) { ex.printStackTrace(); }
            }
            System.out.println("Error en la venta: " + e.getMessage());
            return false;
        }
    }

    //con este metodo revisamos el historial por si se tiene que reembolsar o algun problemilla
    public java.sql.ResultSet obtenerDetalleTicket(int idVenta) {
        String sql = "SELECT d.id_detalle, p.nombre AS carne, d.cantidad, d.precio, d.subtotal " +
                     "FROM detalle_venta d " +
                     "INNER JOIN productos p ON d.id_producto = p.id_producto " +
                     "WHERE d.id_venta = ?";
        try {
            java.sql.Connection con = conectar();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idVenta);
            return ps.executeQuery();
        } catch (java.sql.SQLException e) {
            System.out.println("Error al consultar el ticket: " + e.getMessage());
            return null;
        }
    } 
         
         
 
public java.sql.ResultSet obtenerTodasLasVentas() {   // Obtiene la lista general de ventas para la tabla superior
    String sql = "SELECT id_venta, fecha, total FROM ventas ORDER BY id_venta DESC";
    try {
        java.sql.Connection con = conectar();
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();
    } catch (java.sql.SQLException e) {
        System.out.println("Error al consultar ventas: " + e.getMessage());
        return null;
    }
}


public java.sql.ResultSet obtenerDetalleVenta(int idVenta) {  // Obtiene los productos comprados de un ticket para la tabla inferior en HistorialVentas
    String sql = "SELECT p.nombre AS carne, d.cantidad, d.precio, d.subtotal " +
                 "FROM detalle_venta d " +
                 "INNER JOIN productos p ON d.id_producto = p.id_producto " +
                 "WHERE d.id_venta = ?";
    try {
        java.sql.Connection con = conectar();
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idVenta);
        return ps.executeQuery();
    } catch (java.sql.SQLException e) {
        System.out.println("Error al consultar detalle del ticket: " + e.getMessage());
        return null;
    }
}      
    
}
