package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.Category;
import com.comeon.android.db.SportsType;
import com.comeon.android.fragment.SettingPasswordFragment;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Validator;

/**
 * 运动类型数据表数据提取层
 */
public class SportsTypeDaoImpl extends BaseDao implements SportsTypeDao {

    private static final String TAG = "SportsTypeDaoImpl";
    
    /**
     * 根据运动名称返回数据类型对象
     * @param name 运动类型名称
     * @return 运动类型对象
     */
    public SportsType findSportsTypeByName(String name) {
        List<SportsType> sportsTypes=LitePal.where("typename = ?",name).find(SportsType.class);
        SportsType sportsType = sportsTypes.get(0);
        LogUtil.d(TAG, "进行了几次");
        try {
            Cursor cursor= LitePal.findBySQL("select * from SportsType where typename = ?",name);
            while(cursor.moveToNext()){
                long categoryId=cursor.getLong(cursor.getColumnIndex("category_id"));
                Category category=LitePal.find(Category.class, categoryId);
                sportsType.setTypeName(cursor.getString(cursor.getColumnIndex("typename")));
                sportsType.setCategory(category);
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return sportsType;
    }

    @Override
    public List<SportsType> getSportsTypesByCategoryId(long categoryId) {
        Category category=LitePal.find(Category.class, categoryId);

        List<SportsType> sportsTypes= LitePal.where("category_id = ?",String.valueOf(categoryId)).find(SportsType.class);
        for (int i=0; i<sportsTypes.size(); i++){
            sportsTypes.get(i).setCategory(category);
        }
        return sportsTypes;
    }
}
