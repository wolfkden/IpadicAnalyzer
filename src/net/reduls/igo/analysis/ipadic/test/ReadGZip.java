package net.reduls.igo.analysis.ipadic.test;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.*;

import net.reduls.igo.analysis.ipadic.ReadTwitterFile;
import net.reduls.igo.analysis.ipadic.TweetBase;
import net.reduls.igo.analysis.ipadic.WriteCouchDB;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 *
 * Date: 4/18/11
 * Time: 4:40 PM
 *
 * ReadGZip Class - an implementation class for reading GZip files containing Twitter tweet data and publishing
 * the tweet documents with tokenized text to a CouchDB instance
 */
public class ReadGZip {

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

/*        ReadTwitterFile readTwitterFile = new ReadTwitterFile(tweet_dir, tweet_files[0]);
        try {
            long read_count = readTwitterFile.readTweetFile();
            readTwitterFile = null;
            System.out.printf("Read objects: %d", read_count);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
*/
        Thread t = new Thread(new ReadTwitterFile(tweet_dir, tweet_files[0]));
        t.start();
        while (t.isAlive()) {
            threadMessage("Waiting...");
            t.join(1000);
        }

        for(String file:tweet_files) {

        }


    }

    public int getObjects(String pathName) {
        File fd = new File(pathName);
        if(fd.isDirectory()) {
            for(String fileName:fd.list())
            getObjects(pathName.concat(fileName));

            return fd.list().length;
        }

        if(!fd.isFile()) return 1;

        GZIPInputStream gzipInputStream;
        BufferedReader bufferedReader;
        long count = 0;
        String input;
        JsonFactory jf = new JsonFactory();

        JsonNode jn = null;
        TweetBase base = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            gzipInputStream = new GZIPInputStream(new FileInputStream(pathName));
            bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
            while ((input = bufferedReader.readLine()) != null) {
                count++;
                jn = mapper.readTree(input);

        base = mapper.readValue(input, TweetBase.class);
               if(base != null && base.getId_str() != null && base.getUser() != null && base.getUser().getLocation() != null)// && base.getUser().getLocation().contains("Japan"))
               System.out.printf("Tweet id: %s created at: %s location %s\n", base.getId_str(), base.getCreated_at(), base.getUser().getLocation());
//               System.out.printf("Tweet id: %s created at: %s text: %s", base.getId_str(), base.getCreated_at(), base.getText());
//                    System.out.println(input);
//                    System.out.println();

            }

        }   catch (Exception e) {
            System.out.println("Cannot open file: " + pathName);
            System.out.println(e.getMessage());
        }

//        processNode(jn);



        return 1;
    }

    public JsonNode processNode(JsonNode jsonNode) {

        //if(jp.nextToken() == JsonToken.START_OBJECT)
        //System.out.println("Value Field: " + JsonToken.START_OBJECT.asString());
        //System.out.println("Name  Field: " + jp.getCurrentName());
        //System.out.println("Value Field: " + jp.getText());
        if(jsonNode.isObject())
        for(Iterator<String> iter = jsonNode.getFieldNames(); iter.hasNext();) {
            String fieldName = iter.next().toString();
            System.out.printf("Field Name: %s", fieldName);
            JsonNode value = jsonNode.path(fieldName);
            System.out.printf(" Field value: %s", value.toString());
            System.out.println();
        }


        JsonNode text = jsonNode.path("text");
        System.out.println("Text Value: " + text.getTextValue());
        JsonNode user = jsonNode.path("user");
        if(user.isObject())
        for(Iterator<String> iter = user.getFieldNames(); iter.hasNext();) {
            String fieldName = iter.next().toString();
            System.out.printf("User Field Name: %s", fieldName);
            JsonNode value = user.path(fieldName);
            System.out.printf(" Field value: %s", value.toString());
            System.out.println();
        }



/*
                    while(jp.nextToken() != JsonToken.END_OBJECT) {
                        String nameField = jp.getCurrentName();
                        System.out.println("Name  Field: " + nameField);
         //               if(nameField.compareTo("text") == 0)
 //                       System.out.println("Value Field: " + jp.getText());
                        jp.nextToken();
                        nameField = jp.getCurrentName();
                        String valueField = jp.getCurrentName();
                       // if(nameField.compareTo("text") == 0)
                        System.out.println("Value Field: " + jp.getText());
                    }
*/

        return jsonNode;
    }


}
