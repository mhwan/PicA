package com.picaproject.pica.Item;

import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

public class IndexedImageResultItem extends ImageResultItem {
    private int arrayIndex;

    public IndexedImageResultItem(){
        super();
        setArrayIndex(0);
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }
}
