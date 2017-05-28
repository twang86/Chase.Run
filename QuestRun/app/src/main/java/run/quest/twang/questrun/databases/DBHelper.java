package run.quest.twang.questrun.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import run.quest.twang.questrun.monsters.Monster;
import run.quest.twang.questrun.monsters.MonsterTypes;
import run.quest.twang.questrun.quest.Quest;
import run.quest.twang.questrun.quest.QuestChase;
import run.quest.twang.questrun.quest.QuestDistance;
import run.quest.twang.questrun.quest.QuestDuration;

/**
 * Created by Tim on 5/22/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    //public Quest(long id, Context context, int layoutSource, long timeLimitInMillis, double winValue, int status, boolean ongoing) {
    public static abstract class QUESTS {

        public static final String TABLE_NAME = "quests",
                COLUMN_ID = "id",
                COLUMN_QUEST_TYPE = "quest_type",
                COLUMN_MONSTER = "quest_monster",
                COLUMN_MONSTER_POSITION = "quest_monster_position",
                COLUMN_TIME_LIMIT = "quest_time_limit",
                COLUMN_WIN_CONDITION = "quest_win_condition",
                COLUMN_BACKUP_WIN_CONDITION = "quest_win_backup",
                COLUMN_STATUS = "quest_status",
                COLUMN_ONGOING = "quest_ongoing";

        private static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_QUEST_TYPE + " INTEGER," +
                COLUMN_MONSTER + " TEXT," +
                COLUMN_MONSTER_POSITION + " REAL," +
                COLUMN_TIME_LIMIT + " INTEGER," +
                COLUMN_WIN_CONDITION + " REAL," +
                COLUMN_BACKUP_WIN_CONDITION + " REAL," +
                COLUMN_STATUS + " INTEGER," +
                COLUMN_ONGOING + " INTEGER" +
                ")";

        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }


    private static DBHelper sInstance;
    private Context mContext;

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "QUEST_DATA_RUN.db";

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUESTS.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(QUESTS.DELETE_TABLE);
    }

    public boolean isQuestTableEmpty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor qCursor = db.query(QUESTS.TABLE_NAME, null, null, null, null, null, null);
        boolean isEmpty = !qCursor.moveToFirst();
        qCursor.close();
        return isEmpty;
    }

    public void resetQuestTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(QUESTS.DELETE_TABLE);
        db.execSQL(QUESTS.CREATE_TABLE);
        db.close();
    }

    public void addQuestToDb(Quest quest){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getValuesFromQuest(quest);
        db.insert(QUESTS.TABLE_NAME, null, values);
        db.close();
    }

    public void updateQuestById(long id, Quest quest){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = getValuesFromQuest(quest);
        String selections = QUESTS.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        db.update(QUESTS.TABLE_NAME, values, selections, selectionArgs);
        db.close();
    }

    public void updateQuestIfExist(Quest quest){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = getValuesFromQuest(quest);
        String selections = QUESTS.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(quest.getId())};
        db.update(QUESTS.TABLE_NAME, values, selections, selectionArgs);
        db.close();
    }

    public void deleteQuestById(long id){
        SQLiteDatabase db = getWritableDatabase();
        String selections = QUESTS.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        db.delete(QUESTS.TABLE_NAME, selections, selectionArgs);
        db.close();
    }

    public List<Quest> getAllQuests(){
        List<Quest> resultQuestList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor qCursor = db.query(QUESTS.TABLE_NAME, null, null, null, null, null, null);
        if(qCursor.moveToFirst()){
            do{
                resultQuestList.add(makeQuestFromCursor(qCursor));
            }while(qCursor.moveToNext());
        }
        qCursor.close();
        return resultQuestList;
    }

    public Quest getQuestById(long id){
        Quest resultQuest= null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor qCursor = db.query(QUESTS.TABLE_NAME,
                null,
                QUESTS.COLUMN_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);
        if(qCursor.moveToFirst()){
            resultQuest = makeQuestFromCursor(qCursor);
        }
        qCursor.close();
        return resultQuest;
    }

    private Quest makeQuestFromCursor(Cursor cursor){
        long qId = cursor.getLong(cursor.getColumnIndex(QUESTS.COLUMN_ID));
        int qType = cursor.getInt(cursor.getColumnIndex(QUESTS.COLUMN_QUEST_TYPE));
        String qMonsterType = cursor.getString(cursor.getColumnIndex(QUESTS.COLUMN_MONSTER));
        double qMonsterPosition = cursor.getDouble(cursor.getColumnIndex(QUESTS.COLUMN_MONSTER_POSITION));
        long qTimeLimit = cursor.getLong(cursor.getColumnIndex(QUESTS.COLUMN_TIME_LIMIT));
        double qWinCondition = cursor.getDouble(cursor.getColumnIndex(QUESTS.COLUMN_WIN_CONDITION));
        double qWinConditionBackup = cursor.getDouble(cursor.getColumnIndex(QUESTS.COLUMN_BACKUP_WIN_CONDITION));
        int qStatus = cursor.getInt(cursor.getColumnIndex(QUESTS.COLUMN_STATUS));
        int qOnGoing = cursor.getInt(cursor.getColumnIndex(QUESTS.COLUMN_ONGOING));
        Quest resultQuest = null;

        switch (qType){
            case Quest.TYPE_CHASE:
                MonsterTypes myType = MonsterTypes.valueOf(qMonsterType);
                resultQuest = new QuestChase(qId, new Monster(myType, qMonsterPosition), qTimeLimit, qWinCondition, qStatus, qOnGoing);
                break;
            case Quest.TYPE_DISTANCE:
                resultQuest = new QuestDistance(qId, qTimeLimit, qWinCondition, qStatus, qOnGoing);
                break;
            case Quest.TYPE_DURATION:
                resultQuest = new QuestDuration(qId, qWinCondition, qStatus, qOnGoing);
                break;
        }

        if(resultQuest!=null){
            resultQuest.setBackupWinValue(qWinConditionBackup);
        }

        return resultQuest;
    }

    private ContentValues getValuesFromQuest(Quest quest){
        ContentValues values = new ContentValues();

        long qId = quest.getId();
        values.put(QUESTS.COLUMN_ID, qId);
        int qType = quest.getType();
        values.put(QUESTS.COLUMN_QUEST_TYPE, qType);
        String qMonsterType = MonsterTypes.ZOMBIE.toString();
        double qMonsterPosition = 0;
        if (quest instanceof QuestChase){
            qMonsterType = ((QuestChase) quest).getMonster().getType().toString();
            qMonsterPosition = ((QuestChase) quest).getMonster().getPosition();
        }
        values.put(QUESTS.COLUMN_MONSTER, qMonsterType);
        values.put(QUESTS.COLUMN_MONSTER_POSITION, qMonsterPosition);
        long qTimeLimit = quest.getTimeLimit();
        values.put(QUESTS.COLUMN_TIME_LIMIT, qTimeLimit);
        double qWinCondition = quest.getWinValue();
        values.put(QUESTS.COLUMN_WIN_CONDITION, qWinCondition);
        double qWinConditionBackup = quest.getBackupWinValue();
        values.put(QUESTS.COLUMN_BACKUP_WIN_CONDITION, qWinConditionBackup);
        int qStatus = quest.getStatus();
        values.put(QUESTS.COLUMN_STATUS, qStatus);
        int qOngoing = quest.getOngoing();
        values.put(QUESTS.COLUMN_ONGOING, qOngoing);

        return values;

    }
}
