package onelaunch.model;

public class LaunchItem {

    private String name;//encapsulation
    private String path;
    private ItemType type;

    public LaunchItem(String name, String path , ItemType type) {//constructor
    this.name = name;
    this.path = path;
    this.type = type;
}

    public String getName() {//getter method
        return name;
    }

    public String getPath() {
        return path;
    }

    public ItemType getType() {
    return type;
    }

    @Override
    public String toString() {
        return "Item: " + name + "\nPath: " + path;
    }
}