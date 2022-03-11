import java.util.Objects;

public class Airport {

  private String iata;
  private String name;
  private String city;
  private String country;
  private long longitude;
  private long latitude;

  public Airport(String iata, String name, String city, String country, long longitude,
      long latitude) {
    this.iata = iata;
    this.name = name;
    this.city = city;
    this.country = country;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public String getIata() {
    return iata;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public long getLongitude() {
    return longitude;
  }

  public long getLatitude() {
    return latitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Airport airport = (Airport) o;
    return Objects.equals(iata, airport.iata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iata);
  }

  @Override
  public String toString() {
    return "Airport{" + "iata='" + iata + '\'' + ", name='" + name + '\'' + ", city='" + city + '\''
        + ", country='" + country + '\'' + ", longitude=" + longitude + ", latitude=" + latitude
        + '}';
  }
}
