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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import org.junit.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;

    Badge badge;
    PointScale pointScale;
    User user;
    Event event;
    Rule rule;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private ApiKey apiKey;
    private String lastReceivedLocationHeader;

    private BadgeResponse lastReceivedBadge;
    private PointScaleResponse lastReceivedPointScale;
    private User lastReceivedUser;
    private Rule lastReceivedRule;

    private String lastRuleType;

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
        System.out.println("API KEY : " + apiKey.getValue());
    }

    @Given("there is a user {string} with an ID for the application {string}")
    public void thereIsAnUserWithAnIdForTheApplication(String userReference, String applicationReference)
            throws Throwable {
        user = new ch.heigvd.amt.api.dto.User()
                .id(UUID.randomUUID().toString());

        applicationUsers.get(applicationReference).put(userReference, user);
    }

    @And("The application {string} contains a badge named {string} as {string} attribute")
    public void theApplicationContainsABadgeNamedAsAttribute(String applicationReference, String value,
                                                             String attribute) {
        try {
            checkCurrentApplication(applicationReference);

            ApiResponse<List<BadgeResponse>> getBadgeResponse = api.getBadgesWithHttpInfo();
            List<BadgeResponse> badges = getBadgeResponse.getData();

            assertThat(badges, contains(
                    hasProperty(attribute, is(value))
            ));
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @And("The application {string} contains a point scale named {string} as {string} attribute")
    public void theApplicationContainsAPointScaleNamedAsAttribute(String applicationReference, String value,
                                                                  String attribute) {

        try {
            checkCurrentApplication(applicationReference);

            ApiResponse<List<PointScaleResponse>> getPointScalesResponse = api.getPointScalesWithHttpInfo();
            List<PointScaleResponse> pointScales = getPointScalesResponse.getData();

            assertThat(pointScales, contains(
                    hasProperty(attribute, is(value))
            ));

        } catch (ApiException e) {
            processApiException(e);
        }

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

    @Given("The application has a {string} event payload")
    public void iHaveAnEventPayload(String ruleType) throws Throwable {
        event = new ch.heigvd.amt.api.dto.Event()
                .timestamp(OffsetDateTime.now())
                .type(ruleType)
                .userId(user.getId());
    }

    @Given("The application has a rule payload")
    public void theApplicationHasARulePayload() {
        lastRuleType = generateRandomNewString();
        rule = new ch.heigvd.amt.api.dto.Rule()
                ._if(new RuleIf().type(lastRuleType))
                .then(new RuleThen()
                        .awardBadge(URI.create("badges/1"))
                        .awardPoints(new RuleThenAwardPoints()
                            .amount(1)
                            .pointScale(URI.create("pointScales/1"))
                        )
                );
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

    @When("The application {string} POST the {string} date badge payload to the \\/badges endpoint")
    public void theApplicationPOSTTheDateBadgePayloadToTheBadgesEndpoint(String applicationReference, String date)
            throws Throwable {
        try {

            // change api key if needed
            checkCurrentApplication(applicationReference);

            if(!date.isEmpty())
                badge.setObtainedDate(LocalDate.parse(date));
            else
                badge.setObtainedDate(null);

            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the {string} imageURL badge payload to the \\/badges endpoint")
    public void theApplicationPOSTTheImageURLBadgePayloadToTheBadgesEndpoint(String applicationReference, String imageUrl) {
        try {
            checkCurrentApplication(applicationReference);
            badge.setImageUrl(imageUrl);
            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the event payload to the /events endpoint")
    public void iPOSTTheEventPayloadToTheEventsEndpoint(String applicationReference) throws Throwable {
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the event {string} payload to the \\/events endpoint")
    public void theApplicationPOSTTheEventPayloadToTheEventsEndpoint(String applicationReference, String type) {
        try {
            checkCurrentApplication(applicationReference);
            event.setType(type);
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the event timestamp {string} payload to the \\/events endpoint")
    public void theApplicationPOSTTheEventTimestampPayloadToTheEventsEndpoint(String applicationReference, String timestamp) {
        try {
            checkCurrentApplication(applicationReference);
            if (timestamp.isEmpty())
                event.setTimestamp(null);
            else
                event.setTimestamp(OffsetDateTime.parse(timestamp));
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the event userId {string} payload to the \\/events endpoint")
    public void theApplicationPOSTTheEventUserIdPayloadToTheEventsEndpoint(String applicationReference, String userId) {
        try {
            checkCurrentApplication(applicationReference);
            event.setUserId(userId);
            lastApiResponse = api.eventProcessWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @When("The application {string} POST the {string} pointScale payload to the /pointScales endpoint")
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

    @And("The application {string} POST the {string} description pointScale payload to the \\/pointScales endpoint")
    public void theApplicationPOSTTheDescriptionPointScalePayloadToThePointScalesEndpoint(String applicationReference, String description) {
        try {
            checkCurrentApplication(applicationReference);
            pointScale.setDescription(description);
            lastApiResponse = api.createPointScaleWithHttpInfo(pointScale);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the {string} rule payload to the \\/rules endpoint")
    public void theApplicationPOSTTheRulePayloadToTheRulesEndpoint(String applicationReference, String ruleName) {
        try {
            checkCurrentApplication(applicationReference);
            rule.setIf(new RuleIf().type(ruleName));
            lastApiResponse = api.createRuleWithHttpInfo(rule);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the {string} awardBadge of then rule payload to the \\/rules endpoint")
    public void theApplicationPOSTTheAwardBadgeOfThenRulePayloadToTheRulesEndpoint(String applicationReference, String awardBadge) {
        try {
            checkCurrentApplication(applicationReference);

            rule.setThen(
                    new RuleThen()
                            .awardBadge(URI.create(awardBadge))
                            .awardPoints(rule.getThen().getAwardPoints())
            );

            lastApiResponse = api.createRuleWithHttpInfo(rule);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the {string} pointScale of awardPoints of then rule payload to the \\/rules endpoint")
    public void theApplicationPOSTThePointScaleOfAwardPointsOfThenRulePayloadToTheRulesEndpoint(String applicationReference, String pointScale) {
        try {
            checkCurrentApplication(applicationReference);

            URI createdURI = URI.create(pointScale);

            rule.setThen(
                    new RuleThen()
                            .awardBadge(rule.getThen().getAwardBadge())
                            .awardPoints(new RuleThenAwardPoints()
                                    .pointScale(createdURI)
                                    .amount(rule.getThen().getAwardPoints().getAmount())
                            )
            );

            lastApiResponse = api.createRuleWithHttpInfo(rule);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} POST the {int} amount of awardPoints of then rule payload to the \\/rules endpoint")
    public void theApplicationPOSTTheAmountOfAwardPointsOfThenRulePayloadToTheRulesEndpoint(String applicationReference, int amount) {
        try {
            checkCurrentApplication(applicationReference);

            rule.setThen(
                    new RuleThen()
                            .awardBadge(rule.getThen().getAwardBadge())
                            .awardPoints(new RuleThenAwardPoints()
                                    .pointScale(rule.getThen().getAwardPoints().getPointScale())
                                    .amount(amount)
                            )
            );

            lastApiResponse = api.createRuleWithHttpInfo(rule);
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
            lastReceivedBadge = (BadgeResponse) lastApiResponse.getData();
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
            lastReceivedPointScale = (PointScaleResponse) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }

    }

    @When("The application {string} sends a GET to the rule URL in the location header")
    public void theApplicationSendsAGETToTheRuleURLInTheLocationHeader(String applicationReference) {
        Integer id = Integer
                .parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.getRuleWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedRule = (Rule) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    @When("The application sends a GET to the user URL in the location header")
    public void iSendAGETToTheUserURLInTheLocationHeader() {
        try {
            lastApiResponse = api.getUserWithHttpInfo(user.getId());
            processApiResponse(lastApiResponse);
            lastReceivedUser = (User) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} sends a GET to the /users endpoint")
    public void iSendAGETToTheUsersEndpoint(String applicationReference) {
        try {
            checkCurrentApplication(applicationReference);
            lastApiResponse = api.getUsersWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }


    /*
        ====  PATCH  ====
    */

    @When("The application {string} PATCH a badge with the id {int}")
    public void theApplicationPATCHABadgeWithTheId(String applicationReference, int id) {
        try {
            checkCurrentApplication(applicationReference);

            JsonPatchDocument patchDocument = new JsonPatchDocument()
                    .op(JsonPatchDocument.OpEnum.REPLACE)
                    .path("/name")
                    .value("test");

            lastApiResponse = api.patchBadgeWithHttpInfo(id, Arrays.asList(patchDocument));
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }

    }

    @When("The application {string} PATCH a badge named {string}, he want to change the attribute {string} with the " +
            "value {string}")
    public void theApplicationPATCHABadgeNamedHeWantToChangeTheAttributeWithTheValue(String applicationReference,
                                                                                     String badgeName,
                                                                                     String attribute, String value) {

        try {
            checkCurrentApplication(applicationReference);

            ApiResponse<List<BadgeResponse>> getBadgeResponse = api.getBadgesWithHttpInfo();
            List<BadgeResponse> badges = getBadgeResponse.getData();
            BadgeResponse target = null;
            for (BadgeResponse badge : badges) {
                if (badge.getName().equals(badgeName)) {
                    target = badge;
                    break;
                }
            }

            String path = target.getLinks().get(0).getSelf().getPath();

            String id = path.substring(path.lastIndexOf('/') + 1);

            JsonPatchDocument patchDocument = new JsonPatchDocument()
                    .op(JsonPatchDocument.OpEnum.REPLACE)
                    .path("/" + attribute)
                    .value(value);

            lastApiResponse = api.patchBadgeWithHttpInfo(Integer.parseInt(id), Arrays.asList(patchDocument));
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} PATCH a point scale with the id {int}")
    public void theApplicationPATCHAPointScaleWithTheId(String applicationReference, int id) {

        try {
            checkCurrentApplication(applicationReference);

            JsonPatchDocument patchDocument = new JsonPatchDocument()
                    .op(JsonPatchDocument.OpEnum.REPLACE)
                    .path("/name")
                    .value("test");

            lastApiResponse = api.patchPointScaleWithHttpInfo(id, Arrays.asList(patchDocument));
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("The application {string} PATCH a point scale named {string}, he want to change the attribute {string} with" +
            " the value {string}")
    public void theApplicationPATCHAPointScaleNamedHeWantToChangeTheAttributeWithTheValue(String applicationReference,
                                                                                          String pointScaleName,
                                                                                          String attribute,
                                                                                          String value) {

        try {
            checkCurrentApplication(applicationReference);

            ApiResponse<List<PointScaleResponse>> getPointScalesResponse = api.getPointScalesWithHttpInfo();
            List<PointScaleResponse> pointScales = getPointScalesResponse.getData();
            PointScaleResponse target = null;
            for (PointScaleResponse pointScale : pointScales) {
                if (pointScale.getName().equals(pointScaleName)) {
                    target = pointScale;
                    break;
                }
            }

            String path = target.getLinks().get(0).getSelf().getPath();

            String id = path.substring(path.lastIndexOf('/') + 1);

            JsonPatchDocument patchDocument = new JsonPatchDocument()
                    .op(JsonPatchDocument.OpEnum.REPLACE)
                    .path("/" + attribute)
                    .value(value);

            lastApiResponse = api.patchPointScaleWithHttpInfo(Integer.parseInt(id), Arrays.asList(patchDocument));
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
        assertEquals(pointScale.getName(), lastReceivedPointScale.getName());
        assertEquals(pointScale.getDescription(), lastReceivedPointScale.getDescription());
    }

    @And("The application receives a payload that is the same as the rule payload")
    public void theApplicationReceivesAPayloadThatIsTheSameAsTheRulePayload() {
        assertEquals(rule, lastReceivedRule);
    }

    @And("The application receives a payload with {int} point\\(s) and {int} badge\\(s)")
    public void iReceiveAPayloadWithPointsAndBadges(int sizePoints, int sizeBadges) {
        assert user.getId() != null;
        String lastReceivedUserString = lastReceivedUser.toString();
        Assert.assertTrue(lastReceivedUserString.contains(user.getId()));
        Assert.assertTrue(lastReceivedUserString.contains("points"));
        Assert.assertTrue(lastReceivedUserString.contains("badges"));
        assert lastReceivedUser.getBadgesAwards() != null;
        assertEquals(lastReceivedUser.getBadgesAwards().size(),sizeBadges);
        assert lastReceivedUser.getPointsAwards() != null;
        assertEquals(lastReceivedUser.getPointsAwards().size(),sizePoints);
    }

    @And("The application {string} GET to the /badges endpoint receive a list containing {int} badge\\(s)")
    public void theApplicationReceiveAListContainingBadges(String applicationReference, int size) {

        try {
            checkCurrentApplication(applicationReference);
            List<BadgeResponse> badges = api.getBadges();
            assertEquals(size, badges.size());
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("The application {string} GET to the /pointScales endpoint receive a list containing {int} pointScale\\(s)")
    public void theApplicationReceiveAListContainingPointScales(String applicationReference, int size) {

        try {
            checkCurrentApplication(applicationReference);
            List<PointScaleResponse> pointScales = api.getPointScales();
            assertEquals(pointScales.size(), size);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("The application {string} GET to the \\/rules endpoint receive a list containing {int} rule\\(s)")
    public void theApplicationGETToTheRulesEndpointReceiveAListContainingRuleS(String applicationReference, int size) {
        try {
            checkCurrentApplication(applicationReference);
            List<Rule> rules = api.getRules();
            assertEquals(rules.size(), size);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("The application receives a badge that was created today")
    public void theApplicationReceiveABadgeThatWasCreatedToday() {
        assertEquals(badge.getObtainedDate(), LocalDate.now());
    }

    @And("The application receives {int} users")
    public void theApplicationReceivesUsers(int size) {
        try {
            List<User> users = api.getUsers();
            assertEquals(users.size(), size);
        } catch (ApiException e) {
            processApiException(e);
        }
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

    private String generateRandomNewString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
