package mart.fresh.com.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_banner_image")
    private String eventBannerImage;

    @Column(name = "event_detail_image")
    private String eventDetailImage;

    @Column(name = "event_start_date")
    private Timestamp eventStartDate;

    @Column(name = "event_end_date")  
    private Timestamp eventEndDate;
}