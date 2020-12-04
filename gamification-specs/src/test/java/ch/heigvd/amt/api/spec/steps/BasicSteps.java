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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

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

    // Keep track of the application created during the scenarios execution
    private final Map<String, ApiKey> applications = new HashMap<>();

    // Keep track of the application user created during the scenarios execution
    private final Map<String, Map<String, User>> applicationUsers = new HashMap<>();

    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    /*
        ====  GENERAL  ====
     */

    @Given("there is a Gamification server")
    public void thereIsAGamificationServer() throws Throwable {
        assertNotNull(api);
    }

    @Given("there is a X-API-Key valid for the application {string}")
    public void thereIsAXAPIKeyValidForApplication(String applicationReference) throws ApiException {
        apiKey = api.registerApplication();
        assert apiKey.getValue() != null;
        api.getApiClient().addDefaultHeader("X-API-KEY", apiKey.getValue().toString());
        applications.put(applicationReference, apiKey);
        applicationUsers.put(applicationReference, new HashMap<>());
    }

    @Given("there is a user {string} with an ID for the application {string}")
    public void thereIsAnUserWithAnIdForTheApplication(String userReference, String applicationReference)
            throws Throwable {
        user = new ch.heigvd.amt.api.dto.User()
                .id(UUID.randomUUID().toString());

        applicationUsers.get(applicationReference).put(userReference, user);
    }

    /*
        ====  PAYLOADS  ====
    */

    @Given("The application has a badge payload")
    public void iHaveABadgePayload() throws Throwable {
        badge = new ch.heigvd.amt.api.dto.Badge()
                .name("Diamond")
                .obtainedDate(LocalDate.now())
                .imageUrl(
                        "https://st2.depositphotos.com/1000393/10030/i/600/depositphotos_100308166-stock-photo" +
                                "-diamond-classic-cut.jpg");
    }

    @Given("The application has a pointScale payload")
    public void iHaveAPointScalePayload() throws Throwable {
        pointScale = new ch.heigvd.amt.api.dto.PointScale()
                .name("Diamonds Category")
                .description("it's a diamond");
        ;
    }

    @Given("I have an event payload")
    public void iHaveAnEventPayload() throws Throwable {
        event = new ch.heigvd.amt.api.dto.Event()
                .timestamp(OffsetDateTime.now())
                .type("Rule type")
                .userId(user.getId());
    }

    /*
        ====  POSTS  ====
    */

    @When("The application {string} POST the {string} badge payload to the \\/badges endpoint")
    public void theApplicationPOSTTheBadgePayloadToTheBadgesEndpoint(String applicationReference, String name)
            throws Throwable {
        try {

            // change api key if needed
            checkCurrentApplication(applicationReference);

            badge.setName(name);
            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @When("The application {string} POST the {string} pointScale payload to the \\/pointScales endpoint")
    public void theApplicationPOSTThePointScalePayloadToThePointScalesEndpoint(String applicationReference,
                                                                               String name) {
        try {
            checkCurrentApplication(applicationReference);
            pointScale.setName(name);
            lastApiResponse = api.createPointScaleWithHttpInfo(pointScale);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @When("^I POST the event payload to the /events endpoint")
    public void iPOSTTheEventPayloadToTheEventsEndpoint() throws Throwable {
        try {
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    /*
        ====  GETS  ====
    */


    @When("^A unregistered application sends a GET to the /badges endpoint")
    public void aUnregisteredApplicationSendAGETToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} sends a GET to the \\/badges endpoint")
    public void theApplicationSendAGETToTheBadgesEndpoint(String applicationReference) {
        try {

            checkCurrentApplication(applicationReference);

            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} sends a GET to the badge URL in the location header")
    public void theApplicationSendAGETToTheURLInTheLocationHeaderBadges(String applicationReference) {
        Integer id = Integer
                .parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.getBadgeWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @When("^A unregistered application sends a GET to the \\/pointScale endpoint")
    public void aUnregisteredApplicationSendAGETToThePointScaleEndpoint() {
        try {
            lastApiResponse = api.getPointScalesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} sends a GET to the \\/pointScales endpoint")
    public void theApplicationSendAGETToThePointScalesEndpoint(String applicationReference) {
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.getPointScalesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} sends a GET to the pointScale URL in the location header")
    public void theApplicationSendAGETToThePointScaleURLInTheLocationHeader(String applicationReference) {
        Integer id = Integer
                .parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.getPointScaleWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedPointScale = (PointScale) lastApiResponse.getData();
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

    @When("^I send a GET to the /users endpoint$")
    public void iSendAGETToTheUsersEndpoint() {
        try {
            lastApiResponse = api.getUsersWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    /*
        ====  PATCH  ====
    */


    @When("The application {string} PATCH a badge, he rename the badge named {string} into {string}")
    public void theApplicationPATCHABadgeHeRenameTheBadgeNamedInto(String applicationReference, String oldName,
                                                                   String newName) {


        try {
            checkCurrentApplication(applicationReference);

            ApiResponse<List<Badge>> getBadgeResponse = api.getBadgesWithHttpInfo();
            List<Badge> badges = getBadgeResponse.getData();
            Badge target = null;
            for (Badge badge : badges) {
                if (badge.getName().equals(oldName)) {
                    target = badge;
                    break;
                }
            }

            String path = target.getLinks().get(0).getSelf().getPath();


            String id = path.substring(path.lastIndexOf('/') + 1);

            JsonPatchDocument patchDocument = new JsonPatchDocument()
                    .op(JsonPatchDocument.OpEnum.REPLACE)
                    .path("/name")
                    .value(newName);

            lastApiResponse = api.patchBadgeWithHttpInfo(Integer.parseInt(id), Arrays.asList(patchDocument));
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    /*
        ====  RECEPTION  ====
    */

    @Then("The application receives a {int} status code")
    public void theApplicationReceiveAStatusCode(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, lastStatusCode);
    }

    @And("The application receives a payload that is the same as the badge payload")
    public void theApplicationReceivesAPayloadThatIsTheSameAsTheBadgePayload() {
        // Dont check links
        assertEquals(badge.getName(), lastReceivedBadge.getName());
        assertEquals(badge.getObtainedDate(), lastReceivedBadge.getObtainedDate());
        assertEquals(badge.getImageUrl(), lastReceivedBadge.getImageUrl());
    }

    @And("The application receives a payload that is the same as the pointScale payload")
    public void theApplicationReceiveAPayloadThatIsTheSameAsThePointScalePayload() {
        assertEquals(pointScale, lastReceivedPointScale);
    }

    @And("I receive a payload with points and badges")
    public void iReceiveAPayloadWithPointsAndBadges() {
        assert user.getId() != null;
        String lastReceivedUserString = lastReceivedUser.toString();
        Assert.assertTrue(lastReceivedUserString.contains(user.getId()));
        Assert.assertTrue(lastReceivedUserString.contains("points"));
        Assert.assertTrue(lastReceivedUserString.contains("badges"));
    }

    @And("The application {string} GET to the /badges endpoint receive a list containing {int} badge\\(s)")
    public void theApplicationReceiveAListContainingBadges(String applicationReference, int size) {

        try {
            checkCurrentApplication(applicationReference);
            List<Badge> badges = api.getBadges();
            assertEquals(size, badges.size());
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("The application {string} GET to the /pointScales endpoint receive a list containing {int} pointScale\\(s)")
    public void theApplicationReceiveAListContainingPointScales(String applicationReference, int size) {

        try {
            checkCurrentApplication(applicationReference);
            List<PointScale> pointScales = api.getPointScales();
            assertEquals(pointScales.size(), size);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("The application receives a badge that was created today")
    public void theApplicationReceiveABadgeThatWasCreatedToday() {
        assertEquals(badge.getObtainedDate(), LocalDate.now());
    }

    /*
        ====  UTILS  ====
    */

    private void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
        lastStatusCode = lastApiResponse.getStatusCode();
        List<String> locationHeaderValues = (List<String>) lastApiResponse.getHeaders().get("Location");
        lastReceivedLocationHeader = locationHeaderValues != null ? locationHeaderValues.get(0) : null;
    }

    private void processApiException(ApiException apiException) {
        lastApiCallThrewException = true;
        lastApiResponse = null;
        lastApiException = apiException;
        lastStatusCode = lastApiException.getCode();
    }

    // change api key if needed
    private void checkCurrentApplication(String applicationReference) {
        if (apiKey.getValue() != applications.get(applicationReference).getValue()) {
            apiKey = applications.get(applicationReference);
            api.getApiClient().addDefaultHeader("X-API-KEY", apiKey.getValue().toString());
        }
    }

}
