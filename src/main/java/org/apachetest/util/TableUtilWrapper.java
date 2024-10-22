package org.apachetest.util;


public class TableUtilWrapper implements ITableUtilWrapper{

    @Override
    public String wrapperMethod() {
        return TableUtil.getCurrentTS();
    }
}
