package com.dgkey.db;

import java.util.Arrays;

public class Mark {
    public int[] position_int;
    public int[] type_int;
    public String position;
    public String type;

    public Mark(String position, String type){
        this.position = position;
        this.type = type;
        this.position_int = Arrays.stream(position.split(",")).mapToInt(Integer::parseInt).toArray();
        this.type_int = Arrays.stream(type.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public String toString() {
        return "Mark{" +
                "position_int=" + Arrays.toString(position_int) +
                ", type_int=" + Arrays.toString(type_int) +
                ", position='" + position + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
