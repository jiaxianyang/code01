package cn.itcast.jd.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.itcast.jd.dao.ProductDao;
import cn.itcast.jd.pojo.PageBean;
import cn.itcast.jd.pojo.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
	
	@Autowired
	private SolrServer solrServer;

	public PageBean queryProduct(SolrQuery solrQuery) {
		PageBean pageBean = new PageBean();
		QueryResponse queryResponse = null;
		try {
			queryResponse = solrServer.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		SolrDocumentList results = queryResponse.getResults();
		pageBean.setRecordCount(results.getNumFound());
		
		List<Product> list = new ArrayList<Product>();
		
		for (SolrDocument solrDocument : results) {
			Product product = new Product();
			product.setPid((String) solrDocument.get("id"));
			Map<String, List<String>> map = highlighting.get((String) solrDocument.get("id"));
			List<String> list2 = map.get("product_name");
			
			String product_name ="";
			
			if(list2!=null && list2.size()>0){
				product_name = list2.get(0);
			}else{
				product_name = (String) solrDocument.get("product_name");
			}
			
			product.setDescription(null);
			product.setName(product_name);
			product.setPicture((String) solrDocument.get("product_picture"));
			product.setPrice((float) solrDocument.get("product_price"));
			list.add(product);
		}
		pageBean.setProductList(list);
		
		return pageBean;
	}
}
