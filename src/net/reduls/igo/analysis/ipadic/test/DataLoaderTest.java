package net.reduls.igo.analysis.ipadic.test;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.reduls.igo.analysis.ipadic.ReadTwitterFile;
import net.reduls.igo.analysis.ipadic.TweetBase;
import net.reduls.igo.analysis.ipadic.WriteCouchDB;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Development Environment: IntelliJ IDEA.
 * Developer: Wolfgang Kraske, PhD
 * Date: 5/4/11
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */

class Loader implements Callable<Properties> {
    private String name = null;

    public Loader(String name) {
        this.name = name;
    }

    public Properties call() {
        try{
            File file = new File(name);
         //   System.out.printf("Load File: %s\n", file.getAbsolutePath());
            Properties properties = new Properties();
            if(file.exists() && file.canRead() && file.isFile()) {
                GZIPInputStream fileInputStream = new GZIPInputStream(new FileInputStream(file));
                properties.load(fileInputStream);

                return properties;
            } else {
                System.err.printf("Please check about this file: %s\n", file.getAbsoluteFile());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

public class DataLoaderTest {
    private static String directoryName;
    private static ExecutorService executorService;

    static {
        directoryName = "/root/F7setup/Cluster/data";
        executorService = null;
    }

    public static void main(String...arguments) {
        try{
            File directory = new File(directoryName);
            if(directory.isDirectory()) {
                List<Callable<Properties>> fileList = new ArrayList<Callable<Properties>>();

                List<String> directoryFiles = Arrays.asList(directory.list()).subList(0,1000);

                executorService = Executors.newFixedThreadPool(20);

                System.out.printf("# of files: %d\n", directoryFiles.size());
                for(String file:directoryFiles) {
                    Callable<Properties> loader = new Loader(directory.getAbsolutePath() + File.separator + file);
                    fileList.add(loader);
                }

                List<Future<Properties>> results = executorService.invokeAll(fileList);

                int success = 0, failure = 0;
                for(Future<Properties> result : results) {
                    if(result.get() == null) {
                        failure++;
                    } else {
                        success++;
                    }
                }

                System.out.printf("Total # of files: %d\n", fileList.size());
                System.out.printf("Succeeded: %d Failed: %d\n", success, failure);

            } else {
                throw new IllegalArgumentException("No such directory name exists: " + directory.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(executorService != null) {
                executorService.shutdown();
            }
        }
    }
}
