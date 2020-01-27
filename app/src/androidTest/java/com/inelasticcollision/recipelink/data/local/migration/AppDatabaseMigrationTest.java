package com.inelasticcollision.recipelink.data.local.migration;

import android.database.Cursor;

import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.inelasticcollision.recipelink.data.local.db.AppDatabase;
import com.inelasticcollision.recipelink.data.local.migration.helper.TestVersion2Recipe;
import com.inelasticcollision.recipelink.data.local.migration.helper.TestVersion2SQLiteDataHelper;
import com.inelasticcollision.recipelink.data.local.migration.helper.TestVersion2SQLiteOpenHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public final class AppDatabaseMigrationTest {

    private static final String TEST_DB_NAME = "test-db";
    @Rule
    public final MigrationTestHelper dbTestHelper = new MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory()
    );
    // Helper for creating SQLite database in version 2
    private TestVersion2SQLiteOpenHelper sqLiteOpenHelper;

    @Before
    public void setup() {
        ApplicationProvider.getApplicationContext().deleteDatabase(TEST_DB_NAME);
        sqLiteOpenHelper = new TestVersion2SQLiteOpenHelper(ApplicationProvider.getApplicationContext(), TEST_DB_NAME);
    }

    @After
    public void tearDown() {
        ApplicationProvider.getApplicationContext().deleteDatabase(TEST_DB_NAME);
    }

    @Test
    public void migrateVersion2To3ReturnsMigratesExistingData() throws IOException {
        List<TestVersion2Recipe> expectedValues = TestVersion2SQLiteDataHelper.generateRandomContentSortedByTitle(10);
        sqLiteOpenHelper.insertValues(expectedValues);

        dbTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, AppDatabaseMigration.MIGRATION_2_3);

        AppDatabase db = getMigratedRoomDatabase();
        Cursor actualValues = db.query("SELECT * FROM recipes ORDER BY title", null);

        assertEquals(expectedValues.size(), actualValues.getCount());

        for (int i = 0; i < expectedValues.size(); i++) {
            actualValues.moveToPosition(i);
            TestVersion2Recipe expectedValue = expectedValues.get(i);

            assertNotNull(UUID.fromString(actualValues.getString(0)));
            assertEquals(expectedValue.getLastModified(), actualValues.getLong(1));
            assertEquals(expectedValue.getTitle(), actualValues.getString(2));
            assertEquals(expectedValue.getUrl(), actualValues.getString(3));
            assertEquals(expectedValue.getImageUrl(), actualValues.getString(4));
            assertEquals(expectedValue.isFavorite(), actualValues.getInt(5));
            assertEquals(expectedValue.getNotes(), actualValues.getString(6));
            assertEquals(expectedValue.getTags(), actualValues.getString(7));
        }
    }

    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class, TEST_DB_NAME)
                .addMigrations(AppDatabaseMigration.MIGRATION_2_3)
                .build();
        // close the database and release any stream resources when the test finishes
        dbTestHelper.closeWhenFinished(database);
        return database;
    }

}
