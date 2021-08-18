package ai.konduit.pipelinegenerator.main;

import ai.konduit.serving.pipeline.api.pipeline.Pipeline;
import ai.konduit.serving.pipeline.api.step.PipelineStep;
import ai.konduit.serving.pipeline.impl.pipeline.SequencePipeline;
import ai.konduit.serving.pipeline.util.ObjectMappers;
import ai.konduit.serving.vertx.config.InferenceConfiguration;
import ai.konduit.serving.vertx.config.ServerProtocol;
import org.apache.commons.io.FileUtils;
import org.nd4j.shade.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "inference-server-create",mixinStandardHelpOptions = true)
public class InferenceServerCreate implements Callable<Void> {

    @CommandLine.Option(names = {"--pipeline"},description = "Pipeline file path, must end in json, yml, or yaml",required = true)
    private File pipelineFile;
    @CommandLine.Option(names = {"--port"},description = "The port to use for the inference server, defaults to 9999. 0 means that the server will pick a random port on startup.",required = true)
    private int port = 9999;
    @CommandLine.Option(names = {"--protocol"},description = "The protocol to use. One of kafka,mqtt,http,grpc are supported. Defaults to http",required = true)
    private String protocol = "http";


    private ObjectMapper jsonMapper = ObjectMappers.json();
    private ObjectMapper yamlMapper = ObjectMappers.yaml();

    @Override
    public Void call() throws Exception {
        InferenceConfiguration inferenceConfiguration = new InferenceConfiguration()
                .protocol(ServerProtocol.valueOf(protocol.toUpperCase()))
                .port(port);

        if(pipelineFile.getName().endsWith(".json")) {
            Pipeline p = jsonMapper.readValue(pipelineFile,Pipeline.class);
            inferenceConfiguration.pipeline(p);
            System.out.println(inferenceConfiguration.toJson());

        } else if(pipelineFile.getName().endsWith(".yml") || pipelineFile.getName().endsWith(".yaml")) {
            Pipeline p = yamlMapper.readValue(pipelineFile,Pipeline.class);
            inferenceConfiguration.pipeline(p);
            System.out.println(inferenceConfiguration.toYaml());

        }

        return null;
    }


    public static void main(String...args) throws Exception  {
        StepCreator stepCreator = new StepCreator();
        CommandLine.Model.CommandSpec spec = stepCreator.spec();
        CommandLine commandLine = new CommandLine(spec);
        // CommandLine.ParseResult crop_grid = commandLine.parseArgs("image_to_ndarray", "--config=height=250,width=250,outputNames=image,dataType=double,normalization=scale");
        CommandLine.ParseResult onnx = commandLine.parseArgs("onnx","--inputNames=1","--inputNames=2","--outputNames=1","--outputNames=2","--modelUri=add.onnx");
        PipelineStep stepFromResult = stepCreator.createStepFromResult(onnx);
        FileUtils.write(new File("onnx-1.json"),stepFromResult.toJson(), Charset.defaultCharset());
        CommandLine.ParseResult onnx2 = commandLine.parseArgs("onnx","--inputNames=1","--inputNames=2","--outputNames=1","--outputNames=2","--modelUri=add.onnx");
        PipelineStep stepFromResult2 = stepCreator.createStepFromResult(onnx2);
        FileUtils.write(new File("onnx-2.json"),stepFromResult2.toJson(), Charset.defaultCharset());
        CommandLine combineCommand = new CommandLine(new InferenceServerCreate());
        combineCommand.execute("--pipeline","onnx-1.json","--pipeline","onnx-2.json");


    }

}