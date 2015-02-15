package net.reduls.igo.analysis.ipadic;

/**
 * Created by Wolfgang Kraske, PhD
 * Igo Japanese Tokenizer Analysis Project
 *
 * Date: 4/19/11
 * Time: 8:56 PM
 *
 * TweetBase Class - An ektorp CouchDBDocument representation of a Twitter tweet including A list of Igo Tokens
 */
import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;

import java.util.List;

@JsonWriteNullProperties(false)
@JsonIgnoreProperties({"id", "revision", "place",
        "contributors", "coordinates", "retweeted_status", "in_reply_to_status_id",
        "source", "geo", "entities", "delete" })

public class TweetBase  extends CouchDbDocument {
    private String id;

    @JsonProperty("_id")
    private String id_str;
    private String created_at;
    private String text;
    private boolean retweeted;
    private String retweet_count;
    private String in_reply_to_screen_name;
    private String in_reply_to_user_id;
    private String in_reply_to_status_id_str;
    private String in_reply_to_user_id_str;
    private boolean favorited;
    private boolean truncated;
    @JsonProperty("token")
    private List<IgoToken> token;
    @JsonProperty("user")
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(String retweet_count) {
        this.retweet_count = retweet_count;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public String getIn_reply_to_user_id_str() {
        return in_reply_to_user_id_str;
    }

    public void setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    @JsonProperty("_rev")
    private String revision;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<IgoToken> getToken() {
        return token;
    }

    public void setToken(List<IgoToken> token) {
        this.token = token;
    }

/*
    @JsonIgnore
    public  void addIgoToken(IgoToken token) {
        this.token.add(token);
    }
*/
}
