package br.mackenzie.mackleaps.asset.entity;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetType {

	private String name;
	private String status;
	private String domain;
	private Map<String,String> attributesLabel;
	private Map<String,String> attributesType;
	private String icon;
	private String icon64;
	private String i18nCreateUserLabel;
	private String i18nDateCreatedLabel;
	private String i18nDescriptionLabel;
	
	private String i18nIdLabel;
	private String i18nLastUpdatedLabel;
	
	private String i18nNameLabel;
	private String i18nStatusLabel;
	private String i18nUpdateUserLabel;

	public AssetType() {
		attributesLabel = new HashMap<>();
		attributesType = new HashMap<>();
	}

	public static AssetType.Builder create() {
		return new AssetType.Builder();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public Map<String,String> getAttributesLabel() {
		return attributesLabel;
	}

	public void setAttributesLabel(Map<String,String> attributesLabel) {
		this.attributesLabel = attributesLabel;
	}
	
	public Map<String,String> getAttributesType() {
		return attributesType;
	}

	public void setAttributesType(Map<String,String> attributesType) {
		this.attributesType = attributesType;
	}

	public String getI18nCreateUserLabel() {
		return i18nCreateUserLabel;
	}

	public void setI18nCreateUserLabel(String i18nCreateUserLabel) {
		this.i18nCreateUserLabel = i18nCreateUserLabel;
	}

	public String getI18nDateCreatedLabel() {
		return i18nDateCreatedLabel;
	}

	public void setI18nDateCreatedLabel(String i18nDateCreatedLabel) {
		this.i18nDateCreatedLabel = i18nDateCreatedLabel;
	}

	public String getI18nDescriptionLabel() {
		return i18nDescriptionLabel;
	}

	public void setI18nDescriptionLabel(String i18nDescriptionLabel) {
		this.i18nDescriptionLabel = i18nDescriptionLabel;
	}

	public String getI18nIdLabel() {
		return i18nIdLabel;
	}

	public void setI18nIdLabel(String i18nIdLabel) {
		this.i18nIdLabel = i18nIdLabel;
	}

	public String getI18nLastUpdatedLabel() {
		return i18nLastUpdatedLabel;
	}

	public void setI18nLastUpdatedLabel(String i18nLastUpdatedLabel) {
		this.i18nLastUpdatedLabel = i18nLastUpdatedLabel;
	}

	public String getI18nNameLabel() {
		return i18nNameLabel;
	}

	public void setI18nNameLabel(String i18nNameLabel) {
		this.i18nNameLabel = i18nNameLabel;
	}

	public String getI18nStatusLabel() {
		return i18nStatusLabel;
	}

	public void setI18nStatusLabel(String i18nStatusLabel) {
		this.i18nStatusLabel = i18nStatusLabel;
	}

	public String getI18nUpdateUserLabel() {
		return i18nUpdateUserLabel;
	}

	public void setI18nUpdateUserLabel(String i18nUpdateUserLabel) {
		this.i18nUpdateUserLabel = i18nUpdateUserLabel;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon64() {
		return icon64;
	}

	public void setIcon64(String icon64) {
		this.icon64 = icon64;
	}

	@Override
	public String toString() {
		return "AssetType [name=" + name + ", status=" + status + ", domain=" + domain + ", attributesLabel=" + attributesLabel
				+ ", attributesType=" + attributesType + ", icon=" + icon + ", icon64=" + icon64
				+ ", i18nCreateUserLabel=" + i18nCreateUserLabel + ", i18nDateCreatedLabel=" + i18nDateCreatedLabel
				+ ", i18nDescriptionLabel=" + i18nDescriptionLabel + ", i18nIdLabel=" + i18nIdLabel
				+ ", i18nLastUpdatedLabel=" + i18nLastUpdatedLabel + ", i18nNameLabel=" + i18nNameLabel
				+ ", i18nStatusLabel=" + i18nStatusLabel + ", i18nUpdateUserLabel=" + i18nUpdateUserLabel + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(attributesLabel, attributesType, domain, i18nCreateUserLabel, i18nDateCreatedLabel,
				i18nDescriptionLabel, i18nIdLabel, i18nLastUpdatedLabel, i18nNameLabel, i18nStatusLabel,
				i18nUpdateUserLabel, icon, icon64, name, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetType other = (AssetType) obj;
		return Objects.equals(attributesLabel, other.attributesLabel)
				&& Objects.equals(attributesType, other.attributesType)
				&& Objects.equals(domain, other.domain)
				&& Objects.equals(i18nCreateUserLabel, other.i18nCreateUserLabel)
				&& Objects.equals(i18nDateCreatedLabel, other.i18nDateCreatedLabel)
				&& Objects.equals(i18nDescriptionLabel, other.i18nDescriptionLabel)
				&& Objects.equals(i18nIdLabel, other.i18nIdLabel)
				&& Objects.equals(i18nLastUpdatedLabel, other.i18nLastUpdatedLabel)
				&& Objects.equals(i18nNameLabel, other.i18nNameLabel)
				&& Objects.equals(i18nStatusLabel, other.i18nStatusLabel)
				&& Objects.equals(i18nUpdateUserLabel, other.i18nUpdateUserLabel)
				&& Objects.equals(icon, other.icon)
				&& Objects.equals(icon64, other.icon64)
				&& Objects.equals(name, other.name)
				&& Objects.equals(status, other.status);
	}


	public static final class Builder {
		private String name;
		private String status;
		private Map<String,String> attributesLabel;
		private Map<String,String> attributesType;
		private String icon;
		private String icon64;
		private String i18nCreateUserLabel;
		private String i18nDateCreatedLabel;
		private String i18nDescriptionLabel;
		private String i18nIdLabel;
		private String i18nLastUpdatedLabel;
		private String i18nNameLabel;
		private String i18nStatusLabel;
		private String i18nUpdateUserLabel;
		private String domain;

		private Builder() {
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withStatus(String status) {
			this.status = status;
			return this;
		}
		
		public Builder withDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public Builder withAttributesLabel(Map<String, String> attributesLabel) {
			this.attributesLabel = attributesLabel;
			return this;
		}

		public Builder withAttributesType(Map<String, String> attributesType) {
			this.attributesType = attributesType;
			return this;
		}

		public Builder withIcon(String icon) {
			this.icon = icon;
			return this;
		}

		public Builder withIcon64(String icon64) {
			this.icon64 = icon64;
			return this;
		}

		public Builder withI18nCreateUserLabel(String i18nCreateUserLabel) {
			this.i18nCreateUserLabel = i18nCreateUserLabel;
			return this;
		}

		public Builder withI18nDateCreatedLabel(String i18nDateCreatedLabel) {
			this.i18nDateCreatedLabel = i18nDateCreatedLabel;
			return this;
		}

		public Builder withI18nDescriptionLabel(String i18nDescriptionLabel) {
			this.i18nDescriptionLabel = i18nDescriptionLabel;
			return this;
		}

		public Builder withI18nIdLabel(String i18nIdLabel) {
			this.i18nIdLabel = i18nIdLabel;
			return this;
		}

		public Builder withI18nLastUpdatedLabel(String i18nLastUpdatedLabel) {
			this.i18nLastUpdatedLabel = i18nLastUpdatedLabel;
			return this;
		}

		public Builder withI18nNameLabel(String i18nNameLabel) {
			this.i18nNameLabel = i18nNameLabel;
			return this;
		}

		public Builder withI18nStatusLabel(String i18nStatusLabel) {
			this.i18nStatusLabel = i18nStatusLabel;
			return this;
		}

		public Builder withI18nUpdateUserLabel(String i18nUpdateUserLabel) {
			this.i18nUpdateUserLabel = i18nUpdateUserLabel;
			return this;
		}

		public AssetType build() {
			AssetType assetType = new AssetType();
			assetType.setName(name);
			assetType.setStatus(status);
			assetType.setDomain(domain);
			assetType.setAttributesLabel(attributesLabel);
			assetType.setAttributesType(attributesType);
			assetType.setIcon(icon);
			assetType.setIcon64(icon64);
			assetType.setI18nCreateUserLabel(i18nCreateUserLabel);
			assetType.setI18nDateCreatedLabel(i18nDateCreatedLabel);
			assetType.setI18nDescriptionLabel(i18nDescriptionLabel);
			assetType.setI18nIdLabel(i18nIdLabel);
			assetType.setI18nLastUpdatedLabel(i18nLastUpdatedLabel);
			assetType.setI18nNameLabel(i18nNameLabel);
			assetType.setI18nStatusLabel(i18nStatusLabel);
			assetType.setI18nUpdateUserLabel(i18nUpdateUserLabel);
			return assetType;
		}
	}
}
