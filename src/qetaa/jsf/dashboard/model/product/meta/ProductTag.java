package qetaa.jsf.dashboard.model.product.meta;

import java.io.Serializable;
import java.util.Date;

public class ProductTag implements Serializable {

	private static final long serialVersionUID = 1L;	
	private Tag tag;
	private long productId;
	private Date created;
	private int createdBy;
	
	public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
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
	
	
	public static class ProductTagPK implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected long tag;
		protected long productId;
		
		public ProductTagPK() {}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (productId ^ (productId >>> 32));
			result = prime * result + (int) (tag ^ (tag>>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProductTagPK other = (ProductTagPK) obj;
			if (productId != other.productId)
				return false;
			if (tag != other.tag)
				return false;
			return true;
		}		
		
		
	}
}
