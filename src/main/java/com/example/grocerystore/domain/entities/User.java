package com.example.grocerystore.domain.entities;

import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails 
{
    private String username;
    private String password;
    private String email;
    //private String address;
    private Set<Role> authorities;
    private boolean isDeleted;
    private boolean enabled;
   
  //  @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
  //  private List<Feedback> feedbacks  = new ArrayList<>();
    
    public User() {
        this.authorities = new HashSet<>();
    }

    @Override
    @Column(name = "username", nullable = false, unique = true)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address")
    @ColumnDefault(" ")
    private String address;
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
   
   @Column(name = "reset_password_token")
   @ColumnDefault(" ")
   private String resetPasswordToken;
   
    public String getResetPasswordToken() {
        return this.resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken= resetPasswordToken;
    }
    @Override
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    
    public boolean isEnable() {
        return this.enabled;
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

	/*
	 * public List<Feedback> getFeedbacks() { return feedbacks; }
	 * 
	 * public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks =
	 * feedbacks; }
	 */
    
}