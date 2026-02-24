package com.techiesse.CoinConverter;

import com.techiesse.CoinConverter.bc.CoinType;
import com.techiesse.CoinConverter.bc.Quotation;

public class Converter {
    private Quotation dolar;

    public Converter(Quotation dolar) {
        this.dolar = dolar;
    }

    public double convertToReal(double amount, Quotation quotation) {
        return amount * quotation.getSellRate();
    }

    public double toDolar(double amount, Quotation fromQuotation) throws Exception{
        if (fromQuotation.getCoinSymbol() == "BRL"){
            System.out.println("Convertendo de real para dolar"); //<<<<<
            return amount / dolar.getSellRate();
        }
        if (fromQuotation.getCoinType().equals(CoinType.A)) {
            return amount / fromQuotation.getSellParity();
        }
        else if (fromQuotation.getCoinType() == CoinType.B){
            return amount * fromQuotation.getSellParity();
        }
        else {
            // TODO: Criar exception especifica
            throw new Exception("Tipo de moeda não previsto: " +
                                fromQuotation.getCoinType().toString());
        }
    }

    public double fromDolar(double amount, Quotation toQuotation) throws Exception{
        if (toQuotation.getCoinSymbol() == "BRL"){
            System.out.println("Convertendo de dolar para real"); //<<<<<
            return amount * dolar.getSellRate();
        }
        if (toQuotation.getCoinType().equals(CoinType.A)) {
            return amount * toQuotation.getSellParity();
        }
        else if (toQuotation.getCoinType() == CoinType.B){
            return amount / toQuotation.getSellParity();
        }
        else {
            // TODO: Criar exception especifica
            throw new Exception("Tipo de moeda não previsto: " +
                                toQuotation.getCoinType().toString());
        }
    }

    public double convert(double amount, Quotation fromQuotation, Quotation toQuotation) throws Exception {
        //System.out.println(fromQuotation); //<<<<<
        //System.out.println(toQuotation); //<<<<<
        return fromDolar(toDolar(amount, fromQuotation), toQuotation);
    }

    public boolean equals(Converter other) {
        return dolar.equals(other.dolar);
    }

}
