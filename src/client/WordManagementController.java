package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.util.Pair;
import models.TranslationModel;
import other.Constants;
import other.Package;
import packages.TranslationPackage;

import java.util.List;
import java.util.Locale;

/**
 * @author Rafał Sekular
 */
public class WordManagementController
{
	/**
	 * Lista zawiera filtry dla tłumaczeń
	 */
	private ObservableList < String > chooseOperationList = FXCollections.observableArrayList ( Constants.MESSAGE_ALL, Constants.MESSAGE_CONFIRMED, Constants.MESSAGE_UNCONFIRMED );
	/**
	 * Lista zawiera informacje o tłumaczeniach pobranych z serwera
	 */
	private static ObservableList < TranslationModel > translationData = FXCollections.observableArrayList ();

	@FXML
	private ChoiceBox < String > chooseOperationBox;

	@FXML
	private TableView < TranslationModel > translationTable;

	@FXML
	private TableColumn < TranslationModel, Integer > ID_translationColumn;

	@FXML
	private TableColumn < TranslationModel, String > word1TranslationColumn;

	@FXML
	private TableColumn < TranslationModel, String > word2TranslationColumn;

	@FXML
	private TableColumn < TranslationModel, Double > ratingTranslationColumn;

	@FXML
	private TableColumn < TranslationModel, Boolean > isConfirmedTranslationColumn;

	@FXML
	private TextField polishTextField;

	@FXML
	private TextField englishTextField;

	@FXML
	private void initialize ()
	{
		this.chooseOperationBox.setItems ( this.chooseOperationList );

		this.chooseOperationBox.getSelectionModel ().selectedIndexProperty ().addListener ( ( observableValue, oldValue, newValue ) -> downloadTranslations () );

		this.ID_translationColumn.setCellValueFactory ( cellData -> cellData.getValue ().ID_translationProperty ().asObject () );
		this.word1TranslationColumn.setCellValueFactory ( cellData -> cellData.getValue ().word1Property () );
		this.word2TranslationColumn.setCellValueFactory ( cellData -> cellData.getValue ().word2Property () );
		this.ratingTranslationColumn.setCellValueFactory ( cellData -> cellData.getValue ().avg_ratingProperty ().asObject () );
		this.isConfirmedTranslationColumn.setCellValueFactory ( cellData -> cellData.getValue ().confirmedProperty () );

		this.word1TranslationColumn.setCellFactory ( TextFieldTableCell.forTableColumn () );
		this.word2TranslationColumn.setCellFactory ( TextFieldTableCell.forTableColumn () );
		this.translationTable.setItems ( WordManagementController.translationData );

		this.word1TranslationColumn.setOnEditCommit ( event -> {
			TranslationModel translation = event.getRowValue ();
			ClientController.sendPackage ( new Package <> ( Constants.UPDATE_TRANSLATION, new Pair <> ( translation.getID_translation (), new Pair <> ( "ID_word_first", event.getNewValue () ) ) ) );
		} );

		this.word2TranslationColumn.setOnEditCommit ( event -> {
			TranslationModel translation = event.getRowValue ();
			ClientController.sendPackage ( new Package <> ( Constants.UPDATE_TRANSLATION, new Pair <> ( translation.getID_translation (), new Pair <> ( "ID_word_second", event.getNewValue () ) ) ) );
		} );

		this.isConfirmedTranslationColumn.setOnEditStart ( event -> {
			TranslationModel translation = event.getRowValue ();
			boolean confirmed = translation.is_confirmed ();
			ClientController.sendPackage ( new Package <> ( Constants.CONFIRM_TRANSLATION, new Pair <> ( translation.getID_translation (), !confirmed ) ) );
			downloadTranslations ();
		} );

		this.translationTable.setRowFactory ( tableView -> {
			TableRow < TranslationModel > row = new TableRow <> ();
			row.setOnMouseClicked ( mouseEvent -> {
				if ( !row.isEmpty () && mouseEvent.getButton () == MouseButton.SECONDARY && mouseEvent.getClickCount () == 2 )
				{
					TranslationModel translation = row.getItem ();
					ClientController.sendPackage ( new Package <> ( Constants.DELETE_TRANSLATION, translation.getID_translation () ) );
					downloadTranslations ();
				}
			} );
			return row;
		} );

		this.translationTable.setTooltip ( new Tooltip ( Constants.MESSAGE_TRANSLATION_TABLE_TOOLTIP ) );
	}

	/**
	 * Dodaje tłumaczenie po wciśnięciu przycisku ENTER
	 *
	 * @param keyEvent event dla przycisku
	 */
	@FXML
	private void onKeyReleasedAddTranslation ( KeyEvent keyEvent )
	{
		if ( keyEvent.getCode () == KeyCode.ENTER )
		{
			addTranslation ();
		}
	}

	/**
	 * Dodaje tłumaczenie
	 */
	@FXML
	private void addTranslation ()
	{
		String polish = this.polishTextField.getText ().trim ().toLowerCase ( new Locale ( "pl", "PL" ) );
		String english = this.englishTextField.getText ().trim ().toLowerCase ( new Locale ( "pl", "PL" ) );

		if ( !polish.isEmpty () && !english.isEmpty () )
		{
			ClientController.sendPackage ( new Package <> ( Constants.ADD_TRANSLATION, new Pair <> ( polish, english ) ) );
			this.polishTextField.clear ();
			this.englishTextField.clear ();
			downloadTranslations ();
		}
	}

	/**
	 * Wysyła prośbę o przesłanie tłumaczeń
	 */
	@FXML
	private void downloadTranslations ()
	{
		int choosenOperation = this.chooseOperationBox.getSelectionModel ().getSelectedIndex ();

		ClientController.sendPackage ( new Package <> ( Constants.GET_TRANSLATIONS, choosenOperation ) );
	}

	/**
	 * Ustawia listę tłumaczeń
	 *
	 * @param list lista z tłumaczeniami
	 */
	static void setTranslationData ( List < ? > list )
	{
		Platform.runLater ( () -> {
			translationData.clear ();
			list.forEach ( item -> {
				TranslationPackage tmp = ( TranslationPackage ) item;
				translationData.add ( new TranslationModel ( tmp.getID_translation (), tmp.getWord1 (), tmp.getWord2 (), tmp.isConfirmed (), tmp.getAvg_rating () ) );
			} );
		} );
	}
}
