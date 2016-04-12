package vistaControlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import modelo.Contacto;
import utilities.UtilidadesServidor;

public class ControladorConversacion extends Contacto implements Initializable {

	@FXML
	private TextArea texto;
	@FXML
	private Button enviar;
	@FXML
	private Label contacto;
	@FXML
	private ListView<Label> msgs;

	private ObservableList< Label  > mensajes;

	public void setContacto(Label contacto) {
		this.contacto = contacto;
	}

	public TextArea getTexto() {
		return texto;
	}

	public void setTexto(TextArea texto) {
		this.texto = texto;
	}

	public Button getEnviar() {
		return enviar;
	}

	public void setEnviar(Button enviar) {
		this.enviar = enviar;
	}

	public Label getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {

		this.contacto.setText(contacto);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mensajes = FXCollections.observableArrayList();

		enviar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				String usuario = contacto.getText();
				Message msg = new Message();

				try {
					msg.setBody(texto.getText());
					getChat().sendMessage(msg);
					setMsgs(msg);//aqui tengo que poner mi mensaje para verlo yo
					texto.clear();
					
				} 
				{
					catch (NotConnectedException e) 
					System.out.println("Error al mandar chat");
				}
			}

		});
	}
}
