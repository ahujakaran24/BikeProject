package com.karan.bikedekhoproject.Utils;

import java.util.Objects;

/**
 * Created by karanahuja on 13/07/15.
 */
public class MyEvents {

    public static final int BIKE_RESPONSE = 1;
    public static final int FILTER_RESPONSE = 2;

    private boolean status;
    private int type;
    private Object value;

    public MyEvents(int type, boolean status, Object value)
    {
        this.status = status;
        this.type = type;
        this.value = value;
    }

    public boolean isStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
