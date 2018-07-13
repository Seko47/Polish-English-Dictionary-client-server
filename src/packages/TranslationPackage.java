package packages;

import java.io.Serializable;

/**
 * Klasa służy do przesyłania przez Socket
 *
 * @author Rafał Sekular
 */
public class TranslationPackage implements Serializable
{
	/**
	 * identyfikator tłumaczenia
	 */
	private final int ID_translation;
	/**
	 * pierwsze słowo
	 */
	private final String word1;
	/**
	 * drugie słowo
	 */
	private final String word2;
	/**
	 * stan potwierdzneia tłumaczenia
	 */
	private final boolean confirmed;
	/**
	 * średnia ocena tłumaczenia
	 */
	private final double avg_rating;

	/**
	 * Konstruktor
	 *
	 * @param ID_translation identyfikator tłumaczenia
	 * @param word1          pierwsze słowo (polskie)
	 * @param word2          drugie słowo (angielskie)
	 * @param confirmed      określa, czy tłumaczenie jest potwierdzone
	 * @param avg_rating     średnia ocena tłumaczenia
	 */
	public TranslationPackage ( int ID_translation, String word1, String word2, boolean confirmed, double avg_rating )
	{
		this.ID_translation = ID_translation;
		this.word1 = word1;
		this.word2 = word2;
		this.confirmed = confirmed;
		this.avg_rating = avg_rating;
	}

	/**
	 * Konstruktor
	 *
	 * @param ID_translation identyfikator tłumaczenia
	 * @param word           słowo
	 * @param confirmed      określa, czy tłumaczenie jest potwierdzone
	 * @param avg_rating     średnia ocena tłumaczenia
	 */
	public TranslationPackage ( int ID_translation, String word, boolean confirmed, double avg_rating )
	{
		this.ID_translation = ID_translation;
		this.word1 = word;
		this.confirmed = confirmed;
		this.avg_rating = avg_rating;

		this.word2 = "";
	}

	/**
	 * @return identyfikator tłumaczenia
	 */
	public int getID_translation ()
	{
		return ID_translation;
	}

	/**
	 * @return pierwsze słowo
	 */
	public String getWord1 ()
	{
		return word1;
	}

	/**
	 * @return drugie słowo
	 */
	public String getWord2 ()
	{
		return word2;
	}

	/**
	 * @return stan potwierdzenia
	 */
	public boolean isConfirmed ()
	{
		return confirmed;
	}

	/**
	 * @return średnia ocena tłumaczenia
	 */
	public double getAvg_rating ()
	{
		return avg_rating;
	}
}
