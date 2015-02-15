package net.reduls.igo.analysis.ipadic;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 4/20/11
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Surface {
    private String base;
    private String context;

    public Surface() {
    }

    public Surface(String base, String context) {
        this.base = base;
        this.context = context;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void generate(JsonGenerator jsonGenerator) throws IOException {
        generate("surface", jsonGenerator);
    }

    public void generate(String name, JsonGenerator jsonGenerator) throws IOException {
        if(jsonGenerator == null) return;
        jsonGenerator.writeObjectFieldStart(name);
        if(getBase().compareTo("*") != 0 || getContext().compareTo("*") != 0) {
            jsonGenerator.writeStringField("base", this.getBase());
            jsonGenerator.writeStringField("context", this.getContext());
        }
        jsonGenerator.writeEndObject();

    }
}
