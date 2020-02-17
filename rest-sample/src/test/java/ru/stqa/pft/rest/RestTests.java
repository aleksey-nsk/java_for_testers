package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Set;
import static org.testng.Assert.assertEquals;

public class RestTests {

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
    String json = getExecutor().execute(Request.Get("http://bugify.stqa.ru/api/issues.json?limit=500"))
                               .returnContent()
                               .asString();
    JsonElement parsed = new JsonParser().parse(json);
    System.out.println("  parsed: " + parsed);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    Set<Issue> set = new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
    System.out.println("  количество баг-репортов: " + set.size());
    return set;
  }

  private int createIssue(Issue oneIssue) throws IOException {
    System.out.println("Создать баг-репорт");
    BasicNameValuePair pair1 = new BasicNameValuePair("subject", oneIssue.getSubject());
    BasicNameValuePair pair2 = new BasicNameValuePair("description", oneIssue.getDescription());
    String json = getExecutor().execute(Request.Post("http://bugify.stqa.ru/api/issues.json").bodyForm(pair1, pair2))
                               .returnContent()
                               .asString();
    JsonElement parsed = new JsonParser().parse(json);
    int createdIssueId = parsed.getAsJsonObject().get("issue_id").getAsInt();
    System.out.println("  идентификатор созданного баг-репорта: " + createdIssueId);
    return createdIssueId;
  }

  private Executor getExecutor() {
    System.out.println("  пройти авторизацию");
    return Executor.newInstance().auth("288f44776e7bec4bf44fdfeb1e646490", "");
  }
}
