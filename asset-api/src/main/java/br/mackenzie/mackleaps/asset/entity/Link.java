package br.mackenzie.mackleaps.asset.entity;

import java.util.Objects;

public class Link {

	private long subject;
	private long object;
	private String associationName;
	private String associationDomain;
	private String domain;
	
	public Link()
	{
	}
	
	public Link(long subject, long object, String associationName, String associationDomain, String domain) {
		this.subject = subject;
		this.object = object;
		this.associationName = associationName;
		this.associationDomain = associationDomain;
		this.domain = domain;
	}

	public static Link.Builder create() {
		return new Link.Builder();
	}

	public long getSubject() {
		return subject;
	}
	public void setSubject(long subject) {
		this.subject = subject;
	}
	public long getObject() {
		return object;
	}
	public void setObject(long object) {
		this.object = object;
	}
	public String getAssociationName() {
		return associationName;
	}
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAssociationDomain() {
		return associationDomain;
	}
	public void setAssociationDomain(String associationDomain) {
		this.associationDomain = associationDomain;
	}


	@Override
	public int hashCode() {
		return Objects.hash(associationDomain, associationName, domain, object, subject);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link other = (Link) obj;
		return Objects.equals(associationDomain, other.associationDomain)
				&& Objects.equals(associationName, other.associationName) && Objects.equals(domain, other.domain)
				&& object == other.object && subject == other.subject;
	}


	@Override
	public String toString() {
		return "Link [subject=" + subject + ", object=" + object + ", associationName=" + associationName
				+ ", associationDomain=" + associationDomain + ", domain=" + domain + "]";
	}


	public static final class Builder {
		private long subject;
		private long object;
		private String associationName;
		private String associationDomain;
		private String domain;

		private Builder() {
		}

		public static Builder aLink() {
			return new Builder();
		}

		public Builder withSubject(long subject) {
			this.subject = subject;
			return this;
		}

		public Builder withObject(long object) {
			this.object = object;
			return this;
		}

		public Builder withAssociationName(String associationName) {
			this.associationName = associationName;
			return this;
		}
		
		public Builder withAssociationDomain(String associationDomain) {
			this.associationDomain = associationDomain;
			return this;
		}
		
		public Builder withDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public Link build() {
			Link link = new Link();
			link.setSubject(subject);
			link.setObject(object);
			link.setAssociationName(associationName);
			link.setAssociationDomain(associationDomain);
			link.setDomain(domain);
			return link;
		}
	}
}
