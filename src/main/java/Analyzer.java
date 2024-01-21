import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class Analyzer {


    void minFlightDuration(List<Flight> list) {
        Map<String, Duration> finalMap = new HashMap<>();

        for (Flight flight : list) {
            Duration duration = getDuration(flight);
            String carrier = flight.getCarrier();

            if (finalMap.containsKey(carrier)) {
                Duration currentDuration = finalMap.get(carrier);

                if (duration.compareTo(currentDuration) < 0) {
                    finalMap.put(carrier, duration);
                }
            } else {
                finalMap.put(carrier, duration);
            }
        }

        finalMap.forEach((carrier, duration) -> {
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            System.out.println("Перевозчик " + carrier + ". Минимальное время полета: " + hours + " часов " + minutes + " минут");
        });
    }

    void calculatePriceDifference(List<Flight> list) {
        List<Integer> prices = new ArrayList<>();
        list.forEach(flight -> prices.add(flight.getPrice()));
        double meanPrice = calculateMean(prices);
        double medianPrice = calculateMedian(prices);
        System.out.println("Разница между средней ценой и медианой: " + (meanPrice - medianPrice));
    }

    private double calculateMean(List<Integer> prices) {
        double sum = 0;
        for (Integer price : prices) {
            sum += price;
        }
        return sum / prices.size();
    }

    private double calculateMedian(List<Integer> prices) {
        Collections.sort(prices);
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }

    private Duration getDuration(Flight flight) {

            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("[H:mm][h:mm]")
                    .toFormatter();

            LocalDateTime departure = LocalDateTime.ofInstant(flight.getDepartureDate().toInstant(), ZoneId.systemDefault())
                    .with(LocalTime.parse(flight.getDepartureTime(), formatter));

            LocalDateTime arrival = LocalDateTime.ofInstant(flight.getArrivalDate().toInstant(), ZoneId.systemDefault())
                    .with(LocalTime.parse(flight.getArrivalTime(), formatter));

            return Duration.between(departure, arrival);
    }
}

