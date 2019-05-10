package com.comeon.android.business_logic;

import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db_accessing.UserInfoDao;
import com.comeon.android.db_accessing.UserInfoDaoImpl;
import com.comeon.android.db_accessing.UserLoginDao;
import com.comeon.android.db_accessing.UserLoginDaoImpl;

/**
 * 用户相关业务接口实现类
 */
public class UserBusiness implements UserBusinessInterface {
    private static final String TAG = "UserBusiness";
    
    private UserLoginDao userLoginDao=new UserLoginDaoImpl();
    private UserInfoDao userInfoDao=new UserInfoDaoImpl();
    
    @Override
    public UserInfo login(UserLogin loginUser) {
        long loginId=userLoginDao.selectUser(loginUser);
        if(loginId!=-1){
            UserInfo loginedUser=userInfoDao.selectUserByLoginId(loginId);
            if(loginUser!=null){
                return loginedUser;
            }
        }
        return null;
    }
}
