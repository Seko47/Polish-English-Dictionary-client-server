package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import packages.AccountPackage;

import java.io.Serializable;

/**
 * Informacje, które mogą zostać użyte w tabeli
 *
 * @author Rafał Sekular
 */
public class AccountModel implements Serializable
{
	/**
	 * Identyfikator konta
	 */
	private final IntegerProperty ID_account;
	/**
	 * Login do konta
	 */
	private final StringProperty login;
	/**
	 * Hasło do konta
	 */
	private final StringProperty password;
	/**
	 * Typ konta
	 */
	private final StringProperty accountType;

	/**
	 * konstruktor konwertujący obiekt AccountPackage na AccountModel
	 *
	 * @param accountPackage informacje o koncie w obiekcie AccountPackage (odebrane z serwera)
	 */
	public AccountModel (AccountPackage accountPackage)
	{
		this.ID_account = new SimpleIntegerProperty (accountPackage.getID_account ());
		this.login = new SimpleStringProperty (accountPackage.getLogin ());
		this.password = new SimpleStringProperty (accountPackage.getPassword ());
		this.accountType = new SimpleStringProperty (accountPackage.getAccountType ());
	}

	/**
	 * @return identyfikator konta
	 */
	public int getID_account ()
	{
		return ID_account.get ();
	}

	/**
	 * @return identyfikator konta jako obiekt IntegerProperty
	 */
	public IntegerProperty ID_accountProperty ()
	{
		return ID_account;
	}

	/**
	 * @return login do konta jako obiekt StringProperty
	 */
	public StringProperty loginProperty ()
	{
		return login;
	}

	/**
	 * @return Hasło do konta jako obiekt StringProperty
	 */
	public StringProperty passwordProperty ()
	{
		return password;
	}

	/**
	 * @return Typ konta jako obiekt IntegerProperty
	 */
	public StringProperty accountTypeProperty ()
	{
		return accountType;
	}
}
