/**
 *
 */
package com.toy.component.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toy.component.user.domain.AddressCluster;
import com.toy.component.user.domain.UserVo;

/**
 * @author roger
 */
@Service
public interface IUserService {
    UserVo getUserById(int userId);

    UserVo getOrderById(int id);

    List<UserVo> getAllOrder();

    List<AddressCluster> parseAddressClusterOneByOne(List<UserVo> userVoList);

    List<AddressCluster> parseAddressClusterQuick(List<UserVo> userVoList);

    List<AddressCluster> parseAddressCluster();

    boolean isSimilarAddress(String address1, String address2);

    void saveCluster(List<AddressCluster> clusters);
}
