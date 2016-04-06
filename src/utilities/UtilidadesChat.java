package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import modelo.Contacto;

public class UtilidadesChat 
{
	public Map<String,Contacto> getContacts()
	{
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		Map<String,Contacto> contactos = new HashMap<String,Contacto>();
		
		for (RosterEntry entry : roster.getEntries())
		{
			Contacto contacto = new Contacto();
			contacto.setId(entry.getUser());
			contacto.setNombre(entry.getName());
			
			contacto.setChat(ChatManager.getInstanceFor(UtilidadesServidor.scon).createChat(contacto.getId()));
			contacto.setPresencia(roster.getPresence(contacto.getId()).toString());
			System.out.println(entry.getUser());
			contactos.put(contacto.getId(),contacto);
		}
		
		return contactos;
	}
	
	public void anadirContacto()
	{
		
	}
	
	public void eliminarContacto()
	{
		
	}
	
	public List<Message> getOfflineMessages() throws NoResponseException, XMPPErrorException, NotConnectedException
	{
		OfflineMessageManager omm = new OfflineMessageManager(UtilidadesServidor.scon);
		List<Message> mensajes=null;
		
		if(omm.getMessageCount()==0)
		{
			mensajes = new ArrayList<Message>();
			System.out.println("0 mensajes offline");
		}
		else
		{
			mensajes = omm.getMessages();
			omm.deleteMessages();
			System.out.println("varios mensajes offline->"+mensajes.size());
		}
		
		return mensajes;
	}
}
