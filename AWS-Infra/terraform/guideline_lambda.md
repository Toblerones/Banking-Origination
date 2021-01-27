Create new shell
```commandline
aws lambda create-function --function-name ProcessDynamoDBRecords2 --zip-file fileb://deployment2.zip --handler index.handler --runtime nodejs12.x --role arn:aws:iam::123456789012:role/lambda-dynamodb-role --endpoint-url http://localhost:4566
```

Update function code
```commandline
aws lambda update-function-code --function-name ddb_stream_handler --zip-file fileb://de2.zip --endpoint-url http://localhost:4566
```

Invoke for testing
```commandline
aws lambda invoke --function-name ddb_stream_handler --payload file://events/event.json outputfile.txt --endpoint-url http://localhost:4566
```

Add an event source in AWS Lambda 
```commandline
aws lambda create-event-source-mapping --function-name ProcessDynamoDBRecords \
  --batch-size 100 --starting-position LATEST --event-source DynamoDB-stream-arn
```

List event mapping
```commandline
aws lambda list-event-source-mappings --function-name ProcessDynamoDBRecords
```

Lambda log
1. get stream
```commandline
aws logs describe-log-streams --log-group-name "/aws/lambda/ddb_stream_handler" --endpoint-url=http://localhost:4566
```

Get log 
```commandline
aws logs get-log-events --log-group-name "/aws/lambda/ddb_stream_handler" --log-stream-name 2021/01/27/[LATEST]5ace97d2 --endpoint-url=http://localhost:4566
```

