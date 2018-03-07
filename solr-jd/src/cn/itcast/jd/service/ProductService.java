package cn.itcast.jd.service;

import cn.itcast.jd.pojo.PageBean;

public interface ProductService {
	public PageBean queryProduct(int page,String queryString,String catalog_name,String price,String sort);
}
