package mart.fresh.com.data.dto;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class SearchDto {

    private int searchId;
    private Timestamp searchDate;
    private String searchContent;
}