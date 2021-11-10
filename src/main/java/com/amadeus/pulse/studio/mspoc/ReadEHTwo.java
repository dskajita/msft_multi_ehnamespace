package com.amadeus.pulse.studio.mspoc;

import java.util.List;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

public class ReadEHTwo {
    @FunctionName("OutputTwo")
    public void run(
            @EventHubTrigger(
                name = "outputTwo",
                connection = "AzureEventHubTwo",
                eventHubName = "",
                dataType = "binary")
            List<byte[]> messages,
            final ExecutionContext context) {
        context.getLogger().info("OutputTwo trigger from Outbound sink. Number of messages received: " + messages.size());

        for (byte[] b : messages) {
            context.getLogger().info("OutputTwo: Message " + new String(b));
        }
    }
}
