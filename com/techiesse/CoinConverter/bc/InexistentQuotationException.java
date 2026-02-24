package com.techiesse.CoinConverter.bc;

public class InexistentQuotationException extends Exception {
    public InexistentQuotationException() {
        super("Inexistent Quotation");
    }
}
