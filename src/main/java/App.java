import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("genres", Genre.all());
       model.put("template", "templates/index.vtl");
       return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());

    get("books/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/book-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("books", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      model.put("book", book);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/genres/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/genre-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/genres", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("genres", Genre.all());
      model.put("template", "templates/genres.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/genres/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre genre = Genre.find(Integer.parseInt(request.params(":id")));
      model.put("genre", genre);
      model.put("template", "templates/genre.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("genres/:id/books/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre genre = Genre.find(Integer.parseInt(request.params(":id")));
      model.put("genre", genre);
      model.put("template", "templates/genre-books-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/genres", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Genre newGenre = new Genre(name);
      newGenre.save();
      model.put("template", "templates/genre-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();


      Genre genre = Genre.find(Integer.parseInt(request.queryParams("genreId")));
      String author = request.queryParams("author");
      String name = request.queryParams("name");
      Book newBook = new Book(name, genre.getId(), author);
      newBook.save();

      model.put("genre", genre);
      model.put("template", "templates/genre-books-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/results", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("search");

      Book newbook = new Book("test", 5, "bob");
      newbook = newbook.findBook(name);
      Genre genre = Genre.find(newbook.getGenreId());
      model.put("book", newbook);
      model.put("genre", genre);
      model.put("template", "templates/results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/genres/:genre_Id/books/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre genre = Genre.find(Integer.parseInt(request.params(":genre_Id")));
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      List<Review> reviews = book.getReviews();
      model.put("genre", genre);
      model.put("reviews", reviews);
      model.put("book", book);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/genres/:genre_Id/books/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      String name = request.queryParams("name");
      String stars1 = request.queryParams("rating");
      int stars = Integer.parseInt(stars1);
      Genre genre = Genre.find(book.getGenreId());
      String url = String.format("/genres/%d/books/%d", genre.getId(), book.getId());
      Review review = new Review(name, stars , book.getId());
      review.save();
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/genres/:genre_Id/books/:id/delete", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();
    Book book = Book.find(Integer.parseInt(request.params("id")));
    Genre genre = Genre.find(book.getGenreId());
    book.delete();
    model.put("genre", genre);
    model.put("template", "templates/genre.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  }
}
