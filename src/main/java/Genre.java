import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Genre {
  private String name;
  private int id;
  private List<Book> books;

  public Genre(String name) {
    this.name = name;
    books = new ArrayList<Book>();
  }

  public String getName(){
    return name;
  }

  public static List<Genre> all(){
    String sql = "SELECT id, name FROM genres";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Genre.class);
    }

  }
  @Override
  public boolean equals(Object otherGenre) {
    if (!(otherGenre instanceof Genre)) {
      return false;
    } else {
      Genre newGenre = (Genre) otherGenre;
      return this.getName().equals(newGenre.getName())&&
             this.getId() == newGenre.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO genres(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name).executeUpdate().getKey();
    }
  }

  public static Genre find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM genres where id=:id";
      Genre genre = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Genre.class);
      return genre;
    }
  }

  public int getId(){
    return id;
  }

  public List<Book> getBooks() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "SELECT * FROM books where genreId=:id";
     return con.createQuery(sql)
       .addParameter("id", this.id)
       .executeAndFetch(Book.class);
   }
 }

 public List<Book> sortBooks(List<Book> total) {
  List<Book> result = new ArrayList<Book>();
  int current = 0;
  for(int i = 0; i < total.size(); i ++)
  {
    current = i;
    for(int j = 0; j < total.size(); j ++)
    {
      if(total.get(current).getAverage() < total.get(j).getAverage())
      {
        current = j;
      }
    }
    result.add(total.get(current));
    total.remove(current);
    i -= 1;
  }
  return result;
}

}
