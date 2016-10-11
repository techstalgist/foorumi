/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alue {
    
    private Integer id;
    private String nimi;
    private Integer viestienLukumaara;
    private Date viimeisin;
    private List<Keskustelu> keskustelut;
    
    public Alue(Integer id, String nimi, Integer viestienLukumaara, Date viimeisin) {
        this.id = id;
        this.nimi = nimi;
        if (viestienLukumaara != null) {
            this.viestienLukumaara = viestienLukumaara;
        }
        if (viimeisin != null && viimeisin.getTime() > 0) {
            this.viimeisin = viimeisin;
        }
        this.keskustelut = new ArrayList<>();
    }
    
    public Alue(Integer id, String nimi) {
        this(id, nimi, null, null);
    }
    
    public Alue(String nimi) {
        this(null, nimi, null, null);
    }
    
    public void setKeskustelut(List<Keskustelu> keskustelut) {
        this.keskustelut = keskustelut;
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

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public Integer getViestienLukumaara() {
        return viestienLukumaara;
    }
    
    public Date getViimeisin() {
        return viimeisin;
    }
    
    public List<Keskustelu> getKeskustelut() {
        return keskustelut;
    }

    
}
