import io.restassured.RestAssured;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;


public class FilmsTest {

    private final String BASE_URL = "https://swapi.dev/api/";

    @Test
    public void getMovieTitle() {
        get(BASE_URL + "films/1").then().statusCode(200).assertThat()
                .body("title", equalTo("A New Hope"));
    }


    @Test
    public void getCharacterName() {
        String name = "";
        List<String> allCharactersLinks =
                RestAssured
                        .given()
                        .baseUri(BASE_URL)
                        .basePath("films/1")
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("characters");

        for (String charLink : allCharactersLinks) {
            if (!name.equals("Biggs Darklighter")) {
                RestAssured.baseURI = charLink;
                name = RestAssured.given()
                        .when()
                        .get("")
                        .then().extract().path("name");
            }
            if (name.equals("Biggs Darklighter")) {
                System.out.println(name);
                System.out.println("Character Path: " + charLink);
                break;
            }
        }

    }

    @Test
    public void getCharacterStarship() {
        RestAssured.baseURI = BASE_URL + "films/1";
        List<String> allCharactersLinks =
                RestAssured
                        .given()
                        .baseUri(BASE_URL)
                        .basePath("films/1")
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("characters");

        List<String> starshipLinks =
                RestAssured
                        .given()
                        .baseUri(allCharactersLinks.get(8))
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("starships");

        String starshipName =
                RestAssured
                        .given()
                        .baseUri(starshipLinks.get(0))
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("name");

        System.out.println("Biggs Starship is: " + starshipName);
    }


    @Test
    public void getClassAndAvailablePilots() {
        RestAssured.baseURI = BASE_URL + "films/1";
        List<String> allCharactersLinks =
                RestAssured
                        .given()
                        .baseUri(BASE_URL)
                        .basePath("films/1")
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("characters");

        List<String> starshipLinks =
                RestAssured
                        .given()
                        .baseUri(allCharactersLinks.get(8))
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("starships");

        String starshipClass =
                RestAssured
                        .given()
                        .baseUri(starshipLinks.get(0))
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("starship_class");

        if (starshipClass.equals("Starfighter"))
            System.out.println("Starship class is " + starshipClass);
        else
            System.out.println("Starship class is not a Starfighter");

        List<String> availablePilots =
                RestAssured
                        .given()
                        .baseUri(starshipLinks.get(0))
                        .when()
                        .get()
                        .then()
                        .extract()
                        .path("pilots");

        for (String pilot : availablePilots) {
            String pilotName = RestAssured
                    .given()
                    .baseUri(pilot)
                    .when()
                    .get()
                    .then()
                    .extract()
                    .path("name");
            if (pilotName.equals("Luke Skywalker"))
                System.out.println("Luke is allowed pilot");
        }
    }
}
