package interfaces;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by Jason on 2016/12/3.
 */

public interface IcontactSelect extends Ibase {
    void setAdapterData(List<AVUser> list);
}
