package qetaa.jsf.dashboard.model.product.meta;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Date created;
	private int createdBy;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
}
