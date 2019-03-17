import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.Suite
import kotlin.test.assertTrue

@RunWith(Suite::class)
@Suite.SuiteClasses(BaseTests::class, PropEditTest::class)
class Test {

    @Before fun preconditions() {
        assertTrue(TEST_FILE.exists(), "test file does not exist")
    }
}