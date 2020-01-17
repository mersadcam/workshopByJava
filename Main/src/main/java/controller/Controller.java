package controller;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Controller {


  public class newFormHandler implements Handler<RoutingContext>{


    @Override
    public void handle(RoutingContext ctx) {

      JsonObject userJson = ctx.get("user");
      JsonObject reqJson = ctx.get("json");
      String userType = ctx.get("userType");
      String workshopName = reqJson.getString("workshopName");

    }

  }


}
