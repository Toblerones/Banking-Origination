package test.com.toblers.origination.digital.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.toblers.origination.digital.OpenAPI2SpringBoot;
import com.toblers.origination.digital.repositories.DigitalFormRepository;
import com.toblers.origination.digital.repositories.model.DigitalForm;
import com.toblers.origination.digital.repositories.model.DigitalFormId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=tw725l",
        "amazon.aws.secretkey=ip3qk" })
public class DigitalFormRepositoryIntegrationTest {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DigitalFormRepository repository;

    @Test
    public void testFindById(){
        DigitalFormId id = new DigitalFormId("FORM#ABC123","INFO#AB123");
        DigitalForm digitalForm = new DigitalForm();
        digitalForm.setDigitalFormId(id);
        digitalForm.setProducts("EGA");
        digitalForm.setStatus("IC");

        repository.save(digitalForm);
    }

}
