package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextInputDialog;
import modelo.Contacto;
import vistaControlador.ControladorConversacion;

public class UtilidadesChat 
{
	private FXMLLoader loader;
	
	public FXMLLoader getLoader() {
		return loader;
	}

	public void setLoader(FXMLLoader loader) {
		this.loader = loader;
	}

	public UtilidadesChat() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistaControlador/Conversacion.fxml"));
		loader.load();
	}
	
	public Map<String,ControladorConversacion> getContacts() 
	{
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		Map<String,ControladorConversacion> contactos = new HashMap<String,ControladorConversacion>();
		ControladorConversacion cc = loader.getController();

		for (RosterEntry entry : roster.getEntries()) 
		{
			cc.setContacto(entry.getName());
			cc.setFriend(true);
			cc.setId(entry.getUser());
			cc.setMensajes(new ArrayList<Message>());
			cc.setNombre(entry.getName());
			cc.setPresencia(roster.getPresence(entry.getUser()).toString());
			
			contactos.put(cc.getNombre(),cc);//falta meter los msgs en los labels y en el listview
		}

		return contactos;
	}
	
	public void anadirContacto() 
	{
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		System.out.println("Boton añadir del menu pulsado");
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Añadir contacto");
		dialog.setContentText("Inserte el ID del contacto:");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			try {
				roster.createEntry(result.get(), result.get(), null);
				roster.reload();
			} catch (NoResponseException e) {
				System.out.println("Error de no respuesta");
			} catch (XMPPErrorException e) {
				System.out.println("Error de XMPP");
			} catch (NotConnectedException e) {
				System.out.println("Error de no conexion");
			} catch (NotLoggedInException e) {
				System.out.println("Error de no logeado");
			}
		}

	}

	public void eliminarContacto(String string) {

		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		
        RosterEntry re=roster.getEntry(string);
        try 
        {
			roster.removeEntry(re);
			roster.reload();
		} 
        catch (NoResponseException e) 
        {
        	System.out.println("Error de no respuesta");
		}
        catch (XMPPErrorException e) 
        {
        	System.out.println("Error de XMPP");
		}
        catch (NotConnectedException e) 
        {
        	System.out.println("Error de no conexion");
		} 
        catch (NotLoggedInException e) 
        {
			System.out.println("Error de no logeado");
		}
    }

	public List<Message> getOfflineMessages()
			throws NoResponseException, XMPPErrorException, NotConnectedException 
	{
		OfflineMessageManager omm = new OfflineMessageManager(UtilidadesServidor.scon);
		List<Message> mensajes = null;

		if (omm.getMessageCount() == 0) {
			mensajes = new ArrayList<Message>();
			System.out.println("0 mensajes offline");
		} else {
			mensajes = omm.getMessages();
			omm.deleteMessages();
			System.out.println("varios mensajes offline->" + mensajes.size());
		}

		return mensajes;
	}
	
	public void asignaMensajes(Map<String,ControladorConversacion> contactos , List<Message> mensajesOff)
	{		
		for(Message mensaje : mensajesOff)
		{
			ControladorConversacion cto = contactos.get(mensaje.getFrom().split("@")[0]);
			
			if(cto!=null)
			{
				cto.addMessage(mensaje);
				Chat chat = ChatManager.getInstanceFor(UtilidadesServidor.scon).createChat(mensaje.getFrom(), new ChatMessageListener() 
				{
					@Override
					public void processMessage(Chat chat, Message message) 
					{
						//aqui proceso los mensajes de este contacto
					}
				});
				
				cto.setChat(chat);
			}
			else
			{
				cto = loader.getController();
				cto.setNombre(mensaje.getFrom());
				cto.setId(mensaje.getFrom());
				cto.setFriend(false);
				cto.setMensajes(new ArrayList<Message>(){{add(mensaje);}});
				Chat chat = ChatManager.getInstanceFor(UtilidadesServidor.scon).createChat(mensaje.getFrom(), new ChatMessageListener() 
				{
					@Override
					public void processMessage(Chat chat, Message message) 
					{
						//aqui proceso los mensajes de este contacto
					}
				});				
				cto.setChat(chat);
			}
			contactos.put(cto.getId(), cto);
		}		
		mensajesOff.clear();
	}
	
	public void labelGenerator(String texto)
	{
		
	}
}
