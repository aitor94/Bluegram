package datos;

import org.jivesoftware.smack.packet.Message;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.List;

public class XmlDOM {

	public static void crearFichero(List<Message> listMessage,String path) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("historial");
			doc.appendChild(rootElement);

			for (Message m : listMessage) {
				Element mensaje = doc.createElement("mensaje");

				NamedNodeMap atribMensaje = mensaje.getAttributes();
				Attr from = doc.createAttribute("from");
				Attr to = doc.createAttribute("to");
				Attr subject=doc.createAttribute("subject");
				from.setValue(m.getFrom());
				to.setValue(m.getTo());
				subject.setValue(m.getSubject());

				atribMensaje.setNamedItem(from);
				atribMensaje.setNamedItem(to);
				atribMensaje.setNamedItem(subject);
				
				
				Element body =doc.createElement("body");
				body.setTextContent(m.getBody());
				
				mensaje.appendChild(body);

				rootElement.appendChild(mensaje);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path+".xml"));
			transformer.transform(source, result);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void xmlModify(Message msg,String path) {

		try {
			File inputFile = new File(path+".xml");
			inputFile.getParentFile().mkdirs();
			inputFile.createNewFile();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputFile);
			
			Node historial=doc.getFirstChild();
			
			Element mensaje = doc.createElement("mensaje");

			NamedNodeMap atribMensaje = mensaje.getAttributes();
			Attr from = doc.createAttribute("from");
			Attr to = doc.createAttribute("to");
			Attr subject=doc.createAttribute("subject");
			from.setValue(msg.getFrom());
			to.setValue(msg.getTo());
			subject.setValue(msg.getSubject());

			atribMensaje.setNamedItem(from);
			atribMensaje.setNamedItem(to);
			atribMensaje.setNamedItem(subject);
			
			Element body =doc.createElement("body");
			body.setTextContent(msg.getBody());
			
			mensaje.appendChild(body);
			historial.appendChild(mensaje);
			
			// write the content on console
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(path+".xml");
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
