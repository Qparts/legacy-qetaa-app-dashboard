package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.Date;

public class Wallet implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private char walletType;//Payment , Refund
	private long customerId;
	private String customerName;
	private long cartId;
	private char status;//A = Awaiting sales, S = Sales Made 	
	private Date created;
	private String gateway;//Moyassar
	private String transactionId;//
	private String currency;
	private String paymentType;// creditcard, sadad, wire transfer
	private double amount;// in halalas
	private String ccCompany;// visa, mastercard
	private Bank bank;
	private Integer bankConfirmedBy;//user
	private Double creditFees;//credit fees

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public char getWalletType() {
		return walletType;
	}

	public void setWalletType(char walletType) {
		this.walletType = walletType;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCcCompany() {
		return ccCompany;
	}

	public void setCcCompany(String ccCompany) {
		this.ccCompany = ccCompany;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Integer getBankConfirmedBy() {
		return bankConfirmedBy;
	}

	public void setBankConfirmedBy(Integer bankConfirmedBy) {
		this.bankConfirmedBy = bankConfirmedBy;
	}

	public Double getCreditFees() {
		return creditFees;
	}

	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}
	
}
