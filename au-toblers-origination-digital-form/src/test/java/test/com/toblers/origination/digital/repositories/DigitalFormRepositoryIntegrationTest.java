package test.com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.OpenAPI2SpringBoot;
import com.toblers.origination.digital.repositories.model.DigitalForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class)
@WebAppConfiguration
@ActiveProfiles("local")
//@TestPropertySource(properties = {
//        "amazon.dynamodb.endpoint=http://localhost:8000/",
//        "amazon.aws.accesskey=tw725l",
//        "amazon.aws.secretkey=ip3qk" })
public class DigitalFormRepositoryIntegrationTest {

    @Autowired
    DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Test
    public void testInsert() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalForm> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalForm.class));
        DigitalForm form = new DigitalForm();
        form.setPk("FORM#ABC123");
        form.setSk("INFO#ABC123");
        form.setProducts("EGA");
        form.setStatus("IC");
        form.setCreatedAt("102002012021");
        form.setStatusCreatedAt("IC#102002012021");
        digitalformTable.putItem(form).get();

    }

    @Test
    public void testFindByGSi() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalForm> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalForm.class));

        DynamoDbAsyncIndex<DigitalForm> secIndex = digitalformTable
                        .index("statusAndCreatedAt");

//        AttributeValue attVal = AttributeValue.builder()
//                .s("IC#10200201202")
//                .build();
//
//        // Create a QueryConditional object that's used in the query operation
//        QueryConditional queryConditional = QueryConditional
//                .keyEqualTo(Key.builder().partitionValue(attVal)
//                        .build());
//
//        // Get items in the DigitalForm table
//        SdkPublisher<Page<DigitalForm>> results =  secIndex.query(
//                QueryEnhancedRequest.builder()
//                        .queryConditional(queryConditional)
//                        .build());

        SdkPublisher<Page<DigitalForm>> customersWithName =
                secIndex.query(r -> r.queryConditional(
                        keyEqualTo(k -> k.partitionValue("IC#102002012021"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);


        customersWithName.subscribe(page -> {
            DigitalForm digitalForm = (DigitalForm) page.items().get(atomicInteger.get());
            System.out.println(digitalForm.getProducts());
            atomicInteger.incrementAndGet();
        }).get();
    }
}
