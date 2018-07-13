package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import other.Constants;
import other.Package;

/**
 * @author Rafał Sekular
 */
public class Main extends Application
{

	@Override
	public void start ( Stage primaryStage ) throws Exception
	{
		Parent root = FXMLLoader.load ( getClass ().getResource ( "layout.fxml" ) );
		primaryStage.setTitle ( Constants.MESSAGE_DICTIONARY_CLIENT_NAME );
		primaryStage.setScene ( new Scene ( root, 1000, 700 ) );
		primaryStage.setResizable ( false );
		primaryStage.setOnCloseRequest ( ( windowEvent ) -> {
			ClientController.sendPackage ( new Package <> ( Constants.EXIT, null ) );
			Platform.exit ();
			System.exit ( 0 );
		} );
		primaryStage.show ();
	}

	public static void main ( String[] args )
	{
		launch ( args );
	}
}
