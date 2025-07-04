package com.alex.myexpenses.entity.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.alex.myexpenses.entity.expenses.ExpenseListEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6335395923369247489L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;
	@Column(name = "surname", length = 50, nullable = false)
	private String surname;
	@Column(name = "email",  length = 100, nullable = false)
	private String email;
	@Column(name = "password_hash",  length = 255, nullable = false)
	private String password;
	@Column(name = "phone", length = 20, nullable = false)
	private String phone;
	@Column(name = "currency", length = 3, nullable = false)
	private String currency;
	@Column(name = "creation_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate creation_date;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ExpenseListEntity> expensesList;

}
