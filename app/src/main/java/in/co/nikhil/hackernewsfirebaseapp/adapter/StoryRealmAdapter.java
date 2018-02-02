package in.co.nikhil.hackernewsfirebaseapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.data.HackerStory;
import in.co.nikhil.hackernewsfirebaseapp.data.remoteModel.Story;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by nik on 2/2/2018.
 */

public class StoryRealmAdapter extends RealmBasedRecyclerViewAdapter<HackerStory, StoryRealmAdapter.MyViewHolder> {
  public StoryRealmAdapter(Context context, RealmResults<HackerStory> realmResults, boolean automaticUpdate, boolean animateResults) {
    super(context, realmResults, automaticUpdate, animateResults);
  }

  @Override
  public MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
    View view = inflater.inflate(R.layout.recyler_item_hacker_story, viewGroup, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindRealmViewHolder(MyViewHolder viewHolder, int position) {
    final HackerStory story = realmResults.get(position);
    viewHolder.mScore.setText(story.getScore());
    viewHolder.mTitle.setText(story.getArticleTitle());
    viewHolder.mUrl.setText(story.getUrl());
    viewHolder.mComments.setText(story.getComments());
    String timeUser = story.getDatetime() + story.getSubmitter();
    viewHolder.mStoryUserTime.setText(timeUser);

  }

  public class MyViewHolder extends RealmViewHolder {

    @BindView(R.id.story_score)
    TextView mScore;
    @BindView(R.id.story_title)
    TextView mTitle;
    @BindView(R.id.story_url)
    TextView mUrl;
    @BindView(R.id.story_comments)
    TextView mComments;
    @BindView(R.id.story_time_user)
    TextView mStoryUserTime;


    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

    }


  }
}
