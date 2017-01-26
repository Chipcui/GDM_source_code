package org.gobiiproject.gobiidao.filesystem.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by araquel on 1/26/2017.
 */
public class InstructionFileAccess<T> {

    public PayloadEnvelope<T> fromJson(JsonObject jsonObject,
                                       Class<T> dtoType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        PayloadEnvelope<T> returnVal = objectMapper.readValue(jsonObject.toString(), PayloadEnvelope.class);

        // The Jackson object mapper doesn't seem to have a means for knowing that the embedded list
        // is supposed to be cast to the DTO type. There's probably a more architectural way of doing
        // this -- e.g., a custom deserialization mechanism. But this gets the job done. Most importantly,
        // by properly casting this list of DTO objects, we prevent the Java client from caring too badly
        // about the envelope request semantics.
        JsonArray jsonArray = jsonObject.get("payload").getAsJsonObject().get("data").getAsJsonArray();
        String arrayAsString = jsonArray.toString();
        List<T> resultItemList = objectMapper.readValue(arrayAsString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, dtoType));

        returnVal.getPayload().setData(resultItemList);

        return returnVal;

    } // fromJson

    public InstructionFileAccess(T requestData) {
        this.header.setGobiiProcessType(gobiiProcessType);
        this.payload.getData().add(requestData);
    }

    private List<T> instructions = new ArrayList<>();

    public List<T> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<T> instructions) {
        this.instructions = instructions;
    }

}
