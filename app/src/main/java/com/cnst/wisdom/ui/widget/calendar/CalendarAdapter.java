package com.cnst.wisdom.ui.widget.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnst.wisdom.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日历gridview中的每一个item显示的textview
 *
 * @author taoyuan
 */
public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Calendar> selCalendars = new ArrayList<>();//被选中的日期
    private ArrayList<Calendar> calendars = new ArrayList();//本月所有日期
    private LunarCalendar lunarUtils;//通过传入公立获取农历
    private int maxSel = 1;//设置最多可选中日历数
    private Calendar today = Calendar.getInstance();

    public CalendarAdapter(Context context, Calendar calendar) {
        this.context = context;
        calendars.addAll(CalendarUtils.getCalendar(calendar));//获取calendar所在月份的所有日期
        this.lunarUtils = new LunarCalendar();
    }

    public void refreshDate(Calendar calendar) {
        calendars.clear();//刷新日期
        calendars.addAll(CalendarUtils.getCalendar(calendar));
    }

    public void refreshSelDate(Calendar calendar) {
        selCalendars.clear();
        //需要选两个日期时
        if (maxSel > 1) {
            //当选择日期是本周时
//            if(calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH)&&
//                    calendar.get(Calendar.WEEK_OF_MONTH)==today.get(Calendar.WEEK_OF_MONTH)){
//                selCalendars.add(today);
//            }else {
//                //如果选择了与当前日期不同周时
//
//            }
            Calendar firstDate = Calendar.getInstance();
            firstDate.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            int i =-calendar.get(Calendar.DAY_OF_WEEK)+2;
            firstDate.add(Calendar.DAY_OF_MONTH, i);
            selCalendars.add(firstDate);

            Calendar secondDate = Calendar.getInstance();
            secondDate.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            int j =6-calendar.get(Calendar.DAY_OF_WEEK);
            secondDate.add(Calendar.DAY_OF_MONTH, j);
            selCalendars.add(secondDate);
        }else {
            //只需选一个日期时
            selCalendars.add(calendar);
        }
        notifyDataSetChanged();
    }

    public void setMaxSel(int maxSel) {
        this.maxSel = maxSel;
    }

    public int getMaxSel() {
        return maxSel;
    }

    public ArrayList<Calendar> getSelCalendars() {
        return selCalendars;
    }

    @Override
    public int getCount() {
        return calendars.size();
    }

    @Override
    public Object getItem(int position) {
        return calendars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
        }
        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        TextView monthDayView = (TextView) convertView.findViewById(R.id.month_day);
        Calendar calendar = calendars.get(position);
        if (calendar != null) {
            int d = calendar.get(Calendar.DAY_OF_MONTH);//阳历阿拉伯数字
            String dv = lunarUtils.getLunarDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, d, false);//阴历
            dateView.setText(d + "");
            monthDayView.setText(dv);
            // 当前月信息显示
            if (position % 7 == 0 || position % 7 == 6) {
                // 当前月信息显示
                dateView.setTextColor(context.getResources().getColor(R.color.tip_text));
                monthDayView.setTextColor(context.getResources().getColor(R.color.tip_text));
            }
            dateView.setBackgroundResource(R.color.white);
            //设置选中的颜色
            if (selCalendars.size() > 0) {
                for (int i = 0; i < selCalendars.size(); i++) {
                    if (selCalendars.get(i) != null && CalendarUtils.compareTo(calendar, selCalendars.get(i)))//比较两个日期是不是同一天，需要比较年月日，
                    // 而不能使用List.contains或Object.equals
                    {
                        dateView.setBackgroundResource(R.drawable.green_point);
                    }
                }
            }
        }
        if (position > 7 * (calendars.size() % 7 == 0 ? calendars.size() / 7 - 1 : calendars.size() / 7) - 1) {
            View line = convertView.findViewById(R.id.date_line);
            line.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
}
