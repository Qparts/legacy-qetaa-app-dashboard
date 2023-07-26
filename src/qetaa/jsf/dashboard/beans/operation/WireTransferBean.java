package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.BanksBean;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.WireTransfer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.payment.Bank;
import qetaa.jsf.dashboard.model.payment.PartsPayment;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named(value = "wiretransferBean")
@ViewScoped
public class WireTransferBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private WireTransfer wire;
	private CartReview cartReview;
	private PartsPayment partsPayment;
	private int bankId;

	@Inject
	private Requester reqs;

	@Inject
	private BanksBean banksBean;

	@Inject
	private CitiesBean citiesBean;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart");
			partsPayment = new PartsPayment();
			cartReview = new CartReview();
			initWire(s);
			initParts();
			initCity();
			initPromoCode();
		} catch (Exception ex) {
			Helper.redirect("wire_transfers");
		}
	}

	private void initParts() {
		Response r2 = reqs.getSecuredRequest(AppConstants.getCustomerPartsApprovedITems(wire.getCartId()));
		if(r2.getStatus() == 200) {
			List<ApprovedQuotationItem> approved = r2.readEntity(new GenericType<List<ApprovedQuotationItem>>() {});
			wire.getCart().setApprovedItems(approved);
		}
	}

	private void initPromoCode() {
		if (wire.getCart().getPromotionCode() != null) {
			Response r = reqs.getSecuredRequest(AppConstants.getPromoCode(wire.getCart().getPromotionCode()));
			if (r.getStatus() == 200) {
				wire.getCart().setPromoCodeObject(r.readEntity(PromotionCode.class));
			}
		}
	}


	private void initCity() {
		for (City city : citiesBean.getCities()) {
			if (city.getId() == wire.getCart().getCityId()) {
				wire.getCart().setCity(city);
				break;
			}
		}
	}

	private void initWire(String s) throws Exception {
		wire = new WireTransfer();
		Long cartId = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getWireTransfer(cartId));
		if (r.getStatus() == 200) {
			wire = r.readEntity(WireTransfer.class);
		} else {
			throw new Exception();
		}
	}

	public void deleteWireTransfer() {
		this.wire.setConfirmedBy(loginBean.getUserHolder().getUser().getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UNDO_WIRE_TRANSFER, this.wire);
		if(r.getStatus() == 201) {
			Helper.addInfoMessage("Wire Transfer cancelled");
			init();
		} else {
			Helper.addErrorMessage("Something went wrong");
		}
	}

	public void fundWallet() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("amount", wire.getAmount());
		map.put("bankId", bankId);
		map.put("confirmedBy",loginBean.getUserHolder().getUser().getId());
		map.put("cartId", wire.getCartId());
		map.put("customerId", wire.getCustomerId());
		map.put("customerName", wire.getCustomer().getFullName());
		map.put("wireId", wire.getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_FUND_WALLET, map);
		if(r.getStatus() == 201) {
			Helper.redirect("wire_transfers");
		} else {
			System.out.println(r.getStatus());
			Helper.addErrorMessage("Something went wrong");
		}
	}

	//to be retired
	public void confirmPartsPayment() {
		partsPayment = new PartsPayment(wire);
		prepareBank();
		Response r = reqs.postSecuredRequest(AppConstants.POST_BANK_PARTS_PAYMENT, partsPayment);
		System.out.println("This is dashboard: Response from confirm parts payment " + r.getStatus());
		if (r.getStatus() == 200) {
			wire.setConfirmedBy(loginBean.getUserHolder().getUser().getId());
			// update cart and deactivate payment request
			System.out.println("This is dashboard: calling update wire transfer");
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_CONFIRM_WIRE_TRANSFER, this.wire);
			System.out.println("This is dashboard: Response from update wire transfer: " + r2.getStatus());
			if (r2.getStatus() == 200) {
				Helper.redirect("wire_transfers");
			}
		}
	}

	private void prepareCartReview() {
		cartReview.setStage(4);
		cartReview.setCartId(this.wire.getCartId());
		cartReview.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		cartReview.setStatus(cartReview.getStatusFromActionValue());
	}

	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.cartReview);
		if (r.getStatus() == 200) {
			Helper.redirect("wire-transfer?cart=" + wire.getCartId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void prepareBank() {
		for (Bank b : banksBean.getActiveBanks()) {
			if (b.getBankId() == this.bankId) {
				partsPayment.setBank(b);
				partsPayment.setBankConfirmedBy(this.loginBean.getUserHolder().getUser().getId());
				break;
			}
		}
	}

	public WireTransfer getWire() {
		return wire;
	}

	public void setWire(WireTransfer wire) {
		this.wire = wire;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public CartReview getCartReview() {
		return cartReview;
	}

	public void setCartReview(CartReview cartReview) {
		this.cartReview = cartReview;
	}


}
