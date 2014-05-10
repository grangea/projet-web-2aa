package models;

import java.util.*;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import play.db.jpa.*;

/**
 * Created with IntelliJ IDEA.
 * User: lepeteil
 */

@Entity
public class Ville extends Model {
    @Expose
    public String nom;
    @Expose
    public int codePostal;

    public Ville(String nom, int codePostal) {
        this.nom = nom;
        this.codePostal = codePostal;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", codePostal=" + codePostal +
                '}';
    }
}
