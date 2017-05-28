package run.quest.twang.questrun.jogging;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.animations.ViewHelper;

/**
 * Created by Tim on 5/20/2017.
 */

public class JogQuestRecyclerViewAdapter extends RecyclerView.Adapter<JogQuestRecyclerViewAdapter.QuestViewHolder> {
    //List<Quest> mQuests;
    List<View> mViews;

//    public JogQuestRecyclerViewAdapter(List<Quest> quests) {
//        mQuests = quests;
//    }
    public JogQuestRecyclerViewAdapter(List<View> views){
        mViews = views;
    }

    @Override
    public QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_active_quest, parent, false);
        return new QuestViewHolder(myView);
    }


    @Override
    public void onBindViewHolder(QuestViewHolder holder, int position) {
        //holder.mContainer.removeAllViews();
        //ViewGroup myParent = (ViewGroup) mQuests.get(position).getView().getParent();
        ViewGroup myParent = (ViewGroup) mViews.get(position).getParent();
        if (myParent!=null){myParent.removeAllViews();}
        //holder.mContainer.addView(mQuests.get(position).getView());
        holder.mContainer.addView(mViews.get(position));
        if (mViews.size()>1){
            holder.mContainer.getLayoutParams().width = ViewHelper.dpToPx(250, holder.mContainer.getContext());
        }
    }

    @Override
    public int getItemCount() {
        return mViews.size();
        //return mQuests.size();
    }

    public class QuestViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout mContainer;

        public QuestViewHolder(View itemView) {
            super(itemView);
            mContainer = (FrameLayout) itemView.findViewById(R.id.quest_holder);
        }
    }
}
