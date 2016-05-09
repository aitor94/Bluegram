package utilities;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.jivesoftware.smack.packet.Message;

import datos.FicheroXML;

public class UtilidadesConversacion 
{
	public static List<Message> getOnlineHistory(String from, String to) {
		List<bD.MessageArchive> lista = new ArrayList<bD.MessageArchive>();
		List<Message> msg = new ArrayList<Message>();

		UtilidadRegistro ur = new UtilidadRegistro();
		
		try 
		{
			lista = ur.busquedaEJB().getOnlineMessages(from,to);
			ur.closeConnection();
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
		}

		System.out.println(lista.size());
		
		for (bD.MessageArchive ma : lista) 
		{
			Message ms = new Message();
			ms.setSubject("txt");
			ms.setBody(ma.getBody());
			ms.setFrom(ma.getFromJID());
			ms.setTo(ma.getToJID());
			
			msg.add(ms);
		}
		FicheroXML.escribeFichero(msg,UtilidadesServidor.scon.getUser().split("@")[0] +from.split("@")[0]);
		return msg;
	}
}
