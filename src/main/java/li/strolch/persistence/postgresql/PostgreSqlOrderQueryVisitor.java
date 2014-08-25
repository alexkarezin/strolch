/*
 * Copyright 2013 Robert von Burg <eitch@eitchnet.ch>
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
package li.strolch.persistence.postgresql;

import li.strolch.model.Tags;
import li.strolch.model.query.DateSelection;
import li.strolch.model.query.OrderQueryVisitor;
import li.strolch.model.query.StateSelection;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class PostgreSqlOrderQueryVisitor extends PostgreSqlQueryVisitor implements OrderQueryVisitor {

	/**
	 * @param fields
	 */
	public PostgreSqlOrderQueryVisitor(String fields) {
		super(fields);
	}

	protected String getClassName() {
		return Tags.ORDER;
	}

	protected String getTableName() {
		return PostgreSqlOrderDao.ORDERS;
	}

	@Override
	public void visit(DateSelection selection) {
		sb.append(indent);
		sb.append("date = ?\n");
		values.add(selection.getDate());
	}

	@Override
	public void visit(StateSelection selection) {
		sb.append(indent);
		sb.append("stae = ?\n");
		values.add(selection.getState().name());
	}
}
