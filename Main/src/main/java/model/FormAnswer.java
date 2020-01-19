package model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class FormAnswer {

	private Form form;
	private JsonArray jsonAnswer;
	private FormWriter writer;

	public FormAnswer(JsonArray jsonObject ){
	  this.jsonAnswer = jsonObject;
  }


  public JsonArray getJsonAnswer() {
    return jsonAnswer;
  }

  public void setJsonAnswer(JsonArray jsonAnswer) {
    this.jsonAnswer = jsonAnswer;
  }
}
