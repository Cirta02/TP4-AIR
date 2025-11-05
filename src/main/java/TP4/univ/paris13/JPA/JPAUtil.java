package TP4.univ.paris13.JPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitaire pour JPA (remplace HibernateUtil)
 *  EntityManagerFactory = équivalent de SessionFactory dans Hibernate
 *  EntityManager = équivalent de Session dans Hibernate
 */
public class JPAUtil {

    // Nom de l'unité de persistance (défini dans persistence.xml)
    private static final String PERSISTENCE_UNIT_NAME = "TP4-PU";

    //  Factory pour créer des EntityManager (coûteux à créer, donc on le fait une fois)
    private static EntityManagerFactory entityManagerFactory;

    //  Bloc static : exécuté une fois au chargement de la classe
    static {
        try {
            System.out.println(" Création de l'EntityManagerFactory...");
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println(" EntityManagerFactory créé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'EntityManagerFactory:");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 🔹 Crée un nouvel EntityManager (équivalent d'une Session Hibernate)
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     *  Ferme l'EntityManagerFactory (à appeler à la fin de l'application)
     */
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory fermé.");
        }
    }

    /**
     * Retourne l'EntityManagerFactory (pour usage avancé)
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}