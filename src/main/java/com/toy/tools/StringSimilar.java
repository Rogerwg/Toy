package com.toy.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringSimilar {

    private static String[] ignore = new String[]{
            "《", "》", "（", "）", "<", ">", "【", "】", "(", ")", "/", "、", "+", "＋", "[", "]", "|", "-", "风景区", "景区", "省", "市", "镇", "县", "门票", "票",
    };

    public static double calcSimilarByCosineWord(String name1, String name2) {
        if (name1 == null || name2 == null) {
            return 0.0;
        }
        if (name1.equalsIgnoreCase(name2)) {
            return 1.0;
        }
        for (String stop : ignore) {
            name1 = name1.replace(stop, "");
            name2 = name2.replace(stop, "");
        }
        List<String> result1 = StringTools.getNlpWordSegment(name1.replaceAll("\\s*", ""));
        List<String> result2 = StringTools.getNlpWordSegment(name2.replaceAll("\\s*", ""));
        Map<String, int[]> AlgorithmMap = new HashMap<String, int[]>();
        for (String s : result1) {
            int[] num = AlgorithmMap.get(s);
            if (num != null && num.length == 2) {
                num[0]++;
            } else {
                num = new int[2];
                num[0] = 1;
                num[1] = 0;
                AlgorithmMap.put(s, num);
            }
        }
        for (String s : result2) {
            int[] num = AlgorithmMap.get(s);
            if (num != null && num.length == 2) {
                num[1]++;
            } else {
                num = new int[2];
                num[0] = 0;
                num[1] = 1;
                AlgorithmMap.put(s, num);
            }
        }
        Iterator<String> iterator = AlgorithmMap.keySet().iterator();
        double sqdoc1 = 0;
        double sqdoc2 = 0;
        double denominator = 0;
        while (iterator.hasNext()) {
            int[] c = AlgorithmMap.get(iterator.next());
            denominator += c[0] * c[1];
            sqdoc1 += c[0] * c[0];
            sqdoc2 += c[1] * c[1];
        }
        return denominator / Math.sqrt(sqdoc1 * sqdoc2);
    }

    public static double calcSimilarByIntersection(String name1, String name2) {
        if (name1 == null || name2 == null) {
            return 0.0;
        }
        if (name1.equalsIgnoreCase(name2)) {
            return 1.0;
        }
        for (String stop : ignore) {
            name1 = name1.replace(stop, "");
            name2 = name2.replace(stop, "");
        }
        List<String> result1 = StringTools.getNlpWordSegment(name1.replaceAll("\\s*", ""));
        List<String> result2 = StringTools.getNlpWordSegment(name2.replaceAll("\\s*", ""));
        List<String> max = new ArrayList<String>();
        List<String> min = new ArrayList<String>();
        if (result1.size() >= result2.size()) {
            max = result1;
            min = result2;
        } else {
            min = result1;
            max = result2;
        }
        int num = 0;
        for (String item : min) {
            if (max.contains(item)) {
                num++;
            }
        }
        return (double) num / (double) (min.size());
    }

    public static double calcSimilarByJaccard(String name1, String name2) {
        if (name1 == null || name2 == null) {
            return 0.0;
        }
        if (name1.equalsIgnoreCase(name2)) {
            return 1.0;
        }
        for (String stop : ignore) {
            name2 = name2.replace(stop, "");
            name1 = name1.replace(stop, "");
        }
        List<String> result1 = StringTools.getBasicWordSegment(name1.replaceAll("\\s*", ""));
        List<String> result2 = StringTools.getBasicWordSegment(name2.replaceAll("\\s*", ""));
        Set<String> inter = MathUtils.calcIntersection(result1, result2);
        Set<String> union = MathUtils.calcUnite(result1, result2);
        return (double) inter.size() / union.size();
    }

    public static double calcSimilarByCosineChar(String doc1, String doc2) {
        if (doc1 != null && doc1.trim().length() > 0 && doc2 != null && doc2.trim().length() > 0) {
            Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
            // 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if (isHanZi(d1)) {
                    int charIndex = getGB2312Id(d1);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[0]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }
            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if (isHanZi(d2)) {
                    int charIndex = getGB2312Id(d2);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[1]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }
            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0;
            while (iterator.hasNext()) {
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0] * c[1];
                sqdoc1 += c[0] * c[0];
                sqdoc2 += c[1] * c[1];
            }
            return denominator / Math.sqrt(sqdoc1 * sqdoc2);
        } else {
            return 0.0;
        }
    }

    public static boolean isHanZi(char ch) {
        // 判断是否汉字
        return (ch >= 0x4E00 && ch <= 0x9FA5);

    }

    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                return -1;
            }
            int b0 = (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            String errStr = StringTools.getErrString(e);
            log.error(errStr);
        }
        return -1;
    }

    /**
     * @param name1
     * @param name2
     * @return
     */
    public static boolean isSimilarByED(String name1, String name2) {
        if (StringUtils.isEmpty(name1) || StringUtils.isEmpty(name2)) {
            return false;
        }
        //根据编辑距离和字符串包含关系，模糊判断是否属于相同名称
        return MathUtils.relativeEditDistance(name1, name2) > 0.8;
    }

    public static int getLCStringSize(char[] str1, char[] str2) {
        int size = 0;
        int len1, len2;
        len1 = str1.length;
        len2 = str2.length;
        int maxLen = len1 > len2 ? len1 : len2;

        int[] max = new int[maxLen];// 保存最长子串长度的数组
        int[] maxIndex = new int[maxLen];// 保存最长子串长度最大索引的数组
        int[] c = new int[maxLen];

        int i, j;
        for (i = 0; i < len2; i++) {
            for (j = len1 - 1; j >= 0; j--) {
                if (str2[i] == str1[j]) {
                    if ((i == 0) || (j == 0))
                        c[j] = 1;
                    else
                        c[j] = c[j - 1] + 1;//此时C[j-1]还是上次循环中的值，因为还没被重新赋值
                } else {
                    c[j] = 0;
                }

                // 如果是大于那暂时只有一个是最长的,而且要把后面的清0;
                if (c[j] > max[0]) {
                    max[0] = c[j];
                    maxIndex[0] = j;

                    for (int k = 1; k < maxLen; k++) {
                        max[k] = 0;
                        maxIndex[k] = 0;
                    }
                }
                // 有多个是相同长度的子串
                else if (c[j] == max[0]) {
                    for (int k = 1; k < maxLen; k++) {
                        if (max[k] == 0) {
                            max[k] = c[j];
                            maxIndex[k] = j;
                            break; // 在后面加一个就要退出循环了
                        }
                    }
                }
            }
            for (int temp : c) {
                System.out.print(temp);
            }
            System.out.println();
        }
        for (j = 0; j < maxLen; j++) {
            if (max[j] > 0) {
                for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++) {
                    size++;
                }
            }
        }
        return size;
    }


}
