import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class GenreTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void genre_instantiatesCorrectly_true() {
    Genre testGenre = new Genre("Home");
    assertEquals(true, testGenre instanceof Genre);
  }
  @Test
 public void getName_genreInstantiatesWithName_Home() {
   Genre testGenre = new Genre("Home");
   assertEquals("Home", testGenre.getName());
 }

  @Test
  public void getBooks_initiallyReturnsEmptyList_ArrayList() {
    Genre testGenre = new Genre("Home");
    assertEquals(0, testGenre.getBooks().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Genre firstGenre = new Genre("Household chores");
    Genre secondGenre = new Genre("Household chores");
    assertTrue(firstGenre.equals(secondGenre));
  }
  @Test
  public void save_savesIntoDatabase_true() {
    Genre myGenre = new Genre("Household chores");
    myGenre.save();
    assertTrue(Genre.all().get(0).equals(myGenre));
  }
  @Test
  public void all_returnsAllInstancesOfGenre_true() {
    Genre firstGenre = new Genre("Home");
    firstGenre.save();
    Genre secondGenre = new Genre("Work");
    secondGenre.save();
    assertEquals(true, Genre.all().get(0).equals(firstGenre));
    assertEquals(true, Genre.all().get(1).equals(secondGenre));
  }
  @Test
  public void save_assignsIdToObject() {
    Genre myGenre = new Genre("Household chores");
    myGenre.save();
    Genre savedGenre = Genre.all().get(0);
    assertEquals(myGenre.getId(), savedGenre.getId());
  }
  @Test
  public void getId_categoriesInstantiateWithAnId_1() {
    Genre testGenre = new Genre("Home");
    testGenre.save();
    assertTrue(testGenre.getId() > 0);
  }

  @Test
   public void find_returnsGenreWithSameId_secondGenre() {
     Genre firstGenre = new Genre("Home");
     firstGenre.save();
     Genre secondGenre = new Genre("Work");
     secondGenre.save();
     assertEquals(Genre.find(secondGenre.getId()), secondGenre);
   }

   @Test
   public void getBooks_retrievesAllBooksFromDatabase_bookList() {
     Genre myGenre = new Genre("Household chores");
     myGenre.save();
     Book firstBook = new Book("Mow the lawn", myGenre.getId());
     firstBook.save();
     Book secondBook = new Book("Do the dishes", myGenre.getId());
     secondBook.save();
     Book[] books = new Book[] {firstBook, secondBook};
     assertTrue(myGenre.getBooks().containsAll(Arrays.asList(books)));
   }

}
