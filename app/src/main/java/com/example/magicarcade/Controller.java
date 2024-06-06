package com.example.magicarcade;

public class Controller {

    public Controller(){

    }

    public void getSignal(char signalCharacter){
        switch(String.valueOf(signalCharacter).toLowerCase()){
            case "U": joystickUp();
            case "D": joystickDown();
            case "L": joystickLeft();
            case "R": joystickRight();
            case "B": joystickButton();
            case "Q": buttonOne();
            case "E": buttonTwo();
        }
    }

    public void joystickUp(){

    }

    public void joystickDown(){

    }

    public void joystickLeft(){

    }

    public void joystickRight(){

    }

    public void joystickButton(){

    }

    public void buttonOne(){

    }

    public void buttonTwo(){

    }
}