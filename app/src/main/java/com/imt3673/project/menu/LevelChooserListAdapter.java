package com.imt3673.project.menu;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.level_chooser_item, parent, false);

            viewHolder = new ViewHolder();
            // Initialize ViewHolder
            viewHolder.levelName = convertView.findViewById(R.id.level_id_label);
            viewHolder.goldTime = convertView.findViewById(R.id.level_gold_time);
            viewHolder.silverTime = convertView.findViewById(R.id.level_silver_time);
            viewHolder.bronzeTime = convertView.findViewById(R.id.level_bronze_time);
            viewHolder.highScoreList = convertView.findViewById(R.id.level_score_list);
            viewHolder.startButton = convertView.findViewById(R.id.level_start_button);
            viewHolder.goldStar = convertView.findViewById(R.id.level_gold_star);
            viewHolder.silverStar = convertView.findViewById(R.id.level_silver_star);
            viewHolder.bronzeStar = convertView.findViewById(R.id.level_bronze_star);

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
        viewHolder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("level", mLevelsInfo.get(position).getLevelId());
                mContext.startActivity(intent);
            }
        });

        // Populate list with HighScores
        List<HighScore> dbScores = this.mDatabase.highScoreDao().getAllScoresFromLevelSorted(levelInfo.getLevelId());
        ArrayList<String> bestTimes = new ArrayList<>();

        // Display stars based on time
        if(!dbScores.isEmpty()){
            HighScore currentHighScore = dbScores.get(0);
            String currentScore = currentHighScore.getLevelTime().replace(":","");
            Log.i("ADAPTER", "current score = " + currentScore);

            if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getGoldTime().replace(":",""))){
                viewHolder.goldStar.setVisibility(View.VISIBLE);
                viewHolder.silverStar.setVisibility(View.VISIBLE);
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);
                Log.i("ADAPTER", "GOLD");

            }
            else if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getSilverTime().replace(":",""))){
                viewHolder.silverStar.setVisibility(View.VISIBLE);
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);
                Log.i("ADAPTER", "SILVER");
            }
            else if(Integer.parseInt(currentScore) < Integer.parseInt(levelInfo.getBronzeTime().replace(":",""))){
                viewHolder.bronzeStar.setVisibility(View.VISIBLE);
                Log.i("ADAPTER", "BRONSE");
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

            viewHolder.highScoreList.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,bestTimes));
        }
        
        return convertView;
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
