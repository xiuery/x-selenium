package xiuery.xselenium.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private RunnerJavaExample javaExample;

    @Autowired
    private RunnerPythonExample pythonExample;

    @Override
    public void run(String... args) {

        String className = "JavaExample";

        switch (className) {
            case "JavaExample":
                javaExample.execute();
                break;
            case "PythonExample":
                pythonExample.execute();
                break;
            default:
                System.out.println("Support: JavaExample, pythonExample");
        }
    }
}
