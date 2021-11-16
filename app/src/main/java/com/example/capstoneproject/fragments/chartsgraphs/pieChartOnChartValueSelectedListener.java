package com.example.capstoneproject.fragments.chartsgraphs;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class pieChartOnChartValueSelectedListener implements com.github.mikephil.charting.listener.OnChartValueSelectedListener {

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        System.out.println(e);
    }

    @Override
    public void onNothingSelected() {

    }
}
