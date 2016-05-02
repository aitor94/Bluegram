package utilities;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.jivesoftware.smack.packet.Message;

import datos.FicheroXML;

public class UtilidadesConversacion 
{
	public static List<Message> getOnlineHistory(BD bd, String from, String to) {
		List<bD.MessageArchive> lista = new ArrayList<bD.MessageArchive>();
		List<Message> msg = new ArrayList<Message>();
		boolean check = false;

		UtilidadRegistro ur = new UtilidadRegistro();
		
		try 
		{
			lista = ur.busquedaEJB().getOnlineMessages(from,to);
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
		}

		for (bD.MessageArchive ma : lista) {
			Message ms = new Message();
			check = false;
			if (ma.getFromJID().matches(from) && ma.getToJID().matches(to)) {
				ms.setBody(ma.getBody());
				ms.setFrom(ma.getFromJID());
				ms.setTo(ma.getToJID());
				ms.setSubject("txt");
				check = true;
			} else {
				if (ma.getToJID().matches(from) && ma.getFromJID().matches(to)) {
					ms.setBody(ma.getBody());
					ms.setFrom(ma.getFromJID());
					ms.setTo(ma.getToJID());
					ms.setSubject("txt");
					check = true;
				}
				else
					check=false;
			}
			if (check)
				msg.add(ms);
			check = false;
		}
		FicheroXML.escribeFichero(msg, to.split("@")[0]);
		return msg;
	}
}
