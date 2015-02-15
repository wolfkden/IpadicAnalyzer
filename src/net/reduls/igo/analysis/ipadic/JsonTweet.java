package net.reduls.igo.analysis.ipadic;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import sun.org.mozilla.javascript.internal.Token;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Wolfgang Kraske, PhD
 * Date: 4/25/11
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonTweet {
    private JsonFactory jsonFactory;
    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;
    private byte streamSeparator;
    private StringWriter stringWriter;
    private Map<String, Object> appendAfter;
    private Map<String, Object> appendBefore;

    {
        streamSeparator = '\n';
        jsonFactory = new JsonFactory();
        stringWriter = new StringWriter();
        jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
        appendAfter = appendBefore = null;
    }

    public JsonTweet() throws IOException {
        jsonParser = jsonFactory.createJsonParser("");

    }

    public JsonTweet(String input) throws IOException {
        jsonParser = jsonFactory.createJsonParser(input);

 //       this.writeObject("");

    //    jsonGenerator.close();
//        System.out.println("generated tweet");
     //   System.out.println(jsonGenerator.toString());
//        System.out.println(stringWriter.toString());



        return;
    }

    public String getTweet() {

        return stringWriter.toString();
    }

    public JsonNode getJsonNode() throws IOException {

        ObjectMapper mapper = new ObjectMapper(jsonFactory);


        return mapper.readValue(stringWriter.toString(), JsonNode.class);
    }

    public void readObject(String type, String offset) throws IOException {
        JsonToken jsonToken;
        if(jsonParser == null || !(jsonParser instanceof JsonParser)) return;

        if(!type.isEmpty()) System.out.println(offset.concat(type));

        String fieldType = "";
        while((jsonToken = jsonParser.nextToken()) != null) {
            switch(jsonToken) {
                case FIELD_NAME: fieldType="Field Name: ".concat(jsonParser.getCurrentName()); break;
                case VALUE_FALSE:
                case VALUE_TRUE:  fieldType="Boolean Type: ".concat(String.valueOf(jsonParser.getBooleanValue()));break;//    jsonParser.getBooleanValue();
                case VALUE_STRING: fieldType="String Type: ".concat(String.valueOf(jsonParser.getText()));break;//   jsonParser.getText();
                case VALUE_NUMBER_INT: fieldType="Integer Type: ".concat(String.valueOf(jsonParser.getLongValue()));break;//     jsonParser.getIntValue();
                case VALUE_NUMBER_FLOAT: fieldType="Float Type: ".concat(String.valueOf(jsonParser.getDoubleValue()));break;//   jsonParser.getFloatValue();
                case VALUE_EMBEDDED_OBJECT: fieldType="Embedded Object";break;//jsonParser.getEmbeddedObject();
                case VALUE_NULL:   fieldType="Null Type";break;//
                case START_ARRAY:  fieldType="] End Array";this.readObject("Start Array [", offset.concat("-->"));break;//
                case END_ARRAY:    return;//
                case START_OBJECT: fieldType="} End Object";this.readObject("Start Object {", offset.concat("-->"));break;//
                case END_OBJECT:   return;//
            }
            System.out.println(offset.concat(fieldType));
        }

            return;
    }

/*
    public JsonParser appendObject(Object object) {

        if(object == null) return null;

        System.out.println(object.getClass());

        return jsonFactory.createJsonParser();

    }
*/

    public void appendObjectAfter(String fieldName, Object object) {
        initAppendAfterMap();

        if(appendAfter.get(fieldName) != null) return;

        appendAfter.put(fieldName, object);
    }

    public void appendObjectBefore(String fieldName, Object object) {
        initAppendBeforeMap();

        if(appendBefore.get(fieldName) != null) return;

        appendBefore.put(fieldName, object);
    }

    private void initAppendBeforeMap() {
        appendBefore = new HashMap<String, Object>();
    }

    private void initAppendAfterMap() {
        appendAfter = new HashMap<String, Object>();
    }

    public void writeObject() throws IOException {
        this.writeObject("");
    }

    public void writeObject(String objectName) throws IOException {
        if(jsonParser == null || !(jsonParser instanceof JsonParser)) return;
        if(jsonGenerator == null || !(jsonGenerator instanceof JsonGenerator)) return;

        String fieldType = "", lastFieldName = "", fieldName = "", arrayName = "";
        JsonToken jsonToken;
        while((jsonToken = jsonParser.nextToken()) != null) {
            switch(jsonToken) {
                case FIELD_NAME:
                    lastFieldName = fieldName;
                    fieldType="Field Name: ".concat(fieldName=jsonParser.getCurrentName()); break;
                case VALUE_FALSE:
                case VALUE_TRUE:  jsonGenerator.writeBooleanField(fieldName, jsonParser.getBooleanValue());break;
                case VALUE_STRING: jsonGenerator.writeStringField(fieldName, jsonParser.getText());
                    appendObject();
                    break;
                case VALUE_NUMBER_INT: jsonGenerator.writeNumberField(fieldName, jsonParser.getLongValue());break;
                case VALUE_NUMBER_FLOAT: jsonGenerator.writeNumberField(fieldName, jsonParser.getDoubleValue());break;
                case VALUE_EMBEDDED_OBJECT: jsonGenerator.writeRaw(jsonParser.getEmbeddedObject().toString());break;
                case VALUE_NULL:   jsonGenerator.writeNullField(fieldName);break;
                case START_ARRAY:
                    jsonGenerator.writeArrayFieldStart(arrayName=fieldName);
                    this.writeObject("");break;
                case END_ARRAY:
                    jsonGenerator.writeEndArray();
                    return;
                case START_OBJECT:
                    if(fieldName.isEmpty()) jsonGenerator.writeStartObject();
                    else jsonGenerator.writeObjectFieldStart(fieldName);
                    this.writeObject(fieldName);break;
                case END_OBJECT:
                    jsonGenerator.writeEndObject();
                    if(objectName.isEmpty()) jsonGenerator.close();
                    return;
            }

            if(!fieldType.isEmpty()) System.out.println(fieldType);

        }

        return;
    }

    private void appendObject() throws IOException {

        Object object;
        if(appendAfter != null && (object = appendAfter.get(jsonParser.getCurrentName())) != null) {
            if(object instanceof Analyze)      {
                System.out.printf("Appending Text Analysis - field name: %s field value: %s\n", jsonParser.getCurrentName(), jsonParser.getText());
                TokenList tokenList = new TokenList();
                tokenList.setTokens(((Analyze)object).getTokens(jsonParser.getText()));
                tokenList.generate("token", jsonGenerator);
                List<IgoToken> tokens = tokenList.getTokens();
       //         tokens.get(0).generate("token", jsonGenerator);
            }
            else
            if(object instanceof TokenList)
                jsonGenerator.writeObjectField(((TokenList)object).getName(), ((TokenList)object).getTokens());
            else
                jsonGenerator.writeObjectField(object.getClass().toString(), object);
            appendAfter.remove(jsonParser.getCurrentName());
        }
    }
}
