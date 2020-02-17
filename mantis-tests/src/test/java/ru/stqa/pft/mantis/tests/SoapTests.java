package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;
import static org.testng.Assert.assertEquals;

public class SoapTests extends TestBase {

  @Test
  public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
    System.out.print("\n\n***** Внутри метода testGetProjects() *****\n\n");
    final Set<Project> projects = app.soap().getProjects();
    System.out.println("Количество проектов: " + projects.size());
    System.out.println("Названия проектов:");
    for (Project project : projects) {
      System.out.println("  " + project.getName());
    }
  }

  @Test
  public void testCreateIssue() throws RemoteException, ServiceException, MalformedURLException {
    System.out.print("\n\n***** Внутри метода testCreateIssue() *****\n\n");
    final Set<Project> projects = app.soap().getProjects();
    final Issue issue = new Issue().withSummary("Test summary")
                                   .withDescription("Test description")
                                   .withProject(projects.iterator().next());
    final Issue createdIssue = app.soap().addIssue(issue);
    assertEquals(createdIssue.getSummary(), issue.getSummary());
    assertEquals(createdIssue.getDescription(), issue.getDescription());
  }
}
