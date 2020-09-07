package eql;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.support.SoapUIException;


@RunWith(Parameterized.class)
public class RunnerSoapUI_LogicaldocDemutualiseTest {
	private String testCaseName;
	private static String soapuiProjectName = "src/test/resources/LogicalDoc_API_test_Unitaire.xml";

	public RunnerSoapUI_LogicaldocDemutualiseTest(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	@Parameters(name = "{0}")
	public static Collection<String[]> getTestCases() throws XmlException, IOException, SoapUIException {
		final ArrayList<String[]> testCases = new ArrayList<String[]>();
		WsdlProject project = new WsdlProject(soapuiProjectName);
		List<TestSuite> testSuites = project.getTestSuiteList();
		
		for (TestSuite suite : testSuites) {
			List<TestCase> lTestCases = suite.getTestCaseList();
			for (TestCase testCase : lTestCases) {
				if (!testCase.isDisabled()) {
					testCases.add(new String[] { testCase.getName() });
				}
			}
		}
		return testCases;

	}

	@Test
	public void testSoapUITestCaseTest() throws XmlException, IOException, SoapUIException {
		System.out.println("Nom du cas de test SoapUI : " + testCaseName);
		assertTrue(true);
		assertTrue(runSoapUITestCase(this.testCaseName));
	}

	public static boolean runSoapUITestCase(String testCase) throws XmlException, IOException, SoapUIException {
		TestRunner.Status exitValue = TestRunner.Status.INITIALIZED;
		WsdlProject soapuiProject = new WsdlProject(soapuiProjectName);
		List<TestSuite> testSuites = soapuiProject.getTestSuiteList();
		for (TestSuite suite : testSuites) {
			if (suite == null) {
				System.err.println("runner soapUI, la suite de test est null : " + suite);
				return false;
			}
			TestCase soapuiTestCase = suite.getTestCaseByName(testCase);
			if (soapuiTestCase == null) {
				System.err.println("runner soapUI, le cas de test est null : " + testCase);
			} else {
				TestCaseRunner runner = soapuiTestCase.run(new PropertiesMap(), false);
				exitValue = runner.getStatus();
			}

			System.out.println("cas de test soapUI terminé ('" + suite + "':'" + testCase + "') : " + exitValue);
		}
		if (exitValue == TestRunner.Status.FINISHED) {
			return true;
		} else {
			return false;

		}
	}
}
