package com.techiesse.CoinConverter.bc;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Downloader {
    private String baseUrl;
    private String dataPath;

    public Downloader(String baseUrl, String dataPath) {
        this.baseUrl = baseUrl;
        this.dataPath = dataPath;
    }


    public String download(String path) throws InexistentQuotationException {
        var url = baseUrl + "/" + path;

        // 1. Create an HttpClient instance
        HttpClient client = HttpClient.newBuilder().build();

        // 2. Create an HttpRequest object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // 3. Send the request and receive the response synchronously
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() >= 400 ) {
                throw new InexistentQuotationException();
            }

            // 4. Process the response
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void downloadCoinTable(String strDate) throws InexistentQuotationException {
        var filename = "M" + strDate + ".csv";
        var content = download(filename);
        saveTextToFile(content, dataPath + "\\" + filename);
    }

    public void downloadQuotations(String strDate) throws InexistentQuotationException {
        var filename = strDate + ".csv";
        var content = download(filename);
        saveTextToFile(content, dataPath + "\\" + filename);
    }


    public void saveTextToFile(String text, String filepath) {
        try (FileWriter writer = new FileWriter(filepath)){
            writer.write(text);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
