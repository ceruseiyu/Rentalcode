<?php
    header('Content-Type: application/json');

    //Task 1.1 container
    class PriceItem {
        public $name;
        public $price;

        public function __construct($name, $price) {
            $this->name = $name;
            $this->price = $price;
        }
    }

    //Task 1.2 container
    class SpecItem {
        public $name;
        public $sipp;
        public $carType;
        public $doors;
        public $transmission;
        public $fuel;
        public $airCon;

        public function __construct($name, $sipp, $carType, $doors, $transmission, $fuel, $airCon) {
            $this->name = $name;
            $this->sipp = $sipp;
            $this->carType = $carType;
            $this->doors = $doors;
            $this->transmission = $transmission;
            $this->fuel = $fuel;
            $this->airCon = $airCon;
        }
    }

    //Task 1.3 container
    class SupplierItem {
        public $name;
        public $carType;
        public $supplier;
        public $rating;

        public function __construct($name, $carType, $supplier, $rating) {
            $this->name = $name;
            $this->carType = $carType;
            $this->supplier = $supplier;
            $this->rating = $rating;
        }
    }

    //Task 1.4 container
    class ScoreItem {
        public $name;
        public $score;
        public $rating;
        public $combinedScore;

        public function __construct($name, $score, $rating, $combinedScore) {
            $this->name = $name;
            $this->score = $score;
            $this->rating = $rating;
            $this->combinedScore = $combinedScore;
        }
    }

    //Container for all data
    class APIContainer {
        public $prices;
        public $specifications;
        public $suppliers;
        public $scores;

        public function __construct($prices, $specifications, $suppliers, $scores) {
            $this->prices = $prices;
            $this->specifications = $specifications;
            $this->suppliers = $suppliers;
            $this->scores = $scores;
        }
    }

    //Retrieve output from java app and set up for the loop
    //Update with the following with adjusted classpath as needed on web server:
    //java -classpath .;C:\Users\whati\Desktop\RentalCars\gson-2.8.1.jar;C:\Users\whati\Desktop\RentalCars\; VehicleTask
    exec('cmd.exe /c C:\Users\whati\Desktop\RentalCars\exec.bat', $output);
    $priceArray = array();
    $specArray = array();
    $supplierArray = array();
    $scoreArray = array();
    $stage = 0;

    foreach($output as &$line) { //Splitting up the data and putting it into the correct array
        $splitLine = explode("-", $line);
        if($stage == 0) {
            if(strcmp($line, "####PRICES####") != 0) {
                if(strcmp($line, "####SPECIFICATIONS####") == 0) {
                    $stage = 1;
                } else {
                    $priceArray[] = new PriceItem($splitLine[0], $splitLine[1]);
                }
            }
        } else if($stage == 1) {
            if(strcmp($line, "####SUPPLIERS####") == 0) {
                $stage = 2;
            } else {
                $specArray[] = new SpecItem($splitLine[0], $splitLine[1], $splitLine[2], $splitLine[3], $splitLine[4], $splitLine[5], $splitLine[6]);
            }
        } else if($stage == 2) {
            if(strcmp($line, "####SCORES####") == 0) {
                $stage = 3;
            } else {
                $supplierArray[] = new SupplierItem($splitLine[0], $splitLine[1], $splitLine[2], $splitLine[3]);
            }
        } else{
            $scoreArray[] = new ScoreItem($splitLine[0], $splitLine[1], $splitLine[2], $splitLine[3]);
        }
    }

    //Put all arrays into a container object and encode to JSON
    $container = new APIContainer($priceArray, $specArray, $supplierArray, $scoreArray);
    echo json_encode($container);
?>