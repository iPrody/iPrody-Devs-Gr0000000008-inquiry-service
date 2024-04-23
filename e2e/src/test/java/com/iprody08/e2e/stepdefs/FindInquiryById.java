package com.iprody08.e2e.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
@Log4j2
public class FindInquiryById {

    private HttpResponse response;
    private String endpoint;
    @Given("Inquiry endpoint {string} with http method GET available")
    public void inquiryEndpointWithHttpMethodGETAvailable(String endpointPart) {
        this.endpoint = endpointPart;

    }

    @When("client wants to find a inquiry with id {int}")
    public void clientWantsToFindAInquiryWithId(long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8085/" + endpoint + id))
                .GET()
                .build();

        this.response= client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    @Then("response code is {int}")
    public void responseCodeIs(int expectedCode) {
        final int actualCode = response.statusCode();
        assertThat(actualCode).isEqualTo(expectedCode);
    }
}
