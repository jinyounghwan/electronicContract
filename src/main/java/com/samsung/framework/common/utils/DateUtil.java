package com.samsung.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 날짜 유틸
 */
@Slf4j
@Component
public class DateUtil {

    public static String DATETIME_YMDHM_PATTERN = "yyyy-MM-dd HH:mm";
    public static String DATETIME_YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * @desc epoch -> utc date convert
     * @param exp String
     * @return Date
     */
    public static Date getExpired(String exp) throws Exception {
        // parameter string type expired convert to date
        return new Date(Long.parseLong(exp) * 1000);
    }

    /**
     * @desc 현재시간 utc date convert
     * @return Date
     */
    public static Date getUtcNowDate() throws Exception {
        // utc 현재시간
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info(":::::::::::::::::::::::now:::::::::::::::" + now);

        Instant instant = now.toInstant();
        return Date.from(instant); // utc 현재시간 datetype convert

    }

    /**
     * @desc 현재시간 utc date format
     * @return String
     */
    public static String getUtcNowDateFormat() throws Exception {
        // utc 현재시간
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info(":::::::::::::::::::::::now:::::::::::::::" + now);
        Instant instant = now.toInstant();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = sf.format(Date.from(instant));
        return format1;
    }

    /**
     * @param pattern String
     * @return String
     */
    public static String getUtcNowDateFormat(String pattern) throws Exception {
        // utc 현재시간
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info(":::::::::::::::::::::::now:::::::::::::::" + now);
        Instant instant = now.toInstant();
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String format1 = sf.format(Date.from(instant));
        return format1;
    }

    /**
     * @desc 현재시간 + days utc date convert
     * @param days int
     * @return Date
     */
    public static Date getUtcAddDate(int days) throws Exception {
        // utc 현재시간
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info(":::::::::::::::::::::::now:::::::::::::::" + now);

        Instant instant = now.plusDays(days).toInstant();
        return Date.from(instant); // utc datetype convert

    }

    /**
     * @desc
     * @param day String
     * @return Date
     */
    public static Date getDate(String day) throws Exception {
        return getDate(day, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     *
     * @param day String
     * @param pattern String
     * @return Date
     */
    public static Date getDate(String day, String pattern) throws Exception {
        log.info("pars day:" + day);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date date = sf.parse(day);
        return date;
    }

    /**
     * @desc yyyy-MM-dd HH:mm:ss
     * @param utcTime String: 2022-05-12T03:41:59Z
     * @param country String: Locale.KOREA
     * @param pattern String: yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String utcToConvertPattern(String utcTime, Locale country, String pattern) {
        Date date = null;
        String dateTime = "";

        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", country);
            SimpleDateFormat psf = new SimpleDateFormat(pattern);
            TimeZone utcZone = TimeZone.getTimeZone("UTC");

            sf.setTimeZone(utcZone);
            String strutcTime = utcTime.substring(0, 19);
            date = sf.parse(strutcTime+'Z');
            dateTime = psf.format(date);
        } catch (ParseException e) {
            return "잘못된 형식입니다.";
        }

        return dateTime;
    }

    /**
     * @desc 만 나이 가져오기
     * @param birthDay String
     * @return int
     */
    public static int getAge(String birthDay) throws Exception {
        int age = 0;
        try {

            log.info(" :::: birthYear ::::" + birthDay);
            String birthStr = birthDay.replaceAll("-", "");
            int birthYear = StringUtil.getInt(birthStr.substring(0, 4));
            int birthMonth = StringUtil.getInt(birthStr.substring(4, 6));
            int birthDate = StringUtil.getInt(birthStr.substring(6, 8));

            log.info(" :::: birthYear ::::" + birthYear);
            log.info(" :::: birthMonth ::::" + birthMonth);
            log.info(" :::: birthDate ::::" + birthDate);

            Calendar current = Calendar.getInstance();
            int currentYear = current.get(Calendar.YEAR); // 현재 날짜
            int currentMonth = current.get(Calendar.MONTH) + 1; // 현재 달
            int currentDay = current.get(Calendar.DAY_OF_MONTH); // 현재 일
            // 현재 날짜 - 태어난 날 = 나이
            age = currentYear - birthYear;
            // 만약 생일이 지나지 않았으면 -1
            if (birthMonth * 100 + birthDate > currentMonth * 100 + currentDay) {
                age--;
            }
            log.info("age : " + age);
        } catch (Exception e) {
            log.info("dateUtil -  : getAge {} ::" + e.getMessage());
            throw e;
        }
        return age;
    }

    /**
     * @desc 윤년 판별 메소드 (1 윤년, 0 평년)
     * @param year int
     * @return int
     */
    public static int isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? 1 : 0;
    }

    /**
     *
     * @param buyDate String
     * @param deliveryCycle int
     * @return String
     */
    public static String addDate(String buyDate, int deliveryCycle) throws ParseException {
        String date = "";
        try {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 포멧
            Date time = simpleDate.parse(buyDate);
            Calendar cal = Calendar.getInstance(); // 날짜 계산을 위해 Calendar 추상클래스 선언 getInstance()메소드 사용
            cal.setTime(time);
            cal.add(Calendar.WEEK_OF_YEAR, deliveryCycle);
            date = simpleDate.format(cal.getTime());
        } catch (ParseException e) {
            throw e;
        }
        return date;

    }

    /**
     *
     * @param date Date
     * @return String
     */
    public static String getFormatDateTime(Date date) {
        return getFormatDateTime(date, "yyMMddHHmmssSSS");
    }

    /**
     *
     * @param date Date
     * @param pattern String
     * @return String
     */
    public static String getFormatDateTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     *
     * @param endDate String
     * @return boolean
     */
    public static boolean nowCompare(String endDate) throws Exception {
        boolean v = false;
        try {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 포멧
            Date e = getDate(endDate); // 마지막 날짜
            log.info("e : " + e);
            String d = simpleDate.format(getUtcNowDate());
            log.info("d : " + d);
            Date n = simpleDate.parse(d);
            log.info("n : " + n);
            if (e.before(n)) {
                log.info("> after");
                v = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return v;
    }

    /**
     *
     * @param startDate String
     * @param endDate String
     * @return boolean
     */
    public static boolean nowCompare(String startDate, String endDate) throws Exception {
        boolean v = false;
        try {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 포멧
            Date e = getDate(endDate); // 마지막 날짜
            Date s = getDate(startDate);//
            log.info("e : " + e);
            String d = simpleDate.format(getUtcNowDate());
            log.info("d : " + d);
            Date n = simpleDate.parse(d);
            log.info("n : " + n);
            if (n.after(s) && n.before(e)) {
                v = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return v;
    }

    /**
     * @desc UTC Date to String
     * @param utcDate Date
     * @return String
     */
    public static String convertToUtcTime(Date utcDate) {
        OffsetDateTime offsetDateTime = utcDate.toInstant().atOffset(ZoneOffset.UTC);
        return offsetDateTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss:SSS"));
    }

    /**
     *
     * @param createDate String
     * @param date String
     * @return String
     */
    public static String dateSubtract(String createDate, String date) throws Exception {
        String r = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // String now = getUtcNowDateFormat();
            Date nowDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nowDate);

            // int comparison = StringUtil.getInt(createDate);
            int comparison = Integer.parseInt(createDate);
            cal.add(Calendar.DATE, (comparison)); // 날짜 더하기

            r = dateFormat.format(cal.getTime());
            log.info(" >>>>>>>>>>>>>>" + r);

        } catch (Exception e) {
            throw e;
        }
        return r;
    }

    /**
     * 날짜 문자열 포맷
     * @param tgt {@link LocalDateTime}
     * @param pattern {@link String}
     * @return
     */
    public static String convertLocalDateTimeToString(LocalDateTime tgt, String pattern) {
        if(tgt == null) {
            return "";
        }
        return tgt.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 문자열 날짜 변경 포맷
     * @param str {@link String}
     * @param pattern {@link String}
     * @return LocalDateTime {@link LocalDateTime}
     */
    public static LocalDateTime convertStringToLocalDateTime(String str, String pattern){
        if(str==null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }
    /**
     * 초 => 분초 포맷으로 변경
     * @param target
     * @return String
     */
    public static String secondToMinuteFormatSetting(String target){
        String time = target;
        String minTmFmt = String.valueOf(Integer.parseInt(time) / (60 * 1000));
        String min = minTmFmt.length() < 2 ? "0" + minTmFmt : minTmFmt;
        time = String.valueOf(Integer.parseInt(time) - Integer.parseInt(min) * (60*1000));
        String secTmFmt = String.valueOf(Integer.parseInt(time) / 1000);
        String sec = secTmFmt.length() < 2? "0"+ secTmFmt : secTmFmt;


        return min+sec;
    }

    /**
     * 두 날짜의 초 차이를 구한다.
     * @param targetData1
     * @param targetData2
     * @return
     * @throws ParseException
     */
    public static long secDiffDate(String targetData1, String targetData2) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date1 = format.parse(targetData1);
        Date date2 = format.parse(targetData2);
        long sec = (date1.getTime() - date2.getTime());
        log.info("second Time"+sec);
        return sec;
    }

    /**
     * yyyy-mm-dd 형식 날짜 String -> En 날짜로 변경
     * @param date {@link String}
     * @return String
     */
    public static String getStrContractDateEn(String date){
        StringTokenizer st =new StringTokenizer(date, "-");
        String year = st.nextToken();
        String month = st.nextToken();
        String day = st.nextToken();
        return month+'/'+day+'/'+year;
    }
}
