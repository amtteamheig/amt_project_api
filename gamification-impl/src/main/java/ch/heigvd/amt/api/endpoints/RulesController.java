package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.RulesApi;
import ch.heigvd.amt.api.RulesApi;
import ch.heigvd.amt.api.model.RuleIf;
import ch.heigvd.amt.api.model.RuleThen;
import ch.heigvd.amt.api.model.RuleThenAwardPoints;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.RuleEntity;
import ch.heigvd.amt.api.model.Rule;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class RulesController implements RulesApi {

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    /**
     * Servlet entry point POST /rules
     * @param rule rule object built by the user
     * @return response
     */
    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createRule(@Valid Rule rule) {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        RuleEntity newRuleEntity = toRuleEntity(rule);
        newRuleEntity.setApiKeyEntity(apiKey);
        ruleRepository.save(newRuleEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newRuleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Serlvet entry point GET /rules
     * @return response
     */
    @Override
    public ResponseEntity<List<Rule>> getRules() {

        String apiKeyId = (String) servletRequest.getAttribute("Application");
        Optional<List<RuleEntity>> ruleEntities = ruleRepository.findByApiKeyEntityValue(apiKeyId);
        List<Rule> rules = new ArrayList<>();

        if (ruleEntities.isPresent()) {
            for (RuleEntity ruleEntity : ruleEntities.get()) {
                rules.add(toRule(ruleEntity));
            }
        }

        return ResponseEntity.ok(rules);
    }

    /**
     * Servlet entry point GET /rules/id
     * @param id rule's Id
     * @return response
     */
    @Override
    public ResponseEntity<Rule> getRule(Integer id) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        RuleEntity existingRuleEntity =
                ruleRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(toRule(existingRuleEntity));
    }

    /**
     * Converts a rule to a rule entity
     * @param rule : rule
     * @return rule entity
     */
    private RuleEntity toRuleEntity(Rule rule) {
        return RuleEntity.builder()
                ._if(RuleEntity.If.builder()
                        .type(rule.getIf().getType())
                        .build()
                )
                ._then(RuleEntity.Then.builder()
                        .awardBadge(rule.getThen().getAwardBadge().toString())
                        .awardPoints(RuleEntity.Then.AwardPoints.builder()
                                        .amount(rule.getThen().getAwardPoints().getAmount())
                                        .pointScale(rule.getThen().getAwardPoints().getPointScale().toString())
                                        .build()
                        )
                        .build()
                )
                .build();
    }

    /**
     * Converts a rule entity to a rule
     * @param entity : rule entity
     * @return rule
     */
    private Rule toRule(RuleEntity entity) {
        Rule rule = new Rule();
        RuleIf ruleIf = new RuleIf();
        RuleThen ruleThen = new RuleThen();
        RuleThenAwardPoints ruleThenAwardPoints = new RuleThenAwardPoints();
        ruleThenAwardPoints.setAmount(entity.get_then().getAwardPoints().getAmount());
        ruleThenAwardPoints.setPointScale(URI.create(entity.get_then().getAwardPoints().getPointScale()));
        ruleThen.setAwardPoints(ruleThenAwardPoints);
        ruleThen.setAwardBadge(URI.create(entity.get_then().getAwardBadge()));
        ruleIf.setType(entity.get_if().getType());
        rule.setIf(ruleIf);
        rule.setThen(ruleThen);
        return rule;
    }

}
