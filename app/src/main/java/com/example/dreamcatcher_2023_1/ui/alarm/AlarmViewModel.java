package com.example.dreamcatcher_2023_1.ui.alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private MutableLiveData<Integer> startHours;
    private MutableLiveData<Integer> startMinute;
    private MutableLiveData<Integer> sleepHours;
    private MutableLiveData<Integer> sleepMinute;
    private MutableLiveData<Integer> alarmHours;
    private MutableLiveData<Integer> alarmMinute;
    private MutableLiveData<String> predictionTime;
    private MutableLiveData<Integer> endHours,totalHours,totalMinute;
    private MutableLiveData<Integer> endMinute;
    private MutableLiveData<String> dayOfWeekStr;
    private MutableLiveData<String> memo;
    private MutableLiveData<String> alarmAmPm,endAmPm;
    private MutableLiveData<String> sleepAmPm,startAmPm;
    private MutableLiveData<Float> satisfaction;

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
        sleepAmPm = new MutableLiveData<>();
        sleepHours = new MutableLiveData<>();
        alarmAmPm = new MutableLiveData<>();
        sleepMinute = new MutableLiveData<>();
        satisfaction = new MutableLiveData<>();
        endAmPm=new MutableLiveData<>();
        startAmPm=new MutableLiveData<>();
        totalHours=new MutableLiveData<>();
        totalMinute=new MutableLiveData<>();
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
    public LiveData<String> getAlarmAmPm() {
        return alarmAmPm;
    }

    public void setAlarmAmPm(String alarmAmPm) {
        this.alarmAmPm.setValue(alarmAmPm);
    }

    public LiveData<String> getStartAmPm() {
        return startAmPm;
    }
    public void setStartAmPm(String startAmPm) {
        this.startAmPm.setValue(startAmPm);
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
    //측정 종료 오전/오후
    public LiveData<String> getEndAmPm() {
        return endAmPm;
    }
    public void setEndAmPm(String endAmPm) {
        this.endAmPm.setValue(endAmPm);
    }
    //수면 시작 시간
    public LiveData<Integer> getSleepHours() {
        return sleepHours;
    }
    public void setSleepHours(int hours) {
        sleepHours.setValue(hours);
    }

    public LiveData<Integer> getSleepMinute() {
        return sleepMinute;
    }
    public void setSleepMinute(int minute) {
        sleepMinute.setValue(minute);
    }
    public LiveData<String> getSleepAmPm() {
        return sleepAmPm;
    }

    public void setSleepAmPm(String sleepAmPm) {
        this.sleepAmPm.setValue(sleepAmPm);
    }

    //측정 요일
    public LiveData<String> getDayOfWeekStr() {
        return dayOfWeekStr;
    }
    public void setDayOfWeekStr(String dayOfWeekStr) {
        this.dayOfWeekStr.setValue(dayOfWeekStr);
    }
//EndSleepFragment
    //총 수면시간
public LiveData<Integer> getTotalHours() {
    return totalHours;
}
    public void setTotalHours(int totalHours) {
        this.totalHours.setValue(totalHours);
    }
    public LiveData<Integer> getTotalMinute() {
        return totalMinute;
    }
    public void setTotalMinute(int totalMinute) {
        this.totalMinute.setValue(totalMinute);
    }
    //메모
    public LiveData<String> getMemo() {
    return memo;
}
    public void setMemo(String memo) {
        this.memo.setValue(memo);
    }

    public LiveData<Float> getSatisfaction() {
        return satisfaction;
    }
    public void setSatisfaction(float satisfaction) {
        this.satisfaction.setValue(satisfaction);
    }
}
