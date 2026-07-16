package onelaunch.model;

public class Application {

    private String name;//encapsulation
    private String path;

    public Application(String name, String path) {//constructor
    this.name = name;
    this.path = path;
}

    public String getName() {//getter method
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Application: " + name + "\nPath: " + path;
    }
}