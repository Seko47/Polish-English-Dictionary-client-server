package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import models.AccountModel;
import other.Constants;
import other.Package;
import packages.AccountPackage;

import java.util.List;
import java.util.Optional;

/**
 * @author Rafał Sekular
 */
public class AccountManagementController
{

	/**
	 * Lista zawiera nazwy typów kont
	 */
	private ObservableList < String > chooseOperationList = FXCollections.observableArrayList ( Constants.MESSAGE_ALL, Constants.MESSAGE_USERS, Constants.MESSAGE_MODERATORS, Constants.MESSAGE_ADMINISTRATORS );
	/**
	 * Lista zawiera informacje o kontach pobranych z serwera
	 */
	private static ObservableList < AccountModel > accountData = FXCollections.observableArrayList ();

	@FXML
	private ChoiceBox < String > chooseOperationBox;

	@FXML
	private TableView < AccountModel > accountTable;

	@FXML
	private TableColumn < AccountModel, Integer > ID_accountColumn;

	@FXML
	private TableColumn < AccountModel, String > loginColumn;

	@FXML
	private TableColumn < AccountModel, String > passwordColumn;

	@FXML
	private TableColumn < AccountModel, String > accountTypeColumn;

	@FXML
	private void initialize ()
	{
		this.chooseOperationBox.setItems ( this.chooseOperationList );

		this.chooseOperationBox.getSelectionModel ().selectedIndexProperty ().addListener ( ( observableValue, oldValue, newValue ) -> downloadAccounts () );

		this.ID_accountColumn.setCellValueFactory ( cellData -> cellData.getValue ().ID_accountProperty ().asObject () );
		this.loginColumn.setCellValueFactory ( cellData -> cellData.getValue ().loginProperty () );
		this.passwordColumn.setCellValueFactory ( cellData -> cellData.getValue ().passwordProperty () );
		this.accountTypeColumn.setCellValueFactory ( cellData -> cellData.getValue ().accountTypeProperty () );

		this.accountTable.setItems ( AccountManagementController.accountData );

		this.accountTable.setRowFactory ( tableView -> {
			TableRow < AccountModel > row = new TableRow <> ();
			row.setOnMouseClicked ( mouseEvent -> {
				if ( !row.isEmpty () && mouseEvent.getButton () == MouseButton.PRIMARY && mouseEvent.getClickCount () == 2 )
				{
					AccountModel account = row.getItem ();
					showAccountTypeChanger ( account.getID_account () );
					downloadAccounts ();
				}
				else if ( !row.isEmpty () && mouseEvent.getButton () == MouseButton.SECONDARY && mouseEvent.getClickCount () == 2 )
				{
					AccountModel account = row.getItem ();
					ClientController.sendPackage ( new Package <> ( Constants.DELETE_ACCOUNT, account.getID_account () ) );
					downloadAccounts ();
				}
			} );
			return row;
		} );

		this.accountTable.setTooltip ( new Tooltip ( Constants.MESSAGE_ACCOUNT_TABLE_TOOLTIP ) );
	}

	/**
	 * Wysyła do serwera prośbę o przesłanie informacji o kontach
	 */
	@FXML
	private void downloadAccounts ()
	{
		int choosenOperation = this.chooseOperationBox.getSelectionModel ().getSelectedIndex ();
		ClientController.sendPackage ( new Package <> ( Constants.GET_ACCOUNTS, choosenOperation ) );
	}

	/**
	 * Podmienia dane kont do wyświetlenia w tabeli
	 *
	 * @param list lista kont do wyświetlenia
	 */
	static void setAccountData ( List < ? > list )
	{
		Platform.runLater ( () -> {
			accountData.clear ();
			list.forEach ( item -> {
				AccountPackage tmp = ( AccountPackage ) item;
				accountData.add ( new AccountModel ( tmp.getID_account (), tmp.getLogin (), tmp.getPassword (), tmp.getAccountType () ) );
			} );
		} );
	}

	/**
	 * Wyświetla dialog pozwalający zmienić typ konta
	 *
	 * @param id_account identyfikator konta
	 */
	private void showAccountTypeChanger ( int id_account )
	{
		Dialog < Integer > dialog = new Dialog <> ();
		dialog.setTitle ( Constants.MESSAGE_CHANGING_ACCOUNT_TYPE );
		dialog.setHeaderText ( Constants.MESSAGE_CHOOSE_ACCOUNT_TYPE );

		ButtonType acceptButtonType = new ButtonType ( Constants.MESSAGE_SAVE, ButtonBar.ButtonData.OK_DONE );
		dialog.getDialogPane ().getButtonTypes ().addAll ( acceptButtonType, ButtonType.CANCEL );

		GridPane grid = new GridPane ();
		grid.setHgap ( 10 );
		grid.setVgap ( 10 );
		grid.setPadding ( new Insets ( 20, 80, 10, 80 ) );

		ObservableList < String > choiceBoxData = FXCollections.observableArrayList ( Constants.MESSAGE_USER, Constants.MESSAGE_MODERATOR, Constants.MESSAGE_ADMINISTRATOR );
		ChoiceBox < String > choiceBox = new ChoiceBox <> ( choiceBoxData );
		choiceBox.setValue ( choiceBoxData.get ( 0 ) );

		grid.add ( choiceBox, 0, 0 );

		dialog.getDialogPane ().setContent ( grid );

		dialog.setResultConverter ( dialogButton -> {
			if ( dialogButton == acceptButtonType )
			{
				return choiceBox.getSelectionModel ().getSelectedIndex () + 1;
			}

			return null;
		} );

		Optional < Integer > result = dialog.showAndWait ();

		result.ifPresent ( accountType -> ClientController.sendPackage ( new Package <> ( Constants.SET_ACCOUNT_PERMISSIONS, new Pair <> ( id_account, accountType ) ) ) );
	}
}
