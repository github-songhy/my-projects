package com.earlywarning.common;


public class Chars {
    private static final long serialVersionUID = 1L;


    private String name;
    private String value;


    public Chars(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
