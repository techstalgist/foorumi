package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();
        
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }
        
        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue\n" +
                    "(\n" +
                    "id integer PRIMARY KEY NOT NULL,\n" +
                    "nimi varchar(200) NOT NULL\n" +
                    ");");
        lista.add("INSERT INTO Alue VALUES(1,'Lemmikit');");
        lista.add("INSERT INTO Alue VALUES(2,'Lentokoneet');");
        lista.add("INSERT INTO Alue VALUES(3,'Ohjelmointi');");
        lista.add("INSERT INTO Alue VALUES(4,'Elokuvat');");

        lista.add("CREATE TABLE Keskustelu\n" +
                    "(\n" +
                    "id integer PRIMARY KEY NOT NULL,\n" +
                    "nimi varchar(200) NOT NULL,\n" +
                    "alue_id integer NOT NULL,\n" +
                    "FOREIGN KEY(alue_id) REFERENCES Alue(id)\n" +
                    ");");
      
        lista.add("INSERT INTO Keskustelu VALUES(1,'Java on jees',3);");       
        lista.add("INSERT INTO Keskustelu VALUES(2,'Python on jeesimpi',3);");       
        lista.add("INSERT INTO Keskustelu VALUES(3,'Koirat on jees',1);");       
        lista.add("INSERT INTO Keskustelu VALUES(4,'Kissaton jees',1);"); 
        
        
        lista.add("CREATE TABLE Viesti\n" +"(\n" +
                    "id integer PRIMARY KEY NOT NULL,\n" +
                    "sisalto varchar(10000) NOT NULL,\n" +
                    "lahettaja varchar(100) NOT NULL,\n" +
                    "keskustelu_id integer NOT NULL,\n" +
                    "aikaleima integer NOT NULL,\n" +
                    "FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id)\n" +
                    ");");

        lista.add("INSERT INTO Viesti VALUES(1,'foo','Mikko',1,1474298815);");      
        lista.add("INSERT INTO Viesti VALUES(2,'bar','Santeri',1,1474298842);");
        lista.add("INSERT INTO Viesti VALUES(3,'baz','Antti',1,1474298865);");
        lista.add("INSERT INTO Viesti VALUES(4,'foo','Antti',3,1474299043);");
        
        
        return lista;
    }
    
    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY NOT NULL,\n" +
                    "nimi varchar(200) NOT NULL\n" +
                    ");");
        lista.add("INSERT INTO Alue (nimi) VALUES('Lemmikit');");
        lista.add("INSERT INTO Alue (nimi) VALUES('Lentokoneet');");
        lista.add("INSERT INTO Alue (nimi) VALUES('Ohjelmointi');");
        lista.add("INSERT INTO Alue (nimi) VALUES('Elokuvat');");

        lista.add("CREATE TABLE Keskustelu\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY NOT NULL,\n" +
                    "nimi varchar(200) NOT NULL,\n" +
                    "alue_id integer NOT NULL,\n" +
                    "FOREIGN KEY(alue_id) REFERENCES Alue(id)\n" +
                    ");");
      
        lista.add("INSERT INTO Keskustelu (nimi,alue_id) VALUES('Java on jees',3);");       
        lista.add("INSERT INTO Keskustelu (nimi,alue_id) VALUES('Python on jeesimpi',3);");       
        lista.add("INSERT INTO Keskustelu (nimi,alue_id) VALUES('Koirat on jees',1);");       
        lista.add("INSERT INTO Keskustelu (nimi,alue_id) VALUES('Kissat on jees',1);"); 
        
        
        lista.add("CREATE TABLE Viesti\n" +"(\n" +
                    "id SERIAL PRIMARY KEY NOT NULL,\n" +
                    "sisalto varchar(10000) NOT NULL,\n" +
                    "lahettaja varchar(100) NOT NULL,\n" +
                    "keskustelu_id integer NOT NULL,\n" +
                    "aikaleima integer NOT NULL,\n" +
                    "FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id)\n" +
                    ");");

        lista.add("INSERT INTO Viesti (sisalto,lahettaja,keskustelu_id,aikaleima) VALUES('foo','Mikko',1,1474298815);");      
        lista.add("INSERT INTO Viesti (sisalto,lahettaja,keskustelu_id,aikaleima) VALUES('bar','Santeri',1,1474298842);");
        lista.add("INSERT INTO Viesti (sisalto,lahettaja,keskustelu_id,aikaleima) VALUES('baz','Antti',1,1474298865);");
        lista.add("INSERT INTO Viesti (sisalto,lahettaja,keskustelu_id,aikaleima) VALUES('foo','Antti',3,1474299043);");
        
        return lista;
    }
}
