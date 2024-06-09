package com.septian.kalkulus.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.labels.XYToolTipGenerator;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.geom.RectShape;

public class GraphView extends View {

    private final XYSeriesCollection collection;
    public GraphView(Context context, XYSeriesCollection collection) {
        super(context);
        this.collection=collection;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int widthScreen = displayMetrics.widthPixels;

        RectShape rectShape = new RectShape(0, 0, widthScreen, (widthScreen/2));
        AFreeChart chart = ChartFactory.createXYLineChart("Quadratic Inequality Graph", "x", "y", collection, PlotOrientation.HORIZONTAL, true, true, false);
        chart.draw(canvas, rectShape);
    }
}