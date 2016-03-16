package utilities;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import server.EJBInterface;

public class UtilidadRegistro 
{
	public EJBInterface busquedaEJB() throws NamingException
	{
		Properties prop = new Properties();
		
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory" );
		prop.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080" );
		prop.put(Context.SECURITY_PRINCIPAL,"aitor");
		prop.put(Context.SECURITY_CREDENTIALS,"1234");
		prop.put("jboss.naming.client.ejb.context", true);
		prop.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming" );
		
		Context contexto = null;
		try {
			contexto = new InitialContext(prop);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("Error contexto EJB");
		}
		
		return (EJBInterface) contexto.lookup("/ValidationEJB/EJB!server.EJBInterface");			
	}
}
