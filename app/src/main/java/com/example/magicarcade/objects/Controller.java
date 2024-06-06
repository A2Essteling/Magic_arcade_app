package com.example.magicarcade.objects;

public class Controller {
    private int ID;
    private int joy_x;
    private int joy_y;
    private boolean button_joy;
    private boolean button_1;
    private boolean button_2;

    public int getJoy_x() {
        return joy_x;
    }

    public void setJoy_x(int joy_x) {
        this.joy_x = joy_x;
    }

    public int getJoy_y() {
        return joy_y;
    }

    public void setJoy_y(int joy_y) {
        this.joy_y = joy_y;
    }

    public boolean isButton_joy() {
        return button_joy;
    }

    public void setButton_joy(boolean button_joy) {
        this.button_joy = button_joy;
    }

    public boolean isButton_1() {
        return button_1;
    }

    public void setButton_1(boolean button_1) {
        this.button_1 = button_1;
    }

    public boolean isButton_2() {
        return button_2;
    }

    public void setButton_2(boolean button_2) {
        this.button_2 = button_2;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Controller{" +
                "ID=" + ID +
                ", joy_x=" + joy_x +
                ", joy_y=" + joy_y +
                ", button_joy=" + button_joy +
                ", button_1=" + button_1 +
                ", button_2=" + button_2 +
                '}';
    }
}