package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.ViestiDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        ViestiDao viestiDao = new ViestiDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/viestit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.findAll());

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        get("/viestit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", viestiDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "viesti");
        }, new ThymeleafTemplateEngine());
    }
}
