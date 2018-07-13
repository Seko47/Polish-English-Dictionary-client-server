package server;

import exceptions.AccountAlreadyExistsException;
import exceptions.NoAccountException;
import javafx.util.Pair;
import other.Account;
import other.Constants;
import other.Package;
import packages.AccountPackage;
import packages.TranslationPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Wątek klienta
 *
 * @author Rafał Sekular
 */
public class ClientThread extends Thread
{
	/**
	 * Zawiera dane zalogowanego konta
	 */
	private Account account;

	private Socket client;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;

	/**
	 * Konstruktor
	 *
	 * @param client obiekt typu Socket
	 * @throws IOException jeśli wystąpi błąd
	 */
	ClientThread (Socket client) throws IOException
	{
		this.account = new Account ();

		this.client = client;
		this.objOut = new ObjectOutputStream (this.client.getOutputStream ());
		this.objIn = new ObjectInputStream (this.client.getInputStream ());

		sendPackage (new Package<> (Constants.CONNECTED, this.account));
		this.start ();
	}

	/**
	 * Metoda wysyłająca obiekty klasy Package do klienta
	 *
	 * @param message pakiet przysyłany do klienta
	 */
	private void sendPackage (Package<Object> message)
	{
		try
		{
			this.objOut.writeObject (message);
			this.objOut.flush ();
		} catch (SocketException e)
		{
			System.out.println (Constants.MESSAGE_TRANSMISSION_STOPED);
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Metoda przetwarzająca pakiet otrzymany od klienta
	 *
	 * @param message pakiet odebrany od klienta
	 */
	private void parsePackage (Package<?> message)
	{
		int messageCode = message.getMessageCode ();
		Object data = message.getData ();

		switch (messageCode)
		{
			case Constants.EXIT:
			{
				closeConnection ();
				break;
			}
			case Constants.LOGOUT:
			{
				if (this.account.isLoggedIn ())
				{
					this.account = new Account ();
					sendPackage (new Package<> (Constants.LOGOUT, this.account));
				}
				break;
			}
			case Constants.GET_TRANSLATIONS:
			{
				if (Database.getAccountPermission (this.account) >= 2)
				{
					//data określa które tłumaczenia pobrać (0 - wszystkie, 1 - potwierdzone, 2 - niepotwierdzone)
					int choosenOperation = (Integer) data;

					StringBuilder sql = new StringBuilder ("SELECT t.ID_translation, w1.ID_word AS ID_word_1, w1.word AS word_1, w2.ID_word AS ID_word_2, w2.word AS word_2, t.is_confirmed, ROUND(AVG(r.rating), 2) AS avg_rating\n" +
							"FROM (((translations t\n" +
							"INNER JOIN words w1 ON t.ID_word_first = w1.ID_word)\n" +
							"INNER JOIN words w2 ON t.ID_word_second = w2.ID_word)\n" +
							"LEFT JOIN ratings r ON t.ID_translation = r.ID_translation)\n");
					switch (choosenOperation)
					{
						case 1:
						{
							sql.append ("WHERE t.is_confirmed = '1'\n");
							break;
						}
						case 2:
						{
							sql.append ("WHERE t.is_confirmed = '0'\n");
							break;
						}
					}

					sql.append ("GROUP BY t.ID_translation;");

					List<TranslationPackage> translationPackageList = new ArrayList<> ();

					try (Statement stmt = Database.connect ().createStatement ();
					     ResultSet rs = Database.select (stmt, sql.toString ()))
					{
						while (rs != null && rs.next ())
						{
							translationPackageList.add (new TranslationPackage (rs.getInt ("ID_translation"), rs.getString ("word_1"), rs.getString ("word_2"), rs.getBoolean ("is_confirmed"), rs.getDouble ("avg_rating")));
						}
					} catch (SQLException e)
					{
						e.printStackTrace ();
					}
					sendPackage (new Package<> (Constants.GET_TRANSLATIONS, translationPackageList));
				} else
				{
					sendPackage (new Package<> (Constants.GET_TRANSLATIONS_ERROR, Constants.MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN));
				}
				break;
			}
			case Constants.GET_ACCOUNTS:
			{
				if (Database.getAccountPermission (this.account) >= 3)
				{
					int choosenOperation = (int) data;

					List<AccountPackage> accountPackageList = new ArrayList<> ();

					StringBuilder sql = new StringBuilder ("SELECT a.ID_account, a.login, a.password, at.account_type, a.ID_account_type\n" +
							"FROM (accounts a\n" +
							"INNER JOIN account_types at ON a.ID_account_type = at.ID_account_type)\n");
					switch (choosenOperation)
					{
						case 1:
						{
							sql.append ("WHERE a.ID_account_type = '1';\n");
							break;
						}
						case 2:
						{
							sql.append ("WHERE a.ID_account_type = '2';\n");
							break;
						}
						case 3:
						{
							sql.append ("WHERE a.ID_account_type = '3';\n");
							break;
						}
						default:
						{
							break;
						}
					}

					try (Statement stmt = Database.connect ().createStatement ();
					     ResultSet rs = Database.select (stmt, sql.toString ()))
					{
						while (rs != null && rs.next ())
						{
							accountPackageList.add (new AccountPackage (rs.getInt ("ID_account"), rs.getString ("login"), rs.getString ("password"), rs.getString ("account_type")));
						}
					} catch (SQLException e)
					{
						e.printStackTrace ();
					}
					sendPackage (new Package<> (Constants.GET_ACCOUNTS, accountPackageList));
				} else
				{
					sendPackage (new Package<> (Constants.GET_ACCOUNTS_ERROR, Constants.MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN));
				}
				break;
			}

			case Constants.LOGIN:
			{
				//Pierwszy argument to login, a drugi hasło
				Pair<?, ?> pair = (Pair<?, ?>) data;

				String login = ((String) pair.getKey ()).trim ();
				String password = ((String) pair.getValue ()).trim ();

				try
				{
					this.account = new Account (login, password);
					sendPackage (new Package<> (Constants.LOGIN, this.account));
				} catch (NoAccountException e)
				{
					this.account = new Account ();
					sendPackage (new Package<> (Constants.LOGIN_ERROR, this.account));
				}
				break;
			}
			case Constants.TRANSLATE:
			{
				//Pierwszy argument to słowo, a drugi to język (w kliencie jeżyk 0 = polski, 1 = angielski) (w serwerze 1 = polski, 2 = angielski)
				Pair<?, ?> pair = (Pair<?, ?>) data;

				String word = ((String) pair.getKey ()).trim ();
				int language_index = (Integer) pair.getValue () + 1;

				String sql;
				if (language_index == 1)
				{
					sql = "SELECT t.ID_translation, w2.word AS word, t.is_confirmed, ROUND(AVG(r.rating), 2) AS avg_rating\n" +
							"FROM (((translations t\n" +
							"INNER JOIN  words w1 ON t.ID_word_first =  w1.ID_word)\n" +
							"INNER JOIN words w2 ON t.ID_word_second = w2.ID_word)\n" +
							"LEFT JOIN ratings r ON t.ID_translation = r.ID_translation)\n" +
							"WHERE w1.word LIKE '" + word + "'\n" +
							"GROUP BY t.ID_translation\n" +
							"ORDER BY t.is_confirmed DESC, avg_rating DESC;";
				} else
				{
					sql = "SELECT t.ID_translation, w1.word as word, t.is_confirmed, ROUND(AVG(r.rating), 2) AS avg_rating\n" +
							"FROM (((translations t\n" +
							"INNER JOIN  words w1 ON t.ID_word_first =  w1.ID_word)\n" +
							"INNER JOIN words w2 ON t.ID_word_second = w2.ID_word)\n" +
							"LEFT JOIN ratings r ON t.ID_translation = r.ID_translation)\n" +
							"WHERE w2.word LIKE '" + word + "'\n" +
							"GROUP BY t.ID_translation\n" +
							"ORDER BY t.is_confirmed DESC, avg_rating DESC;";
				}

				List<TranslationPackage> translationPackageList = new ArrayList<> ();

				try (Statement stmt = Database.connect ().createStatement ();
				     ResultSet rs = Database.select (stmt, sql))
				{
					while (rs != null && rs.next ())
					{
						translationPackageList.add (new TranslationPackage (rs.getInt ("ID_translation"), rs.getString ("word"), rs.getBoolean ("is_confirmed"), rs.getDouble ("avg_rating")));
					}
				} catch (SQLException e)
				{
					e.printStackTrace ();
				}

				if (translationPackageList.isEmpty ())
				{
					translationPackageList.add (new TranslationPackage (0, "Nie znaleziono tłumaczenia", true, 0.0));
				}

				sendPackage (new Package<> (Constants.TRANSLATE, translationPackageList));
				break;
			}
			case Constants.ADD_TRANSLATION:
			{
				if (Database.getAccountPermission (this.account) >= 2)
				{
					//Pierwszy argument to słowo polskie, drugi to angielskie
					Pair<?, ?> pair = (Pair<?, ?>) data;

					String polishWord = ((String) pair.getKey ()).trim ();
					String englishWord = ((String) pair.getValue ()).trim ();

					if (!polishWord.isEmpty () && !englishWord.isEmpty ())
					{
						int first_index = 0;

						String sql = "SELECT ID_word\n" +
								"FROM words\n" +
								"WHERE word LIKE '" + polishWord + "' AND ID_language = 1;";

						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs == null || !rs.next ()) //Jeżeli nie ma takiego polskiego słowa w bazie danych
							{
								sql = "INSERT INTO words (word, ID_language) VALUES\n" +
										"('" + polishWord + "', 1)";
								Database.insert (sql);

								sql = "SELECT ID_word\n" +
										"FROM words\n" +
										"WHERE word LIKE '" + polishWord + "' AND ID_language = 1;";
								try (Statement stmt2 = Database.connect ().createStatement ();
								     ResultSet rs2 = Database.select (stmt2, sql))
								{
									if (rs2 != null && rs2.next ()) first_index = rs2.getInt ("ID_word");
								} catch (SQLException e)
								{
									e.printStackTrace ();
								}
							} else
							{
								first_index = rs.getInt ("ID_word");
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}


						int second_index = 0;

						sql = "SELECT ID_word\n" +
								"FROM words\n" +
								"WHERE word LIKE '" + englishWord + "' AND ID_language = 2;";
						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs == null || !rs.next ()) //Jeżeli nie ma takiego angielskiego słowa w bazie danych
							{
								sql = "INSERT INTO words (word, ID_language) VALUES\n" +
										"('" + englishWord + "', 2)";
								Database.insert (sql);

								sql = "SELECT ID_word\n" +
										"FROM words\n" +
										"WHERE word LIKE '" + englishWord + "' AND ID_language = 2;";
								try (Statement stmt2 = Database.connect ().createStatement ();
								     ResultSet rs2 = Database.select (stmt2, sql))
								{
									if (rs2 != null && rs2.next ()) second_index = rs2.getInt ("ID_word");
								} catch (SQLException e)
								{
									e.printStackTrace ();
								}
							} else
							{
								second_index = rs.getInt ("ID_word");
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}

						sql = "SELECT COUNT(*) as amount\n" +
								"FROM translations\n" +
								"WHERE ID_word_first = '" + first_index + "' AND ID_word_second = '" + second_index + "';";
						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs != null && rs.next ())
							{
								if (rs.getInt ("amount") == 0)
								{
									sql = "INSERT INTO translations (ID_word_first, ID_word_second, is_confirmed) VALUES\n" +
											"('" + first_index + "', '" + second_index + "', '1')";
									if (Database.insert (sql))
									{
										sendPackage (new Package<> (Constants.ADD_TRANSLATION, Constants.MESSAGE_TRANSLATION_ADDED));
										break;
									}
								}
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}
					}
					sendPackage (new Package<> (Constants.ADD_TRANSLATION_ERROR, Constants.MESSAGE_CAN_NOT_ADD_TRANSLATION));
				}

				break;
			}
			case Constants.UPDATE_TRANSLATION:
			{
				if (Database.getAccountPermission (this.account) >= 2)
				{
					//Pierwszy argument to ID tłumaczenia, a drugi to kolejno: nazwa kolumny i słowo
					//Pair < Integer, Pair < String, String > >
					Pair<?, ?> integerAndPair = (Pair<?, ?>) data;
					//Pair < String, String >
					Pair<?, ?> pair = (Pair<?, ?>) integerAndPair.getValue ();

					int id_translation = (Integer) integerAndPair.getKey ();
					String columnName = ((String) pair.getKey ()).trim ();
					int id_language = (columnName.equalsIgnoreCase ("ID_word_first")) ? 1 : 2;
					String word = ((String) pair.getValue ()).trim ();

					int id_word = 0;
					String sql = "SELECT ID_word\n" +
							"FROM words\n" +
							"WHERE word LIKE '" + word + "' AND ID_language = '" + id_language + "';";
					try (Statement stmt = Database.connect ().createStatement ();
					     ResultSet rs = Database.select (stmt, sql))
					{
						if (rs == null || !rs.next ())
						{
							sql = "INSERT INTO words (word, ID_language) VALUES\n" +
									"('" + word + "', '" + id_language + "')";
							Database.insert (sql);

							sql = "SELECT ID_word\n" +
									"FROM words\n" +
									"WHERE word LIKE '" + word + "' AND ID_language = '" + id_language + "';";
							try (Statement stmt2 = Database.connect ().createStatement ();
							     ResultSet rs2 = Database.select (stmt2, sql))
							{
								if (rs2 != null && rs2.next ()) id_word = rs2.getInt ("ID_word");
							} catch (SQLException e)
							{
								e.printStackTrace ();
							}
						} else
						{
							id_word = rs.getInt ("ID_word");
						}
					} catch (SQLException e)
					{
						e.printStackTrace ();
					}

					sql = "UPDATE translations\n" +
							"SET " + columnName + " = '" + id_word + "'\n" +
							"WHERE ID_translation = '" + id_translation + "';";
					if (id_word != 0 && Database.update (sql))
					{
						sendPackage (new Package<> (Constants.UPDATE_TRANSLATION, Constants.MESSAGE_TRANSLATION_HAS_BEEN_UPDATED));
						break;
					}

					sendPackage (new Package<> (Constants.UPDATE_TRANSLATION_ERROR, Constants.MESSAGE_CAN_NOT_UPDATE_TRANSLATION));
					break;
				} else
				{
					sendPackage (new Package<> (Constants.GET_TRANSLATIONS_ERROR, Constants.MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN));
				}

				break;
			}
			case Constants.SET_ACCOUNT_PERMISSIONS:
			{
				if (Database.getAccountPermission (this.account) >= 3)
				{
					//Pierwszy argument to ID konta, a drugi to uprawnienia (1 = zwykły użytkownik, 2 = moderator, 3 = admin)
					Pair<?, ?> pair = (Pair<?, ?>) data;

					int id_account = (Integer) pair.getKey ();
					int permission = (Integer) pair.getValue ();

					if (this.account.getID_account () != id_account)
					{
						String sql = "UPDATE accounts\n" +
								"SET ID_account_type = '" + permission + "'\n" +
								"WHERE ID_account = '" + id_account + "';";

						if (Database.update (sql))
						{
							sendPackage (new Package<> (Constants.SET_ACCOUNT_PERMISSIONS, Constants.MESSAGE_ACCOUNT_PERMISSIONS_HAVE_BEEN_CHANGED));
							break;
						}
					}

					sendPackage (new Package<> (Constants.SET_ACCOUNT_PERMISSIONS_ERROR, Constants.MESSAGE_ACCOUNT_PERMISSIONS_CAN_NOT_BE_CHANGED));
				}

				break;
			}
			case Constants.ADD_ACCOUNT:
			{
				//Pierwszy argument to login, a drugi to hasło
				Pair<?, ?> pair = (Pair<?, ?>) data;

				String login = ((String) pair.getKey ()).trim ();
				String password = ((String) pair.getValue ()).trim ();

				if (!login.isEmpty () && !password.isEmpty ())
				{
					try
					{
						if (Database.createAccount (login, password))
						{
							sendPackage (new Package<> (Constants.ADD_ACCOUNT, Constants.MESSAGE_NEW_ACCOUNT_ADDED));
							break;
						}
					} catch (AccountAlreadyExistsException e)
					{
						System.out.println (e.getMessage ());
					}
				}

				sendPackage (new Package<> (Constants.ADD_ACCOUNT_ERROR, Constants.MESSAGE_CAN_NOT_ADD_A_NEW_ACCOUNT));
				break;
			}
			case Constants.RATE_TRANSLATION:
			{
				if (Database.getAccountPermission (this.account) >= 1)
				{
					//Pierwszy argument to ID tłumaczenia, a drugi ocena
					Pair<?, ?> pair = (Pair<?, ?>) data;

					int id_translation = (Integer) pair.getKey ();
					int rating = (Integer) pair.getValue ();

					String sql = "SELECT r.ID_rating\n" +
							"FROM ratings r\n" +
							"WHERE r.ID_account = '" + this.account.getID_account () + "' AND r.ID_translation = '" + id_translation + "';";
					try (Statement stmt = Database.connect ().createStatement ();
					     ResultSet rs = Database.select (stmt, sql))
					{
						if (rs == null || !rs.next ())
						{
							sql = "INSERT INTO ratings\n" +
									"(ID_translation, ID_account, rating) VALUES\n" +
									"('" + id_translation + "', '" + this.account.getID_account () + "', '" + rating + "')";
							if (Database.insert (sql))
							{
								sendPackage (new Package<> (Constants.RATE_TRANSLATION, Constants.MESSAGE_A_RATING_HAS_BEEN_ADDED));
								break;
							}
						} else
						{
							sql = "UPDATE ratings\n" +
									"SET rating = '" + rating + "'\n" +
									"WHERE ID_rating = '" + rs.getInt ("ID_rating") + "';";
							if (Database.update (sql))
							{
								sendPackage (new Package<> (Constants.RATE_TRANSLATION, Constants.MESSAGE_UPDATED_PREVIOUS_RATING));
								break;
							}
						}
					} catch (SQLException e)
					{
						System.out.println (e.getClass () + " | " + e.getMessage ());
					}

					sendPackage (new Package<> (Constants.RATE_TRANSLATION_ERROR, Constants.MESSAGE_CAN_NOT_ADD_RATING));
				} else
				{
					sendPackage (new Package<> (Constants.GET_TRANSLATIONS_ERROR, Constants.MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN));
				}

				break;
			}
			case Constants.ADD_TRANSLATION_PROPOSAL:
			{
				if (Database.getAccountPermission (this.account) >= 1)
				{
					//Pierwszy argument to słowo polskie, drugi to angielskie
					Pair<?, ?> pair = (Pair<?, ?>) data;

					String polishWord = ((String) pair.getKey ()).trim ();
					String englishWord = ((String) pair.getValue ()).trim ();

					if (!polishWord.isEmpty () && !englishWord.isEmpty ())
					{
						int first_index = 0;

						String sql = "SELECT ID_word\n" +
								"FROM words\n" +
								"WHERE word LIKE '" + polishWord + "' AND ID_language = 1;";
						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs == null || !rs.next ()) //Jeżeli nie ma takiego polskiego słowa w bazie danych
							{
								sql = "INSERT INTO words (word, ID_language) VALUES\n" +
										"('" + polishWord + "', 1)";
								Database.insert (sql);

								sql = "SELECT ID_word\n" +
										"FROM words\n" +
										"WHERE word LIKE '" + polishWord + "' AND ID_language = 1;";
								try (Statement stmt2 = Database.connect ().createStatement ();
								     ResultSet rs2 = Database.select (stmt2, sql))
								{
									if (rs2 != null && rs2.next ()) first_index = rs2.getInt ("ID_word");
								} catch (SQLException e)
								{
									e.printStackTrace ();
								}
							} else
							{
								first_index = rs.getInt ("ID_word");
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}

						int second_index = 0;

						sql = "SELECT ID_word\n" +
								"FROM words\n" +
								"WHERE word LIKE '" + englishWord + "' AND ID_language = 2;";
						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs == null || !rs.next ()) //Jeżeli nie ma takiego angielskiego słowa w bazie danych
							{
								sql = "INSERT INTO words (word, ID_language) VALUES\n" +
										"('" + englishWord + "', 2)";
								Database.insert (sql);

								sql = "SELECT ID_word\n" +
										"FROM words\n" +
										"WHERE word LIKE '" + englishWord + "' AND ID_language = 2;";
								try (Statement stmt2 = Database.connect ().createStatement ();
								     ResultSet rs2 = Database.select (stmt2, sql))
								{
									if (rs2 != null && rs2.next ()) second_index = rs2.getInt ("ID_word");
								} catch (SQLException e)
								{
									e.printStackTrace ();
								}
							} else
							{
								second_index = rs.getInt ("ID_word");
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}

						sql = "SELECT COUNT(*) as amount\n" +
								"FROM translations\n" +
								"WHERE ID_word_first = '" + first_index + "' AND ID_word_second = '" + second_index + "';";
						try (Statement stmt = Database.connect ().createStatement ();
						     ResultSet rs = Database.select (stmt, sql))
						{
							if (rs != null && rs.next ())
							{
								if (rs.getInt ("amount") == 0)
								{
									sql = "INSERT INTO translations (ID_word_first, ID_word_second, is_confirmed) VALUES\n" +
											"('" + first_index + "', '" + second_index + "', '0')";
									if (Database.insert (sql))
									{
										sendPackage (new Package<> (Constants.ADD_TRANSLATION_PROPOSAL, Constants.MESSAGE_A_TRANSLATION_PROPOSAL_HAS_BEEN_ADDED));
										break;
									}
								}
							}
						} catch (SQLException e)
						{
							e.printStackTrace ();
						}
					}
					sendPackage (new Package<> (Constants.ADD_TRANSLATION_PROPOSAL_ERROR, Constants.MESSAGE_CAN_NOT_ADD_TRANSLATION_PROPOSAL));
				} else
				{
					sendPackage (new Package<> (Constants.GET_TRANSLATIONS_ERROR, Constants.MESSAGE_THERE_WAS_AN_ERROR_TRY_LOGIN_AGAIN));
				}

				break;
			}
			case Constants.CONFIRM_TRANSLATION:
			{
				if (Database.getAccountPermission (this.account) >= 2)
				{
					//Pierwszy argument to ID tłumaczenia, a drugi potwierdzenie (true(1) - potwierdzone tłumaczenie, false(0) - niepotwierdzone)
					Pair<?, ?> pair = (Pair<?, ?>) data;

					int id_translation = (Integer) pair.getKey ();
					boolean confirm = (Boolean) pair.getValue ();
					String sql = "UPDATE translations\n" +
							"SET is_confirmed = '" + ((confirm) ? 1 : 0) + "'\n" +
							"WHERE ID_translation = '" + id_translation + "';";

					if (Database.update (sql))
					{
						sendPackage (new Package<> (Constants.CONFIRM_TRANSLATION, Constants.MESSAGE_CONFIRMATION_MADE));
						break;
					}

					sendPackage (new Package<> (Constants.CONFIRM_TRANSLATION_ERROR, Constants.MESSAGE_CAN_NOT_CONFIRM));
				}

				break;
			}

			case Constants.DELETE_TRANSLATION:
			{
				if (Database.getAccountPermission (this.account) >= 2)
				{
					int id_translation = (int) data;

					String sql = "DELETE FROM translations\n" +
							"WHERE ID_translation = '" + id_translation + "';";

					if (Database.delete (sql))
					{
						sendPackage (new Package<> (Constants.DELETE_TRANSLATION, Constants.MESSAGE_TRANSLATION_HAS_BEEN_REMOVED));
						break;
					}

					sendPackage (new Package<> (Constants.DELETE_TRANSLATION_ERROR, Constants.MESSAGE_AN_ERROR_OCCURRED_WHILE_DELETING_THE_TRANSLATION));
				}

				break;
			}
			case Constants.DELETE_ACCOUNT:
			{
				if (Database.getAccountPermission (this.account) >= 3)
				{
					//ID konta do usunięcia
					int id_account = (Integer) data;

					if (this.account.getID_account () != id_account)
					{
						String sql = "DELETE FROM accounts\n" +
								"WHERE ID_account = '" + id_account + "';\n";
						if (Database.delete (sql))
						{
							break;
						}
					}
					sendPackage (new Package<> (Constants.DELETE_ACCOUNT_ERROR, Constants.MESSAGE_FAILED_TO_DELETE_ACCOUNT));
				}
				break;
			}
		}
	}

	/**
	 * Zamyka połączenie z serwerem
	 */
	void closeConnection ()
	{
		this.interrupt ();
		try
		{
			if (this.client != null && !this.client.isClosed ())
			{
				sendPackage (new Package<> (Constants.EXIT, null));
				this.client.close ();
				this.client = null;
			}
		} catch (SocketException e)
		{
			System.out.println (e.getClass () + " | " + e.getMessage ());
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Metoda działająca jako oddzielny wątek do odbierania wiadomości od serwera
	 */
	@Override
	public void run ()
	{
		while (true)
		{
			try
			{

				if (this.client != null && this.objIn != null)
				{
					parsePackage ((Package<?>) this.objIn.readObject ());
				}

			} catch (SocketException e)
			{
				System.out.println (Constants.MESSAGE_CLIENT_THREAD_IS_STOPPED);
				return;
			} catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace ();
			}
		}

	}
}
