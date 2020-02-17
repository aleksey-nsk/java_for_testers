package ru.stqa.pft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;
import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

  private ApplicationManager app;

  public SoapHelper(ApplicationManager app) {
    this.app = app;
  }

  private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    System.out.println("  вспомогательный метод getMantisConnect() открывает соединение");
    final URL url = new URL(app.getProperty("web.apiUrl"));
    final MantisConnectPortType mantisConnect = new MantisConnectLocator().getMantisConnectPort(url);
    return mantisConnect;
  }

  public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
    System.out.println("Метод getProjects()");

    final MantisConnectPortType mantisConnect = getMantisConnect();

    System.out.println("  получим проекты к которым имеет доступ администратор");
    final ProjectData[] projectData = mantisConnect.mc_projects_get_user_accessible(
        app.getProperty("web.adminLogin"),
        app.getProperty("web.adminPassword")
    );

    System.out.println("  преобразуем полученные проекты в модельные объекты");
    final Set<Project> projects = Arrays.asList(projectData)
                                        .stream()
                                        .map(p -> new Project().withId(p.getId().intValue()).withName(p.getName()))
                                        .collect(Collectors.toSet());

    System.out.println("  возвращаем множество модельных объектов");
    return projects;
  }

  public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
    System.out.println("Метод addIssue() создаёт баг-репорт");

    final MantisConnectPortType mantisConnect = getMantisConnect();

    final String[] categories = mantisConnect.mc_project_get_categories(
        app.getProperty("web.adminLogin"),
        app.getProperty("web.adminPassword"),
        BigInteger.valueOf(issue.getProject().getId())
    );
    final String category = categories[0];
    System.out.println("  выбрали первую попавшуюся Категорию: " + category);

    System.out.println("  создаём объект типа IssueData");
    IssueData issueData = new IssueData();
    issueData.setSummary(issue.getSummary());
    issueData.setDescription(issue.getDescription());
    final ObjectRef objectRef = new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName());
    issueData.setProject(objectRef);
    issueData.setCategory(category);

    System.out.println("  создаём баг-репорт в Мантисе");
    final BigInteger createdIssueId = mantisConnect.mc_issue_add(
        app.getProperty("web.adminLogin"),
        app.getProperty("web.adminPassword"),
        issueData
    );

    System.out.println("  получаем из Мантиса созданный баг-репорт");
    final IssueData createdIssueData = mantisConnect.mc_issue_get(
        app.getProperty("web.adminLogin"),
        app.getProperty("web.adminPassword"),
        createdIssueId
    );

    System.out.println("  постороим нужный нам модельный объект");
    final Project project = new Project().withId(createdIssueData.getProject().getId().intValue())
                                         .withName(createdIssueData.getProject().getName());
    final Issue createdIssue = new Issue().withId(createdIssueData.getId().intValue())
                                          .withSummary(createdIssueData.getSummary())
                                          .withDescription(createdIssueData.getDescription())
                                          .withProject(project);

    System.out.println("  возвращаем модельный объект");
    return createdIssue;
  }
}
