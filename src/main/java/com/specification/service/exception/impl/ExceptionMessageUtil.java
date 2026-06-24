package com.specification.service.exception.impl;


import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageUtil {

    public static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected Error";

    @Bean
    public static MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static String getMultiLingualMessage(String code) {
        return getMultiLingualMessage(code, "ENG");
    }

    public static String getMultiLingualMessageWithArgs(String code, Object... args) {
        return getMultiLingualMessage(String.format(code, args), "ENG");
    }

    public static String getMultiLingualMessage(String code, String languageCode) {
        Locale locale = Locale.forLanguageTag("ENG");
        String key = code + "." + locale;
        return messageSource().getMessage(key, null, "Unexpected Error", locale);
    }

    public static String extractGenericMessage(String exceptionMessage) {
        if (exceptionMessage == null) {
            return null;
        } else {
            String[] splitByColon = exceptionMessage.split(":", 2);
            String mainPart = splitByColon[0].trim();
            int periodIndex = mainPart.indexOf(46);
            if (periodIndex > 0) {
                mainPart = mainPart.substring(0, periodIndex).trim();
            }

            return mainPart;
        }
    }
}

