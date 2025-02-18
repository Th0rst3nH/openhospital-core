/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.menu.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.isf.utils.db.Auditable;

@Entity
@Table(name = "OH_USERGROUP")
@AttributeOverride(name = "createdBy", column = @Column(name = "UG_CREATED_BY", updatable = false))
@AttributeOverride(name = "createdDate", column = @Column(name = "UG_CREATED_DATE", updatable = false))
@AttributeOverride(name = "lastModifiedBy", column = @Column(name = "UG_LAST_MODIFIED_BY"))
@AttributeOverride(name = "lastModifiedDate", column = @Column(name = "UG_LAST_MODIFIED_DATE"))
@AttributeOverride(name = "active", column = @Column(name = "UG_ACTIVE"))
public class UserGroup extends Auditable<String> {

	@Id
	@Column(name = "UG_ID_A")
	private String code;

	@Column(name = "UG_DESC")
	private String desc;

	@Column(name = "UG_DELETED")
	private boolean deleted;

	@Transient
	private volatile int hashCode;

	public UserGroup(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public UserGroup() {
		this("", "");
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return getCode();
	}

	@Override
	public boolean equals(Object anObject) {
		return anObject instanceof UserGroup && (getCode().equalsIgnoreCase(((UserGroup) anObject).getCode())
			&& getDesc().equalsIgnoreCase(((UserGroup) anObject).getDesc()));
	}

	@Override
	public int hashCode() {
		if (this.hashCode == 0) {
			final int m = 23;
			int c = 133;

			c = m * c + code.hashCode();

			this.hashCode = c;
		}

		return this.hashCode;
	}
}
