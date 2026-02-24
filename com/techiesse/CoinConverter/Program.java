package com.techiesse.CoinConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.techiesse.CoinConverter.bc.*;

public class Program {

    public static final String DOLLAR_CODE = "220";

    public static void testCoins() {
        //Coin dolar = new Coin(
        //    "DOLAR DOS EUA",
        //    "220",
        //    "USD",
        //    CoinType.A,
        //    null
        //);
        Quotation dolarQuotation = new Quotation(
            new Coin(
                "DOLAR DOS EUA",
                "220",
                "USD",
                CoinType.A,
                null
            ),
            LocalDate.parse("2026-02-10"),
            5.3784,
            5.3790,
            1.0000,
            1.0000
        );


        Quotation real = new Quotation(
            new Coin ("REAL BRASIL",
                "790",
                "BRL",
                CoinType.A,
                null),
            LocalDate.parse("2026-02-10"),
            5.3784,
            5.3790,
            1.0000,
            1.0000
        );

        var converter = new Converter(dolarQuotation);
        var convertedValue = converter.convertToReal(1, dolarQuotation);

        //System.out.println(convertedValue); //<<<<<
        try {
            System.out.println(converter.toDolar(convertedValue, real)); //<<<<<
        }
        catch(Exception e) {}

    }

    public static void testCoinParse() {
        var coinStr = "225;DOLAR/ETIOPIA    ;ETB    ;;;A;04/08/1998";

        var coin = Coin.fromBCCoin(coinStr);
        System.out.println(coin); //<<<<<
    }


    public static void main(String[] args) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        var amount = Double.parseDouble(args[0]);
        var fromCoinCode = args[1];
        var toCoinCode = args[2];
        var quotationDate = LocalDate.now().format(formatter);
        if (args.length > 3) {
            quotationDate = args[3];
        }


        // Montar a tabela de moedas
            // Baixar tabela de moedas do banco central:
        var downloader = new Downloader(
            "https://www4.bcb.gov.br/Download/fechamento",
            "data"
        );

        try {
            downloader.downloadCoinTable(quotationDate);
            downloader.downloadQuotations(quotationDate);
        }
        catch (InexistentQuotationException e) {
            System.out.println("Cotação não obtida no dia " + quotationDate);
            System.exit(1);
        }

        // Obter moedas:
        var coinRepo = new CoinRepository("data");
        HashMap<String, Coin> coins = null;
        try {
            coins = coinRepo.getAllCoins(quotationDate);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Montar a tabela de cotações:
        QuotationRepository quotationRepo = null;
        try {
            quotationRepo = new QuotationRepository("data", coins);
        }
        catch (Exception e) {
            // TODO: tratar exceção.
        }

        HashMap<String, Quotation> quotations = null;
        try {
            quotations = quotationRepo.getAllQuotations(quotationDate);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Converter moeda de origem para moeda de destino
            // Usar montante, simbolo da moeda de origem, simbolo da moeda alvo e data da cotação
        // Exibir o resultado.

        var dollarQuotation = quotations.get(DOLLAR_CODE);
        var converter = new Converter(dollarQuotation);
        try {
            var convertedAmount = converter.convert(
                amount,
                quotations.get(fromCoinCode),
                quotations.get(toCoinCode)
            );
            System.out.println(convertedAmount);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



}
