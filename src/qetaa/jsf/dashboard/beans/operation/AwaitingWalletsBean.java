package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.payment.Wallet;

@Named
@ViewScoped
public class AwaitingWalletsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Wallet> wallets;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		wallets = new ArrayList<>();
		initWallets();
	}
	
	private void initWallets() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_AWAITING_WALLETS);
		if(r.getStatus() == 200) {
			wallets = r.readEntity(new GenericType<List<Wallet>>() {});
		}
	}

	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
	
	
	
	
	
}
