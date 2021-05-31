package com.github.renegrob;

import io.quarkus.bootstrap.runner.QuarkusEntryPoint;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MainClass {

    public static void main(String... args) {
        if (args.length > 0) {
            if (args.length == 1 && "reaugment".equals(args[0])) {
                System.setProperty("quarkus.launch.rebuild", "true");
                try {
                    QuarkusEntryPoint.main();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    System.exit(20);
                }
                System.exit(0);
                return;
            }
        }
        Quarkus.run(args);
    }
}

