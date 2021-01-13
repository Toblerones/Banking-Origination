package test.com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.OpenAPI2SpringBoot;
import com.toblers.origination.digital.model.Address;
import com.toblers.origination.digital.model.Customer;
import com.toblers.origination.digital.model.DigitalForm;
import com.toblers.origination.digital.model.Product;
import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.repositories.model.DigitalFormDao;
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
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class)
@WebAppConfiguration
@ActiveProfiles("test")
//@TestPropertySource(properties = {
//        "amazon.dynamodb.endpoint=http://dynamodb.ap-southeast-2.amazonaws.com/",
//        "amazon.aws.accesskey=bbb",
//        "amazon.aws.secretkey=xxx" })
public class DigitalFormDaoRepositoryIntegrationTest {

    @Autowired
    DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Autowired
    DigitalFormRepository repository;

    @Test
    public void testRepo() throws ExecutionException, InterruptedException {
        DigitalForm form = new DigitalForm();
        form.setFormId("ABC123");
        Product product = new Product();
        List<String> codes = new LinkedList<>();
        codes.add("EGA");
        product.setProductCodes(codes);
        form.setProduct(product);
        form.setStatus("IC");

        repository.createDigitalForm(form);

        Customer customer = new Customer();
        customer.setIndex(1);
        customer.setEmail("toblers@toblersEmail11.com");
        customer.setFirstName("Toblers");
        customer.setLastName("Luo");
        customer.setMobileNumber("0434170000");
        customer.setDateOfBirth("10101987");

        Address address = new Address();
        address.setCountry("AUS");
        address.setDetail("999 Ashley Ave, Sydney");
        address.setType("RA");
        customer.addAddressesItem(address);

        form.addCustomerItem(customer);

        repository.updateDigitalForm(form);
    }

    @Test
    public void testInsert() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));
        DigitalFormDao form = new DigitalFormDao();
        form.setPk("FORM#ABC123");
        form.setSk("INFO#ABC123");
        form.setProducts("CMG");
        form.setStatus("IC");
        form.setCreatedAt("102002012021");
        form.setProductsCreatedAt("EGA#102002012021");

        DigitalFormDao.Finance finance = new DigitalFormDao.Finance();
        finance.setCredit(BigDecimal.valueOf(1000));
        finance.setIncome(BigDecimal.valueOf(200000));
        finance.setMortgage(BigDecimal.valueOf(1000000));

        form.setFinanceInfo(finance);
        digitalformTable.putItem(form).get();
    }

    @Test
    public void testGetItemWithPKSK() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        DigitalFormDao form = digitalformTable.getItem(
                Key.builder().partitionValue("FORM#ABC123").sortValue("INFO#ABC123").build()).get();

        System.out.println(form.getSk());
    }

    @Test
    public void testQueryWithPKandSKBegin() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        PagePublisher<DigitalFormDao> digitalForms = digitalformTable.query(
                r -> r.queryConditional(
                        sortBeginsWith(k -> k.partitionValue("FORM#ABC123").sortValue("INFO"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);

        digitalForms.items().subscribe(item -> {
            System.out.println(item.getSk());
            atomicInteger.incrementAndGet();
        }).get();
    }


    @Test
    public void testFindByGSi() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        DynamoDbAsyncIndex<DigitalFormDao> secIndex = digitalformTable
                        .index("productsAndCreatedAt");

        SdkPublisher<Page<DigitalFormDao>> customersWithName =
                secIndex.query(r -> r.queryConditional(
                        keyEqualTo(k -> k.partitionValue("FORM#ABC123").sortValue("EGA#102002012021"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);

        customersWithName.subscribe(page -> {
            DigitalFormDao digitalFormDao = (DigitalFormDao) page.items().get(atomicInteger.get());
            System.out.println(digitalFormDao.getProducts());
            DigitalFormDao.Finance finance = digitalFormDao.getFinanceInfo();
            atomicInteger.incrementAndGet();
        }).get();
    }

    @Test
    public void testFindByInvertedIndex_async() throws ExecutionException, InterruptedException {
        DynamoDbAsyncTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedAsyncClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        DynamoDbAsyncIndex<DigitalFormDao> invertedIndex = digitalformTable
                .index("invertedIndex");

        SdkPublisher<Page<DigitalFormDao>> digitalForms = invertedIndex.query(
                r -> r.queryConditional(
                        sortBeginsWith(k -> k.partitionValue("INFO#ABC123").sortValue("FORM"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);

        digitalForms.subscribe(page -> {
            if (page.items().isEmpty()){
                System.out.println("no data found");
            } else {
                DigitalFormDao digitalFormDao = (DigitalFormDao) page.items().get(atomicInteger.get());
                System.out.println(digitalFormDao.getProducts());
                DigitalFormDao.Finance finance = digitalFormDao.getFinanceInfo();
                atomicInteger.incrementAndGet();
            }
        }).get();
    }

    @Test
    public void testFindByInvertedIndex_sync() throws ExecutionException, InterruptedException {
        DynamoDbTable<DigitalFormDao> digitalformTable = dynamoDbEnhancedClient
                .table("digital_form", TableSchema.fromBean(DigitalFormDao.class));

        DynamoDbIndex<DigitalFormDao> invertedIndex = digitalformTable
                .index("invertedIndex");

        SdkIterable<Page<DigitalFormDao>> digitalForms = invertedIndex.query(
                r -> r.queryConditional(
                        sortBeginsWith(k -> k.partitionValue("INFO#ABC123").sortValue("FORM"))));

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);


        List<DigitalFormDao> daoList = digitalForms.stream().findFirst().get().items();
        if (daoList.isEmpty()) {
            System.out.println("no record found");
        } else {
            System.out.println(daoList.get(0).getProducts());
        }

    }
}
