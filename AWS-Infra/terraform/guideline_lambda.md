- Create new shell
aws lambda create-function --function-name ProcessDynamoDBRecords2 --zip-file fileb://deployment2.zip --handler index.handler --runtime nodejs12.x --role arn:aws:iam::123456789012:role/lambda-dynamodb-role --endpoint-url http://localhost:4566

- update function code
aws lambda update-function-code --function-name ddb_stream_handler --zip-file fileb://de2.zip --endpoint-url http://localhost:4566
  
- Invoke for testing
aws lambda invoke --function-name ddb_stream_handler --payload file://events/event.json outputfile.txt --endpoint-url http://localhost:4566


- Add an event source in AWS Lambda 
aws lambda create-event-source-mapping --function-name ProcessDynamoDBRecords \
  --batch-size 100 --starting-position LATEST --event-source DynamoDB-stream-arn
  
- list event mapping
aws lambda list-event-source-mappings --function-name ProcessDynamoDBRecords

