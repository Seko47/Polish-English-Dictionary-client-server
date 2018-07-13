package exceptions;

import other.Constants;

/**
 * @author Rafał Sekular
 */
public class AccountAlreadyExistsException extends Exception
{
	public AccountAlreadyExistsException ()
	{
		super ( Constants.MESSAGE_ACCOUNT_ALREADY_EXISTS_EXCEPTION );
	}
}
