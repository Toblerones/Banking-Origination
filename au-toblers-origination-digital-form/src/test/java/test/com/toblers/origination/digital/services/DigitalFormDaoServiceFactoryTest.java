package test.com.toblers.origination.digital.services;

import static org.junit.Assert.*;

import com.toblers.origination.digital.services.DigitalFormService;
import com.toblers.origination.digital.services.DigitalFormServiceFactory;
import com.toblers.origination.digital.services.impl.DigitalFormInitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DigitalFormDaoServiceFactoryTest {

    @InjectMocks
    DigitalFormServiceFactory factory;

    @Mock
    Map<String, DigitalFormService> digitalFormServices;

    @Test
    public void testGetService(){
        DigitalFormInitService digitalFormSaveService = new DigitalFormInitService();
        when(digitalFormServices.get("DEFAULT-DIGITAL_FORM-SERVICE")).thenReturn(digitalFormSaveService);

        DigitalFormService service = factory.getService("");

        assertTrue(service == digitalFormSaveService);
    }
}
