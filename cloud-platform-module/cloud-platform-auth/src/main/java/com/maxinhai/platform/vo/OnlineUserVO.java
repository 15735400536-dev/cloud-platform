package com.maxinhai.platform.vo;

import com.maxinhai.platform.bo.OnlineUserBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "在线用户VO")
public class OnlineUserVO {

    @ApiModelProperty(value = "账户")
    private int onlineQty;
    @ApiModelProperty(value = "在线用户集合")
    private List<OnlineUserBO> userList;

    /**
     * 添加在线用户
     * @param onlineUserBO 在线用户信息
     */
    public void addUser(OnlineUserBO onlineUserBO) {
        if(CollectionUtils.isEmpty(userList)) {
            userList = new ArrayList<>();
        }
        userList.add(onlineUserBO);
    }

}
