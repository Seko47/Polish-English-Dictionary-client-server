package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import other.Constants;

/**
 * Główna klasa programu
 *
 * @author Rafał Sekular
 */
public class Main extends Application
{

	@Override
	public void start ( Stage primaryStage ) throws Exception
	{
		Parent root = FXMLLoader.load ( getClass ().getResource ( "layout.fxml" ) );
		primaryStage.setTitle ( Constants.MESSAGE_DICTIONARY_SERVER_NAME );
		primaryStage.setScene ( new Scene ( root, 270, 260 ) );
		primaryStage.setResizable ( false );
		primaryStage.setOnCloseRequest ( ( windowEvent ) -> {
			MainController.closeServerOnExit ();
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
