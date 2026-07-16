package onelaunch.model;

import java.util.ArrayList;

public class Workspace {

    private String name;
    private ArrayList<Application> applications;//inside it we store application objects eg- vscode,chrome,etc

public Workspace(String name) {//constructor
    this.name = name;
    this.applications = new ArrayList<>();
}

public void addApplication(Application application) {//adding app
    applications.add(application);
}

public void removeApplication(Application application) {//removing app
    applications.remove(application);
}

public String getName() {
        return name;
    }

public ArrayList<Application> getApplications() {
        return applications;
    }

@Override
public String toString() {

    String data = "";
    data += "Workspace: " + name + "\n";
    data += "Applications:\n";
    for (Application app : applications) {
        data += app + "\n";
    }
    return data;
}
}