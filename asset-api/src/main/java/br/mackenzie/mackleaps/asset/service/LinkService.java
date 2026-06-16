package br.mackenzie.mackleaps.asset.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.Association;
import br.mackenzie.mackleaps.asset.entity.AssociationType;
import br.mackenzie.mackleaps.asset.entity.Link;
import br.mackenzie.mackleaps.asset.persistence.AssetDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.AssociationDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.AssociationTypeDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.LinkDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.dao.AssetDAO;

@Service
public class LinkService {

	

	public Map<Asset, List<Asset>> getAssociations(String associationTypeName, String subjectType, String objectType,
			String domain) throws SQLException {
		Map<Asset, List<Asset>> finalAssociations = new HashMap<>();
		Map<Long, List<Long>> associations = LinkDAOSingleton.getInstance().findByAssociation(associationTypeName,
				domain);
		for (Entry<Long, List<Long>> entry : associations.entrySet()) {
			Long subId = entry.getKey();
			Asset subject = AssetDAOSingleton.getInstance().findById(subId);
			if (subject.getAssetTypeName().equals(subjectType)) {
				List<Asset> assets = new ArrayList<>();
				for (Long id : entry.getValue()) {
					Asset object = AssetDAOSingleton.getInstance().findById(id);
					if (object.getAssetTypeName().equals(objectType)) {
						assets.add(object);
					}
				}
				finalAssociations.put(subject, assets);
			}
		}
		return finalAssociations;
	}

	public Link insertLink(Link link) throws SQLException {
		Link newLink = null;
		Asset subjectAsset = AssetDAOSingleton.getInstance().findById(link.getSubject());
		Asset objectAsset = AssetDAOSingleton.getInstance().findById(link.getObject());
		AssociationType associationType = AssociationTypeDAOSingleton.getInstance()
				.findByName(link.getAssociationName(), link.getAssociationDomain());
		if (subjectAsset != null && objectAsset != null && associationType != null) {
			List<Association> associations = AssociationDAOSingleton.getInstance().findByDomain(link.getAssociationDomain());
			Asset assetObject = AssetDAOSingleton.getInstance().findById(link.getObject());
			Asset assetSubject = AssetDAOSingleton.getInstance().findById(link.getSubject());
			for (Association association : associations) {
				if (association.getAssociationTypeName().equals(link.getAssociationName())
						&& association.getObjectType().equals(assetObject.getAssetTypeName())
						&& association.getSubjectType().equals(assetSubject.getAssetTypeName())) {
					newLink = LinkDAOSingleton.getInstance().insertLink(link);
					break;
				}
			}
		}
		return newLink;
	}

	public List<Asset> getObject(long subjectId, String associationName, String assetTypeName, String assetTypeDomain, String domain)
			throws SQLException {
		List<Asset> assetsAssetType = new ArrayList<>();
		List<Long> assets = LinkDAOSingleton.getInstance().findObject(subjectId, associationName, domain);
		for (Long id : assets) {
			Asset asset = AssetDAOSingleton.getInstance().findById(id);
			if (asset.getAssetTypeName().equals(assetTypeName) && asset.getAssetTypeDomain().equals(assetTypeDomain)) {
				assetsAssetType.add(asset);
			}
		}
		return assetsAssetType;
	}

	public List<Asset> getSubject(long objectId, String associationName, String assetTypeName, String assetTypeDomain, String domain)
			throws SQLException {
		List<Asset> assetsAssetType = new ArrayList<>();
		List<Long> assets = LinkDAOSingleton.getInstance().findSubject(objectId, associationName, domain);
		for (Long id : assets) {
			Asset asset = AssetDAOSingleton.getInstance().findById(id);
			if (asset != null && assetTypeName.equals(asset.getAssetTypeName())
					&& assetTypeDomain.equals(asset.getAssetTypeDomain())
					&& !AssetDAO.STATUS_DELETED.equals(asset.getStatus())) {
				assetsAssetType.add(asset);
			}
		}
		return assetsAssetType;
	}

	public JSONObject getAssociation(long objectId, long subjectId, String associationTypeName)
			throws SQLException {
		Asset object = AssetDAOSingleton.getInstance().findById(objectId);
		Asset subject = AssetDAOSingleton.getInstance().findById(subjectId);
		if (object == null || subject == null) {
			return new JSONObject();
		}

		JSONObject json = new JSONObject();
		json.put("object", object.toJSON());
		json.put("associationType", associationTypeName);
		json.put("subject", subject.toJSON());
		return json;
	}

	public List<Asset> getObjectAssociation(long subjectId, String associationTypeName, String domain)
			throws SQLException {
		List<Asset> assets = new ArrayList<>();
		List<Long> objects = LinkDAOSingleton.getInstance().findObject(subjectId, associationTypeName, domain);
		for (Long id : objects) {
			assets.add(AssetDAOSingleton.getInstance().findById(id));
		}
		return assets;
	}

	public List<Asset> getObjectsFromSubjects(String subjects, String associationName, String assetType, String assetTypeDomain, String domain)
			throws SQLException {
		List<Asset> allAssets = new ArrayList<>();
		JSONArray subs = new JSONArray(subjects);
		for (int i = 0; i < subs.length(); i++) {
			allAssets.addAll(getObject(subs.getJSONObject(i).getLong("id"), associationName, assetType, assetTypeDomain, domain));
		}
		return allAssets;
	}

	public List<Asset> getSubjectAssociation(long objectId, String associationTypeName, String domain)
			throws SQLException {
		List<Asset> assets = new ArrayList<>();
		List<Long> subjects = LinkDAOSingleton.getInstance().findSubject(objectId, associationTypeName, domain);
		for (Long id : subjects) {
			assets.add(AssetDAOSingleton.getInstance().findById(id));
		}
		return assets;
	}

	public Map<String, List<Asset>> getObjectAssociation(long subjectId, String domain) throws SQLException {
		Map<String, List<Long>> objects = LinkDAOSingleton.getInstance().findObject(subjectId, domain);
		Map<String, List<Asset>> assets = new HashMap<>();
		for (Entry<String, List<Long>> entry : objects.entrySet()) {
			List<Asset> assetsList = new ArrayList<>();
			for (Long assetId : entry.getValue()) {
				Asset assetToBeAdded = (AssetDAOSingleton.getInstance().findById(assetId));
				if (assetToBeAdded != null)
					assetsList.add(assetToBeAdded);
			}
			assets.put(entry.getKey(), assetsList);
		}
		return assets;
	}

	public Map<String, List<Asset>> getSubjectAssociation(long objectId, String domain) throws SQLException {
		Map<String, List<Long>> objects = LinkDAOSingleton.getInstance().findSubject(objectId, domain);
		Map<String, List<Asset>> assets = new HashMap<>();
		for (Entry<String, List<Long>> entry : objects.entrySet()) {
			List<Asset> assetsList = new ArrayList<>();
			for (Long assetId : entry.getValue()) {
				Asset assetToBeAdded = (AssetDAOSingleton.getInstance().findById(assetId));
				if (assetToBeAdded != null)
					assetsList.add(assetToBeAdded);
			}
			assets.put(entry.getKey(), assetsList);
		}
		return assets;
	}

	public List<Link> findAll() throws SQLException {
		return LinkDAOSingleton.getInstance().findAll();
	}

	public JSONArray findAllWithName(String domain) throws SQLException {
		JSONArray links = new JSONArray();
		JSONObject linkObj;
		Asset subjectAsset;
		Asset objectAsset;
		List<Link> linksList = LinkDAOSingleton.getInstance().findAll();
		for (Link link : linksList) {
			if(!(link.getDomain().equals(domain))) {
				continue;
			}
			subjectAsset = AssetDAOSingleton.getInstance().findById(link.getSubject());
			objectAsset = AssetDAOSingleton.getInstance().findById(link.getObject());
			linkObj = new JSONObject();
			linkObj.put("subjectId", subjectAsset.getId());
			linkObj.put("subjectName", subjectAsset.getName());
			linkObj.put("objectId", objectAsset.getId());
			linkObj.put("objectName", objectAsset.getName());
			linkObj.put("associationName", link.getAssociationName());
			linkObj.put("associationDomain", link.getAssociationDomain());
			linkObj.put("domain", link.getDomain());
			links.put(linkObj);
		}
		return links;
	}

	public List<Link> findByDomain(String domain) throws SQLException {
		return LinkDAOSingleton.getInstance().findByDomain(domain);
	}

	public boolean deleteAssociation(long subjectId, long objectId, String associationName, String associationDomain, String domain)
			throws SQLException {
		LinkDAOSingleton.getInstance().delete(new Link(subjectId, objectId, associationName, associationDomain, domain));
		return true;
	}
	
	
}
