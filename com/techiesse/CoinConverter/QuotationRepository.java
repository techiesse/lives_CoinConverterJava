package com.techiesse.CoinConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.techiesse.CoinConverter.bc.Coin;
import com.techiesse.CoinConverter.bc.Quotation;

public class QuotationRepository {
    private String path;
    private HashMap<String, Coin> coinTable;

    public QuotationRepository(String path, HashMap<String, Coin> coinTable) {
        this.path = path;
        this.coinTable = coinTable;
    }

    public HashMap<String, Quotation> getAllQuotations(LocalDate date) throws IOException{
        var dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return getAllQuotations(dateStr);
    }


    public HashMap<String, Quotation> getAllQuotations(String dateStr) throws IOException{
        var filename = dateStr + ".csv";

        var filepath = path + "\\" + filename;

        String content = Files.readString(Paths.get(filepath));
        var quotations = Quotation.parseQuotationTable(content, coinTable);
        var result = new HashMap<String, Quotation>(quotations.length);
        for (var quotation: quotations){
            result.put(quotation.getCode(), quotation);
        }

        var realQuotation = new Quotation(
            coinTable.get(Coin.REAL_CODE),
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd")),
            1, 1, result.get(Coin.DOLLAR_CODE).getBuyRate(), result.get(Coin.DOLLAR_CODE).getSellRate()
        );

        result.put(Coin.REAL_CODE, realQuotation);


        return result;
    }

}
