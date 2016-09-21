import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Review {

  private String info;
  private int id;
  private int stars;
  private int bookId;

  public Review(String info, int stars, int bookId)
  {
    this.info = info;
    this.stars = stars;
    this.bookId = bookId;
  }

  public String getInfo(){
    return info;
  }

  public int getStars(){
    return stars;
  }

  public static List<Review> all(){
    String sql = "SELECT id, info, stars FROM reviews";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Review.class);
    }

  }

  @Override
  public boolean equals(Object otherReview) {
    if (!(otherReview instanceof Review)) {
      return false;
    } else {
      Review newReview = (Review) otherReview;
      return this.getInfo().equals(newReview.getInfo())&&
             this.getId() == newReview.getId();
    }
  }

  public void save() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "INSERT INTO reviews(info, stars, bookId) VALUES (:info, :stars, :bookId)";
     this.id = (int) con.createQuery(sql, true)
       .addParameter("info", this.info)
       .addParameter("stars", this.stars)
       .addParameter("bookId", this.bookId)
       .executeUpdate()
       .getKey();
   }
 }

  public static Review find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews where id=:id";
      Review genre = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Review.class);
      return genre;
    }
  }

  public int getId(){
    return id;
  }

}
