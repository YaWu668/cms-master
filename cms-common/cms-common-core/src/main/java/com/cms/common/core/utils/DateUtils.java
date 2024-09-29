package com.cms.common.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 邓志军
 * @date 2024-05-28
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    };

    /**
     * 表示一天中的最小时间（00:00:00）
     */
    public final static String MIN_TIME = "00:00:00";

    /**
     * 表示一天中的最大时间（23:59:59）
     */
    public final static String MAX_TIME = "23:59:59";

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前时间，并按指定格式进行格式化
     *
     * @return 格式化后的当前时间字符串
     */
    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前时间，并按默认格式（yyyyMMddHHmmss）进行格式化
     *
     * @return 格式化后的当前时间字符串
     */
    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时间，并按指定格式进行格式化
     *
     * @param format 时间格式
     * @return 格式化后的当前时间字符串
     */
    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    /**
     * 将日期对象按默认格式（yyyy-MM-dd）格式化为字符串
     *
     * @param date 日期对象
     * @return 格式化后的日期字符串
     */
    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    /**
     * 将日期对象按指定格式进行格式化为字符串
     *
     * @param format 时间格式
     * @param date   日期对象
     * @return 格式化后的日期字符串
     */
    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将字符串按指定格式解析为日期对象
     *
     * @param format 时间格式
     * @param ts     日期字符串
     * @return 解析后的日期对象
     */
    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 将 LocalDateTime 类型转换为 Date 类型。
     * <p>
     * 根据传入的 LocalDateTime 对象，使用系统默认时区将其转换为 ZonedDateTime 对象。
     * 然后，将 ZonedDateTime 对象转换为 Instant 对象，并使用 Date.from() 方法将其转换为 Date 对象。
     *
     * @param temporalAccessor LocalDateTime 对象
     * @return Date 对象
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 将 LocalDate 类型转换为 Date 类型。
     * <p>
     * 根据传入的 LocalDate 对象，使用 LocalTime.of(0, 0, 0) 方法构造 LocalDateTime 对象。
     * 然后，使用系统默认时区将 LocalDateTime 对象转换为 ZonedDateTime 对象。
     * 接着，将 ZonedDateTime 对象转换为 Instant 对象，并使用 Date.from() 方法将其转换为 Date 对象。
     *
     * @param temporalAccessor LocalDate 对象
     * @return Date 对象
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 在指定日期上增加指定分钟数
     *
     * @param date    要增加的日期
     * @param minutes 要增加的分钟数
     * @return 增加分钟后的日期
     */
    public static Date addMinutesToDate(Date date, int minutes) {
        // 创建一个 Calendar 对象，并将其时间设置为传入的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 在 Calendar 对象中增加指定的分钟数
        calendar.add(Calendar.MINUTE, minutes);

        // 返回更新后的 Date 对象
        return calendar.getTime();
    }

    /**
     * 在指定日期上增加指定天数
     *
     * @param date 要增加的日期
     * @param days 要增加的天数
     * @return 增加天数后的日期
     */
    public static Date addDaysToDate(Date date, int days) {
        // 创建一个 Calendar 对象，并将其时间设置为传入的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 在 Calendar 对象中增加指定的天数
        calendar.add(Calendar.DAY_OF_YEAR, days);

        // 返回更新后的 Date 对象
        return calendar.getTime();
    }

    /**
     * 在指定日期上增加指定毫秒数
     *
     * @param date         要增加的日期
     * @param milliseconds 要增加的毫秒数
     * @return 增加毫秒数后的日期
     */
    public static Date addMillisecondsToDate(Date date, long milliseconds) {
        // 创建一个 Calendar 对象，并将其时间设置为传入的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 在 Calendar 对象中增加指定的毫秒数
        calendar.add(Calendar.MILLISECOND, (int) milliseconds);

        // 返回更新后的 Date 对象
        return calendar.getTime();
    }

    /**
     * 将传递时间对象格式化成当天最小时刻
     *
     * @param date 日期对象
     * @return 传递日期对象的最小时刻
     */
    public static Date formatIntradayMinDateTime(Date date) {
        String dateStr = parseDateToStr(YYYY_MM_DD, date) + " " + MIN_TIME;
        return dateTime(YYYY_MM_DD_HH_MM_SS, dateStr);
    }

    /**
     * 将传递时间对象格式化成当天最大时刻
     *
     * @param date 日期对象
     * @return 传递日期对象的最大时刻
     */
    public static Date formatIntradayMaxDateTime(Date date) {
        String dateStr = parseDateToStr(YYYY_MM_DD, date) + " " + MAX_TIME;
        return dateTime(YYYY_MM_DD_HH_MM_SS, dateStr);
    }
}
