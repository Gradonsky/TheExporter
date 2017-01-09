import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
/**
 * Aufgabe Au04 - The Exporter Es ist eine Klasse für eine Konsolen-Anwendung
 * die den Inhalt einer beliebigen Tabelle eines RDBMS ls Textfile oder in der
 * Konsole ausgibt.
 *
 * Verwendete Bibliotheken: Mysql-Connector : Um eine Verbindung mit der
 * !MYSQL!-Datenbank herzustellen Commons-Cli Apache : Um die Ensprechenden
 * Argumente zu verarbeiten.
 *
 * @author Gradonski Janusz
 *
 */
public class CLI {

	/* ANSI-CODES: RED für die rote Farbe , RESET für die Standartfarbe */
	public static final String RED = "\u001B[31m";
	public static final String RESET = "\u001B[0m";
	private static Options options = new Options();

	/*
	 * Die Einstellungen (Default Werte) um sich zu einer Datenbank zu verbinden
	 * Default werte: Username = Derzeitig eingelogter User Password = leer
	 * hostname = localhost datenbank = leer sortierrichtung = ASC Rest leer
	 */
	private static String username = System.getProperty("user.name"),
			password = "", hostname = "localhost", database = "", sortieren,
			direction = "ASC", sqlbefehl = "SELECT ", whr, separator = ";", list = "",
			output, table = "";
	private static File outputf;

	/**
	 * Die Mainmethode der Klasse CLI
	 *
	 * @param args
	 */

	public static void main(String[] args) throws ParseException {
		/*
		 * Die Argumentoptionen
		 *
		 */

		options.addOption("h", "host", true,
				"Hostname des DBMS. Standard: localhost");
		options.addOption(
				"u",
				"user",
				true,
				"Benutzername. Standard: Benutzername des im Betriebssystem angemeldeten Benutzers");
		options.addOption(
				"p",
				"password",
				true,
				"Passwort. Alternativ kann ein Passwortprompt angezeigt werden. Standard: keins");
		options.addOption("d", "database", true, "Name der Datenbank");
		options.addOption("s", "sort", true,
				"Feld, nach dem sortiert werden soll (nur eines möglich, Standard: keines)");
		options.addOption("r", "direction", true,
				"Sortierrichtung. Standard: ASC");
		options.addOption(
				"w",
				"filter",
				true,
				"eine Bedingung in SQL-Syntax, die um Filtern der Tabelle verwendet wird. Standard: keine");
		options.addOption("t", "separator", true,
				"Trennzeichen, dass für die Ausgabe verwendet werden soll. Standard: ; ");
		options.addOption(
				"f",
				"list",
				true,
				"Kommagetrennte Liste (ohne Leerzeichen) der Felder, die im Ergebnis enthalten sein sollen.");
		options.addOption("o", "output", true,
				"Name der Ausgabedatei. Standard: keine -> Ausgabe auf der Konsole");
		options.addOption("T", "table", true, "Tabellenname (Pflicht)");

		// Die Hilfe wird hier Automatisch generiert
		HelpFormatter formatter = new HelpFormatter();
		BasicParser parser = new BasicParser();
		// Ausgabe der Hilfe
		formatter.printHelp("{-h-u-p-s-r-w-t-f-o} [-f-d-T]", options);
		System.out.println("\nBeispiel: \n java -jar TheExporter.jar -h projekte.tgm.ac.at -u test -p test -d megamarkt -T produkt -f titel,preis -s preis -r DESC -w 'preis > 4' -t '$'\n");
		/*
		 * Die Ensprechenden Optionen (Parameter) werden aufgefangen und dann
		 * verarbeitet.
		 */
		try {
			CommandLine cl = parser.parse(options, args);

			if(!cl.hasOption('T')){
				System.out.println("Um eine Verbindung zur Datenbank herzustellen sind mindestens die \n Argumente -T -d und -f erfordelich.");
				System.exit(1);
			}
			if(!cl.hasOption('f')){
				System.out.println("Um eine Verbindung zur Datenbank herzustellen sind mindestens die \n Argumente -T -d und -f erfordelich.");
				System.exit(1);
			}
			if(!cl.hasOption('d')){
				System.out.println("Um eine Verbindung zur Datenbank herzustellen sind mindestens die \n Argumente -T -d und -f erfordelich.");
				System.exit(1);
			}
			/*
			 * Option h speichert die Eingabe in die Variable hostname und ist
			 * zuständig so wie der Name schon sagt für den Hostname
			 */
			if (cl.hasOption('h')) {
				hostname = cl.getOptionValue("h");

			}

			/*
			 * Die Option u speichert die Eingabe in die Variable username.
			 */
			if (cl.hasOption('u')) {
				username = cl.getOptionValue("u");
			}

			/*
			 * Die Option p speichert die Eingabe in die Variable password.
			 */
			if (cl.hasOption('p')) {
				password = cl.getOptionValue("p");
			}

			/*
			 * Die Option d speichert die Eingabe in die Variable database.
			 */
			if (cl.hasOption('d')) {
				database = cl.getOptionValue("d");
			}

			/*
			 * Die Option s speichert die Eingabe in die Variable sortieren.
			 */
			if (cl.hasOption('s')) {
				sortieren = cl.getOptionValue("s");
			}

			/*
			 * Die Option r speichert die Eingabe in die Variable richtung.
			 */
			if (cl.hasOption('r')) {
				direction = cl.getOptionValue("r");
			}

			/*
			 * Die Option w speichert die Eingabe in die Variable whr ( steht
			 * für WHERE).
			 */
			if (cl.hasOption('w')) {
				whr = cl.getOptionValue("w");
			}

			/*
			 * Die Option S speichert die Eingabe in die Variable separator.
			 */
			if (cl.hasOption('t')) {
				separator = cl.getOptionValue("t");
			}

			/*
			 * Die Option f speichert die Eingabe in die Variable list.
			 */
			if (cl.hasOption('f')) {
				list = cl.getOptionValue("f");
			}

			/*
			 * Die Option o speichert die Eingabe in die Variable output.
			 */
			if (cl.hasOption('o')) {
				output = cl.getOptionValue("o");

			}

			/*
			 * Die Option T speichert die Eingabe in die Variable table.
			 */
			if (cl.hasOption('T')) {
				table = cl.getOptionValue("T");
			}
			/*
			 * Fangt eine ParseException und alle anderen
			 * Exceptions auf und gibt für sie eine Message zurück.
			 */
		} catch (Exception e) {
			System.out.println("General exception: " + e.getMessage());
		}


		// Database Connection
		MysqlDataSource mds = new MysqlDataSource();
		mds.setServerName(hostname);
		mds.setDatabaseName(database);
		mds.setUser(username);
		mds.setPassword(password);

			// Das SQL-Befehl wird entsprechend Vorbereitet

		sqlbefehl += list + " FROM " + table;

		if (whr != null) {
			sqlbefehl += " WHERE " + whr;
		}
		if (sortieren != null) {
			sqlbefehl += " ORDER BY " + sortieren;
		}
		if (direction != "ASC") {
			sqlbefehl += " " + direction;
		} else {
			sqlbefehl += ";";
		}
		try {
			System.out.println("Connecting to a selected database...");
			Connection co = mds.getConnection();
			System.out.println("Connected database successfully...");
			System.out.println("Welcome: " + username);
			System.out.println("Creating statement...");
			Statement stat = co.createStatement();
			ResultSet result = stat.executeQuery(sqlbefehl);
			ResultSetMetaData meta = result.getMetaData();
			int count = meta.getColumnCount();

			// Wenn kein Output angegeben ist , wird alles auf der Konsole ausgegeben
			if (output == null) {
						// Die Tabellen und Kolumnen werden aus
						// den Metadaten ausgelesen
				System.out.println("The columns in the table are: ");
				System.out.println("Table: "
						+ result.getMetaData().getTableName(1));
				for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
					System.out.println("Column " + i + " "
							+ result.getMetaData().getColumnName(i));

				}
				try {
					/*
					 * Mit dem result.next [Cursor] wandern
					 * wir durch die Ausgabe und geben sie
					 * In der konsole oder in dem File
					 * auf.
					 */
					while (result.next()) {

						String row = "";
						for (int i = 1; i <= result.getMetaData()
								.getColumnCount(); i++) { 	// Anzahl der Spalten
							if (result.getMetaData().getColumnCount() == i) {
								row += result.getString(i);
							} else {
								row += result.getString(i) + separator;
							}

						}
						System.out.println(row);
					}
				} catch (Exception e) {
					System.out.println("Unexpected exception: "
							+ e.getMessage());
				}

			} else {
				/*
				 * Wenn ein Output-File angegeben wird
				 * dann erstellt die Klasse ein TextFile
				 * und schreibt das Ergebnis hinein.
				 */
				try {
					outputf = new File(output+".txt");
					if (outputf.exists() == false) {
						outputf.createNewFile();
					}

					PrintWriter out = new PrintWriter(new FileWriter(output
							+ ".txt", true));
						// Wie Oben
					while (result.next()) {
						String row = "";
						for (int i = 1; i <= result.getMetaData()
								.getColumnCount(); i++) {
							if (result.getMetaData().getColumnCount() == i) {
								row += result.getString(i);
							} else {
								row += result.getString(i) + separator;
							}

						}
						//Schreibt die Rows in die Datei
						out.append(row + "\n");
					}
					// Scheißt den PrintWriter
					out.close();

				} catch (Exception e) {
					System.out.println("Unexpected exception: "
							+ e.getMessage());
				}
				System.out.println("Exported in file: " + output + ".txt");
			} // ELSE - File OUTPUT

		} catch (SQLException e) {
			System.out.println("Unexpected exception: " + e.getMessage());

		}

	}
}
