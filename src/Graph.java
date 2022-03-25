import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {

    // aeroports : Iata, name, city, country, longitude, latitude
    // vols : company name, source airport iata, destination airport iata
    private HashMap<String, Airport> airportWithIata = new HashMap<>();
    private HashMap<Airport, Set<Flight>> arcsSortants = new HashMap<>();
    private HashSet<Airport> airports = new HashSet<>();
    private ArrayList<Flight> flights = new ArrayList<>();

    public Graph(File file1, File file2) {
        try {
            String currentLineFile1;
            String[] attributesFile1;
            BufferedReader readerFile1 = new BufferedReader(new FileReader(file1));

            String currentLineFile2;
            String[] attributesFile2;
            BufferedReader readerFile2 = new BufferedReader(new FileReader(file2));

            while ((currentLineFile1 = readerFile1.readLine()) != null) {
                attributesFile1 = currentLineFile1.split("\\,");
                Airport a = new Airport();
                a.setIata(attributesFile1[0]);
                a.setName(attributesFile1[1]);
                a.setCity(attributesFile1[2]);
                a.setCountry(attributesFile1[3]);
                a.setLongitude(Double.parseDouble(attributesFile1[4]));
                a.setLatitude(Double.parseDouble(attributesFile1[5]));
                airportWithIata.put(a.getIata(), a);
                airports.add(a);
                arcsSortants.put(a, new HashSet<>());
            }

            while ((currentLineFile2 = readerFile2.readLine()) != null) {
                attributesFile2 = currentLineFile2.split("\\,");
                Flight f = new Flight();
                f.setCompanyName(attributesFile2[0]);
                f.setSourceIata(attributesFile2[1]);
                f.setDestinationIata(attributesFile2[2]);
                flights.add(f);
                Airport a = airportWithIata.get(f.getSourceIata());
                arcsSortants.get(a).add(f);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void calculerItineraireMinimisantNombreVol(String sourceIata, String destinationIata) {
        // Breadth-First search
        boolean foundRoute = (sourceIata == destinationIata);

        ArrayDeque<String> file = new ArrayDeque<>();
        file.add(sourceIata);

        HashMap<Airport, Flight> flightsHistory = new HashMap<>();
        HashSet<Airport> allVisitedAirports = new HashSet<>();
        allVisitedAirports.add(airportWithIata.get(sourceIata));

        while (!foundRoute && !file.isEmpty()) {
            String actualIata = file.pollFirst();
            Airport actualAirport = airportWithIata.get(actualIata);
            for (Flight f : arcsSortants.get(actualAirport)) {
                if (!allVisitedAirports.contains(airportWithIata.get(f.getDestinationIata()))) {
                    flightsHistory.put(airportWithIata.get(f.getDestinationIata()), f);
                    file.add(f.getDestinationIata());
                    allVisitedAirports.add(airportWithIata.get(f.getDestinationIata()));
                }
                foundRoute = f.getDestinationIata().equals(destinationIata);
            }
        }

        if (!foundRoute) {
            throw new IllegalArgumentException();
        }

        double distanceTotal = 0;
        Airport actualAirport = airportWithIata.get(destinationIata);
        boolean isFinished = false;
        ArrayList<Flight> usedFlights = new ArrayList<>();
        while (!isFinished) {
            Flight f = flightsHistory.get(actualAirport);
            Airport sourceAirport = airportWithIata.get(f.getSourceIata());
            Airport destAirport = airportWithIata.get(f.getDestinationIata());
            double distance = Util.distance(sourceAirport.getLatitude(), sourceAirport.getLongitude(),
                    destAirport.getLatitude(), destAirport.getLongitude());
            f.setDistance(distance);
            distanceTotal += distance;
            usedFlights.add(f);
            isFinished = f.getSourceIata().equals(sourceIata);
            actualAirport = airportWithIata.get(f.getSourceIata());
        }

        System.out.println("distance : " + distanceTotal);
        for (int i = usedFlights.size() - 1; i > -1; i--) {
            Flight f = usedFlights.get(i);
            System.out.println(
                    "Vol [source=" + airportWithIata.get(f.getSourceIata()).getName() +
                            ", destination=" + airportWithIata.get(f.getDestinationIata()).getName() +
                            ", airline=" + f.getCompanyName() +
                            ", distance=" + f.getDistance() + "]");
        }
    }

    public void calculerItineraireMinimisantDistance(String sourceIata, String destinationIata) {

        HashMap<Airport, Double> tempo = new HashMap<Airport, Double>();
        HashMap<Airport, Double> definitive = new HashMap<>();
        Airport sourceAirport = airportWithIata.get(sourceIata);
        Airport destinationAirport = airportWithIata.get(destinationIata);

        definitive.put(sourceAirport, 0.0);
        tempo.put(sourceAirport,0.0);
        Airport minimumTempo = sourceAirport;

        while (!definitive.containsKey(destinationAirport)/*tempo.size() != definitive.size()*/) {
            for (Flight flight : arcsSortants.get(minimumTempo)) {
                System.out.println("remplissage tempo");
                if(definitive.containsKey(flight.getDestinationIata())) {
                    System.out.println("contient deja dansdefinitive");
                    break;
                }
                Airport flightDestination = airportWithIata.get(flight.getDestinationIata());
                Airport flightSource = airportWithIata.get(flight.getSourceIata());
                double dist = Util.distance(minimumTempo.getLatitude(), minimumTempo.getLongitude(),
                        flightSource.getLatitude(), flightDestination.getLongitude());

                if (tempo.get(flightDestination) == null) {      //pas encore de valeur dans tempo
                    flight.setDistance(dist);
                    tempo.put(flightDestination, dist);

                } else {
                    if (tempo.get(flightDestination) > dist + definitive.get(flightSource)) {     //distance doit etre remplacee
                        flight.setDistance(dist);
                        tempo.put(flightDestination, dist);
                    }
                }
            }
            //trouver le minimum prochain
            System.out.println("recherche min");
            double distTemp = 0;
            double min = 99999;

            for (Airport a : tempo.keySet()) {
                double t = tempo.get(a);
                if (t != 0) {
                    if (t < min) {
                        min = tempo.get(a);
                        minimumTempo = a;
                        distTemp = tempo.get(a);
                    }
                }
            }
            definitive.put(minimumTempo, distTemp);
            tempo.remove(minimumTempo);
            System.out.println("distTemp " + distTemp);
            System.out.println("definitive " + definitive);
            System.out.println("tempo "+tempo);
            //return;

        }
        System.out.println("dist tot:" + definitive.get(destinationAirport));
        return;
    /*
    for (int i = usedFlights.size() - 1; i > -1; i--) {
      Flight f = usedFlights.get(i);
      System.out.println(
              "Vol [source=" + airportWithIata.get(f.getSourceIata()).getName() +
                      ", destination=" + airportWithIata.get(f.getDestinationIata()).getName() +
                      ", airline=" + f.getCompanyName() +
                      ", distance=" + f.getDistance() + "]");
    }
     */

    }

}