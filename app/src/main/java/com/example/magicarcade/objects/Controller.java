package com.example.magicarcade.objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Controller extends ViewModel {
    private final MutableLiveData<Double> joyX = new MutableLiveData<Double>();
    private final MutableLiveData<Double> joyY = new MutableLiveData<Double>();
    private final MutableLiveData<Boolean> buttonJoy = new MutableLiveData<>();
    private final MutableLiveData<Boolean> button1 = new MutableLiveData<>();
    private final MutableLiveData<Boolean> button2 = new MutableLiveData<>();
    private final MutableLiveData<Integer> ID = new MutableLiveData<>();

    public LiveData<Double> getJoyX() {
        return joyX;
    }

    public void setJoyX(double joyX) {
        this.joyX.postValue(joyX);
    }

    public LiveData<Double> getJoyY() {
        return joyY;
    }

    public void setJoyY(double joyY) {
        this.joyY.postValue(joyY);
    }

    public LiveData<Boolean> getButtonJoy() {
        return buttonJoy;
    }

    public void setButtonJoy(boolean buttonJoy) {
        this.buttonJoy.postValue(buttonJoy);
    }

    public LiveData<Boolean> getButton1() {
        return button1;
    }

    public void setButton1(boolean button1) {
        this.button1.postValue(button1);
    }

    public LiveData<Boolean> getButton2() {
        return button2;
    }

    public void setButton2(boolean button2) {
        this.button2.postValue(button2);
    }

    public LiveData<Integer> getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.postValue(ID);
    }

    @Override
    public String toString() {
        return "Controller{" +
                "ID=" + ID.getValue() +
                ", joyX=" + joyX.getValue() +
                ", joyY=" + joyY.getValue() +
                ", buttonJoy=" + buttonJoy.getValue() +
                ", button1=" + button1.getValue() +
                ", button2=" + button2.getValue() +
                '}';
    }
}
