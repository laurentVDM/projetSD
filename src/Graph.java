import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Graph {

  // aeroports : Iata, name, city, country, longitude, latitude
  // vols : company name, source airport iata, destination airport iata

  public Graph(File file1, File file2) {
    try {
      String currentLine;
      BufferedReader reader = new BufferedReader(new FileReader(file1));

      while ((currentLine = reader.readLine()) != null) {

      }
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void calculerItineraireMinimisantNombreVol(String sourceIata, String destinationIata) {

  }

  public void calculerItineraireMinimisantDistance(String sourceIata, String destinationIata) {

  }

}
