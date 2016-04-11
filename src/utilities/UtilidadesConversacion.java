package utilities;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import modelo.MessageArchive;

public class UtilidadesConversacion {
	public static List<Message> getOnlineHistory(BD bd, String from,String to) {
		List<MessageArchive> lista = new ArrayList<MessageArchive>();
		List<Message> msg = new ArrayList<Message>();
		boolean check = false;
		
		lista = bd.getMensajes();

		for (MessageArchive ma : lista) {
			Message ms = new Message();
			if (ma.getFromJID().matches(from) && ma.getToJID().matches(to)) 
			{
				ms.setBody(ma.getBody());
				check = true;
			}
			else
				check=false;
			if (ma.getToJID().matches(from) && ma.getFromJID().matches(to)) {
				ms.setBody(ma.getBody());
				check = true;
			}
			if(check)
				msg.add(ms);
			check = false;
		}

		return msg;
	}
}
