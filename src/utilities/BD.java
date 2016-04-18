package utilities;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import javafx.scene.control.Alert.AlertType;
import modelo.MessageArchive;

public class BD {
	
	public static final String bd = Constantes.bd;
	public static final String login = Constantes.login;
	public static final String password = Constantes.password;
	public static final String url = "jdbc:mysql://localhost/" + bd;
	   
	private Connection connection;
	private Statement statement;
	
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
		
	public boolean buscaUsuario (String usuario) {
		
		boolean exis = false;
		ResultSet resultado;
		String instruccion = "select * from "+bd+".ofUser where username="+"\""+usuario+"\"";
		
		try {
			
			resultado = connection.createStatement().executeQuery(instruccion);
				
			if (resultado.first() == true) {
				exis = true;
			}
			
		} catch (Exception e) {
			UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error al consultar la base de datos de usuarios");
		}
		
		return exis;		
	}		
	
	public List<MessageArchive> getMensajes() {

		ResultSet resultado;
		List<MessageArchive> mensajes = new ArrayList<MessageArchive>();
		String instruccion = "SELECT * FROM " + bd + ".ofMessageArchive";
		try {

			statement = connection.createStatement();
			resultado = statement.executeQuery(instruccion);

			while (resultado.next()) {

				MessageArchive ma = new MessageArchive();
				Message msg = new Message();
				
				ma.setBody(resultado.getString("body"));
				ma.setConversationID(BigInteger.valueOf(resultado.getLong("conversationID")));
				ma.setFromJID(resultado.getString("fromJID"));
				ma.setFromJIDResource(resultado.getString("fromJIDResource"));
				ma.setMessageID(BigInteger.valueOf(resultado.getLong("messageID")));
				ma.setSentDate(BigInteger.valueOf(resultado.getLong("sentDate")));
				ma.setStanza(resultado.getString("stanza"));
				ma.setToJID(resultado.getString("toJID"));
				ma.setToJIDResource(resultado.getString("toJIDResource"));
				
				msg.setBody(resultado.getString("body"));
				msg.setFrom(resultado.getString("fromJID"));
				
				mensajes.add(ma);
			}

		} catch (Exception e) {
			System.out.print("ERROR MySQL, consultando mensajes ---> " + e + "\n");
			e.printStackTrace();
			mensajes = null;
		}

		return mensajes;
	}
	
	
	public void cerrarConexion() throws SQLException {
		connection.close();
    }
	
	
	

}
