package com.util;

public class NewBorder {
    public static String addBorder(String[][] src) {
        if (src == null)
            return null;
        if (src[0] == null)
            return null;
        int columns = src[0].length;
        int rows = src.length;
        int[] maxLength = new int[columns];
        for (int i = 0; i < columns; i++) {
            for (int k = 0; k < rows; k++) {
                if (src[k] == null)
                    src[k] = new String[columns];
                if (src[k][i] == null) {
                    src[k][i] = "";
                }
                if (k == 0) {
                    maxLength[i] = src[k][i].length();
                } else {
                    if (src[k][i].length() > maxLength[i]) {
                        maxLength[i] = src[k][i].length();
                    }
                }
            }
        }
        for (int i = 0; i < columns; i++) {
            for (int k = 0; k < rows; k++) {
                int sub = maxLength[i] - src[k][i].length();
                if (sub > 0) {
                    src[k][i] = numSpace(sub) + src[k][i];
                }
                src[k][i] = "| " + src[k][i] + " ";
                if (i == columns - 1) {
                    src[k][i] += "|";
                }
            }

        }
        String result = new String();
        for (int i = 0; i < rows;i++) {
            if (i == 0) {
                result += boderLine(maxLength) ;
            } else if (i == 1) {
                result += boderLine(maxLength);
            }
            for (int k = 0; k < columns;k++) {
                result += src[i][k];
                System.out.print(src[i][k]);
            }
            result += "<br>";
            System.out.println();
        }
        result += boderLine(maxLength);
        return result;
    }
    private static String numSpace(int num)
    {
        String result = new String();
        for (int i = 0; i < num;i++)
            result += " ";
        return result;
    }
    private static String boder(int len)
    {
        if (len == 0)
            return "";
        String result = "+";
        for (int i = 0; i < len; i++)
        {
            result += "-";
        }
        return result;
    }
    private static String boderLine(int[] lens)
    {
        String result = new String();
        for (int i = 0; i < lens.length;i++) {
            result += boder(lens[i] + 2);
        }
        result += "+<br>";
        return result;
    }
}
