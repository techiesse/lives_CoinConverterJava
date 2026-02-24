package com.techiesse.CoinConverter.bc;

public enum CoinType {
    A,
    B;

    static CoinType fromString(String value) {
        try {
            return CoinType.valueOf(value);
        }
        catch (Exception e) {
            return null;
        }
    }
}
