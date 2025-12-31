package com.lms.www.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "url_id")
    private Long urlId;

    private Long pinCode;
    private String district;
    private String mandal;
    private String city;
    private String village;

    @Column(name = "d_no")
    private Long doorNo;
}
