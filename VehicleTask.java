import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class VehicleTask {
    private Vehicle[] vehicles;

    public VehicleTask() {
        vehicles = getVehicles("http://www.rentalcars.com/js/vehicles.json");

        //Task 1.1
        System.out.println("####PRICES####");
        Arrays.sort(vehicles, new PriceComparator());
        for(int i = 0; i < vehicles.length; i++) {
            System.out.println(vehicles[i].getName() + "-" + vehicles[i].getPrice());
        }

        //Task 1.2
        System.out.println("####SPECIFICATIONS####");
        for(int i = 0; i < vehicles.length; i++) {
            vehicles[i].setSpecification();
            System.out.println( vehicles[i].getName() + "-" + 
                                vehicles[i].getSIPP() + "-" +
                                vehicles[i].getCarType() + "-" +
                                vehicles[i].getDoors() + "-" + 
                                vehicles[i].getTransmission() + "-" + 
                                vehicles[i].getFuel() + "-" +
                                vehicles[i].getAir() 
                                );
        }

        //Task 1.3
        System.out.println("####SUPPLIERS####");
        //Construct map of car type to highest rated vehicle (and therefore supplier) for it
        Map<String, Vehicle> carTypeMap = new HashMap<String, Vehicle>();
        for(int i = 0; i < vehicles.length; i++) {
            if(!carTypeMap.containsKey(vehicles[i].getCarType())) {
                carTypeMap.put(vehicles[i].getCarType(), vehicles[i]);
            } else if(vehicles[i].getRating() < carTypeMap.get(vehicles[i].getCarType()).getRating()) {
                carTypeMap.put(vehicles[i].getCarType(), vehicles[i]);
            }
        }

        //See getVehicles() function about speed of new Vehicle[0]
        Vehicle[] bestSuppliers = carTypeMap.values().toArray(new Vehicle[0]);
        Arrays.sort(bestSuppliers, new RatingComparator());
        for(int i = 0; i < bestSuppliers.length; i++) {
            System.out.println( bestSuppliers[i].getName() + "-" +
                                bestSuppliers[i].getCarType() + "-" +
                                bestSuppliers[i].getSupplier() + "-" +
                                bestSuppliers[i].getRating()
                                );
        }

        //Task 1.4
        System.out.println("####SCORES####");
        for(int i = 0; i < vehicles.length; i++) {
            vehicles[i].computeScore();
        }
        Arrays.sort(vehicles, new CombinedScoreComparator());
        for(int i = 0; i < vehicles.length; i++) {
            float combinedScore = vehicles[i].getRating() + vehicles[i].getScore();
            System.out.println( vehicles[i].getName() + "-" + 
                                vehicles[i].getScore() + "-" +
                                vehicles[i].getRating() + "-" +
                                combinedScore
                                );
        }
    }

    //Sort by price in ascending order
    class PriceComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle a, Vehicle b) {
            return a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1;
        }
    }

    //Sort by rating in descending order
    class RatingComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle a, Vehicle b) {
            return a.getRating() > b.getRating() ? -1 : a.getRating() == b.getRating() ? 0 : 1;
        }
    }
    
    //Sort by combined score in descending order
    class CombinedScoreComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle a, Vehicle b) {
            float aScore = a.getRating() + a.getScore();
            float bScore = b.getRating() + b.getScore();
            return aScore > bScore ? -1 : aScore == bScore ? 0 : 1;
        }
    }

    //Retrieve an array of vehicles from the external JSON API
    private Vehicle[] getVehicles(String urlString) {
        StringBuffer stringBuf = new StringBuffer();
        char[] chars = new char[1024];

        URL url = null;
        try{
            url = new URL(urlString);
        } catch(MalformedURLException mue) {
            System.out.println("Unable to parse URL, aborting.");
            System.exit(0);
        }

        BufferedReader readBuf = null;
        try{
            readBuf = new BufferedReader(new InputStreamReader(url.openStream()));
            int curPointer;
            while((curPointer = readBuf.read(chars)) != -1) {
                stringBuf.append(chars, 0, curPointer);
            }
            readBuf.close();
        } catch(IOException ioe) {
            System.out.println("Unable to retrieve JSON from URL, aborting.");
            System.exit(0);
        }
        String rawJSON = stringBuf.toString();

        Gson gson = new Gson();

        RawObject raw = gson.fromJson(rawJSON, RawObject.class);
        List<Vehicle> list = raw.getSearch().getVehicles();

        // Unintuitive, but faster than toArray(new Vehicle[list.size()]). See https://shipilev.net/blog/2016/arrays-wisdom-ancients/
        Vehicle[] vehicleArray = list.toArray(new Vehicle[0]);
        return vehicleArray;
    }

    public static void main(String args[]) {
        new VehicleTask();
    }
}