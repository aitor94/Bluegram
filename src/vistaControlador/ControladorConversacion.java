package vistaControlador;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jivesoftware.smack.packet.Message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import modelo.Contacto;

public class ControladorConversacion implements Initializable
{

	@FXML private TextArea texto;
	@FXML private Button enviar;
	@FXML private Label contacto;
	@FXML private ListView<TextField> msgs;
	@FXML private TextField azul;
	@FXML private TextField verde;
	
	private Contacto persona;
	private ObservableList<TextField> mensajes;

	public void setMsgs(List<Message> msgs) {
		//aqui añado todos los mensajes al cargar la vista
	}
	
	public void setMsgs(Message msg) {
		//aqui añado un mensaje cuando la vista ya esta cargada
	}

	public Contacto getPersona() {
		return persona;
	}

	public void setPersona(Contacto persona) {
		this.persona = persona;
	}

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
	public void initialize(URL location, ResourceBundle resources) 
	{
		mensajes = FXCollections.observableArrayList();
		
		enviar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				//aqui envio el mensaje
			}
				
		});	
	}

}
