package in.co.nikhil.hackernewsfirebaseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.data.pojo.CommentsPojo;

/**
 * Created by nik on 2/3/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

  private LayoutInflater mLayoutInflater;
  private ArrayList<CommentsPojo> arrayList = new ArrayList<>();

  public CommentsAdapter(Context context) {
    mLayoutInflater = LayoutInflater.from(context);
  }

  public void setArrayList(ArrayList<CommentsPojo> arrayList) {
    this.arrayList = arrayList;
    notifyDataSetChanged();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mLayoutInflater.inflate(R.layout.recycler_item_comment, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    CommentsPojo commentsPojo = arrayList.get(position);
    holder.mComment.setText(commentsPojo.getComment());

    long timeMillis = System.currentTimeMillis() / 1000 - commentsPojo.getDateTime();

    long seconds = timeMillis / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    long days = hours / 24;

    if (days == 0) {
      String hoursMinutes = hours % 24 + "H " + minutes % 60 + "M " + seconds % 60 + "S . " + commentsPojo.getAuthor();
      holder.mDateTime.setText(hoursMinutes);
    } else {
      String time = days + "D " + hours % 24 + "H " + minutes % 60 + "M " + seconds % 60 + "S . " + commentsPojo.getAuthor();
      holder.mDateTime.setText(time);
    }
  }

  @Override
  public int getItemCount() {
    return arrayList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_date_time)
    TextView mDateTime;
    @BindView(R.id.comment_text)
    TextView mComment;


    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }


}
