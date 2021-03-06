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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Utils;

/**
 *
 * @author mikkoruuskanen
 */
public class AlueDao implements Dao<Alue, Integer> {
    
    private Database database;
    private KeskusteluDao keskusteluDao;

    public AlueDao(Database database, KeskusteluDao keskusteluDao) {
        this.database = database;
        this.keskusteluDao = keskusteluDao;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Alue a = new Alue(id, nimi);
        a.setKeskustelut(keskusteluDao.findForSection(a));
        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

        
    @Override
    public List<Alue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT \n" +
        "a.id, \n" +
        "a.nimi,\n" +
        "COUNT(v.id) as lkm,\n" +
        "MAX(v.aikaleima) as viimeisin\n" +
        "FROM Alue a\n" +
        "LEFT JOIN Keskustelu k \n" +
        "ON k.alue_id = a.id\n" +
        "LEFT JOIN Viesti v\n" +
        "ON v.keskustelu_id = k.id\n" +
        "GROUP BY a.id\n" +
        "ORDER BY a.nimi ASC");

        ResultSet rs = stmt.executeQuery();
        
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Integer viestienLukumaara = rs.getInt("lkm");
            Long viimeisin = rs.getLong("viimeisin");
            alueet.add(new Alue(id, nimi, viestienLukumaara, Utils.getDateFromLong(viimeisin)));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }


    @Override
    public void delete(Integer key) throws SQLException {
    }

    @Override
    public Integer createOne(Alue alue) throws SQLException {
        Connection connection = database.getConnection();
   
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue (nimi) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setObject(1, alue.getNimi());
    
        stmt.execute();
                
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1); 
   
        stmt.close();
        connection.close();
        
        return id;
    }
    
}
