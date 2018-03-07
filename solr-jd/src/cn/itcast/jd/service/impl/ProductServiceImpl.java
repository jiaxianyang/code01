package cn.itcast.jd.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import cn.itcast.jd.dao.ProductDao;
import cn.itcast.jd.pojo.PageBean;
import cn.itcast.jd.pojo.Product;
import cn.itcast.jd.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private static int rows = 60;
	
	@Autowired
	private ProductDao productDao;
 
	public PageBean queryProduct(int page,String queryString,String catalog_name,String price,String sort){
		SolrQuery  solrQuery = new SolrQuery();
		
		if(queryString!=null&&!queryString.equals("")){
			solrQuery.setQuery(queryString);//设置主查询条件
		}else{
			solrQuery.setQuery("*:*");//设置主查询条件
		}
		if(catalog_name!=null&&!catalog_name.equals("")){
			solrQuery.set("fq", "product_catalog_name:"+catalog_name);//设置过滤条件
		}
//		String price 0-9  10-19  50-*
		if(price!=null&&!price.equals("")){
			String[] split = price.split("-");
			solrQuery.set("fq", "product_price:["+split[0]+" TO "+split[1]+"]");//设置过滤条件
		}
		
//      "start": "0", page-1
//      "rows": "10",
		solrQuery.setStart((page-1)*rows);//设置起始位置
		solrQuery.setRows(rows);//设置条数
//      "df": "product_keywords",
		solrQuery.set("df",  "product_name");//设置默认查询的域名

//      "hl": "true",
//	      "hl.simple.pre": "<span color="red">",
//      "hl.simple.post": "</span>"
//	      "hl.fl": "product_name",
		solrQuery.setHighlight(true);//开启高亮
		solrQuery.setHighlightSimplePre("<span style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</span>");
		solrQuery.addHighlightField("product_name");

		if(sort.equals("1")){
			solrQuery.setSort("product_price", ORDER.asc);//设置排序
		}else{
			solrQuery.setSort("product_price", ORDER.desc);//设置排序
		}
		PageBean pageBean = productDao.queryProduct(solrQuery);
		pageBean.setCurPage(page);
		
		Long pageCount = pageBean.getRecordCount()/rows;  //计算总页数   总页数=总数/每页显示条数
		if(pageBean.getRecordCount()%rows>0){         //判断是否有余数，如果有余数 总页数需要加1
			pageCount++;
		}
		pageBean.setPageCount(pageCount.intValue());
		
	   return  pageBean;
	}
}
