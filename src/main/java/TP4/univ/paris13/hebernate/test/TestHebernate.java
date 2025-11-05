package TP4.univ.paris13.hebernate.test;

import TP4.univ.paris13.hebernate.HebernateUtil;
import TP4.univ.paris13.hebernate.model.Personne;
import org.hibernate.*;
import java.util.List;

public class TestHebernate {
    public static void main(String[] args) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HebernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // === OPTIMISATION : UTILISER JOIN FETCH ===
            System.out.println("=== AVEC JOIN FETCH (OPTIMISÉ) ===");

            String hqlOptimise = "SELECT p FROM Personne p JOIN FETCH p.adresse";
            List<Personne> personnesOptimise = session.createQuery(hqlOptimise, Personne.class).list();

            System.out.println("Nombre de personnes chargées: " + personnesOptimise.size());
            System.out.println("✅ SEULEMENT 1 REQUÊTE SQL !");

            for (Personne p : personnesOptimise) {
                System.out.println(" - " + p.getPrenom() + " " + p.getNom() +
                        " (" + p.getAdresse().getVille() + ")");
            }

            // === COMPARAISON : VERSION NON OPTIMISÉE ===
            System.out.println("\n=== SANS JOIN FETCH (NON OPTIMISÉ) ===");

            transaction.commit();
            transaction = session.beginTransaction(); // Nouvelle transaction

            List<Personne> personnesNonOptimise = session.createQuery("FROM Personne", Personne.class).list();

            System.out.println("Nombre de personnes chargées: " + personnesNonOptimise.size());
            System.out.println("  " + (personnesNonOptimise.size() + 1) + " REQUÊTES SQL !");

            // Juste pour montrer le problème, on accède aux adresses
            for (Personne p : personnesNonOptimise) {
                // Cet accès déclenche une requête SQL supplémentaire
                System.out.println(" - " + p.getPrenom() + " " + p.getNom() +
                        " (" + p.getAdresse().getVille() + ")");
            }

            transaction.commit();
            System.out.println("\n COMPARAISON TERMINÉE !");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            HebernateUtil.shutdown();
        }
    }
}