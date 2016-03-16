package vistaControlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.naming.NamingException;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.captcha.Captcha;
import server.EJBInterface;
import utilities.BD;
import utilities.UtilidadRegistro;

public class ControladorRegistro implements Initializable
{
	@FXML
	private TextField nombre;
	@FXML
	private PasswordField pass;
	@FXML
	private PasswordField confPass;
	@FXML
	private TextField correo;
	@FXML
	private Button boton;
	@FXML
	private TextField capt;
	@FXML
	private ImageView img;

	private String answer;
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		Captcha captcha = new Captcha.Builder(200, 50)
		           .addText()
		           .addBackground()
		           .addNoise()
		           .gimp()
		           .addBorder()
		           .build(); 
		
		Image image = SwingFXUtils.toFXImage(captcha.getImage(), null);
		
		img.setImage(image);
		answer = captcha.getAnswer();
		System.out.println(answer);
		boton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				if(nombre.getText().isEmpty() || correo.getText().isEmpty() || pass.getText().isEmpty())
				{
					lanzaAlerta("Todos los campos son obligatorios");
				}
				else
				{
					if(pass.getText().equals(confPass.getText()))
					{
						if(answer.equals(capt.getText()))
						{
							try 
							{
								UtilidadRegistro ur = new UtilidadRegistro();
								
								if(ur.busquedaEJB().validateEmail(correo.getText()))
								{
									BD bd = new BD();
									
									if(bd.existeUsuario(nombre.getText())==false)
									{
										FXMLLoader loader=new FXMLLoader();
										loader.setLocation(ControladorRegistro.class.getResource("/vistaControlador/RegistroFinal.fxml"));
										Scene escena = new Scene(loader.load());
										
										ControladorRegistroFinal ctrl = loader.getController();
										ctrl.setCode(ur.busquedaEJB().sendEmail(correo.getText()));
										ctrl.setName(nombre.getText());
										ctrl.setPassword(pass.getText());
										ctrl.setEmail(correo.getText());
										
										Stage stg=(Stage)boton.getScene().getWindow();
										stg.setScene(escena);
										stg.show();
									}
									else
									{
										lanzaAlerta("Ya existe un usuario con ese nombre, prueba con otro");
										nombre.setText("");
									}
								}
								else
								{
									lanzaAlerta("Email no valido");
									correo.setText("");
								}
								
							} 
							catch (NamingException e) 
							{
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							lanzaAlerta("Captcha incorrecto");
							capt.setText("");
						}
					}
					else
					{
						lanzaAlerta("Las contraseñas no coinciden");
						pass.setText("");
						confPass.setText("");
					}
				}
			}
		});
	}
	
	private void lanzaAlerta(String descripcion)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(descripcion);

		alert.showAndWait();
	}

}
