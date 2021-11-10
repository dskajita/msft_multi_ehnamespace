package com.amadeus.pulse.studio.mspoc;

import java.util.List;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

public class ReadEHOne {
    @FunctionName("OutputOne")
    public void run(
            @EventHubTrigger(
                name = "outputOne",
                connection = "AzureEventHubOne",
                eventHubName = "",
                dataType = "binary")
            List<byte[]> messages,
            final ExecutionContext context) {
        context.getLogger().info("OutputOne trigger from Outbound sink. Number of messages received: " + messages.size());

        for (byte[] b : messages) {
            context.getLogger().info("OutputOne: Message " + new String(b));
        }
    }
}
