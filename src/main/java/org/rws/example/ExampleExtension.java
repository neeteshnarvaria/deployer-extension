package org.rws.example;

import com.sdl.delivery.configuration.Configuration;
import com.sdl.delivery.configuration.ConfigurationException;
import com.sdl.delivery.deployer.api.processing.annotations.Step;
import com.sdl.delivery.deployer.api.processing.exception.ProcessingException;
import com.sdl.delivery.deployer.api.processing.pipeline.ExecutableStep;
import com.sdl.delivery.deployer.api.processing.pipeline.ExecutableStepResult;
import com.sdl.delivery.deployer.api.processing.pipeline.ProcessingContext;
import com.sdl.delivery.deployer.api.processing.pipeline.StepDataProvider;
import com.tridion.transport.transportpackage.TransportPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sdl.delivery.deployer.steps.util.TridionStepUtil.getPackageUnzipLocation;
import static com.tridion.transport.transportpackage.TransportPackage.DEPLOY_ACTION;
import static com.tridion.transport.transportpackage.TransportPackage.UNDEPLOY_ACTION;

@Step("PageStateNotifier")
public class ExampleExtension implements ExecutableStep {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleExtension.class);

    @Override
    public void configure(Configuration configuration) throws ConfigurationException {
        //this is section where we initialize step
    }

    @Override
    public ExecutableStepResult process(ProcessingContext processingContext, StepDataProvider stepDataProvider) throws ProcessingException {
        LOG.debug("Starting ExampleExtension");
        var location = getPackageUnzipLocation(processingContext, stepDataProvider);
        LOG.debug("ExampleExtension getPackageUnzipLocation location {}", location);
        TransportPackage transportPackage = new TransportPackage(location, stepDataProvider.getBinaryStorage());
        LOG.debug("ExampleExtension transportPackage {}", transportPackage);
        final String action = transportPackage.getAction();
        LOG.debug("PageStateNotifier action {}", action);
        final String transactionId = transportPackage.getTransactionId().toString();
        LOG.debug("Process Action {} for Transaction {}", action, transactionId);
        switch (action) {
            case DEPLOY_ACTION:
                LOG.info("Publish action triggerred");
                break;
            case UNDEPLOY_ACTION:
                LOG.info("UnPublish action triggerred");
                break;
            default:
                LOG.error("Invalid action {}", action);
                throw new ProcessingException("Invalid transport package action " + action);
        }
        return null;
    }
}
