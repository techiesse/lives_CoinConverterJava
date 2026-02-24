package com.techiesse.CoinConverter.bc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Coin {
    private String name;
    private String code;
    private String symbol;
    private CoinType type;
    private LocalDate exclusionDate;


    public Coin(
        String name,
        String code,
        String symbol,
        CoinType type,
        LocalDate exclusionDate
    ) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.type = type;
        this.exclusionDate = exclusionDate;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public CoinType getType() {
        return type;
    }

    public LocalDate getExclusionDate() {
        return exclusionDate;
    }

    public String toString() {
        return "Coin: " + name +
        "(codigo: " + code +
        ", simbolo: " + symbol +
        ", tipo: " + type +
        ")";
    }

    public boolean equals(Coin coin) {
        return code == coin.code && symbol == coin.symbol;
    }

    public static Coin fromBCCoin(String bcCoin) {
        // 225;DOLAR/ETIOPIA    ;ETB    ;;;A;04/08/1998
        // 0;  1                ;2    ;3;4;5;6
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] parts = bcCoin.split(";");

        Coin result = new Coin(
            parts[1].trim(),
            parts[0],
            parts[2],
            CoinType.fromString(parts[5]),
            parts.length > 6 ?
                LocalDate.parse(parts[6], formatter):
                null
        );
        return result;
    }

    public static Coin[] parseCoinTable(String bcCoins) {
        var lines = bcCoins.trim().split("\n");
        var result = new Coin[lines.length - 1];
        for (int i = 1; i < lines.length; ++i) {
            lines[i] = lines[i].trim();
            try {
                result[i-1] = fromBCCoin(lines[i]);
            }
            catch(IllegalArgumentException e){
                throw e;
            }
        }
        return result;
    }

}
