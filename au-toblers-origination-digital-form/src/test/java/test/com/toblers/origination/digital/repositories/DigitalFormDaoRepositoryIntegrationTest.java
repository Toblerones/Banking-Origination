package test.com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.OpenAPI2SpringBoot;
import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.model.Product;
import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.repositories.model.DigitalFormDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
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
public class DigitalFormDaoRepositoryIntegrationTest {

    @Autowired
    DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Autowired
    DigitalFormRepository repository;

    @Test
    public void testRepoInsert() throws ExecutionException, InterruptedException {
        DigitalForm form = new DigitalForm();
        form.setFormId("ABC123");
        Product product = new Product();
        List<String> codes = new LinkedList<>();
        codes.add("EGA");
        product.setProductCodes(codes);
        form.setProduct(product);
        form.setStatus("IC");

        repository.createDigitalForm(form);

    }

    @Test
    public void testInsert() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));
        DigitalFormDao form = new DigitalFormDao();
        form.setPk("FORM#ABC123");
        form.setSk("INFO#ABC123");
        form.setProducts("EGA");
        form.setStatus("IC");
        form.setCreatedAt("102002012021");
        form.setStatusCreatedAt("IC#102002012021");

        DigitalFormDao.Finance finance = new DigitalFormDao.Finance();
        finance.setCredit(BigDecimal.valueOf(1000));
        finance.setIncome(BigDecimal.valueOf(200000));
        finance.setMortgage(BigDecimal.valueOf(1000000));

        form.setFinanceInfo(finance);
        digitalformTable.putItem(form).get();
    }

    @Test
    public void testFindByGSi() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        DynamoDbAsyncIndex<DigitalFormDao> secIndex = digitalformTable
                        .index("statusAndCreatedAt");

        SdkPublisher<Page<DigitalFormDao>> customersWithName =
                secIndex.query(r -> r.queryConditional(
                        keyEqualTo(k -> k.partitionValue("FORM#ABC123").sortValue("IC#102002012021"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);

        customersWithName.subscribe(page -> {
            DigitalFormDao digitalFormDao = (DigitalFormDao) page.items().get(atomicInteger.get());
            System.out.println(digitalFormDao.getProducts());
            DigitalFormDao.Finance finance = digitalFormDao.getFinanceInfo();
            atomicInteger.incrementAndGet();
        }).get();
    }
}
