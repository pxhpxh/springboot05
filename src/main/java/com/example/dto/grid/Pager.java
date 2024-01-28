package com.example.dto.grid;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Pager<T> implements Serializable, IPage<T> {
    private static final long serialVersionUID = 8104292527930673951L;
    //返回的数据
    private List<T> pageData;
    //过滤条件对象列表
    private List<Filter> filters;
    //排序列表
    private List<SortPair> sorts;
    //分页对象列表
    private FlipInfo flipInfo=new FlipInfo(1,20);

    @JSONField(serialize = false)
    @Override
    public List<OrderItem> orders() {
        if(sorts!=null){
            List<OrderItem> orders=new ArrayList<>();
            sorts.forEach(sortPair -> {
                OrderItem item=new OrderItem();
                item.setColumn(sortPair.getSortField());
                item.setAsc(sortPair.getOrder()==SortPair.Order.ASC);
                orders.add(item);
            });
            return orders;
        }
        return null;
    }

    @JSONField(serialize = false)
    public void orderMapping(String srcFieldName, String targetFieldName){
        if(sorts!=null){
            for (int i=0;i<sorts.size();i++) {
                if(sorts.get(i).getSortField().equals(srcFieldName)){
                    sorts.get(i).setSortField(targetFieldName);
                    break;
                }
            }
        }
    }

    @JSONField(serialize = false)
    @Override
    public List<T> getRecords() {
        return pageData;
    }

    @JSONField(serialize = false)
    @Override
    public IPage<T> setRecords(List<T> records) {
        pageData=records;
        return this;
    }

    @JSONField(serialize = false)
    @Override
    public long getTotal() {
        return flipInfo.getTotal();
    }

    @JSONField(serialize = false)
    @Override
    public IPage<T> setTotal(long total) {
        flipInfo.setTotal(Integer.parseInt(""+total));
        return this;
    }

    @JSONField(serialize = false)
    @Override
    public long getSize() {
        return flipInfo.getSize();
    }

    @JSONField(serialize = false)
    @Override
    public IPage<T> setSize(long size) {
        flipInfo.setSize(Integer.parseInt(""+size));
        return this;
    }

    @JSONField(serialize = false)
    @Override
    public long getCurrent() {
        return flipInfo.getPage();
    }

    @JSONField(serialize = false)
    @Override
    public IPage<T> setCurrent(long current) {
        flipInfo.setPage(Integer.parseInt(""+current));
        return this;
    }
}
