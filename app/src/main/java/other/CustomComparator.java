package other;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Jason on 2016/12/10.
 */

public class CustomComparator implements Comparator<Integer>,Serializable{


    @Override
    public int compare(Integer lhs, Integer rhs) {
        if (lhs > rhs) {
            return 1;
        } else if (lhs < rhs) {
            return -1;
        } else {
            return 0;
        }
    }
}
