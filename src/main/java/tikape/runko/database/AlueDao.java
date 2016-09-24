/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;

/**
 *
 * @author mikkoruuskanen
 */
public class AlueDao implements Dao<Alue, Integer> {
    
    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        return null;
    }

        
    @Override
    public List<Alue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT \n" +
        "a.id, \n" +
        "a.nimi,\n" +
        "COUNT(*) as lkm,\n" +
        "MAX(aikaleima) as viimeisin\n" +
        "FROM Viesti v\n" +
        "INNER JOIN Keskustelu k \n" +
        "ON v.keskustelu_id = k.id\n" +
        "INNER JOIN Alue a\n" +
        "ON k.alue_id = a.id\n" +
        "GROUP BY a.id\n" +
        "ORDER BY a.nimi ASC");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Integer viestienLukumaara = rs.getInt("lkm");
            Integer viimeisin = rs.getInt("viimeisin");
            alueet.add(new Alue(id, nimi, viestienLukumaara, viimeisin));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }


    @Override
    public void delete(Integer key) throws SQLException {
    }
    
}
