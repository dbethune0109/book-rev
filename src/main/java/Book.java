import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Book {
  private String name;
  private boolean completed;
  private LocalDateTime createdAt;
  private int id;
  private int genreId;
  private List<Review> reviews;

  public Book(String name, int genreId) {
    this.name = name;
    completed = false;
    createdAt = LocalDateTime.now();
    this.genreId = genreId;
  }

  public String getName() {
    return name;
  }

  public boolean isCompleted() {
    return completed;
  }

  public LocalDateTime getCreatedAt() {
   return createdAt;
 }

 public static List<Book> all() {
    String sql = "SELECT id, name, genreid FROM books";
    try(Connection con = DB.sql2o.open()) {
    return con.createQuery(sql).executeAndFetch(Book.class);
  }
 }

  public int getId() {
    return id;
  }
  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "Select * FROM books where id=:id";
      Book book = con.createQuery(sql)
      .addParameter("id",id)
      .executeAndFetchFirst(Book.class);
    return book;
    }
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getName().equals(newBook.getName()) &&
             this.getId() == newBook.getId();
    }
  }

  public void delete() {
  try(Connection con = DB.sql2o.open()) {
  String sql = "DELETE FROM books WHERE id = :id;";
  con.createQuery(sql)
    .addParameter("id", id)
    .executeUpdate();
    }
  }

  public void save() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "INSERT INTO books(name, genreId) VALUES (:name, :genreId)";
     this.id = (int) con.createQuery(sql, true)
       .addParameter("name", this.name)
       .addParameter("genreId", this.genreId)
       .executeUpdate()
       .getKey();
   }
 }

   public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Review> getReviews() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "SELECT * FROM reviews where bookId =:id";
     return con.createQuery(sql)
       .addParameter("id", this.id)
       .executeAndFetch(Review.class);
   }
 }

 public static Book findBook(String str) {
   try(Connection con = DB.sql2o.open()) {
     str = "%" + str + "%";
     String sql = "Select * FROM books WHERE name LIKE :str";
     Book book = con.createQuery(sql)
     .addParameter("str",str).executeAndFetchFirst(Book.class);
   return book;
   }
 }

 public double getAverage(){
   double sum = 0.0;
   List<Review> total = this.getReviews();
     for(int i = 0; i < total.size(); i ++)
     {
       sum += total.get(i).getStars();
     }

   double roundOff = Math.round(sum/total.size() * 100.0)/ 100.0;
   return (roundOff);
 }

  public int getGenreId() {
    return genreId;
  }

}
