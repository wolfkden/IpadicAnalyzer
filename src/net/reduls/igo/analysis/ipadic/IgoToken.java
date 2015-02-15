package net.reduls.igo.analysis.ipadic;

/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 *
 * Date: 4/19/11
 * Time: 11:04 PM
 *
 * IgoToken - This Class represents an IgoToken to output from the Wrapped Lucene analyzer
 * Igo tokens are classified into word morphemes, also represented as another class
 * IgoToken is a component of the couchDB document class TweetBase
 *
 */
import net.reduls.igo.analysis.ipadic.Morpheme;
import net.reduls.igo.analysis.ipadic.Surface;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IgoToken implements Serializable {
    private String term;
    private String type;
    private Morpheme morpheme;

    {
        term = "";
    }

    public IgoToken() {
    }

    public IgoToken(String term, String type) {
        this.term = term;
        String[] list = type.split(",");
        this.type = 1 < list.length ? "<CJ>" : (1 == list.length ? list[0] : null);
        if(1 < list.length) {
            morpheme = new Morpheme();
            morpheme.setBase(new Surface(list[0], list[1]));
            if(2 < list.length)
            morpheme.setContext(new Surface(list[2], list[3]));
            if(4 < list.length)
            morpheme.setConjugate(new Surface(list[4], list[5]));
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Morpheme getMorpheme() {
        return morpheme;
    }

    public void setMorpheme(Morpheme morpheme) {
        this.morpheme = morpheme;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void generate(JsonGenerator jsonGenerator) throws IOException {
        generate("", jsonGenerator);
    }

    public void generate(String name, JsonGenerator jsonGenerator) throws IOException {
        if(jsonGenerator == null) return;
        if(name.isEmpty()) jsonGenerator.writeStartObject();
        else jsonGenerator.writeObjectFieldStart(name);
        jsonGenerator.writeStringField("term", this.getTerm());
        jsonGenerator.writeStringField("type", this.getType());
        morpheme.generate(jsonGenerator);
        jsonGenerator.writeEndObject();

    }
}
