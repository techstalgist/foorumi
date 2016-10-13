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
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Utils;


/**
 *
 * @author mikkoruuskanen
 */
public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private ViestiDao viestiDao;
    
    public KeskusteluDao(Database database, ViestiDao viestiDao) {
        this.database = database;
        this.viestiDao = viestiDao;
    }
    
    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT k.id, k.nimi, a.id as alue_id, a.nimi as alueen_nimi FROM Keskustelu k \n"
                + "INNER JOIN Alue a ON k.alue_id = a.id WHERE k.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        Integer alueId = rs.getInt("alue_id");
        String alueenNimi = rs.getString("alueen_nimi");
        
        Alue a = new Alue(alueId, alueenNimi);
        Keskustelu k = new Keskustelu(id, nimi, a);
        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
    
    public Keskustelu findOneWithMessages(Integer key, Integer page) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT k.id, k.nimi, a.id as alue_id, a.nimi as alueen_nimi FROM Keskustelu k \n"
                + "INNER JOIN Alue a ON k.alue_id = a.id WHERE k.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        Integer alueId = rs.getInt("alue_id");
        String alueenNimi = rs.getString("alueen_nimi");
        
        Alue a = new Alue(alueId, alueenNimi);
        Keskustelu k = new Keskustelu(id, nimi, a);
        k.setViestit(viestiDao.findForConversation(k, page));
        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
    
    

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT k.id, k.nimi, a.id as alue_id, a.nimi as alueen_nimi FROM Keskustelu k INNER JOIN Alue a ON k.alue_id = a.id");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Integer alueId = rs.getInt("alue_id");
            String alueenNimi = rs.getString("alueen_nimi");
            Alue a = new Alue(alueId, alueenNimi);
            Keskustelu k = new Keskustelu(id, nimi, a);
            keskustelut.add(k);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }
    
    
    public List<Keskustelu> findForSection(Alue a) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT \n" +
        "k.id, \n" +
        "k.nimi,\n" +
        "COUNT(*) as lkm,\n" +
        "MAX(aikaleima) as viimeisin\n" +
        "FROM Keskustelu k\n" +
        "INNER JOIN Viesti v\n" +
        "ON v.keskustelu_id = k.id\n" +
        "INNER JOIN Alue a\n" +
        "ON k.alue_id = a.id\n" +
        "WHERE \n" +
        "a.id = ?\n" +
        "GROUP BY k.id\n" +
        "ORDER BY viimeisin DESC\n" +
        "LIMIT 10;");
        stmt.setObject(1, a.getId());
        
        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Integer viestienLukumaara = rs.getInt("lkm");
            Long viimeisin = rs.getLong("viimeisin");
            Keskustelu k = new Keskustelu(id, nimi, a, viestienLukumaara, Utils.getDateFromLong(viimeisin));
            keskustelut.add(k);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }

    @Override
    public void delete(Integer key) throws SQLException {
    }

    @Override
    public Integer createOne(Keskustelu keskustelu) throws SQLException {
        Connection connection = database.getConnection();
   
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu (nimi, alue_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setObject(1, keskustelu.getNimi());
        stmt.setObject(2, keskustelu.getAlue().getId());
    
        stmt.execute();
                
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1); 
   
        stmt.close();
        connection.close();
        
        return id;
    }
    
}
