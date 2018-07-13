package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import models.TranslationModel;
import other.Account;
import other.Constants;
import other.Package;
import packages.TranslationPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Rafał Sekular
 */
public class ClientController
{
	/**
	 * Lista zawiera obsługiwane języki
	 */
	private ObservableList < String > languagesList = FXCollections.observableArrayList ( Constants.MESSAGE_POLISH, Constants.MESSAGE_ENGLISH );
	/**
	 * Lista zawiera informacje o tłumaczeniach pobranych z serwera
	 */
	private ObservableList < TranslationModel > translationData = FXCollections.observableArrayList ();

	/**
	 * Wątek obsługujący odbieranie wiadomości od serwera
	 */
	private Thread readMessageThread;

	/**
	 * Zawiera dane zalogowanego konta
	 */
	private Account account;

	private Socket client;
	private static ObjectInputStream objIn;
	private static ObjectOutputStream objOut;

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab wordManagementTab;

	@FXML
	private Tab accountManagementTab;

	@FXML
	private Button registerButton;

	@FXML
	private Button loginLogoutButton;

	@FXML
	private TextField enterWordField;

	@FXML
	private ChoiceBox < String > chooseLanguageBox;

	@FXML
	private Label userLoginLabel;

	@FXML
	private TableView < TranslationModel > translationResultTable;

	@FXML
	private TableColumn < TranslationModel, String > wordTranslationResultColumn;

	@FXML
	private TableColumn < TranslationModel, Double > ratingTranslationResultColumn;

	@FXML
	private TableColumn < TranslationModel, Boolean > isConfirmedTranslationResultColumn;

	@FXML
	private Pane translationProposalPane;

	@FXML
	private TextField translationProposalPolishTextField;

	@FXML
	private TextField translationProposalEnglishTextField;

	/**
	 * Konstruktor domyślny
	 */
	public ClientController ()
	{
		startClient ();
	}

	@FXML
	private void initialize ()
	{
		this.chooseLanguageBox.setItems ( languagesList );
		this.chooseLanguageBox.setValue ( languagesList.get ( 0 ) );
		this.chooseLanguageBox.setTooltip ( new Tooltip ( Constants.MESSAGE_CHOOSE_LANGUAGE_BOX_TOOLTIP ) );

		this.wordTranslationResultColumn.setCellValueFactory ( cellData -> cellData.getValue ().word1Property () );
		this.ratingTranslationResultColumn.setCellValueFactory ( cellData -> cellData.getValue ().avg_ratingProperty ().asObject () );
		this.isConfirmedTranslationResultColumn.setCellValueFactory ( cellData -> cellData.getValue ().confirmedProperty () );

		this.translationResultTable.setTooltip ( new Tooltip ( Constants.MESSAGE_TRANSLATION_RESULT_TABLE_TOOLTIP ) );
		this.translationResultTable.setItems ( this.translationData );
		this.translationResultTable.setRowFactory ( tableView -> {
			TableRow < TranslationModel > row = new TableRow <> ();
			row.setOnMouseClicked ( mouseEvent -> {
				if ( this.account.isLoggedIn () )
				{
					if ( !row.isEmpty () && mouseEvent.getButton () == MouseButton.PRIMARY && mouseEvent.getClickCount () == 2 )
					{
						TranslationModel translation = row.getItem ();

						if ( translation.getID_translation () == 0 ) return;

						Dialog < Integer > dialog = new Dialog <> ();
						dialog.setTitle ( Constants.MESSAGE_TRANSLATION_RATING );
						dialog.setHeaderText ( Constants.MESSAGE_SELECT_THE_RATING_FOR_THE_SELECTED_TRANSLATION );

						ButtonType rateButtonType = new ButtonType ( Constants.MESSAGE_RATE, ButtonBar.ButtonData.OK_DONE );
						dialog.getDialogPane ().getButtonTypes ().addAll ( rateButtonType, ButtonType.CANCEL );

						Slider slider = new Slider ( 0, 10, 5 );
						slider.setMajorTickUnit ( 1 );
						slider.setMinorTickCount ( 0 );
						slider.setBlockIncrement ( 1 );
						slider.setShowTickMarks ( true );
						slider.setShowTickLabels ( true );
						slider.setSnapToTicks ( true );

						dialog.getDialogPane ().setContent ( slider );

						dialog.setResultConverter ( dialogButton -> {
							if ( dialogButton == rateButtonType )
							{
								return ( int ) slider.getValue ();
							}
							return null;
						} );

						Optional < Integer > result = dialog.showAndWait ();

						result.ifPresent ( value -> {
							sendPackage ( new Package <> ( Constants.RATE_TRANSLATION, new Pair <> ( translation.getID_translation (), value ) ) );
							System.out.println ( Constants.MESSAGE_RATING_SENT );
						} );
					}
				}
				else
				{
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_REQUIRED_ACCOUNT );
					alert.setHeaderText ( Constants.MESSAGE_NOT_LOGGED_IN );
					alert.setContentText ( Constants.MESSAGE_LOG_IN_TO_LEAVE_A_RATING );

					alert.showAndWait ();
				}
			} );
			return row;
		} );
	}

	/**
	 * Wysyła na serwer prośbę o tłumaczenie dla wpisanego słowa
	 */
	@FXML
	private void translate ()
	{
		String text = this.enterWordField.getText ().trim ().toLowerCase ( new Locale ( "pl", "PL" ) );
		if ( !text.isEmpty () )
		{
			int language = this.chooseLanguageBox.getSelectionModel ().getSelectedIndex ();
			sendPackage ( new Package <> ( Constants.TRANSLATE, new Pair <> ( text, language ) ) );
		}
		this.translationData.clear ();
	}

	/**
	 * Ustanawia połączenie z serwerem
	 */
	private void startClient ()
	{
		try
		{
			int port = 2000;
			String address = "localhost";
			this.client = new Socket ( address, port );
			ClientController.objOut = new ObjectOutputStream ( this.client.getOutputStream () );
			ClientController.objIn = new ObjectInputStream ( this.client.getInputStream () );

			System.out.println ( Constants.MESSAGE_CONNECTED );

			this.readMessageThread = new Thread ( () -> {

				while ( true )
				{
					try
					{
						if ( this.client != null && ClientController.objIn != null )
						{
							parsePackage ( ( Package < ? > ) ClientController.objIn.readObject () );
						}
					}
					catch ( SocketException e )
					{
						return;
					}
					catch ( IOException | ClassNotFoundException e )
					{
						e.printStackTrace ();
					}
				}

			} );
			this.readMessageThread.start ();
		}
		catch ( IOException e )
		{
			Alert alert = new Alert ( Alert.AlertType.ERROR );
			alert.setTitle ( Constants.MESSAGE_INFORMATION );
			alert.setHeaderText ( Constants.MESSAGE_NO_CONNECTION_TO_THE_SERVER );
			alert.setContentText ( Constants.MESSAGE_PLEASE_TRY_AGAIN_LATER );

			alert.showAndWait ();
			Platform.exit ();
			System.exit ( 0 );
		}
	}

	/**
	 * Zamyka połączenie z serwerem
	 */
	private void stopClient ()
	{
		try
		{
			if ( this.client != null && !this.client.isClosed () )
			{
				sendPackage ( new Package <> ( Constants.EXIT, null ) );
				this.client.close ();
				this.client = null;
			}

			System.out.println ( Constants.MESSAGE_DISCONNECTED );

			this.readMessageThread.interrupt ();

			Platform.runLater ( () -> {
				Alert alert = new Alert ( Alert.AlertType.WARNING );
				alert.setTitle ( Constants.MESSAGE_SERVER_IS_OFF );
				alert.setHeaderText ( Constants.MESSAGE_SERVER_HAS_BEEN_TURNED_OFF );
				alert.setContentText ( Constants.MESSAGE_THE_APPLICATION_CAN_NOT_WORK_WITHOUT_A_SERVER );

				alert.showAndWait ();

				Platform.exit ();
				System.exit ( 0 );
			} );
		}
		catch ( IOException e )
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Metoda wysyłająca obiekty klasy Package do serwera
	 *
	 * @param message pakiet przysyłany do serwera
	 */
	static void sendPackage ( Package < Object > message )
	{
		try
		{
			if ( objOut == null ) return;
			objOut.writeObject ( message );
			objOut.flush ();
		}
		catch ( SocketException e )
		{
			System.out.println ( e.getClass () + " | " + e.getMessage () );
		}
		catch ( IOException e )
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Metoda przetwarzająca pakiet otrzymany od serwera
	 *
	 * @param message pakiet odebrany od serwera
	 */
	private void parsePackage ( Package < ? > message )
	{
		int messageCode = message.getMessageCode ();
		Object obj = message.getData ();

		switch ( messageCode )
		{
			case Constants.EXIT:
			{
				stopClient ();
			}
			case Constants.CONNECTED:
			{
				this.account = ( Account ) obj;
				switchAdvancedTabs ();
				break;
			}
			case Constants.LOGIN:
			{
				this.account = ( Account ) obj;

				if ( this.account.isLoggedIn () )
				{
					Platform.runLater ( () -> {
						Alert alert = new Alert ( Alert.AlertType.INFORMATION );
						alert.setTitle ( Constants.MESSAGE_LOGGED_IN_SUCCESSFULLY );
						alert.setHeaderText ( null );
						alert.setContentText ( Constants.MESSAGE_HELLO + " " + this.account.getLogin () + "!" );
						alert.showAndWait ();
					} );
				}

				swichTopStatusPanel ();
				switchAdvancedTabs ();
				break;
			}
			case Constants.LOGIN_ERROR:
			{
				this.account = ( Account ) obj;
				System.out.println ( Constants.MESSAGE_LOGIN_ERROR );
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.ERROR );
					alert.setTitle ( Constants.MESSAGE_LOGIN_ERROR );
					alert.setHeaderText ( null );
					alert.setContentText ( Constants.MESSAGE_INVALID_LOGIN_CREDENTIALS_PROVIDED_PLEASE_TRY_AGAIN );
					alert.showAndWait ();
				} );

				swichTopStatusPanel ();
				switchAdvancedTabs ();
				break;
			}
			case Constants.LOGOUT:
			{
				this.account = ( Account ) obj;

				swichTopStatusPanel ();
				switchAdvancedTabs ();
				break;
			}

			case Constants.ADD_ACCOUNT:
			{
				if ( !this.account.isLoggedIn () )
				{
					Platform.runLater ( () -> {
						Alert alert = new Alert ( Alert.AlertType.INFORMATION );
						alert.setTitle ( Constants.MESSAGE_ACCOUNT_HAS_BEEN_REGISTERED );
						alert.setHeaderText ( null );
						alert.setContentText ( Constants.MESSAGE_YOU_CAN_LOG_IN );
						alert.showAndWait ();
					} );
				}
				break;
			}
			case Constants.ADD_ACCOUNT_ERROR:
			{
				if ( !this.account.isLoggedIn () )
				{
					Platform.runLater ( () -> {
						Alert alert = new Alert ( Alert.AlertType.ERROR );
						alert.setTitle ( Constants.MESSAGE_ERROR_REGISTERING_ACCOUNT );
						alert.setHeaderText ( null );
						alert.setContentText ( Constants.MESSAGE_LOGIN_IS_ALREADY_TAKEN );
						alert.showAndWait ();
					} );
				}
				break;
			}
			case Constants.RATE_TRANSLATION:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_TRANSLATION_RATING );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				translate ();
				break;
			}
			case Constants.RATE_TRANSLATION_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_TRANSLATION_RATING );
					alert.setHeaderText ( text );
					alert.setContentText ( Constants.MESSAGE_TRY_TO_LOG_IN );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.ADD_TRANSLATION_PROPOSAL:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_TRANSLATION_PROPOSAL );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.ADD_TRANSLATION_PROPOSAL_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_TRANSLATION_PROPOSAL );
					alert.setHeaderText ( text );
					alert.setContentText ( Constants.MESSAGE_SUCH_A_TRANSLATION_CAN_ALREADY_EXIST );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.ADD_TRANSLATION:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_ADDING_TRANSLATION );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.ADD_TRANSLATION_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_ADDING_TRANSLATION );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.UPDATE_TRANSLATION:
			{
				System.out.println ( Constants.MESSAGE_UPDATED_TRANSLATION );
				break;
			}
			case Constants.UPDATE_TRANSLATION_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_UPDATING_TRANSLATION );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.SET_ACCOUNT_PERMISSIONS:
			{
				System.out.println ( ( String ) obj );
				break;
			}
			case Constants.SET_ACCOUNT_PERMISSIONS_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_CHANGE_OF_PERMISSIONS );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.DELETE_TRANSLATION_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_DELETING_TRANSLATION );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.CONFIRM_TRANSLATION_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_CONFIRMATION_OF_TRANSLATION );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}
			case Constants.DELETE_ACCOUNT_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.INFORMATION );
					alert.setTitle ( Constants.MESSAGE_DELETING_ACCOUNT );
					alert.setHeaderText ( text );
					alert.setContentText ( null );
					alert.showAndWait ();
				} );
				break;
			}

			case Constants.TRANSLATE:
			{
				List < ? > translationPackageList = ( ArrayList < ? > ) obj;

				final boolean[] dataCleared = { false };
				Platform.runLater ( () -> translationPackageList.forEach ( item -> {
					if ( !dataCleared[ 0 ] )
					{
						this.translationData.clear ();
						dataCleared[ 0 ] = true;
					}
					TranslationPackage tmp = ( TranslationPackage ) item;
					this.translationData.add ( new TranslationModel ( tmp.getID_translation (), tmp.getWord1 (), tmp.isConfirmed (), tmp.getAvg_rating () ) );
				} ) );

				break;
			}
			case Constants.GET_TRANSLATIONS:
			{
				List < ? > translationPackageList = ( ArrayList < ? > ) obj;

				WordManagementController.setTranslationData ( translationPackageList );
				break;
			}
			case Constants.GET_TRANSLATIONS_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.WARNING );
					alert.setTitle ( Constants.MESSAGE_ERROR );
					alert.setHeaderText ( Constants.MESSAGE_ACCOUNT_PERMISSIONS_HAVE_BEEN_CHANGED );
					alert.setContentText ( text );

					alert.showAndWait ();

					sendPackage ( new Package <> ( Constants.LOGOUT, Constants.MESSAGE_LOGGING_OUT ) );
				} );
				break;
			}
			case Constants.GET_ACCOUNTS:
			{
				List < ? > accountPackageList = ( ArrayList < ? > ) obj;

				AccountManagementController.setAccountData ( accountPackageList );
				break;
			}
			case Constants.GET_ACCOUNTS_ERROR:
			{
				String text = ( String ) obj;
				Platform.runLater ( () -> {
					Alert alert = new Alert ( Alert.AlertType.WARNING );
					alert.setTitle ( Constants.MESSAGE_ERROR );
					alert.setHeaderText ( Constants.MESSAGE_ACCOUNT_PERMISSIONS_HAVE_BEEN_CHANGED );
					alert.setContentText ( text );

					alert.showAndWait ();

					sendPackage ( new Package <> ( Constants.LOGOUT, Constants.MESSAGE_LOGGING_OUT ) );
				} );
				break;
			}
		}

	}

	/**
	 * Przełączanie górnego panelu
	 */
	private void swichTopStatusPanel ()
	{
		Platform.runLater ( () -> {
			if ( this.account.isLoggedIn () )
			{
				this.registerButton.setVisible ( false );
				this.loginLogoutButton.setText ( Constants.MESSAGE_LOGOUT );
				this.userLoginLabel.setText ( Constants.MESSAGE_HELLO + ": " + this.account.getLogin () );
			}
			else
			{
				this.registerButton.setVisible ( true );
				this.loginLogoutButton.setText ( Constants.MESSAGE_LOGIN );
				this.userLoginLabel.setText ( this.account.getLogin () );
			}
		} );
	}

	/**
	 * Przełączanie aktywności zakładek aplikacji (robią się aktywne w zależności od uprawnień konta)
	 */
	private void switchAdvancedTabs ()
	{
		Platform.runLater ( () -> {
			if ( this.account == null ) return;
			if ( this.account.isAdmin () )
			{
				this.wordManagementTab.setDisable ( false );
				this.accountManagementTab.setDisable ( false );
				this.translationProposalPane.setVisible ( true );
			}
			else if ( this.account.isModerator () )
			{
				this.wordManagementTab.setDisable ( false );
				this.accountManagementTab.setDisable ( true );
				this.translationProposalPane.setVisible ( true );
			}
			else if ( this.account.isLoggedIn () )
			{
				this.wordManagementTab.setDisable ( true );
				this.accountManagementTab.setDisable ( true );
				this.translationProposalPane.setVisible ( true );
			}
			else
			{
				this.wordManagementTab.setDisable ( true );
				this.accountManagementTab.setDisable ( true );
				this.translationProposalPane.setVisible ( false );
			}

			this.tabPane.getSelectionModel ().select ( 0 );
		} );
	}

	/**
	 * Wyświetla okno dialogowe do logowania
	 */
	@FXML
	private void showLoginAlert ()
	{
		if ( !this.account.isLoggedIn () )
		{
			Dialog < Pair < String, String > > dialog = new Dialog <> ();
			dialog.setTitle ( Constants.MESSAGE_LOGIN_PANEL );
			dialog.setHeaderText ( Constants.MESSAGE_LOGIN );

			ButtonType loginButtonType = new ButtonType ( Constants.MESSAGE_LOGIN_TEXT, ButtonBar.ButtonData.OK_DONE );
			dialog.getDialogPane ().getButtonTypes ().addAll ( loginButtonType, ButtonType.CANCEL );

			GridPane grid = new GridPane ();
			grid.setHgap ( 10 );
			grid.setVgap ( 10 );
			grid.setPadding ( new Insets ( 20, 150, 10, 10 ) );

			TextField username = new TextField ();
			username.setPromptText ( Constants.MESSAGE_LOGIN_TEXT );
			PasswordField password = new PasswordField ();
			password.setPromptText ( Constants.MESSAGE_PASSWORD_TEXT );

			grid.add ( new Label ( Constants.MESSAGE_LOGIN_TEXT + ": " ), 0, 0 );
			grid.add ( username, 1, 0 );
			grid.add ( new Label ( Constants.MESSAGE_PASSWORD_TEXT + ": " ), 0, 1 );
			grid.add ( password, 1, 1 );

			Node loginButton = dialog.getDialogPane ().lookupButton ( loginButtonType );
			loginButton.setDisable ( true );

			password.textProperty ().addListener ( ( observable, oldValue, newValue ) -> loginButton.setDisable ( username.getText ().trim ().isEmpty () || newValue.trim ().isEmpty () ) );

			username.textProperty ().addListener ( ( ( observable, oldValue, newValue ) -> loginButton.setDisable ( password.getText ().trim ().isEmpty () || newValue.trim ().isEmpty () ) ) );

			dialog.getDialogPane ().setContent ( grid );

			Platform.runLater ( username::requestFocus );

			dialog.setResultConverter ( dialogButton -> {
				if ( dialogButton == loginButtonType )
				{
					return new Pair <> ( username.getText ().trim (), password.getText ().trim () );
				}

				return null;
			} );

			Optional < Pair < String, String > > result = dialog.showAndWait ();

			result.ifPresent ( usernamePassword -> sendPackage ( new Package <> ( Constants.LOGIN, usernamePassword ) ) );
		}
		else
		{
			sendPackage ( new Package <> ( Constants.LOGOUT, Constants.MESSAGE_LOGGING_OUT ) );
		}
	}

	/**
	 * Wyświetla okno dialogowe do rejestracji nowego konta
	 */
	@FXML
	private void showRegistrationAlert ()
	{
		Dialog < Pair < String, String > > dialog = new Dialog <> ();
		dialog.setTitle ( Constants.MESSAGE_REGISTRATION_PANEL );
		dialog.setHeaderText ( Constants.MESSAGE_REGISTRATION );

		ButtonType registrationButtonType = new ButtonType ( Constants.MESSAGE_REGISTRATION, ButtonBar.ButtonData.OK_DONE );
		dialog.getDialogPane ().getButtonTypes ().addAll ( registrationButtonType, ButtonType.CANCEL );

		GridPane grid = new GridPane ();
		grid.setHgap ( 10 );
		grid.setVgap ( 10 );
		grid.setPadding ( new Insets ( 20, 150, 10, 10 ) );

		TextField username = new TextField ();
		username.setPromptText ( Constants.MESSAGE_LOGIN_TEXT );
		PasswordField password = new PasswordField ();
		password.setPromptText ( Constants.MESSAGE_PASSWORD_TEXT );

		grid.add ( new Label ( Constants.MESSAGE_LOGIN_TEXT + ": " ), 0, 0 );
		grid.add ( username, 1, 0 );
		grid.add ( new Label ( Constants.MESSAGE_PASSWORD_TEXT + ": " ), 0, 1 );
		grid.add ( password, 1, 1 );

		Node registrationButton = dialog.getDialogPane ().lookupButton ( registrationButtonType );
		registrationButton.setDisable ( true );

		username.textProperty ().addListener ( ( observable, oldValue, newValue ) -> registrationButton.setDisable ( password.getText ().trim ().isEmpty () || newValue.trim ().isEmpty () ) );

		password.textProperty ().addListener ( ( observable, oldValue, newValue ) -> registrationButton.setDisable ( username.getText ().trim ().isEmpty () || newValue.trim ().isEmpty () ) );

		dialog.getDialogPane ().setContent ( grid );

		Platform.runLater ( username::requestFocus );

		dialog.setResultConverter ( dialogButton -> {
			if ( dialogButton == registrationButtonType )
			{
				return new Pair <> ( username.getText ().trim(), password.getText ().trim() );
			}

			return null;
		} );

		Optional < Pair < String, String > > result = dialog.showAndWait ();

		result.ifPresent ( usernamePassword -> sendPackage ( new Package <> ( Constants.ADD_ACCOUNT, usernamePassword ) ) );
	}

	/**
	 * Dodawanie propozycji tłumaczenia
	 */
	@FXML
	private void addTranslationProposal ()
	{
		String word1 = this.translationProposalPolishTextField.getText ().trim ().toLowerCase ( new Locale ( "pl", "PL" ) );
		String word2 = this.translationProposalEnglishTextField.getText ().trim ().toLowerCase ( new Locale ( "pl", "PL" ) );

		if ( !word1.isEmpty () && !word2.isEmpty () )
		{
			sendPackage ( new Package <> ( Constants.ADD_TRANSLATION_PROPOSAL, new Pair <> ( word1, word2 ) ) );
			this.translationProposalPolishTextField.clear ();
			this.translationProposalEnglishTextField.clear ();
		}
	}

	/**
	 * Dodawanie propozycji płumaczenia po wciśnięciu przycisku ENTER
	 *
	 * @param keyEvent event dla przycisku
	 */
	@FXML
	private void onKeyReleasedAddTranslationProposal ( KeyEvent keyEvent )
	{
		if ( keyEvent.getCode () == KeyCode.ENTER )
		{
			addTranslationProposal ();
		}
	}
}

