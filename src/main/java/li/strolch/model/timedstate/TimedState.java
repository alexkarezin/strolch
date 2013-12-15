/*
 * Copyright 2013 Martin Smock <smock.martin@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.strolch.model.timedstate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import li.strolch.model.timevalue.ITimeValue;
import li.strolch.model.timevalue.ITimeVariable;
import li.strolch.model.timevalue.IValue;
import li.strolch.model.timevalue.IValueChange;
import li.strolch.model.timevalue.impl.TimeVariable;

/**
 * @author Martin Smock <smock.martin@gmail.com>
 */
@SuppressWarnings("rawtypes")
public class TimedState<T extends IValue> implements ITimedState<T>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private ITimeVariable<T> timeVariable = new TimeVariable<T>();

	@Override
	@SuppressWarnings("unchecked")
	public ITimeValue<T> getNextMatch(final Long time, final T value) {
		Collection<ITimeValue<T>> futureValues = this.timeVariable.getFutureValues(time);
		for (ITimeValue<T> iTimeValue : futureValues) {
			if (iTimeValue.getValue().matches(value)) {
				return iTimeValue;
			}
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ITimeValue<T> getPreviousMatch(final Long time, final T value) {
		Collection<ITimeValue<T>> pastValues = this.timeVariable.getPastValues(time);
		List<ITimeValue<T>> asList = new ArrayList<ITimeValue<T>>(pastValues);
		Collections.reverse(asList);
		for (ITimeValue<T> iTimeValue : asList) {
			if (iTimeValue.getValue().matches(value)) {
				return iTimeValue;
			}
		}
		return null;
	}

	@Override
	public void applyChange(final IValueChange<T> change) {
		this.timeVariable.applyChange(change);
	}

	@Override
	public ITimeValue<T> getStateAt(final Long time) {
		return this.timeVariable.getValueAt(time);
	}

	@Override
	public ITimeVariable<T> getTimeEvolution() {
		return this.timeVariable;
	}

}
