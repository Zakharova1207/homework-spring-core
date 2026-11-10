package nagikh.dev;

import nagikh.dev.config.AppConfig;
import nagikh.dev.console.ConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    static void main() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        ConsoleListener consoleListener = context.getBean(ConsoleListener.class);
        consoleListener.run();
        context.close();
    }
}
