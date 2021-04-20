
package ops.therapi;

import java.util.Iterator;
import java.util.function.BiFunction;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.scijava.Context;
import org.scijava.cache.CacheService;
import org.scijava.ops.OpField;
import org.scijava.ops.OpInfo;
import org.scijava.ops.OpService;
import org.scijava.ops.core.OpCollection;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;
import org.scijava.thread.ThreadService;
import org.scijava.types.TypeService;

@Plugin(type = OpCollection.class)
public class TestClass {

	protected static Context context;
	protected static OpService ops;

	@BeforeAll
	public static void setUp() {
		context = new Context(OpService.class, CacheService.class,
			ThreadService.class, PluginService.class, TypeService.class);
		ops = context.service(OpService.class);
	}

	@AfterAll
	public static void tearDown() {
		context.dispose();
		context = null;
		ops = null;
	}

	/**
	 * @input a the first input
	 * @input b the second input
	 * @output the sum of a and b
	 */
	@OpField(names = "test.separate_module")
	public final BiFunction<Double, Double, Double> foofunc = (a, b) -> a + b;

	@Test
	public void testJavadocScraping() {
		Iterator<OpInfo> infos = ops.env().infos("test.separate_module").iterator();
		if (!infos.hasNext()) {
			Assert.fail();
		}
		OpInfo info = infos.next();
		String desc = info.toString();

		String expected = "ops.therapi.TestClass.foofunc(\n\t " +
			"Inputs:\n\t\tjava.lang.Double input1\n\t\t" +
			"java.lang.Double input2\n\t " +
			"Outputs:\n\t\tjava.lang.Double output1\n)\n";
		Assertions.assertEquals(expected, desc);
	}

	@Test
	public void testJavadocRetention() {
		Iterator<OpInfo> infos = ops.env().infos("math.minmax").iterator();
		if (!infos.hasNext()) {
			Assert.fail();
		}
		OpInfo info = infos.next();
		String desc = info.toString();

		String expected =
			"org.scijava.ops.math.Normalize$MathMinMaxNormalizeFunction(\n\t " +
				"Inputs:\n\t\tdouble[] input1\n\t\t" + "java.lang.Double input2\n\t\t" +
				"java.lang.Double input3\n\t " + "Outputs:\n\t\tdouble[] output1\n)\n";
		Assertions.assertEquals(expected, desc);
	}
}
