package vistaControlador;

import java.io.IOException;
import java.net.URL;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
	private UtilidadesChat utilidades;
	private ObservableList<String> itemsContactos;
	private List<Message> mensajesRecibidos;
	private FXMLLoader loader;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		utilidades = new UtilidadesChat();
		contactos = utilidades.getContacts();
		
		itemsContactos = FXCollections.observableArrayList();
		List<Message> mensajesOff=null;
		listaContactos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		try {
			mensajesOff = utilidades.getOfflineMessages();
		} catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try 
		{
			UtilidadesServidor.scon.sendPacket(new Presence(Presence.Type.available));
		} catch (NotConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(Contacto contacto : contactos.values())
			itemsContactos.add(contacto.getNombre());
		
		for(Message mensaje : mensajesOff)
		{
			Contacto cto;
			if((cto = contactos.get(mensaje.getFrom()))!=null)
			{
				cto.addMessage(mensaje);
				contactos.put(cto.getId(), cto);
			}
			else
			{
				//aqui mensajes de contactos que no tengo agregados
			}
		}
		
		mensajesOff.clear();
		
		listaContactos.setItems(itemsContactos);
		
		
		ChatManager.getInstanceFor(UtilidadesServidor.scon).addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) 
                    {
                    	mensajesRecibidos.add(message);
                    }
                });
            }
        });
		
		listaContactos.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				if(listaContactos.getSelectionModel().isEmpty()==false)
				{
					loader = new FXMLLoader(getClass().getResource("/vistaControlador/Conversacion.fxml"));
					
					try 
					{
						AnchorPane ap=loader.load();
						panelChat.getChildren().add(ap);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
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
