package apiTests;

import org.testng.annotations.Test;

import core.Log;
import io.restassured.response.Response;

public class CompleteTests extends BaseApi {

	@Test
	public void m1() {

		Response response = given().body("\n" + "{\n" + "	\"username\":\"coding.challenge.login@upgrade.com\",\n"
				+ "	\"password\":\"On$3XcgsW#9q\"\n" + "\n" + "}").log().all().post("login");
		Log.info(response.asString());

	}

}
