package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KeskusteluDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;


public class Main {

    public static void main(String[] args) throws Exception {
       
        initialize();
        
        Database database = getDatabase();
        database.init();

        ViestiDao viestiDao = new ViestiDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database, viestiDao);
        AlueDao alueDao = new AlueDao(database, keskusteluDao);
       
        get("/", (req, res) -> {
            
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        get("/uusialue", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "uusiAlue");
        }, new ThymeleafTemplateEngine());

               
        post("/uusialue", (req, res) -> {
            
            String nimi = req.queryParams("nimi");
            
            if (nimi.length()== 0) {
                res.redirect("/uusialue");
                return null;
            }
          
            Alue uusiAlue = new Alue(nimi);
            int uudenId = alueDao.createOne(uusiAlue);
            
            res.redirect("/alueet/" + uudenId);
            
            return null;
        });
        
        get("/alueet/:id/uusikeskustelu", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alue",  alueDao.findOne(Integer.parseInt(req.params("id"))));
            return new ModelAndView(map, "uusiKeskustelu");
        }, new ThymeleafTemplateEngine());

               
        post("/alueet/:id/uusikeskustelu", (req, res) -> {
            
            Alue alue = alueDao.findOne(Integer.parseInt(req.params("id")));
            String keskustelunNimi = req.queryParams("nimi");
            String viestinSisalto = req.queryParams("sisalto");
            String lahettaja = req.queryParams("lahettaja");
            
            if (keskustelunNimi.length()== 0 || viestinSisalto.length() == 0 ||
                    lahettaja.length() == 0) {
                // Ei luoda uutta keskustelua, jos joku puuttuu
                res.redirect("/alueet/" + alue.getId() + "/uusikeskustelu");
                return null;
            }
           
            // Luo keskustelu
            Keskustelu uusiKeskustelu = new Keskustelu(null, keskustelunNimi, alue);
            int uudenKeskustelunId = keskusteluDao.createOne(uusiKeskustelu);
            uusiKeskustelu = keskusteluDao.findOne(uudenKeskustelunId);
            
            // Luo viesti
            Viesti uusiViesti = new Viesti(viestinSisalto, lahettaja, uusiKeskustelu);
            int uudenViestinId = viestiDao.createOne(uusiViesti);
            
            
            res.redirect("/keskustelut/" + uudenKeskustelunId);
            
            return null;
        });
        
        post("/keskustelut/:id", (req, res) -> {
            
            String sisalto = req.queryParams("sisalto");
            String lahettaja = req.queryParams("lahettaja");
            int keskusteluId = Integer.parseInt(req.params("id"));
            if (sisalto.length()== 0 || lahettaja.length() == 0) {
                res.redirect("/keskustelut/" + keskusteluId);
                return null;
            }
          
            Keskustelu keskustelu = keskusteluDao.findOne(keskusteluId);
            Viesti viesti = new Viesti(sisalto, lahettaja, keskustelu);
            viestiDao.createOne(viesti);
            
            res.redirect("/keskustelut/" + keskusteluId);
            
            return null;
        });
        
        get("/keskustelut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("keskustelu", keskusteluDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());
        
    }
    
    public static void initialize() {
         
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        // aseta sijainti css -tiedostoja varten
        staticFileLocation("/public");
    }
    
    public static Database getDatabase() throws ClassNotFoundException {
          // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:foorumi.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        return new Database(jdbcOsoite);
    }
}
