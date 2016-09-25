/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Keskustelu {
    
    private Integer id;
    private String nimi;
    private Integer alueID;
    private String alueenNimi;
    private List<Viesti> viestit;
    private Integer viestienLukumaara;
    private Date viimeisin;
    
    public Keskustelu(Integer id, String nimi, Integer alueID, String alueenNimi) {
        this.id = id;
        this.nimi = nimi;
        this.alueID = alueID;
        this.alueenNimi = alueenNimi;
        this.viestit = new ArrayList<>();
    }
    
    public Keskustelu(Integer id, String nimi, Integer viestienLukumaara, Integer viimeisin) {
        this.id = id;
        this.nimi = nimi;
        this.viestienLukumaara = viestienLukumaara;
        this.viimeisin = new Date(viimeisin*1000L);
    }
    
    public void setViestit(List<Viesti> viestit) {
        this.viestit = viestit;
    }

    public List<Viesti> getViestit () {
        int nro = 1;
        for(Viesti viesti : viestit) {
            viesti.setViestinNumero(nro);
            nro++;
        }
        return viestit;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }
    
    public Integer getAlueID() {
        return alueID;
    }
    
    public Integer getViestienLukumaara() {
        return viestienLukumaara;
    }
    
    public Date getViimeisin() {
        return viimeisin;
    }
    
    public String getAlueenNimi() {
        return alueenNimi;
    }
    
}
