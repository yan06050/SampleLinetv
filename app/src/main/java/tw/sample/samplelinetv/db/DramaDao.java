package tw.sample.samplelinetv.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DramaDao {
    @Query("SELECT * FROM DramaTable")
    List<Drama> getAllDramas();

    @Insert
    void insert(Drama... dramas);

    @Update
    void update(Drama... dramas);

    @Delete
    void delete(Drama... dramas);
}
