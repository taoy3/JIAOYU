package com.taoy3.freight.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taoy2 on 15-10-15.
 */
public class DateUtils {
    public static String changeDateFormat(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        return sim.format(date);
    }

    public static String changeDateFormat(long time) {
        Date date = new Date(time);
        if (date == null) {
            return "";
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        return sim.format(date);
    }

    public static String changeDateFormat(String oldStr) {
        if (oldStr == null) {
            return "";
        }
        String newStr = "";
        String strs[] = oldStr.split("-");
        if (strs.length > 2) {
            if (strs[1].startsWith("0")) {
                strs[1] = strs[1].substring(1);
            }
            if (strs[2].startsWith("0")) {
                strs[2] = strs[2].substring(1);
            }
            newStr = strs[1] + "/" + strs[2];
        }
        return newStr;
    }

    public static String getMD(long time, int tt) {
        Calendar etaTime = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        //如果是后退几天，就写 -天数 例如：
        etaTime.setTimeInMillis(time);
        etaTime.add(Calendar.DAY_OF_MONTH, tt);
        //进行时间转换
        String date = sim.format(etaTime.getTime());
        return changeDateFormat(date);
    }

    public static long after(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTimeInMillis();
    }
}
