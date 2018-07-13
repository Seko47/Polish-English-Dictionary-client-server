package other;

import java.io.Serializable;

/**
 * @author Rafał Sekular
 */
public class Package < T > implements Serializable
{
	/**
	 * Kod wiadomości (z klasy Constants)
	 */
	private final int messageCode;
	/**
	 * Dane
	 */
	private final T data;

	/**
	 * @param messageCode kod wiadomości
	 * @param data        dane
	 */
	public Package ( int messageCode, T data )
	{
		this.messageCode = messageCode;
		this.data = data;
	}

	/**
	 * @return kod wiadomości
	 */
	public int getMessageCode ()
	{
		return messageCode;
	}

	/**
	 * @return dane
	 */
	public T getData ()
	{
		return data;
	}
}
