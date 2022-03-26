import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
                Airport aSource = airportWithIata.get(f.getSourceIata());
                Airport aDest = airportWithIata.get(f.getDestinationIata());
                f.setDistance(Util.distance(aSource.getLatitude(), aSource.getLongitude(),
                        aDest.getLatitude(), aDest.getLongitude()));
                arcsSortants.get(aSource).add(f);
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
            distanceTotal += f.getDistance();
            usedFlights.add(f);
            isFinished = f.getSourceIata().equals(sourceIata);
            actualAirport = airportWithIata.get(f.getSourceIata());
        }

        System.out.println("distance : " + distanceTotal);

        afficher(usedFlights);
    }

    public void calculerItineraireMinimisantDistance(String sourceIata, String destinationIata) {
        HashMap<Airport, Double> tempo = new HashMap<>();
        HashMap<Airport, Double> definitive = new HashMap<>();
        HashMap<Airport,Flight> visites = new HashMap<>();
        ArrayList<Flight> vols = new ArrayList<>();

        Airport sourceAirport = airportWithIata.get(sourceIata);
        Airport destinationAirport = airportWithIata.get(destinationIata);

        if(sourceAirport.equals(destinationAirport))
            throw new IllegalArgumentException("meme aeroport");

        tempo.put(sourceAirport, 0.0);

        while (!definitive.containsKey(destinationAirport) && !tempo.isEmpty()) {
            Airport minimumTempo = tempo.keySet().stream().min(Comparator.comparing(m -> tempo.get(m))).get();
            double min = tempo.get(minimumTempo);

            definitive.put(minimumTempo, min);
            tempo.remove(minimumTempo);

            for (Flight flight : arcsSortants.get(minimumTempo)) {
                String iata = flight.getDestinationIata();
                visites.put(airportWithIata.get(flight.getDestinationIata()),flight);
                if (definitive.containsKey(airportWithIata.get(iata))) {

                } else {
                    Airport flightDestination = airportWithIata.get(flight.getDestinationIata());
                    Airport flightSource = airportWithIata.get(flight.getSourceIata());
                    double dist = flight.getDistance() + definitive.get(flightSource);
                    if (tempo.get(flightDestination) == null || tempo.get(flightDestination) > dist) {
                        tempo.put(flightDestination, dist);
                    }
                }
            }
        }
        System.out.println("dist tot:" + definitive.get(destinationAirport));
        Flight f =visites.get(destinationAirport);      //le vol qui arrive a la destination

        System.out.println("--------------");
        System.out.println("f " +f);
        Airport sourcevol = airportWithIata.get(f.getSourceIata());
        Airport destinationvol = airportWithIata.get(f.getDestinationIata());
        System.out.println("sourceVol "+sourcevol);
        System.out.println("destintionVol "+destinationvol);

        while(!vols.contains(sourceAirport)){           //probleme boucle vide, il faut retenir le vol pour la destinaion
            if(!vols.contains(f)) {
                vols.add(f);
                System.out.println(vols);
            }
            destinationvol = sourcevol;
            f=visites.get(destinationvol);
            //System.out.println("------");
            //System.out.println(f);
            sourcevol = airportWithIata.get(f.getSourceIata());
            //System.out.println(sourcevol);
            //System.out.println(destinationvol);
        }

    }

    private void afficher(ArrayList<Flight> usedFlights) {
        for (int i = usedFlights.size() - 1; i > -1; i--) {
            Flight f = usedFlights.get(i);
            System.out.println(
                    "Vol [source=" + airportWithIata.get(f.getSourceIata()).getName() +
                            ", destination=" + airportWithIata.get(f.getDestinationIata()).getName() +
                            ", airline=" + f.getCompanyName() +
                            ", distance=" + f.getDistance() + "]");
        }
    }

}