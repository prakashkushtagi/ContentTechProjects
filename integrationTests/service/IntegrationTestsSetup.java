package service;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/SourceContentLoaderTest-context.xml"})
@TestExecutionListeners( {TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public abstract class IntegrationTestsSetup {

}
