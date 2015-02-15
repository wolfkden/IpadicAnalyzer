package net.reduls.igo.analysis.ipadic.test;
/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 *
 * Date: 3/10/11
 * Time: 7:02 PM
 *
 * Simple testing comparison of a list of lucene analyzers
 */

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;


import net.reduls.igo.Tagger;
import net.reduls.igo.analysis.ipadic.IpadicAnalyzer;
import net.reduls.igo.analysis.ipadic.IpadicTokenizer;
import net.reduls.igo.analysis.ipadic.IpadicCompositeAnalyzer;

import java.io.*;
import java.util.*;

public class IpadicTest {
    private static String[] strings = { "@mahiemon いやいやwwwぼくじゃないっすよw", "どうやって恋愛していけば良いか分からない‥", "\"@larkmilds1884 イメージで小栗旬より佐藤隆太だろと思ってましたが大泉洋の方が合ってるかもしれませんね。どこまでやるのかなぁ。自分は映画化実際のところはちょっとなぁという気持ちがありますね。ガンツも映画化されてあれもちょっとなぁと思って結局見に行かなかったんで。", "@soratonbitonbi おいてめぇ。おねーちゃんちょっとお金ちょうだい。", "いつかテンプレ借りてホームページつくってみたいねー！ただし何を載せるかが問題だｗｗｗｗ", "菅直人首相にも在日韓国人からの政治献金が明らかになった", "The quick brown fox jumps over the lazy dog", "http://www.google.com @wolfkden Per-entry attributes apply only to the individual JAR file entry to which the manifest entry is associated with.  If the same attribute also appeared in the main section, then the value of the per-entry attribute overwrites the main attribute's value. For example, if JAR file a.jar has the following manifest content", "http://www.google.com 前原誠司前外相に続き、菅直人首相にも在日韓国人からの政治献金が明らかになった", "トヨタ自動車は、ハイブリッド車（ＨＶ）「プリウス」のワゴン型試作車「プリウス・スペースコンセプト」を国内で初めて公開した。"}; //{"道德經"};

    private static HashMap<String, Analyzer> analyzer_list = new HashMap<String, Analyzer>();
    private static Tagger tagger;

    static {
        try {
        tagger = new Tagger("src/ipadic");
        } catch (IOException e) { System.out.println(e.getMessage()); }

//        analyzer_list.put("Simple Analyzer", new SimpleAnalyzer());
        analyzer_list.put("Standard Analyzer", new StandardAnalyzer(Version.LUCENE_30));
        analyzer_list.put("Filter Test", new IpadicCompositeAnalyzer(tagger));

    }

    public static void main(String[] args) {
//        System.out.println("Yo Bro");

//        analyzer_list.put("Japanese Analyzer", new IpadicAnalyzer(tagger));
        try {
        for(String key_value : analyzer_list.keySet()) {
            analyze(strings[0], analyzer_list.get(key_value), key_value);
        }
        } catch (IOException e) { System.out.println(e.getMessage()); }
    }

    private static void analyze(String string, Analyzer analyzer, String analyzerName) throws IOException {

        StringBuffer buffer = new StringBuffer();

        TokenStream stream = analyzer.tokenStream("contents", new StringReader(string));
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute pos = stream.addAttribute(PositionIncrementAttribute.class);

        while (stream.incrementToken()) {
                buffer.append("[");
                buffer.append(term.term());
                buffer.append(", ");
                buffer.append(type.type());
                buffer.append(", ");
                buffer.append(pos.getPositionIncrement());
//            buffer.append(", ");
//            buffer.append(offset.startOffset());
//            buffer.append(", ");
//            buffer.append(offset.endOffset());
                buffer.append("] ");
        }

        String output = buffer.toString();

        System.out.println(analyzerName + ": " + output);


    }

}
