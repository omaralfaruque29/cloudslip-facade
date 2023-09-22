package com.cloudslip.facade.repository;


import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(final String userName);

    @Query(value = "{ 'userInfo.companyId' : ?0 }")
    Page<User> findAllByUserInfoCompanyId(Pageable pageable, ObjectId companyId);

    @Query(value = "{ 'userInfo.companyId' : ?0 }")
    List<User> findAllByUserInfoCompanyId(ObjectId companyId);

    @Query(value = "{ 'userInfo.companyId' : ?0, 'userInfo.teams._id' : ?1 }")
    List<User> findAllByUserInfoCompanyIdAndUserInfoTeamsId(ObjectId companyId, ObjectId teamId);

    @Query(value = "{ 'userInfo.companyId' : ?0, 'userInfo.teams._id' : ?1 }")
    Page<User> findAllByUserInfoCompanyIdAndUserInfoTeamsId(Pageable pageable, ObjectId companyId, ObjectId teamId);

    @Query(value = "{ 'userInfo.teams._id' : ?0 }")
    List<User> findAllByUserInfoTeamsId(ObjectId teamId);

    @Query(value = "{ 'userInfo.teams._id' : ?0 }")
    Page<User> findAllByUserInfoTeamsId(Pageable pageable, ObjectId teamId);

    Optional<User> findByUsernameIgnoreCase(String username);

    @Query(value = "{ 'userInfo.companyId' : ?0 , 'authorities._' : ?1, 'userType._' : ?2}")
    List<User> findAllByUserInfoCompanyObjectIdAndAuthoritiesAuthorityAndUserType(ObjectId companyId, Authority authority, UserType userType);

    @Query("{'$or':[ {'userInfo._id' : {$in: ?0} }, {'userInfo.companyId' : ?1 , 'authorities._' : ?2}, {'authorities._' : ?3} ] }")
    List<User> findAllByUserInfoIdInOrUserInfoCompanyObjectIdAndAuthoritiesAuthorityOrAuthoritiesAuthority(List<ObjectId> userInfoIdList, ObjectId companyId, Authority adminAuthority, Authority superAdminAuthority);
}