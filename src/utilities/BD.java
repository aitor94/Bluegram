package utilities;

import java.sql.*;
import java.util.ArrayList;

public class BD {
	
	static String bd = "opf";
	static String login = "aitor";
	static String password = "1234";
	static String url = "jdbc:mysql://localhost/"+bd;
	   
	private Connection connection;  
	
	public BD() {
	      try{
	         //obtenemos el driver de para mysql
	         Class.forName("com.mysql.jdbc.Driver");
	         //obtenemos la conexi�n
	         connection = DriverManager.getConnection(url,login,password);
	 
	         if (connection!=null){
	            System.out.println("Conexi�n a base de datos "+bd+" OK\n");
	         }
	      }
	      catch(SQLException e){
	         System.out.println(e);
	      }catch(ClassNotFoundException e){
	         System.out.println(e);
	      }catch(Exception e){
	         System.out.println(e);
	      }
	   }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	public ArrayList<String> consultarUsuarios()
    {
		ResultSet resultado;
		ArrayList<String> usuarios = new ArrayList<>();
        String instruccion = "SELECT * FROM opf.ofuser";
        try {
            resultado= connection.createStatement().executeQuery(instruccion);  
            
            while(resultado.next()){
            	usuarios.add(resultado.getString("username"));
            }
            
        } catch (Exception e) {            
            System.out.print("ERROR MySQL, consultando usuarios ---> "+e+"\n");   
            usuarios =null;
        }    
        
        return usuarios;
    }
	
	public boolean existeUsuario(String usuario){
		
		boolean exis = false;
		
		ArrayList<String> lista = consultarUsuarios();
		
		for(String us : lista){
			if(us.equalsIgnoreCase(usuario)){
				exis = true;
			}
		}
		return exis;
	}
	
	public void cerrarConexion() throws SQLException
    {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
	
	
	

}
