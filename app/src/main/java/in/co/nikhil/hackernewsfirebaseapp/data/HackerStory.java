package in.co.nikhil.hackernewsfirebaseapp.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nkl07 on 02-02-2018.
 */

public class HackerStory extends RealmObject {

  @PrimaryKey
  private long mId;

  private String mArticleTitle;
  private String mSubmitter;
  private String mDatetime;
  private String mVotes;
  private String mComments;
  private String mScore;
  private String mUrl;
  private String mOmg;
  private String mCommentsJson;


  public long getId() {
    return mId;
  }


  public String getArticleTitle() {
    return mArticleTitle;
  }

  public void setArticleTitle(String mArticleTitle) {
    this.mArticleTitle = mArticleTitle;
  }

  public String getSubmitter() {
    return mSubmitter;
  }

  public void setSubmitter(String mSubmitter) {
    this.mSubmitter = mSubmitter;
  }

  public String getDatetime() {
    return mDatetime;
  }

  public void setDatetime(String mDatetime) {
    this.mDatetime = mDatetime;
  }

  public String getVotes() {
    return mVotes;
  }

  public void setVotes(String mVotes) {
    this.mVotes = mVotes;
  }

  public String getComments() {
    return mComments;
  }

  public void setComments(String mComments) {
    this.mComments = mComments;
  }

  public String getScore() {
    return mScore;
  }

  public void setScore(String mScore) {
    this.mScore = mScore;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String mUrl) {
    this.mUrl = mUrl;
  }

  public String getCommentsJson() {
    return mCommentsJson;
  }

  public void setCommentsJson(String mCommentsJson) {
    this.mCommentsJson = mCommentsJson;
  }
}
