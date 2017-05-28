package run.quest.twang.questrun.character;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.animations.ViewHelper;

public class CharacterActivity extends AppCompatActivity {
    LonelyJogger sJogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        sJogger = LonelyJogger.getInstance();

        System.out.println(sJogger.getAverageSpeed());
        ((TextView)findViewById(R.id.character_name)).setText(sJogger.getName());
        ((TextView)findViewById(R.id.character_level)).setText(String.valueOf(sJogger.getLevel()));
        ((TextView)findViewById(R.id.character_speed)).setText(ViewHelper.showAverageSpeed(sJogger.getAverageSpeed(), true));
        ((TextView)findViewById(R.id.character_multiplier)).setText(String.valueOf(sJogger.getSpeedMultiplier()));
        ((TextView)findViewById(R.id.character_distance)).setText(ViewHelper.showDistance(sJogger.getTotalDistance(), true));
        ((TextView)findViewById(R.id.character_run_time)).setText(ViewHelper.showMillis(sJogger.getTotalRunTime(), true));
        ((TextView)findViewById(R.id.character_steps)).setText(String.valueOf(sJogger.getTotalStepsTaken()));

        findViewById(R.id.character_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
