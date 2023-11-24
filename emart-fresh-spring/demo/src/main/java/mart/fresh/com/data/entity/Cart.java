package mart.fresh.com.data.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
	public Cart(int cartId, Member member, Store store) {
		super();
		this.cartId = cartId;
		this.member = member;
		this.store = store;
	}
}