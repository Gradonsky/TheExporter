import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.CommunicationsException;



public class CLITest {
	   @Before
	   public void initialize() {
		   CLI a = new CLI();
	   }

		/**
		 * @throws java.lang.Exception
		 */
		@Before
		public void setUp() throws Exception
		{

		}

		/**
		 * @throws java.lang.Exception
		 */
		@After
		public void tearDown() throws Exception
		{
		}

		@Test (expected = AssertionError.class)
		public void ParameterTestung()
		{
			CLI a = new CLI();
			a.main(new String[] {"-h test", "-u NoUser"});
		//	assertEquals(expected, actual);

		}

//		@Test (expected = Exception.class)
//		public void Testung()
//		{
//			CLI a = new CLI();
//			a.main(new String[] {"-h","-u uhasid"});
//
//		//	assertEquals(expected, actual);
//
//		}

}
