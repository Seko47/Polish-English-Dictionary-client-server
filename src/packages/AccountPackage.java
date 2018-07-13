package packages;

import java.io.Serializable;

/**
 * Klasa służy do przesyłania przez Socket
 *
 * @author Rafał Sekular
 */
public class AccountPackage implements Serializable
{
	/**
	 * identyfikator konta
	 */
	private final int ID_account;
	/**
	 * login do konta
	 */
	private final String login;
	/**
	 * hasło do konta
	 */
	private final String password;
	/**
	 * typ konta
	 */
	private final String accountType;

	/**
	 * Konstruktor
	 *
	 * @param ID_account  identyfikator konta
	 * @param login       login
	 * @param password    hasło
	 * @param accountType typ konta (słownie)
	 */
	public AccountPackage ( int ID_account, String login, String password, String accountType )
	{
		this.ID_account = ID_account;
		this.login = login;
		this.password = password;
		this.accountType = accountType;
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
	 * @return typ konta
	 */
	public String getAccountType ()
	{
		return accountType;
	}
}
