
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

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


        File filePath = new File(jsonPath);

        Analyzer analyzer = new Analyzer();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FlightList flightsArray = objectMapper.readValue(filePath, FlightList.class);

            List<Flight> list = analyzer.validateRoute(flightsArray);
            analyzer.minFlightDuration(list);
            analyzer.calculatePriceDifference(list);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
