package services;

public class Base62 {

    public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int BASE = CHARACTERS.length();

    private Base62() {
        throw new RuntimeException();
    }

    public static String fromBase10(long i) {
        StringBuilder sb = new StringBuilder("");
        if (i == 0) {
            return "a";
        }
        while (i > 0) {
            int rem = (int)(i % BASE);
            sb.append(CHARACTERS.charAt(rem));
            i = i / BASE;
        }
        return sb.reverse().toString();
    }

    public static long toBase10(String str) {
        if (str == null || str.isEmpty()) {
            return -1;
        }

        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            char charIndex = str.charAt(str.length() -  1 - i);
            result += CHARACTERS.indexOf(charIndex) * Math.pow(BASE, i);
        }
        return result;
    }

}
