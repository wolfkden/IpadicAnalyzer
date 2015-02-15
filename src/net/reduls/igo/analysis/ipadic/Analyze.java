package net.reduls.igo.analysis.ipadic;

import net.reduls.igo.Tagger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Development Environment: IntelliJ IDEA.
 * Developer: Wolfgang Kraske, PhD
 * Date: 4/26/11
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Analyze {
    Analyzer analyzer;
    Tagger tagger;
    List<IgoToken> tokens;

    {
        analyzer = null;
        tagger = null;
        tokens = null;
    }

    public Analyze() {
        try {
            tagger = new Tagger("src/ipadic");
        }   catch (Exception e) { System.out.println(e); }
        analyzer = new IpadicCompositeAnalyzer(tagger);
    }

    public Analyze(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public List<IgoToken> getTokens() {
        return tokens;
    }

    public List<IgoToken> getTokens(String text) {

        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute pos = stream.addAttribute(PositionIncrementAttribute.class);

        tokens = new ArrayList<IgoToken>(24);
        try {
            while (stream.incrementToken()) {
                tokens.add(new IgoToken(term.term(), type.type()));
            }
        } catch (Exception e) { System.out.println(e); }

        return tokens;
    }

    public void clearTokens() {
        if(tokens == null || tokens.size() == 0) return;

        tokens.clear();
    }
}
