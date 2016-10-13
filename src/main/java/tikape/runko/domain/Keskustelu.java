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
    private Alue alue;
    private Date viimeisin;
    private List<Viesti> viestit;
    private Integer viestienLukumaara;
    
    public Keskustelu(Integer id, String nimi, Alue alue, Integer viestienLukumaara, Date viimeisin) {
        this.id = id;
        this.nimi = nimi;
        this.alue = alue;
        if (viimeisin != null) {
            this.viimeisin = viimeisin;
        }
        this.viestit = new ArrayList<>();
        if (viestienLukumaara != null) {
            this.viestienLukumaara = viestienLukumaara;
        }
    }
    
    public Keskustelu(Integer id, String nimi, Integer viestienLukumaara, Date viimeisin) {
        this(id, nimi, null, viestienLukumaara, viimeisin);
    }
    
    public Keskustelu(Integer id, String nimi, Alue alue) {
        this(id, nimi, alue, null, null);
    }
    
    public void setViestit(List<Viesti> viestit) {
        this.viestit = viestit;
    }

    public List<Viesti> getViestit (int sivu) {
        // jos sivu on 1, aloitetaan numerointi yhdestä. sivulla 2 numerointi alkaa yhdestätoista.
        int nro = (sivu-1)*10+1;
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
    
    public Alue getAlue() {
        return alue;
    }
    
    public Integer getViestienLukumaara() {
        return viestienLukumaara;
    }
    
    public Date getViimeisin() {
        return viimeisin;
    }
    
    public int haeEdellinenSivu(int sivu) {
        if(sivu-1 == 0) {
            return sivu;
        } else {
            return sivu - 1;
        }
    }
    
    public int haeSeuraavaSivu(int sivu) {
        if(getViestit(sivu).size() < 10) {
            return sivu;
        } else {
            return sivu + 1;
        }
    }
    
    
    
}
