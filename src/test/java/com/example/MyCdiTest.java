package com.example;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.util.Map;

@ArquillianSuiteDeployment
public class MyCdiTest extends Arquillian {

    @Inject
    private MyCdiBean bean;

    @Inject
    private MyEJBBean beanEJB;

    @Deployment(name = "app")
    public static JavaArchive createDeployment() {

        System.out.println("MyCdiTest.createDeployment called");
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(MyCdiBean.class)
                .addClass(MyEJBBean.class)
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Parameters({"suiteParam1"})
    @BeforeSuite //(alwaysRun = true)
    public void beforeSuite(@Optional("defaultSuiteParam") String suiteParam1) {
        System.out.println("Before Suite called param: " + suiteParam1);
    }

    @Parameters({"testParam1"})
    @BeforeMethod //(alwaysRun = true)
    public void beforeMethod(ITestContext context, @Optional("defaultTestParam") String testParam1) {
        String suiteParam = context.getSuite().getParameter("suiteParam1");
        String testParam = context.getCurrentXmlTest().getParameter("testParam1");

        System.out.println("Params from context: " + suiteParam + ", " + testParam);

        Map<String, String> methodParams = context.getCurrentXmlTest().getAllParameters();
        System.out.println("Params from context: " + methodParams);



        System.out.println("Before Method called param: " + testParam1);
        assert bean != null : "CDI bean should be injected";
        assert beanEJB != null : "EJB bean should be injected";
    }

    @Parameters({"suiteParam1","testParam1", "methodParam1"})
    @Test //(dependsOnMethods = "beforeClass")
    @OperateOnDeployment("app")
    public void testCdiBeanInjection(@Optional("defaultSuiteParam") String suiteParam1, @Optional("defaultTestParam") String testParam1, @Optional("defaultMethodParam") String methodParam1) {
        System.out.println("MyCdiTest.testCdiBeanInjection called with params: " + suiteParam1 + ", " + testParam1 + "," + methodParam1);
        System.out.println("MyCdiTest.testCdiBeanInjection called: " + bean);

        ITestResult reportResult = Reporter.getCurrentTestResult();

        System.out.println("MyCdiTest.testCdiBeanInjection called: " + reportResult.getTestContext().getCurrentXmlTest().toXml(" "));

        String msg = bean.getMessage();
        assert msg.equals("Hello from CDI Bean!");
    }

    @Test //(groups = "mytest")
    @OperateOnDeployment("app")
    public void testEjbBeanInjection() {
        System.out.println("MyCdiTest.testEjbBeanInjection called: " + beanEJB);
        String msg = beanEJB.getMessage();
        assert msg.equals("Hello from EJB Bean!");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("After Suite");
    }
}
