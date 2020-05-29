package com.reason.springsec.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT")
@Data //getter setter tostring 구현
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ACCOUNT_USERNAME")
    private String username = null;

    @Column(name = "ACCOUNT_LOGINID")
    private String userId = "reason";

    @Column(name = "ACCOUNT_PASSWORD")
    private String password = "1234";

    @Column(name = "ACCOUNT_ROLE")
    @Enumerated(value = EnumType.STRING) // ordinal 안쓰도록 주의
    private UserRole userRole = UserRole.USER;//enumtype userRole

    @Column(name = "ACCOUNT_SOCIAL_ID")
    private Long socialId;

    @Column(name = "ACCOUNT_SOCIAL_PROVIDER")
    @Enumerated(value = EnumType.STRING)
    private SocialProviders socialProvider;

    @Column(name = "ACCOUNT_SOCIAL_PROFILEPIC")
    private String profileHref;
}
