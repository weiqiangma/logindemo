package com.example.logindemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.logindemo.dto.request.ItemRequest;
import com.example.logindemo.entity.Item;
import com.example.logindemo.mapper.ItemMapper;
import com.example.logindemo.repository.EsItemRepository;
import com.example.logindemo.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>  implements ItemService{

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private EsItemRepository itemRepository;

    /**
     * 将所有商品导入es
     * @return
     */
    @Override
    public int importAll() {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        List<Item> itemList = itemMapper.selectList(queryWrapper);
        Iterable<Item> esItemIterable = itemRepository.saveAll(itemList);
        Iterator<Item> iterator =esItemIterable.iterator();
        int result = 0;
        while(iterator.hasNext()){
            result++;
            iterator.next();
        }
         return result;
    }

    /**
     * 根据id在es查找
     * @param id
     * @return
     */
    @Override
    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    /**
     * 根据id在es删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }

    /**
     * 根据id添加至es
     * @param id
     * @return
     */
    @Override
    public Item create(Integer id) {
        Item item = itemMapper.selectById(id);
        if(item != null){
            itemRepository.save(item);
        }
        return item;
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            List<Item> itemList = new ArrayList<>();
            for(Integer id : ids){
                Item item = new Item();
                item.setId(id);
                itemList.add(item);
            }
            itemRepository.deleteAll(itemList);
        }
    }


    @Override
    public Page<Item> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return itemRepository.findByTitleOrPrice(keyword,keyword,pageable);
    }

    /**
     * 获取所有
     * @return
     */
    @Override
    public Page<Item> findAllItem(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        return items;
    }

    @Override
    public Page<Item> findByTitle(String title, Pageable pageable) {
        Page<Item> page = itemRepository.findByTitleContaining(title, pageable);
        return page;
    }
}
