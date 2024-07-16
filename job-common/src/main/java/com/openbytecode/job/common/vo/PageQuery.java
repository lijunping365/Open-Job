/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.job.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * The class Base query.
 *
 * @author li
 */
@Data
public class PageQuery implements Serializable {

  private static final long serialVersionUID = 3319698607712846427L;

  /**
   * 当前页
   */
  private Integer current = 1;

  /**
   * 每页条数
   */
  private Integer pageSize = 10;

  /**
   * 排序
   */
  private String orderBy;

  /**
   * 获取分页对象
   *
   * @return {@link Page<E>}
   */
  public <E> Page<E> page() {
    return new Page<>(this.current, this.pageSize);
  }

}
