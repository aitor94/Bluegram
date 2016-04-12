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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import modelo.Contacto;
import utilities.BD;
import utilities.UtilidadesChat;
import utilities.UtilidadesConversacion;
import utilities.UtilidadesOtros;
import utilities.UtilidadesServidor;

public class ControladorChat implements Initializable {

	@FXML private ListView<String> listaContactos;
	@FXML private MenuItem anadir;
	@FXML private MenuItem eliminar;
	@FXML private AnchorPane panelChat;

	private Map<String,ControladorConversacion> contactos;
	private ObservableList<String> itemsContactos=FXCollections.observableArrayList();
	private UtilidadesChat uc;

	private ControladorConversacion cc;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		List<Message> mensajesOff = new ArrayList<Message>();	
		try 
		{
			uc = new UtilidadesChat();
		} 
		catch (IOException e2) 
		{
			e2.printStackTrace();
		}
		contactos = uc.getContacts();
		
		for(ControladorConversacion contacto : contactos.values())
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
			public void handle(MouseEvent event) {
				if (listaContactos.getSelectionModel().isEmpty() == false) 
				{
					FXMLLoader loader = null;
					cc = contactos.get(listaContactos.getSelectionModel().getSelectedItem());
					loader.setController(cc);
					try 
					{
						panelChat.getChildren().add(loader.load());
						
						cc.setContacto(listaContactos.getSelectionModel().getSelectedItem());
						cc.setChat(cc.getChat());
						cc.setFriend(cc.isFriend());
						cc.setId(cc.getId());
						cc.setMensajes(cc.getMensajes());
						cc.setNombre(cc.getNombre());
						cc.setPresencia(cc.getPresencia());
						
						//aqui ahora tiene que poner todos los mensajes en labels y que se vean en la conver
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
				//listaContactos.refresh();
				event.consume();
			}

		});

		eliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				uc.eliminarContacto(listaContactos.getSelectionModel().getSelectedItem());
				event.consume();
			}

		});
		
		ChatManager.getInstanceFor(UtilidadesServidor.scon).addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new ChatMessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) 
					{						
						Platform.runLater(() -> {
							//aqui solo muevo mensajes de gente que no tengo agregada
						});
						//nose si recibe de los que no tengo agregados o todos, hay que mirarlo
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
