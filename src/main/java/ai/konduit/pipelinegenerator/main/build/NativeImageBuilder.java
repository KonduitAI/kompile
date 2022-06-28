package ai.konduit.pipelinegenerator.main.build;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.FileUtils;
import org.nd4j.common.base.Preconditions;
import org.nd4j.common.io.ClassPathResource;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "native-image-generate",mixinStandardHelpOptions = false,description = "Generate a native image using the given konduit serving pipeline steps available below. This will also generate a project to build the native image with. Note that the generate-image-and-sdk command also uses this command.")
public class NativeImageBuilder implements Callable<Void> {


    @CommandLine.Option(names = {"--python"},description = "Whether to use python or not")
    private boolean python = false;
    @CommandLine.Option(names = {"--onnx"},description = "Whether to use onnx or not")
    private boolean onnx = false;
    @CommandLine.Option(names = {"--tvm"},description = "Whether to use tvm or not")
    private boolean tvm = false;
    @CommandLine.Option(names = {"--dl4j"},description = "Whether to use dl4j or not")
    private boolean dl4j = false;
    @CommandLine.Option(names = "--samediff",description = "Whether to use samediff or not")
    private boolean samediff = false;
    @CommandLine.Option(names = "--nd4j",description = "Whether to use nd4j or not")
    private boolean nd4j = false;
    @CommandLine.Option(names = "--tensorflow",description = "Whether to use tensorflow or not")
    private boolean tensorflow = false;
    @CommandLine.Option(names = "--image",description = "Whether to use image pre processing or not or not")
    private boolean image = false;
    @CommandLine.Option(names = "--server",description = "Whether to use an http server or not")
    private boolean server = false;


    private Model model;
    @CommandLine.Option(names = {"--imageName"},description = "The image name")
    private String imageName = "konduit-serving";
    //What's the main class we want to run? A generic serving class we pre include?
    @CommandLine.Option(names = {"--mainClass"},description = "The main class for the image")
    private String mainClass;

    @CommandLine.Option(names = {"--extraDependencies"},description = "Extra dependencies to include in the form of: groupId:artifactId,version:classifier")
    private String extraDependencies;

    @CommandLine.Option(names = {"--mavenHome"},description = "The maven home.", required = true)
    private File mavenHome;

    @CommandLine.Option(names = {"--outputFile"},description = "The output file")
    private File outputFile = new File("pom2.xml");

    @CommandLine.Option(names = {"--numpySharedLibrary"},description = "Create a library with a numpy based entry point.")
    private boolean numpySharedLibrary;

    @CommandLine.Option(names = {"--javacppPlatform"},description = "Build for a specific specified platform. An example would be linux-x86_64 - this reduces binary size and prevents out of memories from trying to include binaries for too many platforms.")
    private String javacppPlatform;

    @CommandLine.Option(names = "--pipelinePath",description = "The pipeline path for building the image")
    private String pipelinePath;


    @CommandLine.Option(names = "--nativeImageFilesPath",description = "The path to the files for building an image")
    private String nativeImageFilesPath;

    public void runMain(String...args) throws Exception {
        InvocationRequest invocationRequest = new DefaultInvocationRequest();
        File project = new File(imageName);
        project.mkdirs();
        File srcDir = new File(project,"src/main/java");
        Preconditions.checkState(srcDir.mkdirs(),"Unable to make directory " + srcDir.getAbsolutePath());

        if (numpySharedLibrary) {
            File cEntryPointDir = new File(srcDir,"ai/konduit/pipelinegenerator/main");
            Preconditions.checkState(cEntryPointDir.mkdirs(),"Unable to make directory " + cEntryPointDir.getAbsolutePath());
            ClassPathResource classPathResource = new ClassPathResource("NumpyEntryPoint.java");
            FileUtils.copyFile(classPathResource.getFile(),new File(cEntryPointDir,"NumpyEntryPoint.java"));

        }

        File resourcesDir = new File(project,"src/main/resources");
        Preconditions.checkState(resourcesDir.mkdirs(),"Unable to make directories " + resourcesDir.getAbsolutePath());
        if (numpySharedLibrary) {
            ClassPathResource classPathResource2 = new ClassPathResource("numpy_struct.h");
            FileUtils.copyFile(classPathResource2.getFile(),new File(resourcesDir,"numpy_struct.h"));

        }

        FileUtils.copyFile(outputFile,new File(project,"pom.xml"));
        ClassPathResource nativeImage = new ClassPathResource("META-INF/native-image");
        Preconditions.checkState(nativeImage.exists(),"META-INF/native-image does not exist!");
        File nativeImageResourceDir = new File(resourcesDir,"META-INF/native-image");
        Preconditions.checkState(nativeImageResourceDir.mkdirs(),"Unable to create native image resources directory!");
        String[] resources = {"jni-config.json","proxy-config.json","reflect-config.json","resource-config.json","serialization-config.json"};
        if(nativeImageFilesPath != null) {
            File f = new File(nativeImageFilesPath);
            for(String resource : resources) {
               File src = new File(nativeImageFilesPath,resource);
                File dst = new File(nativeImageResourceDir,resource);
                if(!dst.exists())
                    dst.createNewFile();
                try(InputStream inputStream = new FileInputStream(src);
                    FileOutputStream fileOutputStream = new FileOutputStream(dst)) {
                    IOUtils.copy(inputStream,fileOutputStream);
                    fileOutputStream.flush();
                }

                System.out.println("Wrote resource " + resource + " to " + dst.getAbsolutePath());

            }
        }

        if(javacppPlatform != null && !javacppPlatform.isEmpty()) {
            invocationRequest.setMavenOpts("-Djavacpp.platform=" + javacppPlatform);
            invocationRequest.setGoals(Arrays.asList("-Djavacpp.platform=" + javacppPlatform,"-Dorg.eclipse.python4j.numpyimport=false","clean","package"));
        }
        else {
            invocationRequest.setGoals(Arrays.asList("clean","package"));

        }

        Invoker invoker = new DefaultInvoker();
        invocationRequest.setPomFile(new File(project,"pom.xml"));
        invoker.setWorkingDirectory(project);
        invocationRequest.setBaseDirectory(project);
        invoker.setMavenHome(mavenHome);
        invoker.execute(invocationRequest);
    }

    public static void main(String...args) {
        new CommandLine(new NativeImageBuilder()).execute(args);
    }


    @Override
    public Void call() throws Exception {
        runMain(new String[]{});
        return null;
    }
}