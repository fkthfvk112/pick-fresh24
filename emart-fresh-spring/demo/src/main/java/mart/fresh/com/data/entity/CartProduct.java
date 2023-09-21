package mart.fresh.com.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "cart_product")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id")
    private int cartProductId;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "cart_product_quantity")
    private int cartProductQuantity;
}