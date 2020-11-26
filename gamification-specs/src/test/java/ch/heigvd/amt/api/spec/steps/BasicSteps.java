package ch.heigvd.amt.api.spec.steps;

import ch.heigvd.amt.ApiException;
import ch.heigvd.amt.ApiResponse;
import ch.heigvd.amt.api.DefaultApi;
import ch.heigvd.amt.api.dto.*;
import ch.heigvd.amt.api.dto.Event;
import ch.heigvd.amt.api.spec.helpers.Environment;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.awt.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;

    Badge badge;
    PointScale pointScale;
    User user;
    Event event;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private ApiKey apiKey;
    private String lastReceivedLocationHeader;

    private Badge lastReceivedBadge;
    private PointScale lastReceivedPointScale;
    private User lastReceivedUser;

    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("there is a Gamification server")
    public void there_is_a_Gamification_server() throws Throwable {
        assertNotNull(api);
    }

    @Given("there is a X-API-Key valid")
    public void thereIsAXAPIKeyValid() throws ApiException {
        apiKey = api.registerApplication();
        assert apiKey.getValue() != null;
        api.getApiClient().addDefaultHeader("X-API-KEY", apiKey.getValue().toString());
    }

    @Given("I have a badge payload")
    public void i_have_a_badge_payload() throws Throwable {
        badge = new ch.heigvd.amt.api.dto.Badge()
                .name("Diamond")
                .obtainedDate(LocalDate.now())
                .imageUrl("https://st2.depositphotos.com/1000393/10030/i/600/depositphotos_100308166-stock-photo-diamond-classic-cut.jpg");
    }

    @When("^I POST the badge payload to the /badges endpoint$")
    public void i_POST_the_badge_payload_to_the_badges_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("I receive a {int} status code")
    public void i_receive_a_status_code(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, lastStatusCode);
    }

    @When("^I send a GET to the /badges endpoint$")
    public void iSendAGETToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the badge URL in the location header")
    public void iSendAGETToTheBadgeURLInTheLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getBadgeWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the badge payload")
    public void iReceiveAPayloadThatIsTheSameAsTheBadgePayload() {
        assertEquals(badge, lastReceivedBadge);
    }

    @Given("I have a pointScale payload")
    public void i_have_a_pointScale_payload() throws Throwable {
        pointScale = new ch.heigvd.amt.api.dto.PointScale()
                .name("Diamonds Category")
                .description("it's a diamond");
    }

    @When("^I POST the pointScale payload to the /pointScales endpoint$")
    public void i_POST_the_pointScale_payload_to_the_pointScales_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createPointScaleWithHttpInfo(pointScale);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /pointScales endpoint$")
    public void iSendAGETToThePointScalesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the pointScale URL in the location header")
    public void iSendAGETToThePointScaleURLInTheLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getPointScaleWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedPointScale = (PointScale) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the pointScale payload")
    public void iReceiveAPayloadThatIsTheSameAsThePointScalePayload() {
        assertEquals(pointScale, lastReceivedPointScale);
    }

    @Given("there is a user with an ID")
    public void thereIsAnUserWithAnId() throws Throwable {
        user = new ch.heigvd.amt.api.dto.User()
                .id(UUID.randomUUID().toString());
    }

    @Given("I have an event payload")
    public void iHaveAnEventPayload() throws Throwable {
        event = new ch.heigvd.amt.api.dto.Event()
                .timestamp(OffsetDateTime.now())
                .type("Rule type")
                .userId(user.getId());
    }

    @When("^I POST the event payload to the /events endpoint$")
    public void i_POST_the_event_payload_to_the_events_endpoint() throws Throwable {
        try {
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the user URL in the location header")
    public void iSendAGETToTheUserURLInTheLocationHeader() {
        try {
            lastApiResponse = api.getUserWithHttpInfo(user.getId());
            processApiResponse(lastApiResponse);
            lastReceivedUser = (User) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload with points and badges")
    public void iReceiveAPayloadWithPointsAndBadges() {
        assert user.getId() != null;
        String lastReceivedUserString = lastReceivedUser.toString();
        Assert.assertTrue(lastReceivedUserString.contains(user.getId()));
        Assert.assertTrue(lastReceivedUserString.contains("points"));
        Assert.assertTrue(lastReceivedUserString.contains("badges"));
    }

    @When("^I send a GET to the /users endpoint$")
    public void iSendAGETToTheUsersEndpoint() {
        try {
            lastApiResponse = api.getUsersWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    private void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
        lastStatusCode = lastApiResponse.getStatusCode();
        List<String> locationHeaderValues = (List<String>)lastApiResponse.getHeaders().get("Location");
        lastReceivedLocationHeader = locationHeaderValues != null ? locationHeaderValues.get(0) : null;
    }

    private void processApiException(ApiException apiException) {
        lastApiCallThrewException = true;
        lastApiResponse = null;
        lastApiException = apiException;
        lastStatusCode = lastApiException.getCode();
    }
}
