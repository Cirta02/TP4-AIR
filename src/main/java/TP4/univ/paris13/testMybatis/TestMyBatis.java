package TP4.univ.paris13.testMybatis;

import TP4.univ.paris13.mapper.MyBatisUtil;
import TP4.univ.paris13.mybatis.PersonneMapper;
import TP4.univ.paris13.mybatis.AdresseMapper;
import TP4.univ.paris13.model.Personne;
import TP4.univ.paris13.model.Adresse;
import org.apache.ibatis.session.SqlSession;

public class TestMyBatis {

    public static void main(String[] args) {
        System.out.println("🚀 TEST MYBATIS 100% ANNOTATIONS");

        SqlSession sqlSession = null;

        try {
            sqlSession = MyBatisUtil.getSqlSession();
            System.out.println("✅ Session MyBatis créée");

            PersonneMapper personneMapper = sqlSession.getMapper(PersonneMapper.class);
            AdresseMapper adresseMapper = sqlSession.getMapper(AdresseMapper.class);
            System.out.println("✅ Mappers initialisés");

            // Test 1: Lister les adresses existantes
            System.out.println("\n=== TEST 1: Liste des adresses ===");
            var adresses = adresseMapper.findAll();
            System.out.println("Adresses trouvées: " + adresses.size());
            for (Adresse a : adresses) {
                System.out.println(" - " + a.getVille() + " (" + a.getId() + ")");
            }

            // Test 2: Lister les personnes existantes
            System.out.println("\n=== TEST 2: Liste des personnes ===");
            var personnes = personneMapper.findAll();
            System.out.println("Personnes trouvées: " + personnes.size());
            for (Personne p : personnes) {
                System.out.println(" - " + p.getPrenom() + " " + p.getNom() + " (adresse: " + p.getIdAdresse() + ")");
            }

            // Test 3: Créer une nouvelle adresse
            System.out.println("\n=== TEST 3: Création adresse ===");
            Adresse nouvelleAdresse = new Adresse();
            nouvelleAdresse.setNumero(123);
            nouvelleAdresse.setRue("Rue MyBatis");
            nouvelleAdresse.setCodePostal("75001");
            nouvelleAdresse.setVille("Paris");
            nouvelleAdresse.setPays("France");

            adresseMapper.insert(nouvelleAdresse);
            System.out.println("✅ Nouvelle adresse créée avec ID: " + nouvelleAdresse.getId());

            // Test 4: Créer une nouvelle personne
            System.out.println("\n=== TEST 4: Création personne ===");
            Personne nouvellePersonne = new Personne();
            nouvellePersonne.setNom("Dupont");
            nouvellePersonne.setPrenom("Jean");
            nouvellePersonne.setTel("01 23 45 67 89");
            nouvellePersonne.setMail("jean.dupont@email.com");
            nouvellePersonne.setIdAdresse(nouvelleAdresse.getId());

            personneMapper.insert(nouvellePersonne);
            System.out.println("✅ Nouvelle personne créée avec ID: " + nouvellePersonne.getId());

            // Test 5: Lecture avec jointure
            System.out.println("\n=== TEST 5: Lecture avec jointure ===");
            Personne personneAvecAdresse = personneMapper.findWithAdresse(nouvellePersonne.getId());
            if (personneAvecAdresse != null) {
                if (personneAvecAdresse.getIdAdresse() != null) {
                    System.out.println("✅ Jointure réussie: " +
                            personneAvecAdresse.getPrenom() + " " + personneAvecAdresse.getNom() +
                            " habite à " + personneAvecAdresse.getIdAdresse().intValue());
                } else {
                    System.out.println("ℹ️  Personne trouvée mais adresse non chargée");
                    System.out.println("ℹ️  " + personneAvecAdresse.getPrenom() + " " +
                            personneAvecAdresse.getNom() + " (adresse ID: " +
                            personneAvecAdresse.getIdAdresse() + ")");
                }
            } else {
                System.out.println("❌ Personne non trouvée");
            }

            sqlSession.commit();
            System.out.println("\n🎉 TOUS LES TESTS MYBATIS RÉUSSIS !");

        } catch (Exception e) {
            System.err.println("❌ Erreur MyBatis: " + e.getMessage());
            if (sqlSession != null) {
                sqlSession.rollback();
                System.out.println("🔁 Transaction annulée");
            }
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                System.out.println("🔒 Session fermée");
            }
        }
    }
}