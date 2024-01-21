
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MyApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите путь к файлу JSON в качестве аргумента командной строки.");
            return;
        }
          String jsonPath = args[0];
      //        File file = new File("src/main/resources/tickets.json");
              File filePath = new File(jsonPath);

        Analyzer analyzer = new Analyzer();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FlightList flightsArray = objectMapper.readValue(filePath, FlightList.class);

            List<Flight> list = flightsArray.getTickets()
                    .stream()
                    .filter(flight -> flight.getOriginName().equals("Владивосток"))
                    .filter(flight -> flight.getDestinationName().equals("Тель-Авив"))
                    .collect(Collectors.toList());
            analyzer.minFlightDuration(list);
            analyzer.calculatePriceDifference(list);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
