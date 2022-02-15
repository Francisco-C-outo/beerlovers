package com.objectiveplatform.beerlovers.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericUtil {
    public static boolean isStringAMatch(String stringToCompare, String inputString) {
        final String expression = ".*" + inputString.toLowerCase(Locale.ROOT) + ".*";

        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(stringToCompare.toLowerCase(Locale.ROOT));
        return m.matches();
    }
}
