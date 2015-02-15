package net.reduls.igo.analysis.ipadic;

import net.reduls.igo.Tagger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 *
 * Date: 4/19/11
 * Time: 10:59 AM
 *
 * ReadTwitterFile Class - This class is Runnable as a Java thread and implements functionality to
 * read a succession of twitter gzip compressed files, analyze results with a Lucene Analyzer,
 * combine analysis results with a tweet and write results to a CouchDB instance
 *
 */
public class ReadTwitterFile implements Runnable {

    String tweetDirectory;
    String tweetFile;
    String tweetPath;
    ObjectMapper mapper;

    {
        mapper = new ObjectMapper(new JsonFactory());
    }

    public ReadTwitterFile(String tweetFile) {
        this.tweetPath = tweetFile;
        this.tweetDirectory = "";
    }

    public ReadTwitterFile(String tweetDirectory, String tweetFile) {
        this.tweetDirectory = tweetDirectory;
        this.tweetFile = leftTrimSlash(tweetFile);
        tweetPath = rightTrimSlash(tweetDirectory) + File.separator + this.tweetFile;
    }

    public void run() {
        System.out.printf("Read objects: %d", readTweetFile());
//        this.readTweetFile();
    }

    private String rightTrimSlash(String pathName) {
        if(pathName.isEmpty()) return pathName;

        if(pathName.endsWith(File.separator)) return pathName.substring(0, pathName.length()-1);

        return pathName;
    }

    private String leftTrimSlash(String pathName) {
        if(pathName.isEmpty()) return pathName;

        if(pathName.startsWith(File.separator)) return pathName.substring(1);

        return pathName;
    }

    public long readTweetFile() {
        GZIPInputStream gzipInputStream;
        BufferedReader bufferedReader;
        Analyzer analyzer = null;
        long count = 0;
        String input;
        JsonNode jsonNode = null;
        StringWriter jsonTweet = new StringWriter();
        JsonFactory jf = new JsonFactory();

        Date date = new Date();
        System.out.println("Read Tweet File");
        List<TweetBase> tweetBaseList = new ArrayList<TweetBase>(24);
        TweetBase base = null;
        try {
            analyzer = new IpadicCompositeAnalyzer(new Tagger("src/ipadic"));
        } catch (Exception e) { System.out.println(e); return -1; }
        WriteCouchDB writeCouchDB = new WriteCouchDB();

        String inputval = null;

        try {
            gzipInputStream = new GZIPInputStream(new FileInputStream(tweetPath));
            bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
            while ((input = bufferedReader.readLine()) != null) {
                count++;
 //               JsonNode jn = mapper.readTree(input);
 //               jns.add(jn);
 //               inputval = input;
                if(input.startsWith("{\"delete\"")) {
        //            System.out.println(input);
                } else {
                    base = mapper.readValue(input, TweetBase.class);

                if(base != null && base.getUser() != null && base.getUser().getLocation() != null
                        && base.getUser().getLocation().contains("apan")) {
//                    System.out.println(input);
//                    System.out.printf("Text: %s\n", base.getText());
                }
                    this.analyze(analyzer, base);
                    tweetBaseList.add(base);

                    if(0 < tweetBaseList.size() && tweetBaseList.size() % 2000 == 0) {
                        writeCouchDB.bulkUpdate(tweetBaseList);
                        tweetBaseList.clear();
//                        System.out.printf("Tweet list size: %d\n", tweetBaseList.size());
                    }
                }
            }

        }   catch (Exception e) {
            System.out.println("Cannot open file: " + tweetPath);
            System.out.println(e);
            System.out.println(base.getText());
            System.out.println(inputval);
            return count;
        }

        System.out.printf("TweetList Size: %d\n", tweetBaseList.size());
        writeCouchDB.bulkUpdate(tweetBaseList);
        Date endDate = new Date();
        System.out.printf("Date difference %d\n", endDate.getTime() - date.getTime());

        return count;

    }

    private void analyze(Analyzer analyzer, TweetBase tweetBase) throws IOException {

//      StringBuffer buffer = new StringBuffer();

        if(tweetBase.getText() == null || tweetBase.getText().isEmpty()) return;
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(tweetBase.getText()));
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute pos = stream.addAttribute(PositionIncrementAttribute.class);


        ArrayList<net.reduls.igo.analysis.ipadic.IgoToken> tokenArrayList = new ArrayList<net.reduls.igo.analysis.ipadic.IgoToken>(10);
        while (stream.incrementToken()) {
        tokenArrayList.add(new net.reduls.igo.analysis.ipadic.IgoToken(term.term(), type.type()));
//            tweetBase.addIgoToken(new IgoToken(term.term(), type.type()));
//                buffer.append(term.term());
//                buffer.append(type.type());
//                buffer.append(pos.getPositionIncrement());
//            buffer.append(", ");
//            buffer.append(offset.startOffset());
//            buffer.append(", ");
//            buffer.append(offset.endOffset());
//                buffer.append("] ");
        }
        tweetBase.setToken(tokenArrayList);
//        String output = buffer.toString();

//        System.out.println(analyzerName + ": " + output);


    }

}
