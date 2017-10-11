package com.camunda.consulting.simplerestclient.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonEntity {

  private boolean array = false;

  private JSONObject jsonObject = null;
  private JSONArray jsonArray = null;

  public JsonEntity(String jsonString) {

    try {
      jsonObject = new JSONObject(jsonString);
    } catch (JSONException e1) {
      try {
        array = true;
        jsonArray = new JSONArray(jsonString);
      } catch (JSONException e2) {
        throw new RuntimeException(e2);
      }
    }
  }

  public boolean isArray() {
    return array;
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public JSONArray getJsonArray() {
    return jsonArray;
  }
  
}
