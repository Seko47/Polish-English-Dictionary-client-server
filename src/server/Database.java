package server;

import exceptions.AccountAlreadyExistsException;
import other.Account;

import java.io.File;
import java.sql.*;

/**
 * Klasa obsługująca bazę danych
 *
 * @author Rafał Sekular
 */
public class Database
{
	/**
	 * Przechowuje ścieżkę oraz nazwę bazy danych.
	 */
	private static final String dbName = "dictionary.db";

	/**
	 * Aktualne połączenie z bazą danych
	 */
	private static Connection connection = null;

	/**
	 * Prywatny konstruktor, żeby zablokować możliwość tworzenia obiektu
	 */
	private Database ()
	{
	}

	/**
	 * Tworzy bazę danych, jeśli nie istnieje.
	 */
	static void init ()
	{
		if ( !new File ( dbName ).exists () )
		{
			String sql = "CREATE TABLE account_types (\n" +
					"ID_account_type	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"account_type		VARCHAR (20) NOT NULL UNIQUE\n" +
					");";

			execute ( sql );

			sql = "CREATE TABLE accounts (\n" +
					"ID_account			INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"login				VARCHAR (50) UNIQUE NOT NULL,\n" +
					"password			VARCHAR (50) NOT NULL,\n" +
					"ID_account_type	INTEGER REFERENCES account_types (ID_account_type) ON DELETE SET DEFAULT ON UPDATE CASCADE DEFAULT (1) NOT NULL\n" +
					");";

			execute ( sql );

			sql = "CREATE TABLE languages (\n" +
					"ID_language		INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"language			VARCHAR (30) NOT NULL\n" +
					");";

			execute ( sql );

			sql = "CREATE TABLE ratings (\n" +
					"ID_rating      	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"ID_translation 	INTEGER REFERENCES translations (ID_translation) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,\n" +
					"ID_account     	INTEGER REFERENCES accounts (ID_account) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,\n" +
					"rating         	INTEGER NOT NULL DEFAULT (0) \n" +
					");";

			execute ( sql );

			sql = "CREATE TABLE translations (\n" +
					"ID_translation 	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"ID_word_first  	INTEGER REFERENCES words (ID_word) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,\n" +
					"ID_word_second 	INTEGER REFERENCES words (ID_word) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,\n" +
					"is_confirmed   	BOOLEAN NOT NULL DEFAULT (0)\n" +
					");";

			execute ( sql );

			sql = "CREATE TABLE words (\n" +
					"ID_word 			INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
					"word 				VARCHAR (200) NOT NULL,\n" +
					"ID_language 		INTEGER REFERENCES languages (ID_language) ON UPDATE CASCADE\n" +
					");";

			execute ( sql );


			sql = "INSERT INTO account_types (account_type) VALUES\n" +
					"('user')," +
					"('moderator')," +
					"('admin');";

			insert ( sql );

			sql = "INSERT INTO languages (language) VALUES" +
					"('polish')," +
					"('english');";

			insert ( sql );

			sql = "INSERT INTO accounts (login, password, ID_account_type) VALUES\n" +
					"('admin', 'admin', 3);";

			insert ( sql );
		}
	}

	/**
	 * Połączenie z bazą danych.
	 *
	 * @return obiekt Connection.
	 * @throws SQLException błąd podczas łączenia z bazą
	 */
	static Connection connect () throws SQLException
	{
		if ( connection == null || connection.isClosed () )
		{
			String url = "jdbc:sqlite:" + dbName;

			connection = DriverManager.getConnection ( url );
		}
		return connection;
	}

	/**
	 * Wykonanie zapytania na bazie danych
	 *
	 * @param sql zapytanie do bazy danych.
	 */
	private static void execute ( String sql )
	{
		try ( Statement stmt = connect ().createStatement () )
		{
			stmt.execute ( sql );
		}
		catch ( SQLException e )
		{
			System.out.println ( "Database.execute() | " + e.getClass () + " | " + e.getMessage () );
		}
	}

	/**
	 * Dodawanie danych do bazy danych
	 *
	 * @param sql zapytanie do bazy danych.
	 * @return true jeśli wykona polecenie, false jeśli wystąpi błąd.
	 */
	static boolean insert ( String sql )
	{
		try ( PreparedStatement pstmt = connect ().prepareStatement ( sql ) )
		{
			pstmt.executeUpdate ();
		}
		catch ( SQLException e )
		{
			System.out.println ( "Database.insert() | " + e.getClass () + " | " + e.getMessage () );
			return false;
		}

		return true;
	}

	/**
	 * Wybieranie danych z bazy danych
	 *
	 * @param stmt obiekt Statement
	 * @param sql  zapytanie do bazy danych.
	 * @return obiekt ResultSet odwołujący się do danych z bazy danych
	 */
	static ResultSet select ( Statement stmt, String sql )
	{
		try
		{
			return stmt.executeQuery ( sql );
		}
		catch ( SQLException e )
		{
			System.out.println ( "Database.select() | " + e.getClass () + " | " + e.getMessage () );
		}

		return null;
	}

	/**
	 * Aktualizowanie danych w bazie danych
	 *
	 * @param sql zapytanie do bazy danych.
	 * @return true jeśli wykona polecenie, false jeśli wystąpi błąd.
	 */
	static boolean update ( String sql )
	{
		return insert ( sql );
	}

	/**
	 * Usuwanie danych z bazy danych
	 *
	 * @param sql zapytanie do bazy danych.
	 * @return true jeśli wykona polecenie, false jeśli wystąpi błąd.
	 */
	static boolean delete ( String sql )
	{
		return insert ( sql );
	}

	/**
	 * Sprawdza, czy podane konto istnieje
	 *
	 * @param login login do konta
	 * @return true - konto istnieje, jeśli nie - false
	 */
	private static boolean isAccount ( String login )
	{
		String sql = "SELECT COUNT(*) AS iloscKont\n" +
				"FROM accounts\n" +
				"WHERE login LIKE '" + login + "';";
		try ( Connection conn = Database.connect ();
			  Statement stmt = conn.createStatement ();
			  ResultSet rs = Database.select ( stmt, sql ) )
		{
			if ( rs != null && rs.next () )
			{
				return rs.getInt ( "iloscKont" ) != 0;
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace ();
		}

		return false;
	}

	/**
	 * Sprawdza, czy podane konto istnieje
	 *
	 * @param login    login do konta
	 * @param password hasło do konta
	 * @return true - konto istnieje, jeśli nie - false
	 */
	public static boolean isAccount ( String login, String password )
	{
		String sql = "SELECT COUNT(*) AS iloscKont\n" +
				"FROM accounts\n" +
				"WHERE login LIKE '" + login + "' AND password LIKE '" + password + "';";
		try ( Statement stmt = Database.connect ().createStatement ();
			  ResultSet rs = Database.select ( stmt, sql ) )
		{
			if ( rs != null && rs.next () )
			{
				return rs.getInt ( "iloscKont" ) != 0;
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace ();
		}

		return false;
	}

	/**
	 * Pobiera uprawnienia dla konta
	 *
	 * @param account konto dla którego mają być sprawdzone uprawnienia
	 * @return uprawnienia konta (0 - brak, 1 - użytkownik, 2 - moderator, 3 - admin)
	 */
	static int getAccountPermission ( Account account )
	{
		if ( isAccount ( account.getLogin (), account.getPassword () ) )
		{
			String sql = "SELECT ID_account_type\n" +
					"FROM accounts\n" +
					"WHERE login LIKE '" + account.getLogin () + "' AND password LIKE '" + account.getPassword () + "';\n";
			try ( Statement stmt = Database.connect ().createStatement ();
				  ResultSet rs = Database.select ( stmt, sql ) )
			{
				if ( rs != null && rs.next () )
				{
					return rs.getInt ( "ID_account_type" );
				}
			}
			catch ( SQLException e )
			{
				e.printStackTrace ();
			}
		}
		return 0;
	}

	/**
	 * Zwraca konto z bazy danych
	 *
	 * @param login    login do konta
	 * @param password hasło do konta
	 * @return konto pobrane z bazy danych
	 */
	public static Account getAccount ( String login, String password )
	{
		String sql = "SELECT ID_account, login, password, ID_account_type\n" +
				"FROM accounts\n" +
				"WHERE login LIKE '" + login + "' AND password LIKE '" + password + "';";

		try ( Statement stmt = Database.connect ().createStatement ();
			  ResultSet rs = Database.select ( stmt, sql ) )
		{
			if ( rs != null && rs.next () )
			{
				return new Account ( rs.getInt ( "ID_account" ), rs.getString ( "login" ), rs.getString ( "password" ), rs.getInt ( "ID_account_type" ) );
			}
		}
		catch ( SQLException e )
		{

			e.printStackTrace ();
		}

		return null;
	}

	/**
	 * Dodaje konto do bazy danych (jeśli nie istnieje)
	 *
	 * @param login    login do konta
	 * @param password hasło do konta
	 * @return true - konto utworzone, jeśli nie - false
	 * @throws AccountAlreadyExistsException jeśli konto już istnieje
	 */
	static boolean createAccount ( String login, String password ) throws AccountAlreadyExistsException
	{
		if ( login.equalsIgnoreCase ( "Nie zalogowany" ) || Database.isAccount ( login ) )
		{
			throw new AccountAlreadyExistsException ();
		}

		String sql = "INSERT INTO accounts (login, password) VALUES\n" +
				"('" + login + "', '" + password + "');";

		return Database.insert ( sql );
	}
}
