import java.util.Map;
import java.util.HashMap;
/**
 * Container class for vehicle fields
 */
public class Vehicle {

    //Provided fields
    private String sipp;
    private String name;
    private float price;
    private String supplier;
    private float rating;

    //Specification fields
    private String carType;
    private String doors;
    private String transmission;
    private String fuel;
    private String air;

    //Score - Float so we can easily add it to rating
    private float score;

    //Provided field accessors
    public String getSIPP() {
        return sipp;
    }
    
    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getSupplier() {
        return supplier;
    }

    public float getRating() {
        return rating;
    }

    //Specification accessors
    public String getCarType() {
        return carType;
    }

    public String getDoors() {
        return doors;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getFuel() {
        return fuel;
    }
    
    public String getAir() {
        return air;
    }

    //Score accessor
    public float getScore() {
        return score;
    }

    //Use maps because it makes it easier to extend vehicle specifications later, plus faster than switch cases

    private Map<String, String> buildCarTypeMap() {
        Map<String, String> carTypeMap = new HashMap<String, String>();
        carTypeMap.put("M", "Mini");
        carTypeMap.put("E", "Economy");
        carTypeMap.put("C", "Compact");
        carTypeMap.put("I", "Intermediate");
        carTypeMap.put("S", "Standard");
        carTypeMap.put("F", "Full Size");
        carTypeMap.put("P", "Premium");
        carTypeMap.put("L", "Luxury");
        carTypeMap.put("X", "Special");
        return carTypeMap;
    }

    private Map<String, String> buildDoorMap() {
        Map<String, String> doorMap = new HashMap<String, String>();
        doorMap.put("B", "2 doors");
        doorMap.put("C", "4 doors");
        doorMap.put("D", "5 doors");
        doorMap.put("W", "Estate");
        doorMap.put("F", "SUV");
        doorMap.put("P", "Pick up");
        doorMap.put("V", "Passenger Van");
        //Found below code on https://www.car-hire-centre.co.uk/sipp-codes.html
        doorMap.put("X", "Special");
        return doorMap;
    }

    private Map<String, String> buildTransmissionMap() {
        Map<String, String> transmissionMap = new HashMap<String, String>();
        transmissionMap.put("M", "Manual");
        transmissionMap.put("A", "Automatic");
        return transmissionMap;
    }

    private Map<String, String> buildFuelMap() {
        Map<String, String> fuelMap = new HashMap<String, String>();
        fuelMap.put("N", "Petrol/no AC");
        fuelMap.put("R", "Petrol/AC");
        return fuelMap;
    }

    //Uses the above functions 
    public void setSpecification() {
        //Get all the HashMaps
        Map<String, String> carTypeMap = buildCarTypeMap();
        Map<String, String> doorMap = buildDoorMap();
        Map<String, String> transmissionMap = buildTransmissionMap();
        Map<String, String> fuelMap = buildFuelMap();
        //Resolve to specifications
        carType = carTypeMap.get(String.valueOf(sipp.charAt(0)));
        doors = doorMap.get(String.valueOf(sipp.charAt(1)));
        transmission = transmissionMap.get(String.valueOf(sipp.charAt(2)));
        //Split up the content of the Fuel/Air specification
        String[] airFuel = fuelMap.get(String.valueOf(sipp.charAt(3))).split("/");
        fuel = airFuel[0];
        air = airFuel[1];
    }
    
    public void computeScore() {
        score = 0;
        if(transmission.equals("Manual")) {
            score += 1;
        } else {
            score += 5;
        }

        if(air.equals("AC")) {
            score += 2;
        }
    }

}