package com.example.usuario.pruebas;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Robinson Huacho on 06/10/2018.
 */

public class ValidadorTarjetaCredito {

    /* Regular expression containing the default format for displaying a card's number */
    private static String DEFAULT_CARD_FORMAT = "(\\d{1,4})";

    /**
     * enumeration representing the default cards used by Checkout
     * String name name of the card
     * String pattern regular expression matching the card's code
     * String format default card display format
     * int[] cardLength array containing all the possible lengths of the card's code
     * int[] cvvLength array containing all the possible lengths of the card's CVV
     * boolean luhn does the card's number respects the luhn validation or not
     * boolean supported is this card usable with Checkout services
     */
    public enum Cards {

        MAESTRO("maestro", "^(5[06-9]|6[37])[0-9]{10,17}$", DEFAULT_CARD_FORMAT, new int[]{12,13,14,15,16,17,18,19}, new int[]{3}, true, true),
        MASTERCARD("mastercard", "^5[1-5][0-9]{14}$", DEFAULT_CARD_FORMAT, new int[]{16,17}, new int[]{3}, true, true), //check supported
        DINERSCLUB("dinersclub", "^3(?:0[0-5]|[68][0-9])?[0-9]{11}$", "(\\d{1,4})(\\d{1,6})?(\\d{1,4})?", new int[]{14}, new int[]{3}, true, true), //check supported
        LASER("laser", "^(6304|6706|6709|6771)[0-9]{12,15}$", DEFAULT_CARD_FORMAT, new int[]{16,17,18,19}, new int[]{3}, true, false),
        JCB("jcb", "^(?:2131|1800|35[0-9]{3})[0-9]{11}$", DEFAULT_CARD_FORMAT, new int[]{16}, new int[]{3}, true, true), //check supported
        UNIONPAY("unionpay", "^(62[0-9]{14,17})$", DEFAULT_CARD_FORMAT, new int[]{16,17,18,19}, new int[]{3}, false, false),
        DISCOVER("discover", "^6(?:011|5[0-9]{2})[0-9]{12}$", DEFAULT_CARD_FORMAT, new int[]{16}, new int[]{3}, true, true), //check supported
        AMEX("amex", "^3[47][0-9]{13}$", "^(\\d{1,4})(\\d{1,6})?(\\d{1,5})?$", new int[]{15}, new int[]{4}, true, true), //check supported
        VISA("visa", "^4[0-9]{12}(?:[0-9]{3})?$", DEFAULT_CARD_FORMAT, new int[]{13,16}, new int[]{3}, true, true); //check supported

        private final String name;
        private final String pattern;
        private final String format;
        private final int[] cardLength;
        private final int[] cvvLength;
        private final boolean luhn;
        private final boolean supported;

        Cards(String name, String pattern, String format, int[] cardLength, int[] cvvLength, boolean luhn, boolean supported) {
            this.name = name;
            this.pattern = pattern;
            this.format = format;
            this.cardLength = cardLength;
            this.cvvLength = cvvLength;
            this.luhn = luhn;
            this.supported = supported;
        }
    }

    /* Regular expression used for sanitizing the card's name */
    public final static String CARD_NAME_REPLACE_PATTERN = "[^A-Z\\s]";

    /*
     * Test if the string is composed exclusively of digits
     * @param entry String to be tested
     * @return result of the test
     */
    private static boolean isDigit(String entry) {
        return Pattern.matches("^\\d+$", entry);
    }

    /*
     * Sanitizes the card's name using the regular expression above
     * @param name String to be cleaned
     * @return cleaned string
     */
    private static String sanitizeName(String name) {
        return name.toUpperCase().replaceAll(CARD_NAME_REPLACE_PATTERN, "");
    }

    /**
     * Sanitizes any string given as a parameter
     * @param entry String to be cleaned
     * @param isNumber boolean, if set, the method removes all non digit characters, otherwise only the - and spaces
     * @return cleaned string
     */
    public static String sanitizeEntry(String entry, boolean isNumber) {
        return isNumber ? entry.replaceAll("\\D", "") : entry.replaceAll("\\s+|-", "");
    }

    /**
     * Returns the Cards element corresponding to the given number
     * @param num String containing the card's number
     * @return Cards element corresponding to num or null if it was not recognized
     */
    public static Cards getCardType(String num) {
        num = sanitizeEntry(num, true);
        if (Pattern.matches("^(54)", num) && num.length() > 16) {
            return Cards.MAESTRO;
        }

        Cards[] cards = Cards.values();
        for (int i = 0; i < cards.length ; i++) {
            if (Pattern.matches(cards[i].pattern, num)) {
                return cards[i];
            }
        }
        return null;
    }

    /*
     * Applies the Luhn Algorithm to the given card number
     * @param num String containing the card's number to be tested
     * @return boolean containing the result of the computation
     */

    /**
     * Checks if the card's number is valid by identifying the card's type and checking its conditions
     // @param num String containing the card's code to be verified
     * @return boolean containing the result of the verification
     */
    public static boolean validateCardNumber(long number) {
        //long number = 4593531056752099L;
        if (isValid(number)) {
            return true;
        } else {
            return false;
        }
    }

    // Return true if the card number is valid
    public static boolean isValid(long number)
    {
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    public static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    public static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    public static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

    /**
     * Checks if the card is still valid
     * @param month String containing the expiring month of the card
     * @param year String containing the expiring year of the card
     * @return boolean containing the result of the verification
     */
    public static boolean validateExpiryDate(String month, String year) {
        if (year.length() != 4 && year.length() != 2) {
            return false;
        }
        int iMonth, iYear;
        try {
            iMonth = Integer.parseInt(month);
            iYear = Integer.parseInt(year);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return validateExpiryDate(iMonth, iYear);
    }

    /**
     * Checks if the card is still valid
     * @param month int containing the expiring month of the card
     * @param year int containing the expiring year of the card
     * @return boolean containing the result of the verification
     */
    public static boolean validateExpiryDate(int month, int year) {
        if (month < 1 || year < 1) return false;
        Calendar cal = Calendar.getInstance();
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curYear = cal.get(Calendar.YEAR);
        if(year < 100) curYear -= 2000;
        return (curYear == year) ? curMonth <= month : curYear < year;
    }

    /**
     * Checks if the CVV is valid for a given card's type
     * @param cvv String containing the value of the CVV
     * @param card Cards element containing the card's type
     * @return boolean containing the result of the verification
     */
    public static boolean validateCVV(String cvv, Cards card) {
        if (cvv.equals("") || card == null) return false;
        for (int i = 0 ; i < card.cvvLength.length ; i++) {
            if (card.cvvLength[i] == cvv.length()) return true;
        }
        return false;
    }

    /**
     * Checks if the CVV is valid for a given card's type
     * @param cvv int containing the value of the CVV
     * @param card Cards element containing the card's type
     * @return boolean containing the result of the verification
     */
    public static boolean validateCVV(int cvv, Cards card) {
        return validateCVV(String.valueOf(cvv), card);
    }

}
