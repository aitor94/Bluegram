package utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class UtilidadesArchivos 
{
	private static final String BucketName  = "blue222"; 
    private static final String keyName = "AKIAJB6IOYUK4JQZECIQ";
    private static final String secret = "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";
    
    public static String sendFile(String name,String path)
    {
    	String nombre = name;
    	System.out.println(name+","+path);
    	System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
        System.out.println("empiezo a enviar el archivo");
    	AWSCredentials credentials = new BasicAWSCredentials(keyName,secret);
    	System.out.println("ahora va el client");
	    AmazonS3Client s3Client = new AmazonS3Client(credentials);
	    System.out.println("he hecho lo de las credenciales");
	    int count = 0;System.out.println(s3Client.doesObjectExist(BucketName,"panda"));
	    while(s3Client.doesObjectExist(BucketName,nombre))
	    {System.out.println("dentro del while");
	    	count++;
	    	nombre = nombre.concat("("+Integer.toString(count)+")");
	    }System.out.println("he pasado el while");
	    int n = (int) (100000 + Math.random() * 900000);
	    nombre = nombre.concat(Integer.toString(n));
	    System.out.println("empiezo a mandar");
	    s3Client.putObject(new PutObjectRequest(BucketName,nombre,new File(path)));
	    System.out.println("mandado");
	    return nombre;
    }
    
    public static void receiveFile(String key, String path)
    {
    	System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
        
    	AWSCredentials credentials = new BasicAWSCredentials(keyName,secret);
	    AmazonS3Client s3Client = new AmazonS3Client(credentials);
	    
	    if(s3Client.doesObjectExist(BucketName,key))
	    {
	    	S3Object object = s3Client.getObject(new GetObjectRequest(BucketName,key));
	    	
	    	InputStream reader = new BufferedInputStream(object.getObjectContent());
    		File file = new File(path);
    		file.getParentFile().mkdirs();
    		OutputStream writer;
    		
    		try 
    		{
				writer = new BufferedOutputStream(new FileOutputStream(file));
				int read = -1;

        		while( ( read = reader.read() ) != -1 ) 
        		{
        		    writer.write(read);
        		}

        		writer.flush();
        		writer.close();
        		reader.close();
			} 
    		catch (FileNotFoundException e) 
    		{
				e.printStackTrace();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
	    }
	    else
	    {
	    	//si entra aqui es que el archivo esta borrado
	    }
    }
}
