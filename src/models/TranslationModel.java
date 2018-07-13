package models;

import javafx.beans.property.*;

import java.io.Serializable;

/**
 * Informacje, które mogą zostać użyte w tabeli
 *
 * @author Rafał Sekular
 */
public class TranslationModel implements Serializable
{
	/**
	 * Identyfikator tłumaczenia
	 */
	private final IntegerProperty ID_translation;
	/**
	 * Pierwsze słowo
	 */
	private final StringProperty word1;
	/**
	 * Drugie słowo
	 */
	private final StringProperty word2;
	/**
	 * Określa potwierdzenie tłumaczenia
	 */
	private final BooleanProperty confirmed;
	/**
	 * Srednia ocena tłumaczenia
	 */
	private final DoubleProperty avg_rating;

	/**
	 * Konstruktor
	 *
	 * @param ID_translation identyfikator tłumaczenia
	 * @param word1          pierwsze słowo (polskie)
	 * @param word2          drugie słowo (angielskie)
	 * @param confirmed      określa, czy tłumaczenie jest potwierdzone
	 * @param avg_rating     średnia ocena tłumaczenia
	 */
	public TranslationModel ( Integer ID_translation, String word1, String word2, Boolean confirmed, Double avg_rating )
	{
		this.ID_translation = new SimpleIntegerProperty ( ID_translation );
		this.word1 = new SimpleStringProperty ( word1 );
		this.word2 = new SimpleStringProperty ( word2 );
		this.confirmed = new SimpleBooleanProperty ( confirmed );
		this.avg_rating = new SimpleDoubleProperty ( avg_rating );
	}

	/**
	 * Konstruktor
	 *
	 * @param ID_translation identyfikator tłumaczenia
	 * @param word           słowo
	 * @param confirmed      określa, czy tłumaczenie jest potwierdzone
	 * @param avg_rating     średnia ocena tłumaczenia
	 */
	public TranslationModel ( Integer ID_translation, String word, Boolean confirmed, Double avg_rating )
	{
		this.ID_translation = new SimpleIntegerProperty ( ID_translation );
		this.word1 = new SimpleStringProperty ( word );
		this.confirmed = new SimpleBooleanProperty ( confirmed );
		this.avg_rating = new SimpleDoubleProperty ( avg_rating );

		this.word2 = new SimpleStringProperty ( "" );
	}

	/**
	 * @return identyfikator tłumaczenia
	 */
	public int getID_translation ()
	{
		return ID_translation.get ();
	}

	/**
	 * @return identyfikator tłumaczenia jako obiekt IntegerProperty
	 */
	public IntegerProperty ID_translationProperty ()
	{
		return ID_translation;
	}

	/**
	 * @return drugie słowo jako obiekt StringProperty
	 */
	public StringProperty word2Property ()
	{
		return word2;
	}

	/**
	 * @return pierwsze słowo jako obiekt StringProperty
	 */
	public StringProperty word1Property ()
	{
		return word1;
	}

	/**
	 * @return stan potwierdzenia
	 */
	public boolean is_confirmed ()
	{
		return confirmed.get ();
	}

	/**
	 * @return stan potwierdzenia jako obiekt BooleanProperty
	 */
	public BooleanProperty confirmedProperty ()
	{
		return confirmed;
	}

	/**
	 * @return średnia ocena tłumaczenia jako obiekt DoubleProperty
	 */
	public DoubleProperty avg_ratingProperty ()
	{
		return avg_rating;
	}
}
