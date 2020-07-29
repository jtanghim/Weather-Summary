package weather;

/**
 * This is the Driver class of my weather application. Prompts user to input Latitude then Longitude of the desired location.
 * For the sake of location accuracy, I used latitude and longitude instead of easier identifiers like city names.
 */

import java.io.IOException;
import java.util.Scanner;

public class WeatherDriver {

    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        System.out.print("Please enter your locations latitude: ");
        String lat = scan.next();
        System.out.print("Please enter your locations longitude: ");
        String lon = scan.next();

        Weather spot = new Weather(lat,lon);
        spot.printCurrent();
        spot.printDaily();
    }

}
