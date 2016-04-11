package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;
import modelo.Contacto;

public class UtilidadesChat {
	public static Map<String, Contacto> getContacts() {
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		Map<String, Contacto> contactos = new HashMap<String, Contacto>();

		for (RosterEntry entry : roster.getEntries()) {
			Contacto contacto = new Contacto();
			contacto.setId(entry.getUser());
			contacto.setNombre(entry.getName());

			contacto.setChat(ChatManager.getInstanceFor(UtilidadesServidor.scon).createChat(contacto.getId()));
			contacto.setPresencia(roster.getPresence(contacto.getId()).toString());
			contactos.put(contacto.getId(), contacto);
		}

		return contactos;
	}
	
	public static Contacto getContact(String contact)
	{
		Map<String, Contacto> contactos = getContacts();
		
		return contactos.get(contact);
	}

	public static void anadirContacto() {
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

	public static void eliminarContacto(String string) {

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

	public static List<Message> getOfflineMessages()
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
	
	public static void asignaMensajes(Map<String,Contacto> contactos , ObservableList<String> itemsContactos, List<Message> mensajesOff)
	{
		
		for(Message mensaje : mensajesOff)
		{
			String from=mensaje.getFrom();
			Contacto cto;
			cto = contactos.get(from.split("@")[0]);
			if(cto!=null)
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
	}
	
	
}
