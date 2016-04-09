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
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

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
import utilities.UtilidadesChat;
import utilities.UtilidadesServidor;

public class ControladorChat implements Initializable
{

	@FXML private ListView<String> listaContactos;
	@FXML private MenuItem anadir;
	@FXML private MenuItem eliminar;
	@FXML private AnchorPane panelChat;
	
	private Map<String,Contacto> contactos;
	private ObservableList<String> itemsContactos;
	private List<Message> mensajesRecibidos;
	
	private ControladorConversacion cc;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		contactos = UtilidadesChat.getContacts();
		
		itemsContactos = FXCollections.observableArrayList();
		List<Message> mensajesOff=new ArrayList<Message>();
		listaContactos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		try {
			mensajesOff = UtilidadesChat.getOfflineMessages();
		} catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
			e.printStackTrace();
		}
		
		try 
		{
			UtilidadesServidor.scon.sendPacket(new Presence(Presence.Type.available));
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}
		
		UtilidadesChat.asignaMensajes(contactos, itemsContactos, mensajesOff);
		
		listaContactos.setItems(itemsContactos);
		
		listaContactos.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				if(listaContactos.getSelectionModel().isEmpty()==false)
				{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistaControlador/Conversacion.fxml"));
					
					try 
					{
						AnchorPane ap=loader.load();
						panelChat.getChildren().add(ap);
						cc=loader.getController();
						
						/*Contacto cont=new Contacto();
						
						cont.setId(id);*/
						cc.getContacto().setText("");
						cc.setContacto(listaContactos.getSelectionModel().getSelectedItem());
						
						cc.setPersona(new Contacto());
						
						cc.setChat(listaContactos.getSelectionModel().getSelectedItem());
						
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					} 
				}
			}
		});
		
		anadir.setOnAction(new EventHandler<ActionEvent>() 
		{

			@Override
			public void handle(ActionEvent event) {
				UtilidadesChat.anadirContacto();
				listaContactos.refresh();
			}
			
		});
		
		eliminar.setOnAction(new EventHandler<ActionEvent>() 
		{

			@Override
			public void handle(ActionEvent event) {
				UtilidadesChat.eliminarContacto(listaContactos.getSelectionModel().getSelectedItem());
				listaContactos.refresh();
			}
			
		});
		new Thread(hiloMensajes()).start();
	}
	
	public Task<Void> hiloMensajes()//este hilo va  a actualizar todo el rato la interfaz de usuario
	{
		Task<Void> task = new Task<Void>() 
		{
		    @Override public Void call() 
		    {
		    	long time;
		    	
		    	while(true)
		    	{
		    		for(Message mensaje : mensajesRecibidos)
            		{
		    			Contacto contacto = null;
		    			if(contactos.containsKey(mensaje.getFrom()))
		    			{
		    				contacto = contactos.get(mensaje.getFrom());
		    				contacto.addMessage(mensaje);
		    				contactos.put(contacto.getId(),contacto);
		    			}
		    			else
		    			{
		    				//si entra aqui el mensaje no es de ni gun contacto, tenemos que gestionar esto
		    			}
            			mensajesRecibidos.remove(mensaje);
            		}
		    		if(mensajesRecibidos.isEmpty())
		    			time = 1000;
		    		else
		    			time = 100;
		    		try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		};
		return task;		
	}
}
