package bookstore.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "product_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product_id")
    private String productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductEntity() {
    }

    public ProductEntity(String productId) {
        this.productId = productId;
    }
}