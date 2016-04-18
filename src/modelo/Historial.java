package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Historial implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Mensaje> lista;
	
	public void setLista(List<Mensaje> lista) {
		this.lista = lista;
	}

	@XmlElement(name="mensaje")
	public List<Mensaje> getLista()
	{
		if(lista==null)
			lista=new ArrayList<Mensaje>();
		return lista;
	}
}
