package ch.heigvd.amt.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration for nullable JSON objects (such as imageUrl of a badge)
 */
@Configuration
public class JacksonConfiguration {

    @Autowired
    public void configureJackson(ObjectMapper mapper) {
        mapper.registerModule(new JsonNullableModule());
        mapper.registerModule(new JSR353Module());
    }

}