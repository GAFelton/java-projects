import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


public class RepositoryTest<Commit> {
    private Repository repo1;
    private Repository repo2;

    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    @Test
    @DisplayName("Test Repository Creation")
    public void testRepository() {

        // Check that exception is thrown for empty repository names
        assertThrows(IllegalArgumentException.class, () -> {
            new Repository("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Repository(null);
        });

        // Check that at least one class method can be called on a Repository,
        // which tests that has been initialized correctly.
        assertEquals(0, repo1.getRepoSize());
    }

    @Test
    @DisplayName("Test getHistory()")
    public void getHistory() {
        // Initialize commit messages
        String[] commitMessages = new String[]{"Initial commit.", "Updated method documentation.",
                                                "Removed unnecessary object creation."};

        // Commit the commit messages to repo1
        for (int i = 0; i < commitMessages.length; i++) {
            String commitMessage = commitMessages[i];
            repo1.commit(commitMessage);

            // Assert that the current commit id is at the repository's head
            // We know our ids increment from 0, meaning we can just use i as our id
            assertEquals("" + i, repo1.getRepoHead());
        }

        assertEquals(repo1.getRepoSize(), commitMessages.length);

        // Check IllegalArgumentException for non-positive integer input
        assertThrows(IllegalArgumentException.class, () -> {
            repo1.getHistory(-1);
        });

        // This is the method we are testing for. First, we'll obtain the 2 most recent commits
        // that have been made to repo1.
        String repositoryHistory = repo1.getHistory(2);
        String[] commits = repositoryHistory.split("\n");

        // Verify that getHistory() only returned 2 commits.
        assertEquals(commits.length, 2);

        // Verify that the 2 commits have the correct commit message and commit id
        for (int i = 0; i < commits.length; i++) {
            String commit = commits[i];

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left to ensure that 
            // getHistory() returned the 2 most recent commits.
            int backwardsIndex = (commitMessages.length - 1) - i;
            String commitMessage = commitMessages[backwardsIndex];

            assertTrue(commit.contains(commitMessage));
            assertTrue(commit.contains("" + backwardsIndex));
        }
    }

    @Test
    @DisplayName("Test drop() (empty case)")
    public void testDropEmpty() {
        assertFalse(repo1.drop("123"));
    }

    @Test
    @DisplayName("Test drop() (front case)")
    public void testDropFront() {
        assertEquals(repo1.getRepoSize(), 0);
        // Initialize commit messages
        String[] commitMessages = new String[]{"First commit.", "Added unit tests."};

        // Commit to repo1 - ID = "0"
        repo1.commit(commitMessages[0]);

        // Commit to repo2 - ID = "1"
        repo2.commit(commitMessages[1]);

        // Assert that repo1 successfully dropped "0"
        assertTrue(repo1.drop("0"));
        assertEquals(repo1.getRepoSize(), 0);
        
        // Assert that repo2 does not drop "0" but drops "1"
        // (Note that the commit ID increments regardless of the repository!)
        assertFalse(repo2.drop("0"));
        assertTrue(repo2.drop("1"));
        assertEquals(repo2.getRepoSize(), 0);
    }

    @Test
    @DisplayName("Test getRepoHead()")
    public void testGetRepoHead() {
        String[] commitMessages = new String[]{"Initial commit.", "Linting."};

        // Commit to repo1 - ID = "0"
        repo1.commit(commitMessages[0]);
        assertEquals("0", repo1.getRepoHead());

        // Commit to repo1 - ID = "1"
        repo1.commit(commitMessages[1]);
        assertEquals("1", repo1.getRepoHead());
    }

    @Test
    @DisplayName("Test getRepoSize()")
    public void testGetRepoSize() {
        String[] commitMessages = new String[]{"Initial commit.", "Linting.", "Updated README.md"};

        // Should be empty before adding any commits.
        assertEquals(0, repo1.getRepoSize());

        // Commit to repo1, check size
        repo1.commit(commitMessages[0]);
        assertEquals(1, repo1.getRepoSize());

        // Commit to repo1 twice more, check size
        repo1.commit(commitMessages[1]);
        repo1.commit(commitMessages[2]);
        assertEquals(3, repo1.getRepoSize());

    }

    @Test
    @DisplayName("Test toString()")
    public void testToString() {
        // Check empty repo toString()
        assertEquals("repo1 - No commits", repo1.toString());
        repo1.commit("Initial Commit");
        // Without mocking the timestamp, we will check for the expected
        // pre-/post- timestamp portions of a repo toString() when a commit is present.
        String expected = "repo1 - Current head: 0 at";
        String expectedEnd = ": Initial Commit";

        assertTrue(repo1.toString().contains(expected));
        assertTrue(repo1.toString().contains(expectedEnd));
    }

    @Test
    @DisplayName("Test contains()")
    public void testContains() {
        // Add commit to repo1
        repo1.commit("Initial commit.");  // ID: "0"
        assertTrue(repo1.contains("0"));
        assertFalse(repo1.contains("1"));
    }

    @Test
    @DisplayName("Test commit()")
    public void testCommit() {
        String[] commitMessages = new String[]{"Initial commit.", "Rafactor into Java.", "Write Unit Tests.", "Improve code comments."};

        repo1.commit(commitMessages[0]);   // ID: "0"
        String commit1Id = repo1.commit(commitMessages[1]);   // ID: "1"
        // Check that commit method returns String representation of ID
        assertEquals("1", commit1Id);
        repo2.commit(commitMessages[2]);   // ID: "2"
        repo1.commit(commitMessages[3]);   //  ID: "3"

        // Check that latest commit is current head of Repo
        assertEquals("3", repo1.getRepoHead());

        String repoHistory = repo1.getHistory(5);
        String[] commits = repoHistory.split("\n");

        // Verify that repo1 history is preserved
        assertEquals(commits.length, 3);

    }

    @Test
    @DisplayName("Test synchronize()")
    public void testSynchronize() throws InterruptedException {
        repo1.commit("c1");    // ID: "0"
        Thread.sleep(1);
        repo2.commit("c2");    // ID: "1"
        
        repo1.synchronize(repo2);
        assertTrue(repo1.contains("1"));
        assertNull(repo2.getRepoHead());
    }

    @Test
    @DisplayName("Test synchronize() with empty repo")
    public void testSynchronizeEmpty() throws InterruptedException {
        repo1.commit("c1");    // ID: "0"
        
        // Synchronize empty repo into repo containing commit
        repo1.synchronize(repo2);
        assertEquals(repo1.getRepoSize(), 1);
        assertNull(repo2.getRepoHead());

        // Synchronize repo containing commit into empty repo
        repo2.synchronize(repo1);
        assertNull(repo1.getRepoHead());
        assertEquals(repo2.getRepoSize(), 1);
    }

    @Test
    @DisplayName("Test synchronize() when this repo is larger")
    public void testSynchronizeSelfLarger() throws InterruptedException {
        repo1.commit("c1");    // ID: "0"
        Thread.sleep(1);
        repo2.commit("c2");    // ID: "1"
        Thread.sleep(1);
        repo1.commit("c3");    // ID: "2"
        
        repo1.synchronize(repo2);
        assertTrue(repo1.contains("1"));
        assertEquals(repo1.getRepoSize(), 3);
        assertNull(repo2.getRepoHead());
    }

    @Test
    @DisplayName("Test synchronize() when other repo is larger")
    public void testSynchronizeOtherLarger() throws InterruptedException {
        repo1.commit("c1");    // ID: "0"
        Thread.sleep(1);
        repo2.commit("c2");    // ID: "1"
        Thread.sleep(1);
        repo2.commit("c3");    // ID: "2"
        Thread.sleep(1);
        repo1.commit("c4");    // ID: "3"
        Thread.sleep(1);
        repo2.commit("c5");    // ID: "4"
        
        repo1.synchronize(repo2);
        assertTrue(repo1.contains("1"));
        assertEquals(repo1.getRepoSize(), 5);
        assertNull(repo2.getRepoHead());
    }
}
