package interfaces;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Jason on 2016/12/28.
 */

public interface Isocial extends Ibase {
    void setListData(List<AVObject> avObjects,int type);
}
