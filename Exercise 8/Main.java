import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RouteFinder routeFinder = new RouteFinder();

        BufferedReader nodeReader = new BufferedReader(new FileReader("src/noder.txt"));
        BufferedReader edgeReader = new BufferedReader(new FileReader("src/kanter.txt"));
        BufferedReader pointsOfInterestReader = new BufferedReader(new FileReader("src/interessepktnorden.txt"));

        routeFinder.extractInformationFromFiles(nodeReader, edgeReader, pointsOfInterestReader);


        Node trondheim = routeFinder.findNode(2399829);
        Node helsinki = routeFinder.findNode(1221382);
        Node trondheimLuftHavnVaernes = routeFinder.findNode(6198111);
        Node rorosHotell = routeFinder.findNode(1117256);
        Node kaarvaag = routeFinder.findNode(6013683);
        Node gjemnes = routeFinder.findNode(6225195);

        System.out.println("Kårvåg to Gjemnes: ");

        long startTime = System.nanoTime();
        Node dijkstraNode = routeFinder.dijkstra(kaarvaag, gjemnes);
        long endTime = System.nanoTime();

        System.out.println("------------- PATH USING DIJKSTRA --------------");
        System.out.println("Runtime: " + ((endTime-startTime)/1000000));
        System.out.println("Number of extracted nodes: " + routeFinder.getNumberOfExtractedNodes());
        routeFinder.printTripInformation(dijkstraNode);

        startTime = System.nanoTime();
        Node aStarNode = routeFinder.aStar(kaarvaag, gjemnes);
        endTime = System.nanoTime();

        System.out.println("------------- PATH USING ASTAR --------------");
        System.out.println("Runtime: " + ((endTime-startTime)/1000000));
        System.out.println("Number of extracted nodes: " + routeFinder.getNumberOfExtractedNodes());
        routeFinder.printTripInformation(aStarNode);

        routeFinder.printTrip(aStarNode);

        System.out.println("-------------GAS STATIONS ------------");
        routeFinder.dijkstraLocations(trondheimLuftHavnVaernes, 2, 10);

        System.out.println("------------- CHARGING STATIONS ------------");
        routeFinder.dijkstraLocations(rorosHotell, 4, 10);
    }
}
