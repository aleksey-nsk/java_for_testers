package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Set;
import static org.testng.Assert.assertEquals;

public class RestAssuredTests {

  @BeforeClass
  public void auth() {
    System.out.print("\n\n***** Внутри метода auth() *****\n\n");
    System.out.println("Аутентификация");
    RestAssured.authentication = RestAssured.basic("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  @Test
  public void testCreateIssue() throws IOException {
    System.out.print("\n\n***** Внутри метода testCreateIssue() *****\n\n");

    Set<Issue> oldIssues = getIssues();
    System.out.println("Количество баг-репортов до создания нового: " + oldIssues.size());

    Issue oneIssue = new Issue().withSubject("Subject").withDescription("Description");
    int issueId = createIssue(oneIssue);

    Set<Issue> newIssues = getIssues();
    System.out.println("Количество баг-репортов после создания нового: " + newIssues.size());

    oldIssues.add(oneIssue.withId(issueId));
    assertEquals(newIssues, oldIssues);
  }

  private Set<Issue> getIssues() throws IOException {
    System.out.println("Вернуть множество баг-репортов");

    System.out.println("  используем библиотеку Rest Assured");
    String json = RestAssured.get("http://bugify.stqa.ru/api/issues.json?limit=500").asString();

    JsonElement parsed = new JsonParser().parse(json);
    System.out.println("  parsed: " + parsed);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    Set<Issue> set = new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
    System.out.println("  количество баг-репортов: " + set.size());
    return set;
  }

  private int createIssue(Issue oneIssue) throws IOException {
    System.out.println("Создать баг-репорт");

    System.out.println("  используем библиотеку Rest Assured");
    String json = RestAssured.given()
                             .parameter("subject", oneIssue.getSubject())
                             .parameter("description", oneIssue.getDescription())
                             .post("http://bugify.stqa.ru/api/issues.json")
                             .asString();

    JsonElement parsed = new JsonParser().parse(json);
    int createdIssueId = parsed.getAsJsonObject().get("issue_id").getAsInt();
    System.out.println("  идентификатор созданного баг-репорта: " + createdIssueId);
    return createdIssueId;
  }
}
