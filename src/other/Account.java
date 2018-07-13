package other;

import exceptions.NoAccountException;
import server.Database;

import java.io.Serializable;

/**
 * @author Rafał Sekular
 */
public class Account implements Serializable
{
	/**
	 * Identyfikator konta
	 */
	private int ID_account;
	/**
	 * Login do konta
	 */
	private String login;
	/**
	 * Hasło do konta
	 */
	private String password;
	/**
	 * Typ konta (0 = nie zalogowany, 1 = zalogowany, 2 = moderator, 3 = administrator)
	 */
	private int ID_account_type;

	/**
	 * Domyślny konstruktor
	 */
	public Account ()
	{
		this.ID_account = -1;
		this.login = Constants.MESSAGE_NOT_LOGGED_IN;
		this.password = "";
		this.ID_account_type = 0;
	}

	/**
	 * Konstruktor
	 *
	 * @param login    login do konta
	 * @param password hasło do konta
	 * @throws NoAccountException jeżeli nie ma takiego konta
	 */
	public Account ( String login, String password ) throws NoAccountException
	{
		if ( !Database.isAccount ( login, password ) )
		{
			throw new NoAccountException ();
		}

		Account tmp = Database.getAccount ( login, password );
		if ( tmp == null )
		{
			throw new NoAccountException ();
		}

		this.ID_account = tmp.ID_account;
		this.login = tmp.login;
		this.password = tmp.password;
		this.ID_account_type = tmp.ID_account_type;
	}

	/**
	 * Konstruktor
	 *
	 * @param ID_account      identyfikator konta
	 * @param login           login do konta
	 * @param password        hasło do konta
	 * @param ID_account_type identyfikator typu konta
	 */
	public Account ( int ID_account, String login, String password, int ID_account_type )
	{
		this.ID_account = ID_account;
		this.login = login;
		this.password = password;
		this.ID_account_type = ID_account_type;
	}

	/**
	 * @return identyfikator konta
	 */
	public int getID_account ()
	{
		return ID_account;
	}

	/**
	 * @return login do konta
	 */
	public String getLogin ()
	{
		return login;
	}

	/**
	 * @return hasło do konta
	 */
	public String getPassword ()
	{
		return password;
	}

	/**
	 * @return true - jeśli konto ma uprawnienia moderatora, jeśli nie - false
	 */
	public boolean isModerator ()
	{
		return ID_account_type == 2;
	}

	/**
	 * @return true - jeśli konto ma uprawnienia administratora, jeśli nie - false
	 */
	public boolean isAdmin ()
	{
		return ID_account_type == 3;
	}

	/**
	 * @return true - jeśli konto jest zalogowane, jeśli nie - false
	 */
	public boolean isLoggedIn ()
	{
		return ID_account_type > 0;
	}
}

