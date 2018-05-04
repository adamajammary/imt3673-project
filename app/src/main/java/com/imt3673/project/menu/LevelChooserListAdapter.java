package com.imt3673.project.menu;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imt3673.project.database.AppDatabase;
import com.imt3673.project.database.HighScore;
import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom List adapter for level-chooser
 * Handles Level Items in the level-chooser list.
 */
public class LevelChooserListAdapter extends ArrayAdapter<LevelInfo>{

    private final Activity mContext;
    private ArrayList<LevelInfo> mLevelsInfo;
    private AppDatabase mDatabase;

    /**
     * Constructor
     * @param context context
     * @param levelInfo ArrayList with LevelInfo
     * @param db database instance
     */
    public LevelChooserListAdapter(Activity context, ArrayList<LevelInfo> levelInfo, AppDatabase db) {
        super(context, 0,levelInfo);

        this.mContext = context;
        this.mLevelsInfo = levelInfo;
        this.mDatabase = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        LevelInfo levelInfo = this.mLevelsInfo.get(position);

        if(convertView == null){

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.level_chooser_item, parent, false);

            viewHolder = new ViewHolder();
            initViewHolder(convertView, viewHolder);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Display level info
        viewHolder.levelName.setText(levelInfo.getLevelHeader());
        String gold = " : " + levelInfo.getGoldTime();
        viewHolder.goldTime.setText(gold);
        String silver = " : " + levelInfo.getSilverTime();
        viewHolder.silverTime.setText(silver);
        String bronze =  " : " + levelInfo.getBronzeTime();
        viewHolder.bronzeTime.setText(bronze);
        viewHolder.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("level", mLevelsInfo.get(position).getLevelId());
            intent.putExtra("gold_time",levelInfo.getGoldTime());
            intent.putExtra("silver_time",levelInfo.getSilverTime());
            intent.putExtra("bronze_time",levelInfo.getBronzeTime());
            mContext.startActivity(intent);
        });

        handleBestTimesAndStars(viewHolder, levelInfo);

        return convertView;
    }

    /**
     * Initialize ViewHolder for level_chooser_item
     * @param convertView view
     * @param viewHolder ViewHolder for level_chooser_item
     */
    private void initViewHolder(@Nullable View convertView, ViewHolder viewHolder) {
        if(convertView == null)
            throw new NullPointerException();
        viewHolder.levelName = convertView.findViewById(R.id.level_id_label);
        viewHolder.goldTime = convertView.findViewById(R.id.level_gold_time);
        viewHolder.silverTime = convertView.findViewById(R.id.level_silver_time);
        viewHolder.bronzeTime = convertView.findViewById(R.id.level_bronze_time);
        viewHolder.highScoreList = convertView.findViewById(R.id.level_score_list);
        viewHolder.startButton = convertView.findViewById(R.id.level_start_button);
        viewHolder.goldStar = convertView.findViewById(R.id.level_gold_star);
        viewHolder.silverStar = convertView.findViewById(R.id.level_silver_star);
        viewHolder.bronzeStar = convertView.findViewById(R.id.level_bronze_star);
    }


    /**
     * Populate highscore list and displays time based on Best time
     * @param viewHolder holds all the level_chooser_item views
     * @param levelInfo object containing level info
     */
    private void handleBestTimesAndStars(ViewHolder viewHolder, LevelInfo levelInfo) {
        // Populate list with HighScores
        List<HighScore> dbScores = this.mDatabase.highScoreDao().getTopScoresFromLevelSorted(levelInfo.getLevelId());
        ArrayList<String> bestTimes = new ArrayList<>();

        // Hide all stars initially to avoid level duplication due to caching
        viewHolder.goldStar.setVisibility(View.INVISIBLE);
        viewHolder.silverStar.setVisibility(View.INVISIBLE);
        viewHolder.bronzeStar.setVisibility(View.INVISIBLE);

        // Remove existing adapter to avoid level duplication due to caching
        viewHolder.highScoreList.setAdapter(null);

        // Display stars based on time
        if(!dbScores.isEmpty()){
            HighScore currentHighScore = dbScores.get(0);
            String currentScore = currentHighScore.getLevelTime().replace(":","");

            if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getGoldTime().replace(":",""))){
                viewHolder.goldStar.setVisibility(View.VISIBLE);
                viewHolder.silverStar.setVisibility(View.VISIBLE);
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);

            }
            else if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getSilverTime().replace(":",""))){
                viewHolder.silverStar.setVisibility(View.VISIBLE);
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);
            }
            else if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getBronzeTime().replace(":",""))){
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);
            }

            int i = 0;
            for(HighScore score : dbScores){
                String scorePlacement;
                scorePlacement = ++i + ".      " + score.getLevelTime();
                bestTimes.add(scorePlacement);
                if(i >= 3){
                    break;
                }
            }

            viewHolder.highScoreList.setAdapter(new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,bestTimes));
        }
    }

    /**
     * ViewHolder class
     * Holds all the views for one level item
     */
    static class ViewHolder {
        TextView levelName;
        TextView goldTime;
        TextView silverTime;
        TextView bronzeTime;
        ListView highScoreList;
        Button startButton;
        ImageView goldStar;
        ImageView silverStar;
        ImageView bronzeStar;

    }
}
