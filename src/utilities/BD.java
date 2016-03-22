package utilities;

import java.sql.*;
import java.util.ArrayList;

import javafx.scene.control.Alert.AlertType;

public class BD {
	
	public static final String bd = "opf";
	public static final String login = "aitor";
	public static final String password = "1234";
	public static final String url = "jdbc:mysql://localhost/" + bd;
	   
	private Connection connection;  
	
	public BD() {
		
	      try {
	         // obtenemos el driver de para mysql
	         Class.forName("com.mysql.jdbc.Driver");
	         
	         //obtenemos la conexion
	         connection = DriverManager.getConnection(url, login, password);
	 
	         if (connection != null) {
	            System.out.println("Conexion a base de datos "+ bd +" OK\n");
	         }
	      } catch(SQLException e) {
	    	  UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error de conexion a la base de datos");
	      } catch(ClassNotFoundException e) {
	    	  UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error");
	      } catch(Exception e) {
	    	  UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error");
	      }
	   }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	public ArrayList<String> consultarUsuarios() {
		ResultSet resultado;
		ArrayList<String> usuarios = new ArrayList<>();
        String instruccion = "SELECT * FROM opf.ofuser";
        try {
            resultado = connection.createStatement().executeQuery(instruccion);  
            
            while (resultado.next()) {
            	usuarios.add(resultado.getString("username"));
            }
            
        } catch (Exception e) {            
        	UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error MySQL, consultando usuarios ---> " + e + "\n");   
            usuarios = null;
        }    
        
        return usuarios;
    }
	
	public boolean existeUsuario(String usuario) {
		
		boolean exis = false;
		
		ArrayList<String> lista = consultarUsuarios();
		
		for (String us : lista) {
			if (us.equalsIgnoreCase(usuario)) {
				exis = true;
			}
		}
		return exis;
	}
	
	public void cerrarConexion() throws SQLException {
		connection.close();
    }
	
	
	

}
