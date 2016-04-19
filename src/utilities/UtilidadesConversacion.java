package utilities;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import datos.FicheroXML;
import modelo.MessageArchive;

public class UtilidadesConversacion {
	public static List<Message> getOnlineHistory(BD bd, String from, String to) {
		List<MessageArchive> lista = new ArrayList<MessageArchive>();
		List<Message> msg = new ArrayList<Message>();
		boolean check = false;

		lista = bd.getMensajes();

		for (MessageArchive ma : lista) {
			Message ms = new Message();
			check = false;
			if (ma.getFromJID().matches(from) && ma.getToJID().matches(to)) {
				ms.setBody(ma.getBody());
				ms.setFrom(ma.getFromJID());
				ms.setTo(ma.getToJID());
				check = true;
			} else {
				if (ma.getToJID().matches(from) && ma.getFromJID().matches(to)) {
					ms.setBody(ma.getBody());
					ms.setFrom(ma.getFromJID());
					ms.setTo(ma.getToJID());
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
