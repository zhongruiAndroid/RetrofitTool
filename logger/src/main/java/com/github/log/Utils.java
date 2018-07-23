//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;

final class Utils {
    private Utils() {
    }

    static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    static boolean equals(CharSequence a, CharSequence b) {
        if(a == b) {
            return true;
        } else {
            if(a != null && b != null) {
                int length = a.length();
                if(length == b.length()) {
                    if(a instanceof String && b instanceof String) {
                        return a.equals(b);
                    }

                    for(int i = 0; i < length; ++i) {
                        if(a.charAt(i) != b.charAt(i)) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    static String getStackTraceString(Throwable tr) {
        if(tr == null) {
            return "";
        } else {
            for(Throwable t = tr; t != null; t = t.getCause()) {
                if(t instanceof UnknownHostException) {
                    return "";
                }
            }

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }
    }

    static String logLevel(int value) {
        switch(value) {
            case 2:
                return "VERBOSE";
            case 3:
                return "DEBUG";
            case 4:
                return "INFO";
            case 5:
                return "WARN";
            case 6:
                return "ERROR";
            case 7:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    public static String toString(Object object) {
        return object == null?"null":(!object.getClass().isArray()?object.toString():(object instanceof boolean[]?Arrays.toString((boolean[])((boolean[])object)):(object instanceof byte[]?Arrays.toString((byte[])((byte[])object)):(object instanceof char[]?Arrays.toString((char[])((char[])object)):(object instanceof short[]?Arrays.toString((short[])((short[])object)):(object instanceof int[]?Arrays.toString((int[])((int[])object)):(object instanceof long[]?Arrays.toString((long[])((long[])object)):(object instanceof float[]?Arrays.toString((float[])((float[])object)):(object instanceof double[]?Arrays.toString((double[])((double[])object)):(object instanceof Object[]?Arrays.deepToString((Object[])((Object[])object)):"Couldn\'t find a correct type for the object"))))))))));
    }
}
