package onelaunch.model;

import java.util.ArrayList;

public class Workspace {

    private String name;
    private ArrayList<LaunchItem> items;//inside it we store item objects eg- vscode,chrome,files,folders etc

public Workspace(String name) {//constructor
    this.name = name;
    this.items = new ArrayList<>();
}

public void addItem(LaunchItem item) {//adding app
    items.add(item);
}

public void removeItem(LaunchItem item) {//removing app
    items.remove(item);
}

public String getName() {
        return name;
    }

public ArrayList<LaunchItem> getItems() {
    if (items == null) {
        items = new ArrayList<>();
    }
        return items;
    }

@Override
public String toString() {

    String data = "";
    data += "Workspace: " + name + "\n";
    data += "Items:\n";
    for (LaunchItem item :items) {
        data += item + "\n";
    }
    return data;
}
}