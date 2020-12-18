package ch.heigvd.amt.api.util;

import jdk.jshell.spi.ExecutionControl;
import lombok.SneakyThrows;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import java.io.IOException;

/**
 * Spring boot doesn't convert JsonPatch
 * Need to implement ours
 */
public class JsonPatchHttpMessageConverter extends AbstractHttpMessageConverter {

    public JsonPatchHttpMessageConverter() {
        super(MediaType.valueOf("application/json-patch+json"));
    }

    @Override
    protected boolean supports(Class aClass) {
        return JsonPatch.class.isAssignableFrom(aClass);
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage)
            throws IOException{
        JsonReader reader = Json.createReader(httpInputMessage.getBody());

        return Json.createPatch(reader.readArray());
    }

    @SneakyThrows
    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage)
            throws IOException, HttpMessageNotWritableException {
        throw new Exception("The json patch writer is not implemented");
    }
}
