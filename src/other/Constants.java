package other;

/**
 * Klasa ze stałymi
 *
 * @author Rafał Sekular
 */
public class Constants
{
	private Constants ()
	{
	}

	//Kody wiadomości
	public static final int EXIT = 0;
	public static final int CONNECTED = 1;
	public static final int LOGIN = 2;
	public static final int LOGOUT = 3;

	public static final int GET_TRANSLATIONS = 10;
	public static final int TRANSLATE = 11;
	public static final int ADD_TRANSLATION = 12;
	public static final int DELETE_TRANSLATION = 13;
	public static final int UPDATE_TRANSLATION = 14;
	public static final int RATE_TRANSLATION = 15;
	public static final int ADD_TRANSLATION_PROPOSAL = 16;
	public static final int CONFIRM_TRANSLATION = 17;

	public static final int GET_ACCOUNTS = 100;
	public static final int SET_ACCOUNT_PERMISSIONS = 101;
	public static final int ADD_ACCOUNT = 102;
	public static final int DELETE_ACCOUNT = 103;

	public static final int LOGIN_ERROR = 200;
	public static final int ADD_TRANSLATION_ERROR = 201;
	public static final int DELETE_TRANSLATION_ERROR = 202;
	public static final int UPDATE_TRANSLATION_ERROR = 203;
	public static final int SET_ACCOUNT_PERMISSIONS_ERROR = 204;
	public static final int ADD_ACCOUNT_ERROR = 205;
	public static final int RATE_TRANSLATION_ERROR = 206;
	public static final int ADD_TRANSLATION_PROPOSAL_ERROR = 207;
	public static final int CONFIRM_TRANSLATION_ERROR = 208;
	public static final int DELETE_ACCOUNT_ERROR = 209;
	public static final int GET_TRANSLATIONS_ERROR = 210;
	public static final int GET_ACCOUNTS_ERROR = 211;


	public static final String MESSAGE_DICTIONARY_SERVER_NAME = "Słownik serwer";
	public static final String MESSAGE_DICTIONARY_CLIENT_NAME = "Słownik";
	public static final String MESSAGE_NOT_LOGGED_IN = "Nie zalogowany";

	public static final String MESSAGE_CLIENT_ACCEPTED = "Client accepted";
	public static final String MESSAGE_ACCEPT_CLIENT_THREAD_IS_STOPED = "AcceptClientThread is stopped";
	public static final String MESSAGE_SERVER_IS_ON = "Server is on";
	public static final String MESSAGE_SERVER_IS_OFF = "Serwer wyłączony";
	public static final String MESSAGE_SERVER_HAS_BEEN_TURNED_OFF = "Serwer został wyłączony";
	public static final String MESSAGE_SERVER_ERROR = "Błąd serwera";
	public static final String MESSAGE_THE_PORT_IS_BUSY = "Port zajęty";
	public static final String MESSAGE_THE_APPLICATION_WILL_BY_CLOSED = "Aplikacja zostanie zamknięta";
	public static final String MESSAGE_ERROR = "Błąd";

	public static final String MESSAGE_STOP = "STOP";
	public static final String MESSAGE_START = "START";

	public static final String MESSAGE_TRANSMISSION_STOPED = "Transmission stopped";
	public static final String MESSAGE_CONNECTED = "Connected";
	public static final String MESSAGE_DISCONNECTED = "Disconnected";

	public static final String MESSAGE_TRANSLATION_ADDED = "Dodano tłumaczenie";
	public static final String MESSAGE_CAN_NOT_ADD_TRANSLATION = "Nie można dodać tłumaczenia";
	public static final String MESSAGE_TRANSLATION_HAS_BEEN_UPDATED = "Tłumaczenie zostało zaktualizowane";
	public static final String MESSAGE_CAN_NOT_UPDATE_TRANSLATION = "Nie można zaktualizować tłumaczenia";
	public static final String MESSAGE_ACCOUNT_PERMISSIONS_HAVE_BEEN_CHANGED = "Uprawnienia dla konta zostały zmienione";
	public static final String MESSAGE_ACCOUNT_PERMISSIONS_CAN_NOT_BE_CHANGED = "Nie można zmienić uprawnień dla konta";
	public static final String MESSAGE_NEW_ACCOUNT_ADDED = "Dodano nowe konto";
	public static final String MESSAGE_CAN_NOT_ADD_A_NEW_ACCOUNT = "Nie można dodać nowego konta";
	public static final String MESSAGE_A_RATING_HAS_BEEN_ADDED = "Dodano ocenę";
	public static final String MESSAGE_UPDATED_PREVIOUS_RATING = "Zaktualizowano poprzednią ocenę";
	public static final String MESSAGE_CAN_NOT_ADD_RATING = "Nie można dodać oceny";
	public static final String MESSAGE_A_TRANSLATION_PROPOSAL_HAS_BEEN_ADDED = "Dodano propozycję tłumaczenia";
	public static final String MESSAGE_CAN_NOT_ADD_TRANSLATION_PROPOSAL = "Nie można dodać propozycji tłumaczenia";
	public static final String MESSAGE_CONFIRMATION_MADE = "Wykonano potwierdzenie";
	public static final String MESSAGE_CAN_NOT_CONFIRM = "Nie można wykonać potwierdzenia";
	public static final String MESSAGE_TRANSLATION_HAS_BEEN_REMOVED = "Tłumaczenie zostało usunięte";
	public static final String MESSAGE_AN_ERROR_OCCURRED_WHILE_DELETING_THE_TRANSLATION = "Wystąpił błąd podczas usuwania tłumaczenia";
	public static final String MESSAGE_FAILED_TO_DELETE_ACCOUNT = "Nie udało się usunąć konta";
	public static final String MESSAGE_CLIENT_THREAD_IS_STOPPED = "ClientThread is stopped";

	public static final String MESSAGE_NO_ACCOUNT_EXCEPTION = "Brak konta";
	public static final String MESSAGE_ACCOUNT_ALREADY_EXISTS_EXCEPTION = "Konto już istnieje";

	public static final String MESSAGE_ALL = "Wszystkie";
	public static final String MESSAGE_CONFIRMED = "Potwierdzone";
	public static final String MESSAGE_UNCONFIRMED = "Niepotwierdzone";
	public static final String MESSAGE_TRANSLATION_TABLE_TOOLTIP = "Kliknij dwukrotnie lewym przyciskiem myszy na słowie, aby edytować.\n" +
			"Kliknij dwukrotnie prawym przyciskiem myszy aby usunąć\n" +
			"Kliknij dwukrotnie lewym przyciskiem myszy w obszarze kolumny Potwierdzone aby zmienić stan potwierdzenia";

	public static final String MESSAGE_POLISH = "Polski";
	public static final String MESSAGE_ENGLISH = "Angielski";
	public static final String MESSAGE_CHOOSE_LANGUAGE_BOX_TOOLTIP = "Wybierz język";
	public static final String MESSAGE_TRANSLATION_RESULT_TABLE_TOOLTIP = "Kliknij dwukrotnie aby ocenić tłumaczenie";
	public static final String MESSAGE_TRANSLATION_RATING = "Ocena tłumaczenia";
	public static final String MESSAGE_SELECT_THE_RATING_FOR_THE_SELECTED_TRANSLATION = "Wybierz ocenę dla wybranego tłumaczenia";
	public static final String MESSAGE_RATE = "Oceń";
	public static final String MESSAGE_RATING_SENT = "Wysłano ocenę";
	public static final String MESSAGE_REQUIRED_ACCOUNT = "Wymagane konto";
	public static final String MESSAGE_LOG_IN_TO_LEAVE_A_RATING = "Zaloguj się, aby zostawić ocenę";
	public static final String MESSAGE_INFORMATION = "Informacja";
	public static final String MESSAGE_NO_CONNECTION_TO_THE_SERVER = "Brak połączenia z serwerem";
	public static final String MESSAGE_PLEASE_TRY_AGAIN_LATER = "Spróbuj ponownie później";
	public static final String MESSAGE_THE_APPLICATION_CAN_NOT_WORK_WITHOUT_A_SERVER = "Aplikacja nie może działać bez serwera";

	public static final String MESSAGE_LOGGED_IN_SUCCESSFULLY = "Zalogowano pomyślnie";
	public static final String MESSAGE_HELLO = "Witaj";
	public static final String MESSAGE_LOGIN_ERROR = "Błąd logowania";
	public static final String MESSAGE_INVALID_LOGIN_CREDENTIALS_PROVIDED_PLEASE_TRY_AGAIN = "Podano błędne dane logowania. Spróbuj ponownie";
	public static final String MESSAGE_ACCOUNT_HAS_BEEN_REGISTERED = "Zarejestrowano konto";
	public static final String MESSAGE_YOU_CAN_LOG_IN = "Możesz się zalogować!";
	public static final String MESSAGE_ERROR_REGISTERING_ACCOUNT = "Błąd podczas rejestracji konta";
	public static final String MESSAGE_LOGIN_IS_ALREADY_TAKEN = "Podany login jest już zajęty. Spróbuj podać inny";
	public static final String MESSAGE_TRY_TO_LOG_IN = "Spróbuj się zalogować";
	public static final String MESSAGE_TRANSLATION_PROPOSAL = "Propozycja tłumaczenia";
	public static final String MESSAGE_SUCH_A_TRANSLATION_CAN_ALREADY_EXIST = "Takie tłumaczenie może już istnieć";
	public static final String MESSAGE_ADDING_TRANSLATION = "Dodawanie tłumaczenia";
	public static final String MESSAGE_UPDATED_TRANSLATION = "Zaktualizowano tłumaczenie";
	public static final String MESSAGE_UPDATING_TRANSLATION = "Aktualizowanie tłumaczenia";
	public static final String MESSAGE_CHANGE_OF_PERMISSIONS = "Zmiana uprawnień";
	public static final String MESSAGE_DELETING_TRANSLATION = "Usuwanie tłumaczenia";
	public static final String MESSAGE_CONFIRMATION_OF_TRANSLATION = "Potwierdzenie tłumaczenia";
	public static final String MESSAGE_DELETING_ACCOUNT = "Usuwanie konta";

	public static final String MESSAGE_LOGOUT = "Wyloguj";
	public static final String MESSAGE_LOGIN = "Logowanie";
	public static final String MESSAGE_LOGIN_PANEL = "Panel logowania";
	public static final String MESSAGE_LOGIN_TEXT = "Login";
	public static final String MESSAGE_PASSWORD_TEXT = "Hasło";
	public static final String MESSAGE_LOGGING_OUT = "Wylogowywanie";

	public static final String MESSAGE_REGISTRATION_PANEL = "Panel rejestracji";
	public static final String MESSAGE_REGISTRATION = "Rejestracja";

	public static final String MESSAGE_USERS = "Użytkownicy";
	public static final String MESSAGE_USER = "Użytkownik";
	public static final String MESSAGE_MODERATORS = "Moderatorzy";
	public static final String MESSAGE_MODERATOR = "Moderator";
	public static final String MESSAGE_ADMINISTRATORS = "Administratorzy";
	public static final String MESSAGE_ADMINISTRATOR = "Administrator";
	public static final String MESSAGE_ACCOUNT_TABLE_TOOLTIP = "Kliknij dwukrotnie lewym przyciskiem myszy aby edytować typ konta.\n" +
			"Kliknij dwukrotnie prawym przyciskiem myszy aby usunąć";
	public static final String MESSAGE_CHANGING_ACCOUNT_TYPE = "Zmiana typu konta";
	public static final String MESSAGE_CHOOSE_ACCOUNT_TYPE = "Wybierz typ konta";
	public static final String MESSAGE_SAVE = "Zapisz";

	public static final String MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN = "Wystąpił błąd spróbuj zalogować się ponownie";
}
