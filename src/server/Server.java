package server;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import other.Constants;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rafał Sekular
 */
class Server
{
	private ServerSocket server;

	/**
	 * Wątek do akceptowania klientów
	 */
	private Thread acceptClientThread;
	/**
	 * Lista klientów
	 */
	private final List < ClientThread > clientThreadList = Collections.synchronizedList ( new ArrayList <> () );

	/**
	 * Stan serwera
	 */
	private boolean powerOn;

	/**
	 * Konstruktor
	 */
	Server ()
	{
		Database.init ();
		this.powerOn = false;
	}

	/**
	 * @return stan uruchomienia serwera (true - włączony | false - wyłączony)
	 */
	boolean isPowerOn ()
	{
		return powerOn;
	}

	/**
	 * Włącza serwer
	 *
	 * @return true - gdy udało się uruchomić serwer, jeśli nie to false
	 */
	boolean startServer ()
	{
		try
		{
			int port = 2000;
			this.server = new ServerSocket ( port );

			this.acceptClientThread = new Thread ( () -> {
				try
				{
					Socket client;
					while ( this.server != null )
					{
						client = this.server.accept ();
						System.out.println ( Constants.MESSAGE_CLIENT_ACCEPTED );

						this.clientThreadList.add ( new ClientThread ( client ) );
					}
				}
				catch ( IOException e )
				{
					System.out.println ( Constants.MESSAGE_ACCEPT_CLIENT_THREAD_IS_STOPED );
				}

			} );

			this.acceptClientThread.start ();
			System.out.println ( Constants.MESSAGE_SERVER_IS_ON );
			this.powerOn = true;
			return true;
		}
		catch ( BindException e )
		{
			Platform.runLater ( () -> {
				Alert alert = new Alert ( Alert.AlertType.ERROR );
				alert.setTitle ( Constants.MESSAGE_SERVER_ERROR );
				alert.setHeaderText ( Constants.MESSAGE_THE_PORT_IS_BUSY );
				alert.setContentText ( Constants.MESSAGE_THE_APPLICATION_WILL_BY_CLOSED );

				alert.showAndWait ();

				Platform.exit ();
				System.exit ( 0 );
			} );
		}
		catch ( IOException e )
		{
			e.printStackTrace ();
		}

		return false;
	}

	/**
	 * Wyłącza serwer
	 *
	 * @return true - gdy udało się wyłączyć serwer, jeśli nie to false
	 */
	boolean stopServer ()
	{
		if ( this.acceptClientThread != null )
		{
			this.acceptClientThread.interrupt ();
			this.acceptClientThread = null;
		}

		this.clientThreadList.forEach ( ClientThread::closeConnection );

		this.clientThreadList.clear ();

		try
		{
			if ( this.server != null )
			{
				this.server.close ();
				this.server = null;
			}

			System.out.println ( Constants.MESSAGE_SERVER_IS_OFF );
			this.powerOn = false;
			return true;
		}
		catch ( IOException e )
		{
			e.printStackTrace ();
		}

		return false;
	}
}
