package TP4.univ.paris13.modelMybatis;

public class Adresse {
    private Integer id;
    private Integer numero;
    private String rue;
    private String codePostal;
    private String ville;
    private String pays;

    // Constructeurs
    public Adresse() {}

    public Adresse(Integer numero, String rue, String codePostal, String ville, String pays) {
        this.numero = numero;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    // Méthode utilitaire
    public String getAdresseComplete() {
        return (numero != null ? numero : "") + " " +
                (rue != null ? rue : "") + ", " +
                (codePostal != null ? codePostal : "") + " " +
                (ville != null ? ville : "") + ", " +
                (pays != null ? pays : "");
    }

    @Override
    public String toString() {
        return "Adresse{id=" + id +
                ", numero=" + numero +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' + '}';
    }
}