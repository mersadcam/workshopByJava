package model;

import io.vertx.core.json.JsonObject;

public class FormAnswer {

	private Form form;
	private JsonObject jsonAnswer;
	private FormWriter writer;

	public FormAnswer(JsonObject jsonObject ){
	  this.jsonAnswer = jsonObject;
  }


  public JsonObject getJsonAnswer() {
    return jsonAnswer;
  }

  public void setJsonAnswer(JsonObject jsonAnswer) {
    this.jsonAnswer = jsonAnswer;
  }
}
