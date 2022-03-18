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
      }

      while ((currentLineFile2 = readerFile2.readLine()) != null) {
        attributesFile2 = currentLineFile2.split("\\,");
        Flight f = new Flight();
        f.setCompanyName(attributesFile2[0]);
        f.setSourceIata(attributesFile2[1]);
        f.setDestinationIata(attributesFile2[2]);
        flights.add(f);
        Airport a = airportWithIata.get(f.getSourceIata());
        if (arcsSortants.get(a).isEmpty()) {
          arcsSortants.put(a, new HashSet<>());
        }
        arcsSortants.get(a).add(f);
      }
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void calculerItineraireMinimisantNombreVol(String sourceIata, String destinationIata) {
    // Breadth-First search
    boolean foundRoute = false;
    ArrayDeque<String> file = new ArrayDeque<>();
    HashMap<String, Flight> historiqueVols = new HashMap<>();
    file.add(sourceIata);
    String actual;
    while (!foundRoute && !file.isEmpty()) {
      actual = file.pollFirst();
      for (Flight f : flights) {
        if (f.getSourceIata().equals(actual)) {
          if (f.getDestinationIata().equals(destinationIata)) {
            foundRoute = true;
          } else if (!file.contains(f.getDestinationIata())) {
            file.add(f.getDestinationIata());
          }
        }
      }
    }
  }

  public void calculerItineraireMinimisantDistance(String sourceIata, String destinationIata) {
    //algo du plus court chemin
    if(sourceIata.equals(destinationIata)) {
      System.out.println("Meme endroit");
      return;
    }

    HashMap<Airport,Double> tempo = new HashMap<Airport,Double>();
    HashMap<Airport, Double> definitive = new HashMap<>();
    ArrayList<Airport> visités = new ArrayList<Airport>();
    Airport sourceAirport = airportWithIata.get(sourceIata);
    Airport destinationAirport = airportWithIata.get(destinationIata);

    tempo.put(sourceAirport,null);
    definitive.put(sourceAirport,0.0);
    visités.add(sourceAirport);
    for(Flight flight : arcsSortants.get(sourceAirport)){
      double dist = Util.distance( sourceAirport.getLatitude(),sourceAirport.getLongitude(),
              airportWithIata.get(flight.getDestinationIata()).getLatitude(),
              airportWithIata.get(flight.getDestinationIata()).getLongitude());
      tempo.put(airportWithIata.get(destinationIata), dist);
    }

    //tant que la ville de destination finale n'a pas été remplie dans le hashmap definitif on cherche
    while(definitive.get(destinationAirport)==null){
      Airport minimumTempo = null;
      double distTemp = 0;
      for(Airport a : tempo.keySet()){
        double min = 99999;
        if(tempo.get(a)<min) {
          min = tempo.get(a);
          minimumTempo = a;
          distTemp = tempo.get(a);
        }
      }
      //ajout dans la table definitive
      definitive.put(minimumTempo,distTemp);

      for(Flight flight : arcsSortants.get(minimumTempo)){
        if(!visités.contains(flight.getDestinationIata())) {
          double distance = Util.distance(minimumTempo.getLatitude(), minimumTempo.getLongitude(),
                  airportWithIata.get(flight.getDestinationIata()).getLatitude(),
                  airportWithIata.get(flight.getDestinationIata()).getLongitude());
          if (tempo.get(flight.getDestinationIata()) > definitive.get(minimumTempo) + distance)
            tempo.put(airportWithIata.get(flight.getDestinationIata()), definitive.get(minimumTempo) + distance);
        }
      }

      //suppression dans la table tempo
      tempo.remove(minimumTempo);
    }


  }

}
