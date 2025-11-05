package TP4.univ.paris13.modelMybatis;

public class Personne {
    private Integer id;
    private String nom;
    private String prenom;
    private String tel;
    private String mail;
    private Integer idAdresse;
    private Integer idAdresseTravail;

    // Relation Many-to-One (pour les jointures)
    private Adresse adresse;           // Pour la jointure
    private Adresse adresseTravail;    // Pour la jointure

    // Constructeurs
    public Personne() {}

    public Personne(String nom, String prenom, String tel, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.mail = mail;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public Integer getIdAdresse() { return idAdresse; }
    public void setIdAdresse(Integer idAdresse) { this.idAdresse = idAdresse; }

    public Integer getIdAdresseTravail() { return idAdresseTravail; }
    public void setIdAdresseTravail(Integer idAdresseTravail) { this.idAdresseTravail = idAdresseTravail; }

    // GETTERS POUR LES JOINTURES - AJOUTEZ CES DEUX MÉTHODES
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }

    public Adresse getAdresseTravail() { return adresseTravail; }
    public void setAdresseTravail(Adresse adresseTravail) { this.adresseTravail = adresseTravail; }

    // Méthode utilitaire
    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom : "");
    }

    @Override
    public String toString() {
        return "Personne{id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", mail='" + mail + '\'' +
                ", idAdresse=" + idAdresse +
                ", idAdresseTravail=" + idAdresseTravail + '}';
    }
}