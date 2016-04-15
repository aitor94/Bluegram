package vistaControlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import modelo.Contacto;
import utilities.UtilidadesChat;
import utilities.UtilidadesOtros;
import utilities.UtilidadesServidor;

public class ControladorChat implements Initializable {

	@FXML private ListView<String> listaContactos;
	@FXML private MenuItem anadir;
	@FXML private MenuItem eliminar;
	@FXML private AnchorPane panelChat;

	private Map<String,Contacto> contactos;
	private ObservableList<String> itemsContactos=FXCollections.observableArrayList();
	private UtilidadesChat uc;

	public static ControladorConversacion conversacionActual;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		List<Message> mensajesOff = new ArrayList<Message>();	
		uc = new UtilidadesChat();
		contactos = uc.getContacts();
		
		for(Contacto contacto : contactos.values())
			itemsContactos.add(contacto.getNombre());

		try 
		{
			mensajesOff = uc.getOfflineMessages();
			uc.asignaMensajes(contactos, mensajesOff);
		} 
		catch (NoResponseException | XMPPErrorException | NotConnectedException e) 
		{
			e.printStackTrace();
		}

		try 
		{
			UtilidadesServidor.scon.sendPacket(new Presence(Presence.Type.available));
		} 
		catch (NotConnectedException e1) 
		{
			e1.printStackTrace();
		}

		listaContactos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		listaContactos.setItems(FXCollections.observableArrayList(itemsContactos));
		
		listaContactos.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{	
			@Override
			public void handle(MouseEvent event) 
			{
				Contacto contacto;
				
				if(conversacionActual != null)
				{
					contacto = conversacionActual.getContact();
					contacto.setSelected(false);
					contactos.put(contacto.getNombre(),contacto);
				}				
				
				if (listaContactos.getSelectionModel().isEmpty() == false) 
				{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistaControlador/Conversacion.fxml"));
					contacto = contactos.get(listaContactos.getSelectionModel().getSelectedItem());
					
					try 
					{	
						panelChat.getChildren().add(loader.load());
						conversacionActual = loader.getController();
						contacto.setSelected(true);
						conversacionActual.setContact(contacto);
						
						for(Message msg : contacto.getMensajes())
							UtilidadesChat.labelGenerator(msg.getBody(),Pos.TOP_RIGHT,"green");
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				event.consume();
			}
		});

		anadir.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				uc.anadirContacto();
				event.consume();
				UtilidadesOtros.ventanaFXML("/vistaControlador/Chat.fxml", panelChat.getScene());
			}

		});

		eliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				uc.eliminarContacto(listaContactos.getSelectionModel().getSelectedItem());
				event.consume();
				UtilidadesOtros.ventanaFXML("/vistaControlador/Chat.fxml", panelChat.getScene());
			}

		});
		
		ChatManager.getInstanceFor(UtilidadesServidor.scon).addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chhat, boolean createdLocally) {
				System.out.println(createdLocally);
				chhat.addMessageListener(new ChatMessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) 
					{		
						Contacto contacto = contactos.get(message.getFrom().split("@")[0]);
						contacto.addMessage(message);
						contactos.put(contacto.getNombre(),contacto);
						System.out.println("mensaje recibido de "+contacto.getId());
						System.out.println("mensaje="+message.getBody());
						
						Platform.runLater(() -> {
							if(contacto.isSelected())
								UtilidadesChat.labelGenerator(message.getBody(),Pos.TOP_RIGHT,"green");
							
						});
					}
				});
			}
		});
	}
	
	public void logout() 
	{
		UtilidadesServidor.scon.disconnect();
		UtilidadesOtros.ventanaFXML ("/vistaControlador/Login.fxml", listaContactos.getScene());
	}
}
