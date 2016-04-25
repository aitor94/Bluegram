package vistaControlador;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import utilities.UtilidadesOtros;
import utilities.UtilidadesServidor;

public class ControladorRegistroFinal implements Initializable
{

	@FXML
	private TextField codigo;
	@FXML
	private Button boton;
	
	private String code;
	private String name;
	private String password;
	private String email;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		boton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				if (code.equals(codigo.getText())) {
					
					XMPPTCPConnection con = UtilidadesServidor.ServerConnection("admin", "admin");
					
					try	{
						con.connect();
					} 
					catch (SmackException | IOException | XMPPException e) {
						UtilidadesOtros.alerta(AlertType.ERROR, "Error de conexion", "Error de conexion");
					}
						
					AccountManager am = AccountManager.getInstance(con);
					
					Map<String,String> attr = new HashMap<String,String>();
					attr.put("email", email);
					
					try {
						am.createAccount(name, password, attr);
					} 
					catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
						UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error al crear cuenta");
					}
					
					UtilidadesOtros.alerta(AlertType.CONFIRMATION, "�Exito!", "�Registro completado con exito!");
					UtilidadesOtros.ventanaFXML("/vistaControlador/Principal.fxml", boton.getScene());
					
				}
				else {
					UtilidadesOtros.alerta(AlertType.ERROR, "Fallo de autenticacion", "�La clave no es correcta!");
				}
			}
		});
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
