# The Exporter

The Exporter is a console-app which connects into a database and show any table content on the console or export it into a textfile.

### Used Libraries
* **MySQL-JDBC-Connector:** To establish a connection to MySQL-Database.
* **Apache Commons-CLI:** To process the corresponding arguments.

### Usage
```
usage: {-h-u-p-s-r-w-t-f-o} [-f-d-T]
 -d,--database <arg>    Name der Datenbank (Name of the Database)
 -f,--list <arg>        Kommagetrennte Liste (ohne Leerzeichen) der
                        Felder, die im Ergebnis enthalten sein sollen.
 -h,--host <arg>        Hostname des DBMS. Standard: localhost (Database Hostname)
 -o,--output <arg>      Name der Ausgabedatei. Standard: keine -> Ausgabe
                        auf der Konsole (Filename. Default: None)
 -p,--password <arg>    Passwort. Alternativ kann ein Passwortprompt
                        angezeigt werden. Standard: keins (Password. Alternatively a password prompt will Pop-up. Default: None)
 -r,--direction <arg>   Sortierrichtung. Standard: ASC (Sort direction. Default: ASC)
 -s,--sort <arg>        Feld, nach dem sortiert werden soll (nur eines
                        möglich, Standard: keines) (Sort field. Default: None)
 -t,--separator <arg>   Trennzeichen, dass für die Ausgabe verwendet
                        werden soll. Standard: ; (Delimiter. Default: ;)
 -T,--table <arg>       Tabellenname (Pflicht) (Tablename)
 -u,--user <arg>        Benutzername. Standard: Benutzername des im
                        Betriebssystem angemeldeten Benutzers (Username. Default: OS logged user)
 -w,--filter <arg>      eine Bedingung in SQL-Syntax, die um Filtern der
                        Tabelle verwendet wird. Standard: keine (SQL-Syntax. Default: None)

Beispiel/Example:
 java -jar TheExporter.jar -h projekte.tgm.ac.at -u test -p test -d megamarkt -T produkt -f titel,preis -s preis -r DESC -w 'preis > 4' -t '$'
```
Coded with :heart:
