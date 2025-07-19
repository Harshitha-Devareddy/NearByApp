import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class NearbyApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🌍 Welcome to What's Happening Nearby!");

        System.out.print("Enter your city name: ");
        String city = scanner.nextLine();

        showLocalTime();
        showWeather(city);
        showPublicHolidays();
        showTrendingNews();  

        scanner.close();
    }

    private static void showLocalTime() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("\n Local Time: " + now);
    }

    private static void showWeather(String city) {
        try {
            String apiKey = "033c174b25dc215adc988935f6b75edc"; 
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    city + "&units=metric&appid=" + apiKey;

            String response = getHttpResponse(weatherUrl);
            JSONObject jsonObject = new JSONObject(response);

            JSONObject main = jsonObject.getJSONObject("main");
            JSONArray weather = jsonObject.getJSONArray("weather");
            String description = weather.getJSONObject(0).getString("description");
            double temperature = main.getDouble("temp");

            System.out.println("\n Weather in " + city + ": " + description + ", " + temperature + "°C");

        } catch (Exception e) {
            System.out.println("\n Error fetching weather: " + e.getMessage());
        }
    }

    private static void showPublicHolidays() {
        System.out.println("\n Upcoming Public Holidays:");
        System.out.println(" 1.Independence Day - August 15");
        System.out.println(" 2.Gandhi Jayanti - October 2");
        System.out.println(" 3.Diwali - November 12");
    }

    private static void showTrendingNews() {
        try {
            String newsApiKey = "69d50af3f2994a01ba863e963ebb2ad5"; 
            String newsUrl = "https://newsapi.org/v2/everything?q=cricket&language=en&sortBy=publishedAt&pageSize=5&apiKey=" + newsApiKey;

            String response = getHttpResponse(newsUrl);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray articles = jsonObject.getJSONArray("articles");

            System.out.println("\n Trending News Headlines:");
            for (int i = 0; i < Math.min(5, articles.length()); i++) {
                JSONObject article = articles.getJSONObject(i);
                System.out.println(" " + article.getString("title"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getHttpResponse(String urlString) throws Exception {
        URI uri= new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        int status = con.getResponseCode();

        if (status != 200) {
            throw new RuntimeException("HTTP Error: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            content.append(inputLine);

        in.close();
        con.disconnect();

        return content.toString();
    }
}
