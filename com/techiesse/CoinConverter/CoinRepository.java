package com.techiesse.CoinConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.techiesse.CoinConverter.bc.Coin;

public class CoinRepository {
    private String path;


    public CoinRepository(String path) {
        this.path = path;
    }

    public HashMap<String, Coin> getAllCoins(LocalDate date) throws IOException{
        var dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return getAllCoins(dateStr);
    }


    public HashMap<String, Coin> getAllCoins(String dateStr) throws IOException{
        var filename = "M" + dateStr + ".csv";

        var filepath = path + "\\" + filename;

        String content = Files.readString(Paths.get(filepath));
        var coins = Coin.parseCoinTable(content);
        var result = new HashMap<String, Coin>(coins.length);
        for (var coin: coins){
            result.put(coin.getCode(), coin);
        }
        return result;
    }




}