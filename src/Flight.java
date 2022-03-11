public class Flight {

  private String companyName;
  private String sourceIata;
  private String destinationIata;

  public Flight() {
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getSourceIata() {
    return sourceIata;
  }

  public void setSourceIata(String sourceIata) {
    this.sourceIata = sourceIata;
  }

  public String getDestinationIata() {
    return destinationIata;
  }

  public void setDestinationIata(String destinationIata) {
    this.destinationIata = destinationIata;
  }

  @Override
  public String toString() {
    return "Flight{" + "companyName='" + companyName + '\'' + ", sourceIata='" + sourceIata + '\''
        + ", destinationIata='" + destinationIata + '\'' + '}';
  }
}
