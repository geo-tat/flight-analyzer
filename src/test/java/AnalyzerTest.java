import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyzerTest {
    private Analyzer analyzer;
    private List<Flight> flights;

    @BeforeEach
    void setUp() {
        analyzer = new Analyzer();
        Flight flight1 = createFlight("Carrier1", "Vladivostok", "Tel-Aviv", "10.01.22", "10:00", "10.01.22", "12:00", 2, 200);
        Flight flight2 = createFlight("Carrier1", "Vladivostok", "Tel-Aviv", "10.01.22", "13:00", "10.01.22", "15:00", 1, 150);
        Flight flight3 = createFlight("Carrier2", "Vladivostok", "Tel-Aviv", "10.01.22", "11:00", "10.01.22", "16:00", 0, 100);
        flights = Arrays.asList(flight1, flight2, flight3);

    }

    private Flight createFlight(String carrier, String origin, String destination, String departureDate,
                                String departureTime, String arrivalDate, String arrivalTime, int stops, int price) {
        Flight flight = new Flight();
        flight.setCarrier(carrier);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setDepartureDate(parseDate(departureDate));
        flight.setDepartureTime(departureTime);
        flight.setArrivalDate(parseDate(arrivalDate));
        flight.setArrivalTime(arrivalTime);
        flight.setStops(stops);
        flight.setPrice(price);
        return flight;
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yy").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMinFlightDuration() {

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        analyzer.minFlightDuration(flights);

        String actualOutput = outputStreamCaptor.toString().trim();

        String expectedOutput = "Перевозчик Carrier1. Минимальное время полета: 2 часов 0 минут" + System.lineSeparator() +
                "Перевозчик Carrier2. Минимальное время полета: 5 часов 0 минут";
        assertLinesMatch(expectedOutput.lines(), actualOutput.lines());
        System.setOut(System.out);
    }

    @Test
    public void testCalculatePriceDifference() {

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        analyzer.calculatePriceDifference(flights);
        System.setOut(System.out);
        assertEquals("Разница между средней ценой и медианой: 0.0", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testSamePrices() {
        Flight flight1 = createFlight("S7", "Vladivostok", "Tel-Aviv", "10.01.22", "10:00", "10.01.22", "12:00", 2, 200);
        Flight flight2 = createFlight("S7", "Vladivostok", "Tel-Aviv", "10.01.22", "13:00", "10.01.22", "15:00", 1, 200);
        Flight flight3 = createFlight("S7", "Vladivostok", "Tel-Aviv", "10.01.22", "16:00", "10.01.22", "18:00", 3, 200);
        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        analyzer.calculatePriceDifference(flights);

        assertEquals("Разница между средней ценой и медианой: 0.0", outputStreamCaptor.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testSameDurations() {
        Flight flight1 = createFlight("S7", "Vladivostok", "Tel-Aviv", "10.01.22", "10:00", "10.01.22", "12:00", 2, 200);
        Flight flight2 = createFlight("S7", "Vladivostok", "Tel-Aviv", "10.01.22", "13:00", "10.01.22", "15:00", 1, 200);
        Flight flight3 = createFlight("Fly Emirates", "Vladivostok", "Tel-Aviv", "10.01.22", "16:00", "10.01.22", "18:00", 3, 200);
        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        analyzer.minFlightDuration(flights);

        String expectedOutput = "Перевозчик Fly Emirates. Минимальное время полета: 2 часов 0 минут" + System.lineSeparator() +
                "Перевозчик S7. Минимальное время полета: 2 часов 0 минут";

        assertLinesMatch(expectedOutput.lines(), outputStreamCaptor.toString().trim().lines());
        System.setOut(System.out);
    }

    @Test
    void testValidationNoVladivostok() {
        Flight flight1 = createFlight("S7", "Moscow", "Tel-Aviv", "10.01.22", "10:00", "10.01.22", "12:00", 2, 200);
        Flight flight2 = createFlight("S7", "Moscow", "Tel-Aviv", "10.01.22", "13:00", "10.01.22", "15:00", 1, 200);
        Flight flight3 = createFlight("Fly Emirates", "Moscow", "Tel-Aviv", "10.01.22", "16:00", "10.01.22", "18:00", 3, 200);
        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);
        FlightList flightList = new FlightList();
        flightList.setTickets(flights);
        List<Flight> testResult = analyzer.validateRoute(flightList);
        assertEquals(0, testResult.size());
    }

}
