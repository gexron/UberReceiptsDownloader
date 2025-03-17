package org.gexron.receiptsdownload;

public class Trip {

    public static final String UBER_TRIPS_URL = "https://riders.uber.com/trips";

    private String id;
    private String tripStatus;

    public boolean isTripCompleted() {
        return tripStatus.equals("Completed");
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripUrl() {
        return new StringBuilder(UBER_TRIPS_URL)
                .append("/")
                .append(id)
                .toString();
    }

    public String getTripReceiptDownloadUrl() {
        return new StringBuilder(getTripUrl())
                .append("/receipt?contentType=PDF")
                .toString();
    }
}
