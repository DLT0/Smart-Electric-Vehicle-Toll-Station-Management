package com.evstation;

public class Main {

    public static void main(String[] args) {
        QuanLyTramSac module = new QuanLyTramSac();
        Menu menu = new Menu(module);
        menu.chayChuongTrinh();
    }

}
