package TP4.univ.paris13.JPA.testJPA;

import TP4.univ.paris13.JPA.JPAUtil;
import TP4.univ.paris13.hebernate.model.Personne;
import TP4.univ.paris13.hebernate.model.Adresse;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

public class TestJPA{

    public static void main(String[] args) {
        System.out.println("🚀 DÉBUT DU TEST JPA COMPLET");

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            // === 1. INITIALISATION JPA ===
            entityManager = JPAUtil.getEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            // === 2. CRÉATION DES DONNÉES ===
            System.out.println("\n📝 CRÉATION DES DONNÉES AVEC JPA");
            creerDonneesTest(entityManager);
            transaction.commit();

            // === 3. TEST DES 3 TYPES DE REQUÊTES ===
            System.out.println("\n🔍 TEST DES 3 TYPES DE REQUÊTES JPA");

            // Requête 1: JPQL
            System.out.println("\n--- 1. REQUÊTE JPQL ---");
            List<Personne> personnesJPQL = getPersonnesParAdresseJPQL(entityManager, "Paris", "75001");
            afficherPersonnes(personnesJPQL, "JPQL");

            // Requête 2: Criteria API
            System.out.println("\n--- 2. REQUÊTE CRITERIA API ---");
            List<Personne> personnesCriteria = getPersonnesParAdresseCriteria(entityManager, "Paris", "75001");
            afficherPersonnes(personnesCriteria, "Criteria API");

            // Requête 3: SQL Native
            System.out.println("\n--- 3. REQUÊTE SQL NATIVE ---");
            List<Personne> personnesNative = getPersonnesParAdresseNative(entityManager, "Paris", "75001");
            afficherPersonnes(personnesNative, "SQL Native");

            // === 4. TEST DES RELATIONS ===
            System.out.println("\n🔗 TEST DES RELATIONS JPA");
            testerRelations(entityManager);

            // === 5. TEST DES MÉTHODES DE RECHERCHE ===
            System.out.println("\n🔎 TEST DES MÉTHODES DE RECHERCHE");
            testerMethodesRecherche(entityManager); // ✅ CORRECTION : on passe entityManager

        } catch (Exception e) {
            System.err.println("❌ Erreur pendant l'exécution:");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
            JPAUtil.close();
        }

        System.out.println("\n FIN DU TEST JPA COMPLET");
    }

    // === MÉTHODE 1: CRÉATION DES DONNÉES ===
    private static void creerDonneesTest(EntityManager em) {
        // Création des adresses
        Adresse adresseMaison = new Adresse(15, "Rue de la République", "75001", "Paris", "France");
        Adresse adresseTravail = new Adresse(42, "Avenue des Champs-Élysées", "75008", "Paris", "France");
        Adresse adresseVacances = new Adresse(3, "Rue de Lyon", "69001", "Lyon", "France");
        Adresse adresseClient = new Adresse(10, "Boulevard Haussmann", "75009", "Paris", "France");

        // Sauvegarde avec JPA (au lieu de session.save())
        em.persist(adresseMaison);
        em.persist(adresseTravail);
        em.persist(adresseVacances);
        em.persist(adresseClient);

        // Création des personnes
        Personne p1 = new Personne("Dupont", "Jean", "01 23 45 67 89", "jean.dupont@email.com");
        Personne p2 = new Personne("Martin", "Marie", "01 34 56 78 90", "marie.martin@email.com");
        Personne p3 = new Personne("Bernard", "Pierre", "01 45 67 89 01", "pierre.bernard@email.com");

        // Configuration des relations avec les nouvelles méthodes
        p1.setAdresse(adresseMaison);
        p2.setAdresse(adresseMaison);
        p3.setAdresse(adresseVacances);

        p1.setAdresseTravail(adresseTravail);
        p2.setAdresseTravail(adresseClient);

        p1.addAdresseVisitee(adresseClient);
        p1.addAdresseVisitee(adresseVacances);
        p2.addAdresseVisitee(adresseClient);
        p3.addAdresseVisitee(adresseMaison);

        // Sauvegarde avec JPA
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        System.out.println("✅ 4 adresses et 3 personnes créées avec JPA");
    }

    // === MÉTHODE 2: REQUÊTE JPQL ===
    private static List<Personne> getPersonnesParAdresseJPQL(EntityManager em, String ville, String codePostal) {
        String jpql = "SELECT p FROM Personne p " +
                "JOIN FETCH p.adresse a " +
                "WHERE a.ville = :ville AND a.codePostal = :codePostal";

        TypedQuery<Personne> query = em.createQuery(jpql, Personne.class);
        query.setParameter("ville", ville);
        query.setParameter("codePostal", codePostal);

        return query.getResultList();
    }

    // === MÉTHODE 3: REQUÊTE CRITERIA API ===
    private static List<Personne> getPersonnesParAdresseCriteria(EntityManager em, String ville, String codePostal) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);
        Root<Personne> personne = cq.from(Personne.class);

        Join<Personne, Adresse> adresse = personne.join("adresse");

        Predicate conditionVille = cb.equal(adresse.get("ville"), ville);
        Predicate conditionCodePostal = cb.equal(adresse.get("codePostal"), codePostal);

        cq.select(personne)
                .where(cb.and(conditionVille, conditionCodePostal));

        return em.createQuery(cq).getResultList();
    }

    // === MÉTHODE 4: REQUÊTE SQL NATIVE ===
    private static List<Personne> getPersonnesParAdresseNative(EntityManager em, String ville, String codePostal) {
        String sql = "SELECT p.* FROM personne p " +
                "INNER JOIN adresse a ON p.id_adresse = a.id " +
                "WHERE a.ville = ? AND a.code_postal = ?";

        Query query = em.createNativeQuery(sql, Personne.class);
        query.setParameter(1, ville);
        query.setParameter(2, codePostal);

        return query.getResultList();
    }

    // === MÉTHODE 5: TEST DES RELATIONS ===
    private static void testerRelations(EntityManager em) {
        // Test ManyToOne/OneToMany
        System.out.println("--- ManyToOne/OneToMany ---");
        String jpqlHabitants = "SELECT a FROM Adresse a LEFT JOIN FETCH a.personnes WHERE a.ville = 'Paris'";
        TypedQuery<Adresse> query = em.createQuery(jpqlHabitants, Adresse.class);
        List<Adresse> adressesParis = query.getResultList();

        for (Adresse a : adressesParis) {
            System.out.println(" " + a.getAdresseComplete() +
                    " (" + a.getPersonnes().size() + " habitants)");
        }

        // Test OneToOne
        System.out.println("\n--- OneToOne (Adresse travail) ---");
        String jpqlTravail = "SELECT p FROM Personne p WHERE p.adresseTravail IS NOT NULL";
        TypedQuery<Personne> queryTravail = em.createQuery(jpqlTravail, Personne.class);
        List<Personne> personnesAvecTravail = queryTravail.getResultList();

        for (Personne p : personnesAvecTravail) {
            System.out.println(" " + p.getNomComplet() +
                    " travaille à " + p.getAdresseTravail().getVille());
        }

        // Test ManyToMany
        System.out.println("\n--- ManyToMany (Adresses visitées) ---");
        String jpqlVisites = "SELECT DISTINCT p FROM Personne p JOIN FETCH p.adressesVisitees WHERE SIZE(p.adressesVisitees) > 0";
        TypedQuery<Personne> queryVisites = em.createQuery(jpqlVisites, Personne.class);
        List<Personne> personnesAvecVisites = queryVisites.getResultList();

        for (Personne p : personnesAvecVisites) {
            System.out.println(" " + p.getNomComplet() +
                    " a visité " + p.getAdressesVisitees().size() + " adresses");
            for (Adresse a : p.getAdressesVisitees()) {
                System.out.println("   🏢 " + a.getVille());
            }
        }
    }

    // === MÉTHODE 6: TEST DES MÉTHODES DE RECHERCHE (CORRIGÉE) ===
    private static void testerMethodesRecherche(EntityManager em) {
        System.out.println("--- Recherche par ID (entityManager.find) ---");

        // Recherche d'une personne par ID (équivalent de session.get())
        Personne personne = em.find(Personne.class, 1);
        if (personne != null) {
            System.out.println("✅ Trouvé avec find(): " + personne.getNomComplet());
        }

        System.out.println("\n--- Recherche avec getReference (chargement lazy) ---");
        // getReference ne charge pas immédiatement (optimisation)
        Personne personneRef = em.getReference(Personne.class, 2);
        System.out.println("✅ Référence obtenue pour: " + personneRef.getNomComplet());

        System.out.println("\n--- Test de mise à jour ---");
        // ✅ CORRECTION : Créer une nouvelle transaction locale
        EntityTransaction transactionUpdate = em.getTransaction();
        transactionUpdate.begin();
        personne.setTel("01 99 88 77 66");
        // Pas besoin de appeler update() - JPA détecte les changements automatiquement
        transactionUpdate.commit();
        System.out.println("✅ Téléphone mis à jour automatiquement");

        System.out.println("\n--- Test de suppression ---");
        // ✅ CORRECTION : Créer une nouvelle transaction locale
        EntityTransaction transactionDelete = em.getTransaction();
        transactionDelete.begin();
        Personne personneASupprimer = em.find(Personne.class, 3);
        if (personneASupprimer != null) {
            em.remove(personneASupprimer);
            System.out.println("✅ Personne supprimée: " + personneASupprimer.getNomComplet());
        }
        transactionDelete.commit();
    }

    // === MÉTHODE 7: AFFICHAGE ===
    private static void afficherPersonnes(List<Personne> personnes, String typeRequete) {
        System.out.println("📋 " + typeRequete + " - " + personnes.size() + " résultats:");
        for (Personne p : personnes) {
            System.out.println("    " + p.getNomComplet() +
                    " | Tél: " + p.getTel() +
                    " | Habite: " + p.getAdresse().getVille());
        }
    }
}