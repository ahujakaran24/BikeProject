package com.karan.bikedekhoproject.Entity;

import java.util.ArrayList;

/**
 * Created by karanahuja on 16/07/15.
 */

/*Maintains selection of items clicked on filter*/
public class Selection {

    ArrayList<String> selected;
    ArrayList<String> checkedPosition;

    public Selection()
    {
        selected = new ArrayList<>();
        checkedPosition = new ArrayList<>();
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }

    public ArrayList<String> getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(ArrayList<String> checkedPosition) {
        this.checkedPosition = checkedPosition;
    }
}
