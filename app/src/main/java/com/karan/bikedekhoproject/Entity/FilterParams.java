package com.karan.bikedekhoproject.Entity;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by karanahuja on 14/07/15.
 */
public class FilterParams {
    JSONObject brand, price, fuelType, city, style, start_option,cc;

    public FilterParams() throws JSONException
    {
        brand = new JSONObject();
        price = new JSONObject();
        fuelType = new JSONObject();
        city = new JSONObject();
        style = new JSONObject();
        start_option = new JSONObject();
        cc = new JSONObject();

        this.setBrand("aprilla");
        this.setPrice("15000-rs-30000");
        this.setFuelType("Petrol");
        this.setCity("adilabad");
        this.setStyle("electric");
        this.setStart_option("kick-start");
        this.setCc("less-than-100cc");
    }

    public JSONObject getBrand() {
        return brand;
    }

    public void setBrand(String brand) throws JSONException{
        this.brand.put("0",brand);
    }

    public JSONObject getPrice() {
        return price;
    }

    public void setPrice(String price) throws JSONException{
        this.price.put("0",price);
    }

    public JSONObject getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) throws JSONException{
        this.fuelType.put("0",fuelType);
    }

    public JSONObject getCity() {
        return city;
    }

    public void setCity(String city) throws JSONException{
        this.city.put("0",city);
    }

    public JSONObject getStyle() {
        return style;
    }

    public void setStyle(String style) throws JSONException{
        this.style.put("0",style);
    }

    public JSONObject getStart_option() {
        return start_option;
    }

    public void setStart_option(String start_option) throws JSONException{
        this.start_option.put("0", start_option);
    }

    public JSONObject getCc() {
        return cc;
    }

    public void setCc(String cc) throws JSONException{
        this.cc.put("0",cc);
    }


}
