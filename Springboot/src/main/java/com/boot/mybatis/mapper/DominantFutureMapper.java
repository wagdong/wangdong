package com.boot.mybatis.mapper;


import com.boot.mybatis.domian.DominantFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by fff on 2017/8/16.
 */
public interface DominantFutureMapper  {

    List<DominantFuture> findAll();

    DominantFuture findByCode(@Param("code") String code);


}
