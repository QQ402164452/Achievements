package interfaces;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by Jason on 2016/12/10.
 */

public interface ImultiContact  extends Ibase {
    void setAdapterData(List<AVUser> list);
}
