package xiuery.xselenium.runner;

import org.springframework.stereotype.Component;

@Component
public class RunnerPythonExample implements RunnerBase {

    public void execute() {
        System.out.println(this.getClass().getName());
    }
}
