package com.comeon.android.business_logic;

import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db_accessing.UserInfoDao;
import com.comeon.android.db_accessing.UserInfoDaoImpl;
import com.comeon.android.db_accessing.UserLoginDao;
import com.comeon.android.db_accessing.UserLoginDaoImpl;
import com.comeon.android.db_accessing.UserLoginLogDao;
import com.comeon.android.db_accessing.UserLoginLogDaoImpl;

/**
 * 用户相关业务接口实现类
 */
public class UserBusiness implements UserBusinessInterface {
    private static final String TAG = "UserBusiness";
    
    private UserLoginDao userLoginDao=new UserLoginDaoImpl();
    private UserInfoDao userInfoDao=new UserInfoDaoImpl();
    private UserLoginLogDao userLoginLogDao=new UserLoginLogDaoImpl();
    
    @Override
    public UserInfo login(String phone, String password) {
        UserLogin loginUser=new UserLogin();
        loginUser.setUserPhone(phone);
        loginUser.setUserPassword(password);
        long loginId=userLoginDao.selectUser(loginUser);
        if(loginId!=-1){
            UserInfo loginedUser=userInfoDao.selectUserByLoginId(loginId);
            if(loginUser!=null){
                userLoginLogDao.insertNewLog(loginId);
                return loginedUser;
            }
        }
        return null;
    }

    @Override
    public boolean checkPhoneExist(String phone) {
        UserLogin userLogin=new UserLogin();
        userLogin.setUserPhone(phone);
        int count=userLoginDao.checkExistOnPhone(userLogin);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public UserInfo registration(String phone, String password) {
        UserInfo newUser=null;
        /**
         * 新建登录用户先
         */
        UserLogin registeredUser=new UserLogin();
        registeredUser.setUserPhone(phone);
        registeredUser.setUserPassword(password);
        long loginId=userLoginDao.insertNewUser(registeredUser);
        if(loginId>0){
            newUser=userInfoDao.insertNewUser(loginId,phone);
            //注册之后加第一次登录记录
            userLoginLogDao.insertNewLog(loginId);
        }
        return newUser;
    }


}
