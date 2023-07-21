package qetaa.jsf.dashboard.beans.sales;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturn;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;

@Named
@ViewScoped
public class SalesReturnBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Sales sales;
	private SalesProduct selectedSalesProduct;
	private SalesReturn salesReturn;
	private SalesReturnProduct salesReturnProduct;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("id");
			initSales(s);
			initCart(sales.getCartId());
			initCartVariables();
			initProducts();
			salesReturn = new SalesReturn();
			salesReturn.setSalesReturnProducts(new ArrayList<>());
			selectedSalesProduct = new SalesProduct();
			salesReturnProduct = new SalesReturnProduct();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("sales-return-search");
		}
	}

	public void createSalesReturn() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SALES_RETURN, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			salesReturn.setId(id);
			salesReturn.setCartId(sales.getCartId());
			salesReturn.setMethod('R');
			salesReturn.setPromotionDiscount(
					salesReturn.getTotalPromotionDiscountDeductionNewQuantity(this.sales.getCart()));
			salesReturn.setReturnBy(loginBean.getUserHolder().getUser().getId());
			salesReturn.setPromotionId(sales.getPromotionId());
			salesReturn.setSales(sales);
			salesReturn.setVatPercentage(sales.getCart().getVatPercentage());

			for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
				srp.setQuantity(srp.getNewQuantity());
				srp.setSalesReturn(null);
			}
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SALES_RETURN, salesReturn);
			if (r2.getStatus() == 201) {
				Helper.redirect("sales-return?id=" + this.sales.getId());
				
			} else {
				Helper.addErrorMessage("could not update sales");
			}

		}
	}

	public void chooseSalesProduct(SalesProduct sp) {
		this.salesReturnProduct = new SalesReturnProduct();
		this.salesReturnProduct.setProductId(sp.getProductId());
		this.salesReturnProduct.setPurchaseProduct(sp.getPurchaseProduct());
		this.salesReturnProduct.setQuantity(sp.getQuantity());
		this.salesReturnProduct.setNewQuantity(sp.getQuantity());
		this.salesReturnProduct.setReturnDeductionFees(0);
		this.salesReturnProduct.setUnitCost(sp.getUnitCost());
		this.salesReturnProduct.setUnitCostWv(sp.getUnitCostWv());
		this.salesReturnProduct.setUnitSales(sp.getUnitSales());
		this.salesReturnProduct.setUnitSalesWv(sp.getUnitSalesWv());
		this.salesReturnProduct.setProduct(sp.getProduct());
		this.selectedSalesProduct = sp;
	}
	
	public boolean isItemsAvailableForReturn() {
		boolean found = false;
		for(SalesProduct sp : this.sales.getSalesProducts()) {
			if(getTotalQuantityReturnedInPreviousOrders(sp) < sp.getQuantity() ) {
				found = true;
				break;
			}
		}
		return found;
	}

	public int getTotalQuantityReturnedInPreviousOrders(SalesProduct salesProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesProduct.getProductId()
						&& srp.getPurchaseProduct().getId() == salesProduct.getPurchaseProduct().getId())
					total = total + srp.getQuantity();
			}
		}
		return total;
	}
	
	public int getTotalQuantityReturnedInPreviousOrders(SalesReturnProduct salesReturnProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesReturnProduct.getProductId()
						&& srp.getPurchaseProduct().getId() == salesReturnProduct.getPurchaseProduct().getId())
					total = total + srp.getQuantity();
			}
		}
		return total;
	}

	public void addToReturnProducts() {
		boolean found = false;
		for (SalesReturnProduct srp : this.salesReturn.getSalesReturnProducts()) {
			if (srp.getProduct().getId() == this.salesReturnProduct.getProduct().getId()
					&& srp.getPurchaseProduct().getId() == salesReturnProduct.getPurchaseProduct().getId()) {
				found = true;
				break;
			}
		}
		// check previous returns
		if (!found) {
			for (SalesReturn sr : this.sales.getSalesReturns()) {
				for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
					if (this.salesReturnProduct.getPurchaseProduct().getId() == srp.getPurchaseProduct().getId()) {
						if (salesReturnProduct.getNewQuantity()
								+ getTotalQuantityReturnedInPreviousOrders(srp) > salesReturnProduct.getQuantity())
							found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}

		if (!found) {
			SalesReturnProduct srp = new SalesReturnProduct();
			srp.setProduct(salesReturnProduct.getProduct());
			srp.setProductId(salesReturnProduct.getProductId());
			srp.setPurchaseProduct(salesReturnProduct.getPurchaseProduct());
			srp.setQuantity(salesReturnProduct.getQuantity());
			srp.setReturnDeductionFees(salesReturnProduct.getReturnDeductionFees());
			srp.setUnitCost(salesReturnProduct.getUnitCost());
			srp.setNewQuantity(salesReturnProduct.getNewQuantity());
			srp.setUnitCostWv(salesReturnProduct.getUnitCostWv());
			srp.setUnitSales(salesReturnProduct.getUnitSales());
			srp.setUnitSalesWv(salesReturnProduct.getUnitSalesWv());
			this.salesReturn.getSalesReturnProducts().add(srp);
		} else {
			Helper.addErrorMessage("Product already added or returned");
		}
	}

	private void initSales(String s) throws Exception {
		Long id = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getSalesFromId(id));
		if (r.getStatus() == 200) {
			sales = r.readEntity(Sales.class);
		} else {
			throw new Exception();
		}
	}

	private void initCart(long cartId) {
		Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
		if (r.getStatus() == 200) {
			sales.setCart(r.readEntity(Cart.class));
		}
	}

	private void initProducts() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[sales.getSalesProducts().size()];
		int index = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			sp.setProduct(new Product());
			threads[index] = ThreadRunner.initProduct(sp, header);
			index++;
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[4];
		threads[0] = ThreadRunner.initReviews(sales.getCart(), header);
		threads[1] = ThreadRunner.initCity(sales.getCart(), header);
		threads[2] = ThreadRunner.initPromoCode(sales.getCart(), header);
		threads[3] = ThreadRunner.initAddress(sales.getCart(), header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public SalesProduct getSelectedSalesProduct() {
		return selectedSalesProduct;
	}

	public void setSelectedSalesProduct(SalesProduct selectedSalesProduct) {
		this.selectedSalesProduct = selectedSalesProduct;
	}

	public SalesReturn getSalesReturn() {
		return salesReturn;
	}

	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}

	public SalesReturnProduct getSalesReturnProduct() {
		return salesReturnProduct;
	}

	public void setSalesReturnProduct(SalesReturnProduct salesReturnProduct) {
		this.salesReturnProduct = salesReturnProduct;
	}

}
