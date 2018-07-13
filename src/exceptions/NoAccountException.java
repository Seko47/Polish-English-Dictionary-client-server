package exceptions;

import other.Constants;

/**
 * @author Rafał Sekular
 */
public class NoAccountException extends Exception
{
	public NoAccountException ()
	{
		super ( Constants.MESSAGE_NO_ACCOUNT_EXCEPTION );
	}
}
