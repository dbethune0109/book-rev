import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;


public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Book_instantiatesCorrectly_true() {
    Book myBook = new Book("Mow the lawn",1);
    assertEquals(true, myBook instanceof Book);
  }
  @Test
  public void isCompleted_isFalseAfterInstantiation_false() {
    Book myBook = new Book("Mow the lawn",1);
    assertEquals(false, myBook.isCompleted());
  }
  @Test
  public void Book_instantiatesWithName_String() {
    Book myBook = new Book("Mow the lawn",1);
    assertEquals("Mow the lawn", myBook.getName());
  }
  @Test
  public void getCreatedAt_instantiatesWithCurrentTime_today() {
    Book myBook = new Book("Mow the lawn",1);
    assertEquals(LocalDateTime.now().getDayOfWeek(), myBook.getCreatedAt().getDayOfWeek());
  }

  @Test
  public void clear_emptiesAllBooksFromArrayList_0() {
    Book myBook = new Book("Mow the lawn",1);
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void getId_booksInstantiateWithAnID() {
    Book myBook = new Book("Mow the lawn",1);
    myBook.save();
    assertTrue(myBook.getId() > 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Book firstBook = new Book("Mow the lawn",1);
    Book secondBook = new Book("Mow the lawn",1);
    assertTrue(firstBook.equals(secondBook));
  }

  @Test
  public void save_returnsTrueIfNamesAretheSame() {
    Book myBook = new Book("Mow the lawn",1);
    myBook.save();
    assertTrue(Book.all().get(0).equals(myBook));
  }

  @Test
  public void all_returnsAllInstancesOfBook_true() {
    Book firstBook = new Book("Mow the lawn",1);
    firstBook.save();
    Book secondBook = new Book("Buy groceries",1);
    secondBook.save();
    assertEquals(true, Book.all().get(0).equals(firstBook));
    assertEquals(true, Book.all().get(1).equals(secondBook));
  }

  @Test
  public void save_assignsIdToObject() {
    Book myBook = new Book("Mow the lawn",1);
    myBook.save();
    Book savedBook = Book.all().get(0);
    assertEquals(myBook.getId(), savedBook.getId());
  }

  @Test
  public void find_returnsBookWithSameId_secondBook() {
    Book firstBook = new Book("Mow the lawn",1);
    firstBook.save();
    Book secondBook = new Book("Buy groceries",1);
    secondBook.save();
    assertEquals(Book.find(secondBook.getId()), secondBook);
  }

  @Test
 public void save_savesGenreIdIntoDB_true() {
   Genre myGenre = new Genre("Household chores");
   myGenre.save();
   Book myBook = new Book("Mow the lawn", myGenre.getId());
   myBook.save();
   Book savedBook = Book.find(myBook.getId());
   assertEquals(savedBook.getGenreId(), myGenre.getId());
  }

    @Test
    public void update_updatesBookName_true(){
      Book myBook = new Book("Mow the lawn",1);
      myBook.save();
      myBook.update("Take a nap");
      assertEquals("Take a nap", Book.find(myBook.getId()).getName());
    }

    @Test
    public void delete_deletesBook_true() {
      Book myBook = new Book("Mow the lawn", 1);
      myBook.save();
      int myBookId = myBook.getId();
      myBook.delete();
      assertEquals(null, Book.find(myBookId));
    }

}
