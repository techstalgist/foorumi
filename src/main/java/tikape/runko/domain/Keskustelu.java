/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

public class Keskustelu {
    
    private Integer id;
    private String nimi;
    private Integer alue_id;
    
    public Keskustelu(Integer id, String nimi, Integer alue_id) {
        this.id = id;
        this.nimi = nimi;
        this.alue_id = alue_id;
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
    
    public Integer getAlueId() {
        return alue_id;
    }
    
}
