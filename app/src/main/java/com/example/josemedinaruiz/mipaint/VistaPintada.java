package com.example.josemedinaruiz.mipaint;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.josemedinaruiz.mipaint.pojo.Circulo;
import com.example.josemedinaruiz.mipaint.pojo.Elipse;
import com.example.josemedinaruiz.mipaint.pojo.Linea;
import com.example.josemedinaruiz.mipaint.pojo.Rectangulo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class VistaPintada extends View {
    private Bitmap mapaDeBits;
    private Canvas canvasFondo;
    private float xi, yi, xf, yf;
    private float radio;
    private boolean pintando = false;
    private Path rectaPoligonal = new Path();
    private int color=Color.BLACK;
    private int fondo=Color.WHITE;
    private String dibujo="Linea";
    private int tamaño=12;
    private Paint pincel=new Paint();
    ArrayList<Object> misFiguras=new ArrayList<>();

    public VistaPintada(Context context) {
        super(context);
    }
    public VistaPintada(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void escribirLinea(Canvas canvas, Paint pincel){
        if (pintando) {
            canvas.drawLine(xi, yi, xf, yf, pincel);
        } else {
            canvasFondo.drawLine(xi, yi, xf, yf, pincel);
            misFiguras.add(new Linea(xi, yi, xf, yf, pincel));
        }
    }

    private void escribirCuadrado(Canvas canvas, Paint pincel){
        if (pintando) {
            pincel.setStyle(Paint.Style.STROKE);
            canvas.drawRect(xi, yi, xf, yf, pincel);
        } else {
            pincel.setStyle(Paint.Style.STROKE);
            canvasFondo.drawRect(xi, yi, xf, yf, pincel);
            misFiguras.add(new Rectangulo(xi, yi, xf, yf, pincel));
        }
    }

    private void escribirCirculo(Canvas canvas, Paint pincel){
        if (pintando) {
            pincel.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(xi, yi, radio, pincel);
        } else {
            pincel.setStyle(Paint.Style.STROKE);
            canvasFondo.drawCircle(xi, yi, radio, pincel);
            misFiguras.add(new Circulo(xi, yi, radio, pincel));
        }
    }

    private void escribirElipse(Canvas canvas, Paint pincel){
        if (pintando) {
            pincel.setStyle(Paint.Style.STROKE);
            canvas.drawOval(xi, yi, xf, yf, pincel);
        } else {
            pincel.setStyle(Paint.Style.STROKE);
            canvasFondo.drawOval(xi, yi, xf, yf, pincel);
            misFiguras.add(new Elipse(xi, yi, xf, yf, pincel));
        }
    }

    private void escribirPoligonal(Canvas canvas, Paint pincel){
        pincel.setStyle(Paint.Style.STROKE);
        canvasFondo.drawPath(rectaPoligonal, pincel);
    }

    private void init(Canvas canvas){
        pincel.setColor(fondo);
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(),pincel);
        pincel.setColor(color);
        pincel.setStrokeWidth(tamaño);
        canvas.drawBitmap(mapaDeBits, 0, 0, null);
        pincel.setColor(color);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
        /* Lo anterior lo hacemos siempre siempre */

        switch (dibujo.toLowerCase()){
            case "linea":
                escribirLinea(canvas,pincel);
                break;
            case "cuadrado":
                escribirCuadrado(canvas,pincel);
                break;
            case "circulo":
                escribirCirculo(canvas,pincel);
                break;
            case "elipse":
                escribirElipse(canvas,pincel);
                break;
            case "poligonal":
                escribirPoligonal(canvas,pincel);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasFondo = new Canvas(mapaDeBits);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("TAG", "onTouchEvent");
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // Pulsar el dedo
                pintando = true;
                xf = xi = x;
                yf = yi = y;
                if(dibujo.equalsIgnoreCase("poligonal")) {
                    rectaPoligonal.reset();
                    rectaPoligonal.moveTo(xi, yi);
                }
                break;
            case MotionEvent.ACTION_MOVE: // Mover el dedo
                if(dibujo.equalsIgnoreCase("poligonal")) {
                    xi = xf;
                    yi = yf;
                    xf = x;
                    yf = y;
                    rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi) / 2);
                }else{
                    xf = x;
                    yf = y;
                }
                break;
            case MotionEvent.ACTION_UP: // Quitar el dedo
                pintando = false;
                if(dibujo.equalsIgnoreCase("poligonal")) {
                    xi = xf;
                    yi = yf;
                }
                xf = x;
                yf = y;
                break;
        }
        radio = (float) Math.sqrt(Math.pow(xf - xi, 2) + Math.pow(yf - yi, 2));

        invalidate(); // Le dices a la vista que se tiene que redibujar
        return true;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDibujo(String dibujo) {
        this.dibujo = dibujo;
    }

    public void setFondo(int fondo) {
        this.fondo = fondo;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public void undo(){
        if(misFiguras.size()!=0) {
            misFiguras.remove(misFiguras.size() - 1);
            canvasFondo.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            /*pincel.setColor(fondo);
            pincel.setAntiAlias(true);
            pincel.setStyle(Paint.Style.FILL);
            canvasFondo.drawRect(0, 0, getWidth(), getHeight(),pincel);*/
            for (Object miFigura:misFiguras) {
                if(miFigura.getClass()== Circulo.class){
                    Circulo c=(Circulo)miFigura;
                    canvasFondo.drawCircle(c.getXi(),c.getYi(),c.getRadio(),c.getPincel());

                }else if(miFigura.getClass()== Rectangulo.class){
                    Rectangulo r=(Rectangulo)miFigura;
                    canvasFondo.drawRect(r.getXi(),r.getYi(),r.getXf(),r.getYf(),r.getPincel());

                }else if(miFigura.getClass()== Elipse.class){
                    Elipse e=(Elipse)miFigura;
                    canvasFondo.drawOval(e.getXi(),e.getYi(),e.getXf(),e.getYf(),e.getPincel());

                }else if(miFigura.getClass()==Linea.class){
                    Linea l=(Linea)miFigura;
                    canvasFondo.drawLine(l.getXi(),l.getYi(),l.getXf(),l.getYf(),l.getPincel());
                }
            }
        }
    }
}
