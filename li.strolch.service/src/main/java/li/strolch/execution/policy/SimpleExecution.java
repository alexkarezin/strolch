package li.strolch.execution.policy;

import li.strolch.agent.api.ComponentContainer;
import li.strolch.handler.operationslog.LogMessage;
import li.strolch.handler.operationslog.OperationsLog;
import li.strolch.model.State;
import li.strolch.model.activity.Action;
import li.strolch.model.timevalue.impl.FloatValue;
import li.strolch.model.timevalue.impl.ValueChange;
import li.strolch.persistence.api.StrolchTransaction;

/**
 * <p>
 * Simple Execution Policy which sets the state of the action depending on the method called.
 * </p>
 *
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class SimpleExecution extends ExecutionPolicy {

	public SimpleExecution(ComponentContainer container, StrolchTransaction tx) {
		super(container, tx);
	}

	@Override
	public void toExecution(Action action) {
		setActionState(action, State.EXECUTION);

		FloatValue value = new FloatValue(1.0D);
		action.addChange(new ValueChange<>(System.currentTimeMillis(), value, ""));
	}

	@Override
	public void toWarning(Action action) {
		setActionState(action, State.WARNING);
	}

	@Override
	public void toExecuted(Action action) {
		setActionState(action, State.EXECUTED);

		FloatValue value = new FloatValue(0.0D);
		action.addChange(new ValueChange<>(System.currentTimeMillis(), value, ""));
	}

	@Override
	public void toStopped(Action action) {
		getDelayedExecutionTimer().cancel(action.getLocator());
		setActionState(action, State.STOPPED);

		FloatValue value = new FloatValue(0.0D);
		action.addChange(new ValueChange<>(System.currentTimeMillis(), value, ""));
	}

	@Override
	public void toError(Action action) {
		getDelayedExecutionTimer().cancel(action.getLocator());
		setActionState(action, State.ERROR);

		FloatValue value = new FloatValue(0.0D);
		action.addChange(new ValueChange<>(System.currentTimeMillis(), value, ""));
	}

	protected void addMessage(LogMessage message) {
		if (getContainer().hasComponent(OperationsLog.class)) {
			OperationsLog operationsLog = getContainer().getComponent(OperationsLog.class);
			operationsLog.addMessage(message);
		}
	}

	protected void toError(LogMessage message) {
		addMessage(message);
		getExecutionHandler().toError(message.getRealm(), message.getLocator());
	}

	protected void toWarning(LogMessage message) {
		addMessage(message);
		getExecutionHandler().toWarning(message.getRealm(), message.getLocator());
	}

	@Override
	public void undo() {
		logger.error("Can not undo execution policy " + getClass());
	}
}
