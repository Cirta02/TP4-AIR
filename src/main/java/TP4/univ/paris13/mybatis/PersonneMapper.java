package TP4.univ.paris13.mybatis;

import TP4.univ.paris13.model.Personne;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PersonneMapper {

    // INSERT SIMPLIFIÉ sans id_adresse_travail
    @Insert("INSERT INTO personne (nom, prenom, tel, mail, id_adresse) " +
            "VALUES (#{nom}, #{prenom}, #{tel}, #{mail}, #{idAdresse})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Personne personne);

    @Select("SELECT * FROM personne WHERE id = #{id}")
    Personne findById(Integer id);

    @Select("SELECT * FROM personne")
    List<Personne> findAll();
    @Select("SELECT p.*, a.id as \"adresse.id\", a.numero as \"adresse.numero\", " +
            "a.rue as \"adresse.rue\", a.code_postal as \"adresse.codePostal\", " +
            "a.ville as \"adresse.ville\", a.pays as \"adresse.pays\" " +
            "FROM personne p LEFT JOIN adresse a ON p.id_adresse = a.id " +
            "WHERE p.id = #{id}")

    Personne findWithAdresse(Integer id);

    @Select("SELECT p.*, a.id as \"adresse.id\", a.numero as \"adresse.numero\", " +
            "a.rue as \"adresse.rue\", a.code_postal as \"adresse.codePostal\", " +
            "a.ville as \"adresse.ville\", a.pays as \"adresse.pays\" " +
            "FROM personne p INNER JOIN adresse a ON p.id_adresse = a.id " +
            "WHERE a.ville = #{ville}")
    List<Personne> findPersonnesByVille(String ville);

    @Select("SELECT DISTINCT p.* FROM personne p " +
            "INNER JOIN personne_adresse_visite pav ON p.id = pav.personne_id")
    List<Personne> findWithAdressesVisitees();

    @Insert("INSERT INTO personne_adresse_visite (personne_id, adresse_id) " +
            "VALUES (#{personneId}, #{adresseId})")
    void insertAdresseVisitee(@Param("personneId") Integer personneId,
                              @Param("adresseId") Integer adresseId);

    @Update("UPDATE personne SET nom=#{nom}, prenom=#{prenom}, tel=#{tel}, " +
            "mail=#{mail}, id_adresse=#{idAdresse} " +
            "WHERE id=#{id}")
    void update(Personne personne);

    @Delete("DELETE FROM personne WHERE id=#{id}")
    void delete(Integer id);
}