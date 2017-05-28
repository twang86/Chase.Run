package run.quest.twang.questrun.monsterselector;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.quest.QuestChase;

/**
 * Created by Tim on 5/24/2017.
 */

public class MonsterQuestRecyclerAdapter extends RecyclerView.Adapter<MonsterQuestRecyclerAdapter.MonsterQuestViewHolder> {

    List<QuestChase> mQuestList;

    public MonsterQuestRecyclerAdapter(List<QuestChase> questList){
        mQuestList = questList;
    }

    @Override
    public MonsterQuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MonsterQuestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_monster_quest, parent, false));
    }

    @Override
    public void onBindViewHolder(final MonsterQuestViewHolder holder, int position) {
        QuestChase current = mQuestList.get(position);
        holder.mPic.setImageResource(current.getMonster().getType().getAnimation());
        holder.mName.setText(current.getMonster().getName());
        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestList.remove(holder.getAdapterPosition());
                //notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mQuestList.size();
    }

    public class MonsterQuestViewHolder extends RecyclerView.ViewHolder{
        public ImageView mPic;
        public TextView mName;
        public View mRoot;

        public MonsterQuestViewHolder(View itemView) {
            super(itemView);
            mRoot = itemView;
            mPic = (ImageView) itemView.findViewById(R.id.monster_quest_pic);
            mName = (TextView) itemView.findViewById(R.id.monster_quest_name);
        }
    }
}
