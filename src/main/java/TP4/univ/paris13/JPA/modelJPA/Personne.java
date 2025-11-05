package TP4.univ.paris13.JPA.modelJPA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personne")
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "tel")
    private String tel;

    @Column(name = "mail")
    private String mail;

    // Relation ManyToOne (habitation)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse")
    private Adresse adresse;

    // Relation OneToOne (adresse de travail)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_adresse_travail")
    private Adresse adresseTravail;

    // Relation ManyToMany (adresses visitées)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "personne_adresse_visite",
            joinColumns = @JoinColumn(name = "personne_id"),
            inverseJoinColumns = @JoinColumn(name = "adresse_id")
    )
    private List<Adresse> adressesVisitees = new ArrayList<>();

    // CONSTRUCTEURS
    public Personne() {}

    public Personne(String nom, String prenom, String tel, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.mail = mail;
    }

    public Personne(String nom, String prenom, String tel, String mail, Adresse adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.mail = mail;
        this.adresse = adresse;
    }

    // GETTERS et SETTERS pour les propriétés de base
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // GETTERS et SETTERS pour les relations

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Adresse getAdresseTravail() {
        return adresseTravail;
    }

    public void setAdresseTravail(Adresse adresseTravail) {
        this.adresseTravail = adresseTravail;
    }

    public List<Adresse> getAdressesVisitees() {
        return adressesVisitees;
    }

    public void setAdressesVisitees(List<Adresse> adressesVisitees) {
        this.adressesVisitees = adressesVisitees;
    }

    // MÉTHODES UTILITAIRES POUR LA GESTION DES RELATIONS

    /**
     * Ajoute une adresse visitée et maintient la cohérence bidirectionnelle
     */
    public void addAdresseVisitee(Adresse adresse) {
        if (!this.adressesVisitees.contains(adresse)) {
            this.adressesVisitees.add(adresse);
        }
        if (!adresse.getPersonnesVisiteurs().contains(this)) {
            adresse.getPersonnesVisiteurs().add(this);
        }
    }

    /**
     * Retire une adresse visitée et maintient la cohérence bidirectionnelle
     */
    public void removeAdresseVisitee(Adresse adresse) {
        this.adressesVisitees.remove(adresse);
        adresse.getPersonnesVisiteurs().remove(this);
    }

    /**
     * Définit l'adresse d'habitation et maintient la cohérence bidirectionnelle
     */
    public void setAdresseAvecCohérence(Adresse nouvelleAdresse) {
        // Retire cette personne de l'ancienne adresse
        if (this.adresse != null) {
            this.adresse.getPersonnes().remove(this);
        }

        // Met à jour la référence
        this.adresse = nouvelleAdresse;

        // Ajoute cette personne à la nouvelle adresse
        if (nouvelleAdresse != null && !nouvelleAdresse.getPersonnes().contains(this)) {
            nouvelleAdresse.getPersonnes().add(this);
        }
    }

    // MÉTHODES UTILITAIRES

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getInfosCompletes() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNomComplet())
                .append(" | Tél: ").append(tel)
                .append(" | Mail: ").append(mail);

        if (adresse != null) {
            sb.append(" | Habite: ").append(adresse.getVille());
        }

        if (adresseTravail != null) {
            sb.append(" | Travail: ").append(adresseTravail.getVille());
        }

        if (!adressesVisitees.isEmpty()) {
            sb.append(" | Visites: ").append(adressesVisitees.size()).append(" adresses");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return getNomComplet() + " - " +
                (adresse != null ? adresse.toString() : "Pas d'adresse");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personne)) return false;
        Personne personne = (Personne) o;
        return id != null && id.equals(personne.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}