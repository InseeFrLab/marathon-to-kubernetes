package fr.insee.innovation.marathontokubernetes.cli.components;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.innovation.marathontokubernetes.core.controller.ConvertController;
import org.springframework.stereotype.Controller;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * JavaCliComponent
 */
@Component
@Command(name = "convertToKub", mixinStandardHelpOptions = true, version = "checksum 4.0", description = "Prints the checksum (MD5 by default) of a file to STDOUT.")
public class BaseCommand implements Callable<Integer> {

    @Parameters(index="0", description = "Path to the file to convert")
    private String filePath;

    @Parameters(index = "1", description = "Path where to export converted file")
    private String outputPath="./" ;

    @Option(names = {"-n","--name"},description = "Name of the service")
    private String name;

    @Option(names={"-o","--outputFormat"},description="Format of output can be console or file")
    private String outputFormat="console";
    
    @Autowired
	ConvertController converter;
    
    @Override
    public Integer call() throws Exception {
        InputStream input = new FileInputStream(filePath);
        String converted = converter.convert(input, name);
        if (outputFormat.equals("console")){
            System.out.println(converted);
            return 0;
        }
        else if(outputFormat.equals("file")){
            FileOutputStream out = new FileOutputStream(outputPath+"service.yml");
            out.write(converted.getBytes());
            out.close();
            return 0;
        }
        else{
            return 1;
        }
    }

}