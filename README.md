# TP 5 : Requêtes avancées avec JPA - Salles disponibles, recherche multi-critères et pagination

**Réalisé par : Amine Chekkouri**

## Objectif

Ce TP a pour objectif de mettre en œuvre des requêtes avancées avec JPA et Hibernate pour la gestion des salles et des réservations.

Les principales fonctionnalités réalisées sont :

- recherche des salles disponibles sur un créneau horaire ;
- recherche dynamique multi-critères avec Criteria API ;
- pagination des résultats ;
- organisation de l'application avec les couches Repository et Service ;
- utilisation d'une base de données H2 en mémoire.

## Technologies utilisées

- **Java** : développement de l'application.
- **JPA (Java Persistence API)** : gestion de la persistance et des entités.
- **Hibernate 5.4.30.Final** : implémentation de JPA et génération des requêtes SQL.
- **H2 Database 1.4.200** : base de données en mémoire utilisée pour les tests.
- **JPQL** : recherche des salles disponibles selon un créneau horaire.
- **Criteria API** : construction dynamique des recherches multi-critères.
- **Maven** : gestion des dépendances, compilation et exécution du projet.
- **IntelliJ IDEA** : environnement de développement utilisé pour réaliser le TP.

## 1. Structure du projet

Le projet est organisé en plusieurs couches afin de séparer les responsabilités : les entités JPA dans `model`, l'accès aux données dans `repository`, la logique métier dans `service` et la gestion de la pagination dans `util`.


<img width="356" height="443" alt="cap1_lab5" src="https://github.com/user-attachments/assets/3dfa3a16-2953-4d71-be8c-b1df6c6dee13" />

## 2. Recherche des salles disponibles

Une réservation a été créée pour la Salle A sur le créneau 09:30–10:30.

La recherche des salles disponibles entre 09:00 et 11:00 exclut donc la Salle A et retourne uniquement les salles B et C.

<img width="755" height="188" alt="cap2_lab5" src="https://github.com/user-attachments/assets/a1da6206-5c85-41f3-84a8-b255b9543de8" />


## 3. Recherche multi-critères et pagination

Une recherche a été effectuée avec une capacité minimale de 30 places et sur le même créneau horaire.

Deux salles correspondent aux critères. La pagination étant configurée avec une taille de page égale à 1, seule la Salle B est affichée sur la première page.

Le résultat confirme :

- Nombre total de salles correspondantes : 2
- Taille de la page : 1
- Résultat de la première page : Salle B

<img width="752" height="653" alt="cap3_lab5" src="https://github.com/user-attachments/assets/ecc5c602-c7ef-4a00-86b4-ad8ed6764c59" />

## Conclusion

Ce TP met en pratique les requêtes avancées avec JPA et Hibernate dans une application de gestion des salles. La recherche de disponibilité avec JPQL, la recherche dynamique avec Criteria API et la pagination côté serveur ont été implémentées et testées avec succès. L'utilisation des couches Repository et Service assure également une organisation claire et structurée de l'application.
