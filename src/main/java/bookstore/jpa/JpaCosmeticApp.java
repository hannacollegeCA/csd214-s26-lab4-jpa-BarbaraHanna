package bookstore.jpa;

import bookstore.entities.MoisturizerEntity;
import bookstore.entities.SerumEntity;
import bookstore.pojos.Moisturizer;
import bookstore.pojos.Serum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Part C: Standalone CRUD Testing (JpaCosmeticApp)
// Standalone JPA test harness for the Cosmetic niche hierarchy.
// Demonstrates Create, Read, Update (Dirty Checking), and Delete
// using JPA EntityManager and Hibernate.


public class JpaCosmeticApp {

    private EntityManagerFactory emf;

    public JpaCosmeticApp() {
        //Start JPA using the persistence.xml config
        emf = Persistence.createEntityManagerFactory("bookstore-jpa");
    }

    // CREATE a Moisturizer in the database.
    // I convert the DTO to an Entity and call persist()
    public void createMoisturizer(Moisturizer moisturizer) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(moisturizer.toEntity()); // Hibernate does the INSERT
            em.getTransaction().commit();
        } finally {
            em.close(); //always close to avoid connection leaks
        }
    }

    // I convert the DTO to an Entity and call persist()
    public void createSerum(Serum serum) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(serum.toEntity()); //Hibernate does the INSERT
            em.getTransaction().commit();
        } finally {
            em.close(); //always close to avoid connection leaks
        }
    }


    // READ a Moisturizer by ID
    // use em.find() and then convert the Entity back to a DTO
    public Moisturizer readMoisturizer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            MoisturizerEntity entity = em.find(MoisturizerEntity.class, id);
            return (entity != null) ? Moisturizer.fromEntity(entity) : null;
        } finally {
            em.close();
        }
    }

    // READ a Serum by ID
    public Serum readSerum(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            SerumEntity entity = em.find(SerumEntity.class, id);
            return (entity != null) ? Serum.fromEntity(entity) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE (Dirty Checking)
    //Dirty Checking: I just change the fields on the managed entity
    // Hibernate will automatically run UPDATE when I commit()
    public void updateMoisturizer(Moisturizer moisturizer) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            MoisturizerEntity entity = em.find(MoisturizerEntity.class, moisturizer.getDbId());
            if (entity != null) {
                entity.setSkinType(moisturizer.getSkinType());
                entity.setPrice(moisturizer.getPrice());
                entity.setOilFree(moisturizer.isOilFree());
            }

            em.getTransaction().commit(); // triggers UPDATE
        } finally {
            em.close();
        }
    }

    public void updateSerum(Serum serum) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            SerumEntity entity = em.find(SerumEntity.class, serum.getDbId());
            if (entity != null) {
                entity.setSkinType(serum.getSkinType());
                entity.setPrice(serum.getPrice());
                entity.setActiveIngredient(serum.getActiveIngredient());
            }

            em.getTransaction().commit(); // triggers UPDATE
        } finally {
            em.close();
        }
    }

    // DELETE a Moisturizer by ID
    public void deleteMoisturizer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            MoisturizerEntity entity = em.find(MoisturizerEntity.class, id);
            if (entity != null) {
                em.remove(entity); // Hibernate does the DELETE
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // DELETE a Serum by ID
    public void deleteSerum(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            SerumEntity entity = em.find(SerumEntity.class, id);
            if (entity != null) {
                em.remove(entity); // Hibernate does the DELETE
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
