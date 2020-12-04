package ch.heigvd.amt.api.util;

import ch.heigvd.amt.api.model.JsonPatchDocument;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import java.util.ArrayList;
import java.util.List;

public class Patch {

    public static List<JsonPatch> toJsonPatch(List<JsonPatchDocument> jsonPatchDocuments){
        List<JsonPatch> jsonPatches = new ArrayList<>();


        for (JsonPatchDocument doc : jsonPatchDocuments) {
            JsonPatchBuilder builder = Json.createPatchBuilder();
            switch (doc.getOp()) {
                case REPLACE:
                    builder.replace(doc.getPath(), doc.getValue().toString());
                    break;
            }

            jsonPatches.add(builder.build());
        }

        return jsonPatches;
    }
}
