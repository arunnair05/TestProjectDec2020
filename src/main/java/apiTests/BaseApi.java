package apiTests;

import java.util.UUID;

import core.Log;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
	RequestSpecification req;

	BaseApi() {

		RestAssured.baseURI = "https://credapi.credify.tech";
		RestAssured.basePath = "api/brportorch/v2";
		Log.info(" Base uri :  " + RestAssured.baseURI);
		Log.info(" Base Path :  " + RestAssured.basePath);

	}

	public RequestSpecification given() {
		//
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		return req = RestAssured.given().contentType(ContentType.JSON).header("x-cf-source-id", "coding-challenge")
				.header("x-cf-corr-id", randomUUIDString);

	}

}