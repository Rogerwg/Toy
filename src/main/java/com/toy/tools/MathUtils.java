package com.toy.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.util.CollectionUtils;


public class MathUtils {
    private static final double ZERO_VALUE = 0.000000001;

    private static final double EDIT_DISTANCE_LEVEL1 = 10.0;

    private static final double EDIT_DISTANCE_LEVEL2 = 1.5;

    /**
     * 编辑距离
     *
     * @param s1
     * @param s2
     * @return
     * @author chengxiangfei
     */
    public static int editDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] matrix = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++)
                matrix[i][j] = i + j;
        }

        TreeSet<Integer> comp;
        int row;
        int col;
        for (row = 0; row < len1; row++) {
            for (col = 0; col < len2; col++) {
                comp = new TreeSet<Integer>();
                comp.add(matrix[row + 1][col] + 1);
                comp.add(matrix[row][col + 1] + 1);

                if (s1.charAt(row) == s2.charAt(col)) {
                    comp.add(matrix[row][col]);
                } else {
                    comp.add(matrix[row][col] + 1);
                }
                matrix[row + 1][col + 1] = comp.first();

                comp.clear();
            }
        }

        return matrix[len1][len2];
    }

    /**
     * 编辑距离
     *
     * @param s1
     * @param s2
     * @return
     * @author chengxiangfei
     */
    public static double relativeEditDistance(String s1, String s2) {
        int value = editDistance(StringTools.specialCharFilter(s1), StringTools.specialCharFilter(s2));
        double maxLength;
        double minLength;

        if (s1.length() > s2.length()) {
            maxLength = s1.length();
            minLength = s2.length();
        } else {
            maxLength = s2.length();
            minLength = s1.length();
        }

        // 如果maxLength是minLength数倍，是否削弱某些长字符串的影响作用
        if (maxLength / minLength > EDIT_DISTANCE_LEVEL1) {
            maxLength = minLength * EDIT_DISTANCE_LEVEL2;
        }
        return 1 - value / maxLength;
    }

    /**
     * 欧式距离
     *
     * @param vec1
     * @param vec2
     * @return
     * @author s.cg
     */
    public static double distanceOfVectorEuclidSQ(HashMap vec1, HashMap vec2) {
        double result = 0.0;
        String key;
        Entry item;
        Iterator iterator;
        double value1;
        double value2;

        if (vec1.size() != 0) {
            iterator = vec1.entrySet().iterator();
            while (iterator.hasNext()) {
                item = (Entry) iterator.next();
                key = (String) item.getKey();
                value1 = ((Double) item.getValue()).doubleValue();

                if (vec2.containsKey(key)) {
                    value2 = ((Double) vec2.get(key)).doubleValue();

                    result += Math.pow(value1 - value2, 2);
                } else {
                    result += value1 * value1;
                }
            }
        }

        if (vec2.size() != 0) {
            iterator = vec2.entrySet().iterator();
            while (iterator.hasNext()) {
                item = (Entry) iterator.next();
                key = (String) item.getKey();
                value2 = ((Double) item.getValue()).doubleValue();

                if (!vec1.containsKey(key)) {
                    result += value2 * value2;
                }
            }
        }

        return result;
    }

    /**
     * 余弦距离
     *
     * @param vec1
     * @param vec2
     * @return
     * @author s.cg
     */
    public static double distanceOfVectorCos(HashMap vec1, HashMap vec2) {
        double result = 0.0;
        String key;
        Entry item;
        Iterator iterator;
        double value1;
        double value2;
        double vec1Value = 0;
        double vec2Value = 0;

        if (vec1.size() != 0) {
            iterator = vec1.entrySet().iterator();
            while (iterator.hasNext()) {
                item = (Entry) iterator.next();
                key = (String) item.getKey();
                value1 = ((Double) item.getValue()).doubleValue();

                if (vec2.containsKey(key)) {
                    value2 = ((Double) vec2.get(key)).doubleValue();

                    result += value1 * value2;
                }

                vec1Value += value1 * value1;
            }
        } else {
            return Double.MAX_VALUE;
        }

        if (vec2.size() != 0) {
            // iterator = vec2.entrySet().iterator();
            // while(iterator.hasNext())
            // {
            // item = (Entry)iterator.next();
            // key = (String)item.getKey();
            // value2 = ((Double)item.getValue()).doubleValue();
            //
            // vec2Value += value2 * value2;
            // }
            vec2Value = calcVecValueEuclidSQ(vec2);
        } else {
            return Double.MAX_VALUE;
        }

        if ((Math.abs(vec1Value) < ZERO_VALUE) || (Math.abs(vec2Value) < ZERO_VALUE)) {
            return Double.MAX_VALUE;
        }

        result = result / Math.sqrt(vec1Value * vec2Value);
        return result;
    }

    public static HashMap calcVector(HashMap vec, String oper, double value) {
        String key;
        Entry item;
        Iterator iterator;
        double src;

        iterator = vec.entrySet().iterator();
        while (iterator.hasNext()) {
            item = (Entry) iterator.next();
            key = (String) item.getKey();
            src = ((Double) item.getValue()).doubleValue();

            vec.put(key, mathOper(src, oper, value));
        }

        return vec;
    }

    public static HashMap calcVector(HashMap vec1, HashMap vec2, String oper1, String oper2,
                                     double value) {
        String key;
        Entry item;
        Iterator iterator;
        double value1;
        double value2;

        HashMap result = new HashMap<String, Double>();

        if (vec1.size() != 0) {
            result.putAll(vec1);
        }

        if (vec2.size() != 0) {
            iterator = vec2.entrySet().iterator();
            while (iterator.hasNext()) {
                item = (Entry) iterator.next();
                key = (String) item.getKey();
                value2 = ((Double) item.getValue()).doubleValue();

                value1 = (Double) result.get(key);
                result.put(key, mathOper(mathOper(value1, oper1, value2), oper2, value));
            }
        }

        return result;
    }

    public static void cumCalcVector(HashMap vec1, HashMap vec2, String oper) {
        String key;
        Entry item;
        Iterator iterator;
        double value1;
        double value2;

        if (vec2.size() != 0) {
            iterator = vec2.entrySet().iterator();
            while (iterator.hasNext()) {
                item = (Entry) iterator.next();
                key = (String) item.getKey();
                value2 = ((Double) item.getValue()).doubleValue();

                if (vec1.containsKey(key)) {
                    value1 = (Double) vec1.get(key);
                } else {
                    value1 = 0;
                }

                vec1.put(key, mathOper(value1, oper, value2));
            }
        }
    }

    public static double calcVecValueEuclidSQ(HashMap vec) {
        String key;
        Entry item;
        Iterator iterator;
        double value;
        double vecValue = 0.0;

        if (CollectionUtils.isEmpty(vec)) {
            return 0.0;
        }

        iterator = vec.entrySet().iterator();
        while (iterator.hasNext()) {
            item = (Entry) iterator.next();
            key = (String) item.getKey();
            value = ((Double) item.getValue()).doubleValue();

            vecValue += value * value;
        }

        return vecValue;
    }

    public static int vecCompare(HashMap vec1, HashMap vec2) {
        double value1 = calcVecValueEuclidSQ(vec1);
        double value2 = calcVecValueEuclidSQ(vec2);
        double diff = value1 - value2;
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }

        return 0;
    }

    /**
     * 事实上这个函数实现的内容正是稀疏向量间的欧式距离，但是由于创建了新的距离向量， 因此，实际上认为是浪费了内存，也许还浪费了时间，“实际上获取了3个迭代器，3次循环” 更推荐使用Math.sqrt(distanceOfVectorEuclid(...))
     * 此函数的主要是为了将计算分拆为两步，你也可以认为是我脑子抽了，一个功能实现两遍
     *
     * @param vec1
     * @param vec2
     * @return
     * @author s.cg
     * @see MathUtils#distanceOfVectorEuclidSQ(HashMap, HashMap)
     */
    public static double vecDistance(HashMap vec1, HashMap vec2) {
        HashMap vec = calcVector(vec1, vec2, "-", null, 0);
        return Math.sqrt(calcVecValueEuclidSQ(vec));
    }

    private static double mathOper(double src, String oper, double value) {
        if (oper.equals("+")) {
            return src + value;
        } else if (oper.equals("-")) {
            return src - value;
        } else if (oper.equals("*")) {
            return src * value;
        } else if (oper.equals("/")) {
            if (value == 0) {
                return Double.MAX_VALUE;
            }
            return src / value;
        }

        return src;
    }

    public static double[] supportSorce(HashMap campaigner, HashMap voter) {
        double[] reuslt = new double[2];

        Iterator iterator = campaigner.entrySet().iterator();
        Entry item;
        String key;
        double value1;
        double value2;
        double score1 = 0.0;
        double score2 = 0.0;

        while (iterator.hasNext()) {
            item = (Entry) iterator.next();
            key = (String) item.getKey();
            value1 = (Double) item.getValue();

            if (isInfiniteOrNaN(value1)) {
                value1 = 0;
            }

            if (voter.containsKey(key)) {
                value2 = (Double) voter.get(key);

                if (isInfiniteOrNaN(value2)) {
                    value2 = 0;
                }

                score1 += value1 * value1;
                score2 += value2 * value2;
            }
        }
        score1 = Math.sqrt(score1);
        score2 = Math.sqrt(score2);

        reuslt[0] = score1;
        reuslt[1] = score2;

        return reuslt;
    }

    public static boolean isInfiniteOrNaN(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return true;
        }

        return false;
    }

    public static boolean isInfiniteOrNaN(int value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return true;
        }

        return false;
    }

    public static Set<String> calcIntersection(List<String> set1, List<String> set2) {
        Set<String> intersection = new HashSet<String>();
        if (CollectionUtils.isEmpty(set1) || CollectionUtils.isEmpty(set2)) {
            return intersection;
        }
        if (set1 != null) {
            intersection.addAll(set1);
        }

        if (set2 != null) {
            intersection.retainAll(set2);
        }

        return intersection;
    }

    public static <T> Set<T> calcIntersection(Set<T> set1, Set<T> set2) {
        Set<T> intersection = new HashSet<T>();
        if (CollectionUtils.isEmpty(set1) || CollectionUtils.isEmpty(set2)) {
            return intersection;
        }
        if (set1 != null) {
            intersection.addAll(set1);
        }

        if (set2 != null) {
            intersection.retainAll(set2);
        }

        return intersection;
    }

    public static Set<String> calcUnite(List<String> set1, List<String> set2) {
        Set<String> unite = new HashSet<String>();
        if (set1 != null) {
            unite.addAll(set1);
        }

        if (set2 != null) {
            unite.addAll(set2);
        }
        return unite;
    }


    public static <T> Set<T> calcUnite(Set<T> set1, Set<T> set2) {
        Set<T> unite = new HashSet<T>();
        if (set1 != null) {
            unite.addAll(set1);
        }

        if (set2 != null) {
            unite.addAll(set2);
        }

        return unite;
    }

    public static <T> Set<T> calcSubtract(Set<T> set1, Set<T> set2) {
        Set<T> subtract = new HashSet<T>();
        if (set1 != null) {
            subtract.addAll(set1);
        }

        if (set2 != null) {
            subtract.removeAll(set2);
        }

        return subtract;
    }

    public static <T extends Number> double calcAverage(List<T> nums) {
        if (nums == null || nums.size() == 0) {
            return 0.0;
        }

        double age = 0.0;

        for (int i = 0; i < nums.size(); i++) {
            age += nums.get(i).doubleValue();
        }

        age = age / nums.size();

        return age;
    }
}
