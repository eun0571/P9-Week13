package com.swjungle.spring.repository

import com.swjungle.spring.entity.Member
import com.swjungle.spring.entity.MemberRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByLoginId(loginId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long> {
    //    @Modifying
//    @Query("delete from MemberRole r where r.member = :member")
    fun deleteAllByMember(member: Member)
}