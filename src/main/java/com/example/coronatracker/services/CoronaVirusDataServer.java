package com.example.coronatracker.services;

import com.example.coronatracker.models.LocationStatus;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataServer {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStatus> allStatus = new ArrayList<>();

    public List<LocationStatus> getAllStatus() {
        return allStatus;
    }

    @PostConstruct
    @Scheduled(cron = "* * * 1 * *") // execute and update  the method every day
    public void fetchData() throws IOException, InterruptedException {
        List<LocationStatus> newStatus = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStatus locationStatus = new LocationStatus();// creates a new instance
            locationStatus.setState(record.get("Province/State"));
            locationStatus.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() -1));
            int prevDayCases = Integer.parseInt(record.get(record.size() -2));
            locationStatus.setLatestTotalCases(latestCases);
            locationStatus.setDiffFromPreviousDay(latestCases - prevDayCases);
            newStatus.add(locationStatus);
        }
        this.allStatus = newStatus;
    }
}
