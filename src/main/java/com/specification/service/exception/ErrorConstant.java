package com.specification.service.exception;

public interface ErrorConstant {
    String PLATFORM_ID = "platform-exception";

    public static enum CATEGORY {
        BV,
        TW,
        TU,
        NT,
        TS,
        TH,
        TD,
        TI,
        PT;
    }

    public static enum SEVERITY {
        C,
        M,
        L,
        I;
    }
}

