package com.alex.myexpenses.entity.expenses;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alex.myexpenses.core.exception.NotAllowedOperationException;
import com.alex.myexpenses.entity.shared.BaseEntity;
import com.alex.myexpenses.entity.user.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class CategoryEntity extends BaseEntity {

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "color", length = 50)
	private String color;
	
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ExpenseEntity> expenses;
	
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = true)
	private UserEntity user;
	
	public void checkActionAllowed() {
	    if (Boolean.TRUE.equals(this.isDefault)) {
	        throw new NotAllowedOperationException("System categories cannot be modified or deleted.");
	    }
	}
	
	public void updateName(String newName) {
	    if (newName == null || newName.trim().isEmpty()) {
	        throw new IllegalArgumentException("Category name cannot be empty");
	    }
	    checkActionAllowed();
	    this.name = newName.trim();
	}
	
	public void initUserCategory(String name, UserEntity user) {
	    this.updateName(name);
	    this.user = user;
	    this.isDefault = false;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    
	    CategoryEntity that = (CategoryEntity) o;
	    
	    return getId() != null && getId().equals(that.getId());
	}
	
	@Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
