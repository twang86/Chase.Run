package run.quest.twang.questrun.monsterselector;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.animations.ViewHelper;
import run.quest.twang.questrun.monsters.MonsterTypes;

/**
 * Created by Tim on 5/23/2017.
 */

public class MonsterRecyclerAdapter extends RecyclerView.Adapter<MonsterRecyclerAdapter.MonsterViewHolder>{

    MonsterTypes[] mTypes;
    MonsterSelectorCallback mSelectorCallback;

    public interface MonsterSelectorCallback {

        void typeSelected(MonsterTypes type);
    }


    public MonsterRecyclerAdapter(MonsterTypes[] types, MonsterSelectorCallback selectorCallback) {
        mTypes = types;
        mSelectorCallback = selectorCallback;
    }

    @Override
    public MonsterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MonsterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_monster_selector, parent, false));
    }

    @Override
    public void onBindViewHolder(final MonsterViewHolder holder, int position) {
        final MonsterTypes currentType = mTypes[position];
        holder.mPic.setBackgroundResource(currentType.getAnimation());
        holder.mName.setText(currentType.getName());
        holder.mHealth.setText("Health: " + currentType.getHealth());
        holder.mStamina.setText("Stamina: " + ViewHelper.showMillis((long) currentType.getStamina(),  true));
        holder.mSpeed.setText("Speed: " + ViewHelper.showSpeed(currentType.getSpeed(), true));
        holder.mDescription.setText(currentType.getDescription());
        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectorCallback.typeSelected(currentType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTypes.length;
    }

    public class MonsterViewHolder extends RecyclerView.ViewHolder{
        public ImageView mPic;
        public TextView mName, mSpeed, mStamina, mHealth, mDescription;
        public View mRoot;


        public MonsterViewHolder(View itemView) {
            super(itemView);
            mRoot = itemView;
            mPic = (ImageView) itemView.findViewById(R.id.monster_selector_pic);
            mName = (TextView) itemView.findViewById(R.id.monster_selector_name);
            mSpeed = (TextView) itemView.findViewById(R.id.monster_selector_speed);
            mStamina = (TextView) itemView.findViewById(R.id.monster_selector_stamina);
            mHealth = (TextView) itemView.findViewById(R.id.monster_selector_health);
            mDescription= (TextView) itemView.findViewById(R.id.monster_selector_description);
        }
    }
}
