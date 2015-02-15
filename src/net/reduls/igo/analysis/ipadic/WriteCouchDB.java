package net.reduls.igo.analysis.ipadic;


import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.http.HttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.util.Collection;


/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 * Date: 4/19/11
 * Time: 2:34 PM
 *
 * The WriteCouchDB class is used to write bulk tweets to a CouchDB instance using the ecktorp CouchDB library
 *
 */
public class WriteCouchDB {

    private CouchDbConnector couchDbConnector;

    public WriteCouchDB() {


        HttpClient httpClient = new StdHttpClient.Builder().host("192.168.0.182").port(5984).build();

        CouchDbInstance couchDbInstance = new StdCouchDbInstance(httpClient);
        couchDbConnector = new StdCouchDbConnector("analysis_data", couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

    }

    public void addToBulk() {

    }

    public void bulkUpdate(Collection<?> collection) {
        couchDbConnector.executeBulk(collection);
        couchDbConnector.flushBulkBuffer();
    }
}
