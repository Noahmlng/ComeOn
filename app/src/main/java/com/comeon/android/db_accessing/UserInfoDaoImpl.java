package com.comeon.android.db_accessing;

import com.comeon.android.R;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;

import java.util.Date;
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

    @Override
    public UserInfo insertNewUser(long loginId, String phone) {
        UserInfo newUser=new UserInfo();
        newUser.setUserLoginId(loginId);
        newUser.setUserPhone(phone);
        newUser.setRegisterTime(new Date());
        newUser.setHeadIcon(Utilities.decodeResource(R.drawable.init_portrait));
        newUser.setUserBirthday(new Date(2000,11,15));
        newUser.setAcceptedDistance(2000f);
        newUser.setUserNickName("小明");
        newUser.setUserGender(0);
        newUser.setDescription("这个人很无聊诶，什么都不说！");
        return newUser;
    }
}
