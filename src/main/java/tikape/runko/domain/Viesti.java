package tikape.runko.domain;

public class Viesti {

    private Integer id;
    private String sisalto;

    public Viesti(Integer id, String sisalto) {
        this.id = id;
        this.sisalto = sisalto;
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

}
