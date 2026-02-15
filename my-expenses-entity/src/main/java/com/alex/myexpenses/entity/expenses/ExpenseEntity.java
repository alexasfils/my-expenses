package com.alex.myexpenses.entity.expenses;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alex.myexpenses.entity.shared.BaseEntity;
import com.alex.myexpenses.entity.user.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expenses")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class ExpenseEntity extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6626933298806944820L;

	
	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "expense_date", nullable = false)
	private LocalDate expenseDate;
	
	@Column(name = "amount", length = 50, nullable = false)
	private Double amount;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@ManyToOne
    @JoinColumn(name = "id_expense_list")
	private ExpenseListEntity expenseList;
	
	@ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
	private CategoryEntity category;
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    
	    ExpenseEntity that = (ExpenseEntity) o;
	    
	    return getId() != null && getId().equals(that.getId());
	}
	
	@Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
