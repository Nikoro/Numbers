package com.dominikkrajcer.numbers.data;


import org.json.JSONException;
import org.json.JSONObject;

public class Number{

    private String name;
    private String text;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Number fromJSON(JSONObject jsonObject) {
        Number number = new Number();
        try {
            number.setName(jsonObject.getString("name"));
            number.setImageUrl(jsonObject.getString("image"));
            number.setText(jsonObject.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return number;
    }
}
