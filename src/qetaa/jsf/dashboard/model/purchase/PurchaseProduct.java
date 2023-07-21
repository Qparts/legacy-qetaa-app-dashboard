package qetaa.jsf.dashboard.model.purchase;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;

public class PurchaseProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;	
	private int quantity;
	private Purchase purchase;
	private double unitCost;
	private double unitCostWv;
	@JsonIgnore
	private Product product;
	@JsonIgnore
	private int newQuantity;
	
	
	@JsonIgnore
	public int getQuantityArray(int stockQuantity) []{
		int[] quantityArray = new int[quantity - stockQuantity];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
		return quantityArray;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
	public double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	public double getUnitCostWv() {
		return unitCostWv;
	}
	public void setUnitCostWv(double unitCostWv) {
		this.unitCostWv = unitCostWv;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public int getNewQuantity() {
		return newQuantity;
	}

	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	
	
	
	
}
