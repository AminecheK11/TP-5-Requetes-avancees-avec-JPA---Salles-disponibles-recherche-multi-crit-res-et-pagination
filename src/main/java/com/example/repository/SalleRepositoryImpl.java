package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SalleRepositoryImpl implements SalleRepository {

    @PersistenceContext(unitName = "SallePU")
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Salle> findAvailableRooms(LocalDateTime debut, LocalDateTime fin) {

        String jpql = "SELECT s FROM Salle s WHERE s.id NOT IN (" +
                "SELECT r.salle.id FROM Reservation r WHERE " +
                "(:debut < r.fin AND :fin > r.debut)" +
                ")";

        return entityManager.createQuery(jpql, Salle.class)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList();
    }

    @Override
    public List<Salle> searchRooms(String nom, Integer capaciteMin,
                                   LocalDateTime debut, LocalDateTime fin,
                                   int page, int size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Salle> cq = cb.createQuery(Salle.class);
        Root<Salle> salle = cq.from(Salle.class);

        List<Predicate> predicates = new ArrayList<>();

        // Critère nom
        if (nom != null && !nom.isEmpty()) {
            predicates.add(
                    cb.like(
                            cb.lower(salle.get("nom")),
                            "%" + nom.toLowerCase() + "%"
                    )
            );
        }

        // Critère capacité minimale
        if (capaciteMin != null) {
            predicates.add(cb.ge(salle.get("capacite"), capaciteMin));
        }

        // Critère disponibilité
        if (debut != null && fin != null) {

            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Reservation> reservation = subquery.from(Reservation.class);

            subquery.select(reservation.get("salle").get("id"));

            Predicate chevauchement = cb.and(
                    cb.lessThan(reservation.get("debut"), fin),
                    cb.greaterThan(reservation.get("fin"), debut)
            );

            subquery.where(chevauchement);

            predicates.add(cb.not(salle.get("id").in(subquery)));
        }

        cq.select(salle)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Salle> query = entityManager.createQuery(cq);

        // Pagination
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countRooms(String nom, Integer capaciteMin,
                           LocalDateTime debut, LocalDateTime fin) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Salle> salle = cq.from(Salle.class);

        List<Predicate> predicates = new ArrayList<>();

        // Critère nom
        if (nom != null && !nom.isEmpty()) {
            predicates.add(
                    cb.like(
                            cb.lower(salle.get("nom")),
                            "%" + nom.toLowerCase() + "%"
                    )
            );
        }

        // Critère capacité minimale
        if (capaciteMin != null) {
            predicates.add(cb.ge(salle.get("capacite"), capaciteMin));
        }

        // Critère disponibilité
        if (debut != null && fin != null) {

            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Reservation> reservation = subquery.from(Reservation.class);

            subquery.select(reservation.get("salle").get("id"));

            Predicate chevauchement = cb.and(
                    cb.lessThan(reservation.get("debut"), fin),
                    cb.greaterThan(reservation.get("fin"), debut)
            );

            subquery.where(chevauchement);

            predicates.add(cb.not(salle.get("id").in(subquery)));
        }

        cq.select(cb.count(salle))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getSingleResult();
    }
}