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
import tikape.runko.domain.Keskustelu;


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
        Integer alue_id = rs.getInt("alue_id");
        String alueen_nimi = rs.getString("alueen_nimi");

        Keskustelu k = new Keskustelu(id, nimi, alue_id, alueen_nimi);
        k.setViestit(viestiDao.findForConversation(id));
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
            Integer alue_id = rs.getInt("alue_id");
            String alueenNimi = rs.getString("alueen_nimi");
            Keskustelu k = new Keskustelu(id, nimi, alue_id, alueenNimi);
            keskustelut.add(k);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }
    
    
    public List<Keskustelu> findForSection(Integer alue) throws SQLException {
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
        "GROUP BY v.keskustelu_id\n" +
        "ORDER BY viimeisin DESC\n" +
        "LIMIT 10;");
        stmt.setObject(1, alue);
        
        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Integer viestienLukumaara = rs.getInt("lkm");
            Integer viimeisin = rs.getInt("viimeisin");
            Keskustelu k = new Keskustelu(id, nimi, viestienLukumaara, viimeisin);
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
    
}