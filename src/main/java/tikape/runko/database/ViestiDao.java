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
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;
import tikape.runko.domain.Utils;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String sisalto = rs.getString("sisalto");
        String lahettaja = rs.getString("lahettaja");
        Long aikaleima = rs.getLong("aikaleima");

        Viesti o = new Viesti(id, sisalto, lahettaja, Utils.getDateFromLong(aikaleima));

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String sisalto = rs.getString("sisalto");
            String lahettaja = rs.getString("lahettaja");
            Long aikaleima = rs.getLong("aikaleima");

            viestit.add(new Viesti(id, sisalto, lahettaja, Utils.getDateFromLong(aikaleima)));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public List<Viesti> findForConversation(Keskustelu k, Integer sivu) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE keskustelu_id = ? LIMIT 10 OFFSET ?");
        stmt.setObject(1, k.getId());
        stmt.setObject(2, (sivu-1)*10);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String sisalto = rs.getString("sisalto");
            String lahettaja = rs.getString("lahettaja");
            Long aikaleima = rs.getLong("aikaleima");

            viestit.add(new Viesti(id, sisalto, lahettaja, k, Utils.getDateFromLong(aikaleima)));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
 
    
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    @Override
    public Integer createOne(Viesti viesti) throws SQLException {
        Connection connection = database.getConnection();
   
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti (sisalto, lahettaja, keskustelu_id, aikaleima) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setObject(1, viesti.getSisalto());
        stmt.setObject(2, viesti.getLahettaja());
        stmt.setObject(3, viesti.getKeskustelu().getId());
        stmt.setObject(4, Utils.getLongFromDate(viesti.getAikaleima()));
    
        stmt.execute();
                
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1); 
   
        stmt.close();
        connection.close();
        
        return id;
    }
    
    

}
