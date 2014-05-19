package controllers;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import models.*;
import oauth.signpost.http.HttpRequest;
import org.apache.commons.lang.StringUtils;
import play.*;
import play.mvc.*;
import org.apache.commons.*;

import java.util.*;

import models.*;
import com.google.gson.*;
import play.test.Fixtures;

public class Application extends Controller {

    /*---------------------Initialisation de la base--------------------*/
    public static void initBase() {
        Fixtures.deleteDatabase();

        Ville v1 = new Ville("Annecy", "74000").save();
        Ville v2 = new Ville("Gap", "05000").save();
        Ville v3 = new Ville("Marseille", "13000").save();
        Ville v4 = new Ville("Grenoble", "38000").save();
        Ville v5 = new Ville("Lyon", "69000").save();

        Membre m1 = new Membre("jack", "pom", "123456", 23, "al@clu.fr", "M").save();
        Membre m2 = new Membre("harry", "ruse", "123456", 22, "lg@clu.fr", "M").save();
        Membre m3 = new Membre("mel", "soun", "123456", 20, "yl@clu.fr", "F").save();

        Parcours p1 = new Parcours(m1, v1, v2, 12, 1,14,00).save();
        Parcours p2 = new Parcours(m2, v5, v3, 14, 2,13,50).save();
        Parcours p3 = new Parcours(m3, v4, v1, 15, 3,8,15).save();
        Parcours p4 = new Parcours(m1, v1, v4, 16, 1,22,18).save();
        Parcours p5 = new Parcours(m2, v2, v5, 17, 2,14,17).save();
        Parcours p6 = new Parcours(m3, v3, v1, 18, 3,17,30).save();

        p1.ajouterMembreInscrit(m2);

        p2.ajouterMembreInscrit(m1);
        p2.ajouterMembreInscrit(m3);

        p3.ajouterMembreInscrit(m1);
        p3.ajouterMembreInscrit(m2);

        p4.ajouterMembreInscrit(m2);

        p5.ajouterMembreInscrit(m1);

    }

    /*----------------Affichage des pages coté public -----------------*/
    public static void index() {
        render();
    }

    public static void conduire() {
        render();
    }

    public static void sefaireconduire() {
        render();
    }

    public static void nous() {
        render();
    }

    public static void contact() {
        render();
    }
    /*---------------Methodes contenu des pages -----------------------*/

    public static void tousLesParcours() {
        List<Parcours> listp = Parcours.findAll();
        JSONSerializer serializer = new JSONSerializer();
        renderJSON(serializer.exclude("*.class").exclude("createur").transform(new DateTransformer("yyyy/MM/dd hh:mm:ss"),
                "dateParcours").serialize(listp));
    }

    public static void chercherParcours() {
        //TODO Gerer la recherche avec la date
        String depart = params.get("depart");
        String arrivee = params.get("arrivee");
        String date = params.get("date");

        List<Parcours> listp = null;

        if(depart.matches("[0-9]+") && arrivee.matches("[0-9]+")){
            //2 code postaux
            listp = Parcours.find("depart.codePostal like ? and arrivee.codePostal like ?  ",
                    "%"+ depart+"%",
                    "%"+ arrivee+"%").fetch();
        }
        else{
            //2 villes
            listp = Parcours.find("depart.nom like ? and arrivee.nom like ?  ",
                    "%"+ StringUtils.capitalize(depart)+"%",
                    "%"+StringUtils.capitalize(arrivee)+"%").fetch();
        }

        JSONSerializer serializer = new JSONSerializer();
        renderJSON(serializer.exclude("*.class").exclude("createur").transform(new DateTransformer("yyyy/MM/dd hh:mm:ss"), "dateParcours").serialize(listp));

    }

    public static void toutesLesVilles() {
        List<Ville> listv = Ville.findAll();
        JSONSerializer serializer = new JSONSerializer();
        renderJSON(serializer.exclude("*.class").serialize(listv));
    }

    public static void sinscrire(String nom, String prenom, int age, String email, String mdp, String sexe) {
        new Membre(nom, prenom, mdp, age, email, sexe).save();
        Application.index();
    }

    public static void seconnecter() {
        Membre m = Membre.find("byNom", "harry").first();

        if (m != null) {
            session.put("idUser", m.id);
            session.put("nameUser", m.nom);
            session.put("fnameUser", m.prenom);
            Utilisateur.index();
        } else {
            Application.index();
        }
    }

    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        sc.nextInt(radix);
        return !sc.hasNext();
    }

}