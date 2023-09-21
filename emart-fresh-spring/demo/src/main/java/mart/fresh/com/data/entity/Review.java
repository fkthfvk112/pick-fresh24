package mart.fresh.com.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "review_date")
    private Timestamp reviewDate;

    @Column(name = "review_score")
    private int reviewScore;

}