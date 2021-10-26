public class Node {
    RouteFinder.WEdge edge1;
    int nodeNumber;
    double latitude;
    double longitude;
    Object predecessor;
    int type;
    String locationName;

    public Node(int nodeNumber, double latitude, double longitude) {
        this.nodeNumber = nodeNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void printCoordinates() {
        System.out.println(latitude*(180/Math.PI) + "," + longitude*(180/Math.PI));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
