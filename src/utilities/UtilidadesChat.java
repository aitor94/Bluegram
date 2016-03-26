package utilities;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import modelo.Contacto;


public class UtilidadesChat 
{
	public List<Contacto> getContacts()
	{
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		List<Contacto> contactos = new ArrayList<Contacto>();
		
		for (RosterEntry entry : roster.getEntries())
		{
			Contacto contacto = new Contacto();
			contacto.setId(entry.getUser());
			contacto.setNombre(entry.getName());
			
			contacto.setChat(ChatManager.getInstanceFor(UtilidadesServidor.scon).createChat(contacto.getId()));
			contacto.setPresencia(roster.getPresence(contacto.getId()).toString());
		}
		
		return contactos;
	}
	
	public void anadirContacto()
	{
		
	}
	
	public void eliminarContacto()
	{
		
	}
}
