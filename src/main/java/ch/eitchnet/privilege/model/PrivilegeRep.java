/*
 * Copyright (c) 2010 - 2012
 * 
 * This file is part of Privilege.
 *
 * Privilege is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Privilege is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Privilege.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ch.eitchnet.privilege.model;

import java.io.Serializable;
import java.util.Set;

import ch.eitchnet.privilege.handler.PrivilegeHandler;
import ch.eitchnet.privilege.i18n.PrivilegeException;
import ch.eitchnet.privilege.model.internal.Privilege;
import ch.eitchnet.privilege.model.internal.Role;
import ch.eitchnet.privilege.policy.PrivilegePolicy;

/**
 * To keep certain details of the {@link Privilege} itself hidden from remote clients and make sure instances are only
 * edited by users with the correct privilege, this representational version is allowed to be viewed by remote clients
 * and simply wraps all public data from the {@link Privilege}
 * 
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class PrivilegeRep implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String policy;
	private boolean allAllowed;
	private Set<String> denyList;
	private Set<String> allowList;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *            the name of this privilege, which is unique to all privileges known in the {@link PrivilegeHandler}
	 * @param policy
	 *            the {@link PrivilegePolicy} configured to evaluate if the privilege is granted
	 * @param allAllowed
	 *            a boolean defining if a {@link Role} with this {@link Privilege} has unrestricted access to a
	 *            {@link Restrictable}
	 * @param denyList
	 *            a list of deny rules for this {@link Privilege}
	 * @param allowList
	 *            a list of allow rules for this {@link Privilege}
	 */
	public PrivilegeRep(String name, String policy, boolean allAllowed, Set<String> denyList, Set<String> allowList) {
		this.name = name;
		this.policy = policy;
		this.allAllowed = allAllowed;
		this.denyList = denyList;
		this.allowList = allowList;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the policy
	 */
	public String getPolicy() {
		return this.policy;
	}

	/**
	 * @param policy
	 *            the policy to set
	 */
	public void setPolicy(String policy) {
		this.policy = policy;
	}

	/**
	 * @return the allAllowed
	 */
	public boolean isAllAllowed() {
		return this.allAllowed;
	}

	/**
	 * @param allAllowed
	 *            the allAllowed to set
	 */
	public void setAllAllowed(boolean allAllowed) {
		this.allAllowed = allAllowed;
	}

	/**
	 * @return the denyList
	 */
	public Set<String> getDenyList() {
		return this.denyList;
	}

	/**
	 * @param denyList
	 *            the denyList to set
	 */
	public void setDenyList(Set<String> denyList) {
		this.denyList = denyList;
	}

	/**
	 * @return the allowList
	 */
	public Set<String> getAllowList() {
		return this.allowList;
	}

	/**
	 * @param allowList
	 *            the allowList to set
	 */
	public void setAllowList(Set<String> allowList) {
		this.allowList = allowList;
	}

	/**
	 * <p>
	 * Validates this {@link PrivilegeRep} so that a {@link Privilege} object can later be created from it
	 * </p>
	 * 
	 * TODO write comment on how validation is done
	 */
	public void validate() {

		if (this.name == null || this.name.isEmpty()) {
			throw new PrivilegeException("No name defined!");
		}

		// if not all allowed, then validate that deny and allow lists are defined
		if (this.allAllowed) {

			// all allowed means no policy will be used
			if (this.policy != null && !this.policy.isEmpty()) {
				throw new PrivilegeException("All is allowed, so Policy may not be set!");
			}

			if (this.denyList != null && !this.denyList.isEmpty())
				throw new PrivilegeException("All is allowed, so deny list must be null");
			if (this.allowList != null && !this.allowList.isEmpty())
				throw new PrivilegeException("All is allowed, so allow list must be null");

		} else {

			// not all allowed, so policy must be set
			if (this.policy == null || this.policy.isEmpty()) {
				throw new PrivilegeException("All is not allowed, so Policy must be defined!");
			}

			if (this.denyList == null) {
				throw new PrivilegeException("All is not allowed, so deny list must be set, even if empty");
			}
			if (this.allowList == null) {
				throw new PrivilegeException("All is not allowed, so allow list must be set, even if empty");
			}
		}
	}
}
