package li.strolch.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import li.strolch.model.ModelGenerator;
import li.strolch.model.Order;
import li.strolch.model.ParameterBag;
import li.strolch.model.Resource;
import li.strolch.model.State;
import li.strolch.model.parameter.BooleanParameter;
import li.strolch.model.parameter.DateParameter;
import li.strolch.model.parameter.FloatParameter;
import li.strolch.model.parameter.IntegerParameter;
import li.strolch.model.parameter.LongParameter;
import li.strolch.model.parameter.StringListParameter;
import li.strolch.model.parameter.StringParameter;

import org.junit.Test;

@SuppressWarnings("nls")
public class ModelTest {

	@Test
	public void shouldCreateResource() {

		Resource resource = ModelGenerator.createResource("@res01", "Test resource", "MyType");
		ParameterBag bag = resource.getParameterBag(ModelGenerator.BAG_ID);
		validateBag(bag);
	}

	@Test
	public void shouldCreateOrder() {

		Order order = ModelGenerator.createOrder("@ord01", "Test Order", "MyType", new Date(), State.OPEN);
		ParameterBag bag = order.getParameterBag(ModelGenerator.BAG_ID);
		validateBag(bag);
	}

	public static void validateBag(ParameterBag bag) {

		assertNotNull(bag);

		assertEquals(ModelGenerator.BAG_ID, bag.getId());
		assertEquals(ModelGenerator.BAG_NAME, bag.getName());
		assertEquals(ModelGenerator.BAG_TYPE, bag.getType());

		validateParams(bag);
	}

	public static void validateParams(ParameterBag bag) {

		BooleanParameter boolParam = bag.getParameter(ModelGenerator.PARAM_BOOLEAN_ID);
		assertNotNull("Boolean Param missing with id " + ModelGenerator.PARAM_BOOLEAN_ID, boolParam);
		assertEquals(true, boolParam.getValue().booleanValue());

		FloatParameter floatParam = bag.getParameter(ModelGenerator.PARAM_FLOAT_ID);
		assertNotNull("Float Param missing with id " + ModelGenerator.PARAM_FLOAT_ID, floatParam);
		assertEquals(44.3, floatParam.getValue().doubleValue(), 0.0001);

		IntegerParameter integerParam = bag.getParameter(ModelGenerator.PARAM_INTEGER_ID);
		assertNotNull("Integer Param missing with id " + ModelGenerator.PARAM_INTEGER_ID, integerParam);
		assertEquals(77, integerParam.getValue().intValue());

		LongParameter longParam = bag.getParameter(ModelGenerator.PARAM_LONG_ID);
		assertNotNull("Long Param missing with id " + ModelGenerator.PARAM_LONG_ID, longParam);
		assertEquals(4453234566L, longParam.getValue().longValue());

		StringParameter stringParam = bag.getParameter(ModelGenerator.PARAM_STRING_ID);
		assertNotNull("String Param missing with id " + ModelGenerator.PARAM_STRING_ID, stringParam);
		assertEquals("Strolch", stringParam.getValue());

		DateParameter dateParam = bag.getParameter(ModelGenerator.PARAM_DATE_ID);
		assertNotNull("Date Param missing with id " + ModelGenerator.PARAM_DATE_ID, dateParam);
		assertEquals(1354295525628L, dateParam.getValue().getTime());

		StringListParameter stringListP = bag.getParameter(ModelGenerator.PARAM_LIST_STRING_ID);
		assertNotNull("StringList Param missing with id " + ModelGenerator.PARAM_LIST_STRING_ID, stringListP);

		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add("Hello");
		stringList.add("World");
		assertEquals(stringList, stringListP.getValue());
	}
}
