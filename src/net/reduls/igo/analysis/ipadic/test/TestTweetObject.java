package net.reduls.igo.analysis.ipadic.test;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.*;

import net.reduls.igo.Tagger;
import net.reduls.igo.analysis.ipadic.*;
import net.reduls.igo.analysis.ipadic.IgoToken;
import org.apache.lucene.analysis.Analyzer;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Development Environment: IntelliJ IDEA.
 * Developer: Wolfgang Kraske, PhD
 * Date: 4/25/11
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestTweetObject {
    String tweetDirectory;
    String tweetFile;
    String tweetPath;
    ObjectMapper mapper;

    {
        mapper = new ObjectMapper(new JsonFactory());
    }

    public TestTweetObject(String tweetFile) {
        this.tweetPath = tweetFile;
        this.tweetDirectory = "";
    }

    public TestTweetObject(String tweetDirectory, String tweetFile) {
        this.tweetDirectory = tweetDirectory;
        this.tweetFile = leftTrimSlash(tweetFile);
        tweetPath = rightTrimSlash(tweetDirectory) + File.separator + this.tweetFile;
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

    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    public static void main(String[] args) throws InterruptedException {
        String tweet_dir = "/root/F7setup/Cluster/data/";
        File fd = new File(tweet_dir);
        String[] tweet_files = fd.list();
        long count = 0;
        String input;
//        JSONReader jsonReader = new JSONReader();
        System.out.println("# of files: " + tweet_files.length);
//        ReadGZip instance = new ReadGZip();
//        instance.getObjects(tweet_dir + tweet_files[0]);
//        String file_name = tweet_files[0];

        TestTweetObject testTweetObject = new TestTweetObject(tweet_dir, tweet_files[0]);
        try {
            long read_count = testTweetObject.readTweetFile();
            testTweetObject = null;
            System.out.printf("Read objects: %d", read_count);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public long readTweetFile() {
         GZIPInputStream gzipInputStream;
         BufferedReader bufferedReader;
         long count = 0;
         String input = null;
         JsonNode jsonNode = null;
         JsonTweet jsonTweet = null;
         System.out.println("Read Tweet File");
         List<JsonNode> tweetBaseList = new ArrayList<JsonNode>(24);
         TweetBase base = null;
         WriteCouchDB writeCouchDB = new WriteCouchDB();

         try {
             gzipInputStream = new GZIPInputStream(new FileInputStream(tweetPath));
             bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
             while ((input = bufferedReader.readLine()) != null && count < 1) {
                 count++;
                 if(input.startsWith("{\"delete\"")) {
//                     System.out.println(input);
                 }
                 else {
                     jsonTweet = new JsonTweet(input);
                     jsonTweet.appendObjectAfter("text", new Analyze());
                     jsonTweet.writeObject();
                     tweetBaseList.add(jsonTweet.getJsonNode());
//                     JsonNode text = jsonTweet.getJsonNode().path("text");
//                     Analyze analyze = new Analyze();
//                     List<IgoToken> tokens = analyze.getTokens(text.getTextValue());
//                     TokenList tokenList = new TokenList(tokens);
//                     jsonTweet.appendObjectAfter("text", tokenList);

                     if(0 < tweetBaseList.size() && tweetBaseList.size() % 40 == 0) {
                         writeCouchDB.bulkUpdate(tweetBaseList);
//                         tweetBaseList.clear();
//                        System.out.printf("Tweet list size: %d\n", tweetBaseList.size());
                     }
                 }
             }

         }   catch (Exception e) {
             System.out.println("Cannot open file: " + tweetPath);
             System.out.println(e);
//             System.out.println(base.getText());
             System.out.println(input);
             return count;
         }

//         System.out.printf("TweetList Size: %d\n", tweetBaseList.size());
        writeCouchDB.bulkUpdate(tweetBaseList);

         return count;

     }
}
