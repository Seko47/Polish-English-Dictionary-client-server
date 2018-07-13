package server;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import other.Constants;

/**
 * Klasa głównego kontrolera dla okna serwera
 *
 * @author Rafał Sekular
 */
public class MainController
{
	/**
	 * Przechowuje obiekt serwera
	 */
	private static final Server server = new Server ();

	/**
	 * Przycisk do włączenia/wyłączenia serwera
	 */
	@FXML
	private ToggleButton buttonToggleServer;

	/**
	 * Przełącza serwer w tryb włączony/wyłączony
	 */
	@FXML
	private void switchServerOnOff ()
	{
		if ( !MainController.server.isPowerOn () )
		{
			if ( MainController.server.startServer () )
			{
				this.buttonToggleServer.setText ( Constants.MESSAGE_STOP );
			}
		}
		else
		{
			if ( MainController.server.stopServer () )
			{
				this.buttonToggleServer.setText ( Constants.MESSAGE_START );
			}
		}
	}

	/**
	 * Wyłącza serwer przy zamykaniu aplikacji
	 */
	static void closeServerOnExit ()
	{
		if ( MainController.server.isPowerOn () )
		{
			MainController.server.stopServer ();
		}
	}
}
