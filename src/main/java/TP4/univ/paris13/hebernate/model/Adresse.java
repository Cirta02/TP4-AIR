package TP4.univ.paris13.hebernate.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "rue", length = 255, nullable = false)
    private String rue;

    @Column(name = "code_postal", length = 10, nullable = false)
    private String codePostal;

    @Column(name = "ville", length = 100, nullable = false)
    private String ville;

    @Column(name = "pays", length = 100, nullable = false)
    private String pays;

    // === RELATION OneToMany (habitation) ===
    @OneToMany(mappedBy = "adresse",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Personne> personnes = new ArrayList<>();

    // === RELATION ManyToMany (côté inverse) ===
    @ManyToMany(mappedBy = "adressesVisitees", fetch = FetchType.LAZY)
    private List<Personne> personnesVisiteurs = new ArrayList<>();

    // Constructeur par défaut (obligatoire pour JPA)
    public Adresse() {}

    // Constructeur avec paramètres
    public Adresse(Integer numero, String rue, String codePostal, String ville, String pays) {
        this.numero = numero;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
    }

    // === MÉTHODES UTILITAIRES POUR LA GESTION DES RELATIONS ===

    /**
     * Ajoute une personne à cette adresse (habitation) et maintient la cohérence bidirectionnelle
     */
    public void addPersonne(Personne personne) {
        personnes.add(personne);
        personne.setAdresse(this);
    }

    /**
     * Retire une personne de cette adresse (habitation) et maintient la cohérence bidirectionnelle
     */
    public void removePersonne(Personne personne) {
        personnes.remove(personne);
        personne.setAdresse(null);
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public List<Personne> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<Personne> personnes) {
        this.personnes = personnes;
    }

    public List<Personne> getPersonnesVisiteurs() {
        return personnesVisiteurs;
    }

    public void setPersonnesVisiteurs(List<Personne> personnesVisiteurs) {
        this.personnesVisiteurs = personnesVisiteurs;
    }

    // Méthodes utilitaires
    public String getAdresseComplete() {
        return numero + " " + rue + ", " + codePostal + " " + ville + ", " + pays;
    }

    @Override
    public String toString() {
        return getAdresseComplete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adresse)) return false;
        Adresse adresse = (Adresse) o;
        return id != null && id.equals(adresse.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}