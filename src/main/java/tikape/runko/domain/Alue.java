/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.Date;

public class Alue {
    
    private Integer id;
    private String nimi;
    private Integer viestienLukumaara;
    private Date viimeisin;
    
    public Alue(Integer id, String nimi, Integer viestienLukumaara, Integer viimeisin) {
        this.id = id;
        this.nimi = nimi;
        this.viestienLukumaara = viestienLukumaara;
        this.viimeisin = new Date(viimeisin*1000L);
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
}
