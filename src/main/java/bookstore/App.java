package bookstore;

import bookstore.entities.*;
import bookstore.pojos.*;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookstore-pu");
    private EntityManager em = emf.createEntityManager();
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);

    public void run() {
        populate();
        int choice = 0;
        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println(" 1. Add Items (JPA Persist)");
            System.out.println(" 2. Edit Items (JPA Merge)");
            System.out.println(" 3. Delete Items (JPA Remove)");
            System.out.println(" 4. Sell item(s) (Logic & DB Update)");
            System.out.println(" 5. List items (Polymorphic JPQL)");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: \n");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    editItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    sellItem();
                    break;
                case 5:
                    listAny();
                    break;
                case 99:
                    // Exit
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        em.close();
        emf.close();
    }

    public void addItem() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAdd an item\n");
            System.out.println("1. Add Book");
            System.out.println("2. Add Magazine");
            System.out.println("3. Add DiscMag");
            System.out.println("4. Add Ticket");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            Editable item = null;
            switch(choice) {
                case 1: item = new Book(); break;
                case 2: item = new Magazine(); break;
                case 3: item = new DiscMag(); break;
                case 4: item = new Ticket(); break;
                default: System.out.println("Invalid selection."); continue;
            }

            item.initialize(this.input);
            saveToDb(item);
        }
    }

    private void saveToDb(Editable item) {
        ProductEntity entity = null;
        if (item instanceof Book) {
            entity = ((Book) item).toEntity();
        } else if (item instanceof DiscMag) {
            entity = ((DiscMag) item).toEntity();
        } else if (item instanceof Magazine) {
            entity = ((Magazine) item).toEntity();
        } else if (item instanceof Ticket) {
            entity = ((Ticket) item).toEntity();
        }

        if (entity != null) {
            try {
                em.getTransaction().begin();
                em.persist(entity);
                em.getTransaction().commit();
                System.out.println("Successfully saved to Database via JPA!");
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.out.println("Error persisting transaction: " + e.getMessage());
            }
        }
    }

    public void listAny() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAll Items");
            System.out.println("-----------");
            System.out.println("List Options");
            System.out.println("1. All Products");
            System.out.println("2. Books Only");
            System.out.println("3. Magazines Only");
            System.out.println("4. DiscMags Only");
            System.out.println("5. Tickets Only");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            Class<? extends ProductEntity> filterClass = null;
            switch(choice) {
                case 1: filterClass = ProductEntity.class; break;
                case 2: filterClass = BookEntity.class; break;
                case 3: filterClass = MagazineEntity.class; break;
                case 4: filterClass = DiscMagEntity.class; break;
                case 5: filterClass = TicketEntity.class; break;
                default: System.out.println("Invalid selection."); continue;
            }

            String jpql = "SELECT p FROM ProductEntity p";
            if (filterClass != ProductEntity.class) {
                jpql = "SELECT p FROM " + filterClass.getSimpleName() + " p";
            }

            List<? extends ProductEntity> dbEntities = em.createQuery(jpql, filterClass).getResultList();
            for (ProductEntity entity : dbEntities) {
                // Prevent DiscMags showing up in pure Magazine listings
                if (filterClass == MagazineEntity.class && entity instanceof DiscMagEntity) {
                    continue;
                }
                printEntityAsDto(entity);
            }
        }
    }

    private void printEntityAsDto(ProductEntity entity) {
        if (entity instanceof BookEntity) {
            System.out.println(Book.fromEntity((BookEntity) entity));
        } else if (entity instanceof DiscMagEntity) {
            System.out.println(DiscMag.fromEntity((DiscMagEntity) entity));
        } else if (entity instanceof MagazineEntity) {
            System.out.println(Magazine.fromEntity((MagazineEntity) entity));
        } else if (entity instanceof TicketEntity) {
            System.out.println(Ticket.fromEntity((TicketEntity) entity));
        }
    }

    public void editItem() {
        List<ProductEntity> entities = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        if (entities.isEmpty()) {
            System.out.println("No records found to edit.");
            return;
        }

        System.out.println("Select item index to edit (0 to " + (entities.size() - 1) + "):");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                em.getTransaction().begin();

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    dto.edit(this.input);
                    em.merge(dto.toEntity());
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    dto.edit(this.input);
                    em.merge(dto.toEntity());
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    dto.edit(this.input);
                    em.merge(dto.toEntity());
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    dto.edit(this.input);
                    em.merge(dto.toEntity());
                }

                em.getTransaction().commit();
                System.out.println("Successfully merged updates to the database via JPA.");
            }
        } catch (Exception e) {
            System.out.println("Invalid selection or transaction error.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void deleteItem() {
        List<ProductEntity> entities = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        if (entities.isEmpty()) {
            System.out.println("No records found to delete.");
            return;
        }

        System.out.println("Select item index to delete:");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                em.getTransaction().begin();
                em.remove(entity);
                em.getTransaction().commit();
                System.out.println("Successfully deleted from DB via JPA.");
            }
        } catch (Exception e) {
            System.out.println("Deletion error.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void sellItem() {
        List<ProductEntity> entities = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        if (entities.isEmpty()) {
            System.out.println("No inventory found to sell.");
            return;
        }

        System.out.println("Select item index to sell:");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                em.getTransaction().begin();

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    cashTill.sellItem(dto);
                    em.merge(dto.toEntity()); // Decremented stock persisted
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    cashTill.sellItem(dto);
                    em.merge(dto.toEntity());
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    cashTill.sellItem(dto);
                    em.merge(dto.toEntity());
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    cashTill.sellItem(dto);
                    em.merge(dto.toEntity());
                }

                em.getTransaction().commit();
            }
        } catch (Exception e) {
            System.out.println("Transaction error during purchase.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void populate() {
        long count = em.createQuery("SELECT COUNT(p) FROM ProductEntity p", Long.class).getSingleResult();
        if (count > 0) {
            System.out.println("Database already seeded. Safe boot complete.");
            return;
        }

        System.out.println("Seeding database via JPA Hibernate mapping...");
        Faker faker = new Faker();
        em.getTransaction().begin();

        try {
            for (int i = 0; i < 2; i++) {
                // Book
                BookEntity b = new BookEntity(
                        faker.book().title(),
                        faker.number().randomDouble(2, 10, 50),
                        faker.number().numberBetween(1, 20),
                        faker.book().author()
                );
                em.persist(b);

                // Magazine
                MagazineEntity m = new MagazineEntity(
                        faker.book().title() + " Monthly",
                        faker.number().randomDouble(2, 5, 15),
                        faker.number().numberBetween(5, 50),
                        faker.number().numberBetween(100, 500),
                        faker.date().past(30, TimeUnit.DAYS)
                );
                em.persist(m);

                // DiscMag
                DiscMagEntity dm = new DiscMagEntity(
                        "Tech Disc: " + faker.app().name(),
                        faker.number().randomDouble(2, 10, 25),
                        faker.number().numberBetween(5, 30),
                        faker.number().numberBetween(50, 200),
                        faker.date().past(60, TimeUnit.DAYS),
                        faker.bool().bool()
                );
                em.persist(dm);

                // Ticket
                TicketEntity t = new TicketEntity();
                t.setDescription("Concert: " + faker.rockBand().name());
                t.setPrice(faker.number().randomDouble(2, 50, 150));
                em.persist(t);
            }
            em.getTransaction().commit();
            System.out.println("Seeding complete. Products persisted directly to MySQL disk volume.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Failure during database seeding: " + e.getMessage());
        }
    }

    public SaleableItem findItem(SaleableItem item) {
        // Query all persistent product entities from the database
        List<ProductEntity> entities = em.createQuery(
                "SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();

        for (ProductEntity entity : entities) {
            SaleableItem pojo = null;

            // Map the entity back to its corresponding POJO/DTO
            if (entity instanceof BookEntity) {
                pojo = Book.fromEntity((BookEntity) entity);
            } else if (entity instanceof DiscMagEntity) {
                pojo = DiscMag.fromEntity((DiscMagEntity) entity);
            } else if (entity instanceof MagazineEntity) {
                pojo = Magazine.fromEntity((MagazineEntity) entity);
            } else if (entity instanceof TicketEntity) {
                pojo = Ticket.fromEntity((TicketEntity) entity);
            }

            // Check logically if this database item matches the expected item
            if (pojo != null && pojo.equals(item)) {
                return pojo;
            }
        }
        return null;
    }

    public boolean findItemExists(SaleableItem item) {
        return findItem(item) != null;
    }

    public SaleableItem getItem(SaleableItem item) {
        return findItem(item);
    }
}
