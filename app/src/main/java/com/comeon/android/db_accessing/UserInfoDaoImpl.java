package com.comeon.android.db_accessing;

import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * 用户基本信息数据表接口实现类
 */
public class UserInfoDaoImpl implements UserInfoDao {
    @Override
    public UserInfo selectUserByLoginId(long loginId) {
        List<UserInfo> userInfos= LitePal.where("userLoginId = ?",String.valueOf(loginId)).find(UserInfo.class);
        if(userInfos.size()!=0){
            return userInfos.get(0);
        }
        return null;
    }
}
