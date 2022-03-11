public class Flight {

  private String companyName;
  private String sourceIata;
  private String destinationIata;

  public Flight(String companyName, String sourceIata, String destinationIata) {
    this.companyName = companyName;
    this.sourceIata = sourceIata;
    this.destinationIata = destinationIata;
  }

  public String getCompanyName() {
    return companyName;
  }

  public String getSourceIata() {
    return sourceIata;
  }

  public String getDestinationIata() {
    return destinationIata;
  }

  @Override
  public String toString() {
    return "Flight{" + "companyName='" + companyName + '\'' + ", sourceIata='" + sourceIata + '\''
        + ", destinationIata='" + destinationIata + '\'' + '}';
  }
}
