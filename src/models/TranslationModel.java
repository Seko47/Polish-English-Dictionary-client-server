package models;

import javafx.beans.property.*;
import packages.TranslationPackage;

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
	 * konstruktor konwertujący obiekt TranslationPackage na TranslationModel
	 *
	 * @param translationPackage informacje o tłumaczeniu w obiekcie TranslationPackage (odebrane z serwera)
	 */
	public TranslationModel (TranslationPackage translationPackage)
	{
		this.ID_translation = new SimpleIntegerProperty ( translationPackage.getID_translation () );
		this.word1 = new SimpleStringProperty ( translationPackage.getWord1 () );
		this.word2 = new SimpleStringProperty ( translationPackage.getWord2 () );
		this.confirmed = new SimpleBooleanProperty ( translationPackage.isConfirmed () );
		this.avg_rating = new SimpleDoubleProperty ( translationPackage.getAvg_rating () );
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
