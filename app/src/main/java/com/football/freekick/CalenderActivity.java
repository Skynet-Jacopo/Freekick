package com.football.freekick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beiing.monthcalendar.MonthCalendar;
import com.beiing.monthcalendar.bean.Day;
import com.beiing.monthcalendar.listener.GetViewHelper;
import com.beiing.monthcalendar.listener.OnDateSelectListener;
import com.beiing.monthcalendar.listener.OnMonthChangeListener;
import com.beiing.monthcalendar.utils.CalendarUtil;
import com.football.freekick.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLayoutActivity;

import org.joda.time.DateTime;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日曆選擇器
 */
public class CalenderActivity extends AutoLayoutActivity {

    @Bind(R.id.tv_left)
    TextView mTvLeft;
    @Bind(R.id.tv_month)
    TextView mTvMonth;
    @Bind(R.id.tv_year)
    TextView mTvYear;
    @Bind(R.id.tv_right)
    TextView mTvRight;
    @Bind(R.id.month_calendar)
    MonthCalendar monthCalendar;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    private DateTime mSelectDate;
    private boolean isClicked = false;
    private DateTime now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);
        mTvLeft.setTypeface(App.mTypeface);
        mTvRight.setTypeface(App.mTypeface);
        mSelectDate = (DateTime) getIntent().getSerializableExtra("dateTime") == null ? new DateTime() : (DateTime)
                getIntent().getSerializableExtra("dateTime");
        now = new DateTime();
        Logger.d(mSelectDate.toString());
        initMonthCalendar();
    }

    private void initMonthCalendar() {
        int monthOfYear = mSelectDate.getMonthOfYear();
        int year = mSelectDate.getYear();
        mTvMonth.setText(getMonth(monthOfYear));
        mTvYear.setText(" " + year);
        monthCalendar.setGetViewHelper(new GetViewHelper() {
            @Override
            public View getDayView(int position, View convertView, ViewGroup parent, Day day) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CalenderActivity.this).inflate(R.layout.item_day, parent, false);
                }
                TextView tvDay = (TextView) convertView.findViewById(R.id.tv_day);
                DateTime dateTime = day.getDateTime();
                tvDay.setText(dateTime.toString("d"));
                boolean select = day.isSelect();
                if (CalendarUtil.isToday(dateTime) && select) {
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.retangle_blue);
                } else if (CalendarUtil.isToday(dateTime)) {
                    tvDay.setTextColor(getResources().getColor(R.color.colorTodayText));
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                } else if (select) {
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.retangle_blue);
                } else {
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                    if (day.isOtherMonth()) {
                        tvDay.setTextColor(Color.LTGRAY);
                    } else {
                        tvDay.setTextColor(Color.BLACK);
                    }
                }
                return convertView;
            }

            @Override
            public View getWeekView(int position, View convertView, ViewGroup parent, String week) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CalenderActivity.this).inflate(R.layout.item_week, parent, false);
                }
                TextView tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
                switch (position) {
                    case 0:
                        week = getResources().getString(R.string.Sunday);
                        tvWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 1:
                        week = getResources().getString(R.string.Monday);
                        break;
                    case 2:
                        week = getResources().getString(R.string.Tuesday);
                        break;
                    case 3:
                        week = getResources().getString(R.string.Wednesday);
                        break;
                    case 4:
                        week = getResources().getString(R.string.Thursday);
                        break;
                    case 5:
                        week = getResources().getString(R.string.Friday);
                        break;
                    case 6:
                        week = getResources().getString(R.string.Saturday);
                        tvWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                }
                tvWeek.setText(week);
                return convertView;
            }
        });
        monthCalendar.setSelectDateTime(mSelectDate);
        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(int currentYear, int currentMonth) {
                mTvMonth.setText(getMonth(currentMonth));
                mTvYear.setText(" " + currentYear);
            }
        });
        monthCalendar.setOnDateSelectListener(new OnDateSelectListener() {
            @Override
            public void onDateSelect(DateTime selectDate) {
                isClicked = true;
                mSelectDate = selectDate;
            }
        });
    }

    private String getMonth(int month) {
        String text = null;
        switch (month) {
            case 1:
                text = getResources().getText(R.string.January).toString();
                break;
            case 2:
                text = getResources().getText(R.string.February).toString();
                break;
            case 3:
                text = getResources().getText(R.string.March).toString();
                break;
            case 4:
                text = getResources().getText(R.string.April).toString();
                break;
            case 5:
                text = getResources().getText(R.string.May).toString();
                break;
            case 6:
                text = getResources().getText(R.string.June).toString();
                break;
            case 7:
                text = getResources().getText(R.string.July).toString();
                break;
            case 8:
                text = getResources().getText(R.string.August).toString();
                break;
            case 9:
                text = getResources().getText(R.string.September).toString();
                break;
            case 10:
                text = getResources().getText(R.string.October).toString();
                break;
            case 11:
                text = getResources().getText(R.string.November).toString();
                break;
            case 12:
                text = getResources().getText(R.string.December).toString();
                break;
        }
        return text;
    }

    @OnClick({R.id.tv_left, R.id.tv_right, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                monthCalendar.goToPreMonth();
                break;
            case R.id.tv_right:
                monthCalendar.goToNextMonth();
                break;
            case R.id.tv_confirm:
                Intent intent = getIntent();

                if (now.minusDays(1).getMillis() > mSelectDate.getMillis()) {
                    ToastUtil.toastShort(getString(R.string.date_error));
                    return;
                }

                int dayOfMonth = mSelectDate.getDayOfMonth();
                int monthOfYear = mSelectDate.getMonthOfYear();
                int year = mSelectDate.getYear();
                Logger.d(year + "年" + monthOfYear + "月" + dayOfMonth + "日");

                intent.putExtra("day", dayOfMonth + "");
                intent.putExtra("month", monthOfYear + "");
                intent.putExtra("year", year + "");
                intent.putExtra("dateTime", mSelectDate);

                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
