package TP4.univ.paris13.JPA.modelJPA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "rue", nullable = false)
    private String rue;

    @Column(name = "code_postal", nullable = false)
    private String codePostal;

    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "pays", nullable = false)
    private String pays;

    // Relation OneToMany (habitants)
    @OneToMany(mappedBy = "adresse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Personne> personnes = new ArrayList<>();

    // Relation ManyToMany (côté inverse)
    @ManyToMany(mappedBy = "adressesVisitees", fetch = FetchType.LAZY)
    private List<Personne> personnesVisiteurs = new ArrayList<>();

    // CONSTRUCTEURS
    public Adresse() {}

    public Adresse(Integer numero, String rue, String codePostal, String ville, String pays) {
        this.numero = numero;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
    }

    // GETTERS et SETTERS
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

    // MÉTHODES UTILITAIRES
    public String getAdresseComplete() {
        return numero + " " + rue + ", " + codePostal + " " + ville + ", " + pays;
    }

    /**
     * Ajoute une personne à cette adresse (habitation) et maintient la cohérence bidirectionnelle
     */
    public void addPersonne(Personne personne) {
        if (!this.personnes.contains(personne)) {
            this.personnes.add(personne);
        }
        if (personne.getAdresse() != this) {
            personne.setAdresse(this);
        }
    }

    /**
     * Retire une personne de cette adresse (habitation) et maintient la cohérence bidirectionnelle
     */
    public void removePersonne(Personne personne) {
        this.personnes.remove(personne);
        if (personne.getAdresse() == this) {
            personne.setAdresse(null);
        }
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