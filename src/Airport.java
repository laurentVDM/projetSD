import java.util.Objects;

public class Airport {

  private String iata;
  private String name;
  private String city;
  private String country;
  private double longitude;
  private double latitude;

  public Airport() {
  }

  public String getIata() {
    return iata;
  }

  public void setIata(String iata) {
    this.iata = iata;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
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
