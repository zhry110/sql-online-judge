package com.util;

import java.util.ArrayList;
import java.util.List;

public class Border {
    String[][] result = null;
    String[][] end = null;
    public Border(String[][] src) {
        if (src == null)
            return;
        if (src[0].length == 0)
            return;
        result = new String[src.length][src[0].length];
        int[] maxLen = new int[src[0].length];
        zeroArray(maxLen);
        for (int i = 0; i < src.length;i++)
        {
            if (src[i] == null)
                break;
            for (int k = 0; k < src[i].length;k++)
            {
                if (src[i][k] == null)
                    src[i][k] = " ";
                if (src[i][k].length() >= maxLen[k]) {
                    int len = src[i][k].length() - maxLen[k];
                    maxLen[k] = src[i][k].length();
                    String space = numSpace(len);
                    for (int s = 0; s < i; s++)
                    {
                        result[s][k] = space + result[s][k];
                    }
                    result[i][k] = src[i][k];
                }
                else {
                    int len = maxLen[k] - src[i][k].length();
                    String space = numSpace(len);
                    result[i][k] = space + src[i][k];
                }
            }
        }
        end = new String[result.length + 3][result[0].length];
        for (int i = 0;i < result[0].length;i++)
        {
            end[0][i] = boder(result[0][i].length() + 2);
            end[2][i] = boder(result[0][i].length() + 2);
            end[end.length - 1][i] = boder(result[0][i].length() + 2);
            if (i == (result[0].length - 1)) {
                end[0][i] += "+";
                end[2][i] += "+";
                end[end.length - 1][i] += "+";;
            }
        }
        for (int i = 0,c = 0; i < end.length;i++)
        {
            if (i == 0 || i == 2 || i == (end.length - 1))
                continue;
            for (int k = 0; k < end[0].length;k++)
            {
                end[i][k] = "| " + result[c][k] + " ";
                if (k == (end[0].length - 1))
                    end[i][k] += "|";
            }
            c++;
        }
        for (int i = 0; i < end.length;i++)
        {
            for (int k = 0;k < end[0].length;k++)
                System.out.print(end[i][k]);
            System.out.println();
        }
    }
    private void zeroArray(int[] arr)
    {
        if (arr != null)
        {
            for (int i = 0; i < arr.length;i++)
                arr[i] = 0;
        }
    }
    private String numSpace(int num)
    {
        String result = new String();
        for (int i = 0; i < num;i++)
            result += " ";
        return result;
    }
    private String boder(int len)
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
    public String[][] getResult()
    {
        return end;
    }
}
