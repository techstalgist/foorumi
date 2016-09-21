package tikape.runko.domain;

public class Viesti {

    private Integer id;
    private String sisalto;
    private String lahettaja;
    private Integer keskustelu_id;
    private Integer aikaleima;

    public Viesti(Integer id, String sisalto, String lahettaja, 
                  Integer keskustelu_id, Integer aikaleima) {
        
        this.id = id;
        this.sisalto = sisalto;
        this.lahettaja = lahettaja;
        this.keskustelu_id = keskustelu_id;
        this.aikaleima = aikaleima;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }
    
    public String getLahettaja() {
        return lahettaja;
    }
    
    public Integer getKeskustelu_id() {
        return keskustelu_id;
    }
    
    public Integer getAikaleima() {
        return aikaleima;
    }
}
