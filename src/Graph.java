import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {

  // aeroports : Iata, name, city, country, longitude, latitude
  // vols : company name, source airport iata, destination airport iata
  private HashMap<String, Airport> airportWithIata = new HashMap<>();
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
      }
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void calculerItineraireMinimisantNombreVol(String sourceIata, String destinationIata) {
    Airport sourceAirport = airportWithIata.get(sourceIata);
  }

  public void calculerItineraireMinimisantDistance(String sourceIata, String destinationIata) {

  }

}
