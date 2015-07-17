package com.karan.bikedekhoproject.Interfaces;

import java.util.ArrayList;

/**
 * Created by karanahuja on 16/07/15.
 */
public interface ChangeSelectionListener {

    //Key - > cc, kmpl, brand etc
    //Value -> line_rewrite
     void addSelectedItem(String key, String value);

     void removeUnselectedItem(String key, String value);

    void addCheckedItem(String key, String position);

    void removeCheckedItem ( String key, String position);

    ArrayList<String> getCheckedItems(String key);


}
