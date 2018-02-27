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
package li.strolch.persistence.xml.model;

import li.strolch.model.Tags;
import li.strolch.model.activity.Activity;
import li.strolch.xmlpers.api.PersistenceContext;
import li.strolch.xmlpers.api.PersistenceContextFactory;
import li.strolch.xmlpers.objref.IdOfSubTypeRef;
import li.strolch.xmlpers.objref.ObjectRef;
import li.strolch.xmlpers.objref.ObjectReferenceCache;

public class ActivityContextFactory implements PersistenceContextFactory<Activity> {

	@Override
	public PersistenceContext<Activity> createCtx(ObjectRef objectRef) {
		PersistenceContext<Activity> ctx = new PersistenceContext<>(objectRef);
		ctx.setParserFactory(new ActivityParserFactory());
		return ctx;
	}

	@Override
	public PersistenceContext<Activity> createCtx(ObjectReferenceCache objectRefCache, Activity t) {
		IdOfSubTypeRef objectRef = objectRefCache.getIdOfSubTypeRef(Tags.ACTIVITY, t.getType(), t.getId());
		PersistenceContext<Activity> ctx = createCtx(objectRef);
		ctx.setObject(t);
		return ctx;
	}
}
