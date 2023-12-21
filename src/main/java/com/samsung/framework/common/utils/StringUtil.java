package com.samsung.framework.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 유틸
 */
@Slf4j
@Component
public class StringUtil {

    private static long dummy = 0L;
    /**
     * 생성자를 private으로 지정하여 new 키워드로의 instance 생성을 방지한다.
     */
    private StringUtil() {}

    /**
     * @desc 첫번째 파라미터 값이 NULL인지 체크 한 후 NULL 이라면 두번째 인자로 반환하고 아니라면 첫번째 인자를 반환한다.
     * @param str String
     * @param defaultValue String
     * @return String
     */
    public static String getString(String str, String defaultValue) {
        return (str == null || str.equals("")) ? defaultValue : str.trim();
        // return NVL(str, defaultValue);
    }
    /**
     * @desc 첫번째 파라미터 값이 NULL인지 체크 한 후 NULL 이라면 두번째 인자로 반환하고 아니라면 첫번째 인자를 반환한다.
     * @param obj Object
     * @param defaultValue String
     * @return String
     */
    public static String getString(Object obj, String defaultValue) {
        return (obj == null || obj.equals("")) ? defaultValue : ((String) obj).trim();
        // return NVL(str, defaultValue);
    }

    /**
     * @desc 첫번째 파라미터 값이 NULL인지 체크 한 후 NULL 이라면 빈 값을 반환하고 아니라면 첫번째 인자를 반환한다.
     * @param obj Object
     * @return String
     */
    public static String getString(Object obj) {
        return NVL(obj, "");
    }

    /**
     * @desc 첫번째 파라미터 값이 NULL인지 체크 한 후 NULL 이라면 빈 값을 반환하고 아니라면 첫번째 인자를 반환한다.
     * @param str String
     * @return String
     */
    public static String getString(String str) {
        return NVL(str, "");
    }

    /**
     * 문자열의 교체 (legacy)
     *
     * @param obj Object: 문자열
     * @param oldsub String: 변경시킬 문자열
     * @param newsub String: 변경할 문자열
     * @return String
     * @see java.lang.String#replaceAll(String, String)
     */
    public static String replace(Object obj, String oldsub, String newsub) {
        if ((obj == null) || (oldsub == null) || (newsub == null)) {
            return null;
        }
        return replace(getString(obj), oldsub, newsub);
    }

    /**
     * 문자열의 교체 (legacy)
     *
     * @param str String: 문자열
     * @param oldsub String: 변경시킬 문자열
     * @param newsub String: 변경할 문자열
     * @return String
     * @see java.lang.String#replaceAll(String, String)
     */
    public static String replace(String str, String oldsub, String newsub) {
        if ((str == null) || (oldsub == null) || (newsub == null)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = oldsub.length();
        int x = 0;
        int y = str.indexOf(oldsub);

        while (x <= y) {
            sb.append(str.substring(x, y));
            sb.append(newsub);
            x = y + length;
            y = str.indexOf(oldsub, x);
        }

        sb.append(str.substring(x));

        return sb.toString();
    }

    /**
     * @param str String: 문자열
     * @return String 문자열이 null일 경우 "", 그외에는 문자열 그대로
     */
    public static String NVL(String str) {
        return NVL(str, "");
    }

    /**
     * @param str String: 문자열
     * @param rep String: 문자열이 null일 경우 대체 문자열
     * @return String 문자열이 null일 경우 대체 문자열, 그외에는 문자열 그대로
     */
    public static String NVL(String str, String rep) {
        return str == null ? rep : str;
    }

    /**
     * @param str Object
     * @param rep String: 문자열이 null일 경우 대체 문자열
     * @return String: 문자열이 null일 경우 대체 문자열, 그외에는 문자열 그대로
     */
    public static String NVL(Object str, String rep) {
        //return str == null ? rep : (String) str;
        return str == null ? rep : String.valueOf(str);
    }

    /**
     * @desc byte[]를 String으로 변환
     * @param data byte[]
     * @return String
     */
    public static String byteToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // buf.append(byteToHex(data[i]).toUpperCase());
            buf.append(byteToHex(data[i]));
        }
        return buf.toString();
    }

    /**
     * @desc byte를 String으로 변환
     * @param data byte
     * @return String
     */
    public static String byteToHex(byte data) {
        StringBuilder buf = new StringBuilder();
        buf.append(hexToChar((data >>> 4) & 0x0F));
        buf.append(hexToChar(data & 0x0F));
        return buf.toString();
    }

    /**
     * @desc 헥사값을 char로 변환
     * @param i int
     * @return char
     */
    public static char hexToChar(int i) {
        if ((i >= 0) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('a' + (i - 10));
        }
    }

    /**
     * @desc String을 int로 반환 기본값은 0
     * @param str String
     * @return int
     */
    public static int getInt(String str) {
        return getInt(str, 0);
    }

    /**
     * @desc String을 int로 반환 기본값은 0
     *
     * @param str Object
     * @return int
     */
    public static int getInt(Object str) {
        return getInt(getString(str), 0);
    }

    /**
     * @desc String을 int로 반환, 에러 발생시 두번째 값 반환
     * @param str String: value
     * @param default_int int: 기본값
     * @return int
     */
    public static int getInt(String str, int default_int) {
        try {
            return Integer.parseInt(str);
        }catch(NumberFormatException ne ) {
            return default_int;
        }
    }

    /**
     * @desc String을 boolean으로 반환
     * @param str String: value
     * @param defaultValue boolean: 기본값
     * @return boolean
     */
    public static boolean getBoolean(String str, boolean defaultValue) throws Exception {
        try {
            return (str == null || str.equals("")) ? defaultValue : (Boolean.valueOf(str)).booleanValue();
        }catch (NullPointerException npe) {
            throw npe;
        }catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @desc String을 boolean으로 반환
     * @param str value
     * @return boolean
     */
    public static boolean getBoolean(String str)throws Exception {
        boolean defaultValue = false;
        return getBoolean(str, defaultValue);
    }

    /**
     * @desc String을 long으로 반환
     * @param str String
     * @return long
     */
    public static long getLong(String str) {
        return getLong(str, 0l);
    }

    /**
     * @desc String을 long으로 반환
     * @param str String
     * @param default_long
     * @return long
     */
    public static long getLong(String str, long default_long) {
        try {
            return Long.parseLong(str);
        }catch(NumberFormatException ne) {
            return default_long;
        }catch (Exception e) {
            return default_long;
        }
    }

    /**
     * @desc String을 double로 반환
     * @param str String
     * @return double
     */
    public static double getDouble(String str) {
        return getDouble(str, 0d);
    }

    /**
     * @desc String을 double로 반환
     * @param str String
     * @param default_double double
     * @return double
     */
    public static double getDouble(String str, double default_double) {
        try {
            return Double.parseDouble(str);
        } catch(NumberFormatException ne) {
            return default_double;
        }catch (Exception e) {
            return default_double;
        }
    }


    /**
     * @desc Objet을 double로 반환
     * @param obj Object
     * @return double
     */
    public static double getDouble(Object obj) {
        return getDouble(obj, 0d);
    }
    /**
     * @desc Object을 double로 반환
     * @param obj Object
     * @param default_double double
     * @return double
     */
    public static double getDouble(Object obj, double default_double) {
        try {
            return Double.parseDouble(obj.toString());
        }catch(NumberFormatException ne) {
            return default_double;
        }catch (Exception e) {
            return default_double;
        }
    }

    /**
     * @desc 의미있는 String인지 여부 판단 null과 empty일때 false
     * @param s String
     * @return boolean
     */
    public static boolean isSemantic(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @desc 문자열 길이를 반환
     * @param s String : 문자열
     * @param charset String : 캐릭터셋
     * @return int
     * @throws java.io.UnsupportedEncodingException
     */
    public static int getStringLength(String s, String charset) throws UnsupportedEncodingException {
        if(s.isEmpty()) return 0;
        return s.getBytes(charset).length;
    }
    /**
     * @desc 문자열 길이를 반환 (default:utf-8)
     * @param s String
     * @return int
     * @throws java.io.UnsupportedEncodingException
     */
    public static int getStringLength(String s) throws UnsupportedEncodingException {
        if(s.isEmpty()) return 0;
        else return s.getBytes("utf-8").length;
    }

    /**
     * @desc null 또는 "" 일 경우 true
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return !isSemantic(str);
    }
    public static boolean isEmpty(Object obj) {
        return !isSemantic(getString(obj));
    }

    /**
     * @desc null 또는 "" 가 아닌경우 경우 true
     * @param value Object
     * @return boolean
     */
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    public static String encodeXSS(String value) {
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("\"", "&quot;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        //value = value.replaceAll("script", "");
        return value;
    }

    public static String decodeXSS(String value) {
        if(value == null || value.equals("")) return value;
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        value = value.replaceAll("&#40;", "\\(").replaceAll("&#41;", "\\)");
        value = value.replaceAll("&#39;", "'");
        value = value.replaceAll("&quot;", "\"");
        return value;
    }

    /**
     * @desc 문자열 길이만큼 반환
     * @param o Object
     * @param startIndex int
     * @return String
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getSubstring(Object o, int startIndex) throws UnsupportedEncodingException {
        return getString(o).substring(startIndex);
    }

    /**
     * @desc 문자열 길이만큼 반환
     * @param s String
     * @param startIndex int
     * @return String
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getSubstring(String s, int startIndex) throws UnsupportedEncodingException {
        return s.substring(startIndex);
    }

    /**
     * @desc 문자열 길이만큼 반환
     * @param o
     * @param startIndex
     * @param endIndex
     * @return String
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getSubstring(Object o, int startIndex, int endIndex) throws UnsupportedEncodingException {
        return getSubstring(getString(o), startIndex, endIndex);
    }

    /**
     * @desc 문자열 길이만큼 반환
     * @param s
     * @param startIndex
     * @param endIndex
     * @return String
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getSubstring(String s, int startIndex, int endIndex) throws UnsupportedEncodingException {
        if(s.isEmpty()) return "";
        else if(s.length() <= startIndex) {
            return "";
        }
        else if(s.length() > startIndex && s.length() >= endIndex) {
            return s.substring(startIndex, endIndex);
        }
        else if(s.length() > startIndex && s.length() < endIndex) {
            return s.substring(startIndex);
        }
        else {
            return "";
        }
    }

    /**
     * @desc 반각문자를 전각문자로 변환
     * @param src String
     * @return String
     */
    public static String toFullChar(String src) {
        if (src == null)
            return null;
        StringBuffer strBuf = new StringBuffer();
        char c = 0;
        for (int i = 0; i < src.length(); i++) {
            c = src.charAt(i);
            // 영문 알파벳 이거나 특수 문자
            if (c >= 0x21 && c <= 0x7e)
                c += 0xfee0;
                // 공백
            else if (c == 0x20)
                c = 0x3000;
            strBuf.append(c);
        }
        return strBuf.toString();
    }

    /**
     * @desc 반각문자를 전각문자로 변환 (영문 제외)
     * @param src String
     * @return String
     */
    public static String toFullCharExcludeEn(String src) {
        if (src == null)
            return null;
        StringBuffer strBuf = new StringBuffer();
        char c = 0;
        for (int i = 0; i < src.length(); i++) {
            c = src.charAt(i);
            // 영문 제외
            //if (c >= 0x21 && c <= 0x7e) {
            if ((c >= 0x21 && c <= 0x40) || (c >= 0x5B && c <= 0x60) || (c >= 0x7B && c <= 0x7E)) {
                c += 0xfee0;
            }
            strBuf.append(c);
        }
        return strBuf.toString();
    }

    /**
     * @desc 전각문자를 반각문자로 변환
     * @param src String
     * @return String
     */
    public static String toHalfChar(String src) {
        StringBuffer strBuf = new StringBuffer();
        char c = 0;
        for (int i = 0; i < src.length(); i++) {
            c = src.charAt(i);
            // 영문이거나 특수 문자 일경우.
            if (c >= '！' && c <= '～')
                c -= 0xfee0;
                // 공백
            else if (c == '　')
                c = 0x20;
            strBuf.append(c);
        }
        return strBuf.toString();
    }

    /**
     * @desc value 값을 target 과 비교하여 같을경우 true
     * @param value Object
     * @param target String
     * @return boolean
     */
    public static boolean equals(Object value, String target) {
        return getString(value, "").equals(target);
    }

    /**
     * @desc 중괄호 치환
     * @param o Object
     * @param arr_replace Object...
     * @return String
     */
    public static String replaceDoubleCurlyBrace(Object o, Object... arr_replace) {
        String str = getString(o);
        for(int i=0; i<arr_replace.length; i++) {
            str = str.replace("{{"+i+"}}",String.valueOf(arr_replace[i]));
        }
        return str;
    }
    /**
     * @desc 중괄호 치환
     * @param str String
     * @param arr_replace String...
     * @return String
     */
    public static String replaceDoubleCurlyBrace(String str, String... arr_replace) {
        for(int i=0; i<arr_replace.length; i++) {
            str = str.replace("{{"+i+"}}",arr_replace[i]);
        }
        return str;
    }

    //=============================================================

    /**
     * @desc 숫자 여부 판단 - 실수 가능
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str.replaceAll("\\,", ""));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * @desc 모든 태그 제거
     * @param content String
     * @return String
     */
    public static String removeTag(String content) {
        Pattern SCRIPTS = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        content = content.replaceAll("&lt;", "<");
        content = content.replaceAll("&gt;", ">");

        Pattern TAGS = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
        // Pattern nTAGS = Pattern.compile("<\\w+\\s+[^<]*\\s*>");
        Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");
        Pattern WHITESPACE = Pattern.compile("\\s\\s+");
        Pattern ETC = Pattern.compile("nbsp;");

        Matcher m;

        m = SCRIPTS.matcher(content);
        content = m.replaceAll("");

        m = STYLE.matcher(content);
        content = m.replaceAll("");

        m = TAGS.matcher(content);
        content = m.replaceAll("");
        m = ENTITY_REFS.matcher(content);
        content = m.replaceAll("");
        m = WHITESPACE.matcher(content);
        content = m.replaceAll(" ");

        m = ETC.matcher(content);
        content = m.replaceAll(" ");

        return content;
    }

    /**
     * @desc 랜덤 문자열 생성
     * @param type String: GenType 참조
     * @param length String: 길이
     * @return String
     */
    public static String getRandomValue(GenType type, int length) {

        StringBuffer strPwd = new StringBuffer();
        char str[] = new char[1];
        // 특수기호 포함
        if (type.equals(GenType.SpecialMarks)) {
            for (int i = 0; i < length; i++) {
                str[0] = (char) ((Math.random() * 94) + 33);
                strPwd.append(str);
            }
            // 대문자로만
        }
        else if (type.equals(GenType.UpperCase)) {
            for (int i = 0; i < length; i++) {
                str[0] = (char) ((Math.random() * 26) + 65);
                strPwd.append(str);
            }
            // 소문자로만
        }
        else if (type.equals(GenType.LowerCase)) {
            for (int i = 0; i < length; i++) {
                str[0] = (char) ((Math.random() * 26) + 97);
                strPwd.append(str);
            }
            // 숫자형으로
        }
        else if (type.equals(GenType.Number)) {
            int strs[] = new int[1];
            for (int i = 0; i < length; i++) {
                strs[0] = (int) (Math.random() * 9);
                strPwd.append(strs[0]);
            }
            // 소문자, 숫자형
        }
        else if (type.equals(GenType.LowerCaseAndNumber)) {
            Random rnd = new Random();
            for (int i = 0; i < length; i++) {
                if (rnd.nextBoolean()) {
                    strPwd.append((char) ((rnd.nextInt(26)) + 97));
                }
                else {
                    strPwd.append((rnd.nextInt(10)));
                }
            }
        }

        return strPwd.toString();
    }


    /**
     * @desc 소수 생성
     * @param length int
     * @return String
     */
    public static String getRandomPrimeNumber(int length){
        String sRtn = "";
        if (length == 4){
            //(오늘일자(dd) + 계좌마지막번호(농협은 모두 1)) % 10
            int addNum = (Integer.parseInt(new StringUtil().getDateToString("dd")) + 1) % 10;
            //랜덤숫자
            int i = Integer.parseInt(new StringUtil().getRandomValue(GenType.Number, 1));
            String[] tempKey = null;

            log.info("StringUtil.getRandomPrimeNumber - new StringUtil().getDateToString(dd) : {}, addNum : {}, i : {}", new StringUtil().getDateToString("dd"), addNum, i);

            //addNum의 마지막숫자에 따라 변경
            if (addNum == 1){
                tempKey = new String[] {"1009", "1013", "1019", "1033", "1039", "1049", "1063", "1069", "1087", "1093"};
            }else if (addNum == 3){
                tempKey = new String[] {"1009", "1019", "1021", "1031", "1039", "1049", "1051", "1061", "1069", "1081"};
            }else if (addNum == 7){
                tempKey = new String[] {"1009", "1013", "1019", "1021", "1031", "1123", "1033", "1039", "1049", "1051"};
            }else if (addNum == 9){
                tempKey = new String[] {"1151", "1021", "1031", "1021", "1051", "1061", "1087", "1091", "1097", "1117"};
            }else{
                tempKey = new String[] {"1009", "1013", "1019", "1021", "1087", "1123", "1129", "1171", "1193", "1217"};
            }

            sRtn = tempKey[i];
        }

        return sRtn;
    }

    /**
     * @desc 날짜 형식 문자열 비교
     * @param begin String
     * @param end String
     * @return boolean
     */
    public static boolean diffOfDate(String begin, String end) throws Exception {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date thisdate = formatter.parse(formatter.format(new Date()));

            Date beginDate = formatter.parse(begin);
            Date endDate = formatter.parse(end);

            if ((thisdate.equals(beginDate) || thisdate.after(beginDate)) && (thisdate.equals(endDate) || thisdate.before(endDate))) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @desc escaped html 태그 변경
     * @param content String
     * @return String
     */
    public static String replaceEditorTag(String content) {
        String str = content;
        if (str != null && !str.equals("")) {
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");

            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&apos;", "'");
        }
        else {
            str = "";
        }
        return str;
    }


    /**
     * @desc 오늘로부터 원하는 기간을 설정하여 Date로 반환
     * <br><i>문자열로 가져오고 싶은 경우 getDateToString(Date date, String format) 함수에 적용하여 사용하면 됨.</i>
     * @param year int
     * @param month int
     * @param day int
     * @return java.util.Date
     */
    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);

        return cal.getTime();
    }

    /**
     * @desc yyyy-MM-dd 형식의 문자열을 Date 객체로 가져옵니다.
     * @param input String
     * @return java.util.Date
     * @throws Exception
     */
    public static Date getDate(String input) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(input);
    }

    /**
     * @desc 특정 날짜 기준에서 특정 값을 더하거나 뺀 java.util.Date 형식의 날짜를 가져옵니다.
     * @param date Date 특정 날짜입니다. null일 경우 오늘입니다.
     * @param calendarField int Calendar 상수값
     * @param amount int 더하거나 뺄 값
     * @return java.util.Date
     */
    public static Date getDate(Date date, int calendarField, int amount) {
        Calendar cal = Calendar.getInstance();
        if (date == null)
            date = new Date();

        cal.setTime(date);
        cal.add(calendarField, amount);

        return cal.getTime();
    }

    /**
     * @desc 오늘 날짜를 형식에 맞는 문자열로 가져옵니다.
     * @param format String
     * @return String
     */
    public static String getDateToString(String format) {
        Date date = new Date();
        return getDateToString(date, format);
    }

    /**
     * @desc 날짜 객체를 형식에 맞는 문자열로 가져옵니다.
     * @param date Date
     * @param format String
     * @return String
     */
    public static String getDateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * @desc 특정 날짜 기준에서 그 주의 해당하는 요일의 Date를 yyyy-mm-dd 의 형식으로 가져옵니다.<br>
     * 일요일 : 1, 월요일 : 2, 화요일 : 3, 수요일 : 4, 목요일 : 5, 금요일 : 6, 토요일 : 7
     * @param date Date 특정 날짜 null일 경우 당일
     * @param iWeek int 요일의 해당하는 정수
     * @return String
     */
    public static String getStringDateToWeekDate (Date date, int iWeek) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        if (date == null)
            date = new Date();

        cal.setTime(date);

        cal.set(Calendar.DAY_OF_WEEK, iWeek);

        return formatter.format(cal.getTime());
    }

    /**
     * @desc date1에 대한 date2의 일수 차이를 가져옵니다.<br> - date2가 date1보다 미래일 경우 음수가 나옵니다.
     * @param date1 Date
     * @param date2 Date
     * @return long
     * @throws Exception
     */
    public static long compareTwoDate(Date date1, Date date2) throws Exception {
        // logger.debug(">>>>> date1 : {}, date2 : {}", date1, date2);
        long interval = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        return interval;
    }

    /**
     * @desc 타임스탬프 형식 문자열
     * @return String
     */
    public static String getTimestamp() {
        String rtnStr = null;

        // 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
        String pattern = "yyyyMMddhhmmssSSS";
        SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        rtnStr = sdfCurrent.format(ts.getTime());
        return rtnStr;
    }

    /**
     * @desc 시간 값을 이용하여 20자리 고유 문자열 반환
     * @return String 숫자형태의 문자
     */
    public static synchronized String getUniqueNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 17 characters
        String sReturn = sdf.format(Calendar.getInstance().getTime());
        try {
            ++dummy;
        }
        catch (Exception e) {
            dummy = 0;
        }
        sReturn += padLeft(String.valueOf(dummy % 1000), 3, '0');

        return sReturn;
    }

    /**
     * @desc java.util.UUID를 이용한 32자리 랜덤 문자열 : 하이픈 제거
     * @return String
     */
    public static String getRandomStringByUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @desc 16진 문자열 -&gt; byte 배열 변환 <br> - for RSA encrypt/decrypt <br>
     * @return byte[]
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[] {};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    /**
     * @desc 휴대폰 국번 리스트
     * @return List<String>
     * @throws Exception
     */
    public static List<String> getMobileList() throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        list.add("010");
        list.add("011");
        list.add("016");
        list.add("017");
        list.add("018");
        list.add("019");

        return list;
    }

    /**
     * @desc 전화번호 지역번호 기타 국번 리스트
     * @return List<String>
     * @throws Exception
     */
    public static List<String> getPhoneList() throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        // list.add("010");
        // list.add("011");
        // list.add("016");
        // list.add("017");
        // list.add("018");
        // list.add("019");
        list.add("02");
        list.add("031");
        list.add("032");
        list.add("033");
        list.add("041");
        list.add("042");
        list.add("043");
        list.add("051");
        list.add("052");
        list.add("053");
        list.add("054");
        list.add("055");
        list.add("061");
        list.add("062");
        list.add("063");
        list.add("064");
        list.add("050");
        list.add("0502");
        list.add("0505");
        list.add("070");

        return list;
    }

    /**
     * @desc 핸드폰 국번/일반 지역번호, 기타 국번 가져오기
     * @return List<String>
     * @throws Exception
     */
    public static List<String> getMobilePhoneList() throws Exception {
        List<String> list = getMobileList();
        List<String> list2 = getPhoneList();
        list.addAll(list2);
        return list;
    }

    /**
     * @desc IPIN용 특수 문자 변환
     * @param paramValue String
     * @param gubun String
     * @return String
     */
    public static String requestReplace(String paramValue, String gubun) {
        String result = "";

        if (paramValue != null) {

            paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            paramValue = paramValue.replaceAll("\\*", "");
            paramValue = paramValue.replaceAll("\\?", "");
            paramValue = paramValue.replaceAll("\\[", "");
            paramValue = paramValue.replaceAll("\\{", "");
            paramValue = paramValue.replaceAll("\\(", "");
            paramValue = paramValue.replaceAll("\\)", "");
            paramValue = paramValue.replaceAll("\\^", "");
            paramValue = paramValue.replaceAll("\\$", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("@", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll(";", "");
            paramValue = paramValue.replaceAll(":", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("--", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll(",", "");

            if (gubun != "encodeData") {
                paramValue = paramValue.replaceAll("\\+", "");
                paramValue = paramValue.replaceAll("/", "");
                paramValue = paramValue.replaceAll("=", "");
            }

            result = paramValue;

        }
        return result;
    }

    /**
     * @desc 문자수 단위로 문자열을 자른다.<br>
     * @param s String: 자를 문자열
     * @param i int: 자를 수
     * @param plusStr String: 플러스될 문자열
     * @return String
     */
    public static String cutStringPlus(String s, int i, String plusStr) {
        String str = "";
        if (s.length() <= i)
            return s;
        str = s.substring(0, i);
        return str + plusStr;
    }

    /**
     * @desc 바이트 단위로 문자열을 자른다.
     * @param s String: 자를 문자열
     * @param i int: 자를 수
     * @param plusStr String: 플러스될 문자열
     * @return String
     */
    public static String cutStringBytesPlus(String s, int i, String plusStr) {
        if (getString(s).equals(""))
            return "";

        byte abyte0[] = s.getBytes();
        int j = abyte0.length;
        int k = 0;
        if (i >= j)
            return s;
        for (int l = i - 1; l >= 0; l--)
            if ((abyte0[l] & 0x80) != 0)
                k++;
        String str = new String(abyte0, 0, i - k % 3);
        return str + plusStr;
    }

    /**
     * @desc 바이트 기준 문자열 길이 가져오기
     * @param value String
     * @return int
     */
    public static int getByteLength(String value) {
        if (isEmpty(value))
            return 0;

        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] txtChar = value.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            }
            else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            }
            else {
                etc++;
            }
        }
        return (en + ko + etc);

    }





    /**
     * 문자열 자르기 - 한글 깨지지 않도록
     * @param value String: 대상 문자열
     * @param startStr String: 기준 인덱스 정의할 문자열 (null)
     * @param length int: 자를 길이
     * @param nPrev int: (0)
     * @param isNotag boolean: 태그 불가 : true
     * @param isAddDot boolean : 문자열 끝 ... 추가 : false
     * @return String
     */
    public static String cutString(String value, String startStr, int length, int nPrev, boolean isNotag, boolean isAddDot) {

        String r_val = value;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE); // 태그제거
        // 패턴

        if (isNotag) {
            r_val = p.matcher(r_val).replaceAll("");
        } // 태그 제거
        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", ""); // 공백제거

        try {
            byte[] bytes = r_val.getBytes("UTF-8"); // 바이트로 보관
            if (startStr != null && !startStr.equals("")) {
                nLengthPrev = (r_val.indexOf(startStr) == -1) ? 0 : r_val.indexOf(startStr); // 일단 위치찾고
                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length; // 위치까지길이를 byte로 다시 구한다
                nLengthPrev = (nLengthPrev - nPrev >= 0) ? nLengthPrev - nPrev : 0; // 좀 앞부분부터 가져오도록한다.
            }

            // x부터 y길이만큼 잘라낸다. 한글안깨지게.
            int j = 0;

            if (nLengthPrev > 0)
                while (j < bytes.length) {
                    if ((bytes[j] & 0x80) != 0) {
                        oF += 2;
                        rF += 3;
                        if (oF + 2 > nLengthPrev) {
                            break;
                        }
                        j += 3;
                    }
                    else {
                        if (oF + 1 > nLengthPrev) {
                            break;
                        }
                        ++oF;
                        ++rF;
                        ++j;
                    }
                }

            j = rF;

            while (j < bytes.length) {
                if ((bytes[j] & 0x80) != 0) {
                    if (oL + 2 > length) {
                        break;
                    }
                    oL += 2;
                    rL += 3;
                    j += 3;
                }
                else {
                    if (oL + 1 > length) {
                        break;
                    }
                    ++oL;
                    ++rL;
                    ++j;
                }
            }

            r_val = new String(bytes, rF, rL, "UTF-8"); // charset 옵션

            if (isAddDot && rF + rL + 3 <= bytes.length) {
                r_val += "...";
            } // ...을 붙일지말지 옵션
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return r_val;
    }

    /**
     * @desc 지정한 문자열 반복 결합 후 리턴
     * @param character char: 반복할 문자열
     * @param repeatCount int: 반복 수
     * @return String
     */
    public static String repeatChar(char character, int repeatCount) {
        StringBuffer sb = new StringBuffer(repeatCount);

        while (sb.length() < repeatCount)
            sb.append(character);

        return sb.toString();
    }

    /**
     * @desc 문자열 오른쪽 패딩
     * @param value String: 적용시킬 문자열
     * @param width int: 패딩 길이
     * @param paddingChar char: 채울 문자
     * @return String
     */
    public static String padRight(String value, int width, char paddingChar) {
        try {
            if (value.length() >= width)
                return value;

            String sReturn = value;
            while (sReturn.length() < width)
                sReturn = sReturn + String.valueOf(paddingChar);

            return sReturn;
        }
        catch (Exception e) {
            return "";
        }
    }

    /**
     * @desc 문자열 왼쪽 패딩
     * @param value String: 적용시킬 문자열
     * @param width int: 패딩 길이
     * @param paddingChar char: 채울 문자
     * @return String
     */
    public static String padLeft(String value, int width, char paddingChar) {
        try {
            if (value.length() >= width)
                return value;

            String sReturn = value;
            while (sReturn.length() < width)
                sReturn = String.valueOf(paddingChar) + sReturn;

            return sReturn;
        }
        catch (Exception e) {
            return "";
        }
    }

    /**
     * @desc 마스킹 : 지정 문자열 별표 처리
     * @param value String: 별표 처리할 문자열
     * @param startIndex int: 별표 처리 시작할 문자열 인덱스
     * @param length int: 별표 길이
     * @return String
     */
    public static String maskingChar(String value, int startIndex, int length) {
        try {
            if (value.length() < (startIndex + 1))
                return value;

            char[] chars = value.toCharArray();
            for (int i = 0; i < chars.length ; i++) {
                if (i >= startIndex && i <= startIndex + length - 1)
                    chars[i] = '*';
            }

            return new StringBuffer().append(chars).toString();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return "null";
        }
    }

    /**
     * @desc 마스킹 : 지정문자열 별표 처리 - 인덱스 이상 문자열 모두 별표 처리
     * @param value String: 별표 처리할 문자열
     * @param startIndex int: 별표 처리 시작할 문자열 인덱스
     * @return String
     */
    public static String maskingChar(String value, int startIndex) {
        return maskingChar(value, startIndex, value.length() - startIndex);
    }

    /**
     * @desc 파일 용량 읽기 쉬운 형태로 가져오기 - KB, MB, GB 단위로 가져옴
     * @param fileSize long
     * @return String
     */
    public static String getFileSize(long fileSize) {
        try {
            String sRtn = "0KB";

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            // GB
            if (fileSize >= Math.pow(2, 30))
                sRtn = nf.format((double) (fileSize / Math.pow(2, 30))) + "GB";
            else if (fileSize >= Math.pow(2, 20))
                sRtn = nf.format((double) (fileSize / Math.pow(2, 20))) + "MB";
            else if (fileSize > 0)
                sRtn = nf.format((double) (fileSize / Math.pow(2, 10))) + "KB";

            return sRtn;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return "0KB";
        }
    }

    /**
     * @desc 서버 OS 반환
     * <ul>
     * 	<li>windows</li>
     * 	<li>linux</li>
     * 	<li>darwin(mac)</li>
     * 	<li>solaris</li>
     * </ul>
     * @return os.name String
     */
    public static String getOperatingSystemName() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if(osName.startsWith("windows"))
            return "windows";
        else if(osName.startsWith("linux"))
            return "linux";
        else if(osName.startsWith("mac os"))
            return "darwin";
        else if(osName.startsWith("sunos")||osName.startsWith("solaris"))
            return "solaris";
        else return osName;
    }


    /**
     * @desc 모바일접속 여부 확인
     * @param request HttpServletRequest
     * @return true(모바일), false(그 외)
     */
    public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        boolean mobile1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
        boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
        if(mobile1 || mobile2) {
            return true;
        }
        return false;
    }


    /**
     * @desc 서버의 고정IP 가져오기
     * @return String
     */
    public static String getServerIP(){

        String hostAddr = "";

        try {

            Enumeration<NetworkInterface> nienum = NetworkInterface.getNetworkInterfaces();

            while (nienum.hasMoreElements()) {

                NetworkInterface ni = nienum.nextElement();
                Enumeration<InetAddress> kk= ni.getInetAddresses();

                while (kk.hasMoreElements()) {

                    InetAddress inetAddress = kk.nextElement();

                    if (
                            !inetAddress.isLoopbackAddress()
                                    && !inetAddress.isLinkLocalAddress()
                                    && inetAddress.isSiteLocalAddress())
                    {
                        hostAddr = inetAddress.getHostAddress().toString();
                    }
                }

            }

        } catch (SocketException e) {
            log.error("getServerIP : {}", e.getMessage());
        }

        return hostAddr;
    }



    /**
     * @desc 정규식 유형 체크
     * @param type String
     * <ul>
     * <li>MOBILE</li>
     * </ul>
     * @param value String
     * @return boolean
     */
    public static boolean isCheckPattern(String type, String value) {
        boolean isRtn = false;
        String sPattern = null;

        String sType = type.trim().toUpperCase();

        if (sType.equals("MOBILE")) {
            //잘못된 데이터는 빈값으로 처리 (return false 처리)
            if (value.equals("010-0000-0000") || value.equals("010-000-0000"))
                value = "";

            sPattern = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        }

        isRtn = Pattern.matches(sPattern, value);
        return isRtn;
    }

    /**
     * @desc day 일(1) ~ 토(7)
     * @param day String
     * @return String
     */
    public static String getDayName(String day) {
        return getDayName(day, "S");
    }

    /**
     * @desc 요일명 조회
     * @param day String: 일(1) ~ 토(7)
     * @param returnType String: ("S" : 일, "L" : 일요일)
     * @return String
     */
    public static String getDayName(String day, String returnType) {
        String sRtn = "";

        if (isEmpty(day)) return sRtn;

        switch (day) {
            case "1": sRtn = "일"; break;
            case "2": sRtn = "월"; break;
            case "3": sRtn = "화"; break;
            case "4": sRtn = "수"; break;
            case "5": sRtn = "목"; break;
            case "6": sRtn = "금"; break;
            case "7": sRtn = "토"; break;
            default: break;
        }

        return (returnType.equals("S") ? sRtn : sRtn + "요일");
    }

}
