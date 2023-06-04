package com.example.dreamcatcher_2023_1.ui.alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private MutableLiveData<Integer> startHours;
    private MutableLiveData<Integer> startMinute;
    private MutableLiveData<Integer> alarmHours;
    private MutableLiveData<Integer> alarmMinute;
    private MutableLiveData<String> predictionTime;
    private MutableLiveData<Integer> endHours;
    private MutableLiveData<Integer> endMinute;
    private MutableLiveData<String> dayOfWeekStr;
    private MutableLiveData<String> memo;

    public AlarmViewModel() {
        startHours = new MutableLiveData<>();
        startMinute = new MutableLiveData<>();
        alarmHours = new MutableLiveData<>();
        alarmMinute = new MutableLiveData<>();
        predictionTime=new MutableLiveData<>();
        endHours = new MutableLiveData<>();
        endMinute = new MutableLiveData<>();
        dayOfWeekStr = new MutableLiveData<>();
        memo = new MutableLiveData<>();
    }
//AlarmFragment
    public LiveData<Integer> getStartHours() {
        return startHours;
    }

    public void setStartHours(int hours) {
        startHours.setValue(hours);
    }

    public LiveData<Integer> getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int minute) {
        startMinute.setValue(minute);
    }

    public LiveData<Integer> getAlarmHours() {
        return alarmHours;
    }

    public void setAlarmHours(int hours) {
        alarmHours.setValue(hours);
    }

    public LiveData<Integer> getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int minute) {
        alarmMinute.setValue(minute);
    }
    public LiveData<String> getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(String predictionTime) {
        this.predictionTime.setValue(predictionTime);
    }
//TrackingSleepFragment
    //측정 종료 시간
    public LiveData<Integer> getEndHours() {
        return endHours;
    }
    public void setEndHours(int hours) {
       endHours.setValue(hours);
    }
    //측정 종료 분
    public LiveData<Integer> getEndMinute() {
        return endMinute;
    }
    public void setEndMinute(int minute) {
        endMinute.setValue(minute);
    }

    //측정 요일
    public LiveData<String> getDayOfWeekStr() {
        return dayOfWeekStr;
    }
    public void setDayOfWeekStr(String dayOfWeekStr) {
        this.dayOfWeekStr.setValue(dayOfWeekStr);
    }
//EndSleepFragment
    //메모
    public LiveData<String> getMemo() {
    return memo;
}
    public void setMemo(String memo) {
        this.memo.setValue(memo);
    }
}
