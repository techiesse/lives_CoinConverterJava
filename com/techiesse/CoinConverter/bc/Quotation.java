package com.techiesse.CoinConverter.bc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Quotation {
    private Coin coin;
    private LocalDate date;
    private double buyRate;
    private double sellRate;
    private double buyParity;
    private double sellParity;

    public Quotation(
        Coin coin,
        LocalDate date,
        double buyRate,
        double sellRate,
        double buyParity,
        double sellParity
    ) {
        this.coin = coin;
        this.date = date;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.buyParity = buyParity;
        this.sellParity = sellParity;
    }

    public String getCode() {
        return coin.getCode();
    }

    public String getSymbol() {
        return coin.getSymbol();
    }

    public double getBuyRate() {
        return buyRate;
    };

    public double getSellRate() {
        return sellRate;
    };

    public double getBuyParity() {
        return buyParity;
    };

    public double getSellParity() {
        return sellParity;
    };

    public String getCoinSymbol() {
        return coin.getSymbol();
    }

    public CoinType getCoinType() {
        return coin.getType();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(coin.getName()).append(", ");
        sb.append(date.toString()).append(", ");
        sb.append(String.valueOf(buyRate)).append(", ");
        sb.append(String.valueOf(sellRate)).append(", ");
        sb.append(String.valueOf(buyParity)).append(", ");
        sb.append(String.valueOf(sellParity));

        return sb.toString();
    }

    public static Quotation fromBCQuotation(String bcQuotation, HashMap<String, Coin> coinTable) {
        // 03/02/2026;005;A;AFN;0,07985000;0,08010000;65,21000000;65,41000000
        // 0         ;  1;2;3  ;4         ;5         ;6          ;7
        String[] parts = bcQuotation.split(";");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var quotationDate = LocalDate.parse(parts[0], formatter);

        var coinCode = parts[1];
        var coin = coinTable.get(coinCode);

        Quotation result = new Quotation(
            coin,
            quotationDate,
            Double.parseDouble(parts[4].replace(",", ".")), // buyRate
            Double.parseDouble(parts[5].replace(",", ".")), // sellRate
            Double.parseDouble(parts[6].replace(",", ".")), // buyParity
            Double.parseDouble(parts[7].replace(",", "."))  // sellParity
        );
        return result;
    }

    public static Quotation[] parseQuotationTable(String bcQuotations, HashMap<String, Coin> coinTable) {
        var lines = bcQuotations.trim().split("\n");
        var result = new Quotation[lines.length];
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = lines[i].trim();
            try {
                result[i] = fromBCQuotation(lines[i], coinTable);
            }
            catch(IllegalArgumentException e){
                throw e;
            }
        }
        return result;
    }
}
