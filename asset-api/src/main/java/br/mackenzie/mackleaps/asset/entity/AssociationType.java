package br.mackenzie.mackleaps.asset.entity;

import java.util.Objects;

public class AssociationType {

	private String name;
	private String description;
	private String domain;

	public AssociationType() {
	}

	public AssociationType(String name, String description, String domain) {
		super();
		this.name = name;
		this.description = description;
		this.domain = domain;
	}

	public static AssociationType.Builder create() {
		return new AssociationType.Builder();
	}

	public String getName() {
		return name;
	}

	public void setId(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, domain, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationType other = (AssociationType) obj;
		return Objects.equals(description, other.description) && Objects.equals(domain, other.domain)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "AssociationType [name=" + name + ", description=" + description + ", domain=" + domain + "]";
	}



	public static final class Builder {
		private String name;
		private String description;
		private String domain;

		private Builder() {
		}

		public static Builder anAssociationType() {
			return new Builder();
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Builder withDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public AssociationType build() {
			return new AssociationType(name, description, domain);
		}
	}
}
