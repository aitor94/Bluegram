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
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

public class UtilidadesArchivos 
{
	private static final String BucketName  = "blue222"; 
    private static final String keyName = "AKIAJB6IOYUK4JQZECIQ";
    private static final String secret = "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";
    
    public static String sendFile(String name,String path,ProgressIndicator pi)
    {
    	String nombre = name;
    	
    	System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
        
    	AWSCredentials credentials = new BasicAWSCredentials(keyName,secret);
    	
	    AmazonS3Client s3Client = new AmazonS3Client(credentials);
	    
	    int count = 0;
	    while(s3Client.doesObjectExist(BucketName,nombre))
	    {
	    	count++;
	    	nombre = nombre.concat("("+Integer.toString(count)+")");
	    }
	    int n = (int) (100000 + Math.random() * 900000);
	    nombre = nombre.concat(Integer.toString(n));
	    System.out.println("empiezo a mandar");
	    File f = new File(path);
	    PutObjectRequest por = new PutObjectRequest(BucketName,nombre,f);

	    por.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent arg0) 
			{
				double value = ((double) arg0.getBytesTransferred())/f.length();
				Platform.runLater(() -> pi.setProgress(value+pi.getProgress()));				
			}
	    });
	    
	    s3Client.putObject(por);
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
