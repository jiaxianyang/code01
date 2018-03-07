package cn.itcast.jd.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.itcast.jd.pojo.PageBean;

public interface ProductDao {

	PageBean queryProduct(SolrQuery solrQuery);

}
