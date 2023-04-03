package com.example.grocerystore.domain.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.grocerystore.domain.models.service.UserServiceModel;

@Entity
@Table(name = "feedbacks")
public class Feedback extends BaseEntity
{
	private String ftype;
	private String fdesc;
	private User customer;
	
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	public String getFdesc() {
		return fdesc;
	}
	public void setFdesc(String fdesc) {
		this.fdesc = fdesc;
	}
	
	@ManyToOne(targetEntity = User.class)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id"
    )
    public User getCustomer() {
        return customer;
    }

	public void setCustomer(User currentLoggedUser) 
	{
		this.customer = currentLoggedUser;
	}
	
	@Override
	public String toString() {
		return "Feedback [ftype=" + ftype + ", fdesc=" + fdesc + ", customer=" + customer + "]";
	}
}
