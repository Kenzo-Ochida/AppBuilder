package br.mackenzie.mackleaps.asset.entity;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;


public class Asset {

		private long id;
		private String description;	
		private String name;	
		private String url;
		private String status;
		private int version;
		private Timestamp dateCreated;
		private Timestamp lastUpdated;
		private String assetTypeName;
		private String assetTypeDomain;
		private Map<String,String> attributes;
		private String createUser;
		private String updateUser;
		private String icon;
		private String icon64;
		private String domain;
		

		public Asset() {
			attributes = new HashMap<>();
		}
		
		public static Asset.Builder create() {
			return new Asset.Builder();
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public Timestamp getDateCreated() {
			return dateCreated;
		}

		public void setDateCreated(Timestamp dateCreated) {
			this.dateCreated = dateCreated;
		}

		public Timestamp getLastUpdated() {
			return lastUpdated;
		}

		public void setLastUpdated(Timestamp lastUpdated) {
			this.lastUpdated = lastUpdated;
		}

		public String getAssetTypeName() {
			return assetTypeName;
		}

		public void setAssetTypeName(String assetTypeName) {
			this.assetTypeName = assetTypeName;
		}

		public String getAssetTypeDomain() {
			return assetTypeDomain;
		}

		public void setAssetTypeDomain(String assetTypeDomain) {
			this.assetTypeDomain = assetTypeDomain;
		}

		public Map<String, String> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public String getUpdateUser() {
			return updateUser;
		}

		public void setUpdateUser(String updateUser) {
			this.updateUser = updateUser;
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

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		@Override
		public int hashCode() {
			return Objects.hash(assetTypeDomain, assetTypeName, attributes, createUser, dateCreated, description, domain,
					icon, icon64, id, lastUpdated, name, status, updateUser, url, version);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Asset other = (Asset) obj;
			return Objects.equals(assetTypeDomain, other.assetTypeDomain)
					&& Objects.equals(assetTypeName, other.assetTypeName) && Objects.equals(attributes, other.attributes)
					&& Objects.equals(createUser, other.createUser) && Objects.equals(dateCreated, other.dateCreated)
					&& Objects.equals(description, other.description) && Objects.equals(domain, other.domain)
					&& Objects.equals(icon, other.icon) && Objects.equals(icon64, other.icon64) && id == other.id
					&& Objects.equals(lastUpdated, other.lastUpdated) && Objects.equals(name, other.name)
					&& Objects.equals(status, other.status) && Objects.equals(updateUser, other.updateUser)
					&& Objects.equals(url, other.url) && version == other.version;
		}

		@Override
		public String toString() {
			return "Asset [id=" + id + ", description=" + description + ", name=" + name + ", url=" + url + ", status="
					+ status + ", version=" + version + ", dateCreated=" + dateCreated + ", lastUpdated=" + lastUpdated
					+ ", assetTypeName=" + assetTypeName + ", assetTypeDomain=" + assetTypeDomain + ", attributes="
					+ attributes + ", createUser=" + createUser + ", updateUser=" + updateUser + ", icon=" + icon
					+ ", icon64=" + icon64 + ", domain=" + domain + "]";
		}

		public static final class Builder {
			private long id;
			private String description;
			private String name;
			private String url;
			private String status;
			private int version;
			private Timestamp dateCreated;
			private Timestamp lastUpdated;
			private String assetTypeName;
			private String assetTypeDomain;
			private Map<String,String> attributes;
			private String createUser;
			private String updateUser;
			private String icon;
			private String icon64;
			private String domain;

			private Builder() {
			}

			public static Builder anAsset() {
				return new Builder();
			}

			public Builder withId(long id) {
				this.id = id;
				return this;
			}

			public Builder withDescription(String description) {
				this.description = description;
				return this;
			}

			public Builder withName(String name) {
				this.name = name;
				return this;
			}

			public Builder withUrl(String url) {
				this.url = url;
				return this;
			}

			public Builder withStatus(String status) {
				this.status = status;
				return this;
			}

			public Builder withVersion(int version) {
				this.version = version;
				return this;
			}

			public Builder withDateCreated(Timestamp dateCreated) {
				this.dateCreated = dateCreated;
				return this;
			}

			public Builder withLastUpdated(Timestamp lastUpdated) {
				this.lastUpdated = lastUpdated;
				return this;
			}

			public Builder withAssetTypeName(String assetTypeName) {
				this.assetTypeName = assetTypeName;
				return this;
			}
			
			public Builder withAssetTypeDomain(String assetTypeDomain) {
				this.assetTypeDomain = assetTypeDomain;
				return this;
			}

			public Builder withAttributes(Map<String, String> attributes) {
				this.attributes = attributes;
				return this;
			}

			public Builder withCreateUser(String createUser) {
				this.createUser = createUser;
				return this;
			}

			public Builder withUpdateUser(String updateUser) {
				this.updateUser = updateUser;
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

			public Builder withDomain(String domain) {
				this.domain = domain;
				return this;
			}

			public Asset build() {
				Asset asset = new Asset();
				asset.setId(id);
				asset.setDescription(description);
				asset.setName(name);
				asset.setUrl(url);
				asset.setStatus(status);
				asset.setVersion(version);
				asset.setDateCreated(dateCreated);
				asset.setLastUpdated(lastUpdated);
				asset.setAssetTypeName(assetTypeName);
				asset.setAssetTypeDomain(assetTypeDomain);
				asset.setAttributes(attributes);
				asset.setCreateUser(createUser);
				asset.setUpdateUser(updateUser);
				asset.setIcon(icon);
				asset.setIcon64(icon64);
				asset.setDomain(domain);
				return asset;
			}
		}


		public JSONObject toJSON() {
			return new JSONObject().put("id", this.getId())
					.put("description", this.getDescription())
					.put("name", this.getName())
					.put("url", this.getUrl())
					.put("status", this.getStatus())
					.put("version", this.getVersion())
					.put("dateCreated", this.getDateCreated())
					.put("lastUpdated", this.getLastUpdated())
					.put("assetType", this.getAssetTypeName())
					.put("assetTypeDomain",this.getAssetTypeDomain())
					.put("attributes", this.getAttributes())
					.put("createUser", this.getCreateUser())
					.put("updateUser", this.getUpdateUser())
					.put("icon", this.getIcon())
					.put("icon64", this.getIcon64())
					.put("domain", this.getDomain());
		}
}
