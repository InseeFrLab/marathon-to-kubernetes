package fr.insee.innovation.marathontokubernetes.cli.components;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.innovation.marathontokubernetes.core.controller.ConvertController;
import org.springframework.stereotype.Controller;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * JavaCliComponent
 */
@Component
@Command(name="", mixinStandardHelpOptions = true, description = "Converts a marathon.json file to Kubernetes .yml")
public class BaseCommand implements Callable<Integer> {

    @Parameters(index="0", description = "Path to the file to convert")
    private String inputFile;

    @Option(names = {"-n","--name"},description = "Name of the service")
    private String name;

    @Option(names={"-o","--output"},description="Location of the output file")
    private String outputPath;
    
    @Autowired
	ConvertController converter;
    
    @Override
    public Integer call() throws Exception {
        InputStream input = new FileInputStream(inputFile);
        String converted = converter.convert(input, name);
        if (isConsole()) {
            System.out.println(converted);
            return 0;
        }
        else {
            FileOutputStream out = new FileOutputStream(outputPath+"service.yml");
            out.write(converted.getBytes());
            out.close();
            return 0;
        }
    }

    private boolean isConsole() {
        return outputPath == null;
    }

}