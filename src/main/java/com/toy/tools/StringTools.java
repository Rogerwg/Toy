package com.toy.tools;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;

public class StringTools {
    public static String byteArrayToString(byte[] bytes, String code) throws UnsupportedEncodingException {
        return new String(bytes, code);
    }

    public static byte[] StringTOByteArray(String content, String code) throws UnsupportedEncodingException {
        return content.getBytes(code);
    }

    public static String trimSyntax(String content) {
        return content.trim().replaceAll("'", "’");
    }

    public static String trimChar(String content) {
        if (content != null) {
            return content.trim().replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&lt", "").replaceAll("&gt", "").replaceAll("&nbsp;", " ");
        } else {
            return "";
        }
    }

    public static boolean isBlank(String content) {
        return org.apache.commons.lang3.StringUtils.isBlank(content);
    }

    public static boolean isNotBlank(String content) {
        return (!(isBlank(content)));
    }

    public static String trimToEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.trimToEmpty(str);
    }

    public static boolean isNumeric(CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNumeric(cs);
    }

    private final static SimpleDateFormat STD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat STD_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getSTDDate() {
        synchronized (STD_TIME_FORMAT) {
            return STD_TIME_FORMAT.format(Calendar.getInstance().getTime());
        }
    }

    public static boolean isValidDate(String inDate) {
        if (inDate == null) {
            return false;
        }

        if (inDate.trim().length() != STD_DATE_FORMAT.toPattern().length()) {
            return false;
        }

        STD_DATE_FORMAT.setLenient(false);

        try {
            synchronized (STD_DATE_FORMAT) {
                STD_DATE_FORMAT.parse(inDate.trim());
            }
        } catch (Exception pe) {
            return false;
        }
        return true;
    }

    public static String getRandomKey(String str) {
        return String.valueOf(Calendar.getInstance().getTime().getTime()) + str;
    }

    public static String getErrString(Exception e) {
        StringBuilder err = new StringBuilder();
        err.append(e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            err.append("\n " + trace[i]);
        }

        return err.toString();
    }

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    public static String handlePoiCode(String strPoi) {
        strPoi = strPoi.trim();
        if (strPoi == null || "".equals(strPoi)) {
            return "";
        } else {
            if (strPoi.length() < 14) {
                return strPoi;
            } else {
                strPoi = strPoi.substring(0, 14);
                return strPoi;
            }
        }
    }

    public static String handleRegex(String strHtml, String rexExp) {
        if (strHtml == null || strHtml.trim().equals("")) {
            return "";
        }
        try {
            Pattern p = Pattern.compile(rexExp);
            Matcher m = p.matcher(strHtml);
            String s = m.replaceAll("").trim();
            return s;

        } catch (PatternSyntaxException e) {
            return strHtml;
        }
    }

    public static String replaceDBC2SBC(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        try {
            Pattern pattern = Pattern.compile("[\u3000\uff01-\uff5f]{1}");
            Matcher m = pattern.matcher(str);
            StringBuffer s = new StringBuffer();
            while (m.find()) {
                char c = m.group(0).charAt(0);
                char replacedChar = c == '　' ? ' ' : (char) (c - 0xfee0);
                m.appendReplacement(s, String.valueOf(replacedChar));
            }
            m.appendTail(s);
            return s.toString();
        } catch (PatternSyntaxException e) {
            return str;
        }
    }

    public static String specialCharFilter(String str) {
        String regEx = "[〒`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_-]";
        return StringTools.handleRegex(str, regEx).replaceAll(" ", "");
    }

    public static String transCNToAra(String str) {
        // "一般"，
        String[] CN = {
                "一", "两|二", "三", "四", "五", "六", "七", "八", "九", "十"
        };
        for (int i = 0; i < CN.length; i++) {
            str = str.replaceAll(CN[i], String.valueOf(i + 1));
        }
        str = str.replaceAll("十五", String.valueOf(15)).replaceAll("二十", String.valueOf(20)).replaceAll("三十", String.valueOf(30));
        return str.trim();
    }

    /**
     * 中文数字转阿拉伯数字【十万九千零六十 --> 109060】
     *
     * @param chineseNumber
     * @return
     */
    public static int chnNum2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;// 存放一个单位的数字如：十万
        int count = 0;// 判断是否有chArr
        char[] cnArr = new char[]{
                '一', '二', '三', '四', '五', '六', '七', '八', '九'
        };
        char[] chArr = new char[]{
                '十', '百', '千', '万'
        };
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;// 判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {// 非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {// 单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {// 遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    public static boolean isStringContains(String s1, String s2) {
        if (!StringUtils.isEmpty(s1) && !StringUtils.isEmpty(s2)) {
            String min = s1.length() >= s2.length() ? s2 : s1;
            String max = s1.length() < s2.length() ? s2 : s1;
            if (max.contains(min)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<String> getNlpWordSegment(String content) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isEmpty(content)) {
            return result;
        }
        List<Term> terms = NLPTokenizer.segment(content.trim());
        for (Term item : terms) {
            result.add(item.word);
        }
        return result;
    }

    public static List<String> getBasicWordSegment(String content) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isEmpty(content)) {
            return result;
        }
        List<Term> terms = BasicTokenizer.segment(content.trim());
        for (Term item : terms) {
            result.add(item.word);
        }
        return result;
    }

    /**
     * Description: <br>
     * 提取文本中的地名
     *
     * @param content
     * @return <br>
     * @author wanggang3<br>
     * @taskId <br>
     */
    public static List<String> placeNER(String content) {
        List<String> place = new ArrayList<String>();
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> termList = segment.seg(content);
        for (Term term : termList) {
            if (Nature.ns == term.nature || Nature.nsf == term.nature) {
                place.add(term.word);
            }
        }
        return place;
    }

    public static String transTc2Sc(String str) {
        return HanLP.convertToSimplifiedChinese(str);
    }

    //去掉字段值最前面的空格和最后面的空格，字段值中间的多个空格转化为一个空格
    public static String blankHandler(String str) {
        String result = str.trim();
        result = result.replaceAll(" +", " ");
        return result;
    }

}
