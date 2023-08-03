package qetaa.jsf.dashboard.beans.product;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;

@Named
@ViewScoped
public class ProductDetailsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Product product;
	private boolean editDetails;
	
	private List<PurchaseProduct> purchaseProducts;
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		try {
			editDetails = false;
			String s = Helper.getParam("id");
			product = new Product();
			initProduct(s);
			initPurchases();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	private void initPurchases() {
		Response r = reqs.getSecuredRequest(AppConstants.getProductPurchases(product.getId()));
		if(r.getStatus() == 200) {
			this.purchaseProducts = r.readEntity(new GenericType<List<PurchaseProduct>>() {});
		}
	}
	
	
	private void initProduct(String param) throws Exception {
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getProductFull(id));
		if (r.getStatus() == 200) {
			product = r.readEntity(Product.class);
		} else {
			throw new Exception();
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


	public List<PurchaseProduct> getPurchaseProducts() {
		return purchaseProducts;
	}


	public void setPurchaseProducts(List<PurchaseProduct> purchaseProducts) {
		this.purchaseProducts = purchaseProducts;
	}


	public boolean isEditDetails() {
		return editDetails;
	}

	public void setEditDetails(boolean editDetails) {
		this.editDetails = editDetails;
	}

	
	
}
