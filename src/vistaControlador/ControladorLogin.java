package vistaControlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utilities.UtilidadesOtros;
import utilities.UtilidadesServidor;

public class ControladorLogin implements Initializable {
	
	@FXML private TextField usuario;
	@FXML private PasswordField pass;
	@FXML private CheckBox recordar;
	@FXML private Label usuarioVacio;
	@FXML private Label passVacio;	
	@FXML private Button conectar;
	@FXML private Button registrar;
	@FXML private ProgressIndicator iconoCargando;
	
	private Task<Void> task;
	

	@Override
	public void initialize (URL location, ResourceBundle resources) {
		
		String userpass[] = UtilidadesOtros.leerDeFichero();
		if (userpass != null) {
			usuario.setText(userpass[0]);
			pass.setText(userpass[1]);
		}
		
		task = new Task<Void>() 
		{
		    @Override public Void call() 
		    {
		    	boolean correcto=true;
		    	
		    	usuarioVacio.setVisible(false);
				passVacio.setVisible(false);
				
				if (usuario.getText().isEmpty()) {
					usuarioVacio.setVisible(true);
					correcto=false;
				}
				
				if (pass.getText().isEmpty()) {
					passVacio.setVisible(true);
					correcto=false;
				}
				
				if (recordar.isSelected()) UtilidadesOtros.guardarEnFichero(usuario.getText(), pass.getText());
				
				UtilidadesServidor.scon = UtilidadesServidor.ServerConnection(usuario.getText(), pass.getText());
				
				try {
					if(correcto){
						UtilidadesServidor.scon.connect();
						UtilidadesServidor.scon.login();
					}
				}
				
				catch (SmackException e) {
					UtilidadesOtros.alerta(AlertType.ERROR, "Error de conexion", "Error de conexion");
				}
				
				catch (IOException e) {
					UtilidadesOtros.alerta(AlertType.ERROR, "Error inesperado", "Error inesperado");
				}
				
				catch (XMPPException e) {
					UtilidadesOtros.alerta(AlertType.ERROR, "Error de autenticacion", "Usuario o contrasena erroneos");
				}
				
				finally {
					iconoCargando.setVisible(false);
				}
				return null;			        
		    }
		};
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	        @Override
	        public void handle(WorkerStateEvent t)
	        {
	        	UtilidadesOtros.ventanaFXML("/vistaControlador/Chat.fxml", usuario.getScene());
	        }
	    });
		
		conectar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				iconoCargando.setVisible(true);
				new Thread(task).start();
			}
				
		});
		
		registrar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				UtilidadesOtros.ventanaFXML("/vistaControlador/Registro.fxml", conectar.getScene());
			}				
		});

	}

}
