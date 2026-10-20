package indexing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<double[]> locations = new ArrayList<>();

        locations.add(new double[]{34.052235, -118.243683});
        locations.add(new double[]{51.507351, -0.127758});
        locations.add(new double[]{-33.868820, 151.209290});
        locations.add(new double[]{35.689487, 139.691711});
        locations.add(new double[]{48.856613, 2.352222});
        locations.add(new double[]{40.712776, -74.005974});
        locations.add(new double[]{55.755825, 37.617298});
        locations.add(new double[]{19.432608, -99.133209});
        locations.add(new double[]{-23.550520, -46.633308});
        locations.add(new double[]{1.352083, 103.819839});

        System.out.println("Before sorting");

        for (double[] location : locations) {
            System.out.println("Lat: " + location[0] + ", Lng: " + location[1]);
        }

        locations.sort(
                Comparator
                        .comparingDouble((double[] location) -> location[0])
                        .thenComparingDouble(location -> location[1])
        );

        System.out.println("After sorting");

        for (double[] location : locations) {
            System.out.println("Lat: " + location[0] + ", Lng: " + location[1]);
        }
    }

}