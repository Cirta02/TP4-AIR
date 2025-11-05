package TP4.univ.paris13.mybatis;

import TP4.univ.paris13.model.Adresse;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdresseMapper {

    @Insert("INSERT INTO adresse (numero, rue, code_postal, ville, pays) " +
            "VALUES (#{numero}, #{rue}, #{codePostal}, #{ville}, #{pays})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Adresse adresse);

    @Select("SELECT * FROM adresse WHERE id = #{id}")
    Adresse findById(Integer id);

    @Select("SELECT * FROM adresse")
    List<Adresse> findAll();

    @Select("SELECT DISTINCT a.* FROM adresse a " +
            "INNER JOIN personne p ON a.id = p.id_adresse " +
            "WHERE p.id IS NOT NULL")
    List<Adresse> findWithPersonnes();

    @Select("SELECT * FROM adresse WHERE ville = #{ville}")
    List<Adresse> findByVille(String ville);

    @Update("UPDATE adresse SET numero=#{numero}, rue=#{rue}, code_postal=#{codePostal}, " +
            "ville=#{ville}, pays=#{pays} WHERE id=#{id}")
    void update(Adresse adresse);

    @Delete("DELETE FROM adresse WHERE id=#{id}")
    void delete(Integer id);
}