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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Main;
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
				public void handle(MouseEvent event) 
				{
					if(code.equals(codigo.getText()))
					{
						XMPPTCPConnection con = UtilidadesServidor.ServerConnection("admin","admin");
						
						try 
						{
							con.connect();
						} 
						catch (SmackException | IOException | XMPPException e2) 
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						AccountManager am = AccountManager.getInstance(con);
						
						Map<String,String> attr = new HashMap<String,String>();
						attr.put("email",email);
						
						try 
						{
							am.createAccount(name, password,attr);
						} 
						catch (NoResponseException | XMPPErrorException | NotConnectedException e1) 
						{
							e1.printStackTrace();
						}
						
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Exito");
						alert.setHeaderText(null);
						alert.setContentText("Registro completado con exito!!!!!!");

						alert.showAndWait();
						
						FXMLLoader loader=new FXMLLoader();
						loader.setLocation(Main.class.getResource("/vistaControlador/Principal.fxml"));
						Scene escena=null;
						try 
						{
							escena = new Scene(loader.load());
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						Stage stg=(Stage)boton.getScene().getWindow();
						stg.setScene(escena);
						stg.show();
					}
					else
					{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Fallo");
						alert.setHeaderText(null);
						alert.setContentText("La clave no es correcta");

						alert.showAndWait();
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
