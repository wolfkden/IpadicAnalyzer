package net.reduls.igo.analysis.ipadic;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 4/20/11
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */

@JsonPropertyOrder({"base", "context", "conjugate"})
public class Morpheme {
    private Surface base;
    private Surface context;
    private Surface conjugate;

    public Surface getBase() {
        return base;
    }

    public void setBase(Surface base) {
        this.base = base;
    }

    public Surface getContext() {
        return context;
    }

    public void setContext(Surface context) {
        this.context = context;
    }

    public Surface getConjugate() {
        return conjugate;
    }

    public void setConjugate(Surface alternate) {
        this.conjugate = alternate;
    }

    public void generate(String name, JsonGenerator jsonGenerator) throws IOException {

        if(jsonGenerator == null) return;
        jsonGenerator.writeObjectFieldStart(name);
        base.generate("base", jsonGenerator);
        context.generate("context", jsonGenerator);
        conjugate.generate("conjugate", jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    public void generate(JsonGenerator jsonGenerator) throws IOException {
        generate("morpheme", jsonGenerator);
    }
}
