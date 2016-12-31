package interfaces;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Jason on 2016/12/29.
 */

public interface IsocDetail  extends Ibase {
    void setCommentList(List<AVObject> list);
    void setLikeList(List<AVObject> list);
    void setBottomLike(boolean isSelected);
}
