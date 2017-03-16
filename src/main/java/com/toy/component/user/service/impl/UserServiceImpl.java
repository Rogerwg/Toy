package com.toy.component.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.toy.component.user.domain.AddressCluster;
import com.toy.component.user.domain.Socre;
import com.toy.component.user.domain.UserVo;
import com.toy.component.user.mapper.IUserMapper;
import com.toy.component.user.service.IUserService;
import com.toy.tools.StringSimilar;

import lombok.extern.slf4j.Slf4j;

@Service("userService")
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public UserVo getUserById(int userId) {
        return this.userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public UserVo getOrderById(int id) {
        UserVo order = userMapper.getOrderByPrimaryKey(id);
        return order;
    }

    @Override
    public List<UserVo> getAllOrder() {
        List<UserVo> orderVoList = userMapper.getAllOrders();
        return orderVoList;
    }

    @Override
    public List<AddressCluster> parseAddressClusterOneByOne(List<UserVo> userVoList) {
        List<Socre> socreList = new ArrayList<>();
        Set<String> used = new HashSet<>();
        for (UserVo user1 : userVoList) {
            for (UserVo user2 : userVoList) {
                if (user1.getId() >= user2.getId()) {
                    continue;
                }
                if (used.contains(user1.getId() + "_" + user2.getId())) {
                    continue;
                }
                used.add(user1.getId() + "_" + user2.getId());
                if (isSimilarAddress(user1.getAddress(), user2.getAddress())) {
                    int size = socreList.size() + 1;
                    socreList.add(new Socre(user1.getId(), user2.getId(), true, size));
                    log.info("find similar");
                }
            }
        }
        return generateClusterFromScore(socreList);
    }

    private List<AddressCluster> generateClusterFromScore(List<Socre> socreList) {
        List<AddressCluster> result = new ArrayList<>();
        for (Socre item : socreList) {
            boolean find = false;
            if (CollectionUtils.isEmpty(result)) {
                AddressCluster cluster = new AddressCluster();
                Set<Integer> members = new HashSet<>();
                members.add(item.getIdA());
                members.add(item.getIdB());
                cluster.setMembers(members);
                result.add(cluster);
                continue;
            }
            for (AddressCluster ac : result) {
                Set<Integer> newM = ac.getMembers();
                if (newM.contains(item.getIdA()) || newM.contains(item.getIdB())) {
                    newM.add(item.getIdA());
                    newM.add(item.getIdB());
                    ac.setMembers(newM);
                    find = true;
                    break;
                }
            }
            if (find) {
                continue;
            } else {
                AddressCluster cluster = new AddressCluster();
                Set<Integer> members = new HashSet<>();
                members.add(item.getIdA());
                members.add(item.getIdB());
                cluster.setMembers(members);
                result.add(cluster);
            }
        }
        return result;
    }


    @Override
    public List<AddressCluster> parseAddressClusterQuick(List<UserVo> userVoList) {
        List<AddressCluster> result = new ArrayList<>();
        for (UserVo user : userVoList) {
            if (CollectionUtils.isEmpty(result)) {
                AddressCluster cluster = new AddressCluster();
                Set<Integer> members = new HashSet<>();
                members.add(user.getId());
                cluster.setMembers(members);
                result.add(cluster);
            }


        }
        return null;
    }


    @Override
    public boolean isSimilarAddress(String address1, String address2) {
        String cleanAddress1 = address1.replaceAll(";", "").replaceAll("0", "");
        String cleanAddress2 = address2.replaceAll(";", "").replaceAll("0", "");
        return StringSimilar.isSimilarByED(cleanAddress1, cleanAddress2);
    }

    @Override
    public void saveCluster(List<AddressCluster> clusters) {
        if (CollectionUtils.isEmpty(clusters))
            return;
        int i = 1;
        for (AddressCluster c : clusters) {
            if (c.getMembers().size() > 5) {
                userMapper.insertGroup("group_" + (i++), c.getMembers());
            }
        }
    }

    @Override
    public List<AddressCluster> parseAddressCluster() {
        List<UserVo> userVoList = getAllOrder();
        return parseAddressClusterOneByOne(userVoList);
    }


}
