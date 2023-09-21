package mart.fresh.com.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private int refreshTokenId;
	
	@Column(name = "refresh_token")
    private String refreshToken;
	
	@Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;
	
	@OneToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;
}
