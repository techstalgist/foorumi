package tikape.runko.domain;

import java.util.Date;

public class Viesti {

    private Integer id;
    private String sisalto;
    private String lahettaja;
    private Keskustelu keskustelu;
    private Date aikaleima;
    private Integer viestinNumero;

    public Viesti(Integer id, String sisalto, String lahettaja, 
                  Keskustelu keskustelu, Date aikaleima) {
        this.id = id;
        this.sisalto = sisalto;
        this.lahettaja = lahettaja;
        this.keskustelu = keskustelu;
        this.aikaleima = aikaleima;
        this.viestinNumero = 0;
    }
    
    public Viesti(Integer id, String sisalto, String lahettaja, 
                  Date aikaleima) {
        this(id, sisalto, lahettaja, null, aikaleima);
    }
    
    public Viesti(String sisalto, String lahettaja, Keskustelu keskustelu) {
        this(null, sisalto, lahettaja, keskustelu, new Date());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setViestinNumero(Integer nro) {
        this.viestinNumero = nro;
    }
    
    public Integer getViestinNumero() {
        return viestinNumero;
    }


    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }
    
    public void setLahettaja(String lahettaja) {
        this.lahettaja = lahettaja;
    }
    
    public String getLahettaja() {
        return lahettaja;
    }
    
    public void setKeskustelu(Keskustelu keskustelu) {
        this.keskustelu = keskustelu;
    }
    
    public Keskustelu getKeskustelu() {
        return keskustelu;
    }
    
    public Date getAikaleima() {
        return aikaleima;
    }
}
