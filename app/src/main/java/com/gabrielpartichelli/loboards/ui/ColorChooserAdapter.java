package com.gabrielpartichelli.loboards.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ColorChooserAdapter extends ArrayAdapter<Integer> {
    public ColorChooserAdapter(@NonNull Context context, int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setBackgroundColor(getItem(position));
        view.setText("");
        view.setContentDescription(getColorName(getItem(position)));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setBackgroundColor(getItem(position));
        view.setText("");
        view.setContentDescription(getColorName(getItem(position)));
        return view;
    }

    private String getColorName(int color) {
        switch (color) {
            case Color.RED:
                return "Vermelho";
            case Color.BLUE:
                return "Azul";
            case Color.CYAN:
                return "Ciano";
            case Color.MAGENTA:
                return "Magenta";
            case Color.YELLOW:
                return "Amarelo";
            case Color.GREEN:
                return "Verde";
            default:
                return "";
        }
    }
}
