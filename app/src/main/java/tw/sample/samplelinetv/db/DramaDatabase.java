package tw.sample.samplelinetv.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = { Drama.class }, version = 1, exportSchema = false)
public abstract class DramaDatabase extends RoomDatabase {

    private static final String DB_NAME = "DramaDatabase.db";
    private static volatile DramaDatabase instance;

    public static synchronized DramaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static DramaDatabase create(final Context context) {
        return Room.databaseBuilder(context, DramaDatabase.class, DB_NAME).build();
    }

    public abstract DramaDao getDramaDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

}
