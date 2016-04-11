package vistaControlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import modelo.Contacto;
import utilities.UtilidadesChat;
import utilities.UtilidadesServidor;

public class ControladorConversacion implements Initializable {

	@FXML
	private TextArea texto;
	@FXML
	private Button enviar;
	@FXML
	private Label contacto;
	@FXML
	private ListView<String> msgs;
	@FXML
	private TextField azul;
	@FXML
	private TextField verde;

	private Contacto persona;
	private ObservableList<String> mensajes;
	private List<Message> listaMensajes;

	public void setMsgs(List<Message> msgs) {
		if(msgs==null)
			msgs=new ArrayList<Message>();
		this.listaMensajes = msgs;
		ObservableList<String> lst = FXCollections.observableArrayList();
		for (Message msg : msgs) {
			lst.add(msg.getBody());
		}
		this.msgs.setItems(lst);
	}

	public void setMsgs(Message msg) {
		ObservableList<String> lista = this.msgs.getItems();
		lista.add(msg.getBody());
		if (listaMensajes == null)
			listaMensajes = FXCollections.observableArrayList();

		listaMensajes.add(msg);
		this.msgs.setItems(lista);
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
	public void initialize(URL location, ResourceBundle resources) {
		mensajes = FXCollections.observableArrayList();
		


		ChatManager.getInstanceFor(UtilidadesServidor.scon).addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new ChatMessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						Platform.runLater(() -> {
							mensajes.add(message.getBody());
							msgs.setItems(mensajes);
						});
					}
				});
			}
		});

		enviar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String usuario = contacto.getText();
				Message msg = new Message();

				try {
					msg.setBody(texto.getText());
					persona.getChat().sendMessage(msg);
					setMsgs(msg);
					texto.clear();
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error al mandar chat");
				}
			}

		});
	}

	public void setChat(String contacto) {
		ChatManager chatmanager = ChatManager.getInstanceFor(UtilidadesServidor.scon);

		Chat chat = chatmanager.createChat(contacto + "@mikel-virtualbox", new ChatMessageListener() {
			public void processMessage(Chat chat, Message message) {

				listaMensajes.add(message);
			}
		});

		persona.setChat(chat);
	}

}
