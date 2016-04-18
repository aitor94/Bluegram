package datos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jivesoftware.smack.packet.Message;

import modelo.Historial;
import modelo.Mensaje;

public class FicheroXML {

	private static final String path = "history/";

	public static String getPath() {
		return path;
	}

	public static void escribeFichero(List<Message> listaMessages, String contacto) {
		JAXBContext jaxbContext;
		Historial lista = new Historial();
		List<Mensaje> listaMensajes = new ArrayList<Mensaje>();

		for (Message m : listaMessages) {
			Mensaje ms = new Mensaje();
			if (m.getBody() == null || m.getFrom() == null || m.getTo() == null)
				System.out.println("error, mensajes incompletos"+m.toString());
			else {
				ms.setBody(m.getBody());
				ms.setFrom(m.getFrom());
				ms.setTo(m.getTo());
				listaMensajes.add(ms);
			}
		}

		lista.setLista(listaMensajes);
		try {
			jaxbContext = JAXBContext.newInstance(Historial.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(lista, new File(path + contacto + ".xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static List<Message> leeFichero(String nombre) {
		Historial lista = null;
		List<Message> l = new ArrayList<Message>();
		JAXBContext jaxbContext;

		try {
			jaxbContext = JAXBContext.newInstance(Historial.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File xml = new File(path + nombre + ".xml");
			if (xml.exists())
				lista = (Historial) unmarshaller.unmarshal(xml);
			else
				lista = new Historial();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		List<Mensaje> ls = lista.getLista();

		for (Mensaje m : ls) {
			Message msg = new Message();
			msg.setBody(m.getBody());
			msg.setFrom(m.getFrom());
			msg.setTo(m.getTo());
			l.add(msg);
		}
		return l;

	}

}
