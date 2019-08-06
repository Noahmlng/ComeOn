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
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao {
    private static final String TAG = "UserInfoDaoImpl";

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
        newUser.save();
        return newUser;
    }

    @Override
    public List<UserInfo> selectUsersByPhone(String phone) {
        //调用LitePal的查询方法
        List<UserInfo> userInfos=LitePal.where("userPhone like ?",phone+"%").find(UserInfo.class); //模糊查询

        int count=userInfos!=null?userInfos.size():0;//三元运算符计算查询到底行数
        LogUtil.d(TAG,"查询到的数量："+count);

        return userInfos;
    }
}
