package tikape.runko;
//

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KeskusteluDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Alue;


public class Main {

    public static void main(String[] args) throws Exception {
        
         // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        ViestiDao viestiDao = new ViestiDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database, viestiDao);
        AlueDao alueDao = new AlueDao(database, keskusteluDao);
        
        staticFileLocation("/public");
        
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
                HashMap map = new HashMap<>();
                return new ModelAndView(map, "uusiAlue");
            }
          
            Alue uusiAlue = new Alue(nimi);
            int uudenId = alueDao.createOne(uusiAlue);
            
            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(uudenId));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        get("/keskustelut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("keskustelu", keskusteluDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());
        
        get("/stop", (req, res) -> {
            stop();
            
            return null;
        });
    }
}
