# msft_multi_ehnamespace
This small repository is to reproduce an issue that we found out in Amadeus and it seems not documented (or at least we did not find any explanation for it).

# To reproduce failure :
On the local.settings.json, fill the 3 settings :
* AzureWebJobsStorage (nothing particular, just EH needs a Storage)
* AzureEventHubOne (the Connection String to your Event Hub, not only Event Hub Namespace. It needs to contain the EntityPath) :
    Endpoint=sb://<EHNAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<SAS>;SharedAccessKey=<KEY>;EntityPath=<TOPIC1>
* AzureEventHubTwo (same as above, but use a different topic)
    Endpoint=sb://<EHNAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<SAS>;SharedAccessKey=<KEY>;EntityPath=<TOPIC2>

You would see an output like this :
```
[2021-11-10T15:40:02.213Z] Executing 'Functions.HttpExample' (Reason='This function was programmatically called via the host APIs.', Id=xxxxxxxxxxxxxxxxxxxxxxxx)
[2021-11-10T15:40:02.292Z] Java HTTP trigger processed a request.
[2021-11-10T15:40:02.296Z] Function "HttpExample" (Id: xxxxxxxxxxxxxxxxxxxxxxxx) invoked by Java Worker
[2021-11-10T15:40:02.548Z] Executing 'Functions.OutputOne' (Reason='(null)', Id=yyyyyyyyyyyyyyyyyyy)
[2021-11-10T15:40:02.576Z] Executed 'Functions.HttpExample' (Succeeded, Id=xxxxxxxxxxxxxxxxxxxxxxxx, Duration=369ms)
[2021-11-10T15:40:02.588Z] OutputOne trigger from Outbound sink. Number of messages received: 1
[2021-11-10T15:40:02.591Z] OutputOne: Message outputOne
[2021-11-10T15:40:02.593Z] Function "OutputOne" (Id: yyyyyyyyyyyyyyyyyyy) invoked by Java Worker
[2021-11-10T15:40:02.595Z] Executed 'Functions.OutputOne' (Succeeded, Id=yyyyyyyyyyyyyyyyyyy, Duration=48ms)
[2021-11-10T15:40:02.674Z] Executing 'Functions.OutputOne' (Reason='(null)', Id=zzzzzzzzzzzzzzzzzzzzz)
[2021-11-10T15:40:02.678Z] OutputOne trigger from Outbound sink. Number of messages received: 1
[2021-11-10T15:40:02.680Z] OutputOne: Message outputTwo
[2021-11-10T15:40:02.682Z] Function "OutputOne" (Id: zzzzzzzzzzzzzzzzzzzzz) invoked by Java Worker
[2021-11-10T15:40:02.684Z] Executed 'Functions.OutputOne' (Succeeded, Id=zzzzzzzzzzzzzzzzzzzzz, Duration=10ms)
```

As you can see above, even if the HttpTrigger function is setting values for outputOne and outputTwo, the message is sent/received always for the same one

# To make it work (work-around provided by Microsoft)
On the local.settings.json, fill the 3 settings :
* AzureWebJobsStorage (nothing particular, just EH needs a Storage)
* AzureEventHubOne (the Connection String to your Event Hub Namespace. Do not specify the Entity Path) :
    Endpoint=sb://<EHNAMESPACE>.windows.net/;SharedAccessKeyName=<SAS>;SharedAccessKey=<KEY>
* AzureEventHubTwo (same as above, but use a different topic)
    Endpoint=sb://<EHNAMESPACE>.windows.net/;SharedAccessKeyName=<SAS>;SharedAccessKey=<KEY>

In the EventHub binding, you define the eventHubName parameter, like :

```
            @EventHubOutput(
              name="outputOne",
              connection = "AzureEventHubOne",
              eventHubName = "TOPIC1",
              dataType = "binary")

            @EventHubOutput(
              name="outputTwo",
              connection = "AzureEventHubTwo",
              eventHubName = "TOPIC2",
              dataType = "binary")
```
This works fine.