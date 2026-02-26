package com.techiesse.CoinConverter.bc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Coin {
    private String name;
    private String code;
    private String symbol;
    private CoinType type;
    private LocalDate exclusionDate;

    public static final String DOLLAR_CODE = "220";
    public static final String REAL_CODE = "790";


    public Coin(
        String name,
        String code,
        String symbol,
        CoinType type,
        LocalDate exclusionDate
    ) throws Exception {
        if (name == null || code == null || symbol == null || type == null) {
            throw new Exception("Coin attributes must not be null except for exclusionDate");
        }
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

    public static Coin fromBCCoin(String bcCoin) throws Exception {
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
            catch (Exception e) {
                result[i-1] = null;
            }
        }
        return result;
    }


    // Exemplo de como testar rapidamente uma lib. Não tem problema criar uma main.
    // Num código pra valer esse método deve ser removido após o teste.
    public static void main(String[] args) throws Exception {
        var coin = new Coin(null, null, null, null, null);
        System.out.println(coin.getCode()); //<<<<<
    }

}
