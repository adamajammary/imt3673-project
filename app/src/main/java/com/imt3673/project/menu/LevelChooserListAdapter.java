package com.imt3673.project.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;
import com.imt3673.project.menu.LevelInfo;

import java.util.ArrayList;
import java.util.List;


public class LevelChooserListAdapter extends ArrayAdapter<LevelInfo>{

    private final Activity mContext;
    private ArrayList<LevelInfo> mLevelsInfo;

    public LevelChooserListAdapter(Activity context, ArrayList<LevelInfo> levelInfo) {
        super(context, 0,levelInfo);

        this.mContext = context;
        this.mLevelsInfo = levelInfo;

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

            viewHolder.levelName = convertView.findViewById(R.id.level_id_label);
            viewHolder.goldTime = convertView.findViewById(R.id.level_gold_label);
            viewHolder.silverTime = convertView.findViewById(R.id.level_silver_label);
            viewHolder.bronzeTime = convertView.findViewById(R.id.level_bronze_label);
            viewHolder.highScoreList = convertView.findViewById(R.id.level_score_list);
            viewHolder.startButton = convertView.findViewById(R.id.level_start_button);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.levelName.setText(levelInfo.getLevelHeader());
        String gold = mContext.getString(R.string.level_gold) + " : " + levelInfo.getGoldTime();
        viewHolder.goldTime.setText(gold);
        String silver = mContext.getString(R.string.level_silver) + " : " + levelInfo.getSilverTime();
        viewHolder.silverTime.setText(silver);
        String bronze = mContext.getString(R.string.level_bronsze) + " : " + levelInfo.getBronzeTime();
        viewHolder.bronzeTime.setText(bronze);
        viewHolder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("level", mLevelsInfo.get(position).getLevelId());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView levelName;
        TextView goldTime;
        TextView silverTime;
        TextView bronzeTime;
        ListView highScoreList;
        Button startButton;
    }
}
