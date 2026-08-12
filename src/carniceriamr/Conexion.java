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
         
         
         
         
          
    
}
