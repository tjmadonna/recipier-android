package com.inelasticcollision.recipelink.data.cache.database

import android.content.Context
import androidx.core.database.getStringOrNull
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2SQLiteDataHelper
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2SQLiteOpenHelper
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    companion object {
        private const val DB_NAME = "recipecollection.db"
    }

    @Rule
    @JvmField
    val dbTestHelper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    private val database: AppDatabase
        get() {
            val database = AppDatabase.getInstance(ApplicationProvider.getApplicationContext())
            // close the database and release any stream resources when the test finishes
            dbTestHelper.closeWhenFinished(database)
            return database
        }

    // Helper for creating SQLite database in version 2
    private lateinit var sqLiteOpenHelper: TestVersion2SQLiteOpenHelper

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Context>().deleteDatabase(DB_NAME)
        sqLiteOpenHelper =
            TestVersion2SQLiteOpenHelper(ApplicationProvider.getApplicationContext(), DB_NAME)
    }

    @After
    fun tearDown() {
        ApplicationProvider.getApplicationContext<Context>().deleteDatabase(DB_NAME)
    }

    @Test
    fun migrateVersion2To3ReturnsMigratesExistingData() {
        val content = TestVersion2SQLiteDataHelper.generateRandomContent(15)
        sqLiteOpenHelper.insertValues(content)

        val expectedValues =
            TestVersion2SQLiteDataHelper.mapContentValuesToExpectedRecipes(content)

        // Run test
        dbTestHelper.runMigrationsAndValidate(
            DB_NAME,
            3,
            true,
            AppDatabaseMigration.MIGRATION_2_3
        )

        val actualValues = database.query("SELECT * FROM recipes ORDER BY title", null)

        Assert.assertEquals(expectedValues.size, actualValues.count)
        for (i in expectedValues.indices) {
            actualValues.moveToPosition(i)

            val expectedValue = expectedValues[i]

            // Check id
            val actualId = actualValues.getString(0)
            val uuidRegex = Regex("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
            Assert.assertTrue(uuidRegex.matches(actualId))
            Assert.assertNotNull(actualId)

            // Check last modified
            Assert.assertEquals(expectedValue.lastModified.time, actualValues.getLong(1))

            // Check title
            Assert.assertEquals(expectedValue.title, actualValues.getString(2))

            // Check url
            Assert.assertEquals(expectedValue.url, actualValues.getString(3))

            // Check image url
            Assert.assertNotEquals(actualValues.getStringOrNull(4), "")
            Assert.assertEquals(expectedValue.imageUrl, actualValues.getStringOrNull(4))

            // Check favorite
            Assert.assertEquals(expectedValue.favorite, actualValues.getInt(5) > 0)

            // Check notes
            Assert.assertNotEquals(actualValues.getStringOrNull(6), "")
            Assert.assertEquals(expectedValue.notes, actualValues.getStringOrNull(6))

            // Check tags
            Assert.assertNotEquals(actualValues.getStringOrNull(7), "")
            Assert.assertEquals(expectedValue.tags, actualValues.getStringOrNull(7)?.split("|"))
        }
    }
}