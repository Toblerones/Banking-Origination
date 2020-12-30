package test.com.toblers.origination.digital.services;

import static org.junit.Assert.*;

import com.toblers.origination.digital.services.DigitalFormService;
import com.toblers.origination.digital.services.DigitalFormServiceFactory;
import com.toblers.origination.digital.services.impl.DigitalFormSaveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DigitalFormServiceFactoryTest {

    @InjectMocks
    DigitalFormServiceFactory factory;

    @Mock
    Map<String, DigitalFormService> digitalFormServices;

    @Test
    public void testGetService(){
        DigitalFormSaveService digitalFormSaveService = new DigitalFormSaveService();
        when(digitalFormServices.get("DEFAULT-DIGITAL_FORM-SERVICE")).thenReturn(digitalFormSaveService);

        DigitalFormService service = factory.getService("");

        assertTrue(service == digitalFormSaveService);
    }
}
