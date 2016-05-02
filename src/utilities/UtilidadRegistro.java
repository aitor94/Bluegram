package utilities;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javafx.scene.control.Alert.AlertType;
import server.EJBInterface;

public class UtilidadRegistro 
{
	private Context contexto;
	
	public EJBInterface busquedaEJB() throws NamingException
	{
		Properties prop = new Properties();
		
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory" );
		prop.put(Context.PROVIDER_URL, "http-remoting://54.165.111.148:8080" );
		prop.put(Context.SECURITY_PRINCIPAL,"aitor");
		prop.put(Context.SECURITY_CREDENTIALS,"1234");
		prop.put("jboss.naming.client.ejb.context", true);
		prop.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming" );
		
		contexto = null;
		try {
			contexto = new InitialContext(prop);
		} catch (NamingException e) {
			UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error contexto EJB");
		}
		
		return (EJBInterface) contexto.lookup("/ValidationEJB/EJB!server.EJBInterface");			
	}
	
	public void closeConnection() throws NamingException
	{
		contexto.close();
	}

	public Context getContexto() {
		return contexto;
	}

	public void setContexto(Context contexto) {
		this.contexto = contexto;
	}
}
