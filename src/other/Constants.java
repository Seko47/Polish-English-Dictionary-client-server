package other;

/**
 * Klasa ze stałymi
 *
 * @author Rafał Sekular
 */
public class Constants
{
	//Kody wiadomości
	/**
	 * kod wyłączenia aplikacji
	 */
	public static final int EXIT = 0;
	/**
	 * kod ustanowienia połączenia
	 */
	public static final int CONNECTED = 1;
	/**
	 * kod logowania
	 */
	public static final int LOGIN = 2;
	/**
	 * kod wylogowania
	 */
	public static final int LOGOUT = 3;

	/**
	 * kod pobrania listy tłumaczeń
	 */
	public static final int GET_TRANSLATIONS = 10;
	/**
	 * kod rządający tłumaczenie słowa
	 */
	public static final int TRANSLATE = 11;
	/**
	 * kod dodawania tłumaczenia
	 */
	public static final int ADD_TRANSLATION = 12;
	/**
	 * kod usuwania tłumaczenia
	 */
	public static final int DELETE_TRANSLATION = 13;
	/**
	 * kod aktualizacji tłumaczenia
	 */
	public static final int UPDATE_TRANSLATION = 14;
	/**
	 * kod wysłania oceny tłumaczenia
	 */
	public static final int RATE_TRANSLATION = 15;
	/**
	 * kod dodania propozycji tłumaczenia
	 */
	public static final int ADD_TRANSLATION_PROPOSAL = 16;
	/**
	 * kod zmieniający status potwierdzenia tłumaczenia
	 */
	public static final int CONFIRM_TRANSLATION = 17;

	/**
	 * kod pobrania listy kont
	 */
	public static final int GET_ACCOUNTS = 100;
	/**
	 * kod ustawienia nowych uprawnień dla konta
	 */
	public static final int SET_ACCOUNT_PERMISSIONS = 101;
	/**
	 * kod dodania nowego konta (rejestracja)
	 */
	public static final int ADD_ACCOUNT = 102;
	/**
	 * kod usuwania konta
	 */
	public static final int DELETE_ACCOUNT = 103;

	/**
	 * kod błędu logowania
	 */
	public static final int LOGIN_ERROR = 200;
	/**
	 * kod błędu dodawania tłumaczenia
	 */
	public static final int ADD_TRANSLATION_ERROR = 201;
	/**
	 * kod błędu usuwania tłumaczenia
	 */
	public static final int DELETE_TRANSLATION_ERROR = 202;
	/**
	 * kod błędu aktualizacji tłumaczenia
	 */
	public static final int UPDATE_TRANSLATION_ERROR = 203;
	/**
	 * kod błędu zmiany uprawnień dla konta
	 */
	public static final int SET_ACCOUNT_PERMISSIONS_ERROR = 204;
	/**
	 * kod błędu dodawania konta (rejestracja)
	 */
	public static final int ADD_ACCOUNT_ERROR = 205;
	/**
	 * kod błędu wysłania oceny tłumaczenia
	 */
	public static final int RATE_TRANSLATION_ERROR = 206;
	/**
	 * kod błędu dodawania propozycji tłumaczenia
	 */
	public static final int ADD_TRANSLATION_PROPOSAL_ERROR = 207;
	/**
	 * kod błędu zmiany potwierdzenia tłumaczenia
	 */
	public static final int CONFIRM_TRANSLATION_ERROR = 208;
	/**
	 * kod błędu usuwania konta
	 */
	public static final int DELETE_ACCOUNT_ERROR = 209;
	/**
	 * kod błędu pobrania listy tłumaczeń
	 */
	public static final int GET_TRANSLATIONS_ERROR = 210;
	/**
	 * kod błędu pobrania listy kont
	 */
	public static final int GET_ACCOUNTS_ERROR = 211;


	/**
	 * nazwa aplikacji serwera
	 */
	public static final String MESSAGE_DICTIONARY_SERVER_NAME = "Słownik serwer";
	/**
	 * nazwa aplikacji klienta
	 */
	public static final String MESSAGE_DICTIONARY_CLIENT_NAME = "Słownik";
	/**
	 * komunikat o braku zalogowania
	 */
	public static final String MESSAGE_NOT_LOGGED_IN = "Nie zalogowany";

	/**
	 * komunikat o akceptacji klienta
	 */
	public static final String MESSAGE_CLIENT_ACCEPTED = "Client accepted";
	/**
	 * komunikat informujący o zatrzymaniu wątku akceptacji klientów
	 */
	public static final String MESSAGE_ACCEPT_CLIENT_THREAD_IS_STOPED = "AcceptClientThread is stopped";
	/**
	 * komunikat o uruchomieniu serwera
	 */
	public static final String MESSAGE_SERVER_IS_ON = "Server is on";
	/**
	 * komunikat o stanie serwera
	 */
	public static final String MESSAGE_SERVER_IS_OFF = "Serwer wyłączony";
	/**
	 * komunikat o wyłączeniu serwera
	 */
	public static final String MESSAGE_SERVER_HAS_BEEN_TURNED_OFF = "Serwer został wyłączony";
	/**
	 * komunikat o błędzie serwera
	 */
	public static final String MESSAGE_SERVER_ERROR = "Błąd serwera";
	/**
	 * komunikat o zajętym porcie serwera
	 */
	public static final String MESSAGE_THE_PORT_IS_BUSY = "Port zajęty";
	/**
	 * komunikat informujący o zamykaniu aplikacji
	 */
	public static final String MESSAGE_THE_APPLICATION_WILL_BY_CLOSED = "Aplikacja zostanie zamknięta";
	/**
	 * komunikat o błędzie
	 */
	public static final String MESSAGE_ERROR = "Błąd";

	/**
	 * Nazwa przycisku gdy serwer jest włączony
	 */
	public static final String MESSAGE_STOP = "STOP";
	/**
	 * Nazwa przycisku gdy serwer jest wyłączony
	 */
	public static final String MESSAGE_START = "START";

	/**
	 * komunikat informujący o zatrzymaniu transmisji
	 */
	public static final String MESSAGE_TRANSMISSION_STOPED = "Transmission stopped";
	/**
	 * informacja o połączeniu
	 */
	public static final String MESSAGE_CONNECTED = "Connected";
	/**
	 * informacja o rozłączeniu
	 */
	public static final String MESSAGE_DISCONNECTED = "Disconnected";

	/**
	 * komunikat o dodaniu tłumaczenia
	 */
	public static final String MESSAGE_TRANSLATION_ADDED = "Dodano tłumaczenie";
	/**
	 * komunikat o braku możliwości dodania tłumaczenia
	 */
	public static final String MESSAGE_CAN_NOT_ADD_TRANSLATION = "Nie można dodać tłumaczenia";
	/**
	 * komunikat o aktualizacji tłumaczenia
	 */
	public static final String MESSAGE_TRANSLATION_HAS_BEEN_UPDATED = "Tłumaczenie zostało zaktualizowane";
	/**
	 * komunikat o braku możliwości aktualizacji tłumaczenia
	 */
	public static final String MESSAGE_CAN_NOT_UPDATE_TRANSLATION = "Nie można zaktualizować tłumaczenia";
	/**
	 * komunikat informujący o zmianie uprawnień dla konta
	 */
	public static final String MESSAGE_ACCOUNT_PERMISSIONS_HAVE_BEEN_CHANGED = "Uprawnienia dla konta zostały zmienione";
	/**
	 * komunikat informujący o braku możliwości zmiany uprawnień
	 */
	public static final String MESSAGE_ACCOUNT_PERMISSIONS_CAN_NOT_BE_CHANGED = "Nie można zmienić uprawnień dla konta";
	/**
	 * komunikat o dodaniu nowego konta
	 */
	public static final String MESSAGE_NEW_ACCOUNT_ADDED = "Dodano nowe konto";
	/**
	 * komunikat o braku możliwości dodania nowego konta
	 */
	public static final String MESSAGE_CAN_NOT_ADD_A_NEW_ACCOUNT = "Nie można dodać nowego konta";
	/**
	 * komunikat informujący o dodaniu oceny
	 */
	public static final String MESSAGE_A_RATING_HAS_BEEN_ADDED = "Dodano ocenę";
	/**
	 * komunikat informujący o aktualizacji poprzedniej oceny
	 */
	public static final String MESSAGE_UPDATED_PREVIOUS_RATING = "Zaktualizowano poprzednią ocenę";
	/**
	 * komunikat o braku możliwości dodania oceny
	 */
	public static final String MESSAGE_CAN_NOT_ADD_RATING = "Nie można dodać oceny";
	/**
	 * komunikat informujący o dodaniu propozycji tłumaczenia
	 */
	public static final String MESSAGE_A_TRANSLATION_PROPOSAL_HAS_BEEN_ADDED = "Dodano propozycję tłumaczenia";
	/**
	 * komunikat o braku możliwości dodania propozycji tłumaczenia
	 */
	public static final String MESSAGE_CAN_NOT_ADD_TRANSLATION_PROPOSAL = "Nie można dodać propozycji tłumaczenia";
	/**
	 * komunikat o zmianie stanu potwierdzenia tłumaczenia
	 */
	public static final String MESSAGE_CONFIRMATION_MADE = "Wykonano potwierdzenie";
	/**
	 * komunikat o braku możliwości zmiany stanu potwierdzenia tłumaczenia
	 */
	public static final String MESSAGE_CAN_NOT_CONFIRM = "Nie można wykonać potwierdzenia";
	/**
	 * komunikat o usunięciu tłumaczenia
	 */
	public static final String MESSAGE_TRANSLATION_HAS_BEEN_REMOVED = "Tłumaczenie zostało usunięte";
	/**
	 * informacja o błędzie podczas usuwania tłumaczenia
	 */
	public static final String MESSAGE_AN_ERROR_OCCURRED_WHILE_DELETING_THE_TRANSLATION = "Wystąpił błąd podczas usuwania tłumaczenia";
	/**
	 * informacja o błędzie podczas usuwania konta
	 */
	public static final String MESSAGE_FAILED_TO_DELETE_ACCOUNT = "Nie udało się usunąć konta";
	/**
	 * informacja o zatrzymaniu wątku klienta
	 */
	public static final String MESSAGE_CLIENT_THREAD_IS_STOPPED = "ClientThread is stopped";

	/**
	 * informacja o braku konta
	 */
	public static final String MESSAGE_NO_ACCOUNT_EXCEPTION = "Brak konta";
	/**
	 * informacja o tym, że takie konto już istnieje (rejestracja)
	 */
	public static final String MESSAGE_ACCOUNT_ALREADY_EXISTS_EXCEPTION = "Konto już istnieje";

	/**
	 * informacja Wszystkie (dla combobox)
	 */
	public static final String MESSAGE_ALL = "Wszystkie";
	/**
	 * informacja Potwierdzone (dla combobox)
	 */
	public static final String MESSAGE_CONFIRMED = "Potwierdzone";
	/**
	 * inormacja Niepotwierdzone (dla combobox)
	 */
	public static final String MESSAGE_UNCONFIRMED = "Niepotwierdzone";
	/**
	 * informacja wyświetlana podczas najechania na tabelę z tłumaczeniami
	 */
	public static final String MESSAGE_TRANSLATION_TABLE_TOOLTIP = "Kliknij dwukrotnie lewym przyciskiem myszy na słowie, aby edytować.\n" +
			"Kliknij dwukrotnie prawym przyciskiem myszy aby usunąć\n" +
			"Kliknij dwukrotnie lewym przyciskiem myszy w obszarze kolumny Potwierdzone aby zmienić stan potwierdzenia";

	/**
	 * informacja Polski (dla combobox)
	 */
	public static final String MESSAGE_POLISH = "Polski";
	/**
	 * informacja Angielski (dla combobox)
	 */
	public static final String MESSAGE_ENGLISH = "Angielski";
	/**
	 * komunikat o wyborze języka
	 */
	public static final String MESSAGE_CHOOSE_LANGUAGE_BOX_TOOLTIP = "Wybierz język";
	/**
	 * inforamcja wyświetlana podczas najechania na tabelę z tłumaczeniem
	 */
	public static final String MESSAGE_TRANSLATION_RESULT_TABLE_TOOLTIP = "Kliknij dwukrotnie aby ocenić tłumaczenie";
	/**
	 * komunikat o ocenie tłumaczenia
	 */
	public static final String MESSAGE_TRANSLATION_RATING = "Ocena tłumaczenia";
	/**
	 * komunikat o wyborze porządanej oceny
	 */
	public static final String MESSAGE_SELECT_THE_RATING_FOR_THE_SELECTED_TRANSLATION = "Wybierz ocenę dla wybranego tłumaczenia";
	/**
	 * komunikat o zostawieniu oceny
	 */
	public static final String MESSAGE_RATE = "Oceń";
	/**
	 * komunikat o wysłaniu oceny
	 */
	public static final String MESSAGE_RATING_SENT = "Wysłano ocenę";
	/**
	 * informacja o tym, że do operacji potrzebne jset konto
	 */
	public static final String MESSAGE_REQUIRED_ACCOUNT = "Wymagane konto";
	/**
	 * informacja o wymaganym zalogowaniu
	 */
	public static final String MESSAGE_LOG_IN_TO_LEAVE_A_RATING = "Zaloguj się, aby zostawić ocenę";
	/**
	 * komunikat Informacja
	 */
	public static final String MESSAGE_INFORMATION = "Informacja";
	/**
	 * komunikat o braku połączenia z serwerem
	 */
	public static final String MESSAGE_NO_CONNECTION_TO_THE_SERVER = "Brak połączenia z serwerem";
	/**
	 * komunikat o spróbowaniu ponownie później
	 */
	public static final String MESSAGE_PLEASE_TRY_AGAIN_LATER = "Spróbuj ponownie później";
	/**
	 * informacja, że klient nie może działać bez serwera
	 */
	public static final String MESSAGE_THE_APPLICATION_CAN_NOT_WORK_WITHOUT_A_SERVER = "Aplikacja nie może działać bez serwera";

	/**
	 * komunikat o pomyślnym zalogowaniu
	 */
	public static final String MESSAGE_LOGGED_IN_SUCCESSFULLY = "Zalogowano pomyślnie";
	/**
	 * komunikat Witaj
	 */
	public static final String MESSAGE_HELLO = "Witaj";
	/**
	 * komunikat o błędzie logowania
	 */
	public static final String MESSAGE_LOGIN_ERROR = "Błąd logowania";
	/**
	 * komunikat o błędnych danych logowania
	 */
	public static final String MESSAGE_INVALID_LOGIN_CREDENTIALS_PROVIDED_PLEASE_TRY_AGAIN = "Podano błędne dane logowania. Spróbuj ponownie";
	/**
	 * informacja o rejestracji konta
	 */
	public static final String MESSAGE_ACCOUNT_HAS_BEEN_REGISTERED = "Zarejestrowano konto";
	/**
	 * informacja o możliwości logowania
	 */
	public static final String MESSAGE_YOU_CAN_LOG_IN = "Możesz się zalogować!";
	/**
	 * komunikat o błędzie podczas rejestracji konta
	 */
	public static final String MESSAGE_ERROR_REGISTERING_ACCOUNT = "Błąd podczas rejestracji konta";
	/**
	 * informacja o tym, że login jest zajęty
	 */
	public static final String MESSAGE_LOGIN_IS_ALREADY_TAKEN = "Podany login jest już zajęty. Spróbuj podać inny";
	/**
	 * komunikat Spróbuj się zalogować
	 */
	public static final String MESSAGE_TRY_TO_LOG_IN = "Spróbuj się zalogować";
	/**
	 * komunikat o propozycji tłumaczenia
	 */
	public static final String MESSAGE_TRANSLATION_PROPOSAL = "Propozycja tłumaczenia";
	/**
	 * komunikat o istniejącym tłumaczeniu
	 */
	public static final String MESSAGE_SUCH_A_TRANSLATION_CAN_ALREADY_EXIST = "Takie tłumaczenie może już istnieć";
	/**
	 * informacja o dodawaniu tłumaczenia
	 */
	public static final String MESSAGE_ADDING_TRANSLATION = "Dodawanie tłumaczenia";
	/**
	 * informacja o zaktualizowaniu tłumaczenia
	 */
	public static final String MESSAGE_UPDATED_TRANSLATION = "Zaktualizowano tłumaczenie";
	/**
	 * informacja o aktualizacji tłumaczenia
	 */
	public static final String MESSAGE_UPDATING_TRANSLATION = "Aktualizowanie tłumaczenia";
	/**
	 * informacja o zmianie uprawnień
	 */
	public static final String MESSAGE_CHANGE_OF_PERMISSIONS = "Zmiana uprawnień";
	/**
	 * informacja o usuwaniu tłumaczenia
	 */
	public static final String MESSAGE_DELETING_TRANSLATION = "Usuwanie tłumaczenia";
	/**
	 * informacja o potwierdzeniu tłumaczenia
	 */
	public static final String MESSAGE_CONFIRMATION_OF_TRANSLATION = "Potwierdzenie tłumaczenia";
	/**
	 * informacja o usuwaniu konta
	 */
	public static final String MESSAGE_DELETING_ACCOUNT = "Usuwanie konta";

	/**
	 * Nazwa przycisku Wyloguj
	 */
	public static final String MESSAGE_LOGOUT = "Wyloguj";
	/**
	 * Nazwa przycisku Logowanie
	 */
	public static final String MESSAGE_LOGIN = "Logowanie";
	/**
	 * Nazwa panelu logowania
	 */
	public static final String MESSAGE_LOGIN_PANEL = "Panel logowania";
	/**
	 * nazwa pola login
	 */
	public static final String MESSAGE_LOGIN_TEXT = "Login";
	/**
	 * nazwa pola hasło
	 */
	public static final String MESSAGE_PASSWORD_TEXT = "Hasło";
	/**
	 * informacja o wylogowywaniu
	 */
	public static final String MESSAGE_LOGGING_OUT = "Wylogowywanie";

	/**
	 * nazwa panelu rejestracji
	 */
	public static final String MESSAGE_REGISTRATION_PANEL = "Panel rejestracji";
	/**
	 * nazwa przycisku rejestracji
	 */
	public static final String MESSAGE_REGISTRATION = "Rejestracja";

	/**
	 * informacja Uzytkownicy
	 */
	public static final String MESSAGE_USERS = "Użytkownicy";
	/**
	 * informacja Uzytkownik
	 */
	public static final String MESSAGE_USER = "Użytkownik";
	/**
	 * informacja Moderatorzy
	 */
	public static final String MESSAGE_MODERATORS = "Moderatorzy";
	/**
	 * informacja Moderator
	 */
	public static final String MESSAGE_MODERATOR = "Moderator";
	/**
	 * informacja Administratorzy
	 */
	public static final String MESSAGE_ADMINISTRATORS = "Administratorzy";
	/**
	 * informacja Administrator
	 */
	public static final String MESSAGE_ADMINISTRATOR = "Administrator";
	/**
	 * komunikat wyświetlający się po najechaniu na tabelę z kontami
	 */
	public static final String MESSAGE_ACCOUNT_TABLE_TOOLTIP = "Kliknij dwukrotnie lewym przyciskiem myszy aby edytować typ konta.\n" +
			"Kliknij dwukrotnie prawym przyciskiem myszy aby usunąć";
	/**
	 * komunikat zmiana typu konta
	 */
	public static final String MESSAGE_CHANGING_ACCOUNT_TYPE = "Zmiana typu konta";
	/**
	 * komunikat o wyborze typu konta
	 */
	public static final String MESSAGE_CHOOSE_ACCOUNT_TYPE = "Wybierz typ konta";
	/**
	 * nazwa przycisku Zapisz
	 */
	public static final String MESSAGE_SAVE = "Zapisz";

	/**
	 * informacja, że zalogowane konto nie posiada uprawnień do danej operacji
	 */
	public static final String MESSAGE_YOUR_ACCOUNT_DOES_NOT_HAVE_SUCH_PERMISSIONS = "Twoje konto nie posiada takich uprawnień!";
	/**
	 * informacja o próbie zalogowania się ponownie (prośba)
	 */
	public static final String MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN = "Wystąpił błąd spróbuj zalogować się ponownie";
}
