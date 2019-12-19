package fr.insee.innovation.marathontokubernetes.cli.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class BaseCommandLineRunner implements CommandLineRunner, ExitCodeGenerator {

    private final BaseCommand myCommand;

    private final CommandLine.IFactory factory; // auto-configured to inject PicocliSpringFactory

    private int exitCode;

    public BaseCommandLineRunner(BaseCommand myCommand, CommandLine.IFactory factory) {
        this.myCommand = myCommand;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(myCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}