package cz.wa2.poll.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nell on 10.4.2015.
 */
public class EntitiesList<T> {

    List<T> entities = new ArrayList<T>();
    Integer totalSize = 0;

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
