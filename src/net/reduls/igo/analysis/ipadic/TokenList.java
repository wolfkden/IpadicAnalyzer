package net.reduls.igo.analysis.ipadic;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Development Environment: IntelliJ IDEA.
 * Developer: Wolfgang Kraske, PhD
 * Date: 4/26/11
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokenList {

    private String name;
    private List<IgoToken> tokens;

    {
        name = "tokens";
        tokens = new ArrayList<IgoToken>();
    }

    public TokenList() {}

    public TokenList(List<IgoToken> tokens) {
        this.tokens = tokens;
    }

    public TokenList(String name, List<IgoToken> tokens) {
        this.name = name;
        this.tokens = tokens;
    }

    public void setToken(IgoToken token) {
        tokens.add(token);
    }

    public void setTokens(List<IgoToken> tokens) {
        this.tokens = tokens;
    }

    public List<IgoToken> getTokens() {
        return tokens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void generate(JsonGenerator jsonGenerator) throws IOException {
        generate("token", jsonGenerator);
    }

    public void generate(String name, JsonGenerator jsonGenerator) throws IOException {
        if(jsonGenerator == null) return;
        jsonGenerator.writeArrayFieldStart(name);
        int count = 0;
        for(IgoToken item:tokens) {

           item.generate(jsonGenerator);
        }
        jsonGenerator.writeEndArray();

    }

}
