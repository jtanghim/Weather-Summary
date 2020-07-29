package weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class Weather {

    String jsonResponse;
    String lat;
    String lon;

    /**
     * Please obtain a valid API Key OpenWeatherMap
     * Learn more at https://openweathermap.org/appid
     */
    private static final String API_KEY = "ENTER YOUR API KEY HERE";


    /** Constructor for Weather class
     * @param lat Latitude of desired location to be looked up
     * @param lon Longitude of desired location to be looked up
     */
    public Weather(String lat, String lon) throws IOException {
        this.lat = lat;
        this.lon = lon;
        this.jsonResponse = sendGetRequest();
    }

    /**
     *Excludes fetching and analyzing the minute-to-minute forecast and hourly forecast as the goal of the program is to provide
     *a weather summary
     * @throws IOException if user's input for latitude/longitude was invalid
     */

    private String sendGetRequest() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&units=imperial&exclude=minutely,hourly&appid="+API_KEY)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new IOException("One/Both your inputs are invalid");
        }
    }

    /**
     * Prints the following current conditions: temperature, feels like, wind speed, wind gusts, and description
     * @throws JsonProcessingException
     */
    public void printCurrent() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        int temp = jsonNode.path("current").path("temp").asInt();
        int feelsLike = jsonNode.path("current").path("feels_like").asInt();
        double windSpeed = jsonNode.path("current").path("wind_speed").asDouble();
        double windGust = jsonNode.path("current").path("wind_gust").asDouble();
        //The following lines are used to access the Json Array "weather"
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonResponse);
            JSONObject current = (JSONObject)jsonObject.get("current");
            JSONArray weatherArr = (JSONArray) current.get("weather");
            /**Interested in JSONArray named "weather" which just has 1 element with 4 key/value pairs. I am interested in the key/value pairs "main" and "description."
             * "main" gives the weather condition(cloudy, rainy, sunny, etc...)
             * "description" builds off of "main" by providing a little more in-depth description of conditions
             * Read more at: https://openweathermap.org/api/one-call-api
             */
            JSONObject weather = (JSONObject)weatherArr.get(0);
            System.out.println("Currently the date & time for the location you are looking up is: "+
                    SimpleDateTime.convertToDateTime(jsonNode.path("current").path("dt").asLong(),jsonNode.path("timezone_offset").asLong()));
            System.out.println("Currently the forecast is: "+ weather.get("main") + ", "+ weather.get("description") + ". The temperature is " + temp +"°F, feels like " + feelsLike +
                    "°F with wind speeds of " + windSpeed + " mph and gusts up to " + windGust + " mph.");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Prints the daily forecast summary for 8 days (the current date + the 7 following days)
     * Forecast summary consists of: the high and low temperature, humidity, wind speed, and description
     * @throws JsonProcessingException
     */
    public void printDaily() {
        JSONParser jsonParser = new JSONParser();
        long min, max;
        String humidity, windSpeed;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonResponse);
            JSONArray dailyArr = (JSONArray) jsonObject.get("daily");

            for(int x =0; x<8;x++) {
                JSONObject dailySummary = (JSONObject) dailyArr.get(x);
                System.out.println(SimpleDateTime.convertToDate((long) (dailySummary.get("dt")))+":");
                JSONObject dailyTemp = (JSONObject) dailySummary.get("temp");
                /**
                 * The following try/catch statements are used as the data type returned from the get method could either be double/long
                 * I want to truncate the min & max temperate as traditionally temperates are displaced w/o decimals
                 */
                try{
                min = (long) dailyTemp.get("min");
                }
                catch (ClassCastException e){
                    min = (long) ((double)dailyTemp.get("min"));
                }
                try{
                    max = (long) dailyTemp.get("max");
                } catch (ClassCastException e) {
                    max = (long) ((double)dailyTemp.get("max"));
                }

                humidity = dailySummary.get("humidity").toString();
                windSpeed = dailySummary.get("wind_speed").toString();
                System.out.println("The low is " + min + "°F and the high is " + max + "°F.");
                System.out.println("The humidity is " + humidity + "%. There will be average wind speeds of " +
                        windSpeed + " mph.");
                JSONArray weather = (JSONArray) dailySummary.get("weather");
                JSONObject description = (JSONObject) weather.get(0);
                System.out.println("Expect " + description.get("main") + ", " + description.get("description") + ".");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




}
