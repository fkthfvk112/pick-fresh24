package mart.fresh.com.data.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "store_id")
    private Store store;
}