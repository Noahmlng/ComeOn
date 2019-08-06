package com.comeon.android.db_accessing;

import com.comeon.android.db.UserLogin;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

/**
 * 用户登录信息数据表接口实现类
 */
public class UserLoginDaoImpl extends BaseDao implements UserLoginDao {
    private static final String TAG = "UserLoginDaoImpl";

    @Override
    public long insertNewUser(UserLogin newUser) {
        newUser.setLastModifiedTime(new Date());
        newUser.setUserStatus(0);
        newUser.save();
        LogUtil.d(TAG, "新用户的登录id为"+newUser.getId());
        return newUser.getId();
    }

    @Override
    public long selectUser(UserLogin loginUser) {
        LogUtil.d(TAG, "即将要验证的手机号为："+loginUser.getUserPhone()+"；密码为："+loginUser.getUserPassword());
        List<UserLogin> loginUsers=LitePal.select("id").where("userPhone = ? and userPassword = ? and userStatus = 0",loginUser.getUserPhone(),loginUser.getUserPassword()).find(UserLogin.class);
        long id=-1;
        if(loginUsers.size()!=0){
            id=loginUsers.get(0).getId();
            LogUtil.d(TAG, "登录成功用户的用户id为："+id);
        }
        return id;
    }

    @Override
    public int checkExistOnPhone(UserLogin registerUser) {
        int count=LitePal.where("userPhone = ?",registerUser.getUserPhone()).count(UserLogin.class);
        LogUtil.d(TAG, "此号码的用户数为："+count);
        return count;
    }

}
