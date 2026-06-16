package br.mackenzie.mackleaps.asset.entity;

import java.util.Objects;

public class Association {

	private String subjectType;
	private String objectType;
	private String associationTypeName;
	private String domain;

	public Association() {
	}

	public Association(String subjectType, String objectType, String associationTypeName, String domain) {
		this.subjectType = subjectType;
		this.objectType = objectType;
		this.associationTypeName = associationTypeName;
		this.domain = domain;
	}

	public static Association.Builder create() {
		return new Association.Builder();
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getAssociationTypeName() {
		return associationTypeName;
	}

	public void setAssociationTypeName(String associationTypeName) {
		this.associationTypeName = associationTypeName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "Association [subjectType=" + subjectType + ", objectType=" + objectType
				+ ", associationTypeName=" + associationTypeName + ", domain=" + domain + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(subjectType, objectType, associationTypeName, domain);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Association other = (Association) obj;
		return Objects.equals(subjectType, other.subjectType)
				&& Objects.equals(objectType, other.objectType)
				&& Objects.equals(associationTypeName, other.associationTypeName)
				&& Objects.equals(domain, other.domain);
	}

	public static final class Builder {
		private String subjectType;
		private String objectType;
		private String associationTypeName;
		private String domain;

		private Builder() {
		}

		public static Builder anAssociation() {
			return new Builder();
		}

		public Builder withSubjectType(String subjectType) {
			this.subjectType = subjectType;
			return this;
		}

		public Builder withObjectType(String objectType) {
			this.objectType = objectType;
			return this;
		}

		public Builder withAssociationTypeName(String associationTypeName) {
			this.associationTypeName = associationTypeName;
			return this;
		}

		public Builder withDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public Association build() {
			Association association = new Association();
			association.setSubjectType(subjectType);
			association.setObjectType(objectType);
			association.setAssociationTypeName(associationTypeName);
			association.setDomain(domain);
			return association;
		}
	}
}
