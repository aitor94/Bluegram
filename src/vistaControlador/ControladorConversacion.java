package vistaControlador;

import java.net.URL;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;

import datos.FicheroXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import modelo.Contacto;
import utilities.UtilidadesChat;
import utilities.UtilidadesServidor;

public class ControladorConversacion extends Contacto implements Initializable
{
	@FXML private TextArea texto;
	@FXML private Button enviar;
	@FXML private Label contacto;
	@FXML private VBox vbox;
	@FXML private ScrollPane scrollPane;
	
	public VBox getVbox() 
	{
		return vbox;
	}

	public void setVbox(VBox vbox) 
	{
		this.vbox = vbox;
	}

	public void setVSize(double height)
	{
		scrollPane.setVvalue(height);
	}

	private Contacto contact;

	public void setContact(Contacto contacto) 
	{
		super.setId(contacto.getId());
		super.setFriend(contacto.isFriend());
		super.setMensajes(contacto.getMensajes());
		super.setNombre(contacto.getNombre());
		super.setPresencia(contacto.getPresencia());
		super.setSelected(contacto.isSelected());
		this.contact = contacto;
	}

	public Contacto getContact() 
	{
		Contacto contacto = new Contacto();

		contacto.setFriend(super.isFriend());
		contacto.setId(super.getId());
		contacto.setMensajes(super.getMensajes());
		contacto.setNombre(super.getNombre());
		contacto.setPresencia(super.getPresencia());
		contacto.setSelected(super.isSelected());

		return contacto;
	}

	public void setContacto(Label contacto) 
	{
		this.contacto = contacto;
	}

	public TextArea getTexto() 
	{
		return texto;
	}

	public void setTexto(TextArea texto) 
	{
		this.texto = texto;
	}

	public Button getEnviar() 
	{
		return enviar;
	}

	public void setEnviar(Button enviar) 
	{
		this.enviar = enviar;
	}

	public Label getContacto() 
	{
		return contacto;
	}

	public void setContacto(String contacto) 
	{
		this.contacto.setText(contacto);
	}

	public void setMargin(Node node, Insets i) 
	{
		VBox.setMargin(node, i);
	}

	public void addChildren(Node node) 
	{
		vbox.getChildren().add(node);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		enviar.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				Message msg = new Message();

				try 
				{
					Chat chat = ChatManager.getInstanceFor(UtilidadesServidor.scon)
							.createChat(contact.getId());
					msg.setBody(texto.getText());
					msg.setFrom(UtilidadesServidor.scon.getUser());
					msg.setTo(contact.getId());
					UtilidadesChat.labelGenerator(msg.getBody(), Pos.TOP_LEFT, "paleturquoise");
					contact.addMessage(msg);
					Message ms = new Message(msg.getTo(), msg.getBody());
					chat.sendMessage(ms);

					texto.clear();
					FicheroXML.escribeFichero(contact.getMensajes(),
							UtilidadesServidor.scon.getUser().split("@")[0]+contact.getNombre());
					
				} 
				catch (NotConnectedException e) 
				{
					System.out.println("Error al mandar chat");
				}
			}

		});
		
		vbox.heightProperty().addListener(new ChangeListener<Number>() 
		{
	        @Override
	        public void changed(ObservableValue<? extends Number> arg0,Number arg1, Number arg2) 
	        {
	            scrollPane.setVvalue(1.0);
	        }
		});
		
	}
}
