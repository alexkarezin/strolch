package li.strolch.soql.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import li.strolch.model.Order;
import li.strolch.model.ParameterBag;
import li.strolch.model.Resource;
import li.strolch.model.State;
import li.strolch.model.StrolchRootElement;
import li.strolch.model.activity.Activity;
import li.strolch.model.parameter.FloatParameter;
import li.strolch.model.parameter.Parameter;
import li.strolch.soql.antlr4.generated.SOQLLexer;
import li.strolch.soql.antlr4.generated.SOQLParser;

/**
 * @author msmock
 */
public abstract class BaseTest {

	/**
	 * parse the string and return the antlr tree
	 *
	 * @throws Exception
	 */
	ParseTree parseString(final String s) throws Exception {

		final CharStream input = CharStreams.fromString(s);
		final SOQLLexer lexer = new SOQLLexer(input); // create a buffer of tokens pulled from the lexer

		final CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens
																		// buffer
		final SOQLParser parser = new SOQLParser(tokens);
		parser.addErrorListener(new VerboseListener());

		final ParseTree tree = parser.select_statement(); // begin parsing at block

		// System.out.println(tree.toStringTree(parser)); // print LISP-style tree

		return tree;
	}

	/**
	 * compile the antlr tree to executable
	 *
	 * @param tree
	 * @return CompiledSOQLStatement
	 * @throws Exception
	 */
	CompiledStatement compile(final ParseTree tree) throws Exception {

		final ParseTreeWalker walker = new ParseTreeWalker();
		final SOQLListener listener = new SOQLListener();
		walker.walk(listener, tree);

		final CompiledStatement soqlStatement = new CompiledStatement();
		soqlStatement.entities = listener.getEntities();
		soqlStatement.whereExpression = listener.getWhereExpression();
		soqlStatement.selectClause = listener.getSelectClause();

		return soqlStatement;
	}

	/**
	 * @return a test resource
	 */
	StrolchRootElement getTestResource(final String id) {
		final Resource resource = new Resource();
		resource.setId(id);

		final ParameterBag bag = new ParameterBag();
		bag.setId("testBag");
		resource.addParameterBag(bag);

		final Parameter<Double> parameter = new FloatParameter();
		parameter.setId("testId");
		parameter.setValue(100d);

		resource.addParameter("testBag", parameter);
		return resource;
	}

	/**
	 * @return a test order
	 */
	StrolchRootElement getTestOrder(String id) {
		Order order = new Order();
		order.setId(id);
		order.setState(State.CREATED);

		order.setDate(new Date(117, 10, 01));

		ParameterBag bag = new ParameterBag();
		bag.setId("testBag");
		order.addParameterBag(bag);

		Parameter<Double> parameter = new FloatParameter();
		parameter.setId("testId");
		parameter.setValue(100d);

		order.addParameter("testBag", parameter);
		return order;
	}

	/**
	 * @return a test order
	 */
	StrolchRootElement getTestActivity(final String id) {
		final Activity activity = new Activity();
		activity.setId(id);

		final ParameterBag bag = new ParameterBag();
		bag.setId("testBag");
		activity.addParameterBag(bag);

		final Parameter<Double> parameter = new FloatParameter();
		parameter.setId("testId");
		parameter.setValue(100d);

		activity.addParameter("testBag", parameter);
		return activity;
	}

	List<StrolchRootElement> getTestRessources(int n) {
		final List<StrolchRootElement> result = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			result.add(getTestResource(String.valueOf(i)));
		}
		return result;
	}

	List<StrolchRootElement> getTestOrders(int n) {
		final List<StrolchRootElement> result = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			result.add(getTestOrder(String.valueOf(i)));
		}
		return result;
	}

	List<StrolchRootElement> getTestActivities(int n) {
		final List<StrolchRootElement> result = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			result.add(getTestActivity(String.valueOf(i)));
		}
		return result;
	}

}
