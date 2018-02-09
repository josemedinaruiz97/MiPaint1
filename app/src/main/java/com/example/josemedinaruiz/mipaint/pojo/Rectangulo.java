package com.example.josemedinaruiz.mipaint.pojo;

import android.graphics.Paint;

/**
 * Created by 2damm on 9/2/18.
 */

public class Rectangulo {
    private float xi;
    private float yi;
    private float xf;
    private float yf;
    private Paint pincel;

    public Rectangulo(float xi, float yi, float xf, float yf, Paint pincel) {
        this.xi = xi;
        this.yi = yi;
        this.xf = xf;
        this.yf = yf;
        this.pincel = pincel;
    }

    public float getXi() {
        return xi;
    }

    public void setXi(float xi) {
        this.xi = xi;
    }

    public float getYi() {
        return yi;
    }

    public void setYi(float yi) {
        this.yi = yi;
    }

    public float getXf() {
        return xf;
    }

    public void setXf(float xf) {
        this.xf = xf;
    }

    public float getYf() {
        return yf;
    }

    public void setYf(float yf) {
        this.yf = yf;
    }

    public Paint getPincel() {
        return pincel;
    }

    public void setPincel(Paint pincel) {
        this.pincel = pincel;
    }
}
