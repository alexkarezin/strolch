package ch.eitchnet.utils.dbc;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.eitchnet.utils.dbc.DBC.DbcException;

/**
 * The class <code>DBCTest</code> contains tests for the class <code>{@link DBC}</code>.
 * 
 * @generatedBy CodePro at 2/2/14 8:13 PM
 * @author eitch
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("nls")
public class DBCTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Run the void assertEquals(String,Object,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertEquals_1() throws Exception {
		String msg = "";
		Object value1 = null;
		Object value2 = null;

		DBC.PRE.assertEquals(msg, value1, value2);

		// add additional test code here
	}

	/**
	 * Run the void assertEquals(String,Object,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertEquals_2() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Values are not equal:");
		String msg = "";
		Object value1 = new Object();
		Object value2 = new Object();

		DBC.PRE.assertEquals(msg, value1, value2);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    ch.eitchnet.utils.DBC.PRE.DBC$DbcException: Values are not equal: 
		//       at ch.eitchnet.utils.DBC.PRE.DBC.PRE.assertEquals(DBC.PRE.java:39)
	}

	/**
	 * Run the void assertEquals(String,Object,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertEquals_3() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Values are not equal:");

		String msg = "";
		Object value1 = null;
		Object value2 = new Object();

		DBC.PRE.assertEquals(msg, value1, value2);

		// add additional test code here
	}

	/**
	 * Run the void assertEquals(String,Object,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertEquals_4() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Values are not equal:");

		String msg = "";
		Object value1 = new Object();
		Object value2 = null;

		DBC.PRE.assertEquals(msg, value1, value2);

		// add additional test code here
	}

	/**
	 * Run the void assertEquals(String,Object,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertEquals_5() throws Exception {
		String msg = "";
		Object value1 = "bla";
		Object value2 = "bla";

		DBC.PRE.assertEquals(msg, value1, value2);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    ch.eitchnet.utils.DBC.PRE.DBC$DbcException: Values are not equal: 
		//       at ch.eitchnet.utils.DBC.PRE.DBC.PRE.assertEquals(DBC.PRE.java:39)
	}

	/**
	 * Run the void assertExists(String,File) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertExists_1() throws Exception {
		String msg = "";
		File file = new File("src");

		DBC.PRE.assertExists(msg, file);
	}

	/**
	 * Run the void assertExists(String,File) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertExists_2() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Illegal situation as file (srcc) does not exist:");

		String msg = "";
		File file = new File("srcc");

		DBC.PRE.assertExists(msg, file);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    ch.eitchnet.utils.DBC.PRE.DBC$DbcException: Illegal situation as file () does not exist: 
		//       at ch.eitchnet.utils.DBC.PRE.DBC.PRE.assertExists(DBC.PRE.java:95)
	}

	/**
	 * Run the void assertFalse(String,boolean) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertFalse_1() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Expected false, but was true: ");

		String msg = "";
		boolean value = true;

		DBC.PRE.assertFalse(msg, value);
	}

	/**
	 * Run the void assertFalse(String,boolean) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertFalse_2() throws Exception {
		String msg = "";
		boolean value = false;

		DBC.PRE.assertFalse(msg, value);

		// add additional test code here
	}

	/**
	 * Run the void assertNotEmpty(String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotEmpty_1() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Illegal empty value: ");

		String msg = "Illegal empty value: ";
		String value = "";

		DBC.PRE.assertNotEmpty(msg, value);
	}

	/**
	 * Run the void assertNotEmpty(String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotEmpty_2() throws Exception {
		String msg = "";
		String value = "a";

		DBC.PRE.assertNotEmpty(msg, value);
	}

	/**
	 * Run the void assertNotExists(String,File) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotExists_1() throws Exception {
		String msg = "";
		File file = new File("srcc");

		DBC.PRE.assertNotExists(msg, file);

		// add additional test code here
	}

	/**
	 * Run the void assertNotExists(String,File) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotExists_2() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Illegal situation as file (src) exists: ");

		String msg = "";
		File file = new File("src");

		DBC.PRE.assertNotExists(msg, file);

		// add additional test code here
	}

	/**
	 * Run the void assertNotNull(String,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotNull_1() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Illegal null value:");

		String msg = "";
		Object value = null;

		DBC.PRE.assertNotNull(msg, value);
	}

	/**
	 * Run the void assertNotNull(String,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNotNull_2() throws Exception {
		String msg = "";
		Object value = new Object();

		DBC.PRE.assertNotNull(msg, value);

		// add additional test code here
	}

	/**
	 * Run the void assertNull(String,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNull_1() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Illegal situation as value is not null:");

		String msg = "";
		Object value = new Object();

		DBC.PRE.assertNull(msg, value);
	}

	/**
	 * Run the void assertNull(String,Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertNull_2() throws Exception {
		String msg = "";
		Object value = null;

		DBC.PRE.assertNull(msg, value);

		// add additional test code here
	}

	/**
	 * Run the void assertTrue(String,boolean) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertTrue_1() throws Exception {
		this.exception.expect(DbcException.class);
		this.exception.expectMessage("Expected true, but was false: ");

		String msg = "";
		boolean value = false;

		DBC.PRE.assertTrue(msg, value);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    ch.eitchnet.utils.DBC.PRE.DBC$DbcException: Expected true, but was false: 
		//       at ch.eitchnet.utils.DBC.PRE.DBC.PRE.assertTrue(DBC.PRE.java:47)
	}

	/**
	 * Run the void assertTrue(String,boolean) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 2/2/14 8:13 PM
	 */
	@Test
	public void testAssertTrue_2() throws Exception {
		String msg = "";
		boolean value = true;

		DBC.PRE.assertTrue(msg, value);

		// add additional test code here
	}
}