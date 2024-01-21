import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Flight {
    private String origin;
    @JsonProperty("origin_name")
    private String originName;
    private String destination;
    @JsonProperty("destination_name")
    private String destinationName;
    @JsonProperty("departure_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private Date departureDate;
    @JsonProperty("departure_time")
    private String departureTime;
    @JsonProperty("arrival_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private Date arrivalDate;
    @JsonProperty("arrival_time")
    private String arrivalTime;

    private String carrier;
    private int stops;
    private int price;
}

