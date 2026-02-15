package com.alex.myexpenses.entity.expenses;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alex.myexpenses.core.exception.NotAllowedOperationException;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.entity.shared.BaseEntity;
import com.alex.myexpenses.entity.user.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expense_list")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class ExpenseListEntity extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9060366669952128072L;
	

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "month", nullable = false)
	private Integer month;
	
	@Column(name = "budget", nullable = false)
	private Double budget;
	
	@Column(name = "total_expense", nullable = false)
	private Double totalExpense;
	
	@ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
	private UserEntity user;
	
	@OneToMany(mappedBy = "expenseList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ExpenseEntity> expenses;
	
	public void updateName(String newName) {
	    if (newName == null || newName.trim().isEmpty()) {
	        throw new IllegalArgumentException("Expense list name is required");
	    }
	    this.name = newName.trim();
	}
	
	public void updateBudget(Double budget) {
	    if (budget != null && budget < 0) {
	        throw new IllegalArgumentException("Budget cannot be negative");
	    }
	    this.budget = (budget != null) ? budget : 0.0;
	}
	
	public void updateMonth(Integer month) {
		if(month == null) {
			throw new IllegalArgumentException("Month is mandadory");
		}
		if(month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be between 1 and 12");
		}
		this.month = month;
	}
	
	public void initUserExpenseList(String name, Integer month, Double budget, UserEntity user) {
		this.setId(null);
	    this.updateName(name);
	    updateMonth(month);
	    updateBudget(budget);
	    this.user = user;
	    this.totalExpense = 0.0;
	}
	
	public void updateExpenseList(String name, Integer month, Double budget) {
		this.updateName(name);
	    updateMonth(month);
	    updateBudget(budget);
	}

//ExpenseServoce Section
	public void addAmount(Double amount) {
	    if (amount == null) return;
	    if (this.totalExpense == null) this.totalExpense = 0.0;
	    
	    this.totalExpense += amount;
	}
	
	public void updateBalance(Double oldAmount, Double newAmount) {
		if(this.totalExpense == null) this.totalExpense = 0.0;
		
		this.totalExpense = this.totalExpense - oldAmount + newAmount;
		if( this.totalExpense < 0) this.totalExpense = 0.0;
	}
	
	public void removeExpenseFromTotal(Double amount) {
	    if (this.totalExpense == null) return;
	    this.totalExpense -= amount;
	    if (this.totalExpense < 0) this.totalExpense = 0.0;
	}
	
	public void resetTotal() {
	    this.totalExpense = 0.0;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    
	    ExpenseListEntity that = (ExpenseListEntity) o;
	    
	    return getId() != null && getId().equals(that.getId());
	}
	
	@Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
