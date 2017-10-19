package com.camunda.consulting.simplerestclient.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a container for {@link JSONObject} and {@link JSONArray}.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class JsonEntity {

  /**
   * flag that denotes if an instance of this class contains a {@link JSONArray}
   * or not.
   */
  private boolean array = false;

  /**
   * The JSONObject contained if {@code array == false}
   */
  private JSONObject jsonObject = null;

  /**
   * The JSONArray contained if {@code array == true}
   */
  private JSONArray jsonArray = null;

  /**
   * Constructor that checks if the received {@code jsonString} contains a
   * {@link JSONArray} or a {@link JSONObject}.
   * 
   * @param jsonString
   */
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

  /**
   * @return {@code true} if this instance contains a {@link JSONArray}, if
   *         {@code false} this instance contains a {@link JSONObject}
   */
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
