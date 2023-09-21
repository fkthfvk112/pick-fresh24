package mart.fresh.com.data.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "apply_manager")
public class ApplyManager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "apply_manager_id")
	private int applyManagerId;
	
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_applied")
    private boolean isApplied;

    @Column(name = "apply_date")
    private Timestamp applyDate;
}