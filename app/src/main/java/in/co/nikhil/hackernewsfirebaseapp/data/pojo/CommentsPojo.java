package in.co.nikhil.hackernewsfirebaseapp.data.pojo;

/**
 * Created by nik on 2/3/2018.
 */

public class CommentsPojo {

  private String mAuthor;
  private long mDateTime;
  private String mComment;

  public CommentsPojo() {
  }

  public String getAuthor() {
    return mAuthor;
  }

  public void setAuthor(String mAuthor) {
    this.mAuthor = mAuthor;
  }

  public long getDateTime() {
    return mDateTime;
  }

  public void setDateTime(long mDateTime) {
    this.mDateTime = mDateTime;
  }

  public String getComment() {
    return mComment;
  }

  public void setComment(String mComment) {
    this.mComment = mComment;
  }
}
