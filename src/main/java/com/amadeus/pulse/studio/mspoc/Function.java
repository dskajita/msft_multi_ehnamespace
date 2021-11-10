package com.amadeus.pulse.studio.mspoc;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.EventHubOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            @EventHubOutput(
              name="outputOne",
              connection = "AzureEventHubOne",
              eventHubName = "",
              dataType = "binary")
          final OutputBinding<byte[]> outputOne,
            @EventHubOutput(
              name="outputTwo",
              connection = "AzureEventHubTwo",
              eventHubName = "",
              dataType = "binary")
          final OutputBinding<byte[]> outputTwo,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        outputOne.setValue("outputOne".getBytes());
        outputTwo.setValue("outputTwo".getBytes());

        return request.createResponseBuilder(HttpStatus.OK).build();
    }
}
