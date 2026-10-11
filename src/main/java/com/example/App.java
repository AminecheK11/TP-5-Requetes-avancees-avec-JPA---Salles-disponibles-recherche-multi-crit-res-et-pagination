package com.example;

import com.example.model.Salle;
import com.example.model.Reservation;
import com.example.service.SalleService;
import com.example.service.SalleServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SallePU");
        EntityManager em = emf.createEntityManager();

        SalleService salleService = new SalleServiceImpl();
        // Injection manuelle pour ce test
        ((SalleServiceImpl) salleService).setEntityManager(em);

        em.getTransaction().begin();

        Salle salleA = new Salle();
        salleA.setNom("Salle A");
        salleA.setCapacite(20);
        em.persist(salleA);

        Salle salleB = new Salle();
        salleB.setNom("Salle B");
        salleB.setCapacite(30);
        em.persist(salleB);

        Salle salleC = new Salle();
        salleC.setNom("Salle C");
        salleC.setCapacite(50);
        em.persist(salleC);

        Reservation reservation = new Reservation();
        reservation.setSalle(salleA);
        reservation.setDebut(LocalDateTime.of(2024, 6, 1, 9, 30));
        reservation.setFin(LocalDateTime.of(2024, 6, 1, 10, 30));
        em.persist(reservation);

        em.getTransaction().commit();

        LocalDateTime debut = LocalDateTime.of(2024, 6, 1, 9, 0);
        LocalDateTime fin = LocalDateTime.of(2024, 6, 1, 11, 0);

        List<Salle> sallesDisponibles = salleService.getAvailableRooms(debut, fin);
        System.out.println("Salles disponibles entre " + debut + " et " + fin + ":");
        for (Salle salle : sallesDisponibles) {
            System.out.println("- " + salle.getNom() + " (Capacité: " + salle.getCapacite() + ")");
        }

        System.out.println("\nRecherche multi-critères (capacité >= 30) - Page 1 :");

        List<Salle> sallesPage = salleService.searchRooms(
                null,
                30,
                debut,
                fin,
                1,
                1
        );

        for (Salle salle : sallesPage) {
            System.out.println("- " + salle.getNom() + " (Capacité: " + salle.getCapacite() + ")");
        }

        long total = salleService.countRooms(null, 30, debut, fin);
        System.out.println("Nombre total de salles correspondantes : " + total);
        System.out.println("Taille de la page : 1");

        em.close();
        emf.close();
    }
}